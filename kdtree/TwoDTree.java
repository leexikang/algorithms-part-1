import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

class TwoDTree {
    Node root;
    protected int size;

    public Boolean isEmpty() {
        return root == null;
    }

    public boolean contain(Point2D p) {
        return true;
    }

    public Node search(Point2D p) {
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

    private int size(Node node) {
        if (node == null)
            return 0;
        return node.size;
    }

    public int size() {
        return size(root);
    }

    public Iterable<Point2D> inorder() {
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

    public void put(Point2D p) {
        size++;

        RectHV rect = null;
        if (root != null) {
            rect = root.rect;
        }

        root = put(root, p, false, rect);
    }

    private Node put(Node node, Point2D p, Boolean isX, RectHV rect) {
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

    public Iterable<Point2D> contains(RectHV rect) {
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

    public Point2D nearest(Point2D point) {
        return null;
    }

    private void nearest(Node node, Point2D point, Point2D nearest) {

    }

    private boolean inside(RectHV thisRect, RectHV that) {
        if (thisRect.xmin() < that.xmax() &&
                thisRect.ymin() < that.ymax() &&
                thisRect.xmax() > that.xmax() &&
                thisRect.ymax() > that.ymax())
            return true;

        return false;
    }

    private class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb;
        private Node rt;
        private Boolean isX;
        private int size;

        public Node(Point2D p, Boolean isX, RectHV rect, int size) {
            this.p = p;
            this.isX = isX;
            this.rect = rect;
            this.size = size;
        }

        public String toString() {
            return this.p.toString();
        }
    }
}
