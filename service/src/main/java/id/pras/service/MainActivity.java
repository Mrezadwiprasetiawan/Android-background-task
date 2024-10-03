package id.pras.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.widget.Toast;
import id.pras.service.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements MainService.Callback {
  private MainService service;
  private boolean isBound = false;
  private ActivityMainBinding binding;

  private ServiceConnection serviceConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
          MainService.LocalBinder serviceBinder = (MainService.LocalBinder) binder;
          service = serviceBinder.getService();
          isBound = true;
          service.performTask(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          isBound = false;
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
  }

  @Override
  protected void onStart() {
    super.onStart();
    Intent intent = new Intent(this, MainService.class);
    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (isBound) {
      unbindService(serviceConnection);
      isBound = false;
    }
  }

  @Override
  public void onResult(String result) {
    runOnUiThread(
        () -> {
          binding.text.setText(result);
        });
  }
}
