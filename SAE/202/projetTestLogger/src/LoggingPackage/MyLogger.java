package LoggingPackage;

import java.io.IOException;
import java.util.logging.*;

public class MyLogger {

    // Handlers déclarés comme attributs static avec valeur initiale = null
    public static ConsoleHandler consoleHandler = null;
    public static FileHandler fileHandler = null;
    public static Logger logger;

    public static Logger createLoger(String className){
        logger = Logger.getLogger(className);
        logger.setLevel(Level.INFO);

        // On crée/ajoute les handlers via les méthodes dédiées
        createConsoleHandler();
        createFileHandler();
        consoleHandler.setFormatter(new MyFormatter());
        fileHandler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        return logger;
    }

    public static void createConsoleHandler(){
        if(consoleHandler == null){
            // Première fois : on crée le handler
            consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);
        } else {
            // Déjà créé : on l'ajoute directement au logger courant
            logger.addHandler(consoleHandler);
        }
    }

    public static void createFileHandler(){
        if(fileHandler == null){
            // Première fois : on crée le handler (un seul fichier)
            try {
                fileHandler = new FileHandler("monLog.log", false);
                fileHandler.setLevel(Level.ALL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.addHandler(fileHandler);
        } else {
            // Déjà créé : on le réutilise (même fichier pour tous les loggers)
            logger.addHandler(fileHandler);
        }
    }
}
