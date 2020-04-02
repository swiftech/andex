package andex.mvc.view.composite;

import android.content.Context;

import andex.mvc.model.BaseListAdapter2;

/**
 * 默认布局的适配器。
 * 默认布局文件为：android.R.layout.simple_list_item2
 */
public class DefaultListViewAdapter extends BaseListAdapter2<ListableItem> {

    /**
     * 默认的布局
     *
     * @param context
     */
    public DefaultListViewAdapter(Context context) {
        super(context);
        super.itemViewBuilder = new ItemViewBuilder(context);
        super.itemViewBuilder.itemType(android.R.layout.simple_list_item_2);
        super.itemViewBuilder.title(android.R.id.text1);
        super.itemViewBuilder.description(android.R.id.text2);
    }

    /**
     * 自定义布局的
     *
     * @param context
     * @param itemViewBuilder
     */
    public DefaultListViewAdapter(Context context, ItemViewBuilder itemViewBuilder) {
        super(context, itemViewBuilder);
        super.itemViewBuilder = itemViewBuilder;
    }

}
