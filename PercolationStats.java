import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
    
    private double[] threshold;

    // perform several independent trials on N-by-N grid
    public PercolationStats(int N, int trials) {

        int openCount, row, column;

        if (N <= 0 || trials <= 0)
            throw new IllegalArgumentException("Arguments out of bound");

        threshold = new double[trials];
        
        openCount = 0;
        for (int i = 0; i < trials; i++) {
			//initalize closed grid and choose random sites to open until it percolates
            Percolation pl = new Percolation(N);
            do {
                row     = StdRandom.uniform(1, N+1);
                column  = StdRandom.uniform(1, N+1);
                if (pl.isOpen(row, column))
                    continue;
                pl.open(row, column);
                openCount++;
            } while (!pl.percolates());

            threshold[i] = (double) openCount / (N * N);
            openCount = 0;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // 95% confidence level
    private double halfInterval() {
        return 1.96 * stddev() / Math.sqrt(threshold.length);
    }
    
    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return mean() - halfInterval();
    }
    
    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return mean() + halfInterval();
    }
    
    // Check statistics after running everything
    public static void main(String[] args) {
        PercolationStats pls = new PercolationStats(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]));

        System.out.printf("mean                     = %f\n", pls.mean());
        System.out.printf("stddev                   = %f\n", pls.stddev());
        System.out.printf("95%% confidence Interval  = %f, %f\n",
                pls.confidenceLo(), pls.confidenceHi());
    }
}
