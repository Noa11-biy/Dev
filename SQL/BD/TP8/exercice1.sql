DROP TABLE IF EXISTS Elections,Circonscriptions,Departements;

CREATE TABLE Departements(
id INT PRIMARY KEY,
nom varchar(100));

CREATE TABLE Circonscriptions(
numdept INT,
numcirc INT,
nom varchar(100),
PRIMARY KEY(numdept,numcirc),
CONSTRAINT FK_C_D FOREIGN KEY (numdept) REFERENCES Departements(id));

CREATE TABLE Elections(
dept INT,
circ INT,
date DATE,
tour INT,
nbelecteurs INT,
PRIMARY KEY(dept,circ,date),
CONSTRAINT FK_E_C FOREIGN KEY(dept,circ) REFERENCES Circonscriptions(numdept,numcirc))