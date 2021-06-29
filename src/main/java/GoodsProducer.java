import java.util.ArrayList;
import java.util.List;

public class GoodsProducer {

    private Good good;

    private List<Pop> workers;

    private final double base_production = 10;

    private int workerLimit = 10;

    public GoodsProducer(Good good) {
        this.good = good;
        this.workers = new ArrayList<Pop>();
    }

    public Good getGoodType () {
        return this.good;
    }

    public void addWorker (Pop pop) {
        this.workers.add(pop);
        pop.setJob(this);
    }

    public double produceGoods() {
        return this.workers.size() * this.base_production;
    }

    public boolean hasOpenings() {
        return this.workers.size() < this.workerLimit;
    }
}
