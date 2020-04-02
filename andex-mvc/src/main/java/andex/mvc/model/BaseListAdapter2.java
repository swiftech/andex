package andex.mvc.model;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import org.andex.R;

import java.util.ArrayList;
import java.util.List;

import andex.mvc.view.composite.ItemViewBuilder;
import andex.mvc.view.composite.ListableItem;

/**
 * 列表适配器基类。
 *
 * @author
 * @see ItemViewBuilder
 */
public class BaseListAdapter2<B extends ListableItem> implements ListAdapter {

    private static final String LOG_TAG = BaseListAdapter2.class.getSimpleName();

    protected Context context;

    protected LayoutInflater inflater;

    protected List<B> data = new ArrayList<>();

    protected ItemViewBuilder itemViewBuilder;

    /**
     * 基本的ListAdapter实现，注入Context和初始化了Inflater
     *
     * @param context
     */
    public BaseListAdapter2(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public BaseListAdapter2(Context context, ItemViewBuilder itemViewBuilder) {
        this(context);
        this.itemViewBuilder = itemViewBuilder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (data.isEmpty()) {
            throw new RuntimeException("No data");
        }
        ListableItem item = data.get(position);
        if (item == null) {
            Log.v(LOG_TAG, String.format("No item at position: %d", position));
            return null;
        }

        if (itemViewBuilder == null) {
//			Log.e(LOG_TAG, "No item view builder for adapter");
//			return null;
            Log.w(LOG_TAG, String.format("No customized item view builder specified for position %d, use default instead", position));
            itemViewBuilder = new ItemViewBuilder(context).itemType(R.layout.ax_lv_item_default).title(R.id.ax_tv_lv_item_title);
        }

//		Log.v(LOG_TAG, String.format("ItemType:%d", item.itemType()));

        int itemResId = (Integer) itemViewBuilder.itemTypes.get(item.itemType());
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(itemResId, null);

        itemViewBuilder.build(view, item);
        return view;
    }

    public void addItem(B item) {
        data.add(item);
    }

    public void removeItem(B item) {
        data.remove(item);
    }

    public void removeItem(int id) {
        for (B b : data) {
            if (b.id() == id) {
                removeItem(b);
                return;
            }
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public B getItem(int position) {
        return data.get(position);
    }

    /**
     * 返回初始化时设定的 ID
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    /**
     * @return
     */
    @Override
    public int getViewTypeCount() {
        if (itemViewBuilder == null || itemViewBuilder.itemTypes == null) {
            return 1;
        } else {
            return itemViewBuilder.itemTypes.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemViewBuilder == null || itemViewBuilder.itemTypes == null) {
            return 0;
        } else {
            return data.get(position).itemType() % itemViewBuilder.itemTypes.size();
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}