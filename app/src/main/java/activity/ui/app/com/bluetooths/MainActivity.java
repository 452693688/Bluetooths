package activity.ui.app.com.bluetooths;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.servuce_btn).setOnClickListener(this);
    findViewById(R.id.client_btn).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    Intent it = new Intent();
    switch (v.getId()) {
      case R.id.servuce_btn:
        it.setClass(this, BluetoothServiceActivity.class);
        break;

      case R.id.client_btn:
        it.setClass(this, BluetoothClientActivity.class);
        break;
    }
    startActivity(it);
  }
}
