DROP TABLE IF EXISTS Projets_Collaborateurs;
DROP TABLE IF EXISTS Projets;
DROP TABLE IF EXISTS Collaborateurs;

CREATE TABLE Collaborateurs(
id INT primary key,
nom VARCHAR(20),
prenom VARCHAR(20),
superieur_hierarchique INT,
FOREIGN KEY (superieur_hierarchique) REFERENCES Collaborateurs(id));

CREATE TABLE Projets(
id INT primary key,
titre VARCHAR(20),
dateDebut DATE,
dateFin DATE,
chef_de_projet INT,
FOREIGN KEY (chef_de_projet) REFERENCES Collaborateurs(id));

CREATE TABLE Projets_Collaborateurs(
idprojet INT,
idcollab INT,
date DATE,
duree_passee TIME,
PRIMARY KEY (idprojet,idcollab,date),
FOREIGN KEY (idprojet) REFERENCES Projets(id),
FOREIGN KEY (idcollab) REFERENCES Collaborateurs(id));

INSERT INTO Collaborateurs VALUES (6,'LAVROV','Petre',NULL),
(5,'LAGSIR','Ada',6),(4,'NGUYEN','Kim',5),(3,'DUPONT','Marie',5),
(2,'MAJRI','Brahim',4), (1,'FRENE','Bastien',4);

INSERT INTO Projets VALUES (1,'Pluton','2023-09-15',NULL,NULL),
(3,'Saturne','2023-07-15',NULL,4),(2,'Uranus','2023-09-25','2023-11-27',5);

INSERT INTO Projets_Collaborateurs VALUES (1,1,'2023-10-05','2:30:00'),
(1,2,'2023-10-05','1:00:00'),(1,3,'2023-10-05','4:00:00'),(1,4,'2023-10-05','3:00:00'),
(1,2,'2023-10-06','2:00:00'),(2,1,'2023-10-06','5:00:00'),(2,5,'2023-10-06','3:00:00'),
(3,3,'2023-10-06','4:00:00'),(3,1,'2023-10-06','0:30:00'),(3,5,'2023-10-06','2:00:00');
















