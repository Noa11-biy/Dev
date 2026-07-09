package ToolsPackage;

import GraphsPackage.Node;

import java.util.Comparator;

public class DSATcomparator implements Comparator<Node> {
    /**
     * Compares two nodes based on their DSAT degree in ascending order. If they have the same DSAT degree, the node with the highest degree is chosen.
     * If they also have the same degree, the node with the smallest ID is chosen.
     * @param o1 The first object to be compared.
     * @param o2 The second object to be compared.
     * @return -1 if o1 is before o2, 1 if o1 is after o2, 0 otherwise
     */
    @Override
    public int compare(Node o1, Node o2) {
        int diffSat=0;
        try{
            diffSat=o1.getParameter("degreDSAT")-o2.getParameter("degreDSAT");
        }catch(NullPointerException nullPointerException) {
            return -1;
        }catch (Exception exception){}
        //plus grand dsat en premier
        if(diffSat>0)
            return -1;
        if(diffSat<0)
            return 1;
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
