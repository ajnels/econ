import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GoodsProducer {

    private String name;

    public Good good;

    protected Stockpile stockpile;

    public HashMap<String, Integer> inputNeeds;

    private ArrayList<Pop> workers;

    private final double baseProduction = 5;

    private double money = 10;

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
        if (this.workers.size() > this.getWorkersLimit()) {
            return;
        }
        this.workers.add(pop);
    }

    public int getWorkersLimit() {
        return (this.isRawExtraction) ? 20 : 10;
    }

    public boolean isRawExtraction() {
        return isRawExtraction;
    }

    public ArrayList<Pop> getWorkers() {
        return this.workers;
    }


    public ArrayList<Good> produceGoods() {
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


        int amountProduced = (int) (this.workers.size() * this.baseProduction * lowestRatio);
        ArrayList<Good> producedGoods = new ArrayList<>();
        for (int i = 0; i < amountProduced; i++) {
            Good good = new Good(this.getGood().getName());
            good.setOriginProducer(this);
            producedGoods.add(good);
        }
        return producedGoods;
    }

    public boolean hasOpenings() {
        return this.workers.size() < this.getWorkersLimit();
    }

    public int getNumberOfWorkers() {
        return this.workers.size();
    }

    public HashMap<String, Integer> getInputNeeds () {
        return this.inputNeeds;
    }

    public void addInputNeed (Good good, int neededAmount) {
        this.inputNeeds.put(good.getName(), neededAmount);
    }

    public void addInputNeed (String goodName, int neededAmount) {
        this.inputNeeds.put(goodName, neededAmount);
    }

    public int getNeededAmount(Good good) {
        return this.inputNeeds.get(good.getName());
    }

    public int getNeededAmount(String goodName) {
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

    public double getMoney() {
        return money;
    }

    public void subtractMoney(double money) {
        this.money -= money;
        if (this.money < 0) {
            this.money = 0;
        }
    }

    public String toString() {
        return this.name + " produces " + this.getGood().getName();
    }
}
