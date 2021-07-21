import java.util.HashMap;
import java.util.Set;

public class PopNeedsConfig {

    private static PopNeedsConfig instance = new PopNeedsConfig();

    private HashMap<String, HashMap<String, NeedsInfo>> needsMap;

    private HashMap<String, HashMap<String, NeedsInfo>> wantsMap;

    private PopNeedsConfig () {
        this.needsMap = new HashMap<>();
        this.wantsMap = new HashMap<>();
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

    public int getConsumeAmountForNeed(Pop pop, String needType) {
        return this.needsMap.get(pop.getRace()).get(needType).getConsumeAmount();
    }

    public void addWant(String race, NeedsInfo want) {
        if (this.wantsMap.containsKey(race)) {
            this.wantsMap.get(race).put(want.getName(), want);
        } else {
            HashMap<String, NeedsInfo> need = new HashMap<>();
            need.put(want.getName(), want);
            this.wantsMap.put(race, need);
        }
    }

    public Set<String> getWantTypesForPop(Pop pop) {
        return this.wantsMap.get(pop.getRace()).keySet();
    }

    public int getConsumeAmountForWant(Pop pop, String needType) {
        return this.wantsMap.get(pop.getRace()).get(needType).getConsumeAmount();
    }
}
