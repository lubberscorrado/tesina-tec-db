package DB;
import java.sql.*;

import org.json.*;

public class DBConnection {
	
	private final String nomeDriver	= "com.mysql.jdbc.Driver";	//Contiene il nome del driver JDBC
	
	private String Host			= "localhost";
	private String schemaDB 	= "db_ristorante_1";     		// Nome del Database a cui connettersi
	private String nomeUtente	= "asd";   			// Nome utente utilizzato per la connessione al Database
	private String pwdUtente	= "asd";    			// Password usata per la connessione al Database
	private String errore		= "";       				// Raccoglie informazioni riguardo l'ultima eccezione sollevata
	
	private static DBConnection db_connection = null;
	private Connection db;       // La connessione col Database
	private boolean connesso;    // Flag che indica se la connessione è attiva o meno

	
	
	public DBConnection(){
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName(nomeDriver).newInstance();
		} catch (Exception ex) {
			System.out.println("Eccezione: Driver JDBC");
		}
	}
	
	public boolean connect(){
		if(connesso){ disconnect();}
		try {
			db = DriverManager.getConnection("jdbc:mysql://"+Host+"/"+schemaDB+"?" +"user="+nomeUtente+"&password="+pwdUtente);
			System.out.println("Connesso.");
			connesso = true;
			return true;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("NON Connesso.");
			connesso = false;
			return false;
		}
	}
	
	public void disconnect() {
		try {
			if(!connesso) return;
			db.close();
			System.out.println("Disconnesso.");
			connesso = false;
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return db;
	}
	
	public String executeJSONQuery(String query){
		System.out.println("executeJSONQuery(String query): "+query);
		if(!connesso){
			System.out.println("La connessione non è attiva!");
			return null;
		}
		   String out = "[";
			  
		      String [] record;
		      int colonne = 0;
		      try {
		         Statement stmt = db.createStatement();     // Creo lo Statement per l'esecuzione della query
		         ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
		         ResultSetMetaData rsmd = rs.getMetaData();
		         colonne = rsmd.getColumnCount();
		         boolean first = true;
		         while(rs.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
		        	if(first){out +="{"; first=false;}else{out +=",{";}
		        	 
		            for (int i=0; i<colonne; i++){
		            	out += "\""+rsmd.getColumnName(i+1)+"\":\""+rs.getString(i+1)+"\"";
		            	if(i<colonne-1) out +=",";
		            }
		            out +="}";
		         }
		         out +="]";
		         rs.close();     // Chiudo il ResultSet
		         stmt.close();   // Chiudo lo Statement
		      } catch (Exception e) { e.printStackTrace(); errore = e.getMessage(); }
		  return out; 
	  }
	
	public JSONObject executeJSONQuery(String query,String root){
		System.out.println("executeJSONQuery(String query,String root): "+query);
		JSONObject	json_out = new JSONObject();
		JSONArray	json_array = new JSONArray();
		json_out.put(root, json_array);
		if(!connesso){
			System.out.println("La connessione non è attiva!");
			return json_out;
		}
		
		JSONObject	json_object = null;
		
		int colonne = 0;
		try {
			Statement stmt = db.createStatement();     // Creo lo Statement per l'esecuzione della query
			ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
			ResultSetMetaData rsmd = rs.getMetaData();
			colonne = rsmd.getColumnCount();
			boolean first = true;
			while(rs.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
				json_object = new JSONObject();
				for (int i=0; i<colonne; i++){
					json_object.put(rsmd.getColumnLabel(i+1), rs.getString(i+1));
				}
				json_array.put(json_object);
			}
			rs.close();     // Chiudo il ResultSet
			stmt.close();   // Chiudo lo Statement
		} catch (Exception e) {
			e.printStackTrace();
			errore = e.getMessage();
			System.out.println("Errore: " + errore);
		}
	
	return json_out; 
	}

	/**
	 * Aggiunge ad un JSON ARRAY esterno i risultati della query
	 * @param query
	 * @param json_array
	 * @return
	 */
	public boolean executeJSONQuery(String query, JSONArray json_array){
		System.out.println("executeJSONQuery(String query,String root): "+query);
		JSONObject	tmp = null;
		if(!connesso){
			System.out.println("La connessione non è attiva!");
			return false;
		}
		
		int colonne = 0;
		try {
			Statement stmt = db.createStatement();     // Creo lo Statement per l'esecuzione della query
			ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
			ResultSetMetaData rsmd = rs.getMetaData();
			colonne = rsmd.getColumnCount();
			boolean first = true;
			while(rs.next()) {   // Creo il vettore risultato scorrendo tutto il ResultSet
				tmp = new JSONObject();
				for (int i=0; i<colonne; i++){
					tmp.put(rsmd.getColumnLabel(i+1), rs.getString(i+1));
				}
				json_array.put(tmp);
			}
			rs.close();     // Chiudo il ResultSet
			stmt.close();   // Chiudo lo Statement
		} catch (Exception e) {
			e.printStackTrace();
			errore = e.getMessage();
			System.out.println("Errore: " + errore);
		}
	
	return true; 
	}

}
