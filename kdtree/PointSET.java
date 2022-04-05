import java.util.ArrayList;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private final TreeSet<Point2D> tree;
    
    public PointSET(){
        this.tree = new TreeSet<Point2D>();
    }
    // is the set empty? 
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set 
    public int size() {
        return tree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        tree.add(p);
    }

    // does the set contain point p? 
    public  boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return tree.contains(p);
    }

    // draw all points to standard draw 
    public void draw() {
        
        for (Point2D point: tree) {
            point.draw();
        }
    } 

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D>  points = new ArrayList<Point2D>();
        for(Point2D point: tree){
           if (rect.contains(point)) {
               points.add(point);
           };
        }

        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Point2D nearest = null;
        double distanceToThatPoint = Double.NEGATIVE_INFINITY;

        for(Point2D that: tree){
            if(distanceToThatPoint == Double.NEGATIVE_INFINITY){
               distanceToThatPoint  = p.distanceTo(that);
               nearest = that;
               continue;
            } 

           if (p.distanceSquaredTo(that) < distanceToThatPoint) {
               distanceToThatPoint =  p.distanceSquaredTo(that);
               nearest = that;
           }
        }

        return nearest;
    }
 
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        pointSet.insert(new Point2D(0.2,0.3));
        pointSet.insert(new Point2D(0.2,0.5));
        pointSet.insert(new Point2D(0.2,0.6));
    }
}
