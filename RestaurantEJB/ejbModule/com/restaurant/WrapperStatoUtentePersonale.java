package com.restaurant;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.orb.Comanda;
import com.orb.StatoComandaEnum;
import com.orb.StatoUtentePersonale;
import com.orb.StatoUtentePersonaleEnum;
import com.orb.Variazione;

public class WrapperStatoUtentePersonale {
	
	private int idUtente;
	private int idTenant;
	private StatoUtentePersonaleEnum tipoAccesso;
	private Timestamp loginTime;
	
	public WrapperStatoUtentePersonale(StatoUtentePersonale statoUtentePersonale) {
		this.idUtente = statoUtentePersonale.getIdUtente();
		this.idTenant = statoUtentePersonale.getIdTenant();
		this.tipoAccesso = statoUtentePersonale.getTipoAccesso();
		this.loginTime = statoUtentePersonale.getLoginTime();
	}
	
	
}
