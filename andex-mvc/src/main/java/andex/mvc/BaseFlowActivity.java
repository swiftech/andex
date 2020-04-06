package andex.mvc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import andex.Callback;
import andex.Constants;
import andex.mvc.callback.Callback0;
import andex.mvc.controller.ActivityBuilder;
import andex.mvc.controller.FragmentBuilder;
import andex.mvc.model.DataList;
import andex.mvc.model.DataRow;

/**
 *
 */
public abstract class BaseFlowActivity extends FragmentActivity implements ActivityFlowable {

    protected Context context;

    // Resources from context.
    protected Resources rs;

    /**
     * {@inheritDoc}
     *
     * @param clazz
     */
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(clazz, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param clazz
     * @param forResult
     */
    public void startActivity(Class<? extends Activity> clazz, boolean forResult) {
        if (forResult) {
            startActivityForResult(new Intent(context, clazz), Constants.REQUEST_CODE_DEFAULT);
        } else {
            startActivity(new Intent(context, clazz));
        }
    }

    /**
     * 构建一个 Activity 的构造器。
     *
     * @param activityClass
     * @return
     */
    public ActivityBuilder buildActivity(Class activityClass) {
        return new ActivityBuilder(context, activityClass);
    }

    /**
     * 创建跳转至指定 Fragment 的构造器。
     *
     * @param fragment
     * @param resId
     * @return
     */
    public FragmentBuilder buildFragment(BaseFlowV4Fragment fragment, int resId) {
        return new FragmentBuilder(this, fragment).replace(resId);
    }

    /**
     * @deprecated
     */
    public void finishWithId(long id) {
        getIntent().getExtras().putLong(Constants.INTENT_DATA_ID_KEY, id);
        finish();
    }

    /**
     * @deprecated
     */
    public void finishWithData(DataRow row) {
        finishWithData(row, null);
    }

    /**
     * @param row
     * @param args
     * @deprecated
     */
    public void finishWithData(DataRow row, Bundle args) {
        Intent intent = new Intent();
//		debug("finishWithData() " + row.getClass());
        intent.putExtra(Constants.INTENT_DATA_ROW_KEY, row);
        intent.putExtra(Constants.INTENT_DATA_ARGS_KEY, args);
        setResult(RESULT_OK, intent);
        finish();
    }

    public Object getIdObjectFromPrevious() {
        if (this.getIntent().getExtras() == null) {
            return 0L; //需要转换成Long
        }
        Object v = this.getIntent().getExtras().get(Constants.INTENT_DATA_ID_KEY);
        if (v == null)
            return 0L; //需要转换成Long
        return v;
    }

    public long getLongIdFromPrevious() {
        return (Long) getIdObjectFromPrevious();
    }

    public int getIntIdFromPrevious() {
        return (Integer) getIdObjectFromPrevious();
    }

    public String getIdStrFromIntent() {
        if (this.getIntent().getExtras() == null) {
            return "";
        }
        Object v = this.getIntent().getExtras().get(Constants.INTENT_DATA_ID_KEY);
        if (v == null)
            return "";
        return (String) v;
    }

    public int getArgIntFromIntent(String argName) {
        Object o = getArgFromIntent(argName);
        if (o == null) {
            throw new RuntimeException(String.format("参数值%s不存在", argName));
        }
        return (Integer) o;
    }

    /**
     * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
     *
     * @param argName
     * @return
     */
    public String getArgStrFromIntent(String argName) {
        Object o = getArgFromIntent(argName);
        if (o == null) {
            Log.v(LOG_TAG, String.format("参数值%s不存在", argName));
            return null;
//			throw new RuntimeException(String.format("参数值%s不存在", argName));
        }
        return (String) o;
    }

    /**
     * 根据Key从前一个Activity或者Fragment的Intent参数中的参数对象中获得参数值。
     *
     * @param argName
     * @return
     */
    public Object getArgFromIntent(String argName) {
        Bundle extras = this.getIntent().getExtras();
        if (extras == null) {
            return null;
        }
        Bundle bundle = (Bundle) extras.get(Constants.INTENT_DATA_ARGS_KEY);
        if (bundle == null) {
            return null;
        }
        if (Constants.debugMode) {
            for (String key : bundle.keySet()) {
                Log.v(LOG_TAG, String.format("  ARG: %s = %s", key, bundle.get(key)));
            }
        }
        return bundle.get(argName);
    }


    @Override
    public DataList getDataListFromIntent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataRow getDataRowFromIntent() {
        return (DataRow) this.getIntent().getSerializableExtra(Constants.INTENT_DATA_ROW_KEY);
//		throw new UnsupportedOperationException();
    }

    /**
     * Simple handle click event for any View component.
     *
     * @param resId
     * @param handler
     */
    public View onViewClicked(int resId, final Callback handler) {
        final View view = this.findViewById(resId);
        if (view == null) {
            Log.w(LOG_TAG, String.format("No view found：%s", rs.getResourceName(resId)));
            return view;
        }
        view.setOnClickListener(v -> {
            view.setEnabled(false);
            handler.invoke();
            handler.invoke(v);
            view.setEnabled(true);
        });
        return view;
    }

    @Override
    public View onViewClicked(int resId, Callback0 handler) {
        final View view = this.findViewById(resId);
        if (view == null) {
            Log.w(LOG_TAG, String.format("No view found：%s", rs.getResourceName(resId)));
            return null;
        }
        view.setOnClickListener(v -> {
            view.setEnabled(false);
            handler.invoke();
            view.setEnabled(true);
        });
        return view;
    }

    public CheckBox onCheckBoxChecked(int resId, final CompoundButton.OnCheckedChangeListener listener) {
        CheckBox ckb = findViewById(resId);
        ckb.setOnCheckedChangeListener(listener);
        return ckb;
    }

    public CompoundButton onCompoundButtonChanged(int resId, final Callback<Boolean> handler) {
        final CompoundButton view = this.findViewById(resId);
        if (view == null) {
            Log.w(LOG_TAG, String.format("No view found：%s", rs.getResourceName(resId)));
            return view;
        }
        view.setOnCheckedChangeListener((buttonView, isChecked) -> {
            view.setEnabled(false);
            handler.invoke(isChecked);
            view.setEnabled(true);
        });
        return view;
    }
}
