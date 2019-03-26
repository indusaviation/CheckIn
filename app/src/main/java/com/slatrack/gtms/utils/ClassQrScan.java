package com.slatrack.gtms.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.support.v7.widget.AppCompatTextView;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityWelcome;

import org.json.JSONObject;

import java.io.IOException;


public class ClassQrScan extends Dialog {

    ActivityWelcome activity;

    private AppCompatTextView msgTitleText;
    private AppCompatButton btnOK;
    private String okaction = "NOACTION";
    private String qrcode = "";


    public ClassQrScan(Activity a) {
        super(a);
        activity = (ActivityWelcome) a;
    }


    public ClassQrScan(Activity a, String strText, String diagType) {
        super(a);
        // TODO Auto-generated constructor stub


    }

    public void ClassQrScan() {

    }

    SurfaceView cameraView;
    View scanner;
    LinearLayout wrongQRLayout, btnClose;
    RelativeLayout scanLayout;
    AppCompatTextView QRMsg;


    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private Animation mAnimation;
    private boolean isBarcodeDetected = false;
    private String enc_vi = "fedcba9876543210";
    private String enc_key = "0123456789abcdef";

    OnMyDialogResult mDialogResult; // the callback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        scanner = (View) findViewById(R.id.line);
        scanLayout = (RelativeLayout) findViewById(R.id.scanLayout);
        QRMsg = (AppCompatTextView) findViewById(R.id.QRMsg);
        wrongQRLayout = (LinearLayout) findViewById(R.id.wrongQRLayout);
        btnClose = (LinearLayout) findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             dismiss();
            }
        });

        scanner.setVisibility(View.VISIBLE);
        startScanner();


    }

    //Get the QR code back
    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public String getEnc_vi() {
        return enc_vi;
    }

    public void setEnc_vi(String enc_vi) {
        this.enc_vi = enc_vi;
    }

    public String getEnc_key() {
        return enc_key;
    }

    public void setEnc_key(String enc_key) {
        this.enc_key = enc_key;
    }

    public interface OnMyDialogResult{
        void finish(String result);
    }

    private void startScanner() {

        mAnimation = null;
        barcodeDetector = null;
        cameraSource = null;
        cameraView = null;
        barcodeDetector = null;

        scanLayout.setVisibility(View.VISIBLE);

        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        cameraView.setVisibility(View.VISIBLE);

        mAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
                0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
        mAnimation.setDuration(3000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        scanner.setAnimation(mAnimation);


        barcodeDetector = new BarcodeDetector.Builder(activity)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(activity, barcodeDetector)
                .setRequestedPreviewSize(1280, 1280)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> values = detections.getDetectedItems();

                if (values.size() >= 1) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!isBarcodeDetected) {

                                    final String message = values.valueAt(0).displayValue;
//                                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                                    isBarcodeDetected = true;
                                    scanner.setAnimation(null);
                                    scanner.setVisibility(View.GONE);
                                    cameraView.setVisibility(View.GONE);
                                    scanLayout.setVisibility(View.GONE);

                                 //   if(TextUtils.isDigitsOnly(message)) { TODO Allow any free text
                                        scanLayout.setVisibility(View.GONE);
                                        wrongQRLayout.setVisibility(View.GONE);

                                        qrcode = message;

                                        IndusEncrypt indusencrypt = new IndusEncrypt(enc_vi,enc_key);
                                        String decryptedqr = new String(indusencrypt.decrypt(qrcode ) );

                                        if( mDialogResult != null ){
                                            mDialogResult.finish(String.valueOf(decryptedqr));
                                        }

                                     //  QRMsg.setText(decryptedqr);

                                      ClassQrScan.this.dismiss();

//                                    }else {
//                                        scanLayout.setVisibility(View.GONE);
//                                        wrongQRLayout.setVisibility(View.VISIBLE);
//                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                scanLayout.setVisibility(View.GONE);
                                wrongQRLayout.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }

            }
        });
    }

}