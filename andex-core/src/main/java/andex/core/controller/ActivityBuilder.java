package andex.core.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

import andex.Constants;
import andex.core.constant.FlowConstants;

/**
 * 如果需要更多的处理（比如特定的参数）可以继承实现自己的Builder。
 *
 * @see ResultBuilder
 */
public class ActivityBuilder implements ControlBuilder {

    private Context context;

    private Intent intent;
    private Fragment preFrag;

    private Class activityClass;

    private String activityName;

    /**
     * 通过指定Activity名字来启动它。
     *
     * @param context
     * @param activityName 想要启动的 Activity 名字
     */
    public ActivityBuilder(Context context, String activityName) {
//		if (preFrag == null) {
//			throw new IllegalArgumentException("Previous Fragment Needed");
//		}
        this.context = context;
        this.activityName = activityName;
        this.intent = new Intent(Intent.ACTION_VIEW);
        ComponentName cn = new ComponentName(context.getPackageName(), activityName);
        intent.setComponent(cn);
    }

    /**
     * 通过指定Activity类型启动它。
     *
     * @param context
     * @param activityClass 想要启动的 Activity 类型
     */
    public ActivityBuilder(Context context, Class activityClass) {
//		if (preFrag == null) {
//			throw new IllegalArgumentException("Previous Fragment Needed");
//		}
        this.context = context;
        this.activityClass = activityClass;
        this.intent = new Intent(context, activityClass);
    }

    /**
     * 从某个Fragment启动的。
     *
     * @param preFrag
     * @return
     */
    public ActivityBuilder from(Fragment preFrag) {
        this.preFrag = preFrag;
        return this;
    }

    /**
     * With a Long type ID to new Activity
     *
     * @param id
     * @return
     */
    public ActivityBuilder withId(long id) {
        with(Constants.INTENT_DATA_ID_KEY, id);
        return this;
    }

    public ActivityBuilder withId(Serializable id) {
        with(Constants.INTENT_DATA_ID_KEY, id);
        return this;
    }

    /**
     * With a Integer type option to new Activity
     *
     * @param option
     * @return
     */
    public ActivityBuilder withOption(int option) {
        with(Constants.FRAGMENT_DATA_OPTION_KEY, option);
        return this;
    }

    /**
     * With a key-value arguments
     *
     * @param key
     * @param value
     * @return
     */
    public ActivityBuilder with(String key, Serializable value) {
        this.intent.putExtra(key, value);
        return this;
    }

    /**
     * With arguments from Bundle arguments
     *
     * @param args
     * @return
     */
    public ActivityBuilder with(Bundle args) {
        intent.putExtras(args);
        return this;
    }

    public ActivityBuilder fullscreen() {
        with(FlowConstants.FLAG_FULL_SCREEN, true);
        return this;
    }

    /**
     * Intent.FLAG_ACTIVITY_CLEAR_TOP 标志
     *
     * @return
     */
    public ActivityBuilder clearTop() {
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return this;
    }

    public ActivityBuilder start() {
        if (this.intent == null) {
            throw new IllegalStateException("No Intent Provided");
        }
        if (preFrag == null) {
            context.startActivity(intent);
        } else {
            preFrag.startActivity(this.intent);
        }
        return this;
    }

    /**
     * @return
     */
    public ActivityBuilder startForResult() {
        if (this.intent == null) {
            throw new IllegalStateException("No Intent Provided");
        }
        preFrag.startActivityForResult(this.intent, Constants.REQUEST_CODE_DEFAULT);
        return this;
    }
}
