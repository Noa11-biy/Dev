DROP TABLE IF EXISTS Utilise,Seances;

CREATE TABLE Seances(
idSeance INT PRIMARY KEY,
titreSeance varchar(100),
idModule varchar(6),
jour DATE,
heureDebut TIME,
heureFin TIME);

CREATE TABLE Utilise(
idSeance INT,
idSalle varchar(50),
PRIMARY KEY (idSeance,idSalle),
CONSTRAINT FK_U_S FOREIGN KEY (idSeance) REFERENCES Seances(idSeance));

INSERT INTO Seances
SELECT idSeance, titreSeance, idModule, jour,heureDebut,heureFin
FROM EDT.Seances
WHERE idProf = 'JBO';


INSERT INTO Utilise
SELECT *
FROM EDT.Utilise 
WHERE idSeance IN (SELECT idSeance FROM EDT.Seances WHERE idprof='JBO');
