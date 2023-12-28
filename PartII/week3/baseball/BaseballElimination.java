import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * observation:
 * 1. artificially make a source vertex and target vertex
 * <p>
 * 2. the first column vertices are the correlational matches among the teams except for the x
 * <p>
 * 3. the second column vertices are the team except for x
 * <p>
 * 3. the edge from source to each vertex in the first column is the number of  game left between
 * gij
 * <p>
 * 4. the edge from the first column vertices to the second indicates the number of winning games
 * assignment between team i, j.
 * <p>
 * 5. the edge from the second column vertices to the target is the remaining number of wining
 * games that could allow the vertex x be the firs place, so it could not be larger than w[x] +
 * r[x]
 * - w[i]
 * <p>
 * 6. Elimination condition: maxflow means the max number of the winning games that could be
 * assigned to the remaining  teams in the same division is less than the number winning games that
 * could allow team x win, therefore the team x is not eliminated, otherwise it is eliminated.
 * <p>
 * An intuitive interpretation can be the inflow is the number of the wining games that are assigned
 * to the rest of team and the outflow is the max number allow to for the team x to get the first
 * place(tolerance) if the max tolerance(maxflow scenario) is less than the number of wining games
 * to
 * be assigned to the rest of teams, then it is not possible for the team to take the first place.-
 */


public class BaseballElimination {
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    private Map<String, Integer> teams;
    private String leader;
    private final int n;
    private HashMap<String, ArrayList<String>> certificates;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        int maxWin = Integer.MIN_VALUE;
        certificates = new HashMap<>();
        teams = new HashMap<>();
        In in = new In(filename);
        n = in.readInt();
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        for (int line = 0; line < n; line++) {
            String name = in.readString();
            teams.put(name, line);
            w[line] = in.readInt();
            if (w[line] > maxWin) {
                maxWin = w[line];
                leader = name;
            }
            l[line] = in.readInt();
            r[line] = in.readInt();
            for (int i = 0; i < n; i++) {
                int val = in.readInt();
                g[line][i] = val;
                g[i][line] = val;

            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        return w[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        return l[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        return r[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2))
            throw new IllegalArgumentException();
        return g[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException(team + " is not in the division");
        if (certificates.containsKey(team)) return certificates.get(team).size() != 0;
        certificates.put(team, new ArrayList<>());
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        int x = teams.get(team);
        // trivial case
        if (w[x] + r[x] < w[teams.get(leader)]) {
            certificates.get(team).add(leader);
            return true;
        }

        int games = ((n - 1) * (n - 2)) / 2;
        int t = games + n + 1;
        int s = 0;
        int count = 1;
        FlowNetwork fn = new FlowNetwork(t + 1);

        for (int i = 0; i < this.n; i++) {
            if (i == x) continue;
            for (int j = i + 1; j < this.n; j++) {
                // add games vertices
                if (j == x) continue;
                FlowEdge fe1 = new FlowEdge(s, count, g[i][j]);
                fn.addEdge(fe1);

                // add team vertices
                FlowEdge fe2 = new FlowEdge(count, i + games + 1, Double.POSITIVE_INFINITY);
                fn.addEdge(fe2);
                FlowEdge fe3 = new FlowEdge(count, j + games + 1, Double.POSITIVE_INFINITY);
                fn.addEdge(fe3);

                count++;
            }
        }

        // add the tolerance vertices
        for (int i = 0; i < this.n; i++) {
            if (i == x) continue;
            FlowEdge fe4 = new FlowEdge(i + games + 1, t, w[x] + r[x] - w[i]);
            fn.addEdge(fe4);
        }


        FordFulkerson ff = new FordFulkerson(fn, s, t);
        for (String name : teams.keySet()) {
            if (name.equals(team)) continue;
            // System.out.printf("index: %d\n", teams.get(name) + games + 1);
            if (ff.inCut(teams.get(name) + games + 1)) certificates.get(team).add(name);
        }
        return certificates.get(team).size() != 0;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team)) return null;
        return certificates.get(team);

    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
