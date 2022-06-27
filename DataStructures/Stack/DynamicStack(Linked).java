
package dataStructures;


/**
 * Implements Linked Stack (Dynamic Stack)
 * @author Todor Zarkov
 *
 * @param <T>
 */
public class DynamicStack<T> {
  private class Node {
    Node prev;
    T item;

    Node(T item) {
      this.item = item;
      if (bottom != null) {
        this.prev = top;
        top = this;
      } else {
        this.prev = null;
        bottom = this;
        top = bottom;
      }
      count++;
    }
  }

  private Node bottom;
  private Node top;
  private int count;

  public DynamicStack() {
    this.bottom = null;
    this.top = null;
    this.count = 0;
  }

  // METHODS
  /**
   * Add an item after the top of the stack. After this the 
   * added item is the top of the stack.
   * 
   * @param item - the T which we want to add
   */
  public void push(T item) {
    new Node(item);
  }

  /**
   * Remove the top of the stack and returns it.
   * 
   * @return the top of the stack or null if bottom is reached.
   */
  public T pop() {
    if (count != 0) {
      T result = top.item;
      top = top.prev;
      count--;
      return result;
    } else {
      return null;
    }
  }

  /**
   * Search the parameter item in the stack. Search sequence begins 
   * from top to the bottom of the stack. First match is concerned 
   * as a success. 
   * @param item - T to be searched
   * @return true if item exist in the stack, false if tere's no 
   * such item
   */
  public boolean contains(T item) {
    Node currNode = top;
    Node nextNode = null;

    /*
     * The node-chain has sequence form bottom to top; the
     * next loop begins from the top, so nextNode is after currNode!!!
     */
    while (currNode != null) {
      if (((currNode.item != null) && (currNode.item.equals(item)))
          || (currNode.item == null) && (item == null)) {
        return true;
      }
      nextNode = currNode;
      currNode = nextNode.prev;
    }
    return false;
  }

  /**
   * Getting, without removing, the top of the stack.
   * 
   * @return the top element of the stack.
   */
  public T peek() {
    return top.item;
  }

  /**
   * Obtains the size of the stack (count of the elements in it)
   * 
   * @return the size
   */
  public int size() {
    return count;
  }

  /**
   * Removes the stack and all of its elements.
   */
  public void clear() {
    top.item = null;
    top.prev = null;
    bottom = null;

    count = 0;
  }

  /**
   * Convert the stack into an array. The zero element of the array 
   * is the top element of the stack.
   * 
   * @return Object array contained the stack.
   */
  public Object[] toArray() {
    
    Object[] result = new Object[count];
    int index = 0;
    Node currNode = top;
    Node nextNode = null;
    while (currNode != null) {

      result[index] = currNode.item;

      index++;
      nextNode = currNode;
      currNode = nextNode.prev;
      nextNode = null;
    }
    return result;
  }

}
