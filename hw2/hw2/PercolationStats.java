package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;



public class PercolationStats {
    private PercolationFactory pf;
    private int times;
    private double[] data;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        times = T;
        data = new double[T];
        int sumSize = N * N;
        for (int i = 0; i < times; i += 1) {
            Percolation p = pf.make(N);
            int count = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    count += 1;
                }
                if (p.numberOfOpenSites() == sumSize) {
                    break;
                }
            }
            double temp = (double) count / sumSize;
            data[i] = temp;
        }
    }

    // sample mean of percolation threshold
    //计算渗透阈值的样本平均值。
    public double mean() {
        return StdStats.mean(data);
    }

    // sample standard deviation of percolation threshold
    //计算渗透阈值的样本标准差
    public double stddev() {
        return StdStats.stddev(data);
    }

    // low endpoint of 95% confidence interval
    //计算 95% 置信区间的下界
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(times);
    }

    // high endpoint of 95% confidence interval
    //计算 95% 置信区间的上界
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(times);
    }

    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();
        PercolationStats p = new PercolationStats(1000, 100, pf);
        Stopwatch partTimer = new Stopwatch();
        System.out.print("mean: " + p.mean());
        System.out.print("  stddev:  " + p.stddev());
        System.out.printf("  [ %f ,%f ]", p.confidenceLow(), p.confidenceHigh());
        double time = partTimer.elapsedTime();
        System.out.print("  time : " + time);
        /*
         * N = 100 T = 500
         * mean: 0.5937205999999996
         * stddev:  0.016298349242056143
         *  [ 0.592292 ,0.595149 ]  time : 0.029
         * N = 200 T = 500
         * mean: 0.5922076999999999
         * stddev:  0.009461141956669323
         *  [ 0.591378 ,0.593037 ]  time : 0.024
         * N = 400  T = 500
         * mean: 0.5925924875000002
         * stddev:  0.005673238899181237
         *  [ 0.592095 ,0.593090 ]  time : 0.027

         */
    }
}
