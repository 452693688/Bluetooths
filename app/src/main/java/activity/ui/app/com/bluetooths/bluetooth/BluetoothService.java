package activity.ui.app.com.bluetooths.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import activity.ui.app.com.bluetooths.utile.DLog;

/**
 * Created by Administrator on 2016/11/24.
 */

public class BluetoothService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static boolean isClose;

    @Override
    public ComponentName startService(Intent service) {
        while (true) {
            try {
                BluetoothSocket socket = serverSocket.accept();
                new ReadSocket(socket);
                if (isClose) {
                    closeSocket();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                DLog.et("BluetoothService", "serverSocket 获取异常");
            }
        }
        return super.startService(service);
    }

    private void closeSocket() {

        if (serverSocket == null) {
            DLog.et("BluetoothService", "serverSocket == null");
            return;
        }
        try {
            serverSocket.close();
            serverSocket = null;
            isClose = false;
            DLog.et("BluetoothService", "serverSocket.close");
        } catch (Exception e) {
            DLog.et("BluetoothService", "serverSocket关闭异常" + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        isClose = true;
        super.onDestroy();
        DLog.et("BluetoothService", "onDestroy");

    }

    class ReadSocket implements Runnable {
        private BluetoothSocket socket;

        public ReadSocket(BluetoothSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 获取读取流
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                byte[] buffer = new byte[1024];
                reader.read(buffer);
                int dataLength = -1;
                while ((dataLength = reader.read(buffer)) != -1) {
                    if (isClose) {
                        break;
                    }
                    // 读取数据
                    String msg = new String(buffer, 0, dataLength);
                    DLog.et("BluetoothService", "获取到客户端的信息：" + msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
                DLog.et("BluetoothService", "获取到客户端的信息异常：" + e.getMessage());
            }
        }
    }


    private static BluetoothServerSocket serverSocket;

    public static void start(Context context, BluetoothAdapter adapter, String name, UUID SPP_UUID) {
        if (!isClose) {
            isClose = true;
        }
        try {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(name, SPP_UUID);
            DLog.et("ServiceWrite", "开启成功，ServerSocket获取成功");
        } catch (IOException e) {
            DLog.et("ServiceWrite", "开启失败，ServerSocket获取失败");
        }
        if (serverSocket == null) {
            DLog.et("ServiceWrite", "serverSocket==null");
            return;
        }
        isClose = false;
        Intent intent = new Intent(context, BluetoothService.class);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent it = new Intent(context, BluetoothService.class);
        context.stopService(it);
    }
}
