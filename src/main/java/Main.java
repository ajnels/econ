import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main (String[] args) {
        List<Region> regions = init();

        cycle(regions);

        cycle(regions);

        for (Region region : regions) {
            System.out.println(region.getName() + ": Population: " + region.getPops().size() + " work stats: ");
            System.out.println(region.showWorkStats());
            System.out.println(region.getStockpile() + "\n\n");
            //System.out.println(region.getPops());
        }
    }

    private static List<Region> init() {
        loadGoodTypes();
        loadProducerInfo();
        loadPopNeeds();
        loadPopWants();
        List<Region> regions = loadPopInfo();

        return regions;
    }

    private static List<Region> loadPopInfo() {
        List<Region> regions = new ArrayList<>();
        try {
            YamlReader reader = new YamlReader(new FileReader("config/regions/region.yaml"));
            while (true) {
                Map regionInfo = (Map) reader.read();
                if (regionInfo == null) {
                    break;
                }

                String regionName      = (String) regionInfo.get("name");
                String rawResourceName = (String) regionInfo.get("resource");

                Region region = new Region();
                region.setName(regionName);
                region.setRawResource(GoodsConfig.getInstance().getGood(rawResourceName));
                region.addGoodsProducer(ProducerFactory.getInstance().getProducer(rawResourceName));

                List<HashMap<String, String>> popInfo = (List<HashMap<String, String>>)regionInfo.get("pops");

                for (HashMap<String, String> popSummary : popInfo) {
                    String popRace = popSummary.get("race");
                    int popCount   = Integer.parseInt(popSummary.get("size"));

                    List<Pop> pops = new ArrayList<>();

                    for (int i = 0; i < popCount; i++) {
                        Pop pop = new Pop();
                        pop.setRace(popRace);
                        pops.add(pop);
                    }
                    region.addPops(pops);
                }

                List<String> factoryInfo = (List<String>)regionInfo.get("factories");
                for (String factoryType : factoryInfo) {
                    GoodsProducer factory = ProducerFactory.getInstance().getProducer(factoryType);
                    if (factory != null) {
                        region.addGoodsProducer(factory);
                    }
                }

                regions.add(region);
            }
        } catch (Exception exception) {
            System.out.println("Error reading in region data: " + exception.getMessage());
        }
        return regions;
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
                HashMap<String, String> goodInfo = (HashMap<String, String>)reader.read();
                if (goodInfo == null) {
                    break;
                }
                Good good = new Good(goodInfo.get("name"));
                double defaultValue = Double.parseDouble(goodInfo.get("value"));
                goodsConfig.addGood(good, defaultValue);
            }
        } catch (Exception exception) {
            System.out.println("Error reading in goods data: " + exception.getMessage());
        }
    }

    private static void loadProducerInfo() {
        ProducerFactory producerFactory = ProducerFactory.getInstance();
        try {
            YamlReader reader = new YamlReader(new FileReader("config/producer_info.yaml"));
            while (true) {
                GoodsProducer producer = reader.read(GoodsProducer.class);
                if (producer == null) {
                    break;
                }

                producerFactory.addProducer(producer);
            }
        } catch (Exception exception) {
            System.out.println("Error reading in producer data");
            exception.printStackTrace();
        }
    }

    private static void cycle(List<Region> regions) {
        for (Region region : regions) {
            List<GoodsProducer> regionalProducers = region.getGoodsProducers();

            //pop actions
            for (Pop pop : region.getPops()) {
                if (!pop.hasJob()) {
                    for (GoodsProducer producer : regionalProducers) {
                        if (producer.hasOpenings()) {
                            producer.addWorker(pop);
                            pop.setJob(producer);
                            // stop job search
                            break;
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
                    int needCurrentStock        = goodsProducer.getStockpile().getStockCount(neededGood);
                    int neededAmount            = goodsProducer.getNeededAmount(neededGood) * goodsProducer.getNumberOfWorkers();
                    int desiredAmountToPurchase = neededAmount - needCurrentStock;

                    double goodPrice            = GoodsConfig.getInstance().getGoodPrice(neededGood);
                    int maxPurchasableWithMoney = (int) (goodsProducer.getMoney() / goodPrice);
                    int amountToPurchase = (desiredAmountToPurchase < maxPurchasableWithMoney) ? desiredAmountToPurchase : maxPurchasableWithMoney;

                    ArrayList<Good> goodsTakenFromPile = region.getStockpile().takeStock(neededGood, amountToPurchase);

                    goodsProducer.subtractMoney(goodsTakenFromPile.size() * goodPrice);
                    goodsProducer.getStockpile().addStock(neededGood, goodsTakenFromPile);
                }

                ArrayList<Good> goodsProduced = goodsProducer.produceGoods();
                region.getStockpile().addStock(goodsProducer.getGood(), goodsProduced);

                for (Pop worker: goodsProducer.getWorkers()) {
                    int wage = 3;
                    goodsProducer.subtractMoney(wage);
                    worker.addMoney(wage);
                }
            }
        }

    }

    private static void buyNeeds (Pop pop, Stockpile stockpile) {
        PopNeedsConfig popNeedsConfig = PopNeedsConfig.getInstance();
        Set<String> needTypesForPop = popNeedsConfig.getNeedTypesForPop(pop);

        for (String needType : needTypesForPop) {
            int needCurrentStock        = pop.getStockpile().getStockCount(needType);
            int needCount               = popNeedsConfig.getConsumeAmountForNeed(pop, needType);
            int desiredAmountToPurchase = needCount - needCurrentStock;

            double goodPrice            = GoodsConfig.getInstance().getGoodPrice(needType);
            int maxPurchasableWithMoney = (int) (pop.getMoney() / goodPrice);

            int amountToPurchase = (desiredAmountToPurchase < maxPurchasableWithMoney) ? desiredAmountToPurchase : maxPurchasableWithMoney;

            if (amountToPurchase > 0) {
                ArrayList<Good> stockTakenFromPile = stockpile.takeStock(needType, amountToPurchase);
                pop.getStockpile().addStock(needType, stockTakenFromPile);
                pop.subtractMoney(amountToPurchase * goodPrice);
            }
        }
    }

    private static void buyWants (Pop pop, Stockpile stockpile) {
        PopNeedsConfig popWantsConfig = PopNeedsConfig.getInstance();
        Set<String> wantTypesForPop = popWantsConfig.getWantTypesForPop(pop);

        for (String wantType : wantTypesForPop) {
            int needCurrentStock = pop.getStockpile().getStockCount(wantType);
            int needCount = popWantsConfig.getConsumeAmountForWant(pop, wantType);
            int desiredAmountToPurchase = needCount - needCurrentStock;

            double goodPrice = GoodsConfig.getInstance().getGoodPrice(wantType);
            int maxPurchasableWithMoney = (int) (pop.getMoney() / goodPrice);

            int amountToPurchase = (desiredAmountToPurchase < maxPurchasableWithMoney) ? desiredAmountToPurchase : maxPurchasableWithMoney;

            if (amountToPurchase > 0) {
                ArrayList<Good> stockTakenFromPile = stockpile.takeStock(wantType, amountToPurchase);
                pop.getStockpile().addStock(wantType, stockTakenFromPile);
                pop.subtractMoney(amountToPurchase * goodPrice);
            }
        }
    }

    private static void consumeNeeds(Pop pop) {
        PopNeedsConfig popNeedsConfig = PopNeedsConfig.getInstance();
        Set<String> needTypesForPop = popNeedsConfig.getNeedTypesForPop(pop);
        for (String needType : needTypesForPop) {
            int needConsumeAmount = popNeedsConfig.getConsumeAmountForNeed(pop, needType);

            pop.getStockpile().removeStock(needType, needConsumeAmount);
        }
    }

    private static void consumeWants(Pop pop) {
        PopNeedsConfig popWantsConfig = PopNeedsConfig.getInstance();
        Set<String> wantTypesForPop = popWantsConfig.getWantTypesForPop(pop);
        for (String wantType : wantTypesForPop) {
            int wantConsumeAmount = popWantsConfig.getConsumeAmountForWant(pop, wantType);

            pop.getStockpile().removeStock(wantType, wantConsumeAmount);
        }
    }
}
