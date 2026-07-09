package ToolsPackage;

import GraphsPackage.Node;

import java.util.Comparator;

public class DegreeDecComparator implements Comparator<Node> {

    /**
     * Compares two nodes based on their degree in descending order. If they have the same degree, the node with the lowest ID is chosen.
     * @param o1 The first object to be compared.
     * @param o2 The second object to be compared.
     * @return -1 if o1 is before o2, 1 if o1 is after o2, 0 otherwise
     */
    @Override
    public int compare(Node o1, Node o2) {
        int diffDegre=o1.degree()-o2.degree();
        //plus grand degré en premier
        if(diffDegre>0)
            return -1;
        if(diffDegre<0)
            return 1;
        long res=o1.getId()-o2.getId();
        //plus petit indice en premier
        if (res<0) {
            return -1;
        }
        if (res>0) {
            return 1;
        }
        return 0;
    }
}