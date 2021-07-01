import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GoodsProducer {

    private String name;

    public Good good;

    protected Stockpile stockpile;

    public HashMap<String, Double> inputNeeds;

    private ArrayList<Pop> workers;

    private final double baseProduction = 10;

    public int workerLimit;

    public GoodsProducer() {
        this.workers    = new ArrayList<>();
        this.inputNeeds = new HashMap<>();
        this.stockpile  = new Stockpile();
    }

    public Good getGoodType () {
        return this.good;
    }

    public void setGood (Good good) {
        this.good = good;
    }

    public void addWorker (Pop pop) {
        if (workers.size() >= this.workerLimit) {
            return;
        }
        this.workers.add(pop);
        pop.setJob(this);
    }

    public double produceGoods() {
        boolean hasEnoughStock = true;
        Set<String> producerNeeds = this.getInputNeeds().keySet();

        for (String neededGood : producerNeeds) {
            double neededAmount        = this.getNeededAmount(neededGood);
            double amountInStockpile   = stockpile.getStockCount(neededGood);

            if (neededAmount > amountInStockpile) {
                hasEnoughStock = false;
            }
        }

        if (!hasEnoughStock) {
            return 0;
        }
        return this.workers.size() * this.baseProduction;
    }

    public boolean hasOpenings() {
        return this.workers.size() < this.workerLimit;
    }

    public HashMap<String, Double> getInputNeeds () {
        return this.inputNeeds;
    }

    public void addInputNeed (Good good, double neededAmount) {
        this.inputNeeds.put(good.getName(), neededAmount);
    }

    public void addInputNeed (String goodName, double neededAmount) {
        this.inputNeeds.put(goodName, neededAmount);
    }

    public double getNeededAmount(Good good) {
        return this.inputNeeds.get(good.getName());
    }

    public double getNeededAmount(String goodName) {
        return this.inputNeeds.get(goodName);
    }

    public Stockpile getStockpile() {
        return this.stockpile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name + " produces " + this.getGoodType().getName();
    }
}
