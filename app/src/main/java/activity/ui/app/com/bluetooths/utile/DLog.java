package activity.ui.app.com.bluetooths.utile;

import android.util.Log;
import android.widget.Toast;

import activity.ui.app.com.bluetooths.BaseApplication;

/**
 * Created by Administrator on 2016/11/24.
 */

public class DLog {
    public static void e(String tag, String value) {
        Log.e(tag, value);
        Toast.makeText(BaseApplication.context, value, Toast.LENGTH_LONG).show();
    }
    public static void et(String tag, String value) {
        Log.e(tag, value);
    }
}
