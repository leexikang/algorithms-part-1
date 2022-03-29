import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] foundSegments;
    private Point[]  p;
    private int numOfSegment;

    public FastCollinearPoints(Point[] points) {
        numOfSegment = 0;

        if (points == null) {
            throw new IllegalArgumentException();
        }

        if (points.length < 4) {
            return;
        }
        p = points.clone();
        Arrays.sort(points);
        for (int i = 0; i < p.length; i++) {
            if (i != 0 && p[i - 1] == p[i]) {
                throw new IllegalArgumentException();
            }

            Point[] copy = p.clone();
            Arrays.sort(copy, p[i].slopeOrder());
            int counter = 1;
            double preSlop = Double.NEGATIVE_INFINITY;
            for (int j = 1; j < copy.length; j++) {
                double slop = p[i].slopeTo(copy[j]);

                if (slop == preSlop) {
                    counter++;
                }

                if (counter >= 3 && (slop != preSlop || j == copy.length - 1)) {
                    if (numOfSegment == foundSegments.length) {
                        resize(foundSegments.length);
                    }

                    Point[] candidatePoints = new Point[counter + 1];
                    for (int k = counter; k > -1; k--) {
                        if (j == copy.length - 1 && slop == preSlop) {
                            candidatePoints[k] = copy[j - k];
                        } else {
                            candidatePoints[k] = copy[j - k - 1];
                        }
                    }

                    candidatePoints[counter] = p[i];
                    Arrays.sort(candidatePoints);
                    if (candidatePoints[0] == p[i]) {
                        LineSegment segment = new LineSegment(candidatePoints[0], candidatePoints[counter]);
                        foundSegments[numOfSegment] = segment;
                        numOfSegment++;
                    }
                }
                if (preSlop != slop) {
                    counter = 1;
                    preSlop = slop;
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
        FastCollinearPoints collinear = new FastCollinearPoints(cp);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}