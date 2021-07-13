import java.util.ArrayList;
import java.util.List;

public class Region {
    private List<Pop> pops;

    private List<GoodsProducer> goodsProducers;

    private Stockpile stockpile;

    private Good rawResource;

    private String name;

    public Region() {
        this.pops = new ArrayList<>();
        this.goodsProducers = new ArrayList<>();
        this.stockpile = new Stockpile();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Pop> getPops() {
        return pops;
    }

    public void addPops(List<Pop> pops) {
        this.pops.addAll(pops);
    }

    public Stockpile getStockpile() {
        return stockpile;
    }

    public List<GoodsProducer> getGoodsProducers() {
        return goodsProducers;
    }

    public void setGoodsProducers(List<GoodsProducer> goodsProducers) {
        this.goodsProducers = goodsProducers;
    }

    public Good getRawResource() {
        return rawResource;
    }

    public void setRawResource(Good rawResource) {
        this.rawResource = rawResource;
    }

    public void addGoodsProducer(GoodsProducer factory) {
        this.goodsProducers.add(factory);
    }

    public String showWorkStats() {
        StringBuilder workStats = new StringBuilder();

        // make a double to force double division
        double numEmployed = 0;
        for (Pop pop : this.pops) {
            if (pop.hasJob()) {
                numEmployed++;
            }
        }

        double percentageEmployed = (numEmployed / this.pops.size()) * 100;
        workStats.append("Employed: ").append(String.format("%,.2f", percentageEmployed)).append(" %\n");

        for (GoodsProducer producer : this.goodsProducers) {
            workStats.append(producer.getName()).append(": ").append(producer.getNumberOfWorkers()).append(" / ").append(producer.getWorkersLimit()).append("  ( ").append((producer.getNumberOfWorkers() / producer.getWorkersLimit()) * 100).append(" % )\n");
        }
        workStats.append("-------------------------------------------------------------------\n");
        return workStats.toString();
    }
}
