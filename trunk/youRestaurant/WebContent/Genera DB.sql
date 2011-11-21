delimiter $$

CREATE TABLE `piano` (
  `idPiano` int(11) NOT NULL AUTO_INCREMENT,
  `numero` int(11) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `enabled` int(1) DEFAULT '1',
  PRIMARY KEY (`idPiano`),
  UNIQUE KEY `nome_UNIQUE` (`numero`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE `area` (
  `idArea` int(11) NOT NULL AUTO_INCREMENT,
  `idPiano` int(11) DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `descrizione` varchar(150) DEFAULT NULL,
  `enabled` int(1) DEFAULT '1',
  PRIMARY KEY (`idArea`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE `stato` (
  `idStato` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `descrizione` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`idStato`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE `tavolo` (
  `idTavolo` int(11) NOT NULL AUTO_INCREMENT,
  `idArea` varchar(45) DEFAULT NULL,
  `idStato` varchar(45) DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `numPosti` int(11) NOT NULL,
  `enabled` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idTavolo`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1$$




CREATE VIEW statoTavolo AS
SELECT t.idTavolo, t.nome AS nomeTavolo, t.numPosti, a.nome AS nomeArea, p.numero AS numeroPiano, p.nome AS nomePiano, s.nome AS statoTavolo
FROM piano AS p, area AS a, tavolo AS t, stato AS s
WHERE p.idPiano=a.idPiano AND a.idArea=t.idArea AND t.idStato=s.idStato;