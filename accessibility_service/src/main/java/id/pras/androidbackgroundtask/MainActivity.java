package id.pras.androidbackgroundtask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import id.pras.androidbackgroundtask.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

  private ActivityMainBinding binding;
  private Handler handler;
  private Thread thread;
  private Runnable runnable;
  private Code code;
  private final String timesTag = " Unix Timestamp";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    // background task and update ui with handler
    handler = new Handler(Looper.getMainLooper());
    runnable =
        () -> {
          binding.text1.setText(
              "update with Handler on : " + System.currentTimeMillis() + timesTag);
          handler.postDelayed(runnable, 1000);
        };
    handler.post(runnable);

    code =
        () -> {
          // your code before update UI
          runOnUiThread(
              () -> {
                binding.text2.setText(
                    "update with Thread on : " + System.currentTimeMillis() + timesTag);
              });
        };
    // background task and update ui with thread
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
                  binding.text2.setText("interrupted");
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
    handler.removeCallbacks(runnable);
    thread = null;
  }

  @FunctionalInterface
  interface Code {
    void run();
  }
}
