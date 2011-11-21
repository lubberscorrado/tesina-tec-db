INSERT INTO piano (numero,nome) VALUES (0,'Piano terra');
INSERT INTO piano (numero,nome) VALUES (1,'Primo piano');
INSERT INTO piano (numero,nome) VALUES (2,'Secondo piano');

INSERT INTO area (idPiano,nome,descrizione) VALUES (1,'A','Asderella');
INSERT INTO area (idPiano,nome,descrizione) VALUES (1,'B','Asderella');
INSERT INTO area (idPiano,nome,descrizione) VALUES (1,'C','Asderella');
INSERT INTO area (idPiano,nome,descrizione) VALUES (1,'D','Asderella');
INSERT INTO area (idPiano,nome,descrizione) VALUES (1,'E','Asderella');
INSERT INTO area (idPiano,nome,descrizione) VALUES (2,'F','Asderella');
INSERT INTO area (idPiano,nome,descrizione) VALUES (2,'G','Asderella');

INSERT INTO stato (idStato,nome,descrizione) VALUES (1,'Libero','Asderella');
INSERT INTO stato (idStato,nome,descrizione) VALUES (1,'Occupato','Asderella');
INSERT INTO stato (idStato,nome,descrizione) VALUES (2,'Da Pulire','Asderella');
INSERT INTO stato (idStato,nome,descrizione) VALUES (3,'Prenotato','Asderella');

INSERT INTO stato (idArea,idStato,nome,numPosti) VALUES (1,1,'T1',4);
INSERT INTO stato (idArea,idStato,nome,numPosti) VALUES (1,1,'T2',4);
INSERT INTO stato (idArea,idStato,nome,numPosti) VALUES (1,1,'T3',4);
INSERT INTO stato (idArea,idStato,nome,numPosti) VALUES (2,1,'T4',4);
INSERT INTO stato (idArea,idStato,nome,numPosti) VALUES (3,1,'T5',4);
INSERT INTO stato (idArea,idStato,nome,numPosti) VALUES (4,1,'T6',4);
INSERT INTO stato (idArea,idStato,nome,numPosti) VALUES (5,1,'T7',4);
