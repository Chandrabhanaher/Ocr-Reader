package com.nanosoftrd.ocrdemo.Scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nanosoftrd.ocrdemo.R;

public class MayDemo extends AppCompatActivity {
    TextView mTextView;
    String key;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_may_demo);

        mTextView = (TextView)findViewById(R.id.textView);
        btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
            }
        });
        Bundle b = getIntent().getExtras();


            key = b.getString("key");


          Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
