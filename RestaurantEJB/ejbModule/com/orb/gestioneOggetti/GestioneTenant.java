package com.orb.gestioneOggetti;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
}