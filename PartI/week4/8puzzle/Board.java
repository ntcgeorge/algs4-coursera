import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;


public class Board {
    private final int N;
    private int[][] array2D;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.N = tiles.length;
        array2D = new int[N][N];
        int pos = 0;
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                array2D[j][i] = tiles[j][i];
                pos++;
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int pos;
        sb.append(N);
        for (int i = 0; i < N; i++) {
            sb.append('\n');
            for (int j = 0; j < N; j++) {
                sb.append(" ");
                sb.append(array2D[i][j]);
            }
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int cnt = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (array2D[i][j] == 0) continue;
                if (array2D[i][j] != i * N + j + 1) cnt++;
            }
        return cnt;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // score(pos, array[pos])
        /**
         * observation:
         * 1. convert the index to col and row
         * 2. distance = delta(row) + delta(col)
         */
        int dis = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (array2D[i][j] == 0) continue;
                int col1, col2, row1, row2;

                // the excepted row and col column
                row1 = (array2D[i][j] - 1) / N + 1;
                col1 = array2D[i][j] % N != 0 ? array2D[i][j] % N : N;
                // the index row and col
                row2 = i + 1;
                col2 = j + 1;
                dis += Math.abs(row1 - row2) + Math.abs(col1 - col2);
            }
        return dis;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                int value = array2D[i][j];
                if (value != 0 && value != i * N + j + 1) return false;
            }
        return true;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !(y.getClass() == this.getClass())) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        return this.toString().equals(that.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        int row, col, leftCol, rightCol, upRow, downRow;
        ArrayList<Board> boards = new ArrayList<>();

        int[] pos = blank();
        row = pos[0];
        col = pos[1];
        leftCol = col - 1;
        rightCol = col + 1;
        upRow = row - 1;
        downRow = row + 1;

        validateAndAdd(upRow, col, row, col, boards);
        validateAndAdd(row, leftCol, row, col, boards);
        validateAndAdd(row, rightCol, row, col, boards);
        validateAndAdd(downRow, col, row, col, boards);
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles

    public Board twin() {
        int[][] twin = copy2DArray(array2D);
        // arbitrarily swaps 2 items in square for 2 <= N
        // blank square must not be swapped
        if (array2D[0][0] != 0 && array2D[0][1] != 0) {
            twin[0][0] = array2D[0][1];
            twin[0][1] = array2D[0][0];
        }
        else {
            twin[1][0] = array2D[1][1];
            twin[1][1] = array2D[1][0];
        }
        return new Board(twin);
    }

    private int[] blank() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (array2D[i][j] == 0) return new int[] { i, j };
            }
        }
        return new int[] { 0, 0 };
    }


    // unit testing (not graded)
    private int[][] copy2DArray(int[][] array) {
        int[][] copy = new int[array.length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                copy[i][j] = array[i][j];
            }
        }
        return copy;
    }

    private void validateAndAdd(int row, int col, int currRow, int currCol,
                                ArrayList<Board> boards) {
        if (row > N - 1 || row < 0 || col > N - 1 || col < 0) return;
        int[][] tiles = copy2DArray(array2D);
        // swap
        int temp = tiles[row][col];
        assert tiles[currRow][currCol] == 0;
        tiles[row][col] = 0;
        tiles[currRow][currCol] = temp;
        boards.add(new Board(tiles));
    }

    public static void main(String[] args) {
        In in = new In("input.txt");
        int[][] tiles = new int[3][3];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++)
                tiles[i][j] = in.readInt();
        }
        Board board = new Board(tiles);
        StdOut.println(board);
        StdOut.println(board.manhattan());
    }
}
