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

@Entity
@Table(name="categoria")
public class Categoria {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idCategoria")
	private int idCategoria;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="idTenant")
	private int idTenant;
	
	@Column(name="descrizione")
	private String descrizione;
		
	@ManyToOne
	@JoinColumn(name="idCategoriaPadre", referencedColumnName="idCategoria")
	private Categoria categoriaPadre;
	
	@OneToMany(mappedBy="categoriaPadre")
	private List<Categoria> categorieFiglie;

	@OneToMany(mappedBy="categoriaAppartenenza")
	private List<Variazione> variazioni;
	
	@OneToMany(mappedBy="categoriaAppartenenza")
	private List<VoceMenu> vociMenu;
	
	
	
	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
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

	public Categoria getCategoriaPadre() {
		return categoriaPadre;
	}

	public void setCategoriaPadre(Categoria categoriaPadre) {
		this.categoriaPadre = categoriaPadre;
	}

	public List<Categoria> getCategorieFiglie() {
		return categorieFiglie;
	}

	public void setCategorieFiglie(List<Categoria> categorieFiglie) {
		this.categorieFiglie = categorieFiglie;
	}

	public List<Variazione> getVariazioni() {
		return variazioni;
	}

	public void setVariazioni(List<Variazione> variazioni) {
		this.variazioni = variazioni;
	}

	public List<VoceMenu> getVociMenu() {
		return vociMenu;
	}

	public void setVociMenu(List<VoceMenu> vociMenu) {
		this.vociMenu = vociMenu;
	}

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
	
	
}
