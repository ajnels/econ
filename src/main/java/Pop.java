
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Pop {

    protected Stockpile stockpile;

    protected PopNeeds needs;

    protected GoodsProducer job;

    public Pop () {
        this.stockpile = new Stockpile();
        this.needs = new PopNeeds();

        init();
    }

    private void init () {
        try {
            YamlReader reader = new YamlReader(new FileReader("config/pop_info.yaml"));
            while (true) {
                NeedsInfo needsInfo = reader.read(NeedsInfo.class);
                if (needsInfo == null) {
                    break;
                }
                this.needs.addNeedsInfo(needsInfo);
            }
        } catch (Exception exception) {
            System.out.println("Error reading in init pop data: " + exception.getMessage());
        }
    }

    public void buyNeeds (Stockpile stockpile) {
        Set<String> needTypes = this.needs.getNeedTypes();
        for (String needType : needTypes) {
            double needCount = this.stockpile.getStockCount(needType);
            double needLimit = this.needs.getConsumeAmount(needType) * 2;
            if (needCount < needLimit) {
                this.stockpile.addStock(needType, stockpile.takeStock(needType, needLimit - needCount));
            }
        }
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
