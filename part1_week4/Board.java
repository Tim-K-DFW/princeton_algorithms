import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.lang.Math;

public class Board {
    private final int N, L;
    // private short[][] arr;
    private final short[] arr1d;
    private short blank_r, blank_c;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        N = tiles.length;
        L = N * N;

        short[] res1d = new short[L];
        short k = 0;
        for (short i = 0; i < N; i++) {
            for (short j = 0; j < N; j++) {
                res1d[k] = (short) tiles[i][j];
                if (res1d[k] == 0) {
                    blank_r = i;
                    blank_c = j;
                }
                k++;
            }
        }
        arr1d = res1d;
    }

    public String toString() {
        short[][] arr = tempArray(arr1d, N);
        String res = "";
        res = res.concat(Integer.toString(N) + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) res = res.concat("  " + arr[i][j]);
            res = res.concat("\n");
        }
        return res;
    }    

    // board dimension n
    public int dimension() {
        return N;
    }

    public int hamming() {
        int res = 0;
        for (int i = 0; i < L; i++)
            if (arr1d[i] != 0 && arr1d[i] != goalEntry1d(i)) res++;
        return res;
    }

    public int manhattan() {
        short[][] arr = tempArray(arr1d, N);
        int res = 0;
        int[] tc = new int[2];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (arr[i][j] != 0 && arr[i][j] != goalEntry0ind(i, j)) {
                    tc = targetCoords0ind(arr[i][j], N);
                    res += Math.abs(i - tc[0]);
                    res += Math.abs(j - tc[1]);
                }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < L; i++)
                if (arr1d[i] != goalEntry1d(i)) { return false; }
        return true;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int i = 0; i < L; i++)
            if (that.arr1d[i] != this.arr1d[i]) return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedStack<Board> result = new LinkedStack<Board>();
        LinkedStack<short[]> swaps = getSwaps();
        for (short[] i : swaps) {
            result.push(swapTiles1d(i, new short[] {blank_r, blank_c}));
        }
        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        short[] s1;
        short[] s2;
        if (blank_r == 0 && blank_c == 0) {
            s1 = new short[]{0, 1};
            s2 = new short[]{1, 0};
        } else {
            s1 = new short[]{0, 0};
            if (blank_r == 1 && blank_c == 0) s2 = new short[]{0, 1};
            else                                s2 = new short[]{1, 0};
        }
        return swapTiles1d(s1, s2);
    }

    // helper methods

    // compute {tr, tc} of v; where tr - target row, tc - target column; 0-indexed
    private static int[] targetCoords0ind(int v, int N) {
        int tr = (v - 1) / N;
        int tc = v - (tr * N + 1);
        return new int[] {tr, tc};
    }

    // integer that goal board has at coordinate [r, c], 0-indexed
    private int goalEntry0ind(int r, int c) {
        if (r == N - 1 && c == N - 1) return 0;
        else return N * r + c + 1;
    }

    // integer that goal board has at index ind, in 1-d array... trivial
    private int goalEntry1d(int ind) {
        if (ind == L - 1) return 0;
        else return ind + 1;
    }

    // 2d representation of array, convenience for neighbors and distances
    private static short[][] tempArray(short[] arr, int N) {
        short[][] result = new short[N][N];
        int k = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                result[i][j] = arr[k++];
        return result;
    }

    // get a stack of size in (2, 4) with all possible swaps to blank
    private LinkedStack<short []> getSwaps() {
        LinkedStack<short []> result = new LinkedStack<short []>();
        // scan 4 directions, add to result if not edge
        if (blank_r > 0) result.push(new short[] {(short) (blank_r - 1), blank_c});          // above
        if (blank_c < N - 1) result.push(new short[] {blank_r, (short) (blank_c + 1)});      // right
        if (blank_r < N - 1) result.push(new short[] {(short) (blank_r + 1), blank_c});      // below
        if (blank_c > 0) result.push(new short[] {blank_r, (short) (blank_c - 1)});          // left
        return result;
    }

    // a new board where two tiles at argument coordinates are swapped, using this for neighbors and twin
    private Board swapTiles1d(short[] swap1, short[] swap2) {
        short[][] arr = tempArray(arr1d, N);
        int[][] tempTiles = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                tempTiles[i][j] = (int) arr[i][j];
        int temp = tempTiles[swap1[0]][swap1[1]];
        tempTiles[swap1[0]][swap1[1]] = tempTiles[swap2[0]][swap2[1]];
        tempTiles[swap2[0]][swap2[1]] = temp;
        return new Board(tempTiles);
    }

    // end helper methods

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
