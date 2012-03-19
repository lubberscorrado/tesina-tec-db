package com.orb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="utentepersonale")
public class UtentePersonale {
	
	@Id
	@Column(name="idUtente")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUtente;
	
	@Column(name="idTenant")
	private int idTenant;
	
	@Column(name="username")
	private String username;
	
	@Column(name="passwd")
	private String password;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="cognome")
	private String cognome;
	
	@Column(name="isCameriere")
	private boolean isCameriere;
	
	@Column(name="isCassiere")
	private boolean isCassiere;
	
	@Column(name="isCucina")
	private boolean isCucina;
	
	@Column(name="isAdmin")
	private boolean isAdmin;
	
	@Column(name="removed")
	private boolean removed;
	
	@OneToMany(mappedBy="cameriereAssociato")
	private List<Conto> conti;
	
	@OneToMany(mappedBy="cucinaAssociata")
	private List<Comanda> comandePreparate;
	
	@OneToMany(mappedBy="cameriereAssociato")
	private List<Comanda> comandePreseInCarico;
	

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public boolean isCameriere() {
		return isCameriere;
	}

	public void setCameriere(boolean isCameriere) {
		this.isCameriere = isCameriere;
	}

	public boolean isCassiere() {
		return isCassiere;
	}

	public void setCassiere(boolean isCassiere) {
		this.isCassiere = isCassiere;
	}

	public boolean isCucina() {
		return isCucina;
	}

	public void setCucina(boolean isCucina) {
		this.isCucina = isCucina;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<Conto> getConti() {
		return conti;
	}

	public void setConti(List<Conto> conti) {
		this.conti = conti;
	}

	public List<Comanda> getComandePreparate() {
		return comandePreparate;
	}

	public List<Comanda> getComandePreseInCarico() {
		return comandePreseInCarico;
	}

	public boolean getRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

}



