package andex;

public class Constants {
	
	
	// Key of data in intent extra bundle.
	public static final String INTENT_DATA_ID_KEY = "INTENT_DATA_ID";
	
	public static final String INTENT_DATA_ARGS_KEY = "INTENT_DATA_ARGS";
	public static final String INTENT_DATA_LIST_KEY = "INTENT_DATA_LIST";
	public static final String INTENT_DATA_ROW_KEY = "INTENT_DATA_ROW";
	
	
	// 用于在Fragment参数中传递ID值
	public static final String FRAGMENT_DATA_ID_KEY = "FRAGMENT_DATA_ID_KEY";
	
	// 用于在Fragment参数中传递选项值
	public static final String FRAGMENT_DATA_OPTION_KEY = "FRAGMENT_DATA_OPTION_KEY"; 
	
	// 系统属性名
	public static final String SYS_PROP_DEBUG_MODE = "andex.debug";
	public static final String SYS_PROP_DB_VERSION = "andex.db.version";
	
	//
	public static final int REQUEST_CODE_DEFAULT = 1000;
	
	// DEBUG模式（默认） changed by setting system arguments "andex.debug"
	public static boolean debugMode = true;
	
	
}
