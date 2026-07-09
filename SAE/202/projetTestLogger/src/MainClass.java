import LoggingPackage.MyLogger;
import MesClasses.Personne;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass {

    public static void main(String[] args) {
        logger.log(Level.INFO,"Début du programme");
        Personne p1 = new Personne("Pierre", "Kiroule");
        Personne p2 = new Personne("Sarah", "Croche");
        logger.log(Level.INFO,"Fin du programme");

    }

    private static Logger logger = MyLogger.createLoger(MainClass.class.getName());
}
