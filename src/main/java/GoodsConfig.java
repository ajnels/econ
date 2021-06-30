import java.util.HashMap;

public class GoodsConfig {

    private static GoodsConfig instance = new GoodsConfig();

    private HashMap<String, Good> goodsMap;

    private GoodsConfig () {
        this.goodsMap = new HashMap<>();
    }

    public static GoodsConfig getInstance() {
        return instance;
    }

    public void addGood (String name, Good good) {
        this.goodsMap.put(name, good);
    }

    public Good getGood (String goodName) {
        return this.goodsMap.get(goodName);
    }
}
