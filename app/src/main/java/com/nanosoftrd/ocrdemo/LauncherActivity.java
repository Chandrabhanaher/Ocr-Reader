package com.nanosoftrd.ocrdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.nanosoftrd.ocrdemo.Scanner.CameraActivity;
import com.nanosoftrd.ocrdemo.Scanner.MainScanner;
import com.nanosoftrd.ocrdemo.Scanner.MayDemo;

public class LauncherActivity extends AppCompatActivity {

    ImageView img;
    Button btn,btn1;
    TextView ed;
    Intent intent ;
    public  static final int RequestPermissionCode  = 1 ;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        img = (ImageView)findViewById(R.id.imageView);
        btn  = (Button)findViewById(R.id.button);
        ed = (TextView)findViewById(R.id.editText);

        btn1 = (Button) findViewById(R.id.button1);
        EnableRuntimePermission();

        /*bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ic_ucglogo);
        img.setImageBitmap(bitmap);*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (textRecognizer.isOperational()){
                    Log.i("Text Recog","is operational");

                    Frame frame  = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    for(int i=0; i<items.size(); i++){
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }
                    ed.setText(stringBuilder.toString());
                    Log.i("converted",stringBuilder.toString());
                }else {
                    Log.i("Text Recog","is operational");
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
        Button btnNext1 = (Button)findViewById(R.id.button2);
        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainScanner.class));
            }
        });

        Button btnNext = (Button)findViewById(R.id.button3);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        Button btnMyDemo = (Button)findViewById(R.id.button4);
        btnMyDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CameraActivity.class));
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 7 && resultCode == RESULT_OK){
            bitmap = (Bitmap)data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
    }


    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this, Manifest.permission.CAMERA))
        {

            Toast.makeText(LauncherActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(LauncherActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(LauncherActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(LauncherActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }



}
