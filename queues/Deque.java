import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
   
    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node first;
    private Node last;
    private int qSize = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null && last == null;
    }

    // return the number of items on the deque
    public int size() {
        return qSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node newFirst = new Node();
        newFirst.item =  item;
        if (first != null) {
            first.prev = newFirst;
            newFirst.next = first;
        }

        if (last == null) {
            last = newFirst;
        }

        first = newFirst;
        qSize++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node newLast = new Node();
        newLast.item =  item;
        if (last != null) {
            newLast.prev = last;
            last.next = newLast;
        }

        if (first == null) {
            first = newLast;
        }

        last = newLast;
        qSize++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        Node oldFirst  = first;
        if (first.next != null) {
            first = first.next;
            first.prev = null;
        } else {
            first = null;
            last = null;
        }
        qSize--;
        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }

        Node oldLast = last;
        if (oldLast.prev != null) {
            last = last.prev;
            last.next = null;
        } else {
            last = null;
            first = null;
        }
        qSize--;
        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator(); 
    }

    private class ListIterator implements Iterator<Item> {
       private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null; 
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(11);
        deque.addFirst(12);
        deque.addFirst(13);
        deque.addLast(14);
        int item = deque.removeLast();
        if (item != 14) {
            System.out.println("last item should be 14 but it's " + item);
        }
        
        System.out.println("Testing Iterator");
        for (int s :deque) {
            System.out.println(s);
        }

        deque.removeLast();
        deque.removeLast();
        deque.removeLast();

        if (deque.size() != 0) {
            System.out.println("size should be empty but returning " + deque.size());
        }

        if (!deque.isEmpty()) {
            System.out.println("size should be empty.");
        }

        deque.addFirst(11);
        deque.removeFirst();
        deque.addLast(11);
        deque.removeLast();
        
        if (deque.size() != 0) {
            System.out.println("size should be empty but returning " + deque.size());
        }
    }
}