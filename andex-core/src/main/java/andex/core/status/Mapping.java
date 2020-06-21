package andex.core.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapping status and actions
 */
public class Mapping {

    private Map<String, SubMapping> statusMapping = new HashMap<>();

    public Map<String, SubMapping> getStatusMapping() {
        return statusMapping;
    }

    public void setStatusMapping(Map<String, SubMapping> statusMapping) {
        this.statusMapping = statusMapping;
    }

    public SubMapping getSubMapping(String statusName) {
        SubMapping subMapping = statusMapping.get(statusName);
        if (subMapping == null) {
            subMapping = new SubMapping();
            statusMapping.put(statusName, subMapping);
        }
        return subMapping;
    }

    public List<Action> getIn(String statusName) {
        SubMapping subMapping = statusMapping.get(statusName);
        if (subMapping == null) {
            subMapping = new SubMapping();
            statusMapping.put(statusName, subMapping);
        }
        return subMapping.getActions(StatusDirection.IN);
    }

    public List<Action> getOut(String statusName) {
        SubMapping subMapping = statusMapping.get(statusName);
        if (subMapping == null) {
            subMapping = new SubMapping();
            statusMapping.put(statusName, subMapping);
        }
        return subMapping.getActions(StatusDirection.OUT);
    }

    /**
     * Mapping status direction and actions
     */
    public static class SubMapping {
        private Map<StatusDirection, List<DefaultAction>> defaultActionMapping = new HashMap<>();
        private Map<StatusDirection, List<Action>> customizedActionMapping = new HashMap<>();

        public List<Action> getActions(StatusDirection statusDirection) {
            List<Action> customizedActions = getCustomizedActions(statusDirection);
            List<DefaultAction> defaultActions = getDefaultActions(statusDirection);
            List<Action> ret = new ArrayList<>(customizedActions.size() + defaultActions.size());
            Collections.addAll(ret, customizedActions.toArray(new Action[0]));
            Collections.addAll(ret, defaultActions.toArray(new Action[0]));
            return ret;
        }

        public List<Action> getCustomizedActions(StatusDirection statusDirection) {
            List<Action> actions = customizedActionMapping.get(statusDirection);
            if (actions == null) {
                actions = new ArrayList<>();
                customizedActionMapping.put(statusDirection, actions);
            }
            return actions;
        }

        public List<DefaultAction> getDefaultActions(StatusDirection statusDirection) {
            List<DefaultAction> actions = defaultActionMapping.get(statusDirection);
            if (actions == null) {
                actions = new ArrayList<>();
                defaultActionMapping.put(statusDirection, actions);
            }
            return actions;
        }

    }

    public enum StatusDirection {
        IN, OUT
    }
}
