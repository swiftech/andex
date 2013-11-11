package andex.service;

import android.os.Binder;
import android.os.Handler;

/**
 * Base class for any service binder.
 * @author 
 *
 */
public class BaseServiceBinder extends Binder {
	
	protected BaseService service;
	
	public Handler handler;
	
	public BaseServiceBinder() {
		this.handler = new Handler();
	}
	
	public BaseServiceBinder(BaseService service) {
		this();
		this.service = service;
	}
	
	public boolean isServiceRunning() {
		return service.isServiceRunning;
	}

	public void stopService() {
		if (service.isServiceRunning) {
			service.stopService();
		}
	}
	
	public void restartService() {
		if (service.isServiceRunning) {
			service.restartService();
		}
	}
}
