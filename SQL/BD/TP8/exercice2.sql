DROP TABLE IF EXISTS Lits,Chambres,Etages;

CREATE TABLE Etages(
id INT PRIMARY KEY,
nom varchar(100));

CREATE TABLE Chambres(
etage INT,
num INT,
nom varchar(100),
PRIMARY KEY(etage,num),
CONSTRAINT FK_C_E FOREIGN KEY(etage) REFERENCES Etages(id));

CREATE TABLE Lits(
id INT PRIMARY KEY,
etage INT,
chambre INT,
type ENUM("simple","médicalisé"),
CONSTRAINT FK_L_C FOREIGN KEY (etage,chambre) REFERENCES Chambres(etage,num));
