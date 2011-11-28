package com.restaurant;

import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@Stateful
public class TenantSession {

	private EntityManager em;
	
	
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public EntityManager getEntityManager() {
		
		return em;
	}
	
	public void initializeDbConnection(String username, String password) {
				
	}
	
	
	@Remove
	public void destroySession() {
		em.close();
	}
	
	
	@PreDestroy
	public void cleanUp() {
		System.out.println("Destroying stateful session bean");
		em.close();
	}
	
}
