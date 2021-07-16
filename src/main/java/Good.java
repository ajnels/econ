public class Good {

    private String name;

    private double value;

    public Good() {}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String toString() {
        return this.getName();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
