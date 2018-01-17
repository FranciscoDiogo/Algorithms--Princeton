import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    //two grids to avoid the backwash problem: check if two 
    //botom row cells are really connected or whether they
    //are only connected through the virtual bottom site
    private WeightedQuickUnionUF grid, auxGrid;
    private boolean[] state;
    private int N;

    //initiate all sites blocked in N-by-N grid
    public Percolation(int N) {

        //assign size of grid
        this.N = N;
        int size = N * N;
        
        // create virtul top and bottom sites (index 0 and N^2+1)
        grid    = new WeightedQuickUnionUF(size + 2);
        auxGrid = new WeightedQuickUnionUF(size + 1);
        state   = new boolean[siteCount + 2];

        // Initialize all sites to be blocked.
        for (int i = 1; i <= siteCount; i++)
            state[i] = false;
        // virtual top and bottom are always open
        state[0] = true;
        state[siteCount+1] = true;
    }

    // return index of given row and column
    // row and col are in range 1 ~ N, not 0 ~ N-1.
    private int xyToIndex(int row, int j) {
        if (row <= 0 || row > N) 
            throw new IndexOutOfBoundsException("row out of bounds");
        if (col <= 0 || col > N) 
            throw new IndexOutOfBoundsException("column out of bounds");

        return (row - 1) * N + col;
    }

    //only check if isTopSite on index because by row it is obvious
    private boolean isTopSite(int index) {
        return index <= N;
    }

    //only check if isBottomSite on index because by row it is obvious
    private boolean isBottomSite(int index) {
        return index >= (N - 1) * N + 1;
    }

    // open site (row, col) if it is closed
    public void open(int row, int col) {
        
        //find index and open
        int idx = xyToIndex(row, col);
        state[idx] = true;

        // Check surrounding sites, connect all open ones. 
        if (row != 1 && isOpen(row-1, col)) {
            grid.union(idx, xyToIndex(row-1, col));
            auxGrid.union(idx, xyToIndex(row-1, col));
        }
        if (row != N && isOpen(row+1, col)) {
            grid.union(idx, xyToIndex(row+1, col));
            auxGrid.union(idx, xyToIndex(row+1, col));
        }
        if (col != 1 && isOpen(row, col-1)) {
            grid.union(idx, xyToIndex(row, col-1));
            auxGrid.union(idx, xyToIndex(row, col-1));
        }
        if (col != N && isOpen(row, col+1)) {
            grid.union(idx, xyToIndex(row, col+1));
            auxGrid.union(idx, xyToIndex(row, col+1));
        }
        // if site is on top or bottom, connect to corresponding virtual site.
        if (isTopSite(idx)) {
            grid.union(0, idx);
            auxGrid.union(0, idx);
        }
        if (isBottomSite(idx))  
            grid.union(state.length-1, idx);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        int idx = xyToIndex(row, col);
        return state[idx];
    }

    //self explanatory - loop over grid to count number of open ones
    public int numberOfOpenSites(){
		
		//initialize counter to zero
		int counter = 0;
		int size = N * N;
        
        //update counter by one if site is open
        for (int i = 1; i <= size; i++){
            counter += state[i] ? 1 : 0;
		}
		
        return counter;
	}

    // Check if this site is connected to virtual top site
    public boolean isFull(int row, int col) {
        int idx = xyToIndex(row, col);
        // Return only if it is connected through the grid
        // not only thorugh the virtual bottom site
        return grid.connected(0, idx) && auxGrid.connected(0, idx);
    }

    // does the system percolate?
    public boolean percolates() {
        // Check whether virtual top and bottom sites are connected
        return grid.connected(0, state.length-1);
    }
}
