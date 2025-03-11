/******************************************************************************
 *  Compilation:  javac Knapsack.java
 *  Execution:    java Knapsack N W
 *
 *  Generates an instance of the 0/1 knapsack problem with N items
 *  and maximum weight W and solves it in time and space proportional
 *  to N * W using dynamic programming.
 *
 *  For testing, the inputs are generated at random with weights between 0
 *  and W, and profits between 0 and 1000.
 *
 *  %  java Knapsack 6 2000 
 *  item    profit  weight  take
 *  1       874     580     true
 *  2       620     1616    false
 *  3       345     1906    false
 *  4       369     1942    false
 *  5       360     50      true
 *  6       470     294     true
 *
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Knapsack 
{
    private List<Item> items;
    private int maxWeight;
    private int[][] opt;
    private boolean[][] sol;
    // opt[n][w] = max profit of packing items 1..n with weight limit w
    // sol[n][w] = does opt solution to pack items 1..n with weight limit w include item n?
    public Knapsack(List<Item> items, int maxWeight) {
        this.items = items;
        this.maxWeight = maxWeight;
        this.opt = new int[items.size() + 1][maxWeight + 1];
        this.sol = new boolean[items.size() + 1][maxWeight + 1];
    }
    
    public void solve() {
        int N = items.size();
        
        for (int n = 1; n <= N; n++) {
            for (int w = 1; w <= maxWeight; w++) {
                // don't take item n
                int option1 = opt[n - 1][w];
                
                // take item n
                int option2 = Integer.MIN_VALUE;
                if (items.get(n - 1).getWeight() <= w) {
                    option2 = items.get(n - 1).getProfit() + opt[n - 1][w - items.get(n - 1).getWeight()];
                }
                
                // select better of two options
                opt[n][w] = Math.max(option1, option2);
                sol[n][w] = (option2 > option1);
                
            }
        }
        
        // determine which items to take
        for (int i = N, j = maxWeight; i > 0; i--) {
            if (sol[i][j]) {
                items.get(i - 1).setTake(true);
                j = j - items.get(i - 1).getWeight();
            }
        }
    }
    
    //return items can take
    public List<Item> getTakenItem() {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            if (item.isTake()) {
                list.add(item);
            }
        }
        return list;
    }
    
    //return max profit
    public int getMaxProfit() {
        return opt[items.size()][maxWeight];
    }
    
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]); // number of items
        int W = Integer.parseInt(args[1]); // maximum weight of knapsack
        
        //Random rand = new Random();
        List<Item> items = new ArrayList<>();
        
        // generate random instance, items 1..N
        /*for (int i = 0; i < N; i++) {
            int numorder = i + 1; // set numorder 1 to N
            int profit = rand.nextInt(1000); // Random profit from 0 to 1000
            int weight = 1 + rand.nextInt(W); // Weight from 1 to W
            items.add(new Item(numorder, profit, weight));
        }*/
        
        items.add(new Item(1, 874, 580));
        items.add(new Item(2, 620, 1616));
        items.add(new Item(3, 345, 1906));
        items.add(new Item(4, 369, 1942));
        items.add(new Item(5, 360, 50));
        items.add(new Item(6, 470, 294));
        
        // Giải bài toán
        Knapsack solver = new Knapsack(items, W);
        solver.solve();
        
        // In kết quả
        System.out.println("Item\tProfit\tWeight\tTake");
        for (Item c : solver.getTakenItem()) {
            System.out.println(c);
        }
        System.out.println("Max Profit: " + solver.getMaxProfit());
    }
}
