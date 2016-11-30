package activity.ui.app.com.bluetooths.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import activity.ui.app.com.bluetooths.utile.DLog;

/**
 * Created by Administrator on 2016/11/24.
 */

public class ServiceWrite extends Thread {
    private BluetoothServerSocket mmServerSocket;
    public BluetoothAdapter mAdapter;
    private UUID SPP_UUID;
    private String name;

    public ServiceWrite(BluetoothAdapter mAdapter, String name, UUID SPP_UUID) {
        if (this.mAdapter != null) {
            DLog.et("ServiceWrite", "服务已开启");
        }
        this.mAdapter = mAdapter;
        this.SPP_UUID = SPP_UUID;
        this.name = name;
        initSocket();
        start();
    }


    public void initSocket() {
        try {
            mmServerSocket = mAdapter.listenUsingRfcommWithServiceRecord(name, SPP_UUID);
            DLog.et("ServiceWrite", "开启成功，ServerSocket获取成功");
        } catch (IOException e) {
            mAdapter = null;
            DLog.et("ServiceWrite", "开启失败，ServerSocket获取失败");
        }
    }

    public void run() {
        if (mmServerSocket == null) {

        }
        BluetoothSocket socket;
        try {
            socket = mmServerSocket.accept();
            InputStream input = socket.getInputStream();
            byte[] buffer = new byte[1024];
            input.read(buffer);
            input.close();
            mmServerSocket.close();
            DLog.et("ServiceWrite", "数据写入完成");
        } catch (IOException e) {
            DLog.et("ServiceWrite", "数据写入失败");
        }
    }
}
