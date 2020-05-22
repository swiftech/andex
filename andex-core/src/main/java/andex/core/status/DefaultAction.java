package andex.core.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Action for common like changing text, background etc.
 *
 * @see ActionBuilder
 */
public class DefaultAction implements Action {

    /**
     *
     */
    private List<Integer> resIdList = new ArrayList<>();

    /**
     *
     */
    private Map<ActionConstants, Object> actionMapping = new TreeMap<>();

    public void addResource(int... resIds) {
        for (int resId : resIds) {
            resIdList.add(resId);
        }
    }

    /**
     *
     * @param key
     * @param value
     */
    public void mapping(ActionConstants key, Object value) {
        actionMapping.put(key, value);
    }

    public List<Integer> getResIdList() {
        return resIdList;
    }

    public void setResIdList(List<Integer> resIdList) {
        this.resIdList = resIdList;
    }

    public Map<ActionConstants, Object> getActionMapping() {
        return actionMapping;
    }

    public void setActionMapping(Map<ActionConstants, Object> actionMapping) {
        this.actionMapping = actionMapping;
    }

    @Override
    public void execute() {
        // DO NOTHING for now
    }
}
