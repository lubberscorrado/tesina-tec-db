package Utilita;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
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
	
	public static boolean WriteLoginPrivs(HttpServletRequest request, HttpServletResponse response, boolean success, String message){
		HttpSession session = request.getSession();
		int user_privs = (Integer) session.getAttribute("Privs");
		
		JSONObject json_out = new JSONObject();
		json_out.put("success", success);
		json_out.put("message", message);
		JSONObject json_tmp = new JSONObject();
			json_tmp.put("isCameriere", (user_privs & PRIV_Cameriere) == PRIV_Cameriere	);
			json_tmp.put("isCuoco", 	(user_privs & PRIV_Cuoco) == PRIV_Cuoco	);
			json_tmp.put("isCassiere", 	(user_privs & PRIV_Cassiere) == PRIV_Cassiere	);
		json_out.put("privs", json_tmp);
		
		try {
			response.getWriter().println(json_out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean WriteOutput(HttpServletResponse response, boolean success, String message){
		JSONObject json_out = new JSONObject();
		json_out.put("success", success);
		json_out.put("message", message);
		try {
			response.getWriter().println(json_out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean WriteOutput(HttpServletResponse response, boolean success, String message, String json_array_name, JSONArray json_array){
		JSONObject json_out = new JSONObject();
		json_out.put("success", success);
		json_out.put("message", message);
		json_out.put(json_array_name, json_array);
		try {
			response.getWriter().println(json_out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
