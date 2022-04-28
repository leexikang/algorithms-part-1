import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] foundSegments;
    private int numOfSegment;

    public FastCollinearPoints(Point[] points) {
        numOfSegment = 0;
        foundSegments = new LineSegment[1];
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] p = copy(points.clone());
        try {
            Arrays.sort(p);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        checkDuplication(p);

        if (points.length < 4) {
            return;
        }

        for (int i = 0; i < p.length; i++) {

            Point[] copy = p.clone();
            Point current = copy[i];
            Arrays.sort(copy, current.slopeOrder());
            double preSlope = current.slopeTo(copy[1]);
            double[] slopes = new double[p.length];
            slopes[0] = 1;
            Point[] equalSlope = new Point[] {
                    copy[0],
                    copy[1],
            };

            int equalSlopeIndex = 2;
            for (int j = 2; j < copy.length; j++) {
                double slope = current.slopeTo(copy[j]);
                slopes[j] = slope;
                if (slope == preSlope) {
                    if (equalSlope.length == equalSlopeIndex) {
                        equalSlope = resizePoints(equalSlope, equalSlope.length);
                    }
                    equalSlope[equalSlopeIndex] = copy[j];
                    equalSlopeIndex++;
                    if (j < copy.length - 1)
                        continue;
                }

                if (equalSlopeIndex >= 4) {
                    equalSlope = Arrays.copyOfRange(equalSlope, 0, equalSlopeIndex);
                    Arrays.sort(equalSlope);
                    if (equalSlope[0] == current) {
                        if (numOfSegment == foundSegments.length) {
                            resizeSegments();
                        }

                        foundSegments[numOfSegment] = new LineSegment(equalSlope[0],
                                equalSlope[equalSlope.length - 1]);
                        numOfSegment++;
                    }
                }

                preSlope = slope;
                equalSlopeIndex = 2;
                equalSlope = new Point[] {
                        copy[0],
                        copy[j],
                };
            }
        }
    }

    private void checkDuplication(Point[] p) {
        Point pre = null;
        for (int i = 0; i < p.length; i++) {
            if (p[i] == null) {
                throw new IllegalArgumentException();
            }

            if (pre != null && pre.compareTo(p[i]) == 0) {
                throw new IllegalArgumentException();
            }
            pre = p[i];
        }
    }

    private Point[] copy(Point[] original) {
        Point[] copy = new Point[original.length];
        for (int i = 0; i < original.length; i++) {
            if (original[i] == null) {
                throw new IllegalArgumentException();
            }

            copy[i] = original[i];
        }
        return copy;
    }

    private Point[] resizePoints(Point[] original, int n) {
        if (n == 0) {
            return new Point[1];
        }

        Point[] copy = new Point[n * 2];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i];
        }

        return copy;
    }

    private void resizeSegments() {
        LineSegment[] copy = new LineSegment[foundSegments.length * 2];
        for (int i = 0; i < foundSegments.length; i++) {
            copy[i] = foundSegments[i];
        }
        foundSegments = copy;
    }

    public int numberOfSegments() {
        return numOfSegment;
    }

    public LineSegment[] segments() {
        if (numOfSegment > 0) {
            return Arrays.copyOfRange(foundSegments, 0, numOfSegment);
        }

        return new LineSegment[0];
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

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}