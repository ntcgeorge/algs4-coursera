import edu.princeton.cs.algs4.MergeX;

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int lineCount = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (!checkNullAndDuplicate(points)) throw new IllegalArgumentException();

        // compute the max length of the line segments length
        segments = new LineSegment[(int) Math.pow(points.length, 2)];
        int index = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[m];
                        double sq = p.slopeTo(q);
                        double sr = p.slopeTo(r);
                        double ss = p.slopeTo(s);
                        if (sq == sr && sq == ss) {
                            segments[index] = new LineSegment(p, s);
                            index++;
                            lineCount++;
                        }

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


    // return factorial of an integer
    private static int factorial(int n) {
        int f = 1;
        for (int i = 1; i <= n; i++) {
            f = f * i;
        }
        return f;
    }

    public static void main(String[] args) {
        System.out.println(factorial(4));
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

}


