/* *****************************************************************************
 *  Name: jiqiao Lu
 *  Date: 2023-02-17
 *  Description: Randomized queue. A randomized queue is similar to a stack or
 *  queue, except that the item removed is chosen uniformly at random among
 *  items in the data structure.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private final int INIT_SIZE = 8;
    private int size;
    private Item[] a;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        a = (Item[]) new Object[INIT_SIZE];
    }


    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int maxLength) {
        Item[] copy = (Item[]) new Object[maxLength];
        for (int i = 0; i < size; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == a.length) resize(size * 2);
        a[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int k = StdRandom.uniformInt(size);
        Item item = a[k];
        // replace with last item in the array since order does not matter
        if (k < size - 1) {
            Item temp = a[size - 1];
            a[size - 1] = null;
            a[k] = temp;
        }
        else {
            a[k] = null;
        }
        size--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int k = StdRandom.uniformInt(size);
        return a[k];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int[] idxs;
        int ptr;

        public RandomizedQueueIterator() {
            ptr = 0;
            idxs = new int[size];
            for (int i = 0; i < size; i++) {
                idxs[i] = i;
            }
            StdRandom.shuffle(idxs);
        }

        @Override
        public boolean hasNext() {
            return ptr < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item temp = a[idxs[ptr]];
            ptr++;
            return temp;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }
        StdOut.println(rq.sample());
        for (String s : rq) StdOut.print(s + " ");
        StdOut.println();
        System.out.println("remove" + " " + rq.dequeue());
        for (String s : rq) StdOut.print(s + " ");
    }
}
