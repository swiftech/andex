package andex.widget;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import andex.AndroidUtils;
import andex.Callback;
import andex.mvc.BaseFlowV4Fragment;

/**
 * 扩展的基础Fragment类。
 * 注意：必须在使用前通过构造函数注入、或者onCreate()方法中设置布局资源ID。
 */
public abstract class Basev4Fragment<T extends FragmentActivity> extends BaseFlowV4Fragment<T> implements Extendable {


    /**
     * Simple Dialogs
     */
    public SimpleDialog simpleDialog;


    public Basev4Fragment() {
        super();
    }

    public Basev4Fragment(int resourceId) {
        super();
        if (resourceId == 0) {
            throw new IllegalArgumentException(String.format("Layout resource ID is not available: %d, you may implement the getLayoutResourceId() method", layoutResourceId));
        }
        this.layoutResourceId = resourceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.layoutResourceId = getLayoutResourceId();

        this.context = this.getActivity();
        this.parentActivity = (T) this.getActivity();
        this.rs = context.getResources();
        this.simpleDialog = new SimpleDialog(context);

        // 默认情况下Fragment的菜单都会加到Activity上。
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 此处如果不指定container，可能会出现子布局无法扩张到整个区域的问题
        if (layoutResourceId == 0) {
            throw new IllegalArgumentException(String.format("Layout Resource ID is not available: %d", layoutResourceId));
        }
        View view = inflater.inflate(layoutResourceId, container, false);
        if (view == null) {
            throw new RuntimeException("Check the Layout Resource ID ");
        }
        fragmentView = view;
        return fragmentView;
    }

    /**
     * 设置指定资源ID的TextView的文本为指定文本资源ID
     *
     * @param resId    TextView的资源ID
     * @param strResId 需要设置文本的资源ID
     * @return
     */
    @Override
    public TextView setTextViewText(int resId, int strResId) {
        return this.setTextViewText(resId, rs.getString(strResId));
    }


    /**
     * 设置指定资源ID的TextView的文本
     *
     * @param resId
     * @param str
     * @return
     */
    @Override
    public TextView setTextViewText(int resId, String str) {
        TextView tv = fragmentView.findViewById(resId);
        if (tv != null) {
            tv.setText(str);
        }
        return tv;
    }

    /**
     * 设置指定资源ID的Button的文本
     *
     * @param resId
     * @param str
     * @return
     */
    @Override
    public Button setButtonText(int resId, String str) {
        Button btn = fragmentView.findViewById(resId);
        btn.setText(str);
        return btn;
    }

    @Override
    public String getEditTextString(int resId) {
        return ((EditText)fragmentView.findViewById(resId)).getText().toString();
    }

    @Override
    public EditText setEditTextString(int resId, String str) {
        EditText et = fragmentView.findViewById(resId);
        if (et != null) {
            et.setText(str);
        }
        return et;
    }


    public ViewPager getViewPager(int resId) {
        return (ViewPager) fragmentView.findViewById(resId);
    }

    public DatePicker getDatePicker(int resId) {
        return (DatePicker) fragmentView.findViewById(resId);
    }

    @Override
    public String getNestedString(int sentence, Object... words) {
        return AndroidUtils.getNestedString(context, sentence, words);
    }

    /**
     * Make views disabled by resource ids.
     *
     * @param ids
     */
    @Override
    public void disableViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setEnabled(false);
        }
    }

    /**
     * Make views disabled.
     *
     * @param views
     */
    @Override
    public void disableViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setEnabled(false);
        }
    }

    /**
     * Make views enabled by resource ids.
     *
     * @param ids
     */
    @Override
    public void enableViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setEnabled(true);
        }
    }

    /**
     * Make view enabled.
     *
     * @param views
     */
    @Override
    public void enableViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setEnabled(true);
        }
    }

    /**
     * 显示多个视图组件
     *
     * @param ids
     */
    @Override
    public void showViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示多个视图组件
     *
     * @param views
     */
    @Override
    public void showViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏多个视图组件
     *
     * @param ids
     */
    @Override
    public void hideViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 隐藏多个视图组件
     *
     * @param views
     */
    @Override
    public void hideViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 暂时移除多个视图组件
     *
     * @param ids
     */
    @Override
    public void unblockViews(int... ids) {
        for (int id : ids) {
            if (fragmentView.findViewById(id) == null) {
                continue;
            }
            fragmentView.findViewById(id).setVisibility(View.GONE);
        }
    }

    /**
     * 暂时移多个视图组件
     *
     * @param views
     */
    @Override
    public void unblockViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(View.GONE);
        }
    }


    /**
     * Get view by it's name which is defined in XML.
     *
     * @param name
     * @return
     */
    @Override
    public View getViewByName(String name) {
        if (this.getActivity() == null) {
            return null;
        }
        int id = rs.getIdentifier(name, "id", this.getActivity().getPackageName());
        if (id == 0) {
            return null;
        }
        return fragmentView.findViewById(id);
    }

    @Override
    public View setViewBackground(int viewResId, int bgResId) {
        View v = fragmentView.findViewById(viewResId);
        v.setBackgroundResource(bgResId);
        return v;
    }


    /**
     * 隐藏当前的Fragment
     */
    public void hide() {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.hide(this);
        ft.commit();
    }


    @Override
    public void showConfirmDialog(String msg, DialogCallback callback) {
        simpleDialog.showConfirmDialog(msg, callback);
    }

    @Override
    public void showProgressDialog(String msg, DialogCallback callback) {
        simpleDialog.showProgressDialog(msg, 0, callback);
    }

    @Override
    public void showProgressDialog(String msg, long timeout, DialogCallback callback) {
        simpleDialog.showProgressDialog(msg, timeout, callback);
    }

    @Override
    public AlertDialog showTextInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
        return simpleDialog.showInputDialog(title, msg, InputType.TYPE_CLASS_TEXT, inputInit, callback);
    }

    @Override
    public AlertDialog showIntInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
        return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_SIGNED, inputInit, callback);
    }

    @Override
    public AlertDialog showFloatInputDialog(String title, String msg, String inputInit, DialogCallback callback) {
        return simpleDialog.showInputDialog(title, msg, InputType.TYPE_NUMBER_FLAG_DECIMAL, inputInit, callback);
    }

    @Override
    public AlertDialog showRadioGroupDialog(String title, String msg, String[] labels, int checked,
                                            final DialogCallback callback) {
        return simpleDialog.showRadioGroupDialog(title, msg, labels, checked, callback);
    }

    @Override
    public AlertDialog showCheckBoxsDialog(String title, BaseAdapter checkboxListViewAdapter, DialogCallback callback) {
        return simpleDialog.showCheckBoxesDialog(title, checkboxListViewAdapter, callback);
    }

    @Override
    public void showInfoDialog(int msgId) {
        simpleDialog.showInfoDialog(rs.getString(msgId));
    }

    @Override
    public void showInfoDialog(String msg) {
        simpleDialog.showInfoDialog(msg);
    }

    @Override
    public void showInfoDialog(String msg, DialogCallback callback) {
        simpleDialog.showInfoDialog(msg, callback);
    }

    @Override
    public void showListSelectDialog(String title, String[] items, DialogCallback callback) {
        simpleDialog.showListSelectDialog(title, items, callback);
    }

    @Override
    public void showListSelectDialog(String title, Map items, DialogCallback callback) {
        simpleDialog.showListSelectDialog(title, items, callback);
    }

    @Override
    public void dismissDialogOnTop() {
        simpleDialog.dismissDialogOnTop();
    }


    protected Callback.CallbackAdapter callbackFinish = new Callback.CallbackAdapter() {

        @Override
        public void invoke() {
            super.invoke();
            buildResultToPrevFragment().finish();
        }

    };

}
