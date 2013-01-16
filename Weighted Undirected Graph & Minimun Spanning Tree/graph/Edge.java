/** Edge.java **/

package graph;

public class Edge{
    
    protected Object vertex1;
    protected Object vertex2;
    protected int weight;

    public Edge(Object v1, Object v2, int w){
        vertex1 = v1;
        vertex2 = v2;
        weight = w;
    }

    public int getWeight(){
        return weight;
    }

}
