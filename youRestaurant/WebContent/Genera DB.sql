CREATE TABLE IF NOT EXISTS piano (
  idPiano int(11) 			NOT NULL AUTO_INCREMENT,
  numero int(11) 			NOT NULL,
  nome varchar(45) 			NOT NULL DEFAULT 'undefined',
  enabled int(1) 			NOT NULL DEFAULT 1,
  PRIMARY KEY (idPiano),
  UNIQUE KEY nome_UNIQUE (numero)
);

CREATE TABLE IF NOT EXISTS area (
  idArea int(11) 			NOT NULL AUTO_INCREMENT,
  idPiano int(11) 			NOT NULL,
  nome varchar(45) 			NOT NULL DEFAULT 'undefined',
  descrizione varchar(150) 	DEFAULT NULL,
  enabled int(1) 			NOT NULL DEFAULT 1,
  PRIMARY KEY (idArea)
);

CREATE TABLE IF NOT EXISTS stato (
  idStato int(11) 			NOT NULL AUTO_INCREMENT,
  nome varchar(45) 			NOT NULL DEFAULT 'undefined',
  descrizione varchar(150) 	DEFAULT NULL,
  PRIMARY KEY (idStato)
);

CREATE TABLE IF NOT EXISTS tavolo (
  idTavolo int(11) 			NOT NULL AUTO_INCREMENT,
  idArea int(11) 			NOT NULL,
  idStato int(11) 			NOT NULL,
  nome varchar(45) 			NOT NULL DEFAULT 'undefined',
  numPosti int(11) 			NOT NULL DEFAULT 0,
  enabled int(1) 			NOT NULL DEFAULT 1,
  PRIMARY KEY (idTavolo)
);



CREATE OR REPLACE VIEW statoTavolo AS
SELECT t.idTavolo, t.nome AS nomeTavolo, t.numPosti, a.nome AS nomeArea, p.numero AS numeroPiano, p.nome AS nomePiano, s.nome AS statoTavolo
FROM piano AS p, area AS a, tavolo AS t, stato AS s
WHERE p.idPiano=a.idPiano AND a.idArea=t.idArea AND t.idStato=s.idStato;