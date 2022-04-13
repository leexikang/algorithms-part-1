
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

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

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        contains(root, points, rect);
        return points;
    }

    public void draw() {
        for (Node node : inorderNode()) {
            node.draw();
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Node node = root;
        double thatX = p.x();
        double thatY = p.y();
        while (node != null) {
            int cmp = 0;
            if (node.isX) {
                cmp = Point2D.Y_ORDER.compare(p, node.p);
            } else {
                cmp = Point2D.X_ORDER.compare(p, node.p);
            }

            if (cmp > 0)
                node = node.rt;
            else if (cmp < 0)
                node = node.lb;
            else {
                if (node.x == thatX && node.y == thatY) {
                    return true;
                } else {
                    node = node.rt;
                }
            }
        }
        return false;
    }

    public Point2D nearest(Point2D point) {
        if ( point == null) {
            throw new IllegalArgumentException();
        }

        return nearest(root, point, null);
    }

    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        // PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        // kdtree.insert(new Point2D(0.372, 0.497));
        // kdtree.insert(new Point2D(0.564, 0.413));
        // kdtree.insert(new Point2D(0.226, 0.577));
        // kdtree.insert(new Point2D(0.625, 0.25));
        // kdtree.insert(new Point2D(1.0, 1.0));
        // kdtree.insert(new Point2D(0.5, 0.0));

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            kdtree.insert(new Point2D(x, y));
        }

        // RectHV rect = new RectHV(0.0, 0.654508, 0.206107, 1.0);
        // kdtree.draw();
        // Point2D point  = new Point2D(0.79, 0.54);
        //  Point2D point  = new Point2D(0.44, 0.86);
        // Point2D point2  = new Point2D(0.9, 0.6);
        // Point2D point3  = new Point2D(0.4, 0.7);
        // StdDraw.setPenRadius(0.02);
        // point.draw();
        // point2.draw();
        // point3.draw();
        System.out.println(kdtree.inorderNode());
        System.out.println(kdtree.contains(new Point2D(0.5, 0.75)));
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
            cmp = Point2D.Y_ORDER.compare(p, node.p);
        } else {
            cmp = Point2D.X_ORDER.compare(p, node.p);
        }

        if (cmp > 0) {
            node.rt = put(node.rt, p, !isX, node.rightRec());
        } else if (cmp < 0) {
            node.lb = put(node.lb, p, !isX, node.leftRec());
        } else {
            if (!node.p.equals(p)) {
                node.rt = put(node.rt, p, !isX, node.rightRec());
            }
        }

        node.size = 1 + size(node.rt) + size(node.lb);
        return node;
    }

    private void contains(Node node, ArrayList<Point2D> points, RectHV rect) {
        if (node == null) {
            return;
        }
        if (node.lb != null && node.lb.rect.intersects(rect)) {
            contains(node.lb, points, rect);
        }

        if (node.rt != null && node.rt.rect.intersects(rect)) {
            contains(node.rt, points, rect);
        }

        if (rect.contains(node.p)) {
            points.add(node.p);
        }
    }

    // private boolean inside(RectHV thisRect, RectHV that) {
    // if (thisRect.xmin() <= that.xmax() &&
    // thisRect.ymin() <= that.ymax() &&
    // thisRect.xmax() >= that.xmax() &&
    // thisRect.ymax() >= that.ymax())
    // return true;

    // return false;
    // }

    // draw all points to standard draw
    private Iterable<Node> inorderNode() {
        Queue<Node> queue = new Queue<Node>();
        inorderNode(root, queue);
        return queue;
    }

    private void inorderNode(Node node, Queue<Node> queue) {
        if (node == null)
        return;

        inorderNode(node.lb, queue);
        queue.enqueue(node);
        inorderNode(node.rt, queue);
    }

    // private Iterable<Point2D> inorder() {
    //     Queue<Point2D> queue = new Queue<Point2D>();
    //     inorder(root, queue);
    //     return queue;
    // }

    // private void inorder(Node node, Queue<Point2D> queue) {

    //     if (node == null)
    //         return;

    //     inorder(node.lb, queue);
    //     queue.enqueue(node.p);
    //     inorder(node.rt, queue);
    // }

    private Point2D nearest(Node node, Point2D that, Point2D chmp) {
        if (node == null) {
            return chmp;
        }

        if (chmp == null) {
            chmp = node.p;
        }

        if (that.distanceSquaredTo(node.p) < that.distanceSquaredTo(chmp)) {
            chmp = node.p;
        }

        if (node.rt != null && node.lb != null) {
            if (that.distanceSquaredTo(node.rt.p) < that.distanceSquaredTo(node.lb.p)) {
                if (node.rt.rect.distanceSquaredTo(that) < that.distanceSquaredTo(chmp)) {
                    chmp = nearest(node.rt, that, chmp);
                }

                if (node.lb.rect.distanceSquaredTo(that) < that.distanceSquaredTo(chmp)) {
                    chmp = nearest(node.lb, that, chmp);
                }
            } else {
                if (node.lb.rect.distanceSquaredTo(that) < that.distanceSquaredTo(chmp)) {
                    chmp = nearest(node.lb, that, chmp);
                }

                if (node.rt.rect.distanceSquaredTo(that) < that.distanceSquaredTo(chmp)) {
                    chmp = nearest(node.rt, that, chmp);
                }
            }
        }else {
            if (node.lb != null  && node.lb.rect.distanceSquaredTo(that) < that.distanceSquaredTo(chmp)) {
                chmp = nearest(node.lb, that, chmp);
            }

            if (node.rt != null && node.rt.rect.distanceSquaredTo(that) < that.distanceSquaredTo(chmp)) {
                chmp = nearest(node.rt, that, chmp);
            }
        }

        return chmp;
    }

    private class Node {
        private Point2D p; // the point
        final private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb;
        private Node rt;
        final private boolean isX;
        private int size;
        final private double x;
        final private double y;

        public Node(Point2D p, boolean isX, RectHV rect, int size) {
            this.p = p;
            this.isX = isX;
            this.rect = rect;
            this.size = size;
            this.x = p.x();
            this.y = p.y();
        }

        public String toString() {
            return this.p.toString();
        }

        public RectHV leftRec() {
            if (isX) {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            }
        }

        public RectHV rightRec() {
            if (isX) {
                return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
            } else {
                return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
        }

        public void draw() {
            if (isX) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
            } else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
            }
        }

    }

}
