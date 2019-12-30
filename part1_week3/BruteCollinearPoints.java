import java.util.Arrays;


// using slow array structure for output because this thing is O(N^4) anyways
public class BruteCollinearPoints {
    private int N;
    private Point[] pointArr;
    private LineSegment[] segmentArr;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("argument is null");
        N = points.length;
        copyInput(points);
        segmentArr = new LineSegment[0];
        computeCollinear();
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
        boolean coll;
        Point[] exts;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                for (int k = j + 1; k < N; k++) {
                    for (int l = k + 1; l < N; l++) {
                        // see if 4 points are collinear
                        coll = collinear4(pointArr[i], pointArr[j], pointArr[k], pointArr[l]);
                        // if they are, find two extremes, create new LineSegment, add to stack
                        if (coll) {
                            exts = extremes(pointArr[i], pointArr[j], pointArr[k], pointArr[l]);
                            appendToResult(new LineSegment(exts[0], exts[1]));
                        }
                    }
                }
            }
        }
    }

    private void appendToResult(LineSegment addition) {
        int k = numberOfSegments();
        LineSegment[] newArr = new LineSegment[k + 1];
        for (int i = 0; i < k; i++) newArr[i] = segmentArr[i];
        newArr[k] = addition;
        segmentArr = newArr;
    }

    private static boolean collinear4(Point p, Point q, Point r, Point s) {
        double pq = p.slopeTo(q);
        double pr = p.slopeTo(r);
        double ps = p.slopeTo(s);

        if (pq != ps) return false;
        if (pq != pr) return false;
        return true;
    }

    private static Point[] extremes(Point p, Point q, Point r, Point s) {
        // given 4 points that are known to be collinear, return 2-array of endpoints
        // using custom comparator we have in the Point class
        Point[] temp = {p, q, r, s};
        Arrays.sort(temp);
        return new Point[] {temp[0], temp[3]};
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

    // for testing, remove before submission
    // public static void main(String[] args) {
    //     // Point[] tt = new Point[] {new Point(1,2), new Point(2,4), new Point(3, 6), new Point(4,8), new Point(1,1), new Point(4,7), new Point(4, 6), new Point(4, 5)};
    //     // BruteCollinearPoints t2 = new BruteCollinearPoints(tt);
    //     // LineSegment[] t3 = t2.segments();
    //     // int i = 1;
    // }
}
