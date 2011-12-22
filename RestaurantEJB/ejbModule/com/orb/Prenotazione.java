package com.orb;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="prenotazione")
public class Prenotazione {
		
	@Id
	@Column(name="idPrenotazione")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idPrenotazione;
		
	@Column(name="ora")
	private Time ora;

	@Column(name="data")
	private Date data;
	
	@Column(name="stato")
	private String stato;

	@Column(name="idTenant")
	private int idTenant;
	
	@Column(name="numpersone")
	private int numpersone;
		
	@Column(name="nomecliente")
	private String nomecliente;
	
	@ManyToOne
	@JoinColumn(name="idTavolo", referencedColumnName="idTavolo")
	private Tavolo tavoloAppartenenza;

	public int getIdPrenotazione() {
		return idPrenotazione;
	}

	public void setIdPrenotazione(int idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}

	public Time getOra() {
		return ora;
	}

	public void setOra(Time ora) {
		this.ora = ora;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}

	public int getNumpersone() {
		return numpersone;
	}

	public void setNumpersone(int numpersone) {
		this.numpersone = numpersone;
	}

	public Tavolo getTavoloAppartenenza() {
		return tavoloAppartenenza;
	}

	public void setTavoloAppartenenza(Tavolo tavoloAppartenenza) {
		this.tavoloAppartenenza = tavoloAppartenenza;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}
	
	
	
}
	