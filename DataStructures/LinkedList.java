
package dataStructures;

public class LinkedList {
  
  private class Node {
    Node next;
    Node prev;
    Object item;

    /*
     * To be used in "insert" and "add" situations ;this constructor 
     * inserts after prevNode argument;
     */
    Node(Object item, Node prevNode) {
      this.item = item;
      this.next = prevNode.next;
      prevNode.next = this;
      this.prev = prevNode;
      if (prevNode == tail) {
        tail = this;
      } else {
        this.next.prev = this;
      }

      count++;
    }

    // To be used only when tail == head == null
    // or when we add item at the head
    Node(Object item) {
      this.item = item;
      this.next = null;
      this.prev = null;

      count++;
    }
  }

  private Node head;
  private Node tail;
  private int count;

  public LinkedList() {
    this.head = null;
    this.tail = null;
    this.count = 0;
  }

  
  // METHODS
  /**
   * Adding the item after the tall of the list
   * 
   * @param item - the Object you want to insert
   */
  public void add(Object item) {
    if (head != null) {
      new Node(item, tail);
    } else {
      head = new Node(item);
      ;
      tail = head;
    }
  }

  /**
   * Insert/Add item into the list on the specific index
   * 
   * @param index - ONto which you want to insert the item; 
   * the old index goes after the new one; also we can insert 
   * on the index size(), it is the same as add(item).
   * @param item - the object you want to insert
   * @exception IndexOutOfBoundsException - when index is invalid
   */
  public void add(int index, Object item) {
    if ((index < 0) || (index > count)) {
      throw new IndexOutOfBoundsException("Invalid index: " + index);
    }

    // if we want to insert at the head
    if (index == 0) {
      Node newNode = new Node(item);
      if (head == null) {
        head = newNode;
        tail = head;
        return;
      }
      newNode.next = head;
      head.prev = newNode;
      head = newNode;
      return;
    }

    // If we want to insert after the tail;
    // same as add(item).
    if (index == count) {
      new Node(item, tail);
      return;
    }

    // Consider which is the nearest end,
    // and counting the corresponding node.
    Node prevNode, currNode;
    if (index <= count / 2) {
      prevNode = null;
      currNode = head;
      int currIndex = 0;
      while (currIndex < index) {
        prevNode = currNode;
        currNode = prevNode.next;
        currIndex++;
      }
    } else {
      prevNode = tail;
      currNode = null;
      int currIndex = count - 1;
      while (currIndex != index - 1) {
        currNode = prevNode;
        prevNode = currNode.prev;
        currIndex--;
      }
    }

    // The actual inserting
    new Node(item, prevNode);
  }

  /**
   * Removes and returns the element on the specific index
   * 
   * @param index - the index of the element which we want 
   * to remove
   * @return the removed element
   * @exception IndexOutOfBoundsException
   *                - if the index is invalid
   */
  public Object remove(int index) {
    if ((index < 0) || (index > count - 1)) {
      throw new IndexOutOfBoundsException(
          "Invalid index: " + index);
    }

    // if we want to remove the head
    if (index == 0) {
      Node removed = head;
      if (head != tail) {
        head.next.prev = null;
      }
      head = head.next;
      count--;
      return removed;
    }

    // If we want to remove the tail;
    if (index == count - 1) {
      Node removed = tail;
      if (tail != head) {
        tail.prev.next = null;
      }
      tail = tail.prev;
      count--;
      return removed;
    }

    // Consider which is the nearest end,
    // and counting the corresponding node.
    Node prevNode, currNode;
    if (index <= count / 2) {
      prevNode = null;
      currNode = head;
      int currIndex = 0;
      while (currIndex < index) {
        prevNode = currNode;
        currNode = prevNode.next;
        currIndex++;
      }
    } else {
      prevNode = tail;
      currNode = null;
      int currIndex = count - 1;
      while (currIndex != index - 1) {
        currNode = prevNode;
        prevNode = currNode.prev;
        currIndex--;
      }
    }
    // Remove the inner node
    prevNode.next = currNode.next;
    currNode.next.prev = prevNode;
    count--;
    return currNode;
  }

  
  /**
   * Removes the first founded item
   * 
   * @param item to be removed
   * @return the index of the removed item or -1 if there 
   * is no item found
   */
  public int remove(Object item) {

    int currIndex = 0;
    Node prevNode = null;
    Node currNode = head;
    while (currNode != null) {
      if (((currNode.item != null) && (currNode.item.equals(item)))
          || (currNode.item == null) && (item == null)) {
        break;
      }
      prevNode = currNode;
      currNode = prevNode.next;
      currIndex++;
    }

    if (currNode != null) {
      // Element is found. Remove it!
      count--;
      if (count == 0) {

        head = tail = null;

      } else if (currNode == head) {
        currNode.next.prev = null;
        head = currNode.next;
      } else if (currNode == tail) {
        currNode.prev.next = null;
        tail = currNode.prev;
      } else {
        prevNode.next = currNode.next;
        currNode.next.prev = prevNode;
      }
      return currIndex;
    } else {
      return -1;
    }
  }

  
  /**
   * Search for item in the list and returns the corresponding 
   * index or -1 if no found element. 
   * @param item - the item to search for.
   * @return the index of the found item or -1 if there's no such 
   * item in the list
   */
  public int indexOf(Object item) {
    int currIndex = 0;
    Node prevNode = null;
    Node currNode = head;
    while (currNode != null) {
      if (((currNode.item != null) && (currNode.item.equals(item)))
          || (currNode.item == null) && (item == null)) {
        break;
      }
      prevNode = currNode;
      currNode = prevNode.next;
      currIndex++;
    }

    if (currNode != null) {
      // Element is found. Return the index.
      return currIndex;
    } else {
      return -1;
    }
  }

  
  /**
   * Obtains the corresponding element at index and return it.
   * 
   * @param index - the index of which element we want.
   * @return the element behind the index
   */
  public Object get(int index) {
    if ((index < 0) || (index > count - 1)) {
      throw new IndexOutOfBoundsException("Invalid index: " + index);
    }

    // Consider which is the nearest end,
    // and counting the corresponding node.
    Node prevNode, currNode;
    if (index <= count / 2) {
      prevNode = null;
      currNode = head;
      int currIndex = 0;
      while (currIndex < index) {
        prevNode = currNode;
        currNode = prevNode.next;
        currIndex++;
      }
    } else {
      prevNode = tail;
      currNode = null;
      int currIndex = count - 1;
      while (currIndex != index - 1) {
        currNode = prevNode;
        prevNode = currNode.prev;
        currIndex--;
      }
    }
    return currNode.item;
  }

  
  /**
   * Convert this List into array of type Object[]. You must 
   * convert the returned type to the desired one. The array 
   * is in proper order - [0]-zero element, [1]-first element 
   * and so on... 
   * @return the array representing the list
   */
  public Object[] toArray() {
    Object[] result = new Object[this.count];
    Node currNode = head;
    Node prevNode = null;
    for (int i = 0; i < count; i++) {
      result[i] = currNode.item;
      prevNode = currNode;
      currNode = prevNode.next;
    }
    return result;
  }

  
  /**
   * Obtains the size of the list
   * 
   * @return the size of the list
   */
  public int size() {
    return count;
  }

  
  /**
   * Clearing the list. After this zero elements are remaining.
   */
  public void clear() {
    tail = head = null;
    count = 0;
  }
}
