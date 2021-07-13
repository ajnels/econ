import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main (String[] args) {
        Region region = new Region();

        init(region);

        cycle(region);
        System.out.println(region.getStockpile());
        cycle(region);
        System.out.println(region.getStockpile());
    }

    private static void init(Region region) {
        loadGoodTypes();
        loadPopNeeds();
        loadPopWants();
        loadPopInfo(region);
        loadProducerInfo(region);
    }

    private static void loadPopInfo(Region region) {
        List<Pop> pops = new ArrayList<>();
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
        region.setPops(pops);
    }

    private static void loadPopNeeds() {
        PopNeedsConfig popNeedsConfig = PopNeedsConfig.getInstance();
        try {
            YamlReader reader = new YamlReader(new FileReader("config/pop_needs_info.yaml"));
            while (true) {
                HashMap<String, ArrayList<NeedsInfo>> popNeedsInfo = (HashMap<String, ArrayList<NeedsInfo>>)reader.read();
                if (popNeedsInfo == null) {
                    break;
                }
                for (String race: popNeedsInfo.keySet()) {
                    for (NeedsInfo need: popNeedsInfo.get(race)) {
                        popNeedsConfig.addNeed(race, need);
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("Error reading in init pop needs data:");
            exception.printStackTrace();
        }
    }

    private static void loadPopWants() {
        PopNeedsConfig popNeedsConfig = PopNeedsConfig.getInstance();
        try {
            YamlReader reader = new YamlReader(new FileReader("config/pop_wants.yaml"));
            while (true) {
                HashMap<String, ArrayList<NeedsInfo>> popWants = (HashMap<String, ArrayList<NeedsInfo>>)reader.read();
                if (popWants == null) {
                    break;
                }
                for (String race: popWants.keySet()) {
                    for (NeedsInfo want: popWants.get(race)) {
                        popNeedsConfig.addWant(race, want);
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("Error reading in init pop wants data:");
            exception.printStackTrace();
        }
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
            System.out.println("Error reading in goods data: " + exception.getMessage());
        }
    }

    private static void loadProducerInfo(Region region) {
        List<GoodsProducer> goodProducers = new ArrayList<>();
        try {
            YamlReader reader = new YamlReader(new FileReader("config/producer_info.yaml"));
            while (true) {
                GoodsProducer producer = reader.read(GoodsProducer.class);
                if (producer == null) {
                    break;
                }

                goodProducers.add(producer);
            }
        } catch (Exception exception) {
            System.out.println("Error reading in producer data");
            exception.printStackTrace();
        }
        region.setGoodsProducers(goodProducers);
    }

    private static void cycle(Region region) {
        List<GoodsProducer> regionalProducers = region.getGoodsProducers();

        //pop actions
        for (Pop pop : region.getPops()) {
            if (!pop.hasJob()) {
                for (GoodsProducer producer : regionalProducers) {
                    if (producer.hasOpenings()) {
                        producer.addWorker(pop);
                    }
                }
            }
            buyNeeds(pop, region.getStockpile());
            consumeNeeds(pop);

            buyWants(pop, region.getStockpile());
            consumeWants(pop);

        }

        for (GoodsProducer goodsProducer : regionalProducers) {
            Set<String> producerNeeds = goodsProducer.getInputNeeds().keySet();
            for (String neededGood : producerNeeds) {
                double neededAmount        = goodsProducer.getNeededAmount(neededGood) * goodsProducer.getNumberOfWorkers();
                double amountTakenFromPile = region.getStockpile().takeStock(neededGood, neededAmount);
                goodsProducer.getStockpile().addStock(neededGood, amountTakenFromPile);
            }

            double goodsProducedAmount = goodsProducer.produceGoods();
            region.getStockpile().addStock(goodsProducer.getGood(), goodsProducedAmount);
        }

    }

    private static void buyNeeds (Pop pop, Stockpile stockpile) {
        PopNeedsConfig popNeedsConfig = PopNeedsConfig.getInstance();
        Set<String> needTypesForPop = popNeedsConfig.getNeedTypesForPop(pop);
        for (String needType : needTypesForPop) {
            double needCurrentStock = pop.stockpile.getStockCount(needType);
            double needCount        = popNeedsConfig.getConsumeAmountForNeed(pop, needType);
            if (needCurrentStock <= needCount) {
                double stockTakenFromPile = stockpile.takeStock(needType, needCount - needCurrentStock);
                pop.getStockpile().addStock(needType, stockTakenFromPile);
            }
        }
    }

    private static void buyWants (Pop pop, Stockpile stockpile) {
        PopNeedsConfig popWantsConfig = PopNeedsConfig.getInstance();
        Set<String> wantTypesForPop = popWantsConfig.getWantTypesForPop(pop);

        for (String wantType : wantTypesForPop) {
            double wantCount          = popWantsConfig.getConsumeAmountForWant(pop, wantType);
            double stockTakenFromPile = stockpile.takeStock(wantType, wantCount);

            pop.getStockpile().addStock(wantType, stockTakenFromPile);
        }
    }

    private static void consumeNeeds(Pop pop) {
        PopNeedsConfig popNeedsConfig = PopNeedsConfig.getInstance();
        Set<String> needTypesForPop = popNeedsConfig.getNeedTypesForPop(pop);
        for (String needType : needTypesForPop) {
            double needConsumeAmount = popNeedsConfig.getConsumeAmountForNeed(pop, needType);

            pop.stockpile.removeStock(needType, needConsumeAmount);
        }
    }

    private static void consumeWants(Pop pop) {
        PopNeedsConfig popWantsConfig = PopNeedsConfig.getInstance();
        Set<String> wantTypesForPop = popWantsConfig.getWantTypesForPop(pop);
        for (String wantType : wantTypesForPop) {
            double wantConsumeAmount = popWantsConfig.getConsumeAmountForWant(pop, wantType);

            pop.stockpile.removeStock(wantType, wantConsumeAmount);
        }
    }
}
