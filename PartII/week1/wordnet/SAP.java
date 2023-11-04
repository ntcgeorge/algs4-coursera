import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * the method ancestor(int, int) length(int, int) and  implemented on the top of
 * data type provided by algs4, that is because it has no limitation for
 * the digraph to be DAG at cost of duplicates of searching,
 * while the method ancestor(Iterable<>, Iterable<>)length(Iterable<>, Iterable<>)
 * is implemented not using built-in data type, it is more efficient.
 */
public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v == w) return 0;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int shortestLength = -1;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int distance = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (shortestLength == -1 || distance < shortestLength) {
                    shortestLength = distance;
                }
            }
        }
        return shortestLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v == w) return v;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int shortestLength = -1;
        int ancestor = -1;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int distance = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (shortestLength == -1 || distance < shortestLength) {
                    shortestLength = distance;
                    ancestor = vertex;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        Queue<Integer> q = new Queue<>();
        int[] marked
                = new int[G.V()]; // marked[v] = 0: not touched, 1: touched by v
        // synsets, 2: touched by w synsets
        int[] distTo = new int[G.V()]; // distTo[v] = distance from vetices
        // originated from sources
        for (Integer x : v) {
            marked[x] = 1;
            q.enqueue(x);
        }
        for (Integer x : w) {
            if (marked[x] == 1) return 0;
            marked[x] = 2;
            q.enqueue(x);
        }

        while (!q.isEmpty()) {
            int x = q.dequeue();
            for (int u : G.adj(x)) {
                if (marked[u] != marked[x] && marked[u] != 0) {
                    return distTo[x] + distTo[u] + 1;
                }
                if (marked[u] == 0) {
                    marked[u] = marked[x];
                    distTo[u] = distTo[x] + 1;
                    q.enqueue(u);
                }
            }
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        Queue<Integer> q = new Queue<>();
        int[] marked
                = new int[G.V()]; // marked[v] = 0: not touched, 1: touched by v synsets, 2: touched by w synsets
        for (Integer x : v) {
            marked[x] = 1;
            q.enqueue(x);
        }
        for (Integer x : w) {
            if (marked[x] == 1) return x;
            marked[x] = 2;
            q.enqueue(x);
        }

        while (!q.isEmpty()) {
            int x = q.dequeue();
            for (int u : G.adj(x)) {
                if (marked[u] != marked[x] && marked[u] != 0) {
                    return u;
                }
                if (marked[u] == 0) {
                    marked[u] = marked[x];
                    q.enqueue(u);
                }
            }
        }
        return -1;
    }


    private void validateVertex(int v) {
        int V = this.G.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int vertexCount = 0;
        for (Integer v : vertices) {
            vertexCount++;
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(v);
        }
        if (vertexCount == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
