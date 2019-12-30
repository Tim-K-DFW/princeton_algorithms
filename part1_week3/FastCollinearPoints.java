import java.util.Arrays;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int N;
    private Point[] pointArr;
    private LinkedStack<SegmentBetter> resultStack = new LinkedStack<SegmentBetter>();
    private LineSegment[] segmentArr;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("argument is null");
        N = points.length;
        copyInput(points);
        if (N > 1) computeCollinear();
        convertStack2Arr();
    }

    private class SegmentBetter implements Comparable<SegmentBetter> {
        // unlike LineSegment, supports sorting using Point's native ordering
        // this is so that we can use linked list with linear time and remove duplicates once at the end
        // (need sorting for that)
        // fails style check for "poor design"... sure I'd put it in a separate file if not for submission specs
        public Point p, q;

        public SegmentBetter(Point p, Point q) {
            // always keep "smaller" point first
            if (p.compareTo(q) < 1) {
                this.p = p;
                this.q = q;
            } else {
                this.p = q;
                this.q = p;
            }
        }

        public int compareTo(SegmentBetter that) {
            // generalizing what Point does
            if (this.q.compareTo(that.q) < 0) return -1;
            if (this.q.compareTo(that.q) > 0) return 1;
            if (this.p.compareTo(that.p) < 0) return -1;
            if (this.p.compareTo(that.p) > 0) return 1;
            return 0;
        }
    }

    private void copyInput(Point[] inputArr) {
        Point[] newArr = new Point[N];

        for (int i = 0; i < N; i++) {
            if (inputArr[i] == null) { throw new IllegalArgumentException("argument is null"); }
            newArr[i] = inputArr[i];
        }
        pointArr = newArr;

        Arrays.sort(pointArr);
        for (int i = 0; i < N - 1; i++) {
            if (pointArr[i].compareTo(pointArr[i + 1]) == 0) { throw new IllegalArgumentException("duplicate point in input"); }
            if (pointArr[i] == null) { throw new IllegalArgumentException("argument is null"); }
        }
        if (pointArr[N - 1] == null) { throw new IllegalArgumentException("argument is null"); }
    }

    private void computeCollinear() {
        // copy of points array that we'll be sorting by slope, need to keep original array intact
        Point[] pointArrCopy = new Point[N];
        for (int i = 0; i < N; i++) pointArrCopy[i] = pointArr[i];

        Point currPoint;
        for (int i = 0; i < N; i++) {
            currPoint = pointArr[i];
            Arrays.sort(pointArrCopy, currPoint.slopeOrder());
            checkAdjacent(currPoint, pointArrCopy);
        }
    }

    private void checkAdjacent(Point p, Point[] points) {
        int i = 0;
        int j = 1;
        boolean block = false;
        double[] slopes = new double[N];

        slopes[0] = p.slopeTo(points[0]);
        slopes[1] = p.slopeTo(points[1]);

        // main loop, adds slopes one by one and finds adjacent blocks as it goes
        while (j < N - 1) {
            while (slopes[j] == slopes[i]) {
                // once we see a contigous block, keep incrementing j until it either ends or we reach array's end
                block = true;
                j++;
                if (j == N) break;
                slopes[j] = p.slopeTo(points[j]);
            }
            if (block) {
                if (j - i >= 3) { addSegment(p, i, j, points); }   // add segment from this block only if it is at least 3 long
                // no matter if the block qualified, we need to reset its start (i), unless it's array end
                block = false;
                if (j < N - 2) {                                    // near end of array, we only increment if there are least 3 points left
                    i = j;
                    j = i + 1;
                    slopes[j] = p.slopeTo(points[j]);
                }
            } else {                                                // not a block, just increment both by 1 and keep ckecking
                if (j < N - 1) {
                    i++;
                    j++;
                    slopes[j] = p.slopeTo(points[j]); 
                }
            }
        }
    }

    private void addSegment(Point p, int i, int j, Point[] points) {
        Point[] tempAdjacent = new Point[j - i + 1];                // temp array, including calling point
        for (int k = i; k <= j - 1; k++) {
            tempAdjacent[k - i] = points[k];
        }
        tempAdjacent[tempAdjacent.length - 1] = p;
        Arrays.sort(tempAdjacent);
        Point[] addition = new Point[]{tempAdjacent[0], tempAdjacent[tempAdjacent.length - 1]};
        resultStack.push(new SegmentBetter(addition[0], addition[1]));
    }

    private void convertStack2Arr() {
        // coverts linked stak of massively duplicated segments
        // to a proper array of unique LineSegment

        // create array of SegmentBetter objects and sort
        int k = resultStack.size();
        SegmentBetter[] newArrDup = new SegmentBetter[k];
        for (int i = 0; i < k; i++) newArrDup[i] = resultStack.pop();
        Arrays.sort(newArrDup);

        // create de-duplicated version
        LineSegment[] newArrDedup = new LineSegment[newArrDup.length];
        int j = 0;
        // dummy segment that's guaranteed to be not equal to anything in newArr
        SegmentBetter curr = new SegmentBetter(new Point(-1, -1), new Point(-1, -1));
        for (int i = 0; i < newArrDup.length; i++) {
            if (newArrDup[i].compareTo(curr) != 0) {
                curr = newArrDup[i];
                newArrDedup[j++] = new LineSegment(newArrDup[i].p, newArrDup[i].q);
            }
        }

        // j now has length of actual elements of newArrDedup (the rest are zeros)
        // now we can finally save to instance variable in proper LineSegment format
        LineSegment[] newArrFinal = new LineSegment[j];
        for (int i = 0; i < j; i++) newArrFinal[i] = newArrDedup[i];
        segmentArr = newArrFinal;
    }

    public int numberOfSegments() {
        return segmentArr.length;
    }                    

    public LineSegment[] segments() {
        // returning instance variable fails immutability test, so making a copy
        int k = segmentArr.length;
        LineSegment[] result = new LineSegment[k];
        for (int i = 0; i < k; i++) result[i] = segmentArr[i];
        return result;
    }

    
    // for testing, doesn't affect submission
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
