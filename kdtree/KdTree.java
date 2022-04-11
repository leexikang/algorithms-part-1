
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private final TwoDTree tree;

    public KdTree() {
        tree = new TwoDTree();
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public int size() {
        return tree.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        tree.put(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return tree.search(p) != null;
    }

    // draw all points to standard draw
    public void draw() {

        for (Point2D point : tree.inorder()) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new IllegalArgumentException();
        }

        return tree.contains(rect);
    }

    public Iterable<Point2D> points() {
        return tree.inorder();
    }

    public Point2D nearest(Point2D point) {
        return tree.nearest(point);
    }
    // public static void main(String[] args) {
    // // initialize the data structures from file
    // String filename = args[0];
    // In in = new In(filename);
    // // PointSET brute = new PointSET();
    // KdTree kdtree = new KdTree();
    // while (!in.isEmpty()) {
    // double x = in.readDouble();
    // double y = in.readDouble();
    // Point2D p = new Point2D(x, y);
    // kdtree.insert(p);
    // // brute.insert(p);
    // }

    // System.out.println(kdtree.tree.root);
    // System.out.println(kdtree.size());
    // System.out.println(kdtree.points());
    // }

    public static void main(String[] args) {
        // initialize the data structures from file
        // PointSET brute = new PointSET();
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        points.add(new Point2D(0.372, 0.497));
        points.add(new Point2D(0.564, 0.413));
        points.add(new Point2D(0.226, 0.577));
        KdTree kdtree = new KdTree();
        for (Point2D point : points) {
            kdtree.insert(point);
        }
        System.out.println(kdtree.nearest(new Point2D(0.564, 0.413)));

        // find the distance between the point and rectange and compare the distance
        // with the closested distance
    }

}
