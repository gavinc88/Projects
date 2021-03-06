/* WUGraph.java */

package graph;
import list.*;
import dict.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

    protected VertexList vList;
    protected HashTableChained hashV;
    protected HashTableChained hashE;
    protected int edgecount;

    /**
     * WUGraph() constructs a graph having no vertices or edges.
     *
     * Running time:  O(1).
     */
    public WUGraph(){
        vList = new VertexList();
        hashV = new HashTableChained();
        hashE = new HashTableChained();
        edgecount = 0;
    }

    /**
     * vertexCount() returns the number of vertices in the graph.
     *
     * Running time:  O(1).
     * @return number of vertices in the graph
     */
    public int vertexCount(){
        return vList.length();
    }

    /**
     * edgeCount() returns the number of edges in the graph.
     *
     * Running time:  O(1).
     * @return number of edges
     */
    public int edgeCount(){
        return edgecount;
    }

    /**
     * getVertices() returns an array containing all the objects that serve
     * as vertices of the graph.  The array's length is exactly equal to the
     * number of vertices.  If the graph has no vertices, the array has length
     * zero.
     *
     * (NOTE:  Do not return any internal data structure you use to represent
     * vertices!  Return only the same objects that were provided by the
     * calling application in calls to addVertex().)
     *
     * Running time:  O(|V|).
     * @return array of application representation of vertices
     */
    public Object[] getVertices(){
        Object[] v = new Object[vList.length()];
        try{
            VertexNode current = vList.front();
            for(int i = 0; i < vList.length(); i++){
                v[i] = current.item();
                current = current.next();
            }
        }catch(InvalidNodeException e){
            System.out.println(e);
        }
        return v;
    }

    /**
     * addVertex() adds a vertex (with no incident edges) to the graph.  The
     * vertex's "name" is the object provided as the parameter "vertex".
     * If this object is already a vertex of the graph, the graph is unchanged.
     *
     * Running time:  O(1).
     * @param application representation of a vertex
     */
    public void addVertex(Object vertex){
        if(hashV.find(vertex) == null){
            vList.insertBack(vertex);
            hashV.insert(vertex, vList.back());
        }
    }

    /**
     * removeVertex() removes a vertex from the graph.  All edges incident on the
     * deleted vertex are removed as well.  If the parameter "vertex" does not
     * represent a vertex of the graph, the graph is unchanged.
     *
     * Running time:  O(d), where d is the degree of "vertex".
     * @param application representation of a vertex
     */
    public void removeVertex(Object vertex){
        if(hashV.find(vertex) != null){
            Entry v = hashV.find(vertex);
            try{
                for(int i = ((EdgeList)((VertexNode)v.value()).getEdges()).length() - 1; i >= 0; i--){
                    EdgeNode currentEdgeNode = ((VertexNode)v.value()).getEdges().front();
                    removeEdge(currentEdgeNode.getV1(), currentEdgeNode.getV2());    
                }
                ((VertexNode)v.value()).remove();
                hashV.remove(vertex);
            }catch(InvalidNodeException e){
                System.out.println("removing invalid node");
            }
        }
    }

    /**
     * isVertex() returns true if the parameter "vertex" represents a vertex of
     * the graph.
     *
     * Running time:  O(1).
     * @param application representation of a vertex
     * @return true if parameter "vertex" is contained in the graph
     */
    public boolean isVertex(Object vertex){
        if(hashV.find(vertex) != null){
            return true;
        }
        return false;
    }

    /**
     * degree() returns the degree of a vertex.  Self-edges add only one to the
     * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
     * of the graph, zero is returned.
     *
     * Running time:  O(1).
     * @param application representation of a vertex
     * @return degree of the vertex
     */
    public int degree(Object vertex){
        if(hashV.find(vertex) != null){
            Entry v = hashV.find(vertex);
            int d = ((EdgeList)((VertexNode)v.value()).getEdges()).length();
            return d;
        }
        return 0;
    }

    /**
     * getNeighbors() returns a new Neighbors object referencing two arrays.  The
     * Neighbors.neighborList array contains each object that is connected to the
     * input object by an edge.  The Neighbors.weightList array contains the
     * weights of the corresponding edges.  The length of both arrays is equal to
     * the number of edges incident on the input vertex.  If the vertex has
     * degree zero, or if the parameter "vertex" does not represent a vertex of
     * the graph, null is returned (instead of a Neighbors object).
     *
     * The returned Neighbors object, and the two arrays, are both newly created.
     * No previously existing Neighbors object or array is changed.
     *
     * (NOTE:  In the neighborList array, do not return any internal data
     * structure you use to represent vertices!  Return only the same objects
     * that were provided by the calling application in calls to addVertex().)
     *
     * Running time:  O(d), where d is the degree of "vertex".
     * @param application representation of a vertex
     * @return Neighbors object containing two arrays; an array containing each object
     * connected by an edge to this vertex and an array containing the weights of
     * each edge in correct order.
     */
    public Neighbors getNeighbors(Object vertex){
        if(hashV.find(vertex) == null|| degree(vertex) == 0){
            return null;
        }
        else{
            Entry v = hashV.find(vertex);
            EdgeList edges = (EdgeList)((VertexNode)v.value()).getEdges();
            Neighbors n = new Neighbors();
            n.neighborList = new Object[edges.length()];
            n.weightList = new int[edges.length()];
            EdgeNode current = edges.front();
            for(int i = 0; i < edges.length(); i++){
                n.neighborList[i] = current.getV2();
                n.weightList[i] = current.getWeight();
                try {
                    current = current.next();
                } catch (InvalidNodeException e) {
                    e.printStackTrace();
                }
            }
            return n;
        }                
    }

    /**
     * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
     * u and v does not represent a vertex of the graph, the graph is unchanged.
     * The edge is assigned a weight of "weight".  If the edge is already
     * contained in the graph, the weight is updated to reflect the new value.
     * Self-edges (where u == v) are allowed.
     *
     * Running time:  O(1).
     * @param application representation of a vertex
     * @param application representation of another vertex
     * @param weight of the edge desired
     */
    public void addEdge(Object u, Object v, int weight){
        if(hashV.find(u) == null || hashV.find(v) == null){
            return;
        }
        VertexPair edge = new VertexPair(u, v);
        if(hashE.find(edge) == null){
            if(u.equals(v)){
                EdgeList uEdgeList = ((VertexNode)hashV.find(u).value()).getEdges();
                uEdgeList.insertBack(u, v, weight);
                hashE.insert(edge, uEdgeList.back());
                uEdgeList.back().setPartner(uEdgeList.back());
                edgecount++;
            }else{
                EdgeList uEdgeList = ((VertexNode)hashV.find(u).value()).getEdges();
                EdgeList vEdgeList = ((VertexNode)hashV.find(v).value()).getEdges();
                uEdgeList.insertBack(u, v, weight);
                vEdgeList.insertBack(v, u, weight);
                
                uEdgeList.back().setPartner(vEdgeList.back());
                vEdgeList.back().setPartner(uEdgeList.back());
                
                hashE.insert(edge, uEdgeList.back());

                edgecount++;
            }
        }
        else{
            try {
            	((EdgeNode)hashE.find(edge).value()).setWeight(weight);
                ((EdgeNode)hashE.find(edge).value()).getPartner().setWeight(weight);
            } catch (InvalidNodeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * removeEdge() removes an edge (u, v) from the graph.  If either of the
     * parameters u and v does not represent a vertex of the graph, the graph
     * is unchanged.  If (u, v) is not an edge of the graph, the graph is
     * unchanged.
     *
     * Running time:  O(1).
     * @param application representation of the first vertex in the edge
     * @param application representation of the second vertex in the edge
     */
    public void removeEdge(Object u, Object v){
        if(hashV.find(u) == null || hashV.find(v) == null){
            System.out.println("invalid vertices");
            return;
        }
        VertexPair edge = new VertexPair(u, v);
        
        if(hashE.find(edge) != null){
            if(u.equals(v)){
            	try {
                    ((EdgeNode)hashE.find(edge).value()).remove();
                } 
            	catch (InvalidNodeException e) {
                    e.printStackTrace();
                }
                hashE.remove(edge);
                edgecount--;
            }
            else{
            	try {
                    ((EdgeNode)hashE.find(edge).value()).getPartner().remove();
                    ((EdgeNode)hashE.find(edge).value()).remove();
                } catch (InvalidNodeException e) {
                    e.printStackTrace();
                }
                hashE.remove(edge);
                edgecount--;
            }
        }
    }

    /**
     * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
     * if (u, v) is not an edge (including the case where either of the
     * parameters u and v does not represent a vertex of the graph).
     *
     * Running time:  O(1).
     */
    public boolean isEdge(Object u, Object v){
        VertexPair edge = new VertexPair(u,v);
        if(hashE.find(edge) != null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
     * an edge (including the case where either of the parameters u and v does
     * not represent a vertex of the graph).
     *
     * (NOTE:  A well-behaved application should try to avoid calling this
     * method for an edge that is not in the graph, and should certainly not
     * treat the result as if it actually represents an edge with weight zero.
     * However, some sort of default response is necessary for missing edges,
     * so we return zero.  An exception would be more appropriate, but
     * also more annoying.)
     *
     * Running time:  O(1).
     * @param application representation of the first vertex in the edge
     * @param application representation of the second vertex in the edge
     * @return weight of the specified edge; returns 0 if edge does not exist
     */
    public int weight(Object u, Object v){
        VertexPair edge = new VertexPair(u,v);
        if(hashE.find(edge) == null){
            return 0;
        }
        return ((EdgeNode)hashE.find(edge).value()).getWeight();
    }

}
