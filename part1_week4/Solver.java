import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.LinkedStack;

public class Solver {
    private MinPQ<Node> PQM;
    private MinPQ<Node> PQT;
    private LinkedStack<Board> solutionSeq;
    private boolean unsolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        Node min_main;
        Node min_twin;
        unsolvable = false;
        solutionSeq = new LinkedStack<Board>();

        if (initial == null) throw new IllegalArgumentException("argument is null");

        PQM = new MinPQ<Node>(new Node[] {new Node(initial, null)});
        PQT = new MinPQ<Node>(new Node[] {new Node(initial.twin(), null)});
        while (true) {
            // main process for primary board
            min_main = PQM.delMin();
            if (min_main.board.isGoal()) {
                buildSolution(min_main);
                break;
            }
            for (Board n : min_main.board.neighbors())
                min_main.addNeighborToPQ(PQM, n);

            // identical parallel process for twin on its own priority queue
            min_twin = PQT.delMin();
            if (min_twin.board.isGoal()) {
                unsolvable = true;
                break;
            }
            for (Board n : min_twin.board.neighbors())
                min_twin.addNeighborToPQ(PQT, n);
        }
    }

    // convert linked sequence of Nodes into a proper Queue
    private void buildSolution(Node endpoint) {
        LinkedStack<Board> result = new LinkedStack<Board>();
        Node curr = endpoint;
        while (true) {
            result.push(curr.board);
            if (curr.previous == null) break;
            curr = curr.previous;
        }
        // free up memory
        PQM = new MinPQ<Node>();
        PQT = new MinPQ<Node>();
        solutionSeq = result;
    }

    private class Node implements Comparable<Node> {
        private final Node previous;
        private final Board board;
        private final int priority;
        private int cost;

        public Node(Board newBoard, Node previous) {
            this.previous = previous;
            this.board = newBoard;
            if (previous == null) this.cost = 0;
            else this.cost = previous.cost + 1;
            this.priority = this.cost + this.board.manhattan();
        }

        // if new board is not in immediately preceding Node, add it to respective PQ
        public void addNeighborToPQ(MinPQ<Node> pq, Board addition) {
            if (this.previous == null) {
                pq.insert(new Node(addition, this));
            } else if (!addition.equals(this.previous.board)) {
                pq.insert(new Node(addition, this));
            }
            return;
        }

        public int compareTo(Node that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return 1;
            return 0;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return !unsolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solutionSeq.size() - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!unsolvable) {
            Queue<Board> result = new Queue<Board>();
            for (Board i : solutionSeq) result.enqueue(i);
            return result;
        } else
            return null;
    }

    // test client (see below) 
    public static void main(String[] args) {}
}
