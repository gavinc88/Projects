/* EdgeNode.java */

package list;

public class EdgeNode extends ListNode{

    protected EdgeNode prev;
    protected EdgeNode next;
    protected Object vertex1;
    protected Object vertex2;
    protected int weight;
    protected EdgeNode partner;
    
    /**
     *  EdgeNode() constructor.
     *  @param v1 the first vertex.
     *  @param v2 the second vertex.
     *  @param w the desired weight.
     *  @param l the list that this node belongs to.
     *  @param p the node previous to this node.
     *  @param n the node following this node.
     */
    public EdgeNode(Object v1, Object v2, int w, EdgeList l, EdgeNode p, EdgeNode n){
    	vertex1 = v1;
    	vertex2 = v2;
        weight = w;
    	myList = l;
    	prev = p;
    	next = n;
    }

    /**
     *  getV1() returns the first vertex of this node; the
     *  first vertex should always be the vertex stored by
     *  this node's parent EdgeList's VertexList 
     *  
     *  @return the first vertex
     */
    public Object getV1(){
        return vertex1;
    }

    /**
     *  getV1() returns the second vertex of this node;
     *  
     *  @return the second vertex
     */
    public Object getV2(){
        return vertex2;
    }

    /**
     *  setPartner() sets this node's partner to the 
     *  EdgeNode inputed as a parameter.
     *  
     *  @param the twin of this EdgeNode
     */
    public void setPartner(EdgeNode twin){
    	this.partner = twin;
    }
    
    /**
     *  getWeight() gets this node's weight 
     *  
     *  @return the weight of this edge
     */
    public int getWeight(){
        return weight;
    }
    
    /**
     *  setWeight() sets this node's weight 
     *  
     *  @param the desired weight of this edge
     */
    public void setWeight(int w){
        weight = w;
    }

    /**
     *  next() returns the node following this node.  If this node is invalid,
     *  throws an exception.
     *
     *  @return the node following this node.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    public EdgeNode next() throws InvalidNodeException{
    	if(!isValidNode()){
            throw new InvalidNodeException("next() called on invalid node");
    	}
    	return next;
    }

    /**
     *  prev() returns the node preceding this node.  If this node is invalid,
     *  throws an exception.
     *
     *  @param node the node whose predecessor is sought.
     *  @return the node preceding this node.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    public EdgeNode prev() throws InvalidNodeException{
    	if(!isValidNode()){
            throw new InvalidNodeException("prev() called on invalid node");
    	}
    	return prev;
    }
    
    /**
     *  getPartner() returns this edge's twin partner 
     *  
     *  @return this edge's partner
     */
    public EdgeNode getPartner() throws InvalidNodeException{
    	if(!isValidNode()){
            throw new InvalidNodeException("getPartner() called on invalid node");
    	}
    	return partner;
    }

    /**
     *  insertAfter() inserts an item immediately following this node.  If this
     *  node is invalid, throws an exception.
     *
     *  @param item the item to be inserted.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    public void insertAfter(Object v1, Object v2, int w) throws InvalidNodeException{
    	if(!isValidNode()){
            throw new InvalidNodeException("insertAfter() called on invalid node");
    	}
    	EdgeNode nodeAfter = ((EdgeList)myList).newEdgeNode(v1, v2, w, ((EdgeList)myList), this, next);
    	next = nodeAfter;
    	next.next.prev = nodeAfter;
    	myList.size++;
    }

    /**
     *  This insertAfter() does nothing; required to fully implement
     *  the list abstract class
     **/
    public void insertAfter(Object item){}

    /**
     *  insertBefore() inserts an item immediately preceding this node.  If this
     *  node is invalid, throws an exception.
     *
     *  @param item the item to be inserted.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    public void insertBefore(Object v1, Object v2, int w) throws InvalidNodeException{
        if (!isValidNode()) {
            throw new InvalidNodeException("insertBefore() called on invalid node");
        }
        EdgeNode nodeBefore = ((EdgeList)myList).newEdgeNode(v1, v2, w, ((EdgeList)myList), prev, this);
        prev = nodeBefore;
        prev.prev.next = nodeBefore;
        myList.size++;
    }

    /**
     *  This insertBefore() does nothing; required to fully implement
     *  the list abstract class
     **/
    public void insertBefore(Object item){}

    /**
     *  remove() removes this node from its EdgeList.  If this node is invalid,
     *  throws an exception.
     *
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    public void remove() throws InvalidNodeException {
        if (!isValidNode()) {
            throw new InvalidNodeException("remove() called on invalid node");
        }
        next.prev = prev;
        prev.next = next;
        myList.size--;
        myList = null;
        next = null;
        prev = null;
    }

}
