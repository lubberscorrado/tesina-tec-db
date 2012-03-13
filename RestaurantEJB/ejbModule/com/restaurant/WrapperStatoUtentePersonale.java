package com.restaurant;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.orb.Comanda;
import com.orb.StatoComandaEnum;
import com.orb.StatoUtentePersonale;
import com.orb.StatoUtentePersonaleEnum;
import com.orb.Variazione;

public class WrapperStatoUtentePersonale {
	
	private int idUtente;
	private int idTenant;
	private StatoUtentePersonaleEnum tipoAccesso;
	private Timestamp loginTime;
	
	public WrapperStatoUtentePersonale(StatoUtentePersonale statoUtentePersonale) {
		this.idUtente = statoUtentePersonale.getIdUtente();
		this.idTenant = statoUtentePersonale.getIdTenant();
		this.tipoAccesso = statoUtentePersonale.getTipoAccesso();
		this.loginTime = statoUtentePersonale.getLoginTime();
	}

//	public WrapperStatoUtentePersonale(Comanda comanda) {
//	
//		this.idVoceMenu = comanda.getVoceMenuAssociata().getIdVoceMenu();
//		this.idTenant = comanda.getIdTenant();
//		this.idComanda = comanda.getIdComanda();
//		this.note = comanda.getNote();
//		this.hashGruppo = comanda.getHashGruppo();
//		this.prezzo = comanda.getPrezzo();
//		this.quantita = comanda.getQuantita();
//		this.stato = comanda.getStato();
//		
//		listIdVariazioni = new ArrayList<Integer>();
//		
//		for(Variazione variazione : comanda.getVariazioniAssociate()) 
//			listIdVariazioni.add(variazione.getIdVariazione());
//		
//	}
	
//	
//	public int getIdTenant() {
//		return idTenant;
//	}
//	public void setIdTenant(int idTenant) {
//		this.idTenant = idTenant;
//	}
//	public int getIdComanda() {
//		return idComanda;
//	}
//	public void setIdComanda(int idComanda) {
//		this.idComanda = idComanda;
//	}
//	public String getNote() {
//		return note;
//	}
//	public void setNote(String note) {
//		this.note = note;
//	}
//	public BigDecimal getPrezzo() {
//		return prezzo;
//	}
//	public void setPrezzo(BigDecimal prezzo) {
//		this.prezzo = prezzo;
//	}
//	public int getQuantita() {
//		return quantita;
//	}
//	public void setQuantita(int quantita) {
//		this.quantita = quantita;
//	}
//	public StatoComandaEnum getStato() {
//		return stato;
//	}
//	public void setStato(StatoComandaEnum stato) {
//		this.stato = stato;
//	}
//	
//	public void addVariazione(int id) {
//		listIdVariazioni.add(new Integer(id));
//	}
//	
//	public void removeVariazione(int id) {
//		listIdVariazioni.remove(new Integer(id));
//	}
//	
//	public List<Integer> getListIdVariazioni() {
//		if(listIdVariazioni == null)
//			listIdVariazioni = new ArrayList<Integer>();
//		return listIdVariazioni;
//	}
//
//	public String getHashGruppo() {
//		return hashGruppo;
//	}
//
//	public void setHashGruppo(String hashGruppo) {
//		this.hashGruppo = hashGruppo;
//	}
//
//	public int getIdVoceMenu() {
//		return idVoceMenu;
//	}
//
//	public void setIdVoceMenu(int idVoceMenu) {
//		this.idVoceMenu = idVoceMenu;
//	}
	
	
	
}
