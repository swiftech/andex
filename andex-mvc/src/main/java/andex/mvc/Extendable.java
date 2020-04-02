package andex.mvc;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import andex.Callback;
import andex.mvc.model.DataRow;
import andex.mvc.view.DialogCallback;

public interface Extendable {

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


    void showConfirmDialog(String msg, DialogCallback callback);

    void showProgressDialog(String msg, DialogCallback callback);

    void showProgressDialog(String msg, long timeout, DialogCallback callback);

    /**
     * Show dialog that allows any text to input.
     *
     * @param title
     * @param msg
     * @param inputInit
     * @param callback
     * @return
     */
    AlertDialog showTextInputDialog(String title, String msg, String inputInit, DialogCallback callback);

    /**
     * Show dialog that only allows integer number to input.
     *
     * @param title
     * @param msg
     * @param inputInit
     * @param callback
     * @return
     */
    public AlertDialog showIntInputDialog(String title, String msg, String inputInit, DialogCallback callback);

    /**
     * Show dialog that only allows float number to input.
     *
     * @param title
     * @param msg
     * @param inputInit
     * @param callback
     * @return
     */
    AlertDialog showFloatInputDialog(String title, String msg, String inputInit, DialogCallback callback);

    AlertDialog showRadioGroupDialog(String title, String msg, String[] labels, int checked,
                                     final DialogCallback callback);

    AlertDialog showCheckBoxsDialog(String title, BaseAdapter checkboxListViewAdapter,
                                    final DialogCallback callback);

    void showInfoDialog(int msgId);

    void showInfoDialog(String msg);

    void showInfoDialog(String msg, DialogCallback callback);

    void showListSelectDialog(String title, String[] items, DialogCallback callback);

    void showListSelectDialog(String title, Map items, DialogCallback callback);

    void dismissDialogOnTop();


    /**
     * Get view by it's name which is defined in XML.
     *
     * @param name
     * @return
     */
    View getViewByName(String name);

    View setViewBackground(int viewResId, int bgResId);


    /**
     * 设置指定资源ID的TextView的文本为指定文本资源ID
     *
     * @param resId    TextView的资源ID
     * @param strResId 需要设置文本的资源ID
     * @return
     */
    TextView setTextViewText(int resId, int strResId);

    /**
     * 设置指定资源ID的TextView的文本
     *
     * @param resId
     * @param str
     * @return
     */
    TextView setTextViewText(int resId, String str);

    /**
     * 设置指定资源ID的Button的文本
     *
     * @param resId
     * @param str
     * @return
     */
    Button setButtonText(int resId, String str);


    String getEditTextString(int resId);

    EditText setEditTextString(int resId, String str);

    // int getEditTextInt(int resId) {
    // String str = getEditTextString(resId);
    // if(Utils.isEmpty(str)) {
    // return 0;
    // }
    // Integer.parseInt(str);
    // }


    /**
     * 取出并处理嵌入式的字符资源，嵌入格式: {编号}
     *
     * @param sentence
     * @param words    字符串值或者字符串资源ID可以混合使用
     * @return
     */
    String getNestedString(int sentence, Object... words);

    /**
     * 显示多个视图组件
     *
     * @param ids
     */
    void showViews(int... ids);

    /**
     * 显示多个视图组件
     *
     * @param views
     */
    void showViews(View... views);

    /**
     * 隐藏多个视图组件
     *
     * @param ids
     */
    void hideViews(int... ids);

    /**
     * 隐藏多个视图组件
     *
     * @param views
     */
    void hideViews(View... views);

    /**
     * 暂时移除多个视图组件
     *
     * @param ids
     */
    void unblockViews(int... ids);

    /**
     * 暂时移多个视图组件
     *
     * @param views
     */
    void unblockViews(View... views);

    /**
     * Make views disabled by resource ids.
     *
     * @param ids
     */
    void disableViews(int... ids);

    /**
     * Make views disabled.
     *
     * @param views
     */
    void disableViews(View... views);

    /**
     * Make views enabled by resource ids.
     *
     * @param ids
     */
    void enableViews(int... ids);

    /**
     * Make view enabled.
     *
     * @param views
     */
    void enableViews(View... views);


    /**
     * Simple handle click event for any View component.
     *
     * @param resId
     * @param handler
     */
    View onViewClicked(int resId, final Callback handler);

    CheckBox onCheckBoxChecked(int resId, final OnCheckedChangeListener listener);

    CompoundButton onCompoundButtonChanged(int resId, final Callback<Boolean> handler);
}
