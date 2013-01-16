/* EdgeList.java */

package list;

public class EdgeList extends List{

    protected EdgeNode head;
    /**
     *  newEdgeNode() creates a new edgeNode object and returns it.
     *  
     *	Performance:  runs in O(1) time.
     *
     *  @param v1 the first vertex
     *  @param v2 the second vertex
     *  @param w the desired weight
     *  @param list the EdgeList that the new edgeNode belongs to
     *  @param prev the previous edgeNode this edgeNode should reference
     *  @param next the next edgeNode this edgeNode should reference
     */
    public EdgeNode newEdgeNode(Object v1, Object v2, int w, EdgeList list, EdgeNode prev, EdgeNode next){
    	return new EdgeNode(v1, v2, w, list, prev, next);
    }

    /**
     *  EdgeList() constructs a new EdgeList.
     *  
     *	Performance:  runs in O(1) time.
     *
     */
    public EdgeList(){
    	head = newEdgeNode(null, null, 0, null, null, null);
    	head.next = head;
    	head.prev = head;
    	this.size = 0;
    }
    
    /**
     *  insertFront() inserts an item at the front of this EdgeList.
     *
     *  @param item is the item to be inserted.
     *
     *  Performance:  runs in O(1) time.
     **/
    public void insertFront(Object v1, Object v2, int w){
        EdgeNode front = newEdgeNode(v1, v2, w, this, head, head.next);
        head.next = front;
        head.next.next.prev = front;
        size++;
    }
    
    /**
     *  This insertFront() does nothing; required to fully implement
     *  the list abstract class
     **/
    public void insertFront(Object item){}

    
    /**
     *  insertBack() inserts an item at the back of this EdgeList.
     *
     *  @param item is the item to be inserted.
     *
     *  Performance:  runs in O(1) time.
     **/
    public void insertBack(Object v1, Object v2, int w){
        EdgeNode back = newEdgeNode(v1, v2, w, this, head.prev, head);
      	head.prev = back;
      	head.prev.prev.next = back;
      	size++;
    }
    /**
     *  This insertBack() does nothing; required to fully implement
     *  the list abstract class
     **/
    public void insertBack(Object item){}

    /**
     *  front() returns the node at the front of this EdgeList.  If the EdgeList is
     *  empty, return an "invalid" node--a node with the property that any
     *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
     *
     *
     *  @return an EdgeNode at the front of this EdgeList.
     *
     *  Performance:  runs in O(1) time.
     */
    public EdgeNode front(){
    	return head.next;
    }
    
    /**
     *  back() returns the node at the back of this EdgeList.  If the EdgeList is
     *  empty, return an "invalid" node--a node with the property that any
     *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
     *
     *  @return a EdgeNode at the back of this EdgeList.
     *
     *  Performance:  runs in O(1) time.
     */
    public EdgeNode back(){
    	return head.prev;
    }
    
    /**
     *  nth() returns the nth node of this EdgeList starting at the front.  If the ith
     *  node does not exist, return an invalid node.
     *  
     *  @return the ith EdgeNode.
     *
     *  Performance:  runs in O(n) time where n is the number of edge in this EdgeList.
     */
    public EdgeNode nth(int i){
    	if(i > this.length()){
    		return new EdgeNode(null,null,0,null, null, null);
    	}
        int counter = 0;
        EdgeNode current = front();
        while(counter < i){
            current = current.next;
            counter++;
        }
        return current;
    }
    
    /**
     *  toString() returns a String representation of this EdgeList.
     *
     *
     *  @return a String representation of this EdgeList.
     *
     *  Performance:  runs in O(n) time, where n is the length of the list.
     */
    public String toString() {
        String result = "[  ";
        EdgeNode current = head.next;
        while (current != head) {
            result = result + current.vertex1 + "  " + current.vertex2 + " ";
            current = current.next;
        }
        return result + "]";
    }

}
