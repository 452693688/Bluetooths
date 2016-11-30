package activity.ui.app.com.bluetooths;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import activity.ui.app.com.bluetooths.adapter.ListDataAdapter;
import activity.ui.app.com.bluetooths.bluetooth.BluetoothInstance;
import activity.ui.app.com.bluetooths.utile.DLog;


public class BluetoothClientActivity extends Activity implements OnClickListener {
    private ListDataAdapter adapter;
    private ArrayList<BluetoothDevice> phones = new ArrayList<BluetoothDevice>();
    private BluetoothInstance bluetooths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        bluetooths = new BluetoothInstance();
        bluetooths.bluetoothInit(this);
        bluetooths.setOnBluetoothListener(new BluetoothListener());
        findViewById(R.id.bluetoohs_btn).setOnClickListener(this);
        findViewById(R.id.open_btn).setOnClickListener(this);
        findViewById(R.id.close_btn).setOnClickListener(this);
        findViewById(R.id.find_btn).setOnClickListener(this);
        findViewById(R.id.pd_btn).setOnClickListener(this);
        ListView list = (ListView) findViewById(R.id.list);
        adapter = new ListDataAdapter(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
                //配对 并 且连接
                bluetooths.bluetoothPair(phones.get(i));

            }
        });

    }

    public void write(BluetoothDevice phone) {
        bluetooths.clientWrite(phone);
    }

    public void unpair(BluetoothDevice phone) {
        bluetooths.bluetoothUnpair(phone);
    }

    public void connect(BluetoothDevice phone) {

    }

    @Override
    protected void onDestroy() {
        bluetooths.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetoohs_btn:
                String[] msg = bluetooths.getBluetooth();
                boolean[] openState = bluetooths.getBluetoothOpenState();
                String name = "蓝牙名称：" + msg[0];
                String adderss = "蓝牙地址：" + msg[1];
                String state = "蓝牙状态：" + msg[2];
                String open = "是否打开：" + openState[0];
                String find = "正在查找：" + openState[1];
                DLog.e("蓝牙", name + "\n" + adderss + "\n" + state + "\n"
                        + open + "\n" + find);
                break;
            case R.id.open_btn:
                bluetooths.bluetoothOpen(true);
                break;
            case R.id.close_btn:
                // 关闭蓝牙
                bluetooths.bluetoothClose();
                break;
            case R.id.find_btn:
                bluetooths.bluetoothSearch();
                break;
            case R.id.pd_btn:
                phones.clear();
                phones.addAll(bluetooths.getBundleDevice());
                adapter.setData(phones);
                break;

        }
    }

    class BluetoothListener implements BluetoothInstance.OnBluetoothListener {
        @Override
        public void service(int code, Object obj) {

        }

        @Override
        public void client(int code, Object obj) {
            switch (code) {
                case 10101:
                    //发现设备
                    BluetoothDevice device = (BluetoothDevice) obj;
                    phones.add(device);
                    adapter.setData(phones);
                    break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bluetooths.onActivityResult(requestCode, resultCode, data);
    }

}
