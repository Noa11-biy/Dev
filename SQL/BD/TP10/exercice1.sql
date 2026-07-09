DROP TABLE IF EXISTS p2505279.villes,departements;

CREATE TABLE villes(
id varchar(6) PRIMARY KEY,
nom varchar(50));

CREATE TABLE  departements(
id varchar(3) PRIMARY KEY,
nom varchar(30),
chef_lieu varchar(6),
pop2010 INT,
CONSTRAINT FK_D_V FOREIGN KEY (chef_lieu) REFERENCES villes(id));

INSERT INTO villes (id,nom)
SELECT villes.id,villes.nom
FROM films.villes;

INSERT INTO departements (id,nom,pop2010,chef_lieu)
SELECT departements.id,departements.nom,SUM(v.pop2010),GROUP_CONCAT(IF(v.chef_lieu = 1,v.id,NULL))
FROM films.departements JOIN films.villes v
ON v.departement= departements.id
GROUP BY departements.id 