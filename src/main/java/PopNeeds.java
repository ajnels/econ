import java.util.HashMap;
import java.util.Set;

public class PopNeeds {

    private HashMap<String, NeedsInfo> popNeedsConsumption;

    public PopNeeds () {
        this.popNeedsConsumption = new HashMap<>();
    }

    public void addNeedsInfo (NeedsInfo needsInfo) {
        this.popNeedsConsumption.put(needsInfo.name, needsInfo);
    }

    public Set<String> getNeedTypes() {
        return this.popNeedsConsumption.keySet();
    }

    public double getConsumeAmount (String needType) {
        return this.popNeedsConsumption.get(needType).getConsumeAmount();
    }

    public String toString() {
        return this.popNeedsConsumption.values().toString();
    }
}
