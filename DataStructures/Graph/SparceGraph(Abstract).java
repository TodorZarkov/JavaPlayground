
package dataStructures;

import java.util.Collection;
import java.util.Set;

/**
 * This abstraction uses AbstractNode class.
 * @author Todor Zarkov
 *
 * @param <T> - the type of the name of the node
 */
public abstract class AbstractGraph<T> {

  /**
   * @param nodeName
   * @returns null pointer if the node with the specified nodeName 
   * doesn't exist.
   */
  public abstract AbstractNode<T> getNode(T nodeName);
  
  
  /**
   * Sets two way edge with the specified weight between the the nodes 
   * with the specified names. If the the specified nodes do not exist 
   * it creates them. If the edge already exist - overrides it.
   * @param nodeName1
   * @param nodeName2
   * @param weight - the weight between the two nodes.
   * @throws IllegalArgumentException if a specified nodeName is null, 
   * or if the specified weight is negative, or if a specified nodeName 
   * is empty string.
   */
  public abstract void setEdge (
      T nodeName1, T nodeName2, Float weight) 
          throws IllegalArgumentException;
  
  
  /**
   * Deletes two-way edge if it's there.
   * @param fromNodeName
   * @param toNodeName
   * @return -2 if at least one of the two specified node names aren't
   * names of this graph nodes, or there may exist such nodes but NON of
   * them has the other one as a neighbor. If there is one way edge such
   * from ---> to, it returns -1. If there is one way edge such
   * to ---> from, it returns 1. At the end, if there's two way edge such
   * from <---> to, it returns 0. In all the cases it deletes what exists.
   * @Note If, after deleting, one of the specified nodes had been left 
   * without neighbors, the latter is deleted; so getNode will return null.
   */
  public abstract int deleteEdge(T fromNodeName, T toNodeName);
  
  
  /**
   * Check if there is an edge between the specified nodes.
   * @param fromNodeName
   * @param toNodeName
   * @return -2 if at least one of the two specified node names aren't
   * names of this graph nodes, or there may exist such nodes but NON of
   * them has the other one as a neighbor. If there is one way edge such
   * from ---> to, it returns -1. If there is one way edge such
   * to ---> from, it returns 1. At the end, if there's two way edge such
   * from <---> to, it returns 0.
   */
  public abstract int adjacent(T fromNodeName, T toNodeName);
  
  
  /**
   * 
   * @returns a {@link java.util.Collection Collection} view of 
   * the nodes contained in this graph. The collection is backed 
   * by the graph, so changes to the graph are reflected in the 
   * collection, and vice-versa. 
   */
  public abstract Collection<AbstractNode<T>> getAllNodes();
  
  
  /**
   * @returns a {@link java.util.Set Set} view of the node names 
   * contained in this graph. The set is backed by the graph, so 
   * changes to the graph are reflected in the set, and vice-versa. 
   */
  public abstract Set<T> getAllNames();
  
  
  /**
   * @returns the number of all the nodes in this graph.
   */
  public abstract int size();
  
  
  @Override
  public abstract String toString();
}
