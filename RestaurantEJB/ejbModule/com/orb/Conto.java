package com.orb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="conto")
public class Conto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idConto")
	private int idConto;

	@Column(name="prezzo")	
	private BigDecimal prezzo;
	
	@Column(name="timestamp_apertura")
	private Timestamp timeStampApertura;
	
	@Column(name="timestamp_chiusura")
	private Timestamp timeStampChiusura;
	
	@Column(name="stato")
	@Enumerated(EnumType.STRING)
	private StatoContoEnum stato;
	
	@ManyToOne
	@JoinColumn(name="idTavolo", referencedColumnName="idTavolo")
	private Tavolo tavoloAppartenenza;
	
	@ManyToOne
	@JoinColumn(name="idCameriere", referencedColumnName="idUtente")
	private UtentePersonale cameriereAssociato;
		
	@Column(name="idTenant")
	private int idTenant;

	@OneToMany(mappedBy="contoAppartenenza")
	private List<Comanda> comande;
	
	public int getIdConto() {
		return idConto;
	}
	public void setIdConto(int idConto) {
		this.idConto = idConto;
	}
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	public Timestamp getTimeStampApertura() {
		return timeStampApertura;
	}
	public void setTimeStampApertura(Timestamp timeStampApertura) {
		this.timeStampApertura = timeStampApertura;
	}
	public Timestamp getTimeStampChiusura() {
		return timeStampChiusura;
	}
	public void setTimeStampChiusura(Timestamp timeStampChiusura) {
		this.timeStampChiusura = timeStampChiusura;
	}
	public StatoContoEnum getStato() {
		return stato;
	}
	public void setStato(StatoContoEnum stato) {
		this.stato = stato;
	}
	public Tavolo getTavoloAppartenenza() {
		return tavoloAppartenenza;
	}
	public void setTavoloAppartenenza(Tavolo tavoloAppartenenza) {
		this.tavoloAppartenenza = tavoloAppartenenza;
	}
	public UtentePersonale getCameriereAssociato() {
		return cameriereAssociato;
	}
	public void setCameriereAssociato(UtentePersonale cameriereAssociato) {
		this.cameriereAssociato = cameriereAssociato;
	}
	public int getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
	public List<Comanda> getComande() {
		return comande;
	}

}

