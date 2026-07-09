-- Exercice 1
/*
SELECT nom, matiere,coloris,typ, prixdebase+surcout+coutexpedition"prix total"
FROM Modeles,Versions,Acheminements
WHERE prixdebase+surcout+coutexpedition<=39.99
*/

-- Exercice 2
/*
SELECT CONCAT("[",E.nom,",",F.nom,"]")'segment',
ROUND(SQRT(POW(E.abscisse-F.abscisse,2)+POW(E.ordonnee-F.ordonnee,2)),1)'longueur'
FROM E,F
ORDER BY 2 ASC
LIMIT 4
*/
-- Exercice 3
-- 1) 2450*2621 = 6421450
-- 2) la ligne n°2622 vaudrait 74 PTUT 17 IN0.09

-- Exercice 4
/*
SELECT idSeance,titreSeance,idModule,nomProf,prenomProf,idGroupe,jour,heureDebut,heurefin
FROM Seances, Professeurs
WHERE Seances.idProf = Professeurs.idProf 
*/
-- Exercice 4.2
SELECT *
FROM Seances, Utilise
WHERE Seances.idSeance = Utilise.idSeance













