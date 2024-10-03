package id.pras.thread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import id.pras.thread.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

  private ActivityMainBinding binding;
  private Thread thread;
  private Code code;
  private final String timesTag = " Unix Timestamp";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    // setup Code with background task and update ui with runOnUiThread
    code =
        () -> {
          // your code before update UI
          String result = String.valueOf(System.currentTimeMillis());
          runOnUiThread(
              () -> {
                binding.text.setText("update with Thread on : " + result + timesTag);
              });
        };
    // run interface code
    thread =
        new Thread(
            () -> {
              while (!Thread.currentThread().isInterrupted()) {
                code.run();
                try {
                  /*
                                  1000 is the delay in ms.
                  Note that runOnUiThread schedules the task to the main Thread, so even if the delay is set to 0
                  	  there will still be an additional delay in the actual execution.
                                 */
                  Thread.sleep(1000);
                } catch (InterruptedException err) {
                  binding.text.setText("interrupted :" + err.getMessage());
                  Thread.currentThread().interrupt();
                }
              }
            });
    thread.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.binding = null;
    thread.interrupt();
    thread = null;
  }

  @FunctionalInterface
  interface Code {
    void run();
  }
}
