// import Percolation;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] results;
    private int trials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) { throw new IllegalArgumentException(); }

        results = new double[trials];
        int totalSites = n * n;
        this.trials = trials;

        for (int trial = 0; trial < trials; trial++) {
            Percolation test = new Percolation(n);
            int openSites = 0;
            do {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                if (!test.isOpen(i, j)) { openSites++; }
                test.open(i, j);
            } while (!test.percolates());
            results[trial] = (double) openSites / (double) totalSites;
        }
    }

    public double mean() {
        return (StdStats.mean(results));
    }

    public double stddev() {
        return (StdStats.stddev(results));
    }


    public double confidenceLo() {
        return (mean() - (1.96 * stddev()/Math.sqrt(trials)));
    }
    public double confidenceHi() {
        return (mean() + (1.96 * stddev()/Math.sqrt(trials)));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats thisTest = new PercolationStats(n, trials);

        // System.out.println("mean = " + thisTest.mean());
        System.out.format("%-23s = %.20f%n", "mean", thisTest.mean());
        System.out.format("%-23s = %.20f%n", "stddev", thisTest.stddev());
        System.out.format("%-23s = ", "95% confidence interval");
        System.out.format("%.20f,", thisTest.confidenceLo());
        System.out.format("%.20f%n", thisTest.confidenceHi());
    }
}
