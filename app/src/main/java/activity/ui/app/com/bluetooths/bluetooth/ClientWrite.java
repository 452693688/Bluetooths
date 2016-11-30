package activity.ui.app.com.bluetooths.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import activity.ui.app.com.bluetooths.utile.DLog;

/**
 * Created by Administrator on 2016/11/24.
 */

public class ClientWrite extends Thread {
    private BluetoothDevice device;
    private String context;
    private BluetoothSocket socket;

    public ClientWrite(BluetoothDevice device, String context) {
        if (TextUtils.isEmpty(context)) {
            context = "蓝牙传输测试";
        }
        this.device = device;
        this.context = context;
        try {
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            socket = (BluetoothSocket) m.invoke(device, 1);
            DLog.e("ClientWrite", "连接成功");
            start();
        } catch (Exception e) {
            socket=null;
            DLog.et("ClientWrite", "连接失败" + e.getMessage());
        }

    }

    @Override
    public void run() {
        try {
            OutputStream out = socket.getOutputStream();
            DataOutputStream writer = new DataOutputStream(out);
            writer.write(context.getBytes());
            writer.flush();
            writer.close();
            socket.close();
            DLog.et("ClientWrite", "数据写入完成");
        } catch (Exception e) {
            socket = null;
            e.printStackTrace();
            DLog.et("ClientWrite", "数据写入失败" + e.getMessage());
        }

    }

    private void w() {
        try {
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
            socket.connect();
            DLog.e("ClientWrite", "连接成功");
            //
            OutputStream out = socket.getOutputStream();
            out.write(context.getBytes());
            out.flush();
            out.close();
            socket.close();
            DLog.et("ClientWrite", "数据写入完成");
        } catch (Exception e) {
            e.printStackTrace();
            DLog.et("ClientWrite", "数据写入失败" + e.getMessage());
        }
    }

}
