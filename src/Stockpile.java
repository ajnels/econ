import java.util.HashMap;

public class Stockpile {

    HashMap<String, Double> stockpile = new HashMap<String, Double>();

    public void addStock (Good good, double amount) {
        String goodName = good.getName();
        if (stockpile.containsKey(goodName)) {
            double currentStock = stockpile.get(goodName);
            stockpile.put(goodName, currentStock + amount);
        } else {
            stockpile.put(goodName, amount);
        }
    }

    public double takeStock (Good good, double amount) {
        String goodName = good.getName();

        return _takeStock(goodName, amount);
    }

    public double takeStock (String goodName, double amount) {
        return _takeStock(goodName, amount);
    }

    private double _takeStock(String goodName, double amount) {
        double takenAmount = 0.0;
        if (stockpile.containsKey(goodName)) {
            double currentStock = stockpile.get(goodName);

            double newAmount = 0;
            if (currentStock < amount) {
                takenAmount = currentStock;
            } else {
                takenAmount = amount;
                newAmount = currentStock - amount;
            }
            stockpile.put(goodName, newAmount);
        } else {
            takenAmount = 0;
        }
        return  takenAmount;
    }

    public String toString() {
        return this.stockpile.toString();
    }


}
