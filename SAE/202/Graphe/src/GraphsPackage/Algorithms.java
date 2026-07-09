package GraphsPackage;

import LoggingPackage.MyLogger;
import ToolsPackage.DSATcomparator;
import ToolsPackage.DegreeDecComparator;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Algorithms {
    private static final Logger logger = MyLogger.createLoger(Algorithms.class.getName());

    public static Graph loadGraph(String file) {
        logger.log(Level.INFO, "Début loadGraph({0})", file);
        long debut = System.currentTimeMillis();
        try {
            Scanner scan = new Scanner(new File(file));
            String ligne = scan.nextLine();
            Graph g = new Graph(ligne);
            //les noeuds
            ligne = scan.nextLine();
            String[] t = ligne.split(",");
            for (String s : t) {
                g.addNode(Long.parseLong(s));
            }
            //les arêtes
            long id1, id2;
            while (scan.hasNext()) {
                id1 = id2 = -1;
                try {
                    ligne = scan.nextLine();
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Erreur de lecture : une ligne ne peut pas être lue");
                }
                try {
                    t = ligne.split(":");
                    id1 = Long.parseLong(t[0]);
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Erreur de lecture : le noeud origine ne peut pas être extrait de ({0})", t[0]);
                }
                try {
                    t = t[1].split(",");
                    for (String s : t) {
                        try {
                            id2 = Long.parseLong(s);
                            g.addEdge(id1, id2);
                        } catch (Exception ex) {
                            logger.log(Level.WARNING, "Erreur de lecture : le noeud destination ne peut pas être extrait de ({0})", ligne);
                        }
                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Erreur de lecture : le noeud origine ({0}) n'a pas de voisin dans le fichier", id1);
                }
            }
            long fin = System.currentTimeMillis();
            logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
            logger.log(Level.INFO, "Fin loadGraph({0})", file);
            return g;
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Erreur de lecture : fichier non présent - Exception : {0}", ex.getMessage());
            return null;
        }
    }

    public static Graph duplicate(Graph graph){
        if (graph == null) {logger.log(Level.WARNING, "Erreur, le graphe est null"); return null;}

        Graph graphDup = new Graph(graph.getName()+"_duplication");
        long debut = System.currentTimeMillis();
        logger.log(Level.INFO, "Début dupli({0}, {1})", graph.getName()+ " --> " + graphDup.getName());
        for(Node n : graph.getNodes()){
            graphDup.addNode(n.getId());
        }
        for(Node n : graph.getNodes()){
            for (Node nb : n.getNeighbors()){
                if(nb.getId()>n.getId()){
                    graphDup.addEdge(n.getId(),nb.getId());
                }
            }

        }
        long fin = System.currentTimeMillis();
        logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
        logger.log(Level.INFO, "Fin dupli({0}, {1})", graph.getName()+ " --> " + graphDup.getName());
        return graphDup;
    }

    public static boolean isAcyclic(Graph graph) {
        Graph ac = duplicate(graph);
        boolean suppresion = true;
        long debut = System.currentTimeMillis();
        logger.log(Level.INFO, "debut de la verification acyclique");

        while (suppresion){
            suppresion = false;
            for (Node nga : ac.getNodes()){
                if (nga.degree() <= 1){
                    ac.removeNode(nga.getId());
                    suppresion = true;
                    break;
                }
            }
        }
        if (ac.size() == 0){
            long fin = System.currentTimeMillis();
            logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
            logger.log(Level.INFO, "Fin de la verification acyclique");
            return true;
        }
        long fin = System.currentTimeMillis();
        logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
        logger.log(Level.INFO, "Fin de la verification acyclique");
        return false;
    }

    public static int nbComponents(Graph graph){
        if (graph == null) {logger.log(Level.WARNING, "Erreur, le graphe est null"); return -1;}
        long debut = System.currentTimeMillis();
        logger.log(Level.INFO, "debut de la verification acyclique");
        int cp = 0;

        List<Node> nodeNonV = new ArrayList<>(graph.getNodes());
        List<Node> dih = new ArrayList<>();

        while(!nodeNonV.isEmpty()){
            Node nodeChoisi = nodeNonV.remove(0);

            dih.add(nodeChoisi);
            while(!dih.isEmpty()){
                for(Node n :dih.remove(0).getNeighbors()){
                    if (nodeNonV.contains(n)){
                        nodeNonV.remove(n);
                        dih.add(n);
                    }
                }
            }
            cp++;
        }

        long fin = System.currentTimeMillis();
        logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
        logger.log(Level.INFO, "Fin de la verification acyclique");
        return cp;
    }


    public static Graph lineGraph(Graph graph){
        if (graph == null) {logger.log(Level.WARNING, "Erreur, le graphe est null"); return null;}
        long debut = System.currentTimeMillis();
        logger.log(Level.INFO, "debut création graphe arrête");

        if (graph.order() >= 100000 || graph.size() >= 100000){
            logger.log(Level.WARNING, "Le graphe est trop grand");
            long fin = System.currentTimeMillis();
            logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
            logger.log(Level.INFO, "Fin création graphe arrête");
            return null;
        }

        Graph g = new Graph("LineGraph(" + graph.getName() + ")");
        //Etape 1 : on récupère tous les noeuds du nouveau graphe
        long id1, id2, idCurrent;
        String tmp;
        Node current;
        HashMap<Long,ArrayList<Long>>incidentNodes=new HashMap<>();//clé=noeud de G, list=nouveaux noeuds auxquels il est incident
        for (Node n : graph.getNodes()) {
            incidentNodes.put(n.getId(),new ArrayList<>());
        }
        for (Node n : graph.getNodes()) {
            id1 = n.getId();
            for (Node voisin : n.getNeighbors()) {
                id2 = voisin.getId();
                if (id1 < id2) {
                    tmp = Long.toString(id1) + Long.toString(id2);
                    idCurrent=Long.parseLong(tmp);
                    g.addNode(idCurrent);
                    incidentNodes.get(id1).add(idCurrent);
                    incidentNodes.get(id2).add(idCurrent);
                }
            }
        }
        //Etape 2, on ajoute les arêtes du nouveau graphe
        ArrayList<Long>noeuds;
        int nb;
        for(Node n:graph.getNodes()){
            noeuds=incidentNodes.get(n.getId());
            nb=noeuds.size();
            for(int i=0;i<nb;i++){
                for(int j=i+1;j<nb;j++){
                    g.addEdge(noeuds.get(i),noeuds.get(j));
                }
            }
            noeuds.clear();
        }


        long fin = System.currentTimeMillis();
        logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
        logger.log(Level.INFO, "Fin création graphe arrête");
        return g;

    }

    /**
     * Computes a greedy minimum coloring of a graph. Each node will have a parameter called 'greedy' with its color.
     * @param graph The considered graph
     */
    public static void greedyColoring(Graph graph){
        logger.info("Start greedyColoring("+graph.getName()+")");
        long debut=System.currentTimeMillis();
        //on utilise un TreeSet pour avoir les noeuds triés par identifiant
        TreeSet<Node>aColorer=new TreeSet(graph.getNodes());
        //on utilise un TreeSet pour les couleurs car on a des recherches à faire dans les listes de couleurs
        TreeSet<Integer>couleurs=new TreeSet<>();
        for(Node n:aColorer){
            n.setParameter("greedy",0);
        }
        Node current;
        Integer couleur;
        while(aColorer.size()>0){
            current=aColorer.pollFirst(); //on prend le 1er noeud
            //on cherche toutes les couleurs voisines
            couleurs.clear();
            for(Node n:current.getNeighbors()) {
                couleur=n.getParameter("greedy");
                if(couleur!=null)
                    couleurs.add(couleur);
            }
            //on cherche la plus petite couleur absente du voisinage
            couleur=1;
            while(couleurs.contains(couleur)){
                couleur++;
            }
            //on la donne à current
            current.setParameter("greedy",couleur);
        }
        long fin=System.currentTimeMillis();
        logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
        logger.info("End greedyColoring("+graph.getName()+")");
    }

    /**
     * Computes a minimum coloring of a graph with Welsh-Powell's algorithm. Each node will have a parameter called 'wp' with its color.
     * @param graph The considered graph
     */
    public static void WelshPowellColoring(Graph graph){
        logger.info("Start WelshPowellColoring("+graph.getName()+")");
        long debut=System.currentTimeMillis();
        ArrayList<Node>aColorer=new ArrayList<>(graph.getNodes());
        //on utilise un TreeSet pour les couleurs car on a des recherches à faire dans les listes de couleurs
        TreeSet<Integer>couleurs=new TreeSet<>();
        for(Node n:aColorer){
            n.setParameter("wp",0);
        }
        //on trie les noeuds par leurs degrés décroissants
        Collections.sort(aColorer,new DegreeDecComparator());
        Node current;
        Integer couleur;
        while(aColorer.size()>0){
            current=aColorer.remove(0); //on prend le 1er noeud
            //on cherche toutes les couleurs voisines
            couleurs.clear();
            for(Node n:current.getNeighbors()) {
                couleur=n.getParameter("wp");
                if(couleur!=null)
                    couleurs.add(couleur);
            }
            //on cherche la plus petite couleur absente du voisinage
            couleur=1;
            while(couleurs.contains(couleur)){
                couleur++;
            }
            //on la donne à current
            current.setParameter("wp",couleur);
        }
        long fin=System.currentTimeMillis();
        logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
        logger.info("End WelshPowellColoring("+graph.getName()+")");
    }

    /**
     * Computes a minimum coloring of a graph with DSAT algorithm. Each node will have a parameter called 'dsat' with its color.
     * @param graph
     */
    public static void DSATcoloring(Graph graph){
        logger.info("Start DSATcoloring("+graph.getName()+")");
        long debut=System.currentTimeMillis();
        //on utilise un TreeSet pour les noeuds à colorer car le tri est implicite et est facilité pour le tri de qqs éléments seulement
        //ici ils sont triés via la méthode demandée
        TreeSet<Node>aColorer=new TreeSet<>(new DSATcomparator());
        //on utilise un TreeSet pour les couleurs car on a des recherches à faire dans les listes de couleurs
        TreeSet<Integer>couleurs=new TreeSet<>();
        //une map pour contenir les couleurs voisines de chaque noeud
        HashMap<Node,ArrayList<Integer>>couleursVoisines=new HashMap<>();
        for(Node n:graph.getNodes()){
            n.setParameter("dsat",0); //la couleur
            n.setParameter("degreDSAT",0); //degré dsat
            couleursVoisines.put(n,new ArrayList<>());
        }
        aColorer.addAll(graph.getNodes());
        Node current;
        Integer couleur;
        while(aColorer.size()>0){
            current=aColorer.pollFirst(); //on prend le 1er noeud
            //on cherche les couleurs voisines
            couleurs.clear();
            for(Node n:current.getNeighbors()) {
                couleur=n.getParameter("dsat");
                if(couleur!=null)
                    couleurs.add(couleur);
            }
            //on cherche la plus petite couleur absente du voisinage
            couleur=1;
            while(couleurs.contains(couleur)){
                couleur++;
            }
            //on la donne à current
            current.setParameter("dsat",couleur);
            //on met à jour les degreSAT des voisins
            for(Node n:current.getNeighbors()) { //pour chaque voisin
                if(!couleursVoisines.get(n).contains(couleur)) { //si cette couleur n'était pas dans son voisinage
                    couleursVoisines.get(n).add(couleur); //on l'ajoute
                    //on doit maintenant replacer ce noeud dans le TreeSet (car son degreSAT va changer)
                    //au lieu de tout retrier (opération couteuse), on fait 2 opérations de complexité O(log n)
                    if(aColorer.remove(n)) { //on retire le noeud n
                        n.setParameter("degreDSAT", couleursVoisines.get(n).size()); //on change son degreSAT
                        aColorer.add(n); //on réinsère n
                    }
                }
            }
        }
        long fin=System.currentTimeMillis();
        logger.log(Level.INFO, "Durée: {0} ms", (fin - debut));
        logger.info("End DSATcoloring("+graph.getName()+")");
    }

}
