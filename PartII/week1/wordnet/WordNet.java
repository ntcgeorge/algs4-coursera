import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * the trick is we need to find a way to store the string, a synset is
 * a list of string, but each string can be in more than one synset,
 * therefore we use arraylist to store the noun string, and use a hastmap
 * to hold the instance of the string and its corresponding indexes since each
 * string object has only one instance in JVM, there is no big overhead caused
 * by the duplicates except for the pointer.
 */
public class WordNet {
    private Digraph digraph;
    private HashMap<Integer, ArrayList<String>> idToNouns; // synset to its corresponding indexes
    private HashMap<String, ArrayList<Integer>> nounToIds;
    // private final SAP sap;
    private boolean[] onStack;
    private boolean[] marked;
    private boolean hasCycle;
    private HashSet<Integer> rootSet;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        idToNouns = new HashMap<>();
        nounToIds = new HashMap<>();
        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);
        while (!synsetsIn.isEmpty()) {
            String line = synsetsIn.readLine();
            String[] p1 = line.split(",");
            int index = Integer.parseInt(p1[0]);
            String synset = p1[1];
            String[] nouns = synset.split("\\s");
            for (String noun : nouns) {
                if (nounToIds.containsKey(noun)) {
                    nounToIds.get(noun).add(index);
                }
                else {
                    ArrayList<Integer> arr = new ArrayList<>();
                    arr.add(index);
                    nounToIds.put(noun, arr);
                }
            }


            // put it into the idToNouns
            ArrayList<String> newsynset = new ArrayList<>();
            Collections.addAll(newsynset, nouns);
            idToNouns.put(index, newsynset);
        }

        digraph = new Digraph(idToNouns.size());
        while (!hypernymsIn.isEmpty()) {
            String line = hypernymsIn.readLine();
            String[] parts = line.split(",");
            int source = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                digraph.addEdge(source, Integer.parseInt(parts[i]));
            }
        }
        sap = new SAP(digraph);
        // instantiate variables for validation
        marked = new boolean[digraph.V()];
        onStack = new boolean[digraph.V()];
        rootSet = new HashSet<>();
        validate();

    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("the argument to isNoun() is null");
        return nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("the noun is not in wordnet.");
        return sap.length(nounToIds.get(nounA), nounToIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("the noun is not in wordnet.");
        ArrayList<Integer> idxOfNounA = nounToIds.get(nounA);
        ArrayList<Integer> idxOfNounB = nounToIds.get(nounB);
        int v = sap.ancestor(idxOfNounA, idxOfNounB);
        ArrayList<String> strs = idToNouns.get(v);
        return String.join(" ", strs);
    }


    // apply dfs to determine if the digraph is a rooted DAG.
    private void validate() {
        for (int v = 0; v < digraph.V(); v++)
            if (!marked[v]) dfs(digraph, v);
        if (hasCycle && rootSet.size() > 1)
            throw new IllegalArgumentException(
                    "the digraph is not acyclic and has more than one root");
        if (hasCycle)
            throw new IllegalArgumentException("the digraph is not acyclic");
        if (rootSet.size() > 1)
            throw new IllegalArgumentException("the diagraph has more than one root");
    }

    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        int count = 0;
        for (int x : G.adj(v)) {
            count++;

            if (!marked[x]) {
                dfs(G, x);
            }
            // find the cycle
            else if (onStack[x]) {
                hasCycle = true;
            }
        }
        onStack[v] = false;
        if (count == 0) rootSet.add(v); // v is root


    }

    // do unit testing of this class
    public static void main(String[] args) {
        String snym = args[0];
        String hnym = args[1];
        WordNet wn = new WordNet(snym, hnym);
        for (String noun : wn.nouns()) {
            StdOut.println(noun);
        }
    }
}
