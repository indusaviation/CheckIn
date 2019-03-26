package com.slatrack.gtms.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ListView;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.InputType;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.SettingsClient;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.adapter.AdapterCheckPointsList;
import com.slatrack.gtms.model.ModelEventData;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelCheckINData;
import com.slatrack.gtms.model.ModelCheckpoints;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassQrScan;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import android.text.method.PasswordTransformationMethod;


import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentScan extends Fragment {

    View rootView;

    ModelReader objReader;
    //  UsbDevice mDevice;
    public AppCompatTextView swipeCountText;
    public AppCompatTextView startscan;
    public EditText cardPlaced;
    ListView chkpointlistView;
    AppCompatTextView txtReaderStatus;
    AppCompatTextView svrUpdate;

    private ProgressBar progressBar;
    Toolbar topToolBar;
    public String checkInCode = "";
    public ClassDatabase database;
    public SLAGTMS globalVariable;


    private ImageView mImageView;
    private Bitmap mBitmap;
    private Context mContext;
    private Resources mResources;
    ActivityWelcome activityWelcome;

//    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
//    private long FASTEST_INTERVAL = 2000; /* 2 sec */
//
//    private int locationRequestCode = 1000;
//    private double wayLatitude = 0.0,
//            wayLongitude = 0.0;

    AdapterCheckPointsList adapterCheckPointsList;
    ArrayList<ModelCheckpoints> checkpointsArrayList = new ArrayList<>();

//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationRequest mLocationRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView  = inflater.inflate(R.layout.activity_scan,container,false);

        activityWelcome = (ActivityWelcome) getActivity();

        swipeCountText = (AppCompatTextView) rootView.findViewById(R.id.swipeCounts);
        cardPlaced = (EditText) rootView.findViewById(R.id.placedCard);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarScan);
        chkpointlistView = (ListView) rootView.findViewById(R.id.checkpointlist);
        svrUpdate = (AppCompatTextView) rootView.findViewById(R.id.submitswipes);
        startscan = (AppCompatTextView) rootView.findViewById(R.id.intiatescan);
        //mImageView = (ImageView) rootView.findViewById(R.id.devstatimg);

        mContext = activityWelcome.getApplicationContext();

        cardPlaced.setInputType(InputType.TYPE_NULL);
        cardPlaced.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        txtReaderStatus = (AppCompatTextView) rootView.findViewById(R.id.devicestatus);

        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        objReader = globalVariable.getUser();
        database = new ClassDatabase(activityWelcome);
        checkpointsArrayList = database.getAllCheckpoints();

        mResources = getResources();


        if(globalVariable.getIsReaderConnected().equalsIgnoreCase("YES"))
        {
            txtReaderStatus.setText(getResources().getString(R.string.rd_connected));
            txtReaderStatus.setTextColor(ContextCompat.getColor(activityWelcome,R.color.colorSwipeGreen));
            startscan.setText("SCAN");
            //  Toast.makeText(this, "09: ATTACHED Connected", Toast.LENGTH_SHORT).show();
        }
        else{
            txtReaderStatus.setText(getResources().getString(R.string.rd_disconnected));
            txtReaderStatus.setTextColor(ContextCompat.getColor(activityWelcome,R.color.colorMissedRed));
            startscan.setText("CONNECT");
            //  Toast.makeText(this, "10: DETTACHED Disconnected", Toast.LENGTH_SHORT).show();
        }

        if(checkpointsArrayList !=null){
            if(checkpointsArrayList.size() > 0){
                adapterCheckPointsList = new AdapterCheckPointsList(checkpointsArrayList,activityWelcome);
                chkpointlistView.setAdapter(adapterCheckPointsList);
                chkpointlistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
            else{

                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.noitem_text),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();
            }
        }
        else{
            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                    getResources().getString(R.string.nonetwork_text),getResources().getString(R.string.msg_title_information));
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            cdd.show();
        }

        svrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")){
                    UpdateTodoToServer();
                }
                else {
                    UpdateToServer();
                }
            }
        });


        startscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(globalVariable.getScanningMode().equalsIgnoreCase(ClassCommon.QRMODE)){

//                    if(isLocationEnabled()){
//
//                    }
//                    else{
//
//                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
//                                getResources().getString(R.string.gpsAccess),getResources().getString(R.string.msg_title_request));
//                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialogInterface) {
//                                ActivityCompat.requestPermissions(activityWelcome,
//                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                                        1000);
//                            }
//                        });
//                        cdd.show();
//                    }

                    if (isCameraAccessEnabled()) {
                        ClassQrScan scandd = new ClassQrScan((ActivityWelcome) getActivity());
                        scandd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        scandd.setEnc_key(globalVariable.getEnc_key());
                        scandd.setEnc_vi(globalVariable.getEnc_vi());
                        scandd.setDialogResult(new ClassQrScan.OnMyDialogResult(){
                            public void finish(String result){
                                if(!result.isEmpty()){
                                    cardPlaced.setText(result);
                                }
                            }
                        });

                        scandd.show();
                        //  startActivity(new Intent(activityWelcome, ActivityQR.class));
                    }else {
                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.cameraAccess),getResources().getString(R.string.msg_title_request));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                ActivityCompat.requestPermissions(activityWelcome,
                                        new String[]{Manifest.permission.CAMERA},
                                        101);
                            }
                        });
                        cdd.show();
                    }
                }
                else{
                    cardPlaced.setText("");
                    cardPlaced.requestFocus();


                    if( globalVariable.getIsReaderConnected().equalsIgnoreCase("YES")) {
                        startscan.setText("SCAN");
                        txtReaderStatus.setText(getResources().getString(R.string.rd_connected));
                    }
                    else{
                        startscan.setText("CONNECT");
                        txtReaderStatus.setText(getResources().getString(R.string.rd_disconnected));
                    }
                }


            }
        });

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
//        startLocationUpdates();


        progressBar.setVisibility(View.GONE);

        //ModelReader Operations



        cardPlaced.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String editTextVal = cardPlaced.getText().toString().trim();
                if(!editTextVal.isEmpty()) {
                    checkInCode = editTextVal;
                    adapterCheckPointsList.notifyDataSetChanged();
                }
            }
        });

        //Flash the Device Connecting Status..



        mBitmap = BitmapFactory.decodeResource(mResources,R.drawable.nfc_logo);
        //mImageView.setImageBitmap(mBitmap);
        RoundedBitmapDrawable roundedImageDrawable = createRoundedBitmapImageDrawableWithBorder(mBitmap);
      //  mImageView.setImageDrawable(roundedImageDrawable);

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
       // startscan.setAnimation(animation);
       // startscan.setText("CLICK HERE");
        progressBar.setVisibility(View.GONE);


//        cardPlaced.setText("0430670038");

        return rootView;
    }

//    // Trigger new location updates at interval
//    protected void startLocationUpdates() {
//
//        // Create the location request to start receiving updates
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//
//        // Create LocationSettingsRequest object using location request
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(mLocationRequest);
//        LocationSettingsRequest locationSettingsRequest = builder.build();
//
//        // Check whether location settings are satisfied
//        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
//        SettingsClient settingsClient = LocationServices.getSettingsClient(mContext);
//        settingsClient.checkLocationSettings(locationSettingsRequest);
//
//        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
//        fusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        // do work here
//                        onLocationChanged(locationResult.getLastLocation());
//                    }
//                },
//                Looper.myLooper());
//    }
//
//    public void onLocationChanged(Location location) {
//        // New location has now been determined
//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//    }

//    public void getLastLocation() {
//        // Get last known recent location using new Google Play Services SDK (v11+)
//
//
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // GPS location can be null if GPS is switched off
//                        if (location != null) {
//                            onLocationChanged(location);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
//                        e.printStackTrace();
//                    }
//                });
//    }


    private boolean isCameraAccessEnabled(){

        if (ContextCompat.checkSelfPermission(activityWelcome, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }else {
            return true;
        }

    }

//    private boolean isLocationEnabled(){
//
//        if (ContextCompat.checkSelfPermission(activityWelcome, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            return false;
//        }else {
//            return true;
//        }
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            getResources().getString(R.string.scanNow),getResources().getString(R.string.msg_title_information));
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            ActivityCompat.requestPermissions(activityWelcome,
                                    new String[]{Manifest.permission.CAMERA},
                                    101);
                        }
                    });
                    cdd.show();


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            getResources().getString(R.string.cameraAccess),getResources().getString(R.string.msg_title_information));
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            ActivityCompat.requestPermissions(activityWelcome,
                                    new String[]{Manifest.permission.CAMERA},
                                    101);
                        }
                    });
                    cdd.show();
                }
                return;
            }
//            case 1000: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//                } else {
//                    Toast.makeText(activityWelcome, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }



            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };


    private RoundedBitmapDrawable createRoundedBitmapImageDrawableWithBorder(Bitmap bitmap){
        int bitmapWidthImage = bitmap.getWidth();
        int bitmapHeightImage = bitmap.getHeight();
        int borderWidthHalfImage = 4;

        int bitmapRadiusImage = Math.min(bitmapWidthImage,bitmapHeightImage)/2;
        int bitmapSquareWidthImage = Math.min(bitmapWidthImage,bitmapHeightImage);
        int newBitmapSquareWidthImage = bitmapSquareWidthImage+borderWidthHalfImage;

        Bitmap roundedImageBitmap = Bitmap.createBitmap(newBitmapSquareWidthImage,newBitmapSquareWidthImage,Bitmap.Config.ARGB_8888);
        Canvas mcanvas = new Canvas(roundedImageBitmap);
        mcanvas.drawColor(Color.RED);
        int i = borderWidthHalfImage + bitmapSquareWidthImage - bitmapWidthImage;
        int j = borderWidthHalfImage + bitmapSquareWidthImage - bitmapHeightImage;

        mcanvas.drawBitmap(bitmap, i, j, null);

        Paint borderImagePaint = new Paint();
        borderImagePaint.setStyle(Paint.Style.STROKE);
        borderImagePaint.setStrokeWidth(borderWidthHalfImage*2);
        borderImagePaint.setColor(Color.GRAY);
        mcanvas.drawCircle(mcanvas.getWidth()/2, mcanvas.getWidth()/2, newBitmapSquareWidthImage/2, borderImagePaint);

        RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources,roundedImageBitmap);
        roundedImageBitmapDrawable.setCornerRadius(bitmapRadiusImage);
        roundedImageBitmapDrawable.setAntiAlias(true);
        return roundedImageBitmapDrawable;
    }



    public void UpdateCheckpointData(JSONObject resultObj)
    {
        try {

            //Loop it through all the checkpoints //TODO

            String checkpointcode = resultObj.getString("checkpointcode");
            String checkpointname = resultObj.getString("checkpointname");
            String user_id = "0"; //TODO - please fill the user_id later - ideally checkpoints will be set for Society Only
            String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelCheckpoints checkpointsUpdate = new ModelCheckpoints();
            checkpointsUpdate.setCheckpointcode(checkpointcode);
            checkpointsUpdate.setCheckpointname(checkpointname);
            checkpointsUpdate.setUser_id(user_id);
            checkpointsUpdate.setSociety_id(society_id);
            checkpointsUpdate.setOrganizationId(organization_id);
            checkpointsUpdate.setFacilityId(facility_id);

            database.insertCheckpoint(checkpointsUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    private void UpdateToServer(){

        ArrayList<ModelCheckINData> punchRecords = new ArrayList<ModelCheckINData>();

        punchRecords = database.getCheckInDataList(globalVariable.getReaderCode());
        String dataUpload = "data=";

        if(punchRecords.size() > 0) {
            for(int objCount=0; objCount< punchRecords.size(); objCount++){
                ModelCheckINData chkRecord = punchRecords.get(objCount);
                if(chkRecord!= null) {
                    dataUpload += chkRecord.getReaderID()+","+chkRecord.getCheckpoint()+","+chkRecord.getSwipeDate()+","+chkRecord.getSwipeTime()+",I;";
                }
            }

            String serverURL = ClassCommon.BASE_URL+ClassCommon.SET_SUBMITPUNCHES+dataUpload;
            if (ClassConnectivity.isConnected(activityWelcome)) {
                if(!dataUpload.equalsIgnoreCase("data=")) {
                    UploadPunchRecords(serverURL);
                }
            }
        }

    }

    private void UpdateTodoToServer(){

       ArrayList<ModelEventData> punchRecords = new ArrayList<ModelEventData>();

        punchRecords = database.getEventDataList(globalVariable.getReaderCode());
        String dataUpload = "data=";

        if(punchRecords.size() > 0) {
            for(int objCount=0; objCount< punchRecords.size(); objCount++){
                ModelEventData chkRecord = punchRecords.get(objCount);
                if(chkRecord!= null) {
                    dataUpload += chkRecord.getReaderID()+","+chkRecord.getCheckpoint()+","+chkRecord.getEventcode()+","+chkRecord.getSwipeDate()+","+chkRecord.getSwipeTime()+","+chkRecord.getGpsLat()+","+chkRecord.getGpsLong()+";";
                }
            }

            String serverURL = ClassCommon.BASE_URL+ClassCommon.SET_TODOPUNCHES+dataUpload;
            if (ClassConnectivity.isConnected(activityWelcome)) {
                if(!dataUpload.equalsIgnoreCase("data=")) {
                    UploadPunchRecords(serverURL);
                }
            }
        }

    }


    private void UploadPunchRecords(String checkInURL) {

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(checkInURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")){

                        String strResponseCode = responseObj.getString("STATUS");

                        if (strResponseCode.equals(ClassCommon.SUCCESS)) {
                            Toast.makeText(activityWelcome, getResources().getString(R.string.checkinSuccess), Toast.LENGTH_SHORT).show();
                            if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")) {
                                database.deleteAllEventData(globalVariable.getReaderCode());
                            }
                            else {
                                database.deleteAllCheckIn(globalVariable.getReaderCode());
                            }
                            Intent intent = new Intent(activityWelcome, ActivityWelcome.class);
                            startActivity(intent);

                        }
                    }
                    else{
                        String strResponseCode = responseObj.getString("STATUS");
                        if (strResponseCode.equals(ClassCommon.INVALIDCHECKPOINT)) {
                            Toast.makeText(activityWelcome, getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();

                        }
                        else if(strResponseCode.equals(ClassCommon.SUBSCRIPTIONEXPIRED)) {

                            ClassCustomDialog cdd = new ClassCustomDialog((ActivityWelcome) getActivity());
                            cdd.setDigtext(getResources().getString(R.string.renew_subscription));
                            cdd.setDigtype(getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();
//
//                            String msgString = String.format(getResources().getString(R.string.renew_subscription), globalVariable.getSupportphone());
//                            Toast.makeText(getActivity(), msgString, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(activityWelcome, getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activityWelcome, getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activityWelcome, getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();

            }
        });

    }



}
