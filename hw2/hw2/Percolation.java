package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    //(0,0)表示左下角

    private Site[][] array;
    private int openSize;
    private int virtualHead;
    private int virtualTail;
    private int N;
    private WeightedQuickUnionUF ufForPercolation;
    private WeightedQuickUnionUF ufForFullness;

    private class Site {
        boolean isOpen;
        int index;

        public Site(int i) {
            isOpen = false;
            index = i;
        }
    }

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        this.N = N;
        array = new Site[N][N];
        openSize = 0;
        virtualHead = 0;
        virtualTail = N * N + 1;
        int count = 1;

        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                array[i][j] = new Site(count);
                count += 1;
            }
        }

        ufForPercolation = new WeightedQuickUnionUF(N * N + 2);
        ufForFullness = new WeightedQuickUnionUF(N * N + 2);

        for (int i = 1; i <= N; i += 1) {
            ufForPercolation.union(virtualHead, i);
            ufForPercolation.union(virtualTail, virtualTail - i);
            ufForFullness.union(virtualHead, i);
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (array[row][col].isOpen) {
            return;
        }
        array[row][col].isOpen = true;
        openSize += 1;
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            ufForPercolation.union(indexChange(row - 1, col), indexChange(row, col));
            ufForFullness.union(indexChange(row - 1, col), indexChange(row, col));
        }
        if (row + 1 < N && isOpen(row + 1, col)) {
            ufForPercolation.union(indexChange(row + 1, col), indexChange(row, col));
            ufForFullness.union(indexChange(row + 1, col), indexChange(row, col));
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            ufForPercolation.union(indexChange(row, col - 1), indexChange(row, col));
            ufForFullness.union(indexChange(row, col - 1), indexChange(row, col));
        }
        if (col + 1 < N && isOpen(row, col + 1)) {
            ufForPercolation.union(indexChange(row, col + 1), indexChange(row, col));
            ufForFullness.union(indexChange(row, col + 1), indexChange(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return array[row][col].isOpen;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return ufForFullness.connected(virtualHead, indexChange(row, col)) && isOpen(row, col);
    }

    private int indexChange(int row, int col) {
        return row * N + col + 1;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSize;
    }


    // does the system percolate?
    public boolean percolates() {
        return ufForPercolation.connected(virtualHead, virtualTail);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
    }
}

