package andex.mvc.view.composite;

import java.util.HashMap;

/**
 * 构造列表项。
 */
public class ListableItem extends HashMap<String, Object> {

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "desc";
    public static final String KEY_ITEM_TYPE = "item_type";

    public ListableItem(long id, String title) {
        put(KEY_ID, id);
        put(KEY_TITLE, title);
        put(KEY_ITEM_TYPE, 0);
    }

    public ListableItem(long id, String title, String desc) {
        put(KEY_ID, id);
        put(KEY_TITLE, title);
        put(KEY_DESC, desc);
        put(KEY_ITEM_TYPE, 0);
    }

    public ListableItem(long id, String title, int itemType) {
        put(KEY_ID, id);
        put(KEY_TITLE, title);
        put(KEY_ITEM_TYPE, itemType);
    }

    /**
     * @param id
     * @param title
     * @param desc
     * @param itemType 表示列表项的类型，用于显示不同的项，从0开始，可以有任意多的值。
     */
    public ListableItem(long id, String title, String desc, int itemType) {
        put(KEY_ID, id);
        put(KEY_TITLE, title);
        put(KEY_DESC, desc);
        put(KEY_ITEM_TYPE, itemType);
    }

    /**
     * 业务ID，-1表示无效。
     *
     * @return
     */
    public long id() {
        return (Long) get(KEY_ID);
    }

    public String title() {
        return (String) get(KEY_TITLE);
    }

    public String description() {
        return (String) get(KEY_DESC);
    }

    public int itemType() {
        return (Integer) get(KEY_ITEM_TYPE);
    }

}
