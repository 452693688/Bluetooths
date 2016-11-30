package activity.ui.app.com.bluetooths;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import activity.ui.app.com.bluetooths.bluetooth.BluetoothInstance;
import activity.ui.app.com.bluetooths.bluetooth.BluetoothService;
import activity.ui.app.com.bluetooths.utile.DLog;


public class BluetoothServiceActivity extends Activity implements OnClickListener {
    private TextView message;

    private String msgT;
    private BluetoothInstance bluetoots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actviity_service);
        message = (TextView) findViewById(R.id.message_tv);
        bluetoots = new BluetoothInstance();
        bluetoots.bluetoothInit(this);
        bluetoots.setOnBluetoothListener(new OnBluetooth());
        findViewById(R.id.serach_btn).setOnClickListener(this);
        findViewById(R.id.open_btn).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        bluetoots.onDestroy(this);
        BluetoothService.stop(this);
        super.onDestroy();
    }

    private Han han = new Han();

    class Han extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            message.setText(msgT);
        }
    }

    class OnBluetooth implements BluetoothInstance.OnBluetoothListener {
        @Override
        public void service(int code, Object obj) {
            byte[] buffer = (byte[]) obj;
            msgT = new String(buffer);
            han.sendEmptyMessage(1);
        }

        @Override
        public void client(int code, Object obj) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bluetoots.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.serach_btn:
                bluetoots.bluetoothSetFind(300);
                DLog.e("===", "已经设置成蓝牙可以被搜索");
                break;
            case R.id.open_btn:
                BluetoothService.start(this, bluetoots.getBluetoothAdapter(),
                        BluetoothInstance.NAME, BluetoothInstance.SPP_UUID);
                break;
        }


    }
}
