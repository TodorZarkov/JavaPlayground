
package dataStructures;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.SortedSet;

/**
 * This class extends {@link java.util.PriorityQueue PriorityQueue}.
 * The only new implemented method is update().
 * @author TodorZarkov
 *
 * @param E - The elements that the heap contains.
 */
public class Heap<E> extends PriorityQueue<E> {
  
  private static final long serialVersionUID = 1L;
  
  
  public Heap() {
    super();
  }

  public Heap(int initialCapacity) {
    super(initialCapacity);
  }

  public Heap(Collection<? extends E> c) {
    super(c);
  }

  public Heap(PriorityQueue<? extends E> c) {
    super(c);
  }

  public Heap(SortedSet<? extends E> c) {
    super(c);
  }

  public Heap(int initialCapacity, Comparator<? super E> comparator) {
    super(initialCapacity, comparator);
  }
  
  
  /**
   * This will work in case that the 
   * {@linkplain java.util.PriorityQueue PriorityQueue}
   * removes elements considering only e1.equals(e2), and adds elements 
   * considering (e1.equals(e2) && e1.compareTo(e2) != 0).<br>
   * {@link java.lang.Object#equals(Object) equals()} must consider
   * only the reference equality, not the hash code equality 
   * or any stronger.
   * @param e
   * @return true if there is any update; false if the specified 
   * element hasn't 
   * been in the heap, moreover if false - the element is added 
   * within the heap. 
   */
  public boolean update(E e) {
    boolean result = this.remove(e);
    this.add(e);
    return result;
  }

}
