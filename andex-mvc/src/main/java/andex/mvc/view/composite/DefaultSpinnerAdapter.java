package andex.mvc.view.composite;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import org.andex.R;

import andex.mvc.model.BaseListAdapter2;

/**
 * 默认的 Spinner 的适配器
 */
public class DefaultSpinnerAdapter extends BaseListAdapter2 implements SpinnerAdapter {

    public DefaultSpinnerAdapter(Context context) {
        super(context);
        super.itemViewBuilder = new ItemViewBuilder(context);
//		super.itemViewBuilder.itemType(android.R.layout.simple_spinner_item);
        super.itemViewBuilder.itemType(R.layout.ax_spinner_item);
        super.itemViewBuilder.title(android.R.id.text1);
    }

    public DefaultSpinnerAdapter(Context context, ItemViewBuilder itemViewBuilder) {
        super(context, itemViewBuilder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /**
     * 根据 Spinner 项的 ID 获取它的位置索引（通常用于选中动作）
     *
     * @param id
     * @return
     */
    public int indexOf(long id) {
        int ret = 0;
        for (Object item : super.data) {
            if (((ListableItem) item).id() == id) {
                return ret;
            }
            ret++;
        }
        return -1;
    }

    /**
     * 添加只有 title 的项
     *
     * @param items
     */
    public void addItems(String... items) {
        for (String item : items) {
            addItem(new ListableItem(0, item));
        }
    }

    /**
     * 添加 ID 和 title 一样的项
     *
     * @param items
     */
    public void addItems(Long... items) {
        for (Long item : items) {
            addItem(new ListableItem(item, String.valueOf(item)));
        }
    }

    /**
     * 添加 ID 和 title 一样的项
     *
     * @param items
     */
    public void addItems(Integer... items) {
        for (Integer item : items) {
            addItem(new ListableItem(item, String.valueOf(item)));
        }
    }
}
