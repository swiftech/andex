package andex.mvc;

import andex.constants.LogConstants;
import andex.mvc.model.DataList;
import andex.mvc.model.DataRow;

public interface ActivityFlowable extends Flowable, LogConstants {

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
}
