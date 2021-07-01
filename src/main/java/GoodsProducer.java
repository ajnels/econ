import java.util.ArrayList;
import java.util.List;

public class GoodsProducer {

    private String name;

    public Good good;

    private List<Pop> workers;

    private final double baseProduction = 10;

    public int workerLimit;

    public GoodsProducer() {
        this.workers = new ArrayList<Pop>();
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
        return this.workers.size() * this.baseProduction;
    }

    public boolean hasOpenings() {
        return this.workers.size() < this.workerLimit;
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
