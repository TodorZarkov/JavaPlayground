
package dataStructures;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.imageio.ImageIO;


/**
 * This class works with a class Graph of type T as the graph must 
 * implement the following:<br>
 * AbstractGraph&ltT&gt;<br>
 * AbstractNode&ltT&gt.
 * @author Todor Zarkov
 *
 */
public class Graphs {
  
  // This class cannot be instantiated
  private Graphs() {};
  
  
  /**
   * Sets the graph nodes from string in which every row must contains
   * <br>|String|SPACE|String|SPACE|Integer|<br> , nothing less and 
   * nothing else.<br>
   *   The first part of the file are the edges of the graph, and the 
   * second part are the requested paths to be found.
   * @param map - map which obey the above specification.
   * @throws FileNotFoundException if the file specified is missing or
   * unreadable.
   * @throws IllegalArgumentException if the input file doesn't fit
   * the file specification.
   */
  @SuppressWarnings("resource")
  public static void set(Graph<String> graph, String map) 
      throws IllegalArgumentException {
    Scanner mapScanner = null;
    Scanner lineScanner = null;
    int rowCounter = 1;
    try {
      mapScanner = new Scanner(map);
      while (mapScanner.hasNextLine()) {
        String line = mapScanner.nextLine();
        lineScanner = new Scanner(line);
        
        String nodeName1 = lineScanner.next();
        String nodeName2 = lineScanner.next();
        int weight = lineScanner.nextInt();
        graph.setEdge(nodeName1, nodeName2, (float) weight);
        
        if (lineScanner.hasNext()) {
          throw new InputMismatchException();
        }
        rowCounter++;
      }
      lineScanner.close();
      mapScanner.close();
    
    } catch (InputMismatchException e) {
      throw new IllegalArgumentException(
          "\n The map string contains wrong data at row " + 
          rowCounter +
          ". The row must contains "
          + "|String|SPACE|String|SPACE|Integer|"
          + ",\nnothisng less and nothing else!");
    } finally {
      if (lineScanner != null) {
        lineScanner.close();
      }
      if (mapScanner != null) {
        mapScanner.close();
      }
    }
  }
  
  
  /**
   * Sets the specified graph g from the image im considering the
   *  forbidden color forbColor as proceed:<br>
   * Every pixel is a node candidate. If two pixels are neighbors
   * (one pixel can possess eight neighbors maximum) and non of them 
   * is in the forbidden color, then every one of them become a node 
   * and there's an edge between them with weight - the difference 
   * between the colors of the corresponding  pixels.<br>
   * If a pixel is in the forbidden color it is discarded and 
   * represent no node. The weight is calculating by the formulae:<br>
   * weight(n1,n2) = abs((int n1Color) - (int n1Color)) + 1  for non 
   * diagonal neighbors, and<br>
   * weight(n1,n2) = sqrt(2)*abs((int n1Color) - (int n1Color)) + 1  
   * for diagonal neighbors.
   * @param g - the graph with node names of type 
   * {@link java.awt.Point Point}.
   * @param im - BufferedImage with color scheme of type 
   * TYPE_3BYTE_BGR.
   * @param forbColor - the forbidden color.
   * @throws InterruptedException if another thread reads/writes 
   * the image.
   * @throws IllegalArgumentException if the image has different 
   * than TYPE_3BYTE_BGR type.
   */
  public static void setFromImage(
      Graph<Point> g, BufferedImage im, Color forbColor) 
          throws InterruptedException, IllegalArgumentException {
    if (im.getType() != BufferedImage.TYPE_3BYTE_BGR) {
      throw new IllegalArgumentException(
          "The specified BufferedImage must be "
          + "of type BufferedImage.TYPE_3BYTE_BGR !");
    }
    
    int w = im.getWidth();
    int h = im.getHeight();
    int[] pixels = new int[w*h];
    PixelGrabber pg = new PixelGrabber(im, 0, 0, w, h, pixels, 0, w);
    pg.grabPixels();
    int[] neighbors = new int[8];
    float[] weights = new float[8];
    for (int p = 0;p < pixels.length;p++) {
      getNeighborPixelsOf(p, pixels, neighbors, weights, forbColor, w, h);
      int x = (p+w)%w;
      int y = p/w;
      Point nodeName = new Point(x,y);
      for (int i = 0;i < neighbors.length;i++) {
        if (neighbors[i] != -1) {
          x = (neighbors[i]+w)%w;
          y = neighbors[i]/w;
          Point neighborName = new Point(x,y);
          if (g.adjacent(nodeName, neighborName) != 0) {
            g.setEdge(nodeName, neighborName, weights[i]);
          }
          
        }
      }
    }  
  }
  
  /**
   * Takes as an argument p in the given array of pixels pixels[ ]. 
   * Returns, in the specified array neighbors[ ], all the neighbor 
   * pixels of p within the array pixels[ ], concerning the borders, 
   * specified by w and h, and the forbidden color, specified by 
   * forbColor.<br>
   * The image, hence the pixels[ ] array, must be TYPE_3BYTE_BGR which 
   * represent Windows-style BGR color model. If that is violated there 
   * would be unpredictable outcome.<br>
   * The corresponding weights[ ] to the returned neighbors[ ] are 
   * calculating  with the formula:<br>
   * weights[i] =Math.abs(pixels[neighbors[i]] - pixels[p] + 1) for non 
   * diagonal sides, and:<br>
   * weights[i] = (float)Math.sqrt(2) * Math.abs(<br> 
   * pixels[neighbors[i]] - pixels[p] + 1) for diagonal sides.
   * @param p the concerning position in the pixels array.
   * @param pixels - the array with the image data.
   * @param neighbors - return all the neighbors here. If a neighbor 
   * is missing (border, forbidden color or other reason), returns -1. 
   * It expect to return 8 neighbors (neighbors[8]).
   * @param weights - return all 8 corresponding weights.
   * @param forbColor - if a pixel is in that color it will have no 
   * neighbors; if pixel has neighbor in that color, that neighbor is no 
   * more neighbor of the given pixel.
   * @param w - the w of the image which data are passed by pixels array.
   * @param h - the height of the image which data are passed by pixels 
   * array.
   * @exception IllegalArgumentException if neighbors or weights array 
   * lengths are different than 8.
   */
  private static void getNeighborPixelsOf(
      int p, int[] pixels, int[] neighbors, float[] weights, 
      Color forbColor, int w, int h) {
    if ((neighbors.length != 8) || (weights.length != 8)) {
      throw new IllegalArgumentException(
          "The specified arrays neighbors"
          + "and weights must have length 8!");
    }
    int fColor = forbColor.getRGB(); // The forbidden color.
    fColor = fColor & 0x00ffffff; // Get rid of the alpha component 
    int pColor = pixels[p]; // The current pixel color.
    pColor = pColor & 0x00ffffff; // Get rid of the alpha component

    // Forbidden color condition for the current pixel p.
    if (fColor == pColor) {
      for (int i = 0;i < neighbors.length;i++) {
        neighbors[i] = -1;
      }
      return;
    }
    
    // Calculating the positions of the neighbors.
    neighbors[0] = p - 1;//    Left
    neighbors[1] = p + 1;//   Right
    neighbors[2] = p - w;//   Up
    neighbors[3] = p + w;//   Down
    neighbors[4] = p - w - 1;// Up-Left
    neighbors[5] = p - w + 1;// Up-Right
    neighbors[6] = p + w - 1;// Down-Left
    neighbors[7] = p + w + 1;// Down-Right
    
    // Left border condition filtering
    if(p%w == 0) {
      neighbors[4] = neighbors[0] = neighbors[6] = -1;
      
      // Left corners condition filtering
      if(p == 0) {
        neighbors[2] = neighbors[5] = -1;
      }else if(p == w*(h-1)) {
        neighbors[3] = neighbors[7] = -1;
      }
    }
    
    // Right border condition filtering
    if((p+1)%w == 0) {
      neighbors[5] = neighbors[1] = neighbors[7] = -1;
      
      // Right corners condition filtering
      if((p+1) == w) {
        neighbors[2] = neighbors[4] = -1;
      }else if(p == ((w*h)-1)) {
        neighbors[3] = neighbors[6] = -1;
      }
    }
    
    // Up border condition filtering
    if(p/w == 0) {
      neighbors[4] = neighbors[2] = neighbors[5] = -1;
    }
    
    // Down border condition filtering
    if(p/(w*(h-1)) != 0) {
      neighbors[6] = neighbors[3] = neighbors[7] = -1;
    }
    
    // Neighbors color filtering
    for(int i = 0;i < neighbors.length;i++) {
      // if the neighbor is already discarded
      if(neighbors[i] == -1) {
        continue;
      }

      if(pixels[neighbors[i]] == fColor){
        neighbors[i] = -1;
      }
    }
    
    // Calculating the weights
    for(int i = 0;i < neighbors.length;i++) {
      if(neighbors[i] == -1) {
        weights[i] = Float.POSITIVE_INFINITY;
        continue;
      }
      
      int neighborColor = pixels[neighbors[i]];
      neighborColor = neighborColor & 0x00ffffff;//GetRidOfAlphaComponent.
      if(i < 4) {// Left, Right, Up, Down (i.e. not diagonal) sides
        weights[i] =Math.abs(
            neighborColor - pColor + 1);
      }else {// Diagonal sides
        weights[i] = (float)Math.sqrt(2) * Math.abs(
            neighborColor - pColor + 1);
      }
    }
  }
  
  
  
  /**
   *Represents the specified graph as a picture in which each pixel 
   *is a vertex. Each pixel can have eight neighbors, so each pixel 
   *is colored differently according to how many neighbors it has.
   * @param g - the graph that will be represented. Its node names 
   * are of type {@link java.awt.Point Point}.
   * @param w - the width of the image that will be created 
   * (in pixels).
   * @param h - the height of the image that will be created 
   * (in pixels).
   * @return creates an bitmap picture in the current directory; 
   * it is named "print_graph.bmp".
   * @exception IOException if error occurs during image writing.
   */
  public static void printAsImage(Graph<Point> g, int w, int h) 
      throws IOException {
    BufferedImage im = new BufferedImage(
        w, h, BufferedImage.TYPE_3BYTE_BGR);
    for (int x = 0;x < w;x++) {
      for (int y = 0;y < h;y++) {
        Point nodeName = new Point(x,y);
        
        if (g.getNode(nodeName) == null) {
          im.setRGB(x, y, 0x000000);// Black.
        }else if(g.getNode(nodeName).getNeighbours().size() == 8) {
          im.setRGB(x, y, 0xffffff);//white
          
        }else if(g.getNode(nodeName).getNeighbours().size() == 7){
          im.setRGB(x, y, 0xffd800);//yellow
          
        }else if(g.getNode(nodeName).getNeighbours().size() == 6){
          im.setRGB(x, y, 0xff0000);//red
          
        }else if(g.getNode(nodeName).getNeighbours().size() == 5){
          im.setRGB(x, y, 0x0026FF);//blue
          
        }else if(g.getNode(nodeName).getNeighbours().size() == 4){
          im.setRGB(x, y, 0xF50E61);//pink
          
        }else if(g.getNode(nodeName).getNeighbours().size() == 3){
          im.setRGB(x, y, 0xD71CC8);//purple
          
        }else if(g.getNode(nodeName).getNeighbours().size() == 2){
          im.setRGB(x, y, 0x25590F);//dark green
          
        }else if(g.getNode(nodeName).getNeighbours().size() == 1){
          im.setRGB(x, y, 0x5F2C0B);//brown
          
        }
      }
    }
    File output = new File("print_graph.bmp");
    ImageIO.write(im, "bmp", output);  
  }
  
  
  
  /**
   * This method implements Dijkstra's algorithm. It uses 
   * {@link Heap PriorityQue}.
   * You can override it in order to improve the algorithm.
   * @param graph - the graph you want to search of.
   * @param nodeNameFrom - the start node.
   * @param nodeNameTo - the destination node.
   * @return list of pairs. Every pair contains node name and 
   * the weight to that node from the start node.
   */
  public static <T> ArrayList<OrderedPair<T,Float>> 
  findShortestPathDijkstraIn(
      Graph<T> graph, T nodeNameFrom, T nodeNameTo) {
    ArrayList<OrderedPair<T,Float>> resultPath = 
        new ArrayList<OrderedPair<T, Float>>();
    if ((nodeNameFrom == null) || (nodeNameTo == null) ||
        nodeNameFrom.equals("") || nodeNameTo.equals("") ||
        nodeNameFrom.equals(nodeNameTo)) {
      resultPath.add(null);
      return resultPath;
    }
    Node<T> nodeFrom = graph.getNode(nodeNameFrom);
    Node<T> nodeTo = graph.getNode(nodeNameTo);
    if ((nodeFrom == null) || (nodeTo == null)) {
      resultPath.add(null);
      return resultPath;
    }
    
    Collection<Node<T>> allNodes = graph.getAllNodes();
    Heap<Node<T>> pq = new Heap<Node<T>>(graph.size()); 
    for (Node<T> node: allNodes) {
      node.setVisited(false);
      node.setValue(Float.POSITIVE_INFINITY);
    } 
    nodeFrom.setValue(0.0f);
    pq.addAll(allNodes);
    
    resultPath.add(null);
    Node<T> currNode = null;
    while (pq.size() != 0) {
      currNode = pq.poll();
      if (currNode == nodeTo) {
        resultPath = getPath(nodeTo, nodeFrom);
        return resultPath;
      }
        HashSet<Node<T>> neighbors = currNode.getNeighbours();
      for (Node<T> neighbor: neighbors) {
        if (neighbor.isVisited()) {
          continue;
        }
        float altValue = currNode.getValue() 
            + currNode.getWeight(neighbor);
        if (altValue < neighbor.getValue()) {
          neighbor.setValue(altValue);
          neighbor.setPrevious(currNode);
          pq.update(neighbor);//TODO FIX equals AND compare IF PROBLEM
        }
      }
    }
    return resultPath;
  }
  
  
  private static <T> ArrayList<OrderedPair<T,Float>> 
  getPath(Node<T> nodeTo, Node<T> nodeFrom) {
    ArrayList<OrderedPair<T,Float>> result = 
        new ArrayList<OrderedPair<T,Float>>();
    
    Node<T> prevNode = nodeTo;
    Node<T> currNode = null;
    while (prevNode != nodeFrom) {
      T name = prevNode.getName();
      float value = prevNode.getValue();
      result.add(new OrderedPair<T,Float>(name, value));
      currNode = prevNode;
      prevNode = currNode.getPrevious();
      if (prevNode == null) {
        result = new ArrayList<OrderedPair<T, Float>>();
        result.add(null);
        return result;
      }
    }
    T name = prevNode.getName();
    float value = prevNode.getValue();
    result.add(new OrderedPair<T,Float>(name, value));
    
    return result;
  }
}
