DROP TABLE IF EXISTS Notes;
DROP TABLE IF EXISTS Etudiants;
DROP TABLE IF EXISTS Suivre;
DROP TABLE IF EXISTS Groupes;
DROP TABLE IF EXISTS Matieres;

CREATE TABLE Matieres
( id INT AUTO_INCREMENT,
 nom VARCHAR(20),
 PRIMARY KEY (id)
);
CREATE TABLE Groupes
( id INT AUTO_INCREMENT,
 nom VARCHAR(20),
 PRIMARY KEY (id)
);

CREATE TABLE Suivre
( groupe INT,
 matiere INT,
 PRIMARY KEY (groupe, matiere),
 CONSTRAINT FK_S_G FOREIGN KEY (groupe) REFERENCES Groupes(id) ON UPDATE CASCADE -- i,
 CONSTRAINT FK_S_M FOREIGN KEY (matiere) REFERENCES Matieres(id)
);

CREATE TABLE Etudiants
( id INT,
 nom VARCHAR(20),
 prenom VARCHAR(20),
 groupe INT,
 PRIMARY KEY (id),
 CONSTRAINT FK_E_G FOREIGN KEY (groupe) REFERENCES Groupes(id) ON DELETE SET NULL -- c 
 ON update cascade -- g
);

CREATE TABLE Notes
( id INT AUTO_INCREMENT,
 etudiant INT not null, -- a
 matiere INT not null, -- a
 note FLOAT,
 PRIMARY KEY (id),
 CONSTRAINT FK_N_E FOREIGN KEY (etudiant) REFERENCES Etudiants(id),
 CONSTRAINT FK_N_M FOREIGN KEY (matiere) REFERENCES Matieres(id) ON delete cascade -- f
 on update restrict -- h 
);

-- b est impossible
-- d est impossible
-- e est impossible





