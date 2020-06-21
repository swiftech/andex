package andex.core.status;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A StatusBus is a
 */
public class StatusBus {

    public static final String INIT_STATUS = "init";

    private static Map<Class, StatusBus> allBuses = new HashMap<>();

    private Context context;
    private View rootView;

    private Mapping statusMapping = new Mapping();

    /**
     * 当前的状态
     */
    private String currentStatus;

    private String composeStatus;

    /**
     * Create a new status instance if not exist?
     *
     * @param clazz
     * @param context
     * @param rootView
     * @return
     */
    public static StatusBus newInstance(Class clazz, Context context, View rootView) {
        StatusBus statusBus = new StatusBus(context, rootView);
        allBuses.put(clazz, statusBus);
        return statusBus;
    }

    /**
     * @param clazz
     * @return
     */
    public static StatusBus getInstance(Class clazz) {
        return allBuses.get(clazz);
    }


    private StatusBus(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    /**
     * Whether current status is INIT_STATUS
     *
     * @return
     */
    public boolean isInit() {
        return INIT_STATUS.equals(currentStatus);
    }

    /**
     * Check current status is
     *
     * @param statusName
     * @return
     */
    public boolean isStatus(String statusName) {
        return StringUtils.isNotBlank(currentStatus)
                && currentStatus.equals(statusName);
    }

    /**
     * Check current status is in listed statuses.
     *
     * @param statuses
     * @return
     */
    public boolean isStatusIn(String... statuses) {
        for (String status : statuses) {
            if (StringUtils.isNotBlank(currentStatus)
                    && currentStatus.equals(status)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get Current status
     *
     * @return
     */
    public String getStatus() {
        return currentStatus;
    }

    public boolean post() {
        return this.post(INIT_STATUS);
    }

    public boolean post(final String statusName) {
        if (StringUtils.isNotBlank(currentStatus)
                && currentStatus.equals(statusName)) {
            Log.w(StatusBus.class.getSimpleName(), "No status changes");
            return false;
        }

        Log.i(StatusBus.class.getSimpleName(), String.format("Change status from %s to %s", currentStatus, statusName));

        new Handler(context.getMainLooper()).post(() -> {
            // Handle OUT for current status
            if (StringUtils.isNotBlank(currentStatus)) {
                List<Action> outActions = statusMapping.getOut(currentStatus);
                if (outActions == null || outActions.isEmpty()) {
                    Log.d(getClass().getSimpleName(), String.format("No actions to execute for status '%s' out", currentStatus));
                } else {
                    Log.d(StatusBus.class.getSimpleName(), String.format("Execute %d actions for status '%s' out ", outActions.size(), currentStatus));
                    exeActions(outActions);
                }
            }

            // Handler IN for new status
            List<Action> inActions = statusMapping.getIn(statusName);
            if (inActions == null || inActions.isEmpty()) {
                Log.d(getClass().getSimpleName(), String.format("No actions to execute for status '%s' in", statusName));
            } else {
                Log.d(StatusBus.class.getSimpleName(), String.format("Execute %d actions for status '%s' in ", inActions.size(), statusName));
                exeActions(inActions);
            }
            currentStatus = statusName;
        });
        return true;
    }

    public boolean toggleStatus(String status1, String status2) {
        if (isStatus(status1)) {
            return post(status2);
        } else if (isStatus(status2)) {
            return post(status1);
        }
        return false;
    }

    private void exeActions(List<Action> actions) {
        // All mapped actions for one status
        for (Action action : actions) {
            if (action instanceof DefaultAction) {
                DefaultAction a = (DefaultAction) action;
                for (Integer resId : a.getResIdList()) {
                    View view = rootView.findViewById(resId);
                    if (view == null) {
                        Log.e(StatusBus.class.getSimpleName(), String.format("View %d could not be found", resId));
                        continue;
                    }
                    for (ActionConstants key : a.getActionMapping().keySet()) {
                        Object o = a.getActionMapping().get(key);
                        if (o == null) {
                            Log.w(this.getClass().getSimpleName(), String.format("Not available value for action %s", key));
                            continue;
                        }
                        Log.v(StatusBus.class.getSimpleName(), String.format("execute action(%s) to value %s", key, o));
                        switch (key) {
                            case ENABLE:
                                view.setEnabled(true);
                                break;
                            case DISABLE:
                                view.setEnabled(false);
                                break;
                            case VISIBILITY:
                                view.setVisibility((Integer) o);
                                break;
                            case TEXT_STRING:
                                ((TextView) view).setText(o.toString());
                                break;
                            case TEXT_RES:
                                ((TextView) view).setText((Integer) o);
                                break;
                            case HINT_STRING:
                                ((TextView) view).setHint(o.toString());
                                break;
                            case HINT_RES:
                                ((TextView) view).setHint((Integer) o);
                                break;
                            case BACKGROUND_COLOR:
                                Color color = (Color) o;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    view.setBackgroundColor(color.toArgb());
                                }
                                break;
                            case BACKGROUND_COLOR_INT:
                                view.setBackgroundColor((Integer) o);
                                break;
                            case BACKGROUND_RES:
                                view.setBackgroundResource((Integer) o);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + key);
                        }
                    }
                }
            } else {
                // customized actions execute before default actions, and if exception caught, will break the execution process.
                try {
                    action.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    break; // Prevent all other actions to be executed
                }
            }
        }
    }

    public StatusBus init() {
        composeStatus = INIT_STATUS;
        return this;
    }

    public StatusBus status(String statusName) {
        if (INIT_STATUS.equals(statusName)) {
            throw new IllegalArgumentException(String.format("The status %s is reserved", INIT_STATUS));
        }
        composeStatus = statusName;
        return this;
    }

    public StatusBus in(ActionBuilder actionBuilder) {
        Mapping.SubMapping inMapping = statusMapping.getSubMapping(composeStatus);
        List<DefaultAction> actions = inMapping.getDefaultActions(Mapping.StatusDirection.IN);
        for (Action action : actionBuilder.getActionList()) {
            actions.add((DefaultAction) action); // This may be NOT appropriate
        }
        return this;
    }

    public StatusBus in(Action action) {
        Mapping.SubMapping inMapping = statusMapping.getSubMapping(composeStatus);
        List<Action> actions = inMapping.getCustomizedActions(Mapping.StatusDirection.IN);
        actions.add(action);
        return this;
    }

    public StatusBus out(ActionBuilder actionBuilder) {
        Mapping.SubMapping inMapping = statusMapping.getSubMapping(composeStatus);
        List<DefaultAction> actions = inMapping.getDefaultActions(Mapping.StatusDirection.OUT);
        for (Action action : actionBuilder.getActionList()) {
            actions.add((DefaultAction) action); // This may be NOT appropriate
        }
        return this;
    }

    public StatusBus out(Action action) {
        Mapping.SubMapping inMapping = statusMapping.getSubMapping(composeStatus);
        List<Action> actions = inMapping.getCustomizedActions(Mapping.StatusDirection.OUT);
        actions.add(action);
        return this;
    }
}
