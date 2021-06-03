import java.util.HashMap;
import java.util.Set;

public class Stockpile {

    protected HashMap<String, Double> stockpile = new HashMap<>();

    public void addStock (Good good, double amount) {
        String goodName = good.getName();
        this._addStock(goodName, amount);
    }

    public void addStock (String goodName, double amount) {
        this._addStock(goodName, amount);
    }

    private void _addStock(String goodName, double amount) {
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

    public double getStockCount (String goodType) {
        if (this.stockpile.containsKey(goodType)) {
            return this.stockpile.get(goodType);
        } else {
            return 0;
        }
    }

    public Set<String> getGoodTypes () {
        return this.stockpile.keySet();
    }

    public String toString() {
        return this.stockpile.toString();
    }


}
