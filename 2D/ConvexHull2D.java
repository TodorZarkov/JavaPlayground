
package geom;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;



/**
 * This class has purpose to provide set wrapping, i.e. to
 * calculate a convex hull of a given 2D set of 
 * {@link java.awt.Point points}. The method {@link #getLimitPoints(Set)}
 * works with n*sqrt(n) time complexity which is highly overstated 
 * valuation.
 * 
 * @author Todor Zarkov
 *
 */
public class ConvexHull2D {
  
  /*public static void main(String[] args) 
      throws IllegalArgumentException, IOException {
    File setFile = new File("f_circ120000R200");
    
    Set<Point> points;
    points = Files.get2DSet(setFile);
    //printAsImage(points, null);
long s = System.currentTimeMillis();
    List<Point> limitPoints = getLimitPoints(points);
long e = System.currentTimeMillis();
long elapsed = e - s;
System.out.println(elapsed + "ms");
    printAsImage(points, limitPoints);
    
    System.out.println(limitPoints.toString());
  }*/
  
  //This class cannot be instantiated. 
  private ConvexHull2D(){};

  
  /**
   * Gets the limit points of the specified set, considering it as
   * a convex set. Thus it returns the convex hull of the specified
   * set.
   * @param points the set of points.
   * @return the desired convex hull as list of points. The list
   * is ordered in a way that allow every  next point to be joined
   * to the previews one by a straight line.
   * @throws NullPointerException if the specified set is null or
   * empty.
   */
  public static List<Point> getLimitPoints(Set<Point> points) {
    ArrayList<Point> limitPoints = new ArrayList<Point>();
    if (points.size() == 1) {
      Iterator<Point> i = points.iterator();
      limitPoints.add(i.next());
      return limitPoints;
    }
    
    // Absolute most distant(near) points in the set, i.e.
    //don't depend on the sight.
    Point absMost[] = getMost(points);
    
    // Depends on the sight (viewpoint).
    Point rightMost = null;
    
    // Restriction of the points. In what manner - 
    //depends on the sight.
    List<Point> set = null;
    
    for (Sight s: Sight.values()) {
      Point A = null, C = null;// The main diagonal of the rectangle.
      Comparator<Point> c = null, cL = null;
      switch (s) {
        case BOT:
          c = new CompareStrYNoX();
          cL = new CompareStrXNoY();
          A = absMost[0];
          C = absMost[1];
          set = loadListSetFrom(points, A, C, 
              new CompareStrYStrX());
          rightMost = absMost[1]; break;
        case RIGHT:
          c = new CompareRevXNoY();
          cL = new CompareStrYNoX();
          A = new Point(absMost[2].x, absMost[1].y);
          C = new Point(absMost[1].x, absMost[2].y);
          set = loadListSetFrom(points, A, C, 
          new CompareRevXStrY());
          rightMost = absMost[2]; break;
        case TOP: 
          c = new CompareRevYNoX();
          cL = new CompareRevXNoY();
          A = absMost[3];
          C = absMost[2];
          set = loadListSetFrom(points, A, C, 
              new CompareRevYRevX());
          rightMost = absMost[3]; break;
        case LEFT:
          c = new CompareStrXNoY();
          cL = new CompareRevYNoX();
          A = new Point(absMost[3].x, absMost[0].y);
          C = new Point(absMost[0].x, absMost[3].y);
          set = loadListSetFrom(points, A, C, 
              new CompareStrXRevY());
          rightMost = absMost[0]; break;
      }
      List<Point> next = nextLimitPoints(set, rightMost, c, cL);
      limitPoints.addAll(next);
    }
    return limitPoints;
  }
  
  
  /*
   *   Every time it is invoked, the method consider one of the four sights
   * - down sight, right sight, up sight, left sight - of view, assuming
   * right Cartesian coordinate system. The sight is determined unambiguously
   * by the specified comparators, namely cL and cU, assuring that in every
   * sight if the element(point), for example p1, is more left and more up
   * than p2, than cL.compare(p1,p2) = -1, and cU.compare(p1,p2) = 1.
   *   The set of points is contained in the specified List<Point> and it is
   * recommended LinkedList<Point>.
   *   The rightMost specified element is to be the most right element in
   * the specified set according to the specified comparators.
   *   The method returns all the limit points in the specified set of points
   * on the specified sight, from the most bottom to the rightmost elements,
   * assuming convex set; thus it returns one of the four sights of the hole
   * convex hull of the specified set.
   */
  private static List<Point> nextLimitPoints(List<Point> set,
      Point rightMost, Comparator<Point> cU, Comparator<Point> cL) {
    // Calculating the sign of all the points
    //"under" the straight-line.
    Point tmpBig = new Point(2,2);
    Point tmpSmall = new Point(1,1);
    int sgn = cU.compare(tmpBig,tmpSmall);
    
    ArrayList<Point> result = new ArrayList<Point>();
    Iterator<Point> i = set.iterator();
    
    // The limit point
    Point lP = i.next(); i.remove();
    
    // The candidate limit point
    if (set.isEmpty()) {
      //result.add(lP); 
      return result;
    }
    Point cP = null;
    
    // The iterator point
    Point p = null;
    
    while (!lP.equals(rightMost)) {
      do {
      cP = i.next(); i.remove();
      } while (i.hasNext() && (cL.compare(lP, cP)>=0));
      
      StraightLine l = new StraightLine(lP,cP);
      // Determines what is the "upper" point that has meaning
      //to be consider
      int upperBoundX = (int) ((rightMost.y - l.getB())/l.getA());
      int upperBoundY = (int) (l.getA()*rightMost.x + l.getB());
      Point upperBound = new Point(upperBoundX, upperBoundY);
      
      if (set.isEmpty()) {
        result.add(cP); return result;
      }
      p = i.next(); // without removing
      while (cU.compare(p, upperBound) <= 0) {
        double dist = l.distFrom(p);
        if ((cL.compare(lP, p)<0) && sgn*dist <= 0) {
          cP = p; i.remove(); i = set.iterator();
          if (set.isEmpty()) {
            result.add(cP); 
            return result;
          }
          
          l = new StraightLine(lP,cP);
          // Determines what is the "upper" point that has meaning
          //to be consider
          upperBoundX = (int) (sgn*(rightMost.y - l.getB())/l.getA());
          upperBoundY = (int) (l.getA()*rightMost.x + l.getB());
          upperBound = new Point(upperBoundX, upperBoundY);
          
        }
        if (!i.hasNext()) {break;}
        p = i.next();
      }
      lP = cP; i = set.iterator();
      result.add(lP);
      if (set.isEmpty()) {
        result.add(p); return result;
      }
    }
    return result;
  }
  
  
  /**
   * Calculates the distance from the straight line defined by
   * the two points p1 and p1, to the point p. The distance is 
   * oriented; that means when two points are from the one side
   * of the specified straight line, the two corresponding 
   * distances are with the same sign,  and vice versa.
   * @deprecated Replaced by {@link geom.StraightLine#distFrom(Point)
   * distFrom()}
   * @param p
   * @param p1
   * @param p2
   * @return the oriented distance between the specified(by p1,p2) 
   * straight line, and the specified point p.
   */
  public static float distance(Point p, Point p1, Point p2) {
    // the straight line a*x + b = y:
    float a = ((float)(p1.y - p2.y))/(p1.x - p2.x);;
    float b = p2.y - a * p2.x;
    
    // the distance from the straight line to the point p:
    float d = p.y - a*p.x - b;
    if (d == 0f) {
      return d;
    }
    float dist = (float) (d / Math.sqrt(Math.pow(a, 2) + 1));
    
    return dist;
  }
  
  
  /**
   * Returns an array containing as follows:
   * 0 - botmost, 1 - rightmost, 2 - topmost, 3 - leftmost.
   * These are the most near (distant) points to the x-axes and
   * the y-axes in the specified set of points, considering
   * right Cartesian coordinate system.
   * @param set
   * @return the 4 array with the nearest (most distant) points.
   * @throws NullPointerException if null set or empty set.
   */
  public static Point[] getMost(Collection<Point> set) {
    //0 - botmost, 1 - rightmost, 2 - topmost, 3 - leftmost.
    Iterator<Point> iter = set.iterator();
    if (!iter.hasNext()) {
      throw new NullPointerException("The set is empty!");
    }
    Point[] result = new Point[4];
    Point point = iter.next();
    for (int i = 0;i < result.length;i++) {
      result[i] = new Point(point.x, point.y);
    }
    
    for (Point p: set) {
      if (p.y < result[0].y) {
        result[0].y = p.y;
        result[0].x = p.x;
      }
      if (p.y > result[2].y) {
        result[2].y = p.y;
        result[2].x = p.x;
      }
      if (p.x < result[3].x) {
        result[3].y = p.y;
        result[3].x = p.x;
      }
      if (p.x > result[1].x) {
        result[1].y = p.y;
        result[1].x = p.x;
      }
    }
    return result;
  }
  
  
  /**
   * Creates a {@link java.util.TreeSet TreeSet}. Fills it with
   * the specified points in manner obeying the rules of the 
   * specified comparator. Also there is a restriction over the
   * points whit which the set is going to be filled: If the point
   * is not in the rectangle specified by the points A and C, 
   * the point is discarded. The specified points A and C are the
   * tips of the rectangle ABCD, lying on the main diagonal.
   * @deprecated Replaced by 
   * {@link #loadListSetFrom(Point[], Point, Point, Comparator) 
   * loadListSetFrom}
   * @param points 
   * @param A
   * @param C
   * @param c
   * @return the restriction of the specified set of points.
   */
  public static TreeSet<Point> loadTreeSet(
      Set<Point> points,
      Point A, Point C,
      Comparator<Point> c) {
    TreeSet<Point> set = new TreeSet<Point>(c);
    for (Point p: points) {
      if ((p.x < A.x) || (p.y < A.y)) {
        continue;
      }
      if ((p.x > C.x) || (p.y > C.y)) {
        continue;
      }
      set.add(p); 
    }
    
    return set;
  }

  
  /**
   * Creates a {@link java.util.LinkedList List}. Fills it with
   * the specified points and sort it, in manner obeying the rules 
   * of the specified comparator. Also there is a restriction over 
   * the points whit which the list is going to be filled: If the point
   * is not in the rectangle specified by the points A and C, 
   * the point is discarded. The specified points A and C are the
   * tips of the rectangle ABCD, lying on the main diagonal.
   * @param points 
   * @param A
   * @param C
   * @param c
   * @return the restriction of the specified set of points.
   */
  public static LinkedList<Point> loadListSetFrom(
      Collection<Point> points,
      Point A, Point C,
      Comparator<Point> c) {
    LinkedList<Point> set = new LinkedList<Point>();
    for (Point p: points) {
      if ((p.x < A.x) || (p.y < A.y)) {
        continue;
      }
      if ((p.x > C.x) || (p.y > C.y)) {
        continue;
      }
      set.add(p); 
    }
    Collections.sort(set, c);
    return set;
  }


  /**
   * Express the specified set as image thus  pixel coordinate
   * correspond to point coordinate. Creates image with name
   * "convexHull.lim".
   * @param set - the set of points.
   * @param limitPoints - the list of limit points. If null, only
   * the specified set is expressed.
   * @throws IOException if fail writing to the image.
   * @throws NullPointerException if the specified set is null or
   * empty.
   */
  public static void printAsImage(
      Collection<Point> set, List<Point> limitPoints) 
      throws IOException {
    Point[] bounds = getMost(set);
    int w = bounds[1].x+1;
    int h = bounds[2].y+1;
    
    BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
    int limitPointColor = 0x0000ff;// Blue
    int setPointColor = 0x00ff00; // Green
    for (Point setPoint: set) {
      im.setRGB(setPoint.x, setPoint.y, setPointColor);
    }
    if (limitPoints != null) {
      for (Point limPoint: limitPoints) {
        im.setRGB(limPoint.x,limPoint.y, limitPointColor);
      }
    }
    
    File imFile = new File("convexHull.lim");
    ImageIO.write(im, "bmp", imFile);
    
  }
  
  
  public enum Sight {
    BOT, RIGHT, TOP, LEFT
  }
}


import java.awt.Point;
import java.util.Comparator;

public class CompareRevXNoY implements Comparator<Point> {

  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.x > p2.x) {
      return -1;
    }
    if (p1.x < p2.x) {
      return 1;
    }
    return 0;
  }
}


import java.awt.Point;
import java.util.Comparator;

public class CompareRevXStrY implements Comparator<Point> {

  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.x > p2.x) {
      return -1;
    }
    if (p1.x < p2.x) {
      return 1;
    }
    if (p1.x == p2.x) {
      if (p1.y > p2.y) {
        return 1;
      }
      if (p1.y < p2.y) {
        return -1;
      }
      return 0;
    }
    return 0;
  }

  

}


import java.awt.Point;
import java.util.Comparator;

public class CompareRevYNoX implements Comparator<Point> {

  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.y > p2.y) {
      return -1;
    }
    if (p1.y < p2.y) {
      return 1;
    }
    return 0;
  }
}


import java.awt.Point;
import java.util.Comparator;

public class CompareRevYRevX implements Comparator<Point> {
  
  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.y > p2.y) {
      return -1;
    }
    if (p1.y < p2.y) {
      return 1;
    }
    if (p1.y == p2.y) {
      if (p1.x > p2.x) {
        return -1;
      }
      if (p1.x < p2.x) {
        return 1;
      }
      return 0;
    }
    return 0;
  }
}


import java.awt.Point;
import java.util.Comparator;

public class CompareStrXNoY implements Comparator<Point> {

  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.x > p2.x) {
      return 1;
    }
    if (p1.x < p2.x) {
      return -1;
    }
    return 0;
  }
}


import java.awt.Point;
import java.util.Comparator;

public class CompareStrXRevY implements Comparator<Point> {

  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.x > p2.x) {
      return 1;
    }
    if (p1.x < p2.x) {
      return -1;
    }
    if (p1.x == p2.x) {
      if (p1.y > p2.y) {
        return -1;
      }
      if (p1.y < p2.y) {
        return 1;
      }
      return 0;
    }
    return 0;
  }
}


import java.awt.Point;
import java.util.Comparator;

public class CompareStrYNoX implements Comparator<Point> {

  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.y > p2.y) {
      return 1;
    }
    if (p1.y < p2.y) {
      return -1;
    }
    return 0;
  }
}


import java.awt.Point;
import java.util.Comparator;

public class CompareStrYStrX implements Comparator<Point> {

  @Override
  public int compare(Point p1, Point p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new NullPointerException();
    }
    if (!(p1 instanceof Point) || !(p2 instanceof Point)) {
      throw new ClassCastException();
    }
    
    if (p1.y > p2.y) {
      return 1;
    }
    if (p1.y < p2.y) {
      return -1;
    }
    if (p1.y == p2.y) {
      if (p1.x > p2.x) {
        return 1;
      }
      if (p1.x < p2.x) {
        return -1;
      }
      return 0;
    }
    return 0;
  }

}
