import java.util.ArrayList;
import java.util.List;

public class Region {
    private List<Pop> pops;

    private List<GoodsProducer> goodsProducers;

    private Stockpile stockpile;

    public Region() {
        this.pops = new ArrayList<>();
        this.goodsProducers = new ArrayList<>();
        this.stockpile = new Stockpile();
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
}
