package hotfix.pingan.com.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by ndh on 16/5/8.
 */
public class ActivityTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResManager.getInstance().init(this, "");
        setContentView(ResManager.getInstance().inflate(this.getApplicationContext(),R.layout.activity_main1,null));
        (findViewById(R.id.bt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ActivityTest.this,"已经修复完bug",1).show();
//                int i =1/0;
                Intent intent =new Intent(ActivityTest.this,ActivityB.class);
                startActivity(intent);
            }
        });
    }
}
