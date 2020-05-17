package andex.mvc.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapping {

    private Map<String, SubMapping> statusMapping = new HashMap<>();

    public Map<String, SubMapping> getStatusMapping() {
        return statusMapping;
    }

    public void setStatusMapping(Map<String, SubMapping> statusMapping) {
        this.statusMapping = statusMapping;
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


    public static class SubMapping {
        private Map<StatusDirection, List<Action>> m = new HashMap<>();

        public List<Action> getActions(StatusDirection dir) {
            List<Action> actions = m.get(dir);
            if (actions == null) {
                actions = new ArrayList<>();
                m.put(dir, actions);
            }
            return actions;
        }

    }

    public enum StatusDirection {
        IN, OUT
    }
}
