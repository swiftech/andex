package andex.service; 
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Base class for any background services. 
 * Use static method {@code checkServiceStatus()} to check any service status.
 * @author 
 *
 */
public abstract class BaseService extends Service {
	public Context context;
	
	/**  */
	public boolean isServiceRunning;
	
	protected String LOG_TAG = "service";

	public BaseService() {
		super();
		this.context = this;
	}

	/**
	 * 实现这个方法可以使得Service驻留在内存中（即使APP被销毁），除非调用stopItself()或者stopService().
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int ret = super.onStartCommand(intent, flags, startId);
		Log.d("andex", String.format("Service %s start", this.getClass().getName()));
		return ret;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new BaseServiceBinder(this);
	}
	
	/**
	 * 
	 */
	public abstract void stopService();
	
	/**
	 * 
	 */
	public abstract void restartService();
	
	/**
	 * Check whether a service is running.
	 * @param ctx
	 * @param serviceClass
	 * @return true if service is running, false otherwise
	 */
	public static boolean checkServiceStatus(Context ctx, Class serviceClass) {
		ActivityManager actManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : actManager.getRunningServices(Integer.MAX_VALUE)) {
	        if (service.service.getClassName().equals(serviceClass.getName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	protected void debug(Object log) {
		Log.d("service", log.toString());
	}
}
