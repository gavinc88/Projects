/* VertexList.java */

package list;

public class VertexList extends List{

    protected VertexNode head;

    /**
     *  newNode() creates a new VertexNode object and returns it.
     *  
     *	Performance:  runs in O(1) time.
     *
     *  @param vertex the application representation of the vertex
     *  @param list the VertexList that the new VertexNode belongs to
     *  @param prev the previous VertexNode this VertexNode should reference
     *  @param next the next VertexNode this VertexNode should reference
     */
    public VertexNode newNode(Object vertex, VertexList list, VertexNode prev, VertexNode next){
    	return new VertexNode(vertex, list, prev, next);
    }
    
    /**
     *  VertexList() constructs a new VertexList.
     *  
     *	Performance:  runs in O(1) time.
     *
     */
    public VertexList(){
    	head = newNode(null, null, null, null);
    	head.next = head;
    	head.prev = head;
    	this.size = 0;
    }

    /**
     *  insertFront() inserts an item at the front of this VertexList.
     *
     *  @param item is the item to be inserted.
     *
     *  Performance:  runs in O(1) time.
     **/
    public void insertFront(Object vertex){
        VertexNode front = newNode(vertex, this, head, head.next);
        head.next = front;
        head.next.next.prev = front;
        size++;
    }

    /**
     *  insertBack() inserts an item at the back of this VertexList.
     *
     *  @param item is the item to be inserted.
     *
     *  Performance:  runs in O(1) time.
     **/
    public void insertBack(Object vertex){
        VertexNode back = newNode(vertex, this, head.prev, head);
      	head.prev = back;
      	head.prev.prev.next = back;
      	size++;
    }

    /**
     *  This insertFront() does nothing; required to fully implement
     *  the list abstract class
     **/
    
    public VertexNode front(){
    	return head.next;
    }

    /**
     *  This insertBack() does nothing; required to fully implement
     *  the list abstract class
     **/
    public VertexNode back(){
    	return head.prev;
    }
 
    /**
     *  nth() returns the nth node of this EdgeList starting at the front.  If the ith
     *  node does not exist, return an invalid node.
     *  
     *  @return the ith EdgeNode.
     *
     *  Performance:  runs in O(V) time where V is the size of the VertexList.
     */
    public VertexNode nth(int i){
    	if(i > this.length()){
    		return new VertexNode(null,null,null, null);
    	}
        int counter = 0;
        VertexNode current = head.next;
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
        VertexNode current = head.next;
        while (current != head) {
            result = result + current.item + "  ";
            current = current.next;
        }
        return result + "]";
    }

}
