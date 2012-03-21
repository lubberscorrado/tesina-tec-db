package com.orb.gestioneOggetti;

import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mail.ThreadMailSender;
import com.orb.StatoContoEnum;
import com.orb.Tenant;

@Stateless
public class ScheduledBusinessProcess {

	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ScheduledBusinessProcess() {
        // TODO Auto-generated constructor stub
    }
	
	@SuppressWarnings("unused")
	@Schedule(minute="*/15", hour="*", dayOfWeek="*", dayOfMonth="*", month="*", year="*", info="MyTimer")
    private void scheduledTimeout(final Timer t) {
        //System.out.println("@Esecuzione fatturazioni: called at: " + new java.util.Date());
        Query query = em.createQuery("SELECT t FROM Tenant t");
        List<Tenant> listTenant = query.getResultList();
        float canoneMensile = 15.00f;
        float costoConto = 0.05f;
        
        for(Tenant tenant : listTenant){
        	query = em.createQuery("SELECT Count(*) FROM Conto c WHERE c.idTenant = :idTenant");
        	query.setParameter("idTenant", tenant.getIdTenant());
        	long numeroConti = (Long) query.getSingleResult();
        	int idTenant = tenant.getIdTenant();
        	
        	
        	try {
				new ThreadMailSender(tenant.getRagioneSociale(), numeroConti, canoneMensile, costoConto, tenant.getEmail()).run();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//System.out.println("Tenant:"+idTenant+" N_Conti:"+numeroConti);
        	
        }
        
    }
	

	@SuppressWarnings("unused")
	@Schedule(minute="*/5", hour="*", dayOfWeek="*", dayOfMonth="*", month="*", year="*", info="removeExpiredSession")
    private void removeExpiredSession(final Timer t) {
        //System.out.println("@Cancellazione vecchie sessioni: called at: " + new java.util.Date());
        Query query = em.createQuery("DELETE FROM StatoUtentePersonale s WHERE (now()-s.loginTime) > 1800");
        query.executeUpdate();
    }
}
