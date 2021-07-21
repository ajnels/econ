
public class Pop {

    private Stockpile stockpile;

    private GoodsProducer job;

    private String race;

    private double money = 5;

    public Pop () {
        this.stockpile = new Stockpile();
        this.job = null;
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

    public double getMoney() {
        return money;
    }

    public String toString () {
        return "Race: " + this.getRace() + "; money: " + money + "\n";
    }


    public void subtractMoney(double money) {
        this.money -= money;
        if (this.money < 0) {
            this.money = 0;
        }
    }

    public void addMoney (double money) {
        this.money += money;
    }
}
