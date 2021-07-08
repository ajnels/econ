import java.util.HashMap;
import java.util.Set;

public class PopNeedsConfig {

    private static PopNeedsConfig instance = new PopNeedsConfig();

    private HashMap<String, HashMap<String, NeedsInfo>> needsMap;

    private PopNeedsConfig () {
        this.needsMap = new HashMap<>();
    }

    public static PopNeedsConfig getInstance() {
        return instance;
    }

    public void addNeed(String race, NeedsInfo needsInfo) {
        if (this.needsMap.containsKey(race)) {
            this.needsMap.get(race).put(needsInfo.getName(), needsInfo);
        } else {
            HashMap<String, NeedsInfo> need = new HashMap<>();
            need.put(needsInfo.getName(), needsInfo);
            this.needsMap.put(race, need);
        }
    }

    public Set<String> getNeedTypesForPop(Pop pop) {
        return this.needsMap.get(pop.getRace()).keySet();
    }

    public double getConsumeAmount(Pop pop, String needType) {
        return this.needsMap.get(pop.getRace()).get(needType).getConsumeAmount();
    }
}
