import java.util.ArrayList;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private final Node goalNode;
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<Node> minPQ = new MinPQ<Node>();
        MinPQ<Node> twinPQ = new MinPQ<Node>();
        int moves = 0;
        minPQ.insert(new Node(null, initial, moves));
        twinPQ.insert(new Node(null, initial.twin(), moves));

        while (true) {
            Node currentNode = minPQ.delMin();
            if (currentNode.current.isGoal()) {
                this.goalNode = currentNode;
                this.solvable = true;
                return;
            }

            for (Board b : currentNode.current.neighbors()) {
                if (currentNode.previous != null && currentNode.previous.current.equals(b)) {
                    continue;
                }

                minPQ.insert(new Node(currentNode, b, currentNode.moves + 1));
            }

            Node twinNode = twinPQ.delMin();
            if (twinNode.current.isGoal()) {
                this.goalNode = null;
                this.solvable = false;
                return;
            }

            for (Board b : twinNode.current.neighbors()) {
                if (twinNode.previous != null && twinNode.previous.current.equals(b)) {
                    continue;
                }

                twinPQ.insert(new Node(twinNode, b, twinNode.moves + 1));
            }

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if(goalNode == null){
            return -1;
        }

        return goalNode.moves;
    }

    private class Node implements Comparable<Node> {

        final Node previous;
        final Board current;
        private final int moves;
        private final int manhattan;

        public Node(Node previous, Board current, int moves) {
            this.previous = previous;
            this.current = current;
            this.moves = moves;
            manhattan = this.current.manhattan();
        }

        public int cost() {
            return this.moves + this.manhattan;
        }

        @Override
        public int compareTo(Node that) {
            if (this.cost() != that.cost())
                return this.cost() - that.cost();
            if (this.manhattan != that.manhattan)
                return this.manhattan - that.manhattan;

            return this.moves - that.moves;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        if (!solvable) {
            return null;
        }

        ArrayList<Board> list = new ArrayList<Board>();
        Node node = goalNode;
        while (true) {
            
            list.add(node.current);
            if (node.previous == null) {
                break;
            }
            node = node.previous;
        }

        ArrayList<Board> revArrayList = new ArrayList<Board>();
        for (int i = list.size() - 1; i >= 0; i--) {

            revArrayList.add(list.get(i));
        }
        return revArrayList;
    }
}
