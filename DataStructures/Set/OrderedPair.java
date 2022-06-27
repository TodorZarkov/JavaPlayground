
package dataStructures;


import java.util.ArrayList;

/**
 * 
 * @author Todor Zarkov
 *
 * @param <E1>
 * @param <E2>
 */
public class OrderedPair<E1,E2> {
  private E1 first;
  private E2 second;
  
  public OrderedPair(E1 first, E2 second) {
    this.first = first;
    this.second = second;
  }
  
  public OrderedPair() {
    this.first = null;
    this.second = null;
  }

  public E1 getFirst() {
    return first;
  }

  public void setFirst(E1 first) {
    this.first = first;
  }

  public E2 getSecond() {
    return second;
  }

  public void setSecond(E2 second) {
    this.second = second;
  }
  
  public void set(E1 first, E2 second) {
    this.first = first;
    this.second = second;
  }
  
  public ArrayList<Object> get() {
    ArrayList<Object> result = new ArrayList<Object>(2);
    result.add(first);
    result.add(second);
    return result;
  }
  
  @Override
  public String toString() {
    String result;
    result = new String("(" + first + ", " + second + ") ");
    return result;
  }
}
