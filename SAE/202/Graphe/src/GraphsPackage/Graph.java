package GraphsPackage;

import LoggingPackage.MyLogger;
import ToolsPackage.Encoding;

import java.util.*;
import java.util.logging.Logger;

public class Graph {

    // Logger de la classe
    private static final Logger logger = MyLogger.createLoger(Graph.class.getName());

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    // HashMap pour un accès O(1) aux noeuds via leur identifiant
    private HashMap<Long, Node> nodes;

    /**
     * Constructeur d'un graphe à partir de son nom.
     * @param name nom du graphe
     */
    public Graph(String name) {
        this.name = name;
        this.nodes = new HashMap<>();
        logger.info("Création du graphe \"" + name + "\"");
    }

    /**
     * @return le nom du graphe
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter sur le noeud d'identifiant donné.
     * @param nodeId identifiant du noeud
     * @return le noeud correspondant, ou null s'il n'existe pas
     */
    public Node getNode(long nodeId) {
        return this.nodes.get(nodeId);
    }

    /**
     * Retourne l'ensemble des noeuds du graphe sous forme non modifiable.
     * @return collection non modifiable des noeuds
     */
    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(this.nodes.values());
    }

    /**
     * Ajoute un noeud au graphe si aucun noeud n'a déjà cet identifiant.
     * @param nodeId identifiant du noeud à ajouter
     * @return true si l'ajout a été fait, false sinon
     */
    public boolean addNode(long nodeId) {
        if (this.nodes.containsKey(nodeId)) {
            logger.warning("Ajout impossible : le noeud " + nodeId + " existe déjà dans le graphe \"" + name + "\"");
            return false;
        }
        this.nodes.put(nodeId, new Node(nodeId));
        logger.fine("Noeud " + nodeId + " ajouté au graphe \"" + name + "\"");
        return true;
    }

    /**
     * Retire un noeud du graphe, et le retire des voisinages des autres noeuds.
     * @param nodeId identifiant du noeud à retirer
     * @return true si la suppression a été faite, false sinon
     */
    public boolean removeNode(long nodeId) {
        Node toRemove = this.nodes.remove(nodeId);
        if (toRemove == null) {
            logger.warning("Suppression impossible : le noeud " + nodeId + " n'existe pas dans le graphe \"" + name + "\"");
            return false;
        }
        // Retirer ce noeud des voisinages des autres noeuds
        for (Node n : this.nodes.values()) {
            n.removeNeighbor(toRemove);
        }
        logger.fine("Noeud " + nodeId + " retiré du graphe \"" + name + "\"");
        return true;
    }

    /**
     * Ajoute une arête entre les noeuds d'identifiants donnés.
     * @param nodeId1 identifiant du premier noeud
     * @param nodeId2 identifiant du second noeud
     * @return true si l'ajout a été fait, false sinon
     */
    public boolean addEdge(long nodeId1, long nodeId2) {
        Node n1 = this.nodes.get(nodeId1);
        Node n2 = this.nodes.get(nodeId2);
        if (n1 == null || n2 == null) {
            logger.warning("Ajout d'arête impossible : l'un des noeuds (" + nodeId1 + ", " + nodeId2
                    + ") n'existe pas dans le graphe \"" + name + "\"");
            return false;
        }
        n1.addNeighbor(n2);
        n2.addNeighbor(n1);
        logger.fine("Arête ajoutée entre " + nodeId1 + " et " + nodeId2);
        return true;
    }

    /**
     * @return l'ordre du graphe (nombre de sommets)
     */
    public int order() {
        return this.nodes.size();
    }

    /**
     * @return la taille du graphe (nombre d'arêtes)
     */
    public int size() {
        // Chaque arête est comptée deux fois (une dans chaque voisinage)
        int total = 0;
        for (Node n : this.nodes.values()) {
            total += n.degree();
        }
        return total / 2;
    }

    /**
     * @return le degré maximum du graphe (0 si le graphe est vide)
     */
    public int maximumDegree() {
        int max = 0;
        for (Node n : this.nodes.values()) {
            if (n.degree() > max) {
                max = n.degree();
            }
        }
        return max;
    }

    /**
     * @return le degré moyen du graphe (0 si le graphe est vide)
     */
    public double averageDegree() {
        if (this.nodes.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (Node n : this.nodes.values()) {
            total += n.degree();
        }
        return (double) total / this.nodes.size();
    }

    public String graphCode(){
        ArrayList<Node> list=new ArrayList(this.nodes.values());
        list.sort((a, b) -> Long.compare(a.getId(), b.getId()));
        StringBuilder sb=new StringBuilder("");
        sb.append(this.order());
        sb.append(this.size());
        ArrayList<Node> voisins;
        for(Node n:list){
            sb.append(n.getId());
            voisins=new ArrayList<>(n.getNeighbors());
            voisins.sort((a, b) -> Long.compare(a.getId(), b.getId()));
            for(Node n2:voisins)
                sb.append(n2.getId());
        }
        return Encoding.encode(sb.toString());
    }

    public String parameterCode(String parameterName){
        ArrayList<Node> list=new ArrayList(this.nodes.values());
        list.sort((a, b) -> Long.compare(a.getId(), b.getId()));
        StringBuilder sb=new StringBuilder("");
        for(Node n:list){
            sb.append(n.getId());
            sb.append(n.getParameter(parameterName));
        }
        return Encoding.encode(sb.toString());
    }

    @Override
    public String toString() {
        return "Graph[" + name + ", ordre=" + order() + ", taille=" + size() + "]";
    }
}
