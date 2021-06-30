public class NeedsInfo {
    public String name;

    public double consumeAmount;

    public double getConsumeAmount() {
        return consumeAmount;
    }

    public String toString () {
        return name + ": " + consumeAmount;
    }
}
