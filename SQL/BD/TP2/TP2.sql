-- Exercice 1.1
/*
 SELECT UPPER(nomProf) "nom", LOWER(prenomProf) "prenom" 
 FROM Professeurs
 */

-- Exercice 1.2
/*
SELECT *
FROM Modules
WHERE lower(nomModule) LIKE '%base%'
*/

-- Exercice 1.3
/*
SELECT idmodule , LEFT(nomModule, 4) "nompartiel"
FROM Modules 
*/

-- Exercice 1.4
/*
SELECT idModule "id", REPLACE(nomModule, 'à','a') "nom"
FROM Modules 
*/

-- Exercice 1.5
/*
SELECT nomProf, prenomProf, CONCAT(lower(prenomProf) , ".",lower(nomProf) ,"@univ-lyon1.fr") "mail"
FROM Professeurs 
*/
 -- Exercice 1.6
-- 1) 
/*
SELECT idProf, jour   
FROM  Seances 
Where idProf Like 'JBO' AND DAYNAME(jour) ="FRIDAY"
*/
-- 2)
/*
SELECT idProf, jour   
FROM  Seances 
Where idProf Like 'JBO' AND WEEKDAY(jour) ="4"
*/
-- 3) 
/*
SELECT idProf, jour   
FROM  Seances 
Where idProf Like 'JBO' AND DAYOFWEEK(jour) ="FRIDAY"
*/

-- Exercice 1.7
/*
SELECT * 
FROM Seances
WHERE HOUR(heurefin)='18:00:00'
*/

-- Exercice 1.8

SELECT idSeance, idProf, jour, heureDebut, heureFin, TIMEDIFF(heureFin, heureDebut) "Durée"
FROM Seances  
WHERE idProf LIKE 'JBO' AND 'Durée'>='01:45:00'














