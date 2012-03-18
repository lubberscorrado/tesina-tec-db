package com.orb;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="variazione")

@NamedQueries({
	@NamedQuery(name = "getVariazioniByCategoria",
				query = "SELECT v FROM Variazione v JOIN v.categoriaAppartenenza c " +
						"WHERE v.idTenant = :idTenant AND c.idCategoria = :idCategoria " +
						"AND v.removed = :removed ")})
public class Variazione {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idVariazione")
	private int idVariazione;
	
	@Column(name="idTenant")
	private int idTenant;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="descrizione")
	private String descrizone;
	
	@Column(name="prezzo")
	private BigDecimal prezzo;
	
	@Column(name="removed")
	private boolean removed;
	
	@ManyToOne
	@JoinColumn(name="idCategoria", referencedColumnName="idCategoria")
	private Categoria categoriaAppartenenza;
	
	
	@ManyToMany(mappedBy="variazioniAssociate")
	private List<Comanda> comande;

	public int getIdVariazione() {
		return idVariazione;
	}

	public void setIdVariazione(int idVariazione) {
		this.idVariazione = idVariazione;
	}

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizone() {
		return descrizone;
	}

	public void setDescrizone(String descrizone) {
		this.descrizone = descrizone;
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

	public Categoria getCategoriaAppartenenza() {
		return categoriaAppartenenza;
	}

	public void setCategoriaAppartenenza(Categoria categoriaAppartenenza) {
		this.categoriaAppartenenza = categoriaAppartenenza;
	}

	public List<Comanda> getComande() {
		return comande;
	}

	public boolean getRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

}
