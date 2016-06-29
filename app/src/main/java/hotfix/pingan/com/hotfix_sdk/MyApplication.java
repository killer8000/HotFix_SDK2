package hotfix.pingan.com.myapplication;
import android.app.Application;
import hotfix.pingan.com.mylibrary.ResManager;
import hotfix.pingan.com.mylibrary.hotfix.PAHot;


/**
 * Created by ndh on 16/5/20.
 */
public class MyApplication extends Application {
//    static class a=aa.class;
    @Override
    public void onCreate() {
        super.onCreate();
        PAHot.getInstance().init(this,"1.1.0");
        ResManager.getInstance().init(this, "");
    }

}
