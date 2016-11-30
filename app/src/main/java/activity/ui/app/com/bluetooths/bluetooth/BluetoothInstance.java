package activity.ui.app.com.bluetooths.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import activity.ui.app.com.bluetooths.utile.DLog;

public class BluetoothInstance {
    private String tag = "BluetoothInstance";

    // 固定的UUID
    public static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FA");

    public static final String NAME = "test_name";
    private final int OPEN_BLUETOOTH_INTENT_CODE = 100;
    private final int SHOW_TIME_INTENT_CODE = 200;

    private BluetoothAdapter mAdapter;
    private BluetoothReceiver bluetoothReceiver;
    private Activity activity;

    public void bluetoothInit(Activity activity) {
        this.activity = activity;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        // 注册广播

        bluetoothReceiver = new BluetoothReceiver();
        activity.registerReceiver(bluetoothReceiver, getFilter());
        // 获取已经配对的设备

    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mAdapter;
    }

    //蓝牙名称,蓝牙地址 ,蓝牙状态
    public String[] getBluetooth() {
        String[] msg = new String[]{mAdapter.getName(), mAdapter.getAddress()
                , mAdapter.getState() + ""};
        return msg;
    }

    //是否打开,正在查找
    public boolean[] getBluetoothOpenState() {
        boolean[] state = new boolean[]{mAdapter.isEnabled(), mAdapter.isDiscovering()};
        return state;
    }

    // 已经配对的设备列表
    public ArrayList<BluetoothDevice> getBundleDevice() {
        ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
        Set<BluetoothDevice> bd = mAdapter.getBondedDevices();
        if (devices != null) {
            devices.addAll(bd);
        }
        return devices;
    }

    //openType 蓝牙打开方式
    public void bluetoothOpen(boolean openType) {
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.isEnabled()) {
            // 蓝牙已经打开
            return;
        }
        if (openType) {
            // 调用系统API去打开蓝牙
            Intent Intemtenabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(Intemtenabler, OPEN_BLUETOOTH_INTENT_CODE);
        } else {
            //打开蓝牙
            mAdapter.enable();
        }
    }

    //关闭蓝牙
    public void bluetoothClose() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.disable();
    }

    //打开本机的蓝牙被发现功能（默认打开120秒，可以将时间最多延长至300秒）
    public void bluetoothSetFind(int showTime) {
        if (showTime < 120) {
            showTime = 120;
        }
        if (showTime > 300) {
            showTime = 300;
        }
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, showTime);// 设置持续时
        activity.startActivityForResult(discoveryIntent, SHOW_TIME_INTENT_CODE);
    }

    //取消搜索
    public void bluetoothCancelSearch() {
        if (!mAdapter.isDiscovering()) {
            return;
        }
        mAdapter.cancelDiscovery();
    }

    //   开始搜索蓝牙设备
    public void bluetoothSearch() {
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        mAdapter.startDiscovery();
    }

    // 配对：Lenovo S898t+(21010): CC:07:E4:90:65:AF
    public void bluetoothPair(BluetoothDevice device) {
        String name = device.getName();
        String address = device.getAddress();
        // 取消搜索
        mAdapter.cancelDiscovery();
        // 获取蓝牙设备的连接状态
        int connectState = device.getBondState();
        switch (connectState) {
            case BluetoothDevice.BOND_NONE:

                // 未配对
                try {
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    createBondMethod.invoke(device);
                    DLog.e(tag, "配对完成");
                } catch (Exception e) {
                    DLog.e(tag, "配对出错");
                }
                break;

            case BluetoothDevice.BOND_BONDED:
                // 已配对

                break;
        }

    }

    public void bluetoothUnpair(BluetoothDevice device) {
        /*if (Build.VERSION.SDK_INT >= 19) {
            boolean isUn = device.createBond();
            DLog.e(tag, "取消配对" + Build.VERSION.SDK_INT + isUn);
            return;
        }*/
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            DLog.e(tag, "取消配对成功");
        } catch (Exception e) {
            DLog.e(tag, e.getMessage());
        }
    }

    public void test(BluetoothDevice device) {
        // mAdapter.
        //boolean b = device.createBond();
    }

    public void clientWrite(BluetoothDevice device) {
        mAdapter.cancelDiscovery();
        new ClientWrite(device, "");
    }

    //设备连接
 /*   public void bluetoothConnect(BluetoothDevice device) {
        try {
            // 固定的UUID
            ParcelUuid[] uuid = device.getUuids();
            UUID uid = uuid[0].getUuid();
            DLog.e(tag, "uid:" + uid);
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uid);
            socket.connect();
            //socket.getRemoteDevice();
            DLog.e(tag, "连接成功");
        } catch (Exception e) {
            DLog.e(tag, "连接失败\n" + e.getMessage());
        }
    }*/

  /*  public void v(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
            mAdapter.cancelDiscovery();
            socket.connect();
            DLog.e(tag, "连接成功");
        } catch (Exception e) {
            DLog.e(tag, "连接失败\n" + e.getMessage());
        }
    }*/

    public void openService() {
        new ServiceWrite(mAdapter, NAME, SPP_UUID);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPEN_BLUETOOTH_INTENT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    DLog.e(tag, "允许本地蓝牙被附近的其它蓝牙设备发现");
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    DLog.e(tag, "不允许蓝牙被附近的其它蓝牙设备发现");
                }
                break;
            case SHOW_TIME_INTENT_CODE:
                DLog.e(tag, "蓝牙设备可以被发现时间" + resultCode);
                break;
        }

    }

    public IntentFilter getFilter() {
        IntentFilter intentFilter = new IntentFilter();
        //蓝牙搜索
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //蓝牙配对
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        return intentFilter;
    }

    //蓝牙广播
    class BluetoothReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                // 开始搜索
                DLog.e("搜索设备", "开始搜索...........");
            }
            // 发现设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !TextUtils.isEmpty(device.getName())) {
                    onBluetoothListener.client(10101, device);
                }
                DLog.e("搜索设备", "发现设备...........");
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 搜索完成
                DLog.e("搜索设备", "搜索完成...........");
            }
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        DLog.e("BlueToothTestActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        DLog.e("BlueToothTestActivity", "完成配对");
                        break;
                    case BluetoothDevice.BOND_NONE:
                        DLog.e("BlueToothTestActivity", "取消配对");
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 注销广播
     */
    public void onDestroy(Activity activity) {
        if (bluetoothReceiver == null) {
            return;
        }
        activity.unregisterReceiver(bluetoothReceiver);
    }

    private OnBluetoothListener onBluetoothListener;

    public void setOnBluetoothListener(OnBluetoothListener onBluetoothListener) {
        this.onBluetoothListener = onBluetoothListener;
    }

    //事件回调
    public interface OnBluetoothListener {
        void service(int code, Object obj);

        void client(int code, Object obj);
    }
}
