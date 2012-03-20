package com.orb.gestioneOggetti;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.exceptions.DatabaseException;
import com.mail.ThreadMailSender;
import com.orb.Categoria;
import com.orb.Tenant;
import com.orb.TipoCategoriaEnum;
import com.restaurant.TreeNodeCategoria;
import com.restaurant.WrapperTenant;
import com.restaurant.WrapperUtentePersonale;


@SuppressWarnings("unchecked") 
@Stateless
public class GestioneTenant{

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
	
	public GestioneTenant() {}
	
//	/**
//	 * Aggiunge un area per un determinato cliente
//	 * @param idTenant Id del cliente a cui appartiene l'area
//	 * @param nome Nome dell'area
//	 * @param descrizione Descrizione dell'area
//	 * @param enabled Stato dell'area, abilitata o disabilitata
//	 * @param idPiano Id del piano a cui appartiene l'area
//	 * @return Oggetto TreeNodeArea che rappresenta la nuova area 
//	 * inserita
//	 * @throws DatabaseException Eccezione che incapsula le informazioni
//	 * sull'ultimo errore verificatosi
//	 */
	public WrapperTenant aggiungiTenant(	
										//int idTenant,	//Assegnato dal db
										String ragioneSociale,
										String piva,
										String indirizzo,
										int civico,
										int cap,
										String citta,
										String provincia,
										String nazione,
										String telefono,
										String fax,
										String email,
										String url,
										String superadmin,
										String passwd
										) throws DatabaseException {
		try {
			Tenant tenant = new Tenant();
			
			tenant.setRagioneSociale(ragioneSociale);
			tenant.setPiva(piva);
			tenant.setIndirizzo(indirizzo);
			tenant.setCivico(civico);
			tenant.setCap(cap);
			tenant.setCitta(citta);
			tenant.setProvincia(provincia);
			tenant.setNazione(nazione);
			tenant.setTelefono(telefono);
			tenant.setFax(fax);
			tenant.setEmail(email);
			tenant.setUrl(url);
			tenant.setSuperadmin(superadmin);
			tenant.setPasswd(passwd);
			
			
			Query query = em.createQuery("SELECT t FROM Tenant t WHERE t.piva = :piva");
			query.setParameter("piva", tenant.getPiva());
			
			Tenant findTenant;
			try{
				findTenant = (Tenant) query.getSingleResult();
			}catch(Exception e){
				findTenant = null;
			}
			
			if(findTenant != null)
				throw new DatabaseException("Partita IVA già registrata nel sistema.");
			
			
			em.persist(tenant);
			
			WrapperTenant wrapperTenant = new WrapperTenant(tenant);
			
//			String[] sendTo = { email };
//			String emailFromAddress = "YouRestaurant";
//			String emailSubjectTxt = 	"Registrazione al servizio youRestaurant";
//			String emailMsgTxt = 		"Grazie per avere scelto il nostro servizio!\n\n" +
//										"Qui di seguito saranno fornite le tue credenziali di accesso all'area di amministrazione.\n\n" +
//										"Codice ristorante:\t"++"\n" +
//										"Username:\t\t"++"\n" +
//										"Password:\t\t"++"\n\n" +
//										"Mandaci i tuoi feedback per un servizio sempre migliore!";
//			try {
//				SendMail.sendSSLMessage(sendTo, emailSubjectTxt,emailMsgTxt, emailFromAddress);
//			} catch (MessagingException e) {
//				System.out.println("Exception");
//				e.printStackTrace();
//				throw new DatabaseException("ERRORE INVIO MAIL");
//			}
//			System.out.println("Sucessfully Sent mail to All Users");
//			System.out.println("MAIL INVIATA");
			ThreadMailSender newThread = new ThreadMailSender(wrapperTenant.getIdTenant(),wrapperTenant.getSuperadmin(),wrapperTenant.getPasswd(),wrapperTenant.getEmail() );
			newThread.run();
			
			
			//Inizializzo le entità per la gestione del menù
			Categoria categoriaPadre = new Categoria();
				categoriaPadre.setIdTenant(wrapperTenant.getIdTenant());
				categoriaPadre.setNome("Menù");
				categoriaPadre.setCategoriaPadre(null);
				em.persist(categoriaPadre);
			
				
			Categoria categoria = new Categoria();
				categoria.setIdTenant(wrapperTenant.getIdTenant());
				categoria.setNome("Bevande");
				categoria.setCategoriaPadre(categoriaPadre);
				categoria.setTipo(TipoCategoriaEnum.BEVANDA);
				em.persist(categoria);
				
			categoria = new Categoria();
				categoria.setIdTenant(wrapperTenant.getIdTenant());
				categoria.setNome("Cibo");
				categoria.setCategoriaPadre(categoriaPadre);
				categoria.setTipo(TipoCategoriaEnum.CIBO);
				em.persist(categoria);
			
			return new WrapperTenant(tenant);
		
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento del tenant (" + e.toString() +")");
		}
	}
	
	public WrapperTenant getTenantById(int idTenant) throws DatabaseException {
	
		try {
			Tenant tenant = em.find(Tenant.class, idTenant);
			if(tenant == null)
				throw new DatabaseException("Impossibile trovare il tenant");
			
			return new WrapperTenant(tenant);
				
		} catch(Exception e) {
			throw new DatabaseException("Impossibile trovare il tenant" +
										"(" + e.toString()+")");
		}
	}
	
	public WrapperUtentePersonale getWrapperUtentePersonaleByTenantId(int idTenant) throws DatabaseException {
		
		try {
			Tenant tenant = em.find(Tenant.class, idTenant);
			if(tenant == null)
				throw new DatabaseException("Il codice del ristorante non è valido");
			
			WrapperUtentePersonale wrapperUtentePersonale = new WrapperUtentePersonale();
			wrapperUtentePersonale.setSuperAdmin(true);
			wrapperUtentePersonale.setIdUtentePersonale(0);
			wrapperUtentePersonale.setUsername(	tenant.getSuperadmin()	);
			wrapperUtentePersonale.setPassword(	tenant.getPasswd()	);
			
			return wrapperUtentePersonale;
				
		} catch(Exception e) {
			throw new DatabaseException("Impossibile trovare il tenant" +
										"(" + e.toString()+")");
		}
	}

//	/**
//	 * Modifica un'area a partire dal suo id
//	 * @param idArea Id dell'area da modificare
//	 * @param nome Nome dell'area modificata
//	 * @param descrizione Descrizione dell'area modificata
//	 * @param enabled Stato dell'area modificata
//	 * @return Oggetto TreeNodeArea che rappresenta la nuova area modificata
//	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore verificatosi
//	 */
//	 public TreeNodeArea updateArea(int idArea,
//			 						String nome,
//									String descrizione,
//									boolean enabled) throws DatabaseException {
//			
//		try {
//			
//			Area area = em.find(Area.class, idArea);
//			if(area == null)
//				throw new DatabaseException("Errore durante la ricerca dell'area da aggiornare");
//		
//			area.setNome(nome);
//			area.setDescrizione(descrizione);
//			area.setEnabled(enabled);
//			
//			return new TreeNodeArea(area);
//			
//		}catch(Exception e) {
//			throw new DatabaseException("Errore durante la modifica del tavolo (" +e.toString() +")");
//		}
//	 }
//	 
//	 /**
//	  * Elimina un'area a partire dall'id.
//	  * @param idArea id dell'area da eliminare
//	  * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è verificato
//	  */
//	 
//	 public void deleteArea(int idArea) throws DatabaseException {
//		 try {
//			 Area area = em.find(Area.class, idArea);
//			 if(area == null)
//				 throw new DatabaseException("Errore durante la ricerca dell'area da rimuovere");
//			 em.remove(area);
//		 } catch (Exception e) {
//			 throw new DatabaseException("Errore durante l'eliminazione dell'area ("+ e.toString() +")");
//		 }
//	  }
//		
//	/** 
//	 * Ritorna l'elenco di tutte le aree appartenenti ad un cliente 
//	 * @param idTenant Id del cliente
//	 * @return Oggetto TreeNodeArea che rappresenta un area di un cliente
//	 * @throws DatabaseException Eccezione che incapsula le informazioni
//	 * sull'ultimo errore verificatosi
//	 */
//	 
//	public List<TreeNodeArea> getAreeTenant(int idTenant) throws DatabaseException {
//		try {
//			
//			Query query = em.createQuery("SELECT a FROM Area a WHERE a.idTenant = :idTenant");
//			query.setParameter("idTenant", idTenant);
//			List<Area> listaAree = (List<Area>)query.getResultList();
//			List<TreeNodeArea> listaTreeNodeArea = new ArrayList<TreeNodeArea>();
//			
//			for(Area area : listaAree) 
//				listaTreeNodeArea.add(new TreeNodeArea(area));
//					
//			return listaTreeNodeArea;
//					
//		}catch(Exception e) {
//			throw new DatabaseException("Errore durante la ricerca delle aree (" + e.toString() +")");
//		}
//	}
//	
//	
//	/** 
//	 * Ritorna la lista delle aree associate ad un determinato piano
//	 * @param idPiano id del piano del quale si vuole ottenere la lista delle aree
//	 * @return Lista di oggetti TreeNodeArea che incapsulano di dati di un area
//	 * @throws DatabaseException Eccezione di errore durante l'accesso al database
//	 */
//	
//	// TODO Forzare il fetch con una query FETCH per ottenere le aree di un piano in un' unica query?
//	
//	public List<TreeNodeArea> getAreeByPiano(int idPiano) throws DatabaseException {
//	
//		try {
//			
//			Piano piano = em.find(Piano.class, idPiano);
//			if(piano == null)
//				throw new DatabaseException("Impossibile trovare il piano");
//			
//			/* TODO Impostare l'associazione con piano di ogni area come LAZY per evitare
//			il fetch di oggetti inutili? */
//			List<Area> listaAree = piano.getAree();
//			List<TreeNodeArea> listaTreeNodeArea = new ArrayList<TreeNodeArea>();
//			
//			for(Area area : listaAree) 
//				listaTreeNodeArea.add(new TreeNodeArea(area));
//				
//			return listaTreeNodeArea;
//				
//		} catch(Exception e) {
//			throw new DatabaseException("Impossibile ottenere le aree associate al piano " +
//										"(" + e.toString()+")");
//		}
//	}
}