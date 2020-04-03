package andex.mvc;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import andex.Callback;
import andex.constants.LogConstants;
import andex.mvc.model.DataRow;

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

    void finishWithId(long id);

    void finishWithData(DataRow row);

    long getLongIdFromPrevious();

    /**
     * Simple handle click event for any View component.
     *
     * @param resId
     * @param handler
     */
    View onViewClicked(int resId, Callback handler);

    CheckBox onCheckBoxChecked(int resId, CompoundButton.OnCheckedChangeListener listener);

    CompoundButton onCompoundButtonChanged(int resId, Callback<Boolean> handler);
}
