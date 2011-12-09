package Utilita;

import org.json.JSONObject;

import com.orb.*;
import com.restaurant.*;

public class JSONFromBean {
	/*
	public static JSONObject jsonFromPiano(Piano piano){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 1);
			json_obj.put("id", "P"+piano.getIdPiano());
			json_obj.put("parentId", "root");
			//json_obj.put("", piano.getIdTenant());
			json_obj.put("numeroPiano", piano.getNumero());
			json_obj.put("descrizione", piano.getDescrizione());
			json_obj.put("nome", piano.getNome());
		return json_obj;
	}

	public static JSONObject jsonFromArea(Area area) {
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo",2);
			json_obj.put("id", "+A"+area.getIdArea());
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
	*/
	
	/*
	 * JSON FROM TREE NODE GESTIONE TAVOLO
	 */
	
	public static JSONObject jsonFromTreeNodePiano(TreeNodePiano treeNodePiano){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 1);
			json_obj.put("id", 		"P"+treeNodePiano.getIdPiano());
			json_obj.put("parentId", "root");
			//json_obj.put("", treeNodePiano.getIdTenant());
			json_obj.put("numeroPiano", treeNodePiano.getNumero());
			json_obj.put("descrizione", treeNodePiano.getDescrizione());
			json_obj.put("nome", 		treeNodePiano.getNome());
			json_obj.put("enabled", 	treeNodePiano.isEnabled());
		return json_obj;
	}
	
	public static JSONObject jsonFromTreeNodeArea(TreeNodeArea treeNodeArea){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 2);
			json_obj.put("id", 		"A"+treeNodeArea.getIdArea());
			//json_obj.put("parentId", "xxx");
			//json_obj.put("idTenant", treeNodeArea.getIdTenant());
			json_obj.put("descrizione", treeNodeArea.getDescrizione());
			json_obj.put("nome", 		treeNodeArea.getNome());
			json_obj.put("enabled", 	treeNodeArea.isEnabled());
		return json_obj;
	}
	
	public static JSONObject jsonFromTreeNodeTavolo(TreeNodeTavolo treeNodeTavolo){
		JSONObject json_obj = new JSONObject();
			json_obj.put("tipo", 3);
			json_obj.put("id", 		"T"+treeNodeTavolo.getIdTavolo());
			//json_obj.put("parentId", "xxx");
			//json_obj.put("idTenant", treeNodeTavolo.getIdTenant());
			json_obj.put("descrizione", treeNodeTavolo.getDescrizione());
			json_obj.put("nome", 		treeNodeTavolo.getNome());
			json_obj.put("stato", 		treeNodeTavolo.getStato());
			json_obj.put("numPosti", 	treeNodeTavolo.getNumposti());
			json_obj.put("enabled", 	treeNodeTavolo.isEnabled());
		return json_obj;
	}
	
	/*
	 * JSON FROM STATO LOCALE
	 */
	public static JSONObject jsonFromOBJ(StatoTavolo statotavolo){
		JSONObject json_obj = new JSONObject();
		json_obj.put("idTavolo", 		statotavolo.getIdTavolo());
		json_obj.put("numeroPiano", 	statotavolo.getNumeroPiano());
		json_obj.put("numPosti", 		statotavolo.getNumPosti());
		json_obj.put("cameriere", 		statotavolo.getCameriere());
		json_obj.put("nomeArea", 		statotavolo.getNomeArea());
		json_obj.put("nomeTavolo", 		statotavolo.getNomeTavolo());
		json_obj.put("statoTavolo", 	statotavolo.getStatoTavolo());
		return json_obj;
	}
	
	/*
	 * JSON FROM GESTIONE MENU
	 */
	
	public static JSONObject jsonFromOBJ(TreeNodeCategoria treeNodeCategoria){
		JSONObject json_obj = new JSONObject();
		json_obj.put("id", 		"C"+treeNodeCategoria.getIdCategoria());
		//json_obj.put("parentId", 		"C"+treeNodeCategoria.);
		json_obj.put("nome", 		treeNodeCategoria.getNome());
		json_obj.put("descrizione", 		treeNodeCategoria.getDescrizione());
		return json_obj;
	}
	
	public static JSONObject jsonFromOBJ(TreeNodeVoceMenu treeNodeVoceMenu){
		JSONObject json_obj = new JSONObject();
		json_obj.put("id", 		"V"+treeNodeVoceMenu.getIdVoceMenu());
		//json_obj.put("parentId", 		"C"+treeNodeVoceMenu.);
		json_obj.put("nome", 		treeNodeVoceMenu.getNome());
		json_obj.put("descrizione", 		treeNodeVoceMenu.getDescrizione());
		json_obj.put("prezzo", 		treeNodeVoceMenu.getPrezzo());
		return json_obj;
	}
	
	public static JSONObject jsonFromOBJ(WrapperVariazione wrapperVariazione){
		JSONObject json_obj = new JSONObject();
		json_obj.put("id", 			wrapperVariazione.getIdVariazione());
		//json_obj.put("parentId", 		"C"+treeNodeVoceMenu.);
		json_obj.put("nome", 		wrapperVariazione.getNome());
		json_obj.put("descrizione", wrapperVariazione.getDescrizione());
		json_obj.put("prezzo", 		wrapperVariazione.getPrezzoVariazione());
		return json_obj;
	}

}
