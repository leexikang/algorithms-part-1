
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] foundSegments;
    private int numOfSegment;
    private Point[] p;

    public BruteCollinearPoints(Point[] points) {
        numOfSegment = 0;
        foundSegments = new LineSegment[0];
        if (points == null) {
            throw new IllegalArgumentException();
        }

        if (points.length < 4) {
            return;
        }
        p = points.clone();
        Arrays.sort(p);

        for (int i = 0; i < p.length - 3; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = i + 1; j < p.length - 2; j++) {
                for (int k = j + 1; k < p.length - 1; k++) {
                    for (int l = k + 1; l < p.length; l++) {
                        if (i != 0 && p[i - 1] == p[i]) {
                            throw new IllegalArgumentException();
                        }

                        Point[] segmentsPoints = new Point[] { p[i], p[j], p[k], p[l], };
                        if (p[i].slopeTo(p[j]) == p[i].slopeTo(p[k])
                                && p[i].slopeTo(p[j]) == p[i].slopeTo(p[l])) {
                            Arrays.sort(segmentsPoints);
                            LineSegment segment = new LineSegment(segmentsPoints[3], segmentsPoints[0]);

                            if (numOfSegment == foundSegments.length) {
                                resize(foundSegments.length);
                            }
                            foundSegments[numOfSegment] = segment;
                            numOfSegment++;
                        }
                    }

                }
            }
        }
        if (numOfSegment > 0) {
            LineSegment[] copy = new LineSegment[numOfSegment];
            for (int i = 0; i < numOfSegment; i++) {
                copy[i] = foundSegments[i];
            }
            foundSegments = copy;
        }
    }

    private void resize(int n) {
        if (n == 0) {
            foundSegments = new LineSegment[1];
            return;
        }

        LineSegment[] copy = new LineSegment[n * 2];
        for (int i = 0; i < foundSegments.length; i++) {
            copy[i] = foundSegments[i];
        }
        foundSegments = copy;
    }

    public int numberOfSegments() {
        return numOfSegment;
    }

    public LineSegment[] segments() {
        return foundSegments.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        Point[] cp = new Point[n + 1];
        for (int i = 0; i < points.length; i++) {
            cp[i] = points[i];
        }
        cp[n] = points[n - 1];
        // print and draw the line segmentsA
        BruteCollinearPoints collinear = new BruteCollinearPoints(cp);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        StdDraw.show();
    }
}