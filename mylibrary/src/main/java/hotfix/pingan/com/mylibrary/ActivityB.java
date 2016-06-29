package hotfix.pingan.com.mylibrary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


/**
 * Created by ndh on 16/5/9.
 */
public class ActivityB extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResManager.getInstance().inflate(this.getApplicationContext(), R.layout.content_main1, null));
        String appName = ResManager.getInstance().getResources().getString(R.string.app_name);
        Toast.makeText(this, appName, Toast.LENGTH_SHORT).show();
    }
}
