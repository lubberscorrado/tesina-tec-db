//package com.restaurant;
//
//import javax.jms.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.annotation.Resource;
//import javax.ejb.Stateful;
//import javax.ejb.Stateless;
//import javax.jms.ConnectionFactory;
//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.MessageProducer;
//import javax.jms.ObjectMessage;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.sql.DataSource;
//
//
//
///**
// * Session Bean implementation class ProvaSession
// */
//@Stateless
//public class ProvaSession implements ProvaSessionRemote {
//	//@Resource(name="java:jboss/datasources/MySqlDS2")
//	//private DataSource dataSource;
//	//private java.sql.Connection connection;
//	
//	@Resource(mappedName="java:/ConnectionFactory")
//	private ConnectionFactory connectionFactory;
//	
//	@Resource(mappedName="java:/jms/ordersQueue")
//	private Destination orderQueue;
//	
//	
//	//@PersistenceContext(unitName="ejbrelationships") 
//	//private EntityManager em;
//	
//	
//    
//	
//	@PostConstruct
//	public void initialize() {
//		//Person p = em.find(Person.class,1);
//		
//		 //p.setNome("Carollllina");
////		try {
////			connection = dataSource.getConnection();
////		}catch(SQLException e) {
////			e.printStackTrace();
////		}
//	}
//	
//	
//	public int returnFirstId() {
////		Statement statement;
////		try {
////			///statement = connection.createStatement();
////			//ResultSet rs;
////			rs = statement.executeQuery("SELECT id FROM film");
////		
////			rs.next();
////			//return rs.getInt("id");
////		}
////		catch (SQLException e) {
////			 
////			e.printStackTrace();
////		}
//		
//		
//		/* Place a message */
//		
//		
////		UserInfo ui = new UserInfo();
//		//ui.setName("Marco");
//		
//		try {
//			System.out.println("Sending..");
//			Connection connection = connectionFactory.createConnection();
//			
//			if(orderQueue == null)
//				System.out.println("Order queue is null");
//			else	
//				System.out.println("Destination is not null");
//			
//			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//			MessageProducer producer = session.createProducer(orderQueue);
//						
//			connection.start();
//			
//			TextMessage message = session.createTextMessage("Hello AS 7 !");
//			
//			producer.send(message);
//			
//			
//			
//			
//			connection.close();
//			System.out.println("Exiting");
//			
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//			
//			return 0;
//	}
//
//	
//	@PreDestroy
//	public void cleanup() {
//		//try {
//		//	connection.close();
//		//} catch (SQLException e) {
//		//	e.printStackTrace();
//		//}
//	}
//	
//	
//	public ProvaSession() {
//        // TODO Auto-generated constructor stub
//    }
//
//	public String greeting() {
//		
//		return "ciao";
//	}
//	
//}
