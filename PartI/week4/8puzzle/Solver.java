/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private boolean DEBUG = false;
    // find a solution to the initial board (using the A* algorithm)
    private Board initial;
    private MinPQ<Node> pq;
    private Stack<Board> boards;
    private boolean solvable = true;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        this.initial = initial;
        Board twin = initial.twin();
        Comparator<Node> comparator = (o1, o2) ->
                o1.priority - o2.priority;
        pq = new MinPQ<Node>(comparator);
        MinPQ<Node> twinPQ = new MinPQ<Node>(comparator);
        boards = new Stack<Board>();

        Node tail = new Node(initial, null, 0);
        Node twinTail = new Node(twin, null, 0);

        pq.insert(tail);
        twinPQ.insert(twinTail);
        while (!tail.board.isGoal() && !twinTail.board.isGoal()) {

            tail = pq.delMin();
            twinTail = twinPQ.delMin();

            if (DEBUG)
                System.out.println(tail.board);

            Iterable<Board> neighbors = tail.board.neighbors();
            for (Board b : neighbors) {
                if (tail.prev != null && b.equals(tail.prev.board))
                    continue; // do not add previous board;
                pq.insert(new Node(b, tail));
            }

            Iterable<Board> twinNeighbors = twinTail.board.neighbors();
            for (Board b : twinNeighbors) {
                if (twinTail.prev != null && b.equals(twinTail.prev.board))
                    continue; // do not add previous board;
                twinPQ.insert(new Node(b, twinTail));
            }
            if (twinTail.board.isGoal()) {
                solvable = false;
            }


        }
        Node goal = tail;
        while (goal != null) {
            boards.push(goal.board);
            goal = goal.prev;
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;

    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? boards.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? boards : null;

    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable()) StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) StdOut.println(board);
        }
    }

    private class Node {
        Board board;
        Node prev;
        int moves;
        int priority;
        int manhattan;

        Node(Board board, Node prev, int moves) {
            this.board = board;
            this.moves = moves;
            this.manhattan = board.manhattan();
            this.priority = this.manhattan + moves;
            this.prev = prev;
        }

        Node(Board board, Node prev) {
            this(board, prev, prev.moves + 1);
        }
    }

}
