package Utilita;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

public class JSONResponse {
	//Privilegi
	public static final int PRIV_Cameriere = 			0x0001;		//00000001
	public static final int PRIV_Cuoco =				0x0002;		//00000010
	public static final int PRIV_Cassiere = 			0x0004;		//00000100
	public static final int PRIV_Administrator = 		0x000F;		//00001111
	public static final int PRIV_SuperAdministrator = 	0x001F;		//00011111
	
	public static boolean UserAccessControl(HttpServletRequest request, HttpServletResponse response, int privs) throws IOException{
		HttpSession session = request.getSession();
		if(session == null || session.isNew() || session.getAttribute("Logged").equals(false)){
			JSONObject json_out = new JSONObject();
			json_out.put("success", false);
			json_out.put("message", "L'utente non è autenticato nel sistema.");
			response.getWriter().println(json_out);
			return false;
		}
		
		int user_privs = (Integer) session.getAttribute("Privs");
		
		if((user_privs&privs) == privs){
			return true;
		}else{
			JSONObject json_out = new JSONObject();
			json_out.put("success", false);
			json_out.put("message", "I privilegi dell'utente non sono sufficienti.");
			response.getWriter().println(json_out);
			return false;
		}
		
		
	}
}
