package activity.ui.app.com.bluetooths;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/11/24.
 */

public class BaseApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
