package andex.mvc;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import andex.Callback;
import andex.mvc.model.DataList;
import andex.mvc.model.DataRow;

public interface Flowable {
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
     * 从前一Activity或Fragment获取ID。
     *
     * @return
     */
    Object getIdObjectFromPrevious();

    int getIntIdFromPrevious();

    /**
     * @return
     */
    String getIdStrFromIntent();


    /**
     * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
     *
     * @param argName
     * @return
     */
    int getArgIntFromIntent(String argName);

    /**
     * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
     *
     * @param argName
     * @return
     */
    String getArgStrFromIntent(String argName);

    /**
     * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
     *
     * @param argName
     * @return
     */
    Object getArgFromIntent(String argName);


    DataList getDataListFromIntent();

    DataRow getDataRowFromIntent();

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
