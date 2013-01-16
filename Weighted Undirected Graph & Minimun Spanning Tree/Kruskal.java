/* Kruskal.java */

import graph.*;
import set.*;
import dict.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

    /**
     * minSpanTree() returns a WUGraph that represents the minimum spanning tree
     * of the WUGraph g.  The original WUGraph g is NOT changed.
     * @param g is a Weighted Undirected Graph
     * @return T is the minimum spanning tree
     */
    public static WUGraph minSpanTree(WUGraph g){
        //initializing new separate graph
    	WUGraph T = new WUGraph();

        //Add all the vertices from the original graph to the one returned [1]
        Object[] verts = g.getVertices();
        for (int i = 0; i < verts.length; i++){
            T.addVertex(verts[i]);
        }
        //Data structure for edges
        
        Neighbors[] neighbors = new Neighbors[verts.length];
        Edge[] edge = new Edge[g.edgeCount()];
        
        int counter = 0;
        for(int i = 0; i < verts.length; i++){

            // can be removed if efficiency in storage is needed
            neighbors[i] = g.getNeighbors(verts[i]);

            //Getting all other vertices connected to this vertex & its weight
            Object[] verts2 = neighbors[i].neighborList;
            int[] weight = neighbors[i].weightList;

            //Creates all of the edges and puts them into the array
            for (int j = 0; j < verts2.length; j++){
            	boolean duplicateEdge = false;
            	Edge e = new Edge(verts[i], verts2[j], weight[j]);
            	for(int k = 0; k < counter; k++){
                    if(edge[k].isEqual(e)){
                        duplicateEdge = true;
                    }
            	}
            	if(duplicateEdge == false){
                    edge[counter] = e;
                    counter++;
            	}
            }
        }
        
        //sorts the array according to edge weight
        quicksort((Comparable[])edge, 0, edge.length - 1);
        
        //Hashtable to store
        HashTableChained vertHash = new HashTableChained(verts.length);

        //Hashing vertex to table and representing it with an integer i
        for(int i = 0; i < verts.length; i++){
            vertHash.insert(verts[i], i);
        }

        //Use disjointSets in order to keep track of linked vertices
        DisjointSets vSet = new DisjointSets(verts.length);

        int index = 0;
        while(T.edgeCount() < T.vertexCount() - 1){

            //Must use integers for disjoint sets, uses hash table to find these
            //edge (Array of Edges) should be in sorted order, so it's safe to do this
            Object vert1 = edge[index].getV1();
            Object vert2 = edge[index].getV2();
            int weight = edge[index].getWeight();

            int v1 = (Integer)vertHash.find(vert1).value();
            int v2 = (Integer)vertHash.find(vert2).value();

            //Uses disjoint sets find()
            int x1 = vSet.find(v1);
            int x2 = vSet.find(v2);

            //If the two are not connected already, add the edge
            if(x1 != x2){
                T.addEdge(vert1, vert2, weight);
                vSet.union(x1, x2);
            }

            //increment to the next edge
            index++;
        }
        return T;
    }

    /**
     *Sorting algorithm to arrange Edges based off of weight using most of the Quicksort code from lecture
     *@param a is any array of Comparable objects utilizing the Comparable interface
     *@param low is the low end
     *@param high is the high end
     **/       
    public static void quicksort(Comparable[] a, int low, int high) {
        // If there's fewer than two items, do nothing.
        if (low < high) {
            int pivotIndex = low + (high - low) / 2;
            Comparable pivot = a[pivotIndex];
            a[pivotIndex] = a[high];                       // Swap pivot with last item
            a[high] = pivot;
            
            int i = low - 1;
            int j = high;
            do {
                do { i++; } while (a[i].compareTo(pivot) < 0);
                do { j--; } while ((a[j].compareTo(pivot) > 0) && (j > low));
                if (i < j) {
                    Comparable temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            } while (i < j);
            
            a[high] = a[i];
            a[i] = pivot;                   // Put pivot in the middle where it belongs
            quicksort(a, low, i - 1);                     // Recursively sort left list
            quicksort(a, i + 1, high);                   // Recursively sort right list
        }
    }
}
