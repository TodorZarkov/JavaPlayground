
package geom;

import java.awt.Point;


/**
 * This class represent the straight line in manner<br>
 * ax + b = y, <br>
 * where a, b are double coefficients and x, y are the variables.
 * 
 * @author Todor Zarkov
 *
 */
public class StraightLine {
  
  // The coefficients of the straight line.
  private double a;
  private double b;
  
  private boolean collinearOx;
  private boolean collinearOy;
  
  private final double xConst;
  private final double yConst;

  public double getA() {
    return a;
  }

  /*public void setA(double a) {
    this.a = a;
  }*/

  public double getB() {
    return b;
  }

  /*public void setB(double b) {
    this.b = b;
  }*/
  
  
  /**
   * Constructs straight line with the specified coefficients
   * as "ax + b = y".
   * @param a - coefficient.
   * @param b - coefficient.
   * @throws IllegalArgumentException if a = b = 0.
   */
  public StraightLine(double a, double b) {
    if (((a == 0d) && (b == 0d)) || Double.isInfinite(a)) {
      throw new IllegalArgumentException(
          "Cannot create \"ax + b = y\" "
          + "with a == 0 and b == 0.");
    }
    // exceptional cases
    if (a == 0d) {
      this.collinearOx = true;
      this.collinearOy = false;
      this.yConst = b;
      this.xConst = Double.NaN;
    } else {
      this.collinearOx = false;
      this.collinearOy = false;
      this.yConst = Double.NaN;
      this.xConst = Double.NaN;
    }
    
    this.a = a;
    this.b = b;
  }
  
  
  /**
   * Constructs straight line that passes through the specified
   * points p1 and p2.
   * @param p1
   * @param p2
   * @throws IllegalArgumentException if the specified arguments
   * are equal.
   */
  public StraightLine(Point p1, Point p2) {
    if (p1.equals(p2)) {
      throw new IllegalArgumentException(
          "Attempt to create point instead of a straight"
          + "line. The specified arguments must differ!"); 
    }
    this.a = ((double)(p2.y - p1.y))/(p2.x - p1.x);
    this.b = p2.y - a * p2.x;
    // exceptional cases
    if (a == 0d) {
      this.collinearOx = true;
      this.collinearOy = false;
      this.yConst = b;
      this.xConst = Double.NaN;
    } else if (Double.isInfinite(a)){
      this.collinearOx = false;
      this.collinearOy = true;
      this.b = 0d;
      this.yConst = Double.NaN;
      this.xConst = p1.x;
    } else {
      this.collinearOx = false;
      this.collinearOy = false;
      this.yConst = Double.NaN;
      this.xConst = Double.NaN;
    }
  }
  
  
  /**
   * Gets the distance between this straight line and the
   * specified point p. The distance is oriented; that means 
   * when two points are from the one side of this straight 
   * line, the two corresponding distances are with same sign,  
   * and vice versa.
   * @param p - the considering point.
   * @return - the oriented distance.
   */
  public double distFrom(Point p) {
    double dist;
    // exceptional cases
    if (collinearOy) {
      dist = p.x - this.xConst;
    } else if (collinearOx) {
      dist = p.y - this.yConst;
    } else {
      // the distance from the straight line to the point p:
      double d = p.y - a*p.x - b;
      if (d == 0f) {
        return d;
      }
      dist = d / Math.sqrt(Math.pow(a, 2) + 1);
    }
    
    
    return dist;
  }
  
  
  /**
   * Calculates the cross point between this line and the specified
   * one. If the lines are parallel returns Infinity for both coordinates.
   * Returns the point in array thus 0 is x and 1 is y.<br><br>
   * Implementation Notes: The calculation is implemented in simple
   * manner without any matrix calculations, thus doesn't fit to 
   * more than two lines crossing calculation. 
   * @param line
   * @return the desired cross point.
   */
  public double[] crossPoint(StraightLine line) {
    double y = (this.b*line.a - line.b*this.a)/(line.a - this.a);
    double x = (y - this.b)/this.a;
    double[] result = new double[2];
    result[0] = x;
    result[1] = y;
    return result;
  }
  
  
  public double calculateX(double y) {
    double result;
    // Exceptional case
    if (collinearOy || collinearOx) {
      result = this.xConst;
    } else {
      result = (y - b)/a;
    }
    return result;
  }
  
  
  public double calculateY(double x) {
    double result;
    // Exceptional case
    if (collinearOx || collinearOy) {
      result = this.yConst;
    } else {
      result = a*x + b;
    }
    return result;
    
  }

}
