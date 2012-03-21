package com.orb;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQueries({
	@NamedQuery(name = "getStatoUtentePersonale",
				query = "SELECT t FROM StatoUtentePersonale t WHERE t.idTenant = :idTenant")})
@Table(name="statoutentepersonale")
public class StatoUtentePersonale {
		
	@Id
	@Column(name="idUtente")
	private int idUtente;
		
	@Column(name="idTenant")
	private int idTenant;

	@Column(name="tipoAccesso")
	@Enumerated(EnumType.STRING)
	private StatoUtentePersonaleEnum tipoAccesso;
	
	@Column(name="loginTime")
	private Timestamp loginTime;
	
	@OneToOne
	@JoinColumn(name="idUtente", referencedColumnName="idUtente")
	private UtentePersonale utentePersonale;
	
	public StatoUtentePersonale(){super();}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}

	public StatoUtentePersonaleEnum getTipoAccesso() {
		return tipoAccesso;
	}

	public void setTipoAccesso(StatoUtentePersonaleEnum tipoAccesso) {
		this.tipoAccesso = tipoAccesso;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public UtentePersonale getUtentePersonale() {
		return utentePersonale;
	}

	public void setUtentePersonale(UtentePersonale utentePersonale) {
		this.utentePersonale = utentePersonale;
	}

}