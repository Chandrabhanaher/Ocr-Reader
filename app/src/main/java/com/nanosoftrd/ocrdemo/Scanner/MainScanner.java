package com.nanosoftrd.ocrdemo.Scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.nanosoftrd.ocrdemo.LauncherActivity;
import com.nanosoftrd.ocrdemo.R;

import org.w3c.dom.Text;

public class MainScanner extends AppCompatActivity {
    TextView  textView;
    Intent intent ;
    ImageView imageView;
    public  static final int RequestPermissionCode  = 1 ;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scanner);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);

        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational()){
                    Toast.makeText(getApplicationContext(),"Could not get the text",Toast.LENGTH_SHORT).show();
                }else {
                    Frame frame  = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0; i<items.size(); i++){
                        TextBlock textBlock = items.valueAt(i);
                        stringBuilder.append(textBlock.getValue());
                        stringBuilder.append("\n");
                    }
                    textView.setText(stringBuilder.toString());

                }
            }
        });
       Button btn1 = (Button) findViewById(R.id.button1);
       EnableRuntimePermission();
       btn1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(intent, 7);
           }
       });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 7 && resultCode == RESULT_OK){
            bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainScanner.this, Manifest.permission.CAMERA))
        {

            Toast.makeText(MainScanner.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainScanner.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainScanner.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainScanner.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
