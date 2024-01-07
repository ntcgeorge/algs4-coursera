import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

/**
 * the time complexity should be up to square of the character on the board?
 */
public class BoggleSolver {
    private static class Node {
        Node left, mid, right;
        char c;
        boolean isString = false;
    }

    private Node root;

    public BoggleSolver(String[] dictionary) {
        // shuffle to give the randomness guarantee
        // StdRandom.shuffle(dictionary);
        for (String word : dictionary) {
            put(word);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int m = board.cols();
        int n = board.rows();
        StringBuilder prefix = new StringBuilder();
        HashSet<String> validaStrings = new HashSet<>();


        for (int i = 0; i < board.cols(); i++) {
            for (int j = 0; j < board.rows(); j++) {
                boolean[][] onStack = new boolean[m][n];
                dfs(root, board, prefix, i, j, onStack, validaStrings, false);
            }
        }
        return validaStrings;
    }


    public int scoreOf(String word) {
        if (!contains(word)) return 0;
        int n = word.length();
        if (n >= 3 && n <= 4) return 1;
        if (n == 5) return 2;
        if (n == 6) return 3;
        if (n == 7) return 5;
        if (n >= 8) return 11;
        return 0;
    }


    private void put(String key) {
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) {
            // System.out.println("go left");
            x.left = put(x.left, key, d);

        }
        else if (c > x.c) x.right = put(x.right, key, d);
        else if (d < key.length() - 1) x.mid = put(x.mid, key, d + 1);
        else x.isString = true;
        return x;
    }

    private boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        if (c > x.c) return get(x.right, key, d);
        if (d == key.length() - 1) return x;
        return get(x.mid, key, d + 1);

    }


    private void dfs(Node x, BoggleBoard board, StringBuilder prefix, int col, int row,
                     boolean[][] onStack, HashSet<String> validStrings, boolean u) {
        if (x == null) return;
        if (col >= board.cols() || col < 0) return;
        if (row >= board.rows() || row < 0) return;
        if (onStack[col][row]) return;
        char c = u ? 'U' : board.getLetter(row, col);
        int[] gap = new int[] { -1, 0, 1 };
        // search the tenary tree
        if (x.c > c) dfs(x.left, board, prefix, col, row, onStack, validStrings, false);
        else if (x.c < c) dfs(x.right, board, prefix, col, row, onStack, validStrings, false);
            // search hit
        else {
            prefix.append(x.c);
            // QU block
            if (c == 'Q') {
                dfs(x.mid, board, prefix, col, row, onStack, validStrings, true);
                prefix.deleteCharAt(prefix.length() - 1);
                return;
            }
            else if (x.isString && prefix.length() > 2) validStrings.add(prefix.toString());

            onStack[col][row] = true;

            int newCol, newRow;
            for (int g0 : gap) {
                newCol = col + g0;
                for (int g1 : gap) {
                    newRow = row + g1;
                    dfs(x.mid, board, prefix, newCol, newRow, onStack, validStrings, false);
                }
            }
            prefix.deleteCharAt(prefix.length() - 1);
            onStack[col][row] = false;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);

        int score = 0;
        int cnt = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            cnt++;
        }
        StdOut.println("Score = " + score);
        // String query = args[1];
        // StdOut.println(solver.contains(query));
        StdOut.println("The valid number of strings: " + cnt);
    }
}
