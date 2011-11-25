package Mail;

import java.security.Security;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {
	
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	//private static final String emailMsgTxt = "Test Message Contents";
	//private static final String emailSubjectTxt = "A test from gmail";
	//private static final String emailFromAddress = "";
	//private static final String[] sendTo = {""};
	
	
	public static void sendSSLMessage(String recipients[], String subject,String message, String from) throws MessagingException {
		boolean debug = true;
		
		/*Da errore ma non c'e' da preoccuparsi*/
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("yourestaurant@gmail.com", "magicrestaurant");
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}

}
