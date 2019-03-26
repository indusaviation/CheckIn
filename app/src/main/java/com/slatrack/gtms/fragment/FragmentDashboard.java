package com.slatrack.gtms.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityRegistration;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.model.ModelCheckINData;
import com.slatrack.gtms.model.ModelCheckpoints;
import com.slatrack.gtms.model.ModelEventData;
import com.slatrack.gtms.model.ModelEvents;
import com.slatrack.gtms.model.ModelFeatures;
import com.slatrack.gtms.model.ModelLastupdate;
import com.slatrack.gtms.model.ModelOrganization;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.model.ModelSchedule;
import com.slatrack.gtms.model.ModelShift;
import com.slatrack.gtms.model.ModelSlaParam;
import com.slatrack.gtms.model.ModelSosMessage;
import com.slatrack.gtms.model.ModelSubscription;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.utils.ClassRdStatusObservable;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import java.util.Observable;
import java.util.Observer;

public class FragmentDashboard extends Fragment {

    View rootView;

    private ProgressBar progressBar;
    private AppCompatTextView txtRecCount;
    private AppCompatTextView txtTotalSwipeCount;
    private AppCompatTextView txtTotalMissedCount;
    private AppCompatTextView txtTotalPercentageCount;
    private AppCompatTextView txtTotalExpectedCount;
    private AppCompatTextView txtShowRdStatus;

    private AppCompatTextView totalSwipeText;
    private AppCompatTextView totalMissedText;
    private AppCompatTextView totalExpectedText;
    private AppCompatTextView percentComplianceText;



    public static final String PARAM = "param";

    //    private ImageView imgOrgLogo;
    private boolean isOneAttempt;


    private AppCompatTextView orgName, userName, subDate, subType, orgStatus, subStatus, noHistoryText;

    private int recordCount = 0;

    ActivityWelcome activityWelcome;

    ArrayList<ModelCheckINData> checkINDataList;
    ArrayList<ModelEventData> eventDataList;

    public Resources mResources;
    public ClassDatabase database;
    public SLAGTMS globalVariable;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);



        activityWelcome = (ActivityWelcome) getActivity();
        isOneAttempt = false;


        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = activityWelcome.database;

        mResources = getResources();

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarWelcome);
        txtRecCount = (AppCompatTextView) rootView.findViewById(R.id.recCount);

        orgName = (AppCompatTextView) rootView.findViewById(R.id.txnorgnamewel);
        userName = (AppCompatTextView) rootView.findViewById(R.id.txnusernamewel);
        subDate = (AppCompatTextView) rootView.findViewById(R.id.txnsubdatewel);
        subType = (AppCompatTextView) rootView.findViewById(R.id.txnsubtypewel);
        orgStatus = (AppCompatTextView) rootView.findViewById(R.id.txnorgstatuswel);
        subStatus = (AppCompatTextView) rootView.findViewById(R.id.txnsubstatuswel);
        txtShowRdStatus = (AppCompatTextView) rootView.findViewById(R.id.showrdstatus);

        txtTotalExpectedCount = (AppCompatTextView) rootView.findViewById(R.id.totalExpected);
        txtTotalMissedCount = (AppCompatTextView) rootView.findViewById(R.id.totalMissed);
        txtTotalSwipeCount = (AppCompatTextView) rootView.findViewById(R.id.totalSwipe);
        txtTotalPercentageCount = (AppCompatTextView) rootView.findViewById(R.id.percentCompliance);

        totalSwipeText = (AppCompatTextView) rootView.findViewById(R.id.totalSwipeText);
        totalMissedText = (AppCompatTextView) rootView.findViewById(R.id.totalMissedText);
        totalExpectedText = (AppCompatTextView) rootView.findViewById(R.id.totalExpectedText);
        percentComplianceText = (AppCompatTextView) rootView.findViewById(R.id.percentComplianceText);

        if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")){
            totalSwipeText.setText("Completed Tasks");
            totalMissedText.setText("Missed Tasks");
            totalExpectedText.setText("Expected Tasks");
        }

        final AppCompatButton submitbutton = rootView.findViewById(R.id.upload);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(recordCount == 0) {
                    Toast.makeText(getActivity(), "No Record to update", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")){
                        UploadToDoPunchesRecordsToServer();
                    }
                    else {

                        UploadPunchRecordsToServer();
                    }
                }
            }
        });

        RefreshControls();

        progressBar.setVisibility(View.GONE);


        ((ActivityWelcome)getActivity()).setFragmentRefreshListener(new ActivityWelcome.FragmentRefreshListener() {
            @Override
            public void onRefresh() {

                if(globalVariable.getScanningMode().equalsIgnoreCase(ClassCommon.QRMODE)){
                    txtShowRdStatus.setText(getResources().getString(R.string.rd_connected));
                    txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_green));
                }
                else {

                    if (globalVariable.getIsReaderConnected().equalsIgnoreCase("YES")) {
                        txtShowRdStatus.setText(getResources().getString(R.string.rd_connected));
                        txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_green));
                    } else {
                        txtShowRdStatus.setText(getResources().getString(R.string.rd_disconnected));
                        txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_red));
                    }
                }
            }
        });

        GetSummaryData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(globalVariable.getScanningMode().equalsIgnoreCase(ClassCommon.QRMODE)){
            txtShowRdStatus.setText(getResources().getString(R.string.rd_connected));
            txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_green));
        }
        else {
            if (globalVariable.getIsReaderConnected().equalsIgnoreCase("YES")) {
                txtShowRdStatus.setText(getResources().getString(R.string.rd_connected));
                txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_green));
            } else {
                txtShowRdStatus.setText(getResources().getString(R.string.rd_disconnected));
                txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_red));
            }
        }

    }


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
        borderImagePaint.setColor(Color.BLUE);
        mcanvas.drawCircle(mcanvas.getWidth()/2, mcanvas.getWidth()/2, newBitmapSquareWidthImage/2, borderImagePaint);

        RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources,roundedImageBitmap);
        roundedImageBitmapDrawable.setCornerRadius(bitmapRadiusImage);
        roundedImageBitmapDrawable.setAntiAlias(true);
        return roundedImageBitmapDrawable;
    }


    private void GetSummaryData(){

        progressBar.setVisibility(View.VISIBLE);
        String data = globalVariable.getImei();
        AsyncHttpClient client = new AsyncHttpClient();

        String configURL = "";
        if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")){
            configURL = ClassCommon.BASE_URL+ClassCommon.GET_TODOSUMMARY+"data="+data;
        }
        else{
            configURL = ClassCommon.BASE_URL+ClassCommon.GET_PUNCHSUMMARY+"data="+data;
        }


        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);
                    if (!responseObj.getBoolean("ERROR")){
                        JSONObject resultObj = responseObj.getJSONObject("RESULT");


                        String totalSwipeCount = resultObj.getString("TotalSwipe");
                        String totalExpectedCount = resultObj.getString("TotalExpected");
                        String totalMissedCount = resultObj.getString("TotalMissed");
                        String totalSwipePercentage = resultObj.getString("SwipePercentage");

                        globalVariable.setTotalExpectedCount(totalExpectedCount);
                        globalVariable.setTotalMissedCount(totalMissedCount);
                        globalVariable.setTotalSwipeCount(totalSwipeCount);
                        globalVariable.setSwipePercentage(totalSwipePercentage);

                    }else {

                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();
                    }

                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception",""+e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);
                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }


            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

                UpdateSummaryData();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void UpdateSummaryData(){

        if(!globalVariable.getTotalExpectedCount().isEmpty()) {
            txtTotalExpectedCount.setText(globalVariable.getTotalExpectedCount());
        }
        else{
            txtTotalExpectedCount.setText("0");
        }

        if(!globalVariable.getTotalMissedCount().isEmpty()) {
            txtTotalMissedCount.setText(globalVariable.getTotalMissedCount() );
        }
        else{
            txtTotalMissedCount.setText("0" );
        }

        if(!globalVariable.getTotalSwipeCount().isEmpty()) {
            txtTotalSwipeCount.setText(globalVariable.getTotalSwipeCount());
        }
        else{
            txtTotalSwipeCount.setText("0");
        }

        if(!globalVariable.getSwipePercentage().isEmpty()) {
            txtTotalPercentageCount.setText(globalVariable.getSwipePercentage());
        }
        else{
            txtTotalPercentageCount.setText("0");
        }
    }

    private void UploadToDoPunchesRecordsToServer(){
        ArrayList<ModelEventData> punchRecords = new ArrayList<>();

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
            if (ClassConnectivity.isConnected(getActivity())) {
                if(!dataUpload.equalsIgnoreCase("data=")) {
                    UploadPunchRecords(serverURL);
                }
            }
        }
    }




    private void UploadPunchRecordsToServer(){
        ArrayList<ModelCheckINData> punchRecords = new ArrayList<>();

        punchRecords = database.getCheckInDataList(globalVariable.getReaderCode());
        String dataUpload = "data=";

        if(punchRecords.size() > 0) {
            for(int objCount=0; objCount< punchRecords.size(); objCount++){
                ModelCheckINData chkRecord = punchRecords.get(objCount);
                if(chkRecord!= null) {
                 //   http://slatrack.com/webservice2.0/settodopunches.php?data=3039,AC1COACH,23/03/2019,10:00:00,13.00000,14.0000
                    dataUpload += chkRecord.getReaderID()+","+chkRecord.getCheckpoint()+","+chkRecord.getSwipeDate()+","+chkRecord.getSwipeTime()+",";
                }
            }
            String serverURL = ClassCommon.BASE_URL+ClassCommon.SET_SUBMITPUNCHES+dataUpload;
            if (ClassConnectivity.isConnected(getActivity())) {
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
                            Toast.makeText(getActivity(), getResources().getString(R.string.checkinSuccess), Toast.LENGTH_SHORT).show();

                            if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")) {
                                database.deleteAllEventData(globalVariable.getReaderCode());
                            }
                            else{
                                database.deleteAllCheckIn(globalVariable.getReaderCode());
                            }


                        }
                    }
                    else{
                        String strResponseCode = responseObj.getString("STATUS");
                        if (strResponseCode.equals(ClassCommon.INVALIDCHECKPOINT)) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();
                        }
                        else if(strResponseCode.equals(ClassCommon.SUBSCRIPTIONEXPIRED)) {
                            ClassCustomDialog cdd = new ClassCustomDialog((ActivityWelcome) getActivity());
                            cdd.setDigtext(getResources().getString(R.string.renew_subscription));
                            cdd.setDigtype(getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();
                        }
                        else {
                            RefreshControls();
                            // Toast.makeText(getActivity(), getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

//                database = new ClassDatabase(getActivity());
                if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")) {

                    eventDataList = database.getEventDataList(globalVariable.getReaderCode());
                    if (eventDataList != null) {
                        recordCount = eventDataList.size();
                    } else {
                        recordCount = 0;
                    }

                }
                else {
                    checkINDataList = database.getCheckInDataList(globalVariable.getReaderCode());
                    if (checkINDataList != null) {
                        recordCount = checkINDataList.size();
                    } else {
                        recordCount = 0;
                    }
                }
                String formatted = String.format("%04d", recordCount);
                txtRecCount.setText(formatted);

                progressBar.setVisibility(View.GONE);
            }

        });

    }

    private void RefreshControls() {

//        database = new ClassDatabase(getActivity());

        if(database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")) {

            eventDataList = database.getEventDataList(globalVariable.getReaderCode());

            if (eventDataList != null) {
                recordCount = eventDataList.size();
            } else {
                recordCount = 0;
            }

        }
        else {
            checkINDataList = database.getCheckInDataList(globalVariable.getReaderCode());

            if (checkINDataList != null) {
                recordCount = checkINDataList.size();
            } else {
                recordCount = 0;
            }
        }

        String formatted = String.format("%04d", recordCount);
        txtRecCount.setText(formatted);

        ModelReader userDetails = database.getUser();
        ModelOrganization orgDetails = database.getOrganization();
        if (userDetails != null && orgDetails != null) {
            if (database.userCount() > 0 && database.organizationCount() > 0) {
                orgName.setText(orgDetails.getOrgname().trim());
                userName.setText(userDetails.getFirstname() + " " + userDetails.getLastname().trim());
                subDate.setText(orgDetails.getExpiry_date());
                subType.setText(orgDetails.getSubtype().trim());

                if (orgDetails.getSubstatus().trim().equalsIgnoreCase("1")) {
                    subStatus.setText("ACTIVE");
                } else {
                    subStatus.setText("INACTIVE");
                }

                if (orgDetails.getOrgstatus().trim().equalsIgnoreCase("1")) {
                    orgStatus.setText("ACTIVE");
                } else {
                    orgStatus.setText("INACTIVE");
                }
            } else {

            }
        } else {
            //TODO Refresh Database
        }

//        File file = getActivity().getApplicationContext().getFileStreamPath(ClassCommon.ORGLOGO_NAME);
//
//        if (file.exists()){
//
//            mBitmap = loadImageBitmap(getApplicationContext(), ClassCommon.ORGLOGO_NAME);
//            if(mBitmap == null) {
//                mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nfc_logo);
//            }
//            imgOrgLogo.setImageBitmap(mBitmap);
//            RoundedBitmapDrawable roundedImageDrawable = createRoundedBitmapImageDrawableWithBorder(mBitmap);
//            imgOrgLogo.setImageDrawable(roundedImageDrawable);
//
//
//        }
//        else{
//            mBitmap = BitmapFactory.decodeResource(mResources,R.drawable.nfc_logo);
//            imgOrgLogo.setImageBitmap(mBitmap);
//            RoundedBitmapDrawable roundedImageDrawable = createRoundedBitmapImageDrawableWithBorder(mBitmap);
//            imgOrgLogo.setImageDrawable(roundedImageDrawable);
//        }

        // new DownloadImage(imgOrgLogo).execute(globalVariableOld.getLogoPath());

        if (globalVariable.getSwipePercentage().equalsIgnoreCase("NA")) {
            GetSummaryData();
        } else {
            UpdateSummaryData();
        }

        if (globalVariable.getScanningMode().equalsIgnoreCase(ClassCommon.QRMODE)) {
            txtShowRdStatus.setText(getResources().getString(R.string.rd_connected));
            txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_green));
        } else {


        if (globalVariable.getIsReaderConnected().equalsIgnoreCase("YES")) {
            txtShowRdStatus.setText(getResources().getString(R.string.rd_connected));
            txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_green));

        } else {
            txtShowRdStatus.setText(getResources().getString(R.string.rd_disconnected));
            txtShowRdStatus.setBackgroundDrawable(ContextCompat.getDrawable(activityWelcome, R.drawable.rdstatus_back_red));
        }
        }

    }


    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }


    public void UpdateSubScriptionRates(JSONObject resultObj) {
//////        ClassDatabase database = new ClassDatabase(getActivity());
        try {

            String subid = resultObj.getString("subid");
            String subname = resultObj.getString("subname");
            String subrates = resultObj.getString("subrates");
            String subperiod = resultObj.getString("subperiod");
            String substatus = resultObj.getString("substatus");
            String suboffertext = resultObj.getString("suboffertext");
            String subamount = resultObj.getString("subamount");

            ModelSubscription subRatesUpdate = new ModelSubscription();

            subRatesUpdate.setSubid(subid);
            subRatesUpdate.setSubname(subname);
            subRatesUpdate.setSubrates(subrates);
            subRatesUpdate.setSubperiod(subperiod);
            subRatesUpdate.setSubstatus(substatus);
            subRatesUpdate.setSuboffertext(suboffertext);
            subRatesUpdate.setSubamount(subamount);

            database.insertSubscriptionRate(subRatesUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    public void CheckIfUpdateNeeded(String society_id){

        String data = globalVariable.getImei();
        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_LASTUPDATE+"data="+data;

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")){

                        //Parse Last Update

                        JSONArray resultArr = responseObj.getJSONArray("LASTUPDATE");

                        if(resultArr.length() > 0) {

                            JSONObject resultObj = resultArr.getJSONObject(0);
                            int NeedToUpdate = ValidateLastUpdate(resultObj);
                            if(NeedToUpdate > 0)
                            {
                                globalVariable.setUpdateNeeded("YES");
                            }
                            else{
                                globalVariable.setUpdateNeeded("YES");
                            }

                        }else {

                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }

                    }
                    else {

                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();

                    }
                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception",""+e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);

                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }


    public void DownloadOrganizationData(String strImei){

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_ALLDATA+"data="+strImei;

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")){

                        JSONArray resultArr = responseObj.getJSONArray("LASTUPDATE");

                        if(resultArr.length() > 0) {

                            JSONObject resultObj = resultArr.getJSONObject(0);
                            int NeedToUpdate = ValidateLastUpdate(resultObj);
                            if(NeedToUpdate > 0)
                            {
                                globalVariable.setUpdateNeeded("YES");
                                JSONArray featuresArr = responseObj.getJSONArray("FEATURES");
                                if(featuresArr.length() > 0){

                                    database.deleteAllFeatures();
                                    for(int objCount=0; objCount< featuresArr.length(); objCount++){
                                        JSONObject featureObj = featuresArr.getJSONObject(objCount);
                                        if(featureObj!= null) {
                                            UpdateFeaturesData(featureObj);
                                        }
                                    }

                                }

                                JSONArray checkPointsArr = responseObj.getJSONArray("CHECKPOINTS");
                                if(checkPointsArr.length() > 0){
                                    database.deleteAllCheckpoints();
                                    for(int objCount=0; objCount< checkPointsArr.length(); objCount++){
                                        JSONObject checkpointObj = checkPointsArr.getJSONObject(objCount);
                                        if(checkpointObj!= null) {
                                            UpdateCheckpointData(checkpointObj);
                                        }
                                    }
                                }

                                JSONArray eventsArr = responseObj.getJSONArray("EVENTS");
                                if(eventsArr.length() > 0){
                                    database.deleteAllEvents();
                                    for(int objCount=0; objCount< eventsArr.length(); objCount++){
                                        JSONObject eventsObj = eventsArr.getJSONObject(objCount);
                                        if(eventsObj!= null) {
                                            UpdateEventsData(eventsObj);
                                        }
                                    }

                                }

                                JSONArray shiftsArr = responseObj.getJSONArray("SHIFTS");
                                if(shiftsArr.length() > 0){
                                    database.deleteAllShift();
                                    for(int objCount=0; objCount< shiftsArr.length(); objCount++){
                                        JSONObject shiftsObj = shiftsArr.getJSONObject(objCount);
                                        if(shiftsObj!= null) {
                                            UpdateShiftData(shiftsObj);
                                        }
                                    }

                                }

                                JSONArray scheduleArr = responseObj.getJSONArray("SCHEDULES");
                                if(scheduleArr.length() > 0){
                                    database.deleteAllSchedule();
                                    for(int objCount=0; objCount< scheduleArr.length(); objCount++){
                                        JSONObject scheduleObj = scheduleArr.getJSONObject(objCount);
                                        if(scheduleObj!= null) {
                                            UpdateScheduleData(scheduleObj);
                                        }
                                    }

                                }

                                JSONArray sosMsgArr = responseObj.getJSONArray("SOSMESSAGES");
                                if(sosMsgArr.length() > 0){
                                    database.deleteAllSOSMessage();
                                    for(int objCount=0; objCount< sosMsgArr.length(); objCount++){
                                        JSONObject sosMsgObj = sosMsgArr.getJSONObject(objCount);
                                        if(sosMsgObj!= null) {
                                            UpdateSosMessageData(sosMsgObj);
                                        }
                                    }

                                }

                                JSONArray slaParamArr = responseObj.getJSONArray("SLAPARAM");
                                if(slaParamArr.length() > 0){
                                    database.deleteAllSlaParam();
                                    for(int objCount=0; objCount< slaParamArr.length(); objCount++){
                                        JSONObject slaParamObj = slaParamArr.getJSONObject(objCount);
                                        if(slaParamObj!= null) {
                                            UpdateSlaParam(slaParamObj);
                                        }
                                    }

                                }


                                JSONArray subscriptionArr = responseObj.getJSONArray("SUBSCRIPTION");
                                if(subscriptionArr.length() > 0){
                                    database.deleteAllSubscriptionRates();
                                    for(int objCount=0; objCount< subscriptionArr.length(); objCount++){
                                        JSONObject subratesObj = subscriptionArr.getJSONObject(objCount);
                                        if(subratesObj!= null) {
                                            UpdateSubScriptionRates(subratesObj);
                                        }
                                    }
                                }

                            }
                            else{
                                globalVariable.setUpdateNeeded("NO");
                            }

                        }else {

                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }

                    }
                    else {
                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();

                    }


                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception",""+e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);
                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }

    public void UnMapUser(String imei){

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL+ClassCommon.SET_UNMAPUSER+"data="+imei;
        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")){

                    }
                    else {

                    }


                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception",""+e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);
                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }

    public void UpdateSosMessageData(JSONObject sosMsgObject){

////        ClassDatabase database = new ClassDatabase(getActivity());
        try {

            String _id = sosMsgObject.getString("id");
            String _societyid = sosMsgObject.getString("society_id");
            String _msgconst = sosMsgObject.getString("msgconst");
            String _msgdetails = sosMsgObject.getString("msgdetails");


            ModelSosMessage sosMessageList = new ModelSosMessage();
            sosMessageList.setId(_id);
            sosMessageList.setSociety_id(_societyid);
            sosMessageList.setMsgconst(_msgconst);
            sosMessageList.setMsgdetails(_msgdetails);

            database.insertSOSMessage(sosMessageList);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }
    }


    public void UpdateSlaParam(JSONObject slaParamObject){

        try {

            String _id = slaParamObject.getString("id");
            String _encvi = slaParamObject.getString("enc_vi");
            String _enckey = slaParamObject.getString("enc_key");
            String _slapercent = slaParamObject.getString("sla_percent");


            ModelSlaParam slaParam = new ModelSlaParam();
            slaParam.setId(_id);
            slaParam.setEnc_vi(_encvi);
            slaParam.setEnc_key(_enckey);
            slaParam.setSla_percent(_slapercent);

            database.insertSlaParam(slaParam);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }
    }

    public void UpdateFeaturesData(JSONObject resultObj)
    {
////        ClassDatabase database = new ClassDatabase(getActivity());
        try {

            String enable_camera = resultObj.getString("enable_camera");
            String enable_gps = resultObj.getString("enable_gps");
            String enable_sig = resultObj.getString("enable_sig");
            String enable_selfie = resultObj.getString("enable_selfie");
            String enable_overide = resultObj.getString("enable_override");
            String scan_mode = resultObj.getString("scan_mode");
            String user_id = "0"; //TODO in future, please add the user_id mapping
            String society_id = resultObj.getString("society_id");


            ModelFeatures featuresUpdate = new ModelFeatures();
            featuresUpdate.setEnable_camera(enable_camera);
            featuresUpdate.setEnable_gps(enable_gps);
            featuresUpdate.setEnable_sig(enable_sig);
            featuresUpdate.setEnable_selfie(enable_selfie);
            featuresUpdate.setEnable_overide(enable_overide);
            featuresUpdate.setScan_mode(scan_mode);
            featuresUpdate.setUser_id(user_id);
            featuresUpdate.setSociety_id(society_id);


            database.insertFeatures(featuresUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    public void UpdateCheckpointData(JSONObject resultObj)
    {


////        ClassDatabase database = new ClassDatabase(getActivity());
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

            // database.insertCheckpoint(checkpointsUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }
    public void UpdateShiftData(JSONObject resultObj)
    {


////        ClassDatabase database = new ClassDatabase(getActivity());
        try {
            //Loop it through all the events //TODO


            String slashift_id = resultObj.getString("id");
            String shiftname = resultObj.getString("shiftname");
            String shiftfrom = resultObj.getString("shift_time_from");
            String shiftto = resultObj.getString("shift_time_to");
            String user_id = "0"; //TODO - in future -ModelEvents shall be for each user
            String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelShift shiftUpdate = new ModelShift();

            shiftUpdate.setSlashift_id(slashift_id);
            shiftUpdate.setShiftname(shiftname);
            shiftUpdate.setShiftfrom(shiftfrom);
            shiftUpdate.setShiftto(shiftto);
            shiftUpdate.setUser_id(user_id);
            shiftUpdate.setSociety_id(society_id);
            shiftUpdate.setOrganizationId(organization_id);
            shiftUpdate.setFacilityId(facility_id);

            database.insertShift(shiftUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    public void UpdateScheduleData(JSONObject resultObj)
    {


////        ClassDatabase database = new ClassDatabase(getActivity());
        try {
            //Loop it through all the events //TODO

            String slaschedule_id = resultObj.getString("id");
            String slashift_id = resultObj.getString("shift_id");
            String shiftname = resultObj.getString("shift_id"); //TODO Provide the Join on ModelShift Table
            String schedulefrom = resultObj.getString("schedule_from");
            String scheduleto = resultObj.getString("schedule_to");
            String user_id = "0"; //TODO - in future -ModelEvents shall be for each user
            String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelSchedule scheduleUpdate = new ModelSchedule();
            scheduleUpdate.setSlaschedule_id(slaschedule_id);
            scheduleUpdate.setSlashift_id(slashift_id);
            scheduleUpdate.setShiftname(shiftname);
            scheduleUpdate.setSchedulefrom(schedulefrom);
            scheduleUpdate.setScheduleto(scheduleto);
            scheduleUpdate.setUser_id(user_id);
            scheduleUpdate.setSociety_id(society_id);
            scheduleUpdate.setOrganizationId(organization_id);
            scheduleUpdate.setFacilityId(facility_id);

            database.insertSchedule(scheduleUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    public void UpdateEventsData(JSONObject resultObj)
    {


////        ClassDatabase database = new ClassDatabase(getActivity());
        try {
            //Loop it through all the events //TODO

            String eventcode = resultObj.getString("eventcode");
            String eventname = resultObj.getString("eventname");
            String eventdescription = resultObj.getString("description");
            String eventperiod = resultObj.getString("period");
            String eventfrequency = resultObj.getString("frequency");
            String user_id = "0"; //TODO - in future -ModelEvents shall be for each user
            String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelEvents eventsUpdate = new ModelEvents();
            eventsUpdate.setEventcode(eventcode);
            eventsUpdate.setEventname(eventname);
            eventsUpdate.setEventdescription(eventdescription);
            eventsUpdate.setEventperiod(eventperiod);
            eventsUpdate.setEventfrequency(eventfrequency);
            eventsUpdate.setUser_id(user_id);
            eventsUpdate.setSociety_id(society_id);
            eventsUpdate.setOrganizationId(organization_id);
            eventsUpdate.setFacilityId(facility_id);
            database.insertEvents(eventsUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }
    public int ValidateLastUpdate(JSONObject resultObj) {

        int UpdatedNeeded = 0;
////        ClassDatabase database = new ClassDatabase(getActivity());
        try {

            String id = resultObj.getString("id");
            String society_id = resultObj.getString("society_id");
            String lastupdate = resultObj.getString("lastupdate");
            String needupdate = resultObj.getString("needupdate");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelLastupdate currentUpdate = database.getLastupdate();
            String currVersion = currentUpdate.getNeedupdate();

            int serverUpdate = 0;
            int mobileUpdate = 0;

            try {
                serverUpdate = Integer.parseInt(needupdate);
                mobileUpdate = Integer.parseInt(currVersion);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }


            if(serverUpdate > mobileUpdate){
                database.deleteLastupdate(society_id);
                ModelLastupdate lastUpdate = new ModelLastupdate();
                lastUpdate.setId(id);
                lastUpdate.setSociety_id(society_id);
                lastUpdate.setLastupdate(lastupdate);
                lastUpdate.setNeedupdate(needupdate);
                lastUpdate.setOrganizationId(organization_id);
                lastUpdate.setFacilityId(facility_id);
                lastUpdate.setServerupdate(lastupdate);

                Date date = new Date();
                SimpleDateFormat mdformatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String deviceupdatetime = mdformatTime.format(date);

                lastUpdate.setDeviceupdate(deviceupdatetime);

                database.deleteAllLastupdate();
                database.insertLastupdate(lastUpdate);
                UpdatedNeeded = 1;
            }
            else{
                UpdatedNeeded = 0;
            }

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }
        return UpdatedNeeded;
    }

    public void ValidateAndInsertUser(String imei){

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_CONFIG+"data="+imei;
        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")){

                        JSONArray resultArr = responseObj.getJSONArray("RESULT");
                        if(resultArr.length() > 0) {

                            JSONObject resultObj = resultArr.getJSONObject(0);
                            InserUserData(resultObj);
                        }else {

                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }

                    }
                    else {
                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));

                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();

                    }


                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception",""+e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);
                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();


            }
        });
    }

    public void CheckSubscriptionExpiry(JSONObject userObject){

////        // ClassDatabase database = new ClassDatabase(getActivity());
        //   ModelReader userInfo = database.getUser();
        int subscription_expired = 0;
        String valid_until = "";

        try{
            valid_until = userObject.getString("subdate");
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Date strDate = sdf.parse(valid_until);
            if (new Date().after(strDate)) {
                subscription_expired = 1;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        if(subscription_expired > 0) {

            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                    getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            cdd.show();

        }

    }

    public void ValidateIMEIUser(String imei){

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_USERDETAILS+"data="+imei;

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody,"UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")){

                        String strResponseCode = responseObj.getString("STATUS");

                        if(strResponseCode.equals(ClassCommon.SUBSCRIPTIONEXPIRED)) {

                            // CheckSubscriptionExpiry(resultObj);
                            //MOVE TO SUBSCRIPTION RENEWAL SCREEEN //TODO

                        }else if(strResponseCode.equals(ClassCommon.SUCCESS)){

                            JSONArray resultArr = responseObj.getJSONArray("RESULT");
                            if (resultArr.length() > 0) {
                                JSONObject resultObj = resultArr.getJSONObject(0);
                                UpdateUserData(resultObj);
                            }
                        }else if(strResponseCode.equals(ClassCommon.REGREQUESTRECEIVED)){

                            // CheckSubscriptionExpiry(resultObj);
                            //MOVE TO INFORMATION SCREEN with SLATRACK Details //TODO
                        }
                        else {
                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();


                        }

                    }
                    else {

                        String strResponseCode = responseObj.getString("STATUS");

                        if(strResponseCode.equals(ClassCommon.USERNOTREGISTERED)){

                            Intent intent = new Intent(getActivity(), ActivityRegistration.class);
                            startActivity(intent);
                            getActivity().finish();

                        }
                        else{

                        }
                    }


                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception",""+e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);

                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }

    private void UpdateUserData(JSONObject resultObj){

////        final ClassDatabase database = new ClassDatabase(getActivity());

        try {

            String name = resultObj.getString("name");
            String imei1 = resultObj.getString("imei1");
            String imei2 = resultObj.getString("imei2");
            String role = resultObj.getString("role");
            String firstname = resultObj.getString("firstname");
            String middelname = resultObj.getString("middelname");
            String lastname = resultObj.getString("lastname");
            String amount = resultObj.getString("amount");
            String expiry_date = resultObj.getString("subdate");
            String user_id = resultObj.getString("user_id");
            String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");
            String subtype = resultObj.getString("subtype");
            String substatus = resultObj.getString("substatus");
            String readerstatus = resultObj.getString("readerstatus");
            String subname = resultObj.getString("subname");
            String orgstatus = resultObj.getString("status");


            ModelReader objReader = new ModelReader();
            objReader.setName(name);
            objReader.setImei1(imei1);
            objReader.setImei2(imei2);
            objReader.setRole(role);
            objReader.setFirstname(firstname);
            objReader.setMiddelname(middelname);
            objReader.setLastname(lastname);
            objReader.setAmount(amount);
            objReader.setUser_id(user_id);
            objReader.setSociety_id(society_id);
            objReader.setExpiryDate(expiry_date);

            objReader.setOrganization_id(organization_id);
            objReader.setFacility_id(facility_id);

            objReader.setSubtype(subtype);
            objReader.setSubstatus(substatus);
            objReader.setSubname(subname);
            objReader.setReaderstatus(readerstatus);

            database.updateUser(objReader);

        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception",""+e.getMessage());
        }

    }

    private void InserUserData(JSONObject resultObj){

//////        final ClassDatabase database = new ClassDatabase(getActivity());

        try {

            String name = resultObj.getString("name");
            String imei1 = resultObj.getString("imei1");
            String imei2 = resultObj.getString("imei2");
            String role = resultObj.getString("role");
            String firstname = resultObj.getString("firstname");
            String middelname = resultObj.getString("middelname");
            String lastname = resultObj.getString("lastname");
            String amount = resultObj.getString("amount");
            String expiry_date = resultObj.getString("subdate");
            String user_id = resultObj.getString("user_id");
            String readercode = resultObj.getString("readercode");
            String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            //Organization Data
            String orgname = resultObj.getString("orgname");
            String enablesms = resultObj.getString("enablesms");
            String enableemail = resultObj.getString("enableemail");
            String isverified = resultObj.getString("isverified");
            String primarysos = resultObj.getString("primarysos");
            String updatemode = resultObj.getString("updatemode");
            String secmobno = resultObj.getString("secmobno");
            String subtype = resultObj.getString("subtype");
            String societytype = resultObj.getString("societytype");
            String logopath = resultObj.getString("logopath");

            String substatus = resultObj.getString("substatus");
            String readerstatus = resultObj.getString("readerstatus");
            String subname = resultObj.getString("subname");
            String orgstatus = resultObj.getString("status");

            // String refUserId = "";
            ModelReader refReader = database.getUser();
            if (refReader != null) {
                if (database.userCount() > 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getResources().getString(R.string.multiple_user));
                    builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            while( database.userCount() > 0){
                                ModelReader objReader = database.getUser();
                                UnMapUser(objReader.getImei1());
                                UnMapUser(objReader.getImei2());
                                database.deleteUser(objReader.getUser_id());
                            }
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();

                }
            }

            ModelReader objReader = new ModelReader();
            objReader.setName(name);
            objReader.setImei1(imei1);
            objReader.setImei2(imei2);
            objReader.setRole(role);
            objReader.setFirstname(firstname);
            objReader.setMiddelname(middelname);
            objReader.setLastname(lastname);
            objReader.setAmount(amount);
            objReader.setUser_id(user_id);
            objReader.setSociety_id(society_id);
            objReader.setReadercode(readercode);
            objReader.setExpiryDate(expiry_date);

            objReader.setOrganization_id(organization_id);
            objReader.setFacility_id(facility_id);

            objReader.setSubtype(subtype);
            objReader.setSubstatus(substatus);
            objReader.setSubname(subname);
            objReader.setReaderstatus(readerstatus);

            if (user_id.equals(refReader.getUser_id())) {
                database.updateUser(objReader);
            } else {

                database.insertUser(objReader);
            }

            globalVariable.setReaderCode(readercode);

            ModelOrganization objOrganization = new ModelOrganization();

            //Update Organization Data


            objOrganization.setOrgname(orgname);
            objOrganization.setUser_id(user_id);
            objOrganization.setSociety_id(society_id);
            objOrganization.setExpiry_date(expiry_date);
            objOrganization.setEnablesms(enablesms);
            objOrganization.setEnableemail(enableemail);
            objOrganization.setIsverified(isverified);
            objOrganization.setPrimarysos(primarysos);
            objOrganization.setUpdatemode(updatemode);
            objOrganization.setSecmobno(secmobno);
            objOrganization.setSubtype(subtype);
            objOrganization.setSocietytype(societytype);
            objOrganization.setLogopath(logopath);
            objOrganization.setOrganizationId(organization_id);
            objOrganization.setFacilityId(facility_id);

            objOrganization.setOrganizationId(organization_id);
            objOrganization.setFacilityId(facility_id);

            objOrganization.setSubstatus(substatus);
            objOrganization.setOrgstatus(orgstatus);

            database.insertOrganization(objOrganization);


        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception",""+e.getMessage());
        }

    }


    public void checkIN(final ModelCheckINData checkINData) {

        String checkInURL = "http://slatrack.com/webservice/slawebservice.php?d=" + checkINData.getReaderID() + "," + checkINData.getCheckpoint()+ "," + checkINData.getSwipeDate()+ ","+checkINData.getSwipeTime()+",I";
        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(checkInURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    if (response.equalsIgnoreCase("Done")) {
                        database.deleteCheckIn(checkINData.getAutoIncID());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void checkPermissionAndGetIMEI() {

        if (ContextCompat.checkSelfPermission(activityWelcome, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activityWelcome,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    101);
        } else {
            try {
                TelephonyManager tel = (TelephonyManager) activityWelcome.getSystemService(Context.TELEPHONY_SERVICE);
                String imei = tel.getDeviceId().toString();
                globalVariable.setImei(imei);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        TelephonyManager tel = (TelephonyManager) activityWelcome.getSystemService(Context.TELEPHONY_SERVICE);
                        String imei = tel.getDeviceId().toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activityWelcome);

                    if (!isOneAttempt) {
                        builder.setMessage(getResources().getString(R.string.permissionMsg));
                        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isOneAttempt = true;
                                dialogInterface.dismiss();
                                checkPermissionAndGetIMEI();
                            }
                        });
                        builder.show();
                    } else {
                        builder.setMessage(getResources().getString(R.string.app_close));
                        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                activityWelcome.finish();
                            }
                        });

                        builder.show();
                    }
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}
