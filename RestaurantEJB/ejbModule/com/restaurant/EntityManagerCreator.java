package com.restaurant;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

@Stateless
public class EntityManagerCreator {

	
//	@PersistenceUnit
//	private EntityManagerFactory entityManagerFactory;
	
	
	public EntityManager createEntityManager() {
		return null;
		
//		System.out.println("Creating entity manager");
//		
//		
//		Map<String,Object> properties = new HashMap<String,Object>();
//		properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
//		properties.put("javax.persistence.jdbc.username", "root");
//		properties.put("javax.persistence.jdbc.password", "41871201");
//		properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost/restaurant");		
//		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ejbrelationships", properties);
//		return entityManagerFactory.createEntityManager(properties);
		
		
	}
	
}
