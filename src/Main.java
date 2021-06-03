import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main (String[] args) {
        Good food = new Good("Food");


        Pop pop = new Pop();
        List<Pop> pops = new ArrayList<Pop>();
        pops.add(pop);

        Stockpile stockpile = new Stockpile();
        //stockpile.addStock(food, 10);

        GoodsProducer farm = new GoodsProducer(food);
        List<GoodsProducer> goodsProducers = new ArrayList<>();
        goodsProducers.add(farm);

        System.out.println("Stockpile starts as: " + stockpile);
        cycle(pops, stockpile, goodsProducers);
        System.out.println("Stockpile is now: " + stockpile);
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

            // TODO expand to work on all goods
            if (pop.getFoodNeeded() > 0) {
                pop.setFoodAmount(stockpile.takeStock("Food", pop.getFoodNeeded()));
            }
        }

        //produce goods
        for (GoodsProducer goodsProducer : goodsProducers) {
            double goodsProducedAmount = goodsProducer.produceGoods();
            stockpile.addStock(goodsProducer.getGoodType(), goodsProducedAmount);
        }

    }
}
