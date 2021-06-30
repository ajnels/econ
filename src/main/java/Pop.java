
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;

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

    public GoodsProducer getJob () {
        return this.job;
    }

    public boolean hasJob() {
        return this.job != null;
    }

    public void setJob(GoodsProducer job) {
        this.job = job;
    }

    public Stockpile getStockpile() {
        return this.stockpile;
    }

    public String toString () {
        return "Stock: " + stockpile + "  Needs: " + needs;
    }

}
