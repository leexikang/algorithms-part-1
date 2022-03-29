import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int n = 0;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (true) {
            try {
                rq.enqueue(StdIn.readString());
            } catch (NoSuchElementException e) {
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.println(rq.dequeue());
        }
    }
}
