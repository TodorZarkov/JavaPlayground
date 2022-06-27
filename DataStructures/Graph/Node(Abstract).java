
package dataStructures;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * 
 * @author Todor Zarkov
 *
 * @param <T> - the type of the name of the node
 */
public abstract class AbstractNode<T> 
    implements Comparator<Node<T>>, Comparable<Node<T>> {
  
  /**
   * Constructs new node. The new node isn't related to any other node nor
   * Graph. The new node type T concerns the name of the node, i.e. the name
   * of the node is of type T.
   * @param name - the new node name. Must not be empty string or null.
   * @throws NullPointerException if the specified name is null nameType.
   * @throws IllegalArgumentException if the specified name is empty string.
   */
  public AbstractNode(T name) {};
  
  /**
   * @returns Infinity if the value hasn't been set yet.
   */
  public abstract Float getValue();
  
  
  /**
   * @param value must be positive.
   * @throws IllegalArgumentException if (value < 0).
   */
  public abstract void setValue(Float value);
  
  
  public abstract boolean isVisited();
  
  
  public abstract void setVisited(boolean visited);
  
  
  public abstract AbstractNode<T> getPrevious();
  
  
  public abstract void setPrevious(AbstractNode<T> previous);
  
  
  public abstract T getName();
  
  
  public abstract HashSet<AbstractNode<T>> getNeighbours();
  
  
  /**
   * @param toINode
   * @return the weight of the edge between this node and the specified
   * one. The edge(if exist) has an orientation from this node to the
   * specified one.
   * @throws NullPointerException if the argument is null. 
   * @throws NoSuchElementException if the argument isn't a neighbor 
   * of this node, i.e. the edge does not exist.
   */
  public abstract Float getWeight(AbstractNode<T> toINode);
  
  
  /**
   * Sets new neighbor. If exist - overrides it, more precisely 
   * overrides the weight.
   * @param neighbor - the neighbor node we desire to add.
   * @param weight - the weight between the two nodes. It must
   * be positive.
   * @return false if the specified neighbor already exist, 
   * otherwise true.
   * @throws NullPointerException if the specified neighbor is null.
   * @throws IllegalArgumentException if the specified weight is negative.
   */
  public abstract void setNeighbor(AbstractNode<T> neighbor, Float weight);
  
  
  /**
   * @param neighbor - the neighbor we want to delete.
   * @return If the specified neighbor exist returns true, otherwise - false.
   */
  public abstract boolean deleteNeighbor(AbstractNode<T> neighbor);
  
}
