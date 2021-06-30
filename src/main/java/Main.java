import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;
import java.util.*;

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
            buyNeeds(pop, stockpile);
            consumeNeeds(pop);
        }

        //produce goods
        for (GoodsProducer goodsProducer : goodsProducers) {
            double goodsProducedAmount = goodsProducer.produceGoods();
            stockpile.addStock(goodsProducer.getGoodType(), goodsProducedAmount);
        }

    }

    private static void buyNeeds (Pop pop, Stockpile stockpile) {
        Set<String> needTypes = pop.needs.getNeedTypes();
        for (String needType : needTypes) {
            double needCount = pop.stockpile.getStockCount(needType);
            double needLimit = pop.needs.getConsumeAmount(needType) * 2;
            if (needCount < needLimit) {
                pop.stockpile.addStock(needType, stockpile.takeStock(needType, needLimit - needCount));
            }
        }
    }

    private static void consumeNeeds(Pop pop) {
        Set<String> needTypes = pop.needs.getNeedTypes();
        for (String needType : needTypes) {
            double needConsumeAmount = pop.needs.getConsumeAmount(needType);

            pop.stockpile.removeStock(needType, needConsumeAmount);
            System.out.println("Pop is consuming: " + needType + " of amt: " + needConsumeAmount);
        }
    }
}
