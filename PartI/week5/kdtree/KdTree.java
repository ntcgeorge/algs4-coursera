import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private boolean DEBUG = false;
    private int n;
    private Node champ;
    private double distance;

    private class Node {
        Node left;
        Node right;
        RectHV rect;
        Point2D point;
        boolean horizontal;

        Node(Point2D p, RectHV rect, boolean direction) {
            this.point = p;
            this.rect = rect;
            this.horizontal = direction;
        }
    }

    private Node root;

    public KdTree() {
        root = null;
        n = 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, 0, 0, 1, 1, true);
    }

    private Node insert(Node node, Point2D p, double xmin, double ymin, double xmax, double ymax, boolean direction) {
        if (node == null) {
            n++;
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            return new Node(p, rect, direction);
        }
        if (node.point.equals(p)) return node;
        if (node.horizontal) {
            if (p.x() < node.point.x()) { // go left
                xmax = node.point.x();
                if (DEBUG) {
                    RectHV r = new RectHV(xmin, ymin, xmax, ymax);
                    System.out.println("insert the rectangle: " + r);
                }
                node.left = insert(node.left, p, xmin, ymin, xmax, ymax, false);
            } else { // go right
                xmin = node.point.x();
                if (DEBUG) {
                    RectHV r = new RectHV(xmin, ymin, xmax, ymax);
                    System.out.println("insert the rectangle: " + r);
                }
                node.right = insert(node.right, p, xmin, ymin, xmax, ymax, false);
            }
        } else {
            if (p.y() < node.point.y()) { // go down
                ymax = node.point.y();
                if (DEBUG) {
                    RectHV r = new RectHV(xmin, ymin, xmax, ymax);
                    System.out.println("insert the rectangle: " + r);
                }
                node.left = insert(node.left, p, xmin, ymin, xmax, ymax, true);

            } else { // go up
                ymin = node.point.y();
                if (DEBUG) {
                    RectHV r = new RectHV(xmin, ymin, xmax, ymax);
                    System.out.println("insert the rectangle: " + r);
                }
                node.right = insert(node.right, p, xmin, ymin, xmax, ymax, true);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("the argument is null to method contains()");
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D point) {
        if (node == null) return false;
        if (node.point.equals(point)) return true;
        double comp;
        if (node.horizontal) {
            comp = node.point.x() - point.x();
        } else {
            comp = node.point.y() - point.y();
        }
        if (comp > 0) {
            return contains(node.left, point);
        } else return contains(node.right, point);
    }

    public void draw() {
        for (Point2D p : range(root.rect)) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle in null to method range()");
        List<Point2D> points = new ArrayList<>();
        if (isEmpty()) return points;
        findPoints(root, rect, points);
        return points;
    }

    private void findPoints(Node node, RectHV rect, List<Point2D> points) {
        if (node != null && rect.intersects(node.rect)) {
            if (rect.contains(node.point)) {
                points.add(node.point);
            }
            findPoints(node.left, rect, points);
            findPoints(node.right, rect, points);
        }
    }


    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point in null to nearest()");
        if (isEmpty()) return null;
        champ = root;
        nearest(root, p);
        return champ.point;
    }

    /**
     * basic idea is the champ is the nearest one between the
     * nearest points from left subtree and right subtree respectively
     */
    private void nearest(Node node, Point2D point) {
        if (node == null) return;
        if (node.horizontal) {
            if (point.x() < node.point.x()) { // go left
                nearest(node.left, point);
                nearest(node.right, point);

            } else {
                nearest(node.right, point);
                nearest(node.left, point);
            }
        } else {
            if (point.y() < node.point.x()) { // go left
                nearest(node.left, point);
                nearest(node.right, point);
            } else {
                nearest(node.right, point);
                nearest(node.left, point);
            }
        }

        distance = point.distanceSquaredTo(champ.point);
        if (node.rect.distanceSquaredTo(point) > distance) return;
        if (node.point.distanceSquaredTo(point) < distance) {
            champ = node;
        }
//        System.out.println(champ.point + " distance is: " + node.point.distanceSquaredTo(point) + " champ distance is: " + champDistance);

    }

    public static void main(String[] args) {
        In in = new In("./input1.txt");
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            in.readString();
            kdTree.insert(new Point2D(in.readDouble(), in.readDouble()));
        }
//        StdOut.println(kdTree.nearest(new Point2D(0.375, 0.125)));
        for (Point2D p : kdTree.range(new RectHV(0, 0.875, 0.125, 1))) {
            StdOut.println(p);
        }
    }
    // unit testing of the methods (optional)
}

