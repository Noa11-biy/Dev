DROP TABLE IF EXISTS Projets_Collaborateurs,Projets,Collaborateurs;

CREATE TABLE Collaborateurs(
id INT PRIMARY KEY,
nom varchar(20),
prenom varchar(20),
superieur_hiearchique INT,
CONSTRAINT FK_C_C FOREIGN KEY (superieur_hiearchique) REFERENCES Collaborateurs(superieur_hiearchique));

CREATE TABLE Projets(
id INT AUTO_INCREMENT PRIMARY KEY,
titre varchar(20),
dateDebut DATE,
dateFin DATE DEFAULT NULL,
chef_de_projet INT,
CONSTRAINT FK_P_C FOREIGN KEY (chef_de_projet) REFERENCES Collaborateurs(superieur_hiearchique));

CREATE TABLE Projets_Collaborateurs(
idprojet INT,
idcollab INT,
date DATE,
duree_passee TIME,
PRIMARY KEY (idprojet,idcollab,date),
CONSTRAINT FK_PC_P FOREIGN KEY (idprojet) REFERENCES Projets(id),
CONSTRAINT FK_PC_C FOREIGN KEY (idcollab) REFERENCES Collaborateurs(id));

