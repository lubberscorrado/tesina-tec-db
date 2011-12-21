package com.restaurant;

import com.orb.Tenant;

public class WrapperTenant {

	private int idTenant;
	private String ragioneSociale;
	private String piva;
	private String indirizzo;
	private int civico;
	private int cap;
	private String citta;
	private String provincia;
	private String nazione;
	private String telefono;
	private String fax;
	private String email;
	private String url;
	private String superadmin;
	private String passwd;
	
	public WrapperTenant(Tenant tenant) {
		this.idTenant = tenant.getIdTenant();
		this.ragioneSociale = tenant.getRagioneSociale();
		this.piva = tenant.getPiva();
		this.indirizzo = tenant.getIndirizzo();
		this.civico = tenant.getCivico();
		this.cap = tenant.getCap();
		this.citta = tenant.getCitta();
		this.provincia = tenant.getProvincia();
		this.nazione = tenant.getNazione();
		this.telefono = tenant.getTelefono();
		this.fax = tenant.getFax();
		this.email = tenant.getEmail();
		this.url = tenant.getUrl();
		this.superadmin = tenant.getSuperadmin();
		this.passwd = tenant.getPasswd();
	}

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
