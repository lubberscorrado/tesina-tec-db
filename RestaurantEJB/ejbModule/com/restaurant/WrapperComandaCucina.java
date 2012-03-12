package com.restaurant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orb.Comanda;
import com.orb.StatoComandaEnum;

public class WrapperComandaCucina {
	private int idTenant;
	private int idComanda;
	private StatoComandaEnum stato;
	private int quantita;
	private BigDecimal prezzo;
	private String note;
	private Date lastModified;
	private String hashGruppo;
	
	private int idConto;
	
	private int idVoceMenu;
	private String nomeVoceMenu;
	
	private int idCategoria;
	private String nomeCategoria;
	private String tipoCategoria; // Cibo o bevanda
	
	private int idTavolo;
	private String nomeTavolo;
	
	private List<WrapperVariazione> listVariazioni;
	
	public WrapperComandaCucina() {
		listVariazioni = new ArrayList<WrapperVariazione>();
		this.idVoceMenu = 0;
		this.idTenant = 0;
		this.idComanda = 0;
		this.note = "";
		this.hashGruppo = "";
		this.prezzo = new BigDecimal(0);
		this.quantita = 0;
		this.stato = StatoComandaEnum.SOSPESA;
		
	}

	public WrapperComandaCucina(Comanda comanda) {
		
		/* Anche se l'oggetto comanda è detached il fetching della voce di
		 * menu associata è eager quindi deve essere disponibile */
		this.idVoceMenu = comanda.getVoceMenuAssociata().getIdVoceMenu();
		this.idTenant = comanda.getIdTenant();
		this.idComanda = comanda.getIdComanda();
		this.note = comanda.getNote();
		this.hashGruppo = comanda.getHashGruppo();
		this.prezzo = comanda.getPrezzo();
		this.quantita = comanda.getQuantita();
		this.stato = comanda.getStato();
		
		listVariazioni = new ArrayList<WrapperVariazione>();
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
	
	public List<WrapperVariazione> getListVariazioni() {
		if(listVariazioni == null) 
			listVariazioni = new ArrayList<WrapperVariazione>();
		return listVariazioni;
	}
	
	public void setListVariazioni(List<WrapperVariazione> listVariazioni) {
		this.listVariazioni = listVariazioni;
	}
	
	public void addVariazione(int id, String nome, String descrizione, BigDecimal prezzo) {
		listVariazioni.add(new WrapperVariazione(id, nome, descrizione, prezzo));
	}
	
	public void removeVariazione(int id) {
		listVariazioni.remove(id);
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

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public int getIdConto() {
		return idConto;
	}

	public void setIdConto(int idConto) {
		this.idConto = idConto;
	}

	public String getNomeVoceMenu() {
		return nomeVoceMenu;
	}

	public void setNomeVoceMenu(String nomeVoceMenu) {
		this.nomeVoceMenu = nomeVoceMenu;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public String getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(String tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	public int getIdTavolo() {
		return idTavolo;
	}

	public void setIdTavolo(int idTavolo) {
		this.idTavolo = idTavolo;
	}

	public String getNomeTavolo() {
		return nomeTavolo;
	}

	public void setNomeTavolo(String nomeTavolo) {
		this.nomeTavolo = nomeTavolo;
	}
	
	
	
}
