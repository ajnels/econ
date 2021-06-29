import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static ArrayList<String> goods;

    public static void main (String[] args) {
        init();

        Good food = new Good(goods.get(0));

        Pop pop = new Pop();
        List<Pop> pops = new ArrayList<Pop>();
        pops.add(pop);

        Stockpile stockpile = new Stockpile();

        GoodsProducer farm = new GoodsProducer(food);
        List<GoodsProducer> goodsProducers = new ArrayList<>();
        goodsProducers.add(farm);

        cycle(pops, stockpile, goodsProducers);
        cycle(pops, stockpile, goodsProducers);

        System.out.println("Pop info: " + pops.get(0));
    }

    private static void init() {
        try {
            YamlReader reader = new YamlReader(new FileReader("config/good_types.yaml"));
            Object object = reader.read();
            ArrayList<String> yaml_config = (ArrayList<String>)object;

            goods = yaml_config;

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void cycle(List<Pop> Pops, Stockpile stockpile, List<GoodsProducer> goodsProducers) {
        //pop actions
        for (Pop pop : Pops) {
            if (!pop.hasJob()) {
                for (GoodsProducer producer : goodsProducers) {
                    if (producer.hasOpenings()) {
                        producer.addWorker(pop);
                    }
                }
            }
            pop.buyNeeds(stockpile);
            consumeNeeds(pop);
        }

        //produce goods
        for (GoodsProducer goodsProducer : goodsProducers) {
            double goodsProducedAmount = goodsProducer.produceGoods();
            stockpile.addStock(goodsProducer.getGoodType(), goodsProducedAmount);
        }

    }

    private static void consumeNeeds(Pop pop) {

    }
}
