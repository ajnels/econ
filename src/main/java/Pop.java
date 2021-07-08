
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileReader;

public class Pop {

    protected Stockpile stockpile;

    protected GoodsProducer job;

    protected String race;

    public Pop () {
        this.stockpile = new Stockpile();
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

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String toString () {
        return "Race: " + this.getRace() + "; Stock: " + stockpile + "\n";
    }

}
