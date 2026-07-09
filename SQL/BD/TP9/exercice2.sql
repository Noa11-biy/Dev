DROP TABLE IF EXISTS TP2_synthese;

CREATE TABLE TP2_synthese(
id INT PRIMARY KEY,
nom varchar(100),
prenom varchar(100),
datenaissance DATE,
sexe ENUM('H','F'),
pere INT,
mere INT,
CONSTRAINT FK_TP2_TP2s FOREIGN KEY (pere) REFERENCES TP2_synthese(id),
CONSTRAINT FK_TP2_TP2s2 FOREIGN KEY (mere) REFERENCES TP2_synthese(id));

INSERT INTO TP2_synthese
SELECT*
