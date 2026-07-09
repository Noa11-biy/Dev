package GraphsPackage;

import LoggingPackage.MyLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;

public class Node {

    // Logger de la classe
    private static final Logger logger = MyLogger.createLoger(Node.class.getName());

    private long id;
    private HashMap<String, Integer> parameters;
    private ArrayList<Node> neighbors;

    /**
     * Constructeur d'un noeud à partir de son identifiant.
     * @param id identifiant du noeud
     */
    public Node(long id) {
        this.id = id;
        this.parameters = new HashMap<>();
        this.neighbors = new ArrayList<>();
        logger.fine("Création du noeud " + id);
    }

    /**
     * @return l'identifiant du noeud
     */
    public long getId() {
        return this.id;
    }

    /**
     * Ajoute (ou remplace) un paramètre dans la HashMap des paramètres.
     * @param name nom du paramètre
     * @param value valeur associée
     */
    public void setParameter(String name, int value) {
        this.parameters.put(name, value);
    }

    /**
     * Getter sur un paramètre.
     * @param name nom du paramètre recherché
     * @return la valeur du paramètre, ou null s'il n'existe pas
     */
    public Integer getParameter(String name) {
        return this.parameters.get(name);
    }

    /**
     * Ajoute un voisin au noeud.
     * @param n noeud voisin à ajouter
     */
    public void addNeighbor(Node n) {
        if (n == null) {
            logger.warning("Tentative d'ajout d'un voisin null au noeud " + this.id);
            return;
        }
        if (!this.neighbors.contains(n)) {
            this.neighbors.add(n);
        } else {
            logger.fine("Le noeud " + n.getId() + " est déjà voisin du noeud " + this.id);
        }
    }

    /**
     * Retire un voisin au noeud.
     * @param n noeud voisin à retirer
     */
    public void removeNeighbor(Node n) {
        if (!this.neighbors.remove(n)) {
            logger.fine("Le noeud " + (n == null ? "null" : n.getId()) + " n'est pas voisin du noeud " + this.id);
        }
    }

    /**
     * @return le degré du noeud (nombre de voisins)
     */
    public int degree() {
        return this.neighbors.size();
    }

    /**
     * Retourne la liste des voisins sous forme non modifiable
     * afin de respecter le principe d'encapsulation.
     * @return collection non modifiable des voisins
     */
    public Collection<Node> getNeighbors() {
        return Collections.unmodifiableList(this.neighbors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return this.id == node.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }

    @Override
    public String toString() {
        return "Node[" + this.id + "]";
    }
}
