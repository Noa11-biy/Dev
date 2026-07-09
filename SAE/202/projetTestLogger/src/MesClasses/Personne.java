package MesClasses;

import LoggingPackage.MyLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Personne {
    private String prenom,nom;

    public Personne(String prenom, String nom) {
        this.prenom = prenom;
        this.nom = nom;
        logger.log(Level.INFO,"Personne crée : {0} {1}", new Object[]{prenom,nom});
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return prenom+" "+nom;
    }

    private static Logger logger = MyLogger.createLoger(Personne.class.getName());
}
