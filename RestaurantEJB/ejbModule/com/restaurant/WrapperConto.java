package com.restaurant;

import java.math.BigDecimal;
import java.util.Date;

import com.orb.Conto;

public class WrapperConto {
	private int idTenant;
	private int idConto;
	private BigDecimal prezzo;
	private Date timestampApertura;
	private Date timestampChiusura;
	
	public WrapperConto(Conto conto) {
		this.idTenant = conto.getIdTenant();
		this.idConto = conto.getIdConto();
		this.prezzo = conto.getPrezzo();
		this.timestampApertura = conto.getTimeStampApertura();
		this.timestampChiusura = conto.getTimeStampChiusura();
	}
	
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
	public Date getTimestampApertura() {
		return timestampApertura;
	}
	public void setTimestampApertura(Date timestampApertura) {
		this.timestampApertura = timestampApertura;
	}
	public Date getTimestampChiusura() {
		return timestampChiusura;
	}
	public void setTimestampChiusura(Date timestampChiusura) {
		this.timestampChiusura = timestampChiusura;
	}
		
}
