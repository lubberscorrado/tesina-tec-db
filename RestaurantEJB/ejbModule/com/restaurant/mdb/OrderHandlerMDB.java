package com.restaurant.mdb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.sql.DataSource;

import com.utils.UserInfo;


@MessageDriven(
		activationConfig = { 
				@ActivationConfigProperty(
						propertyName = "destination", 
						propertyValue = "jms/ordersQueue"
				),
				@ActivationConfigProperty(
							propertyName = "destinationType", 
							propertyValue = "javax.jms.Queue")}
		)



public class OrderHandlerMDB implements MessageListener {
	
//	@Resource(name="java:jboss/datasources/MySqlDS2")
//	private DataSource dataSource;
//	private Connection connection;

	
	
    public OrderHandlerMDB() {
    }
    
    @PostConstruct
    public void initialize() {
//    	
//    	try {
//			connection = dataSource.getConnection();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    
    }
	
    public void onMessage(Message message) {
    	TextMessage tm = (TextMessage) message;
    	try {
			System.out.println("Received message "+ tm.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	/* Recupero l'oggetto associato al messaggio */
//    	ObjectMessage objectMessage = (ObjectMessage) message;
//    	UserInfo ui = null;
//		try {
//			ui = (UserInfo)objectMessage.getObject();
//		} catch (JMSException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//    	try {
//    		    	
//    		Statement st = connection.createStatement();
//    		String query;
//    	
//			st.executeQuery("INSERT INTO film (nome,genere,director,data) " + 
//							" VALUES(\"" + ui.getName() + "\",\"ciao\"," + 
//							"\"hola\",\"buoenosdias\")");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }
    
    
    @PreDestroy
    public void cleanup() {
//    	try {
//    		connection.close();
//    		connection = null;
//    	} catch(Exception e) {
//    		e.printStackTrace();
//    	}
    }

}
