CREATE TABLE SEANCES AS SELECT * FROM EDT.Seances;
CREATE TABLE PROFS AS SELECT * FROM EDT.Professeurs;
CREATE TABLE SEANCES2 AS SELECT idSeance,titreSeance,idModule,
CONCAT(UPPER(nomProf)," ",LOWER(prenomProf))n_p_pr, 
TIMESTAMP(jour,heureDebut) jour_heure_debut,
TIME_TO_SEC(TIMEDIFF(heureFin,heureDebut))/60 durée
FROM Seances NATURAL JOIN Profs



alter table SEANCES add PRIMARY KEY (idSeance);
alter table PROFS add PRIMARY KEY (idProf);
alter table SEANCES add FOREIGN KEY (idProf) REFERENCES PROFS(idProf);
alter table SEANCES2 add PRIMARY KEY (idSeance);
alter table SEANCE2 MODIFY n_p_pr varchar(90);
