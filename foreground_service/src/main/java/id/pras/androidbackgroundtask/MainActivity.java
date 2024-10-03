package id.pras.androidbackgroundtask;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import id.pras.androidbackgroundtask.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements ForegroundService.Callback{

  private boolean isBound=false;
  private ForegroundService service;
  private ServiceConnection serviceConnection=new ServiceConnection(){
	
	  @Override
	  public void onServiceConnected(ComponentName name, IBinder binder) {
	  	ForegroundService.LocalBinder serviceBinder=(ForegroundService.LocalBinder)binder;
		  service=serviceBinder.getService();
		  service.performTask(MainActivity.this);
		  isBound= true;
	  }
	  
		@Override
		public void onServiceDisconnected(ComponentName name) {
			isBound=false;
		}
		
	}
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

  }
  
  @Override
  protected void onStart() {
  	super.onStart();
	  Intent i=new Intent(this,ForegroundService.class);
	  bindService(i,serviceConnection,Context.BIND_AUTO_CREATE);
  }
  
  @Override
  protected void onStop() {
  	super.onStop();
  	unbindService(serviceConnection);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.binding = null;
  }
  
  @Override
  public void onResult(String s) {
	  if(isBound){
			runOnUiThread(()->binding.text.setText(s));
		}
  }
  
}
