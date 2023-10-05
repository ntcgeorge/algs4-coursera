import edu.princeton.cs.algs4.StdIn;

public class Percolation {
    private boolean[][] opened;
    private int n, topNode, bottomNode, sites;
    private int[] idx;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.sites = 0;
        this.n = n;
        this.opened = new boolean[n][n];
        this.topNode = n * n;
        this.bottomNode = n * n + 1;
        this.idx = new int[n * n + 2];
        for (int i = 0; i < this.idx.length; i++) {
            idx[i] = i;
        }

        // union all the index on the first row with topNode and last row with
        // bottomNode
        for (int i = 0; i < n; i++) {
            union(topNode, i);
        }
        for (int i = n * (n - 1); i < n * n; i++) {
            union(bottomNode, i);
        }

    }

    // open the site(row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        validate(row, col);
        int q = getIndex(row, col);
        int left = col - 1;
        int right = col + 1;
        int up = row + 1;
        int down = row - 1;

        if (isValid(row, left) && isOpen(row, left)) {
            int p = getIndex(row, left);
            union(p, q);
        }
        if (isValid(row, right) && isOpen(row, right)) {
            int p = getIndex(row, right);
            union(p, q);
        }
        if (isValid(up, col) && isOpen(up, col)) {
            int p = getIndex(up, col);
            union(p, q);
        }
        if (isValid(up, col) && isOpen(down, col)) {
            int p = getIndex(down, col);
            union(p, q);
        }

        opened[row - 1][col - 1] = true;
        sites++;
    }


    // is the site(row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return opened[row - 1][col - 1];
    }

    // is the site(row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        int id = getIndex(row, col);
        return connected(topNode, id);
    }

    public int numberOfOpensites() {
        return sites;
    }

    // does the system percolates?
    public boolean percolates() {
        return connected(topNode, bottomNode);
    }

    // is the given row and col number valid
    private void validate(int row, int col) {
        if ((row >= 0) && (row < this.n) && (col >= 0) && (col < this.n)) {
            return;
        } else throw new IllegalArgumentException("the arguments are outside of prescribed range");
    }

    // is the given row and column number inside prescribed range
    private boolean isValid(int row, int col) {
        return (row >= 0) && (row < this.n) && (col >= 0) && (col < this.n);
    }

    // get the index of corresponding row and column
    private int getIndex(int row, int col) {
        return this.n * (row - 1) + col - 1;
    }

    public void union(int p, int q) {
        int root = find(p);
        if (!connected(p, q)) {
            idx[root] = find(q);
        }
    }

    public int find(int p) {
        int root = p;
        while (root != idx[root]) {
            root = idx[root];
        }

        while (idx[p] != p) {
            int temp = idx[p];
            idx[p] = root;
            p = temp;
        }
        return root;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }


    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation per = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            per.union(p, q);
            System.out.println("union node: " + p + " " + q);
        }
        if (per.percolates()) {
            System.out.println("The system is percolated");
        } else {
            System.out.println("The system is not percolated");
        }

    }
}
