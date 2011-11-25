package Mail;

import javax.mail.MessagingException;
import java.security.*;
import com.sun.net.ssl.internal.ssl.*;


public class Provamail {

	public static void main(String[] args) {
		String emailFromAddress = "YouRestaurant";
		String emailSubjectTxt = 	"Registrazione al servizio youRestaurant";
		String emailMsgTxt = 		"Grazie per avere scelto il nostro servizio!\n\n" +
									"Qui di seguito saranno fornite le tue credenziali di accesso all'area di amministrazione.\n\n" +
									"Codice ristorante: bagnatu che bagno anch'io\n" +
									"Username: biascicchigna\n" +
									"Password: asderello con l'ombrello\n\n" +
									"Mandaci i tuoi feedback per un servizio sempre migliore!";
		String[] sendTo = { "kastknocker@gmail.com","biofrost88@gmail.com","stefano.soli3@gmail.com"};
		

		try {
			SendMail.sendSSLMessage(sendTo, emailSubjectTxt,emailMsgTxt, emailFromAddress);
		} catch (MessagingException e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
		System.out.println("Sucessfully Sent mail to All Users");
		
		System.out.println("MAIL INVIATA");
	}

}
