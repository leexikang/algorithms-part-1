
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
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
            cmp = Point2D.Y_ORDER.compare(p, node.p);
        } else {
            cmp = Point2D.X_ORDER.compare(p, node.p);
        }

        if (cmp > 0) {
            RectHV rtRect;
            if (isX) {
                rtRect = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
            } else {
                rtRect = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            node.rt = put(node.rt, p, !isX, rtRect);
        } else if (cmp < 0) {
            RectHV lbRect;

            if (isX) {
                lbRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
            } else {
                lbRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
            }

            node.lb = put(node.lb, p, !isX, lbRect);
        } else {
            RectHV rtRect;
            if (!node.p.equals(p)) {

                if (isX) {
                    rtRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                } else {
                    rtRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                }
                node.rt = put(node.rt, p, !isX, rtRect);
            }
        }

        node.size = 1 + size(node.rt) + size(node.lb);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Node node = root;
        while (node != null) {

            int cmp = Point2D.R_ORDER.compare(node.p, p);
            if (cmp > 0)
                node = node.rt;
            else if (cmp < 0)
                node = node.lb;
            else
                return true;

            // int cmp = -11;
            // if (node.isX) {
            // cmp = Point2D.Y_ORDER.compare(node.p, p);
            // }else{
            // cmp = Point2D.X_ORDER.compare(node.p, p);
            // }

            // System.out.println(cmp);
            // if (cmp > 0) node = node.rt;
            // else if (cmp < 0) node = node.lb;
            // else {
            // System.out.println("---------");
            // if (node.isX) {
            // if(Point2D.X_ORDER.compare(node.p, p) == 0){
            // return true;
            // }else {
            // node.rt = node.rt;
            // }
            // } else {
            // if(Point2D.Y_ORDER.compare(node.p, p) == 0){
            // return true;
            // }else {
            // node.rt = node.rt;
            // }
            // }
            // }
        }
        return false;
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
    public void draw() {
        for (Point2D point : inorder()) {
            point.draw();
        }
    }

    // private Iterable<Node> inorderNode() {
    // Queue<Node> queue = new Queue<Node>();
    // inorderNode(root, queue);
    // return queue;
    // }

    // private void inorderNode(Node node, Queue<Node> queue) {
    // if (node == null)
    // return;

    // inorderNode(node.lb, queue);
    // queue.enqueue(node);
    // inorderNode(node.rt, queue);
    // }

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

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        contains(root, points, rect);
        return points;
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
        private Point2D p; // the point
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

        System.out.println(kdtree.range(new RectHV(0.13, 0.24, 0.32, 0.68)));
        System.out.println(kdtree.inorder());
        // System.out.println(kdtree.size());
    }
}
