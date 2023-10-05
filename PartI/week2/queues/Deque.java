import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node head, tail;
    private int size;

    // construct an empty deque
    public Deque() {
        size = 0;
        this.head = new Node();
        this.tail = head;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Argument cannot be null");
        if (isEmpty()) {
            head.item = item;
            tail = head;
            size++;
        }
        else {
            Node newNode = new Node();
            newNode.item = item;
            newNode.next = head;
            head.prev = newNode;
            head = head.prev;
            size++;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Argument cannot be null");
        if (isEmpty()) {
            tail.item = item;
            head = tail;
            size++;
        }
        else {
            Node newNode = new Node();
            newNode.item = item;
            newNode.prev = tail;
            tail.next = newNode;
            tail = tail.next;
            size++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item first = head.item;
        if (head.next == null) {
            head = new Node();
            tail = head;
        }
        else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return first;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item last = tail.item;
        if (tail.prev == null) {
            tail = new Node();
            head = tail;
        }
        else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return last;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node curr = head;

        @Override
        public boolean hasNext() {
            if (isEmpty()) return false;
            return curr != null;
        }

        @Override
        public Item next() {
            if (curr == null) throw new NoSuchElementException();
            Item item = curr.item;
            curr = curr.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        // Deque<String> deque = new Deque<>();
        // deque.addFirst("Beyond");
        // deque.addLast("Coldplay");
        // deque.addFirst("Joey");
        // deque.addLast("TravisScout");
        // for (String s : deque) {
        //     System.out.print(s + " ");
        // }
        // deque.removeFirst();
        // deque.removeLast();
        // deque.addLast("Qiezidan");
        // deque.removeFirst();
        // System.out.println();
        // for (String s : deque) {
        //     System.out.print(s + " ");
        Deque<Integer> deque = new Deque<>();
        deque.size();
        deque.isEmpty();
        deque.size();
        deque.addLast(4);
        deque.removeFirst();
        deque.size();
        deque.size();
        deque.size();
        deque.addLast(9);
        deque.isEmpty();
        deque.removeFirst();

    }
}


