
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
   private final int noOfTrails;
   private final double[] numberOfOpenSites;
   static private final double CONFIDENCE_95 = 1.96;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n <= 0 || trials <= 0){
            throw new IllegalArgumentException();
        }
        noOfTrails = trials;

        numberOfOpenSites = new double[trials];

        for(int i = 0; i < trials; i++){
            Percolation percolation = new Percolation(n);
            while(!percolation.percolates()){
               int row = StdRandom.uniform(1, n + 1);
               int col = StdRandom.uniform(1, n + 1 );
               while(percolation.isOpen(row, col)){
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1 );
               }
                percolation.open(row, col);
            }
            numberOfOpenSites[i] = percolation.numberOfOpenSites() / (double) (n * n) ;
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(numberOfOpenSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(numberOfOpenSites);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(noOfTrails);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(noOfTrails);
    }

   // test client (see below)
   public static void main(String[] args){
        int n = 10;          
        int trials = 10;
        if (args.length == 2){
          n = Integer.parseInt(args[0]);  
          trials = Integer.parseInt(args[1]);  
        } 

       PercolationStats percolationStats =  new PercolationStats(n, trials);

       System.out.println("mean                    = " + percolationStats.mean());
       System.out.println("stddev                  = " + percolationStats.stddev());
       System.out.println("95% confidence interval = " + "[" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
   }
}
