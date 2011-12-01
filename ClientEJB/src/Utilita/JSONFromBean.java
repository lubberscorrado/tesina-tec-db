package Utilita;

import org.json.JSONObject;

import com.orb.*;
import com.restaurant.*;

public class JSONFromBean {
	
	public static JSONObject jsonFromPiano(Piano piano){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 1);
			json_obj.put("id", piano.getIdPiano());
			json_obj.put("parentId", "root");
			//json_obj.put("", piano.getIdTenant());
			json_obj.put("numero", piano.getNumero());
			json_obj.put("descrizione", piano.getDescrizione());
			json_obj.put("nome", piano.getNome());
		return json_obj;
	}

	public static JSONObject jsonFromArea(Area area) {
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo",2);
			json_obj.put("id", area.getIdArea());
			json_obj.put("parentId", "xxx");
			//json_obj.put("idTenant", area.getIdTenant());
			json_obj.put("descrizione", area.getDescrizione());
			json_obj.put("nome", area.getNome());
		return json_obj;
	}

	public static JSONObject jsonFromTavolo(Tavolo tavolo) {
		JSONObject json_obj = new JSONObject();
		json_obj.put("tipo", 3);
//		json_obj.put("idPiano", piano.getIdPiano());
//		//json_obj.put("", piano.getIdTenant());
//		json_obj.put("numero", piano.getNumero());
//		json_obj.put("descrizione", piano.getDescrizione());
//		json_obj.put("nome", piano.getNome());
	return json_obj;
	}
	
	
	/*
	 * JSON FROM TREE NODE GESTIONE TAVOLO
	 */
	
	public static JSONObject jsonFromTreeNodePiano(TreeNodePiano treeNodePiano){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 1);
			json_obj.put("id", treeNodePiano.getIdPiano());
			json_obj.put("parentId", "root");
			//json_obj.put("", treeNodePiano.getIdTenant());
			json_obj.put("numero", treeNodePiano.getNumero());
			json_obj.put("descrizione", treeNodePiano.getDescrizione());
			json_obj.put("nome", treeNodePiano.getNome());
			
			
		return json_obj;
	}
	
	public static JSONObject jsonFromTreeNodeArea(TreeNodeArea treeNodeArea){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 2);
			json_obj.put("id", treeNodeArea.getIdArea());
			//json_obj.put("parentId", "xxx");
			//json_obj.put("idTenant", treeNodeArea.getIdTenant());
			json_obj.put("descrizione", treeNodeArea.getDescrizione());
			json_obj.put("nome", treeNodeArea.getNome());
			
		return json_obj;
	}
	
	public static JSONObject jsonFromTreeNodeTavolo(TreeNodeTavolo treeNodeTavolo){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 3);
			json_obj.put("id", treeNodeTavolo.getIdTavolo());
			//json_obj.put("parentId", "xxx");
			//json_obj.put("idTenant", treeNodeTavolo.getIdTenant());
			json_obj.put("descrizione", treeNodeTavolo.getDescrizione());
			json_obj.put("nome", treeNodeTavolo.getNome());
			json_obj.put("stato", treeNodeTavolo.getStato());
			
		return json_obj;
	}

}
