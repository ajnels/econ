import java.util.HashMap;

public class ProducerFactory {

    private static ProducerFactory instance = new ProducerFactory();

    private HashMap<String, GoodsProducer> producerMap;

    private ProducerFactory () {
        this.producerMap = new HashMap<>();
    }

    public static ProducerFactory getInstance() {
        return instance;
    }

    public void addProducer(GoodsProducer goodsProducer) {
        this.producerMap.put(goodsProducer.getGood().getName(), goodsProducer);
    }

    public GoodsProducer getProducer(String producedGoodName) {
        if (!this.producerMap.containsKey(producedGoodName)) {
            return null;
        }
        return this.producerMap.get(producedGoodName);
    }


}
