import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {


    public static void main (String[] args) {
        init();

        Good food = GoodsConfig.getInstance().getGood("Food");

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
        GoodsConfig goodsConfig = GoodsConfig.getInstance();
        try {
            YamlReader reader = new YamlReader(new FileReader("config/goods.yaml"));
            while (true) {
                Good good = reader.read(Good.class);
                if (good == null) {
                    break;
                }
                goodsConfig.addGood(good.name, good);
            }
        } catch (Exception exception) {
            System.out.println("Error reading in init data: " + exception.getMessage());
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
