// import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private WeightedQuickUnionUF ufInstance;
    private byte[] openness;
    private byte[] fullness;
    private int n;

    public Percolation(int n) {
        if (n <= 0) { throw new IllegalArgumentException(); }
        this.n = n;
        ufInstance = new WeightedQuickUnionUF(n * n + 2);
        openness = new byte[n * n];
        fullness = new byte[n * n];
    }

    private int oneDimId(int i, int j) {
        return ((i - 1) * n + j - 1);
    }

    private int[] twoDimId(int id) {
        int[] result = new int[2];
        result[0] = (int) Math.ceil((double) (id + 1) / (double) n);
        result[1] = (id + 1) - (result[0] - 1) * n;
        return (result);
    }

    private int[] neighbors(int i, int j) {
        int[] result = new int[4];
        int id = oneDimId(i, j);

        if (j == 1) { result[0] = id; }  // left edge
        else { result[0] = id - 1; }

        if (i == 1) { result[1] = id; }  // top edge
        else { result[1] = id - n; }

        if (j == n) { result[2] = id; } // right edge
        else { result[2] = id + 1; }

        if (i == n) { result[3] = id; } // bottom edge
        else { result[3] = id + n; }
        return (result);
    }

    private void flow(int i, int j) {
        int id = oneDimId(i, j);
        fullness[id] = 1;

        int[] neighbors = neighbors(i, j);
        for (int thisNeighbor : neighbors) {
            if (openness[thisNeighbor] == 1 && id != thisNeighbor && fullness[thisNeighbor] == 0) {
                int[] nCoords = twoDimId(thisNeighbor); 
                flow(nCoords[0], nCoords[1]);
            }
        }
    }

    public void open(int i, int j) {
        if ((i <= 0 || i > n) || (j <= 0 || j > n)) {
            throw new IndexOutOfBoundsException();
        }

        int id = oneDimId(i, j);
        if (openness[id] == 0) {
            openness[id] = 1;
            
            // if on top row, connect to TOP virtual component and fill
            if (i == 1) {
                ufInstance.union(id, n * n);
                flow(i, j);
            }
            // if on bottom row, connect to BOTTOM virtual component
            if (i == n) {
                ufInstance.union(id, n * n + 1);
            }

            int[] neighbors = neighbors(i, j);

            // if any of the NEIGHBORS is connected to top VC, flow into current site
            // this will fill current site and all its open neigbors
            // this is done BEFORE connecting current element

            for (int thisNeighbor : neighbors) {
                if (id != thisNeighbor && fullness[thisNeighbor] == 1) {
                    flow(i, j);
                }
            }

            for (int thisNeighbor : neighbors) {
                if (openness[thisNeighbor] == 1) {
                    ufInstance.union(id, thisNeighbor);
                }
            }
        }
    }

    public boolean isOpen(int i, int j) {    // is site (row i, column j) open?
        if ((i <= 0 || i > n) || (j <= 0 || j > n)) {
            throw new IndexOutOfBoundsException();
        }
        int id = oneDimId(i, j);
        return (openness[id] == 1);
    }

    public boolean isFull(int i, int j) {   // is site (row i, column j) full?
        if ((i <= 0 || i > n) || (j <= 0 || j > n)) {
            throw new IndexOutOfBoundsException();
        }
        int id = oneDimId(i, j);
        return (fullness[id] == 1);
    }

    public boolean percolates() {             // does the system percolate?
        return (ufInstance.connected(n * n, n * n + 1));
    }

    public static void main(String[] args) {  // test client (optional)
    }
}