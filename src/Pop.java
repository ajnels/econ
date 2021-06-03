public class Pop {

    protected double foodAmount = 0;
    protected double foodMax = 5;

    protected GoodsProducer job;

    public double getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount (double foodAmount) {
        this.foodAmount = foodAmount;
    }

    public double getFoodNeeded() {
        return this. foodMax - this.foodAmount;
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

}
