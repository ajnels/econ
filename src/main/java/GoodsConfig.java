import java.util.Collection;
import java.util.HashMap;

public class GoodsConfig {

    private static GoodsConfig instance = new GoodsConfig();

    private HashMap<String, Good> goodsMap;

    private HashMap<String, Double> goodsPriceMap;

    private GoodsConfig () {
        this.goodsMap = new HashMap<>();
        this.goodsPriceMap = new HashMap<>();
    }

    public static GoodsConfig getInstance() {
        return instance;
    }

    public void addGood (Good good, double value) {
        this.goodsMap.put(good.getName(), good);
        this.goodsPriceMap.put(good.getName(), value);
    }

    public Good getGood (String goodName) {
        return this.goodsMap.get(goodName);
    }

    public Collection<Good> getAllGoods() {
        return this.goodsMap.values();
    }

    public double getGoodPrice (String goodName) {
        return this.goodsPriceMap.get(goodName);
    }
}
