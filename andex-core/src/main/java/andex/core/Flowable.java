package andex.core;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import andex.Callback;
import andex.constants.LogConstants;
import andex.core.callback.Callback0;
import andex.core.model.DataRow;

public interface Flowable extends LogConstants {

    /**
     * 按照Activity的Class启动
     *
     * @param clazz
     */
    void startActivity(Class<? extends Activity> clazz);

    /**
     * 按照Activity的Class启动并返回
     *
     * @param clazz
     * @param forResult
     */
    void startActivity(Class<? extends Activity> clazz, boolean forResult);

    /**
     * Finish current activity and return with a long id.
     *
     * @param id
     * @deprecated
     */
    void finishWithId(long id);

    /**
     * Finish current activity and return with a {@link DataRow}
     *
     * @param row
     */
    void finishWithData(DataRow row);

    long getLongIdFromPrevious();

    /**
     * Simple handle click event for any View component.
     *
     * @param resId
     * @param handler
     * @deprecated
     */
    View onViewClicked(int resId, Callback handler);

    /**
     * Simple handle click event for any View component.
     *
     * @param resId
     * @param handler
     * @return
     */
    View onViewClicked(int resId, Callback0 handler);

    CheckBox onCheckBoxChecked(int resId, CompoundButton.OnCheckedChangeListener listener);

    CompoundButton onCompoundButtonChanged(int resId, Callback<Boolean> handler);
}
