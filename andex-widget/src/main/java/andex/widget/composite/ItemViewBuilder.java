package andex.widget.composite;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * 列表项视图创建器。如果有更多的内容需要构建，则继承并实现 build() 方法。
 * itemType() 定制项布局资源文件。
 * 用 title() 和 description() 方法指定显示它们在布局何处。
 */
public class ItemViewBuilder<I extends ListableItem> {

    protected Context context;

    /**
     * 列表项与项资源文件的对应关系
     */
    public Map<Integer, Integer> itemTypes = new HashMap<>();

    protected View itemLayout;

    protected int titleResId;

    protected int descResId;

    public ItemViewBuilder(Context context) {
        this.context = context;
    }

    public ItemViewBuilder title(int resId) {
        titleResId = resId;
        return this;
    }

    public ItemViewBuilder description(int resId) {
        descResId = resId;
        return this;
    }

    /**
     * 默认布局
     *
     * @param layoutResId
     * @return
     */
    public ItemViewBuilder itemType(int layoutResId) {
        return itemType(0, layoutResId);
    }

    /**
     * 设置列表项与项布局的对应关系
     *
     * @param type
     * @param layoutResId
     * @return
     */
    public ItemViewBuilder itemType(int type, int layoutResId) {
        itemTypes.put(type, layoutResId);
        return this;
    }

    /**
     * @param itemLayout
     * @param item
     */
    public void build(View itemLayout, final I item) {
        if (itemLayout == null || item == null) {
            throw new IllegalArgumentException();
        }

        this.itemLayout = itemLayout;
        TextView tvTitle = getTitleView();
        TextView tvDesc = getDescriptionView();

        if (tvTitle != null) {
            tvTitle.setText(item.title());
        }

        if (tvDesc != null) {
            tvDesc.setText(item.description());
        }
    }


    protected TextView getTitleView() {
        return (TextView) itemLayout.findViewById(titleResId);
    }

    protected TextView getDescriptionView() {
        return (TextView) itemLayout.findViewById(descResId);
    }

}
