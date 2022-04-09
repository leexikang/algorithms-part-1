
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
}
