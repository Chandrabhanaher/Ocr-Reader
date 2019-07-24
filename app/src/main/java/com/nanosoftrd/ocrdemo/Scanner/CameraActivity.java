package com.nanosoftrd.ocrdemo.Scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.nanosoftrd.ocrdemo.LauncherActivity;
import com.nanosoftrd.ocrdemo.R;

public class CameraActivity extends AppCompatActivity {
    CameraSource mCameraSource;
    SurfaceView cameraView;
    StringBuilder sb = new StringBuilder();
    String textDetected = "No text Detected";
    final int RequestCameraPermissionId = 1001;
    boolean txtDtctd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView = (SurfaceView) findViewById(R.id.surfaceview);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.i("CameraActivity", "OnCreate : Detector Dependencies are not yet available ");
        } else {
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mCameraSource.start(cameraView.getHolder());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                        mCameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    Log.i("TAG","receiveDetections");
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() !=0){
                        txtDtctd = true;
                        for(int i=0; i<items.size(); i++){
                            TextBlock item = items.valueAt(i);
                            sb.append(item.getValue());
                            sb.append("\n");
                        }
                        textDetected = sb.toString();
                        if(txtDtctd){
                            Intent i  = new Intent(getApplicationContext(),MayDemo.class);
                            i.putExtra("key",textDetected);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(),textDetected,Toast.LENGTH_SHORT).show();
                        }

                    }
                    /*CameraActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(),textDetected,Toast.LENGTH_SHORT).show();
                        }
                    });*/

                }
            });
            Log.d("TAG","onCreate : one");
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        switch (RC) {

            case RequestCameraPermissionId:

                if (PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    try{
                        mCameraSource.start(cameraView.getHolder());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(CameraActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
