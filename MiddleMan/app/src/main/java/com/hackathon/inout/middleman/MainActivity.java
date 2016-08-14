package com.hackathon.inout.middleman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
    private static final String    TAG                 = "MiddleMan::MainActivity";

    private Button button_middleman;
    private Button button_messaging;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        button_middleman=(Button)findViewById(R.id.button_middleman);
        button_messaging=(Button)findViewById(R.id.button_messaging);

        button_middleman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, EmotionDetectionActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        button_messaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
