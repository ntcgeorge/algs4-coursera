import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

import java.awt.Color;

/**
 * since we know the graph is asyclic and the topological order is determined
 * naturally, therefore we only need to apply the relaxation operation by the order
 * of the topological order.
 */
public class SeamCarver {
    private int width;
    private int height;
    private final static int MAX_ENERGY = 1000;
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        this.width = this.picture.width();
        this.height = this.picture.height();

    }

    // current picture
    public Picture picture() {

        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width || y >= height) throw new IllegalArgumentException();
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) return MAX_ENERGY;
        int deltaX = 0;
        int deltaY = 0;

        int[] rgbx0 = toRGB(picture.getRGB(x - 1, y));
        int[] rgbx1 = toRGB(picture.getRGB(x + 1, y));
        for (int i = 0; i < 3; i++) {
            deltaX += Math.abs(rgbx0[i] - rgbx1[i]) * Math.abs(rgbx0[i] - rgbx1[i]);
        }

        int[] rgby0 = toRGB(picture.getRGB(x, y - 1));
        int[] rgby1 = toRGB(picture.getRGB(x, y + 1));
        for (int i = 0; i < 3; i++) {
            deltaY += Math.abs(rgby0[i] - rgby1[i]) * Math.abs(rgby0[i] - rgby1[i]);
        }
        double e = Math.sqrt(deltaX + deltaY);
        return e;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] shortest = new int[width];
        int[][][] edgeTo = new int[height][width][2];
        double[][] distTo = new double[height][width];

        // left to right
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (col == 0) {
                    distTo[row][col] = MAX_ENERGY;
                }
                else distTo[row][col] = Double.POSITIVE_INFINITY;
                edgeTo[row][col] = new int[] { -1, -1 };
            }
        }

        // follow  the topological order to do the relaxation
        for (int col = 0; col < width; col++)
            for (int row = 0; row < height; row++) {
                for (Integer[] pos : adj(row, col, true)) {
                    int r = pos[0];
                    int c = pos[1];
                    if (distTo[r][c] > distTo[row][col] + energy(col, row)) {
                        distTo[r][c] = distTo[row][col] + energy(col, row);
                        edgeTo[r][c] = new int[] { row, col };
                    }
                }
            }
        double maxDist = Double.POSITIVE_INFINITY;
        int[] tail = new int[] { height - 1, width - 1 };
        for (int i = 0; i < height; i++) {
            if (distTo[i][width - 1] < maxDist) {
                maxDist = distTo[i][width - 1];
                tail = new int[] { i, width - 1 };
            }
        }
        for (int i = width - 1; i >= 0; i--) {
            shortest[i] = tail[0];
            tail = edgeTo[tail[0]][tail[1]];
        }
        return shortest;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] shortest = new int[height];
        int[][][] edgeTo = new int[height][width][2];
        double[][] distTo = new double[height][width];

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (row == 0) {
                    distTo[row][col] = MAX_ENERGY;
                }
                else distTo[row][col] = Double.POSITIVE_INFINITY;
                edgeTo[row][col] = new int[] { -1, -1 };
            }
        }

        // follow  the topological order to do the relaxation
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++) {
                for (Integer[] pos : adj(row, col, false)) {
                    int r = pos[0];
                    int c = pos[1];
                    if (distTo[r][c] > distTo[row][col] + energy(col, row)) {
                        distTo[r][c] = distTo[row][col] + energy(col, row);
                        edgeTo[r][c] = new int[] { row, col };
                    }
                }
            }
        double maxDist = Double.POSITIVE_INFINITY;
        int[] tail = new int[] { height - 1, width - 1 };
        for (int i = 0; i < width; i++) {
            if (distTo[height - 1][i] < maxDist) {
                maxDist = distTo[height - 1][i];
                tail = new int[] { height - 1, i };
            }
        }
        for (int i = height - 1; i >= 0; i--) {
            shortest[i] = tail[1];
            tail = edgeTo[tail[0]][tail[1]];
        }
        return shortest;
    }


    private int[] toRGB(int rgb) {
        Color c = new Color(rgb);
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        return new int[] { red, green, blue };
    }

    // return the adjacent vertex with respect to the source
    private Iterable<Integer[]> adj(int row, int col, boolean horizontal) {
        Queue<Integer[]> q = new Queue<>();
        if (horizontal && col < width - 1) {
            q.enqueue(new Integer[] { row, col + 1 });
            if (row + 1 < height) q.enqueue(new Integer[] { row + 1, col + 1 });
            if (row - 1 > 0) q.enqueue(new Integer[] { row - 1, col + 1 });
        }
        if (!horizontal && row < height - 1) {
            q.enqueue(new Integer[] { row + 1, col });
            if (col + 1 < width) q.enqueue(new Integer[] { row + 1, col + 1 });
            if (col - 1 > 0) q.enqueue(new Integer[] { row + 1, col - 1 });
        }
        return q;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (height <= 1) throw new IllegalArgumentException();
        if (seam.length != width) throw new IllegalArgumentException();
        for (int i = 1; i < width; i++) {
            if (seam[i - 1] >= height || Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width, height - 1);
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < seam[w]; h++) {
                newPicture.set(w, h, picture.get(w, h));
            }
            for (int h = seam[w]; h < height - 1; h++) {
                newPicture.set(w, h, picture.get(w, h + 1));
            }
        }
        picture = newPicture;
        width = picture.width();
        height = picture.height();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (width <= 1) throw new IllegalArgumentException();
        if (seam.length != height) throw new IllegalArgumentException();
        for (int i = 1; i < height; i++) {
            if (seam[i - 1] >= width || Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width - 1, height);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < seam[h]; w++) {
                newPicture.set(w, h, picture.get(w, h));
            }
            for (int w = seam[h]; w < width - 1; w++) {
                newPicture.set(w, h, picture.get(w + 1, h));
            }
        }
        picture = newPicture;
        width = picture.width();
        height = picture.height();
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
