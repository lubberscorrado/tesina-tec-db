package Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.util.privilegedactions.GetConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import Utilita.JSONResponse;

import com.business.BusinessComande;
import com.business.BusinessTavolo;
import com.exceptions.DatabaseException;
import com.orb.StatoComandaEnum;
import com.orb.gestioneOggetti.GestioneUtentePersonale;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperComandaCucina;
import com.restaurant.WrapperUtentePersonale;
import com.restaurant.WrapperVariazione;


@WebServlet("/gestioneComande")
public class gestioneComande extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	@EJB	
	private BusinessTavolo businessTavolo;
	@EJB
	private BusinessComande businessComande;
    @EJB
    private GestioneUtentePersonale gestioneUtentePersonale;
    
	public gestioneComande() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if( !JSONResponse.UserAccessControlAnyPrivs(request, response, new int[]{JSONResponse.PRIV_Cassiere,JSONResponse.PRIV_Cameriere,JSONResponse.PRIV_Cuoco}) ){
    		return;
    	}
		
//		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cameriere) || 
//			(Integer) request.getSession().getAttribute("idTenant") == null || 
//			(Integer)request.getSession().getAttribute("idUtente") == null) {
//			return;
//		}
		
		int idTenant = (Integer) request.getSession().getAttribute("idTenant");
			
		WrapperUtentePersonale utentePersonale;
		
		try {
			utentePersonale = gestioneUtentePersonale.getUtentePersonaleById(	(Integer)request
																				.getSession()
																				.getAttribute("idUtente"));
		} catch (Exception e) {
			JSONResponse.WriteOutput(response, false, e.toString());
			return;
		}
		
		if( request.getParameter("action") != null &&
			request.getParameter("action").equals("OCCUPA_TAVOLO")) {
			
			/* ****************************************************************************
			 * Viene occupato il tavolo e aperto il conto associato. Se l'id del tavolo
			 * è nullo ritorna un'eccezione perchè non è in grado di trovare il tavolo
			 * associato.
			 *****************************************************************************/
			try {
				int idTavolo = 0;
				if(request.getParameter("idTavolo")!= null)
					idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
			
				int numeroPersone = 0;
				if(request.getParameter("numeroPersone") != null) 
					numeroPersone = Integer.parseInt(request.getParameter("numeroPersone"));
				
				businessTavolo.occupaTavolo(idTavolo, 
											idTenant, 
											utentePersonale.getIdUtentePersonale(), 
											numeroPersone);
			
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("cameriere", utentePersonale.getNome() + " " +
											utentePersonale.getCognome());
				jsonObject.put("success", true);
			
				response.getWriter().print(jsonObject);
				
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
		
		} else if(	request.getParameter("action") != null &&
					request.getParameter("action").equals("LIBERA_TAVOLO")) {
			
			/* ****************************************************************************
			 * Viene liberato il tavolo e settato il conto come  DAPAGARE. Se il
			 * tavolo non è settato ritorna un'eccezione poichè non è in grado di 
			 * trovare un tavolo con id 0
			 *****************************************************************************/
			
			try {
				
				int idTavolo = 0;
				if(request.getParameter("idTavolo")!= null)
					idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
				
				businessTavolo.liberaTavolo(idTavolo);
				
				JSONResponse.WriteOutput(response, true, "");
			
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if(request.getParameter("action").equals("PULISCI_TAVOLO")) {
			
			/* ************************************************************************
			 * Effettua il passaggio di stato di un tavolo da PULIRE a LIBERO
			 **************************************************************************/
			 
			try {
				int idTavolo = 0;
				if(request.getParameter("idTavolo")!= null)
					idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
					
				businessTavolo.pulisciTavolo(idTavolo);
				
				JSONResponse.WriteOutput(response, true, "");
			
			} catch(Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if(	request.getParameter("action") != null &&
					request.getParameter("action").equals("INSERISCI_COMANDE")){
			
			/* ****************************************************************************
			 * Riceve un gruppo di ordinazioni destinate ad un determinato tavolo.
			 * Decodifica la richiesta e passa alla logica di business la lista
			 * delle comande.
			 *****************************************************************************/
			
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			/* Lista delle comande estratte dalla richiesta */
			List<WrapperComanda> listaComande = new ArrayList<WrapperComanda>();
			
			try {
				reader.read(httpBody, 0, length);
				JSONObject jsonObjectOrdinazioni = new JSONObject(new String(httpBody));
				JSONArray jsonArrayOrdinazioni = jsonObjectOrdinazioni.getJSONArray("comande");
				
				
				
				int idTavolo = jsonObjectOrdinazioni.getInt("idTavolo");
				
				for(int i=0; i<jsonArrayOrdinazioni.length(); i++) {
					
					JSONObject jsonObjectOrdinazione = jsonArrayOrdinazioni.getJSONObject(i);
				
					/* Estraggo gli id di tutte le variazioni richieste per l'ordinazione
					 * e le associto alla comanda da passare alla logica di business */
					
					JSONArray jsonArrayVariazioni = jsonObjectOrdinazione.getJSONArray("variazioni");
					
					WrapperComanda wrapperComanda = new WrapperComanda();
					
					for(int j=0; j<jsonArrayVariazioni.length(); j++) {
						wrapperComanda.addVariazione	(jsonArrayVariazioni
														.getJSONObject(j)
														.getInt("idVariazione"));
					}
				
					wrapperComanda.setIdTenant((Integer)request.getSession().getAttribute("idTenant"));
					wrapperComanda.setNote(jsonObjectOrdinazione.getString("note"));
					wrapperComanda.setQuantita(jsonObjectOrdinazione.getInt("quantita"));
					wrapperComanda.setIdVoceMenu(jsonObjectOrdinazione.getInt("idVoceMenu"));
					wrapperComanda.setStato(StatoComandaEnum.INVIATA);
					
					listaComande.add(wrapperComanda);
				}
				
				int idUtente = (Integer)request.getSession().getAttribute("idUtente");
				List<Integer> listaIdRemoti = businessComande.inserisciComande(	listaComande, 
																				idTavolo, 
																				idUtente);
					
					
				/* Array JSON che contiene gli id associati alle comande sul
				 * database. Vengono ritornati al client in modo che sucessive 
				 * operazioni di modifica possano essere fatte con riferimento 
				 * a quegli id */
					
					
				JSONArray jsonArrayIdComande = new JSONArray();
				for(int j=0; j< listaIdRemoti.size(); j++) {
					JSONObject jsonObjectId = new JSONObject();
					jsonObjectId.put("id", listaIdRemoti.get(j));
					jsonArrayIdComande.put(jsonObjectId);
				}
			
				JSONResponse.WriteOutput(response, true, "","idComande", jsonArrayIdComande);
				
				
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if(	request.getParameter("action") != null &&
					request.getParameter("action").equals("MODIFICA_COMANDA")) {
			
			/* ****************************************************************************
			 * Decodifica la richiesta di modifica di una comanda e la passa alla logica
			 * di business.
			 *****************************************************************************/
		
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			try {
				reader.read(httpBody, 0, length);
				JSONObject jsonObjectComanda = new JSONObject(new String(httpBody));
				
				WrapperComanda wrapperComanda = new WrapperComanda();
				
				wrapperComanda.setIdComanda(jsonObjectComanda.getInt("idRemotoComanda"));
				wrapperComanda.setNote(jsonObjectComanda.getString("note"));
				wrapperComanda.setQuantita(jsonObjectComanda.getInt("quantita"));
				
				/* *************************************************************************
				 * Associo le variazioni richieste alla comanda
				 ***************************************************************************/
				JSONArray jsonArrayVariazioni = jsonObjectComanda.getJSONArray("variazioni");
				
				for(int i=0; i<jsonArrayVariazioni.length(); i++) 
					wrapperComanda.getListIdVariazioni().add(new Integer(	jsonArrayVariazioni
																			.getJSONObject(i)
																			.getInt("idVariazione")));
				
				
				businessComande.modificaComanda(wrapperComanda);
				JSONResponse.WriteOutput(response, true, "Comanda modificata");
			
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if (	request.getParameter("action") != null &&
					request.getParameter("action").equals("ELIMINA_COMANDA")) {
			
			/* ****************************************************************************
			 * Acquisisce l'id della comanda da eliminare e passa la richiesta alla logica
			 * di business
			 *****************************************************************************/
			
			try {
				int idRemotoComanda = 0;
				if(request.getParameter("idRemotoComanda") != null)
					idRemotoComanda = new Integer(request.getParameter("idRemotoComanda"));
				
				businessComande.eliminaComanda(idRemotoComanda);
				
				JSONResponse.WriteOutput(response, true, "Comanda eliminata");
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if(request.getParameter("action").equals("ELENCO_COMANDE")) {
			
			System.out.println("Ho ricevuto una richiesta!");
			
			/* ****************************************************************************
			 * Acquisisce il tipo di comande da cercare (CIBO o BEVANDA) e 
			 * lo passa alla logica di business
			 * *****************************************************************************/
			
			try {
				String tipo = request.getParameter("tipoComande");
				
				/* Nota: passo anche l'idTenant per filtrare i risultati letti */
				List<WrapperComandaCucina> listaComandeCucina =  businessComande.getElencoComandeByType(idTenant, tipo);
				
				System.out.println("POFFERBACCO!!! "+listaComandeCucina.size()+" IdTenant:"+idTenant+" Tipo: "+tipo);
				JSONObject jsonObject = new JSONObject();
				
				JSONArray jsonArrayComande = new JSONArray();
				
				for(int i=0; i<listaComandeCucina.size(); ++i) {
					JSONObject tempObject = new JSONObject();
					
					/* Parametri della comanda letta */
					tempObject.put("idComanda", listaComandeCucina.get(i).getIdComanda());
					tempObject.put("nomeVoceMenu", listaComandeCucina.get(i).getNomeVoceMenu());
					tempObject.put("nomeCategoria", listaComandeCucina.get(i).getNomeCategoria());
					tempObject.put("quantita", listaComandeCucina.get(i).getQuantita());
					tempObject.put("statoComanda", listaComandeCucina.get(i).getStato());
					tempObject.put("nomeTavolo", listaComandeCucina.get(i).getNomeTavolo());
					
					if(listaComandeCucina.get(i).getNote().equals("") || listaComandeCucina.get(i).getNote() == null) 
						tempObject.put("noteComanda", "");
					else
						tempObject.put("noteComanda", listaComandeCucina.get(i).getNote());
					
					/** Elenco variazioni */
					JSONArray tempArray_2 = new JSONArray();
					List<WrapperVariazione> listaVariazioni = listaComandeCucina.get(i).getListVariazioni();
					
					for(int j=0; j<listaVariazioni.size(); j++) {
						JSONObject tempObject_2 = new JSONObject();
						tempObject_2.put("idVariazione", listaVariazioni.get(j).getIdVariazione());
						tempObject_2.put("nomeVariazione", listaVariazioni.get(j).getNome());
						tempObject_2.put("prezzoVariazione", listaVariazioni.get(j).getPrezzoVariazione());
						tempArray_2.put(tempObject_2);
					}
					
					tempObject.put("elencoVariazioni", tempArray_2);
					/** Fine reperimento Elenco Variazioni */
					
					
					/* Metto tutto in un array */
					jsonArrayComande.put(tempObject);
				}
				
				jsonObject.put("elencoComande", jsonArrayComande);
				jsonObject.put("success", true);
				
				response.getWriter().print(jsonObject);
				
//				JSONResponse.WriteOutput(response, true, "Elenco comande reperito");
				
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
			
		}  else if(	request.getParameter("action") != null &&
					request.getParameter("action").equals("UPDATE_STATO")) {
			
			/* Modfica lo stato di una comanda senza necessariamente 
			 * associare ad una comanda l'id di una cucina */
			
			int idComanda = 0;
			if(request.getParameter("idComanda") != null)
				idComanda = Integer.parseInt(request.getParameter("idComanda"));
			
			int idCucina = utentePersonale.getIdUtentePersonale();
			
			String stato = "";
			if(request.getParameter("stato")!= null) 
				stato = request.getParameter("stato");
			
			try {
				businessComande.modificaStatoComanda(idComanda, idCucina, stato);
				JSONResponse.WriteOutput(response, true, "");
				
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, true, e.toString());
			}
			
		}
	}
	

}
