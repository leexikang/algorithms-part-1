import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

class TwoDTree {
    private Node root;
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

            if (p.equals(p)) return node;

            if(node.isX) {
                if(node.p.y() > p.y()) node = node.rt;
                if(node.p.y() < p.y()) node = node.lb;
            }

            if(!node.isX) {
                if(node.p.x() > p.x()) node = node.rt;
                if(node.p.x() < p.x()) node = node.lb;
            }
        }

        return node;
    }

    private int size (Node node) {
        if (node == null) return 0;
        return node.size;
    }

    public int size () {
        return size(root);
    }

    public Iterable<Point2D> inorder() {
        Queue<Point2D> queue = new Queue<Point2D>();
        inorder(root, queue);
        return queue;
    }

    private void inorder(Node node, Queue<Point2D> queue) {

        if(node == null) return;

        inorder(node.lb, queue);
        queue.enqueue(node.p);
        inorder(node.rt, queue);
    }

    public void put(Point2D p) {
        size++;
        root = put(root, p, false);
    }

    private Node put(Node node, Point2D p, Boolean isX) {
        if(node == null) return new Node(p, isX);
        int cmp =  0;

        if(isX){
            if(node.p.y() > p.y()) cmp = 1;
            if(node.p.y() < p.y()) cmp = -1;
            else cmp = 0;
        }else{
            if(node.p.x() > p.x()) cmp = 1;
            if(node.p.x() < p.x()) cmp = -1;
            else cmp = 0;
        }
        
        if (cmp > 0) node.rt = put(node.rt, p, !isX);
        if (cmp < 0) node.lb = put(node.lb, p, !isX);
        else node.p = p;
        node.size = 1 + size(node.rt) + size(node.lb);
        return node;
    }

    private class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        
        private Node rt;
        private Boolean isX;
        private int size;

        public Node(Point2D p, Boolean isX) {
            this.p = p;
            this.isX = isX;
        }
    }
}
        
