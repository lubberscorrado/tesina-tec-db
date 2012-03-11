package com.restaurant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.orb.Comanda;
import com.orb.StatoComandaEnum;
import com.orb.Variazione;

public class WrapperComanda {
	private int idTenant;
	private int idComanda;
	private String note;
	private String hashGruppo;
	private BigDecimal prezzo;
	private int quantita;
	private int idVoceMenu;
	private StatoComandaEnum stato;
	private List<Integer> listIdVariazioni;
	
	public WrapperComanda() {
		listIdVariazioni = new ArrayList<Integer>();
		this.idVoceMenu = 0;
		this.idTenant = 0;
		this.idComanda = 0;
		this.note = "";
		this.hashGruppo = "";
		this.prezzo = new BigDecimal(0);
		this.quantita = 0;
		this.stato = StatoComandaEnum.SOSPESA;
		
		listIdVariazioni = new ArrayList<Integer>();
	}

	public WrapperComanda(Comanda comanda) {
	
		this.idVoceMenu = comanda.getVoceMenuAssociata().getIdVoceMenu();
		this.idTenant = comanda.getIdTenant();
		this.idComanda = comanda.getIdComanda();
		this.note = comanda.getNote();
		this.hashGruppo = comanda.getHashGruppo();
		this.prezzo = comanda.getPrezzo();
		this.quantita = comanda.getQuantita();
		this.stato = comanda.getStato();
		
		listIdVariazioni = new ArrayList<Integer>();
		
		for(Variazione variazione : comanda.getVariazioniAssociate()) 
			listIdVariazioni.add(variazione.getIdVariazione());
		
	}
	
	
	public int getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
	public int getIdComanda() {
		return idComanda;
	}
	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	public int getQuantita() {
		return quantita;
	}
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	public StatoComandaEnum getStato() {
		return stato;
	}
	public void setStato(StatoComandaEnum stato) {
		this.stato = stato;
	}
	
	public void addVariazione(int id) {
		listIdVariazioni.add(new Integer(id));
	}
	
	public void removeVariazione(int id) {
		listIdVariazioni.remove(new Integer(id));
	}
	
	public List<Integer> getListIdVariazioni() {
		if(listIdVariazioni == null)
			listIdVariazioni = new ArrayList<Integer>();
		return listIdVariazioni;
	}

	public String getHashGruppo() {
		return hashGruppo;
	}

	public void setHashGruppo(String hashGruppo) {
		this.hashGruppo = hashGruppo;
	}

	public int getIdVoceMenu() {
		return idVoceMenu;
	}

	public void setIdVoceMenu(int idVoceMenu) {
		this.idVoceMenu = idVoceMenu;
	}
	
	
	
}
