import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
        // construct an empty randomized queue
        private Item[] queue;
        private int current = 0;

        public RandomizedQueue(){
            queue = (Item[]) new Object[1];
        }

        // is the randomized queue empty?
        public boolean isEmpty() {
            return current  == 0;
        }
    
        // return the number of items on the randomized queue
        public int size() {
            return current;
        }
    
        // add the item
        public void enqueue(Item item) {
            if(item == null){
                throw new IllegalArgumentException();
            }

            if (current == queue.length) {
                resize(queue.length * 2);
            }
            queue[current] = item;
            current++;
        }
    
        // remove and return a random item
        public Item dequeue() {

            if (isEmpty()) {
                throw new  NoSuchElementException();
            }

            if (current == queue.length /4) {
                resize(queue.length / 2);
            }
            int random = StdRandom.uniform(current);
            Item removed  = queue[random];
            queue[random] = queue[current - 1];
            current--;
            queue[current] = null;
            return removed;
        }

        private void resize(int capacity) {
            assert capacity >= current;
           Item[] newQueue = (Item[]) new Object[capacity];
           for (int i = 0; i < current; i++) {
               newQueue[i] = queue[i];
           }

           queue = newQueue;
        }
    
        // return a random item (but do not remove it)
        public Item sample() {
            if (isEmpty()) {
                throw new  NoSuchElementException();
            }

            return queue[StdRandom.uniform(current)];
        }
    
        // return an independent iterator over items in random order
        // public Iterator<Item> iterator()
        public Iterator<Item> iterator() {
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Item> {
            int cursor = 0;
            int[] previousRandoms = new int[current];

            @Override
            public boolean hasNext() {
                return cursor < current;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                
                int random = StdRandom.uniform(current);

                while (previousRandoms[random] == -1) {
                    random = StdRandom.uniform(current);
                }
               
                previousRandoms[random] = -1; 
                cursor++;
                return queue[random];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        // unit testing (required)
        public static void main(String[] args) {
            int n = 5;
            RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
            for (int i = 0; i < n; i++)
                queue.enqueue(i);
            for (int a : queue) {
                for (int b : queue)
                    StdOut.print(a + "-" + b + " ");
                StdOut.println();
        }
    }
}
