package com.mail;

import javax.mail.MessagingException;

import com.exceptions.DatabaseException;

public class ThreadMailSender implements Runnable {

	private String[] sendTo;
	private String subject;
	private String message;
	private String from;
	
	
	public ThreadMailSender(int idTenant, String superadmin, String passwd,String email) throws InterruptedException{
		Thread.currentThread().setName("ThreadMailSender");
		
		sendTo = new String[1];
		sendTo[0] = new String(email);
		
		from = 		new String("YouRestaurant");
		subject = 	new String("Registrazione al servizio youRestaurant");
		message =	new String("Grazie per avere scelto il nostro servizio!\n\n" +
					"Qui di seguito saranno fornite le tue credenziali di accesso all'area di amministrazione.\n\n" +
					"Codice ristorante:\t"+ idTenant +"\n" +
					"Username:\t\t"+ superadmin +"\n" +
					"Password:\t\t"+ passwd +"\n\n" +
					"Mandaci i tuoi feedback per un servizio sempre migliore!");
		
	}

	@Override
	public void run() {

//		try {
//			Thread.currentThread().sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		sendMail();
	}
	
	private void sendMail(){
//		try {
//			System.out.print("Invio a: ");
//			for(int i=0; i<sendTo.length; i++)
//				System.out.print(sendTo[i]+" ");
//			
//			System.out.print("\nSoggetto: "+subject);
//			System.out.print("\nMessaggio: "+message);
//			
//			//SendMail.sendSSLMessage(sendTo, subject,message, from);
//			
//			
//		} catch (SecurityException e){
//			e.printStackTrace();
//			System.out.println("SECURITY EXCEPTIONNNNNNN ");
//			return;
//		}
//		System.out.println("MAIL INVIATA");
		
		try {
			SendMail.sendSSLMessage(sendTo, subject,message, from);
		} catch (MessagingException e) {
			return;
		} catch (SecurityException e){
			return;
		}
		
	}

}
