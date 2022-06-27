
package dataStructures;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 
 * @author Todor Zarkov
 *
 * @param <T> - the type of the name of the graph's node.
 */
public class Graph<T> {
  private HashMap<T, Node<T>> graph;
  
  
  /**
   * Constructs an empty graph with initial capacity of 16 nodes. This new
   * graph contains nodes of type Node. The type T concerns the names of 
   * the nodes in this graph, i.e. every node's name in this graph is of 
   * type T.
   * @param size initial capacity
   */
  public Graph() {
    this.graph = new HashMap<T,Node<T>>();
  }
  
  
  /**
   * Constructs an empty graph with the specified initial capacity. This 
   * new graph contains nodes of type Node. The type T concerns the names 
   * of the nodes in this graph, i.e. every node's name in this graph is 
   * of type T.
   * @param size initial capacity
   */
  public Graph(int size) {
    this.graph = new HashMap<T,Node<T>>(size);
  }
  
  
  /**
   * @param nodeName
   * @returns null pointer if the node with the specified nodeName 
   * doesn't exist.
   */
  public Node<T> getNode(T nodeName) {
    if ((nodeName == null) || (nodeName.equals(""))) {
      return null;
    }
    return graph.get(nodeName);
  }
  
  
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
  public void setEdge (
      T nodeName1, T nodeName2, Float weight) 
          throws IllegalArgumentException {
    if ((nodeName1 == null) || (nodeName1.equals(""))) {
      throw new IllegalArgumentException(
          "The specified nodeName1 must not be null or"
          + " empty string!");
    }
    if ((nodeName2 == null) || (nodeName2.equals(""))) {
      throw new IllegalArgumentException(
          "The specified nodeName1 must not be null or"
          + " empty string!");
    }
    Node<T> node1 = getNode(nodeName1);
    Node<T> node2 = getNode(nodeName2);
    if (node1 == null) {
      node1 = new Node<T>(nodeName1);
      graph.put(nodeName1, node1);
    }
    if (node2 == null) {
      node2 = new Node<T>(nodeName2);
      graph.put(nodeName2, node2);
    }
    node1.setNeighbor(node2, weight);
    node2.setNeighbor(node1, weight);
  }
  
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
  public int deleteEdge(T fromNodeName, T toNodeName) {
    if (!graph.containsKey(fromNodeName) || 
        !graph.containsKey(toNodeName)) {
      return -2;
    }
    Node<T> fromNode = graph.get(fromNodeName);
    Node<T> toNode = graph.get(toNodeName);
    boolean hasToNode = fromNode.deleteNeighbor(toNode);
    boolean hasFromNode = toNode.deleteNeighbor(fromNode);
    if (fromNode.getNeighbours().size() == 0) {
      graph.remove(fromNodeName);
    }
    if (toNode.getNeighbours().size() == 0) {
      graph.remove(toNodeName);
    }
    
    if (hasFromNode && hasToNode) {
      return 0;
    } else if (!hasFromNode && hasToNode) {
      return -1;
    } else if (hasFromNode && !hasToNode) {
      return 1;
    } else {
      return -2;
    }  
  }
  
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
  public int adjacent(T fromNodeName, T toNodeName) {
    if (!graph.containsKey(fromNodeName) || 
        !graph.containsKey(toNodeName)) {
      return -2;
    }
    Node<T> fromNode = graph.get(fromNodeName);
    Node<T> toNode = graph.get(toNodeName);
    boolean hasToNode = fromNode.getNeighbours().contains(toNode);
    boolean hasFromNode = toNode.getNeighbours().contains(fromNode);
    if (hasFromNode && hasToNode) {
      return 0;
    } else if (!hasFromNode && hasToNode) {
      return -1;
    } else if (hasFromNode && !hasToNode) {
      return 1;
    } else {
      return -2;
    }
  }
  
  
  /**
   * 
   * @returns a {@link java.util.Collection Collection} view of 
   * the nodes contained in this graph. The collection is backed 
   * by the graph, so changes to the graph are reflected in the 
   * collection, and vice-versa. 
   */
  public Collection<Node<T>> getAllNodes() {
    return graph.values();
  }
  
  /**
   * @returns a {@link java.util.Set Set} view of the node names 
   * contained in this graph. The set is backed by the graph, so 
   * changes to the graph are reflected in the set, and vice-versa. 
   */
  public Set<T> getAllNames() {
    return graph.keySet();
    
  }
  
  /**
   * @returns the number of all the nodes in this graph.
   */
  public int size() {
    int size = graph.size();
    return size;
  }
  
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Entry<T, Node<T>> entry: graph.entrySet()) {
      Node<T> node1 = entry.getValue();
      T nodeName1 = node1.getName();
      HashSet<Node<T>> neighbors = node1.getNeighbours();
      for (Node<T> node2: neighbors) {
        T nodeName2 = node2.getName();
        float weight = node1.getWeight(node2);
        sb.append("(");
        sb.append(nodeName1);
        sb.append(" ");
        sb.append(nodeName2);
        sb.append(") ");
        sb.append(weight);
        sb.append("\n");
      }
    }
    String result = new String(sb);
    return result;
  }
}

package dataStructures;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class Node<T> implements Comparator<Node<T>>, Comparable<Node<T>> {
  private T name;
  private Float value;
  private boolean visited;
  private Node<T> previous;
  private HashSet<Node<T>> neighbors;
  private HashMap<Node<T>,Float>  weights;

  
  /**
   * Constructs new node. The new node isn't related to any other node nor
   * Graph. The new node type T concerns the name of the node, i.e. the name
   * of the node is of type T.
   * @param name - the new node name. Must not be empty string or null.
   * @throws NullPointerException if the specified name is null nameType.
   * @throws IllegalArgumentException if the specified name is empty string.
   */
  public Node(T name) {
    if (name == null) {
      throw new NullPointerException (
          "The specified name must be valid(not null) string.");
    }
    if (name.equals("")) {
      throw new IllegalArgumentException(
          "The specified name must not be empty string.");
    }
    this.name = name;
    this.value = Float.POSITIVE_INFINITY;
    this.visited = false;
    this.previous = null;
    this.neighbors = new HashSet<Node<T>>();
    this.weights = new HashMap<Node<T>,Float>();
  }
  
  
  /**
   * @returns Infinity if the value hasn't been set yet.
   */
  public Float getValue() {
    return value;
  }


  /**
   * @param value must be positive.
   * @throws IllegalArgumentException if (value < 0).
   */
  public void setValue(Float value) {
    if (value < 0) {
      throw new IllegalArgumentException(
          "The argument value (" + value + ") must be positive");
    }
    this.value = value;
  }


  public boolean isVisited() {
    return visited;
  }


  public void setVisited(boolean visited) {
    this.visited = visited;
  }


  public Node<T> getPrevious() {
    return previous;
  }


  public void setPrevious(Node<T> previous) {
    this.previous = previous;
  }


  public T getName() {
    return name;
  }


  public HashSet<Node<T>> getNeighbours() {
    return neighbors;
  }
  
  
  /**
   * @param toNode
   * @return the weight of the edge between this node and the specified
   * one. The edge(if exist) has an orientation from this node to the
   * specified one.
   * @throws NullPointerException if the argument is null. 
   * @throws NoSuchElementException if the argument isn't a neighbor 
   * of this node, i.e. the edge does not exist.
   */
  public Float getWeight(Node<T> toNode) {
    if (toNode == null) {
      throw new NullPointerException(
          "The argument mstn't to be null!");
    }
    if (!weights.containsKey(toNode)) {
      throw new NoSuchElementException(
          "The argument must to be neighbor of this node!");
    }
    return weights.get(toNode);
  }
  
  
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
  public void setNeighbor(Node<T> neighbor, Float weight) {
    if (neighbor == null) {
      throw new NullPointerException(
          "The argument must not to be null!");
    }
    if (weight < 0) {
      throw new IllegalArgumentException(
          "The specified weight must be positive number");
    }
    weights.put(neighbor, weight);
    neighbors.add(neighbor);
  }
  
  /**
   * @param neighbor - the neighbor we want to delete.
   * @return If the specified neighbor exist returns true, otherwise - false.
   */
  public boolean deleteNeighbor(Node<T> neighbor) {
    boolean hasNeighbor = this.neighbors.remove(neighbor);
    this.weights.remove(neighbor);
    return hasNeighbor;
  }

  @Override
  /**
   * Note: this comparator imposes orderings that 
   *are inconsistent with equals.
   * Note: throws NullPointerException.
   * Note: throws ClassCastException.
   */
  public int compare(Node<T> node1, Node<T> node2) {
    if ((node1 == null) || (node2 == null)) {
      throw new NullPointerException();
    }
    if (!(node1 instanceof Node) || !(node2 instanceof Node)) {
      throw new ClassCastException();
    }
    if (node1.value > node2.value) {
      return 1;
    }
    if (node1.value < node2.value) {
      return -1;
    }
    return 0;
  }


  @Override
  public int compareTo(Node<T> node) {
    if ((node == null) || (this == null)) {
      throw new NullPointerException();
    }
    if (!(node instanceof Node)) {
      throw new ClassCastException();
    }
    if (this.value > node.value) {
      return 1;
    }
    if (this.value < node.value) {
      return -1;
    }
    return 0;
  }
}
