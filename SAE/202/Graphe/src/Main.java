import GraphsPackage.*;

public class Main {
    private static String[] graphFiles={"files/G1-5-4.txt","files/G2-5-3.txt","files/G3-5-6.txt","files/G4-5-10.txt","files/G5-10-9.txt","files/G6-10-8.txt","files/G7-10-26.txt","files/G8-10-20.txt","files/G9-500-492.txt","files/G10-500-1000.txt","files/G11-1000-999.txt","files/G12-1000-249890.txt","files/G13-5000-4983.txt","files/G14-5000-10000.txt","files/G15-10000-9999.txt","files/G16-10000-20000.txt","files/G17-25000-24870.txt","files/G18-25000-30000.txt","files/G19-50000-49999.txt","files/G20-50000-60000.txt","files/G21-9-17.txt","files/G22-10-26.txt"};

    public static void main(String[] args) {
        Graph graph=null;
        for (int i = 0; i < 1; i++) {
            graph = Algorithms.loadGraph(graphFiles[i]);
            System.out.println(graph.getName()+" : order "+graph.order());
            System.out.println(graph.getName()+" : size "+graph.size());
            System.out.println(graph.getName()+" : max degree "+graph.maximumDegree());
            System.out.println(graph.getName()+" : avg dg "+graph.averageDegree());
            Graph gdup = Algorithms.duplicate(graph);

            System.out.println(gdup.getName()+" : order "+gdup.order());
            System.out.println(gdup.getName()+" : size "+gdup.size());
            System.out.println(gdup.getName()+" : max degree "+gdup.maximumDegree());
            System.out.println(gdup.getName()+" : avg dg "+gdup.averageDegree());



        }

        graph=Algorithms.loadGraph(graphFiles[0]);
        for(Node n:graph.getNodes()){
            n.setParameter("degree",n.degree());
        }
        System.out.println(graph.parameterCode("degree"));

    }

    public static Graph createPath(int order){
        Graph graph = new Graph("Path_"+order);
        for (int i = 1; i <= order ; i++){
            graph.addNode(i);
        }
        for (int i = 1; i <= order-1 ; i++){
            graph.addEdge(i,i+1);
        }

        return graph;
    }



    public static Graph createCycle(int order){
        Graph graph = createPath(order);

        if (order >= 2){
            graph.addEdge(order, 1);
        }
        graph.setName("Cycle_"+order);
        return graph;
    }


    public static Graph createCompleteGraph(int order){
        Graph graph = new Graph("Complet_"+order);
        for (int i = 1; i <= order ; i++){
            graph.addNode(i);
        }
        for (int i = 1; i <= order ; i++){
            for (int j = i+1; j <= order; j++) {
                graph.addEdge(i, j);
            }
        }

        return graph;
    }
}