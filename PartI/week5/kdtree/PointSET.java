import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> bst;

    public static void main(String[] args) {

    }

    public PointSET() {
        bst = new TreeSet<>();
    }

    public boolean isEmpty() {
        return bst.isEmpty();
    }

    public int size() {
        return bst.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("the argument to insert() is null");
        bst.add(p);
    }

    // add the point to the set (if it is not already in the set)
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("the argument to contains() is null");
        return bst.contains(p);
    }

    public void draw() {
        for (Point2D p : bst) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("the argument to range() is null");
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        for (Point2D p : bst) {
            if (rect.contains(p)) points.add(p);
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        double dis = Double.MAX_VALUE;
        Point2D champ = null;
        for (Point2D that : bst) {
            if (p.distanceTo(that) < dis) {
                dis = p.distanceTo(that);
                champ = that;
            }
        }
        return champ;
    }
}
