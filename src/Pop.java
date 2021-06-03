
import java.util.Set;

public class Pop {

    protected Stockpile stockpile;

    protected Stockpile needs;

    protected GoodsProducer job;

    public Pop () {
        this.stockpile = new Stockpile();

        this.needs = new Stockpile();
        this.needs.addStock(new Good("Food"), 5);
        this.needs.addStock(new Good("Beer"), 10);
        this.needs.addStock(new Good("Clothing"), 10);
    }

    public void buyNeeds (Stockpile stockpile) {
        Set<String> needTypes = this.needs.getGoodTypes();
        for (String needType : needTypes) {
            double needCount = this.stockpile.getStockCount(needType);
            double needLimit = this.needs.getStockCount(needType);
            if (needCount < needLimit) {
                this.stockpile.addStock(needType, stockpile.takeStock(needType, needLimit - needCount));
            }
        }
    }

    public void consumeNeeds() {
    }

    public GoodsProducer getJob () {
        return this.job;
    }

    public boolean hasJob() {
        return this.job != null;
    }

    public void setJob(GoodsProducer job) {
        this.job = job;
    }

    public String toString () {
        return "Stock: " + stockpile + "  Needs: " + needs;
    }

}
