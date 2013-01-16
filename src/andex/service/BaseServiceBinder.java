package andex.service;

import android.os.Binder;

/**
 * Base class for any service binder.
 * @author 
 *
 */
public class BaseServiceBinder extends Binder {
	
	protected BaseService service;
	
	public BaseServiceBinder(BaseService service) {
		this.service = service;
	}
	
	public boolean isServiceRunning() {
		return service.isRunning;
	}

	public void stopService() {
		service.stopService();
	}
	
	public void restartService() {
		service.restartService();
	}
}
