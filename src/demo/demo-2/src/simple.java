import java.util.Scanner;
import java.util.Set;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.GraphMLExporter;

import LabelledNode.LabelledNode;
import NameProvider.NameProvider;

public class simple
{
	// Choose values for p and q based on study by Sultan, Farley, and Lehmann in 1990
 	static double p = 0.03; // Fixed coefficient of innovation
 	static double q = 0.38; // Fixed coefficient of imitation
 	static int noAdoptions = 0;
 	static int fileCount = 1;
 	
    public static void main(String [] args)
    {
    	 Scanner scanner = new Scanner(System.in);
         System.out.println("How many nodes?");
         int noNodes = scanner.nextInt();
         System.out.println("Insert edge probability:");
         double edgeProb = scanner.nextDouble();
         
    	//create a graph
        UndirectedGraph<LabelledNode, DefaultEdge> graph = initRandomGraph(noNodes, edgeProb);
        NeighborIndex<LabelledNode, DefaultEdge> ni = new NeighborIndex(graph);
        
        /* initially set some nodes to adopt by p
        initAdoption(graph);
     	try {
			export(graph);
		} catch (ExportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
     	
     	// Simple adoption
     	do {
     		consequentAdoption(graph,ni);
         	try {
         		//System.out.println(fileCount);
    			export(graph);
    		} catch (ExportException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
     		System.out.println("Total number of nodes adopted: " +noAdoptions);
     		//System.out.println("New number of nodes adopted (p): ");
     		//System.out.println("New number of nodes adopted (q): ");
     	} while (noAdoptions < noNodes);
     	
    }

    
    // method to create an initial graph for future manipulation
    private static UndirectedGraph<LabelledNode, DefaultEdge> initRandomGraph(int noNodes, double edgeProb)
    {
        UndirectedGraph<LabelledNode, DefaultEdge> g = new SimpleGraph<LabelledNode, DefaultEdge>(DefaultEdge.class);

        //add specified number of nodes
        for (int i=1; i<=noNodes; i++) {
        	LabelledNode node = new LabelledNode(Integer.toString(i), false);
        	g.addVertex(node);
        }
        
        //add x% of random edges
        Object[] nodes = g.vertexSet().toArray(); 
        for (int i=0; i<noNodes; i++) { 
        	for (int j=i+1; j<noNodes; j++) {
        		if (Math.random() < edgeProb) {
        			LabelledNode edgeNodeA = (LabelledNode) nodes[i];
        			LabelledNode edgeNodeB = (LabelledNode) nodes[j];
       				g.addEdge(edgeNodeA, edgeNodeB);
       				System.out.println("Added edge between " +((LabelledNode) edgeNodeA).getLabel()+ " and " +((LabelledNode) edgeNodeB).getLabel());
        		}
        	}
        }
        return g;
    }
    
    /* method to set p and q nodes adopted
    private static UndirectedGraph<LabelledNode, DefaultEdge> initAdoption(UndirectedGraph<LabelledNode, DefaultEdge> g) {
    	Object[] nodes = g.vertexSet().toArray(); 
    	for (Object node : nodes) {
    		if (Math.random() < p) {
				((LabelledNode) node).setAdoptedStatus(true);
				noAdoptions++;
				//System.out.println("p1Node " +((LabelledNode) node).getLabel()+ " has status " +((LabelledNode) node).getAdoptedStatus());
			}
    	}
    	
    	return g;
    }*/
    
    private static UndirectedGraph<LabelledNode, DefaultEdge> consequentAdoption(UndirectedGraph<LabelledNode, DefaultEdge> g, NeighborIndex<LabelledNode, DefaultEdge> ni) {
    	Object[] nodes = g.vertexSet().toArray(); 
    	for (Object node : nodes) {
    		// if node has adopted, getNeighbours of node
    		if (((LabelledNode) node).getAdoptedStatus()) {
    			Set<LabelledNode> neighbors = ni.neighborsOf((LabelledNode) node);
				// assign q% neighbours with adopted attribute
				for (Object neighbor : neighbors) {
					if (Math.random() < q && !((LabelledNode) node).getAdoptedStatus()) {
						((LabelledNode) neighbor).setAdoptedStatus(true); 
						noAdoptions++;
					}
				//System.out.println("qNode " +((LabelledNode) neighbor).getLabel()+ " has status " +((LabelledNode) neighbor).getAdoptedStatus());
				}
			}
    		// make a further p of the non-adopted nodes adopted
    		else if (Math.random() < p) { 
    			((LabelledNode) node).setAdoptedStatus(true);
    			noAdoptions++;
    		}
    		//System.out.println("p2Node " +((LabelledNode) node).getLabel()+ " has status " +((LabelledNode) node).getAdoptedStatus());
    	}
		return g;
	
    }   
    
    private static void export(UndirectedGraph<LabelledNode, DefaultEdge> g) throws ExportException {
    	try {
			File file = new File("iteration" +fileCount+ ".graphml");

			// if file doesn't exist, create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			NameProvider np = new NameProvider();
			GraphMLExporter<LabelledNode, DefaultEdge> exporter = new GraphMLExporter<LabelledNode, DefaultEdge>();
	        exporter.setVertexLabelProvider(np);
	        exporter.exportGraph(g, fw);
			fileCount++;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

