
package dataStructures;


/**
 * Looped array queue.
 * 
 * @author Todor Zarkov
 * 
 */
public class ArrayQueue {
  private Object[] arr = null;
  private int count;
  private int head;
  private int tail;

  public ArrayQueue() {
    arr = new Object[16];
    count = 0;
    head = tail = 0;
  }

  public ArrayQueue(int length) {
    arr = new Object[length];
    count = 0;
    head = tail = 0;
  }

  
  
  // METHODS
  public void offer(Object item) {
    if (count == arr.length) {
      Object[] newArr = new Object[arr.length * 2];
      for (int i = head, j = 0; j < arr.length; i++, j++) {
        if (i == arr.length) i = 0;
        newArr[j] = arr[i];
      }
      head = 0;
      tail = arr.length - 1;
      arr = newArr;

      arr[tail + 1] = item;
      tail++;
      count++;
      return;
    } else if (count == 0) {
      arr[tail] = item;
      count++;
      return;
    }
    tail++;
    count++;
    if (tail == arr.length)
      tail = 0;

    arr[tail] = item;
    return;

  }

  
  /**
   * Returns and remove the head of the queue
   * 
   * @return the head Object item of the queue
   * @exception EmptyQueueException - unchecked exception. 
   * Throws it when trying to poll from an empty queue
   */
  public Object poll() {
    if (count == 0) {
      throw new EmptyQueueException("The queue is empty!!");
    }
    if (tail == head) {
      count--;
      return arr[head];
    }
    count--;
    head++;
    if (head == arr.length) {
      head = 0;
      return arr[arr.length - 1];
    }
    return arr[head - 1];
  }

  
  /**
   * Returns without removing the head of the queue
   * 
   * @return the head Object item of the queue
   * @exception EmptyQueueException - unchecked exception. 
   * Throws it when trying to poll from an empty queue
   */
  public Object peek() {
    if (count == 0) {
      throw new EmptyQueueException("The queue is empty!!");
    }
    return arr[head];
  }

  /**
   * Searches extremely slow.
   * TODO: OVERRIDE equals().
   * @param item
   * @return true if there is such an object, depends on equals().
   */
  public boolean contains(Object item) {
    if (count == 0) {
      return false;
    }
    for (int i = head, j = 0; j < arr.length; i++, j++) {
      if (i == arr.length)
        i = 0;

      if (((arr[i] != null) && (arr[i].equals(item)))
          || ((arr[i] == null) && (item == null))) {
        return true;
      }
    }
    return false;
  }

  
  public int size() {
    return this.count;
  }

  
  public void clear() {
    count = 0;
    head = tail;
  }

}
