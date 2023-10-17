import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private int n = 0;

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
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, new RectHV(0, 0, 1, 1), true);
    }

    private Node insert(Node node, Point2D p, RectHV rect, boolean direction) {
        double xmin, xmax, ymin, ymax;
        if (node == null) {
            n++;
            return new Node(p, rect, direction);
        }
        if (node.horizontal) {
            ymin = rect.ymin();
            ymax = rect.ymax();
            if (p.x() <= node.point.x()) { // go left
                xmin = rect.xmin();
                xmax = node.point.x();
                node.left = insert(node.left, p, new RectHV(xmin, ymin, xmax, ymax), false);
            } else { // go right
                xmin = node.point.x();
                xmax = rect.xmax();
                node.right = insert(node.right, p, new RectHV(xmin, ymin, xmax, ymax), false);
            }


        } else {
            xmin = rect.xmin();
            xmax = rect.xmax();
            if (p.y() <= node.point.y()) { // go down
                ymin = rect.ymin();
                ymax = node.point.y();
                node.left = insert(node.left, p, new RectHV(xmin, ymin, xmax, ymax), true);

            } else { // go up
                ymin = node.point.x();
                ymax = rect.ymax();
                node.right = insert(node.right, p, new RectHV(xmin, ymin, xmax, ymax), true);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("the argument is null to method contains()");
        if (root == null) return false;
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
        if (comp == 0.0) return true;
        if (comp < 0) {
            return contains(node.left, point);
        } else return contains(node.right, point);
    }

    public void draw() {
        for (Point2D p : range(root.rect)) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> points = new ArrayList<>();
        if (isEmpty()) return points;
        findPoints(root, rect, points);
        return points;
    }

    public void findPoints(Node node, RectHV rect, List<Point2D> points) {
        if (rect.contains(node.point)) points.add(node.point);
        if (node.left != null && rect.intersects(node.left.rect)) findPoints(node.left, rect, points);
        if (node.right != null && rect.intersects(node.right.rect)) findPoints(node.right, rect, points);
    }


    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        Node champ = root;
        nearest(root, p, champ);
        return champ.point;
    }

    private void nearest(Node node, Point2D point, Node champ) {
        if (node == null) return;
        if (node.rect.distanceTo(point) > node.rect.distanceTo(champ.point)) return;
        // update the champ
        champ = node;
        nearest(node.left, point, champ);
        nearest(node.right, point, champ);
    }

    public static void main(String[] args) {
    }
    // unit testing of the methods (optional)
}

