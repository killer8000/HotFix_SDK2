package hotfix.pingan.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;

import hotfix.pingan.com.mylibrary.ActivityTest;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.open)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityTest.class);
                startActivity(intent);
            }
        });
//        setContentView(ResManager.getInstance().getRid("hotfix.pingan.com.myapplication.R.layout.activity_main"));

    }
}
