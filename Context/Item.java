
public class Item
{
    private int numorder; //numerical order
    private int profit;
    private int weight;
    private boolean take;

    public Item(int numorder, int profit, int weight) {
        this.numorder = numorder;
        this.profit = profit;
        this.weight = weight;
    }
    
    public int getNumorder() {
        return numorder;
    }
    
    public int getProfit() {
        return profit;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isTake() {
        return take;
    }
    
     public void setTake(boolean take) {
        this.take = take;
    }
    
    @Override
    public String toString() {
        return numorder + "\t" + profit + "\t" + weight + "\t" + take;
    }
}
