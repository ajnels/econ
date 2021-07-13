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

    public void setPops(List<Pop> pops) {
        this.pops = pops;
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
}
