/* *****************************************************************************
 *  Name: Jiqiao Lu
 *  Date: 18 Sep 2023
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }
        for (String str : rq) {
            if (k <= 0) break;
            StdOut.println(str);
            k--;
        }
    }
}
