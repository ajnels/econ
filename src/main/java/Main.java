import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;
import java.util.*;

public class Main {

    private static List<Pop> pops;

    public static void main (String[] args) {
        pops = new ArrayList<>();
        init();

        Good food = GoodsConfig.getInstance().getGood("Food");

        Stockpile stockpile = new Stockpile();

        GoodsProducer farm = new GoodsProducer(food);
        List<GoodsProducer> goodsProducers = new ArrayList<>();
        goodsProducers.add(farm);

        cycle(stockpile, goodsProducers);
        cycle(stockpile, goodsProducers);

        System.out.println("Pop info: " + pops);
    }

    private static void init() {
        loadGoodTypes();
        loadPopInfo();
    }

    private static void loadGoodTypes() {
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

    private static void loadPopInfo() {
        try {
            YamlReader reader = new YamlReader(new FileReader("config/pop_info.yaml"));
            while (true) {
                HashMap<String, String> popInfo = (HashMap<String, String>)reader.read();
                if (popInfo == null) {
                    break;
                }
                String popRace = popInfo.get("race");
                int popCount   = Integer.parseInt(popInfo.get("size"));

                for (int i = 0; i < popCount; i++) {
                    Pop pop = new Pop();
                    pop.setRace(popRace);
                    pops.add(pop);
                }
            }
        } catch (Exception exception) {
            System.out.println("Error reading in population data: " + exception.getMessage());
        }
    }

    private static void cycle(Stockpile stockpile, List<GoodsProducer> goodsProducers) {
        //pop actions
        for (Pop pop : pops) {
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
        }
    }
}
