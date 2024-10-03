package id.pras.androidbackgroundtask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class ForegroundService extends Service{
  private Thread thread;
  @Override
  public IBinder onBind(Intent i){
		return null;
	}
	public class LocalBinder extends Binder{
		ForegroundService getService(){
			return ForegroundService.this;
		}
	}
	
	public void performTask(Callback c){
		thread =
        new Thread(
            () -> {
              while (!Thread.currentThread().isInterrupted()) {
                String result = String.valueOf(System.currentTimeMillis());
                c.onResult(result);
                try {
                  Thread.sleep(1000);

                } catch (InterruptedException err) {
                  c.onResult(err.getMessage());
                  Thread.currentThread().interrupt();
                }
              }
            });
    thread.start();
	}
	public interface Callback{
	  void onResult(String s);
	}
}
