-- Exercice 1
/*
SELECT *
FROM  Seances NATURAL JOIN Modules 
WHERE nomModule = "Anglais et Informatique"
*/

-- Exercice 1.1
/*
SELECT*
FROM Seances
WHERE idModule = (SELECT idModule FROM Modules WHERE nomModule = "Anglais et Informatique");
*/

-- Exercice 1.2
/*
SELECT*
FROM Salles
WHERE effectifMax > (SELECT effectifMax FROM Salles WHERE idSalle ="Foyer RPF")
*/

-- Exercice 2
/*
SELECT DISTINCT(idSalle)
FROM Utilise
WHERE idSeance IN (SELECT idSeance FROM Seances WHERE MONTH(jour) = 6)
*/

-- Exercice 2.2
/*
SELECT*
FROM Seances 
WHERE idGroupe IN (SELECT idGroupe FROM Groupes WHERE effectif>25)
*/

-- Exercice 2.3
/*
SELECT DISTINCT idGroupe 
FROM Seances
WHERE idGroupe NOT IN (SELECT idGroupe FROM Seances WHERE idProf = "JFA")
*/

-- Exercice 2.4
/*
SELECT DISTINCT idProf
FROM Seances 
WHERE idProf <> "LMI" AND jour = ANY (SELECT Jour FROM Seances WHERE idProf = "LMI" AND MONTH(jour) = 11)
*/
-- Exercice 3.1










