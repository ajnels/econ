import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Stockpile {

    protected HashMap<String, ArrayList<Good>> stockpile = new HashMap<>();

    public void addStock (Good good, ArrayList<Good> goods) {
        String goodName = good.getName();
        this._addStock(goodName, goods);
    }

    public void addStock (String goodName, ArrayList<Good> goods) {
        this._addStock(goodName, goods);
    }

    private void _addStock(String goodName, ArrayList<Good> goods) {
        if (stockpile.containsKey(goodName)) {
            ArrayList<Good> currentStock = stockpile.get(goodName);
            currentStock.addAll(goods);
            stockpile.put(goodName, currentStock);
        } else {
            stockpile.put(goodName, goods);
        }
    }

    public ArrayList<Good> takeStock (Good good, int amount) {
        String goodName = good.getName();

        return _takeStock(goodName, amount);
    }

    public ArrayList<Good> takeStock (String goodName, int amount) {
        return _takeStock(goodName, amount);
    }

    public void removeStock (String goodName, int amount) {
        _takeStock(goodName, amount);
    }

    private ArrayList<Good> _takeStock(String goodName, int amountRequested) {
        ArrayList<Good> goodsTaken = new ArrayList<Good>();
        if (!stockpile.containsKey(goodName)) {
            return goodsTaken;
        }

        ArrayList<Good> goods = stockpile.get(goodName);

        if (goods.size() < amountRequested) {
            goodsTaken = goods;
            stockpile.put(goodName, new ArrayList<Good>());
        } else {
            goodsTaken = new ArrayList<Good>(goods.subList(0, amountRequested));
            goods.removeAll(goodsTaken);
            stockpile.put(goodName, goods);
        }

        return goodsTaken;
    }

    public int getStockCount (String goodType) {
        if (this.stockpile.containsKey(goodType)) {
            return this.stockpile.get(goodType).size();
        } else {
            return 0;
        }
    }

    public Set<String> getGoodTypes () {
        return this.stockpile.keySet();
    }

    public String toString() {
        StringBuilder summary = new StringBuilder();
        for (String good : this.getGoodTypes()) {
            summary.append(good).append(": ");
            summary.append(this.stockpile.get(good).size()).append("    ");
        }
        return summary.append("\n").toString();
    }


}
