import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main (String[] args) {
        Good food = new Good("Food");

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
            pop.consumeNeeds();
        }

        //produce goods
        for (GoodsProducer goodsProducer : goodsProducers) {
            double goodsProducedAmount = goodsProducer.produceGoods();
            stockpile.addStock(goodsProducer.getGoodType(), goodsProducedAmount);
        }

    }
}
