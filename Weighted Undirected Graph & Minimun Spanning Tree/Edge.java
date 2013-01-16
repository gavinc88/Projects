/* Edge.java */

/**
 * The Edge object represents an edge in a WUGraph. It is used in order to effectively
 * store two vertices and its weight. In addition, it contains a few methods in order
 * to maintain an Object that is not easily corruptable and to compare Edge objects.
 */

public class Edge implements Comparable {

    private Object v1;
    private Object v2;
    private int weight;

    /**
     * Constructor for an Edge Object.
     * @param vertex1 is the first vertex of the Edge.
     * @param vertex2 is the second vertex of the Edge.
     * @param w is the weight of the Edge.
     */
    public Edge(Object vertex1, Object vertex2, int w){
        v1 = vertex1;
        v2 = vertex2;
        weight = w;
    }
    
    /**
     * Returns the first vertex of the Edge.
     */
    public Object getV1(){
    	return v1;
    }
    
    /**
     * Returns the second vertex of the Edge.
     */
    public Object getV2(){
    	return v2;
    }
    
    /**
     * Returns the weight of the Edge.
     */
    public int getWeight(){
    	return weight;
    }

    /**
     * Compares the weight of 'this' Edge with an imput Edge.
     * @param e The (Edge) Object that you are comparing 'this' Edge with 
     * @return 0 if the weights are equal
     * @return 1 if 'this' Edge has a higher weight
     * @return -1 if 'this' Edge has a lower weight
     */
    public int compareTo(Object e){
        if(weight == ((Edge)e).weight){
            return 0;
        }
        else if(weight > ((Edge)e).weight){
            return 1;
        }
        else{
            return -1;
        }
    }
    
   /**
    * Compares two Edges to check if they are the same based off the veritices in each Edge.
    * @param e is an Edge object to compare 'this' Edge with
    * @return true if vertices consist of the same set
    * @return false if the vertices are a different set
    */
    public boolean isEqual(Edge e){
        if(v1.equals(e.v1) && v2.equals(e.v2)){
            return true;
        }
        else if(v1.equals(e.v2) && v2.equals(e.v1)){
            return true;
        }
        else{
            return false;
        }
    }
}
