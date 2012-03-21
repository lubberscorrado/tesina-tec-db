package com.mail;

import javax.mail.MessagingException;

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
	
	/*INVIO FATTURE*/
	public ThreadMailSender(String ragioneSociale,long numeroConti,float canoneMensile,float costoConto,String email) throws InterruptedException{
		Thread.currentThread().setName("ThreadMailSender");
		
		sendTo = new String[1];
		sendTo[0] = new String(email);
		
		from = 		new String("YouRestaurant");
		subject = 	new String("Fatturazione servizio YouRestaurant");
		message =	new String("Fatturazione servizio YouRestaurant - " +ragioneSociale+"\n" +
				"Canone mensile: " +canoneMensile+"€\n" +
				"Numero conti del mese:" +numeroConti+"\n" +
				"Costo/Conto: " +costoConto+"€\n" +
				"Totale:"+(canoneMensile+(costoConto*numeroConti))+"€\n\n" +
				"Mandaci i tuoi feedback per un servizio sempre migliore!");
		
	}

	@Override
	public void run() {
		sendMail();
	}
	
	private void sendMail(){
		try {
			SendMail.sendSSLMessage(sendTo, subject,message, from);
		} catch (MessagingException e) {
			return;
		} catch (SecurityException e){
			return;
		}
		
	}

}
