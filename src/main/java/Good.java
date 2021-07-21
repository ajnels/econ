public class Good {

    private String name;

    private GoodsProducer originProducer;

    public Good() {}

    public Good(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginProducer (GoodsProducer originProducer) {
        this.originProducer = originProducer;
    }

    public GoodsProducer getOriginProducer() {
        return originProducer;
    }

    public String toString() {
        return this.getName();
    }
}
