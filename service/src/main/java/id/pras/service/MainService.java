package id.pras.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MainService extends Service {
  private final IBinder binder = new LocalBinder();
  private String inMessage;
  private Thread thread;

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  public class LocalBinder extends Binder {
    MainService getService() {
      return MainService.this;
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (thread != null && thread.isAlive()) {
      thread.interrupt();
    }
  }

  public void performTask(Callback c) {
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

  public void sendMessage(String s) {
    this.inMessage = s;
  }

  public interface Callback {
    void onResult(String s);
  }
}
