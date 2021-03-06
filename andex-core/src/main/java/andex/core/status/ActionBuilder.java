package andex.core.status;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Building the actions for execution when status changes.
 */
public class ActionBuilder {

    private List<Action> actionList = new ArrayList<>();

    private DefaultAction current;

    public static ActionBuilder create() {
        return new ActionBuilder();
    }


    public ActionBuilder change(int... resIds) {
        current = new DefaultAction();
        current.addResource(resIds);
        actionList.add(current);
        return this;
    }

    public ActionBuilder enable(int... resIds) {
        this.change(resIds);
        current.mapping(ActionConstants.ENABLE, true); // this not work actually, just mark
        return this;
    }

    public ActionBuilder disable(int... resIds) {
        this.change(resIds);
        current.mapping(ActionConstants.DISABLE, false); // this not work actually, just mark
        return this;
    }

    public ActionBuilder visible(int... resIds) {
        this.change(resIds);
        current.mapping(ActionConstants.VISIBILITY, View.VISIBLE);
        return this;
    }

    public ActionBuilder invisible(int... resIds) {
        this.change(resIds);
        current.mapping(ActionConstants.VISIBILITY, View.INVISIBLE);
        return this;
    }

    public ActionBuilder gone(int... resIds) {
        this.change(resIds);
        current.mapping(ActionConstants.VISIBILITY, View.GONE);
        return this;
    }

    public ActionBuilder text(String content) {
        this.checkBuildState();
        current.mapping(ActionConstants.TEXT_STRING, content);
        return this;
    }

    public ActionBuilder text(int resId) {
        this.checkBuildState();
        current.mapping(ActionConstants.TEXT_RES, resId);
        return this;
    }

    public ActionBuilder text(int resId, String content) {
        current = new DefaultAction();
        current.addResource(resId);
        current.mapping(ActionConstants.TEXT_STRING, content);
        actionList.add(current);
        return this;
    }

    public ActionBuilder text(int resId, int strResId) {
        current = new DefaultAction();
        current.addResource(resId);
        current.mapping(ActionConstants.TEXT_RES, strResId);
        actionList.add(current);
        return this;
    }

    public ActionBuilder hint(String str) {
        this.checkBuildState();
        current.mapping(ActionConstants.HINT_STRING, str);
        return this;
    }

    public ActionBuilder hint(int resId) {
        this.checkBuildState();
        current.mapping(ActionConstants.HINT_RES, resId);
        return this;
    }

    public ActionBuilder hint(int resId, String str) {
        current = new DefaultAction();
        current.addResource(resId);
        current.mapping(ActionConstants.HINT_STRING, str);
        actionList.add(current);
        return this;
    }

    public ActionBuilder hint(int resId, int str) {
        current = new DefaultAction();
        current.addResource(resId);
        current.mapping(ActionConstants.HINT_RES, str);
        actionList.add(current);
        return this;
    }

    /**
     * @param resId
     * @param color
     * @return
     */
    @TargetApi(Build.VERSION_CODES.O)
    public ActionBuilder bgColor(int resId, Color color) {
        current = new DefaultAction();
        current.addResource(resId);
        current.mapping(ActionConstants.BACKGROUND_COLOR, color);
        actionList.add(current);
        return this;
    }

    public ActionBuilder bgColorInt(int resId, int colorInt) {
        current = new DefaultAction();
        current.addResource(resId);
        current.mapping(ActionConstants.BACKGROUND_COLOR_INT, colorInt);
        actionList.add(current);
        return this;
    }

    public ActionBuilder bgColorRes(int resId, int colorResId) {
        current = new DefaultAction();
        current.addResource(resId);
        current.mapping(ActionConstants.BACKGROUND_RES, colorResId);
        actionList.add(current);
        return this;
    }

    public ActionBuilder bgColor(Color color) {
        this.checkBuildState();
        current.mapping(ActionConstants.BACKGROUND_COLOR, color);
        return this;
    }

    public ActionBuilder bgColorInt(int colorInt) {
        this.checkBuildState();
        current.mapping(ActionConstants.BACKGROUND_COLOR_INT, colorInt);
        return this;
    }

    public ActionBuilder bgColorRes(int colorResId) {
        this.checkBuildState();
        current.mapping(ActionConstants.BACKGROUND_RES, colorResId);
        return this;
    }

    private void checkBuildState() {
        if (current == null) {
            throw new IllegalStateException("invoke change() method to specify one or more targets");
        }
    }

    /**
     * @return
     */
    public List<Action> getActionList() {
        return actionList;
    }
}
