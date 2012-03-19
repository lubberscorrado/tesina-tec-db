package com.orb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({
	@NamedQuery(name = "getPiani",
				query = "SELECT p FROM Piano p " +
						"WHERE p.idTenant = :idTenant " +
						"AND p.removed = :removed ")})
@Table(name="piano")
public class Piano {
		
	@Id
	@Column(name="idPiano")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idPiano;
		
	@Column(name="idTenant")
	private int idTenant;
		
	@Column(name="numero")
	private int numero;
		
	@Column(name="nome")
	private String nome;

	@Column(name="descrizione")
	private String descrizione;
	
	@Column(name="enabled")
	private boolean enabled;
	
	@Column(name="removed")
	private boolean removed;
	
	@OneToMany(mappedBy="pianoAppartenenza")
	private List<Area> listAree;

	public int getIdPiano() {
		return idPiano;
	}

	public void setIdPiano(int idPiano) {
		this.idPiano = idPiano;
	}

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Area> getAree() {
		return listAree;
	}

	public boolean getRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	
}