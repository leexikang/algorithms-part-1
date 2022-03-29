import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
       
        private final WeightedQuickUnionUF weightedQuickUnionUF;
        private final WeightedQuickUnionUF topConnectedUF;
        private Boolean[] sites;
        private int opendSites = 0;
        private final int n;

        public Percolation(int i) {
            if (i <= 0 ) {
                throw new IllegalArgumentException();
            }
            n = i;
            sites = new Boolean[i*i];
            for (int j = 0 ; j < n * n ; j++) {
                sites[j] = false;
            }
            weightedQuickUnionUF = new WeightedQuickUnionUF((i * i) + 2);
            topConnectedUF = new WeightedQuickUnionUF((i * i) + 1);
        }


        public void open(int row, int col) {
           int current = getIndex(row, col);
           if (isOpen(row, col)) {
               return;
           }

            sites[current] = true;
            unionWith(row -1 , col, current);
            unionWith(row + 1 , col, current);
            unionWith(row  , col + 1, current);
            unionWith(row  , col - 1, current);
            opendSites++;
        }

        private void unionWith(int row, int col, int unionWith) {
            if(reachedHorizontalLimit(col))
            {
                return ;
            }

            if(row < 1)
            {
                weightedQuickUnionUF.union(unionWith, arbitaryTop());
                topConnectedUF.union(unionWith, arbitaryTop());
                return;
            }

            if(row > n)
            {
                weightedQuickUnionUF.union(unionWith, arbitaryBottom());
                return;
            }

            int index = getIndex(row, col);
            if(sites[index])
            {
                weightedQuickUnionUF.union(unionWith, index); 
                topConnectedUF.union(unionWith, index);
                sites[index] = true;
            }
        }

        private Boolean reachedHorizontalLimit(int col) {
           return col < 1 || col > n  ;
        }

        private int arbitaryTop() {
            return n * n;
        }

        private int arbitaryBottom() {
            return n * n  + 1;
        }

        // is the site (row, col) open?
        public boolean isOpen(int row, int col) {
            return sites[getIndex(row, col)];
        }
        
    
        // is the site (row, col) full?
        public boolean isFull(int row, int col) {
            return topConnectedUF.find(getIndex(row, col)) 
            ==  topConnectedUF.find(arbitaryTop());
        }
    
        // returns the number of open sites
        public int numberOfOpenSites() {
            return opendSites;
        }
    
        // does the system percolate?
        public boolean percolates() {
            return weightedQuickUnionUF.find(arbitaryTop())
            == weightedQuickUnionUF.find(arbitaryBottom());
        }

        private int getIndex(int row, int col) {
            if (row < 1 || row > n  || col < 1  || col > n) {
                throw  new IllegalArgumentException();
            }
            return (row - 1) * n  +  col - 1;
        }
        // test client (optional)
        public static void main(String[] args) {
          Percolation percolation = new Percolation(3);
          percolation.open(1, 1);
          percolation.open(2, 2);
          percolation.open(2, 3);
          if (percolation.numberOfOpenSites() != 3) {
              System.out.println("opened sites donot match should be 3 but is " + percolation.opendSites);
          }
    }
}
