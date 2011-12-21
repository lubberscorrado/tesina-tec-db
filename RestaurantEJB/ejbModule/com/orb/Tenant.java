package com.orb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/*
 * create table if not exists tenant(
	idTenant int(11) not null auto_increment,
	ragionesociale varchar(40) not null default 'Undefined',
	piva	varchar(20) not null,
	indirizzo varchar(30) not null,
	civico int(3) not null,
	cap int(5) not null,
	citta varchar(20) not null,
	provincia varchar(20) not null,
	nazione varchar(20) not null,
	telefono varchar(20) not null,
	fax varchar(20) not null,
	email varchar(20) not null,
	url varchar(30) not null,
	superadmin varchar(30) not null,
	passwd varchar(30) not null,
	primary key (idTenant)
);
 */
@Entity
@Table(name="tenant")
public class Tenant {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idTenant")
	private int idTenant;
	
	@Column(name="ragionesociale")
	private String ragioneSociale;
	
	@Column(name="piva")
	private String piva;
	
	@Column(name="indirizzo")
	private String indirizzo;
	
	@Column(name="civico")
	private int civico;
	
	@Column(name="cap")
	private int cap;
	
	@Column(name="citta")
	private String citta;
	
	@Column(name="provincia")
	private String provincia;
	
	@Column(name="nazione")
	private String nazione;
	
	@Column(name="telefono")
	private String telefono;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="email")
	private String email;
	
	@Column(name="url")
	private String url;
	
	@Column(name="superadmin")
	private String superadmin;
	
	@Column(name="passwd")
	private String passwd;

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public int getCivico() {
		return civico;
	}

	public void setCivico(int civico) {
		this.civico = civico;
	}

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSuperadmin() {
		return superadmin;
	}

	public void setSuperadmin(String superadmin) {
		this.superadmin = superadmin;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
}
