
package dataStructures;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Represents the binary tree structure
 * 
 * @author Todor Zarkov
 */
public class BinaryTree<T> {

  /**
   * Represents the node of the binary tree
   * 
   * @author Todor Zarkov
   * @param <T>
   *            - the type of the values in nodes
   */
  public static class BinaryTreeNode<T> {
    // The value of the node.
    private T value;

    // Determines whether a node has parent or not.
    private boolean hasParent;

    // The left and the right child to the current node.
    private BinaryTreeNode<T> leftChild;
    private BinaryTreeNode<T> rightChild;

    private int childrenCount = 0;

    /**
     * Constructs a node of the tree
     * 
     * @param value
     *            - the value of the node
     */
    private BinaryTreeNode(T value) {
      if (value == null) {
        throw new NullPointerException("Null value is unexceptable!");
      }
      this.value = value;
      this.leftChild = null;
      this.rightChild = null;

      this.hasParent = false;
    }

    // Getters and Setters
    public T getValue() {
      return value;
    }

    public void setValue(T value) {
      if (value == null) {
        throw new NullPointerException("Null value is unexceptable!");
      }
      this.value = value;
    }

    public BinaryTreeNode<T> getLeftChild() {
      return leftChild;
    }

    public void setLeftChild(BinaryTreeNode<T> leftChild) {
      if (leftChild == null) {
        throw new NullPointerException("Null value is unexceptable!");
      }

      /*
       * if(leftChild.hasParent) { throw new IllegalArgumentException(
       * "The node already has a parent!"); }
       */

      this.leftChild = leftChild;
      leftChild.hasParent = true;
    }

    public BinaryTreeNode<T> getRightChild() {
      return rightChild;
    }

    public void setRightChild(BinaryTreeNode<T> rightCild) {
      if (rightCild == null) {
        throw new NullPointerException("Null value is unexceptable!");
      }

      /*
       * if(leftChild.hasParent) { throw new IllegalArgumentException(
       * "The node already has a parent!"); }
       */

      this.rightChild = rightCild;
      rightChild.hasParent = true;
    }

    public int getChildrenCount() {
      return this.childrenCount;
    }

  }

  // The root of the tree
  private BinaryTreeNode<T> root;

  // Counter of the nodes(trees in this case...)
  private static int count = 0;

  /**
   * Gets the root of this tree.
   * 
   * @return the root BinaryTreeNode<T> node of the tree.
   */
  public BinaryTreeNode<T> getRoot() {
    return root;
  }

  public BinaryTree(T value) {
    this(value, null, null);
  }

  public BinaryTree(
      T value, BinaryTree<T> leftChild, BinaryTree<T> rightChild) {
    this.root = new BinaryTreeNode<T>(value);

    if (leftChild != null) {
      this.root.setLeftChild(leftChild.root);
      this.root.childrenCount++;
    }

    if (rightChild != null) {
      this.root.setRightChild(rightChild.root);
      this.root.childrenCount++;
    }

    count++;
  }

  public BinaryTreeNode<T> getLeftChildNode() {
    if (this.root != null) {
      return this.root.getLeftChild();
    }
    return null;
  }

  public BinaryTreeNode<T> getRightChildNode() {
    if (this.root != null) {
      return this.root.getRightChild();
    }
    return null;
  }

  private void printPreOrder(BinaryTreeNode<T> root) {
    if (root == null) {
      return;
    }

    printPreOrder(root.getLeftChild());

    System.out.print(root.value + " ");

    printPreOrder(root.getRightChild());

  }

  public void printPreOrder() {
    printPreOrder(this.root);
    System.out.println();
  }

  /**
   * Searches for the specified value into the tree and counts 
   * how many times it occurs.
   * 
   * @param value
   * @return the number of occurrence.
   */
  public int searchAndCountDFS(Integer value) {
    Stack<BinaryTreeNode<T>> stack = new Stack<BinaryTreeNode<T>>();
    BinaryTreeNode<T> root = this.getRoot();
    stack.push(root);

    int count = 0;
    while (stack.size() != 0) {
      root = stack.pop();

      if (root.getValue() == value) {
        count++;
      }

      BinaryTreeNode<T> tmp = root.getLeftChild();
      if (tmp != null) {
        stack.push(tmp);
      }
      tmp = root.getRightChild();
      if (tmp != null) {
        stack.push(tmp);
      }

    }
    return count;
  }

  private int depth(BinaryTreeNode<T> root) {
    if (root == null)
      return 0;
    Queue<BinaryTreeNode<T>> queue = new LinkedList<BinaryTreeNode<T>>();
    queue.offer(root);

    int count = 1; // *************
    int currChildren = 0; // *************

    int level = 0;
    while (queue.size() != 0) {
      root = queue.poll();

      /*
       * This algorithm traverses over the tree in Breadth First Search
       * manner. Here is the point where the current node of the tree
       * appears and here we can look at its fields and search for
       * something. In this case ...
       */

      count--; // *************
      currChildren += root.getChildrenCount();// *************
      if (count == 0) { // *************
        count = currChildren; // *************
        // System.out.println(currChildren); //*************
        currChildren = 0; // *************

        /*
         * here we can add something that we want to clear after
         * changing the level of the tree.(Every tree has a level,
         * that's all nodes with same path length from the main root.)
         */

        level++;

      } // *************

      BinaryTreeNode<T> tmp = root.getLeftChild();
      if (tmp != null) {
        queue.offer(tmp);
      }
      tmp = root.getRightChild();
      if (tmp != null) {
        queue.offer(tmp);
      }

    }
    return level;
  }

  private void printAsDFS(BinaryTreeNode<T> root, String separator) {
    if (this.root == null) {
      return;
    }

    System.out.println(separator + root.getValue());

    if (root.getLeftChild() != null) {
      // Fixing the white space
      String space = " ", tmp = "";
      for (int j = 0; j < separator.length(); j++) {
        tmp += space;
      }
      separator = tmp;

      // Recursive calling
      printAsDFS(root.getLeftChild(), separator + "\\_____");
    }
    if (root.getRightChild() != null) {
      // Fixing the white space
      String space = " ", tmp = "";
      for (int j = 0; j < separator.length(); j++) {
        tmp += space;
      }
      separator = tmp;

      // Recursive calling
      printAsDFS(root.getRightChild(), separator + "\\_____");
    }

    return;

  }

  /**
   * Express this tree into the system standard output with a human readable
   * view. Uses Deep First Search algorithm to traverse over the tree.
   */
  public void printAsDFS() {
    printAsDFS(this.root, new String());
  }

  public boolean isBalanced() {
    Queue<BinaryTreeNode<T>> queue = new LinkedList<BinaryTreeNode<T>>();
    BinaryTreeNode<T> root = this.getRoot();
    queue.offer(root);

    while (queue.size() != 0) {
      root = queue.poll();

      if (root != null) {
        int depthL = depth(root.getLeftChild());
        int depthR = depth(root.getRightChild());
        if (Math.abs(depthL - depthR) > 1)
          return false;

        queue.offer(root.getLeftChild());
        queue.offer(root.getRightChild());
      }
    }
    return true;
  }

}
