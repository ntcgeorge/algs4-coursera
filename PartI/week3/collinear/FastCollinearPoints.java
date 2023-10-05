/**
 * observation:
 * 1.
 */

import edu.princeton.cs.algs4.MergeX;

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private int lineCount = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (!checkNullAndDuplicate(points)) throw new IllegalArgumentException();
        segments = new LineSegment[(int) Math.pow(points.length, 2)];
        int index = 0;
        for (int i = 0; i < points.length; i++) {

            // make a copy of the original array
            Point[] aux = new Point[points.length];
            for (int j = 0; j < points.length; j++) {
                aux[j] = points[j];
            }

            // get reference of the target point
            Point p = points[i];
            // sort the points by the basis of slope
            MergeX.sort(aux, p.slopeOrder());
            for (int j = 1; j < aux.length - 2; j++) {
                double s1 = p.slopeTo(aux[j]);
                double s2 = p.slopeTo(aux[j + 2]);
                if (s1 == s2 || Math.abs(s1) - Math.abs(s2) < 0.00000001) {
                    if (points[i].compareTo(aux[j]) < 0) {
                        segments[index++] = new LineSegment(points[i], aux[j + 2]);
                        lineCount++;
                    }
                }
            }
        }


    }

    // the number of line segments
    public int numberOfSegments() {
        return lineCount;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, lineCount);
    }

    private boolean checkNullAndDuplicate(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) return false;
        }
        assert points.length >= 2;
        // sort the array to find out the repeat
        MergeX.sort(points);
        // make a new array to record the distance
        for (int i = 1; i < points.length; i++) {
            if (points[i].equals(points[i - 1])) return false;
        }
        return true;

    }

    // private int factorial(int n) {
    //     int f = 1;
    //     for (int i = 1; i <= n; i++) {
    //         f = f * i;
    //     }
    //     return f;
    // }

    public static void main(String[] args) {
        Point a = new Point(3, 3);
        Point b = new Point(5, 5);
        Point c = new Point(6, 6);
        Point d = new Point(7, 7);
        Point e = new Point(-1, 6);
        Point f = new Point(-3, 7);
        Point g = new Point(-5, 8);
        Point h = new Point(-7, 9);
        Point[] points = { a, b, c, d, e, f, g, h };
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        //        System.out.println("numberOfSegments: " + fcp.numberOfSegments());
        //        System.out.println("segments: " + fcp.segments()[0].toString());

        LineSegment[] segments = fcp.segments();
        System.out.println("first segments: ");
        for (LineSegment seg : segments) {
            System.out.println(seg);
        }
        System.out.println(" ");
        System.out.println("last segments: ");
        for (LineSegment seg : fcp.segments()) {
            System.out.println(seg);
        }
        System.out.println(fcp.lineCount);
    }

}
