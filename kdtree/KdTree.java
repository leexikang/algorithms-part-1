
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private Node root;

    public KdTree() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null)
            return 0;
        return node.size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        RectHV rect = null;
        if (root != null) {
            rect = root.rect;
        }

        root = put(root, p, false, rect);

    }

    private Node put(Node node, Point2D p, boolean isX, RectHV rect) {
        if (node == null) {
            if (rect == null) {
                return new Node(p, isX, new RectHV(0, 0, 1, 1), 1);
            }

            return new Node(p, isX, rect, 1);
        }

        int cmp = 0;

        if (isX) {
            if (node.p.y() < p.y())
                cmp = 1;
            else if (node.p.y() > p.y())
                cmp = -1;
            else
                cmp = 0;
        } else {
            if (node.p.x() < p.x())
                cmp = 1;
            else if (node.p.x() > p.x())
                cmp = -1;
            else
                cmp = 0;
        }

        if (cmp < 0) {
            RectHV rtRect;
            if (isX) {
                rtRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
            } else {
                rtRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
            }
            node.rt = put(node.rt, p, !isX, rtRect);
        }

        if (cmp > 0) {
            RectHV lbRect;
            if (isX) {
                lbRect = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
            } else {
                lbRect = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }

            node.lb = put(node.lb, p, !isX, lbRect);
        }

        node.size = 1 + size(node.rt) + size(node.lb);
        return node;
    }
        // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return search(p) != null;
    }

    private Iterable<Point2D> contains(RectHV rect) {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        contains(root, points, rect);
        return points;
    }

    private void contains(Node node, ArrayList<Point2D> points, RectHV rect) {
        if (node == null) {
            return;
        }

        if (rect.intersects(node.rect)) {
            contains(node.lb, points, rect);
            contains(node.rt, points, rect);
        } else {
            if (node.rt != null && inside(node.rt.rect, rect)) {
                contains(node.rt, points, rect);
            }

            if (node.lb != null && inside(node.lb.rect, rect)) {
                contains(node.lb, points, rect);
            }
        }

        if (rect.contains(node.p)) {
            points.add(node.p);
        }
    }

    private boolean inside(RectHV thisRect, RectHV that) {
        if (thisRect.xmin() < that.xmax() &&
                thisRect.ymin() < that.ymax() &&
                thisRect.xmax() > that.xmax() &&
                thisRect.ymax() > that.ymax())
            return true;

        return false;
    }



    private Node search(Point2D p) {
        Node node = root;
        while (node != null) {

            if (p.equals(p))
                return node;

            if (node.isX) {
                if (node.p.y() > p.y())
                    node = node.rt;
                if (node.p.y() < p.y())
                    node = node.lb;
            }

            if (!node.isX) {
                if (node.p.x() > p.x())
                    node = node.rt;
                if (node.p.x() < p.x())
                    node = node.lb;
            }
        }

        return node;
    }



    // draw all points to standard draw
    public void draw() {
        for (Point2D point : inorder()) {
            point.draw();
        }
    }

    private Iterable<Point2D> inorder() {
        Queue<Point2D> queue = new Queue<Point2D>();
        inorder(root, queue);
        return queue;
    }

    private void inorder(Node node, Queue<Point2D> queue) {

        if (node == null)
            return;

        inorder(node.lb, queue);
        queue.enqueue(node.p);
        inorder(node.rt, queue);
    }


    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new IllegalArgumentException();
        }

        return contains(rect);
    }

    public Point2D nearest(Point2D point) {
        return nearest(root, point, null);
    }

    private Point2D nearest(Node node, Point2D that, Point2D chmp) {
        if (node == null) {
            return chmp;
        }

        if (chmp == null) {
            chmp = node.p;
        }

        double chmpDistance = chmp.distanceSquaredTo(that);
        
        if (node.p.distanceSquaredTo(that) < chmp.distanceSquaredTo(that)) {
            chmp = node.p;
            chmpDistance = node.p.distanceSquaredTo(that);
        }

        if (node.lb != null && node.lb.rect.distanceSquaredTo(that) < chmpDistance) {
                        chmp = nearest(node.lb, that, chmp);
        }

        if (node.rt != null && node.rt.rect.distanceSquaredTo(that) < chmpDistance) {
            chmp = nearest(node.rt, that, chmp);
        }

        return chmp;
    }

    private class Node {
        final private Point2D p; // the point
        final private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb;
        private Node rt;
        final private boolean isX;
        private int size;

        public Node(Point2D p, boolean isX, RectHV rect, int size) {
            this.p = p;
            this.isX = isX;
            this.rect = rect;
            this.size = size;
        }

        public String toString() {
            return this.p.toString();
        }
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
        // points.add(new Point2D(0.144, 0.179));
        KdTree kdtree = new KdTree();
        for (Point2D point : points) {
            kdtree.insert(point);
        }
        System.out.println(kdtree.nearest(new Point2D(0.504, 0.413)));

        // find the distance between the point and rectange and compare the distance
        // with the closested distance
    }

}
