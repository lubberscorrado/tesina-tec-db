package com.restaurant.android.cucina.comanda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/** 
 * Versione dell'oggetto "Comanda" che contiene le informazioni relative alle comande
 * che dovranno essere mostrate nell'ambito dell'interfaccia cucina.
 * 
 * @author Fabio Pierazzi
 */
public class ComandaCucina {
	// Comanda
	private int idComanda;
	private String statoComanda;
	private int quantita;
	private BigDecimal prezzo;
	private String note;
	private Date lastModified;
	private String hashGroup;
	
	// Conto
	private int idConto;
	
	// VoceMenu
	private int idVoceMenu;
	private String nomeVoceMenu;
	
	// Categoria
	private int idCategoria;
	private String nomeCategoria;
	private String tipoCategoria; // Cibo o Bevanda
	
	// Tavolo
	private int idTavolo;
	private String nomeTavolo;
	
	// Variazioni
	private ArrayList<Variazione> arrayList_variazioni;

	public int getIdComanda() {
		return idComanda;
	}

	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	public String getStatoComanda() {
		return statoComanda;
	}

	public void setStatoComanda(String statoComanda) {
		this.statoComanda = statoComanda;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public int getIdVoceMenu() {
		return idVoceMenu;
	}

	public void setIdVoceMenu(int idVoceMenu) {
		this.idVoceMenu = idVoceMenu;
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

	public ArrayList<Variazione> getArrayList_variazioni() {
		return arrayList_variazioni;
	}

	public void setArrayList_variazioni(ArrayList<Variazione> arrayList_variazioni) {
		this.arrayList_variazioni = arrayList_variazioni;
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

	public String getHashGroup() {
		return hashGroup;
	}

	public void setHashGroup(String hashGroup) {
		this.hashGroup = hashGroup;
	}

	public int getIdConto() {
		return idConto;
	}

	public void setIdConto(int idConto) {
		this.idConto = idConto;
	}
	
}
