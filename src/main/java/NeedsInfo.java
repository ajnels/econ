public class NeedsInfo {
    public String name;

    public double consumeAmount;

    public double getConsumeAmount() {
        return consumeAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString () {
        return name + ": " + consumeAmount;
    }
}
