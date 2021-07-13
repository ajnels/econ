import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GoodsProducer {

    private String name;

    public Good good;

    protected Stockpile stockpile;

    public HashMap<String, Double> inputNeeds;

    private ArrayList<Pop> workers;

    private final double baseProduction = 5;

    public boolean isRawExtraction;

    public GoodsProducer() {
        this.workers    = new ArrayList<>();
        this.inputNeeds = new HashMap<>();
        this.stockpile  = new Stockpile();
    }

    public Good getGood() {
        return this.good;
    }

    public void setGood (Good good) {
        this.good = good;
    }

    public void addWorker (Pop pop) {
        if (workers.size() >= this.getWorkersLimit()) {
            return;
        }
        this.workers.add(pop);
        pop.setJob(this);
    }

    private int getWorkersLimit() {
        return (isRawExtraction) ? 20 : 10;
    }

    public double produceGoods() {
        Set<String> producerNeeds = this.getInputNeeds().keySet();

        double lowestRatio = 1;
        for (String neededGood : producerNeeds) {
            double neededAmountMax     = this.getNeededAmount(neededGood) * this.workers.size();
            double amountInStockpile   = stockpile.getStockCount(neededGood);

            double ratioOfGoods = amountInStockpile / neededAmountMax;
            if (ratioOfGoods > 1) {
                ratioOfGoods = 1.0;
            }
            if (ratioOfGoods < lowestRatio) {
                lowestRatio = ratioOfGoods;
            }
        }

        return this.workers.size() * this.baseProduction * lowestRatio;
    }

    public boolean hasOpenings() {
        return this.workers.size() < this.getWorkersLimit();
    }

    public int getNumberOfWorkers() {
        return this.workers.size();
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
        return this.name + " produces " + this.getGood().getName();
    }
}
