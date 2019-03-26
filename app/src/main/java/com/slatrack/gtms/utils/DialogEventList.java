package com.slatrack.gtms.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.adapter.AdapterEventList;
import com.slatrack.gtms.model.ModelEventData;
import com.slatrack.gtms.model.ModelEvents;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class DialogEventList extends Dialog {


    LinearLayout btnClose;

    public ClassDatabase database;
    public SLAGTMS globalVariable;
    ActivityWelcome activityWelcome;

    public ProgressBar progressBar;
    public ListView listView;

    ArrayList<ModelEvents> eventsArrayList = new ArrayList<>();
    AdapterEventList adapterEventList;
    private String checkpointcode = "";

    private String gpslat = "0.00";
    private String gpslong= "0.00";

//    private LocationManager locationManager=null;
//    private LocationListener locationListener=null;

    AppCompatTextView uploadtoserver, saverecords;



    public DialogEventList(@NonNull Context context, String checkpointcode) {
        super(context);
        this.activityWelcome = (ActivityWelcome) context;
        this.checkpointcode = checkpointcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_dialog);

        btnClose = (LinearLayout) findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);
        eventsArrayList = database.getAllEvents();

        progressBar = (ProgressBar) findViewById(R.id.progressBarEvent);
        saverecords = (AppCompatTextView) findViewById(R.id.saverecords);
        listView = (ListView) findViewById(R.id.eventlist);


        saverecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int iCount = 0; iCount < adapterEventList.getCount(); iCount++){

                    ModelEvents ev = (ModelEvents)adapterEventList.getItem(iCount);
                    if(ev.isChecked()){

                        Date date = new Date();
                        SimpleDateFormat mdformatTime = new SimpleDateFormat("HH:MM:ss ");
                        String swipeTime = mdformatTime.format(date);

                        SimpleDateFormat mdformatDate = new SimpleDateFormat("dd/MM/yyyy");
                        String swipeDate = mdformatDate.format(date);

                        String readerId = globalVariable.getReaderCode();
                        String evCode = ev.getEventcode();

                        getLocation();

                        ModelEventData evData = new ModelEventData(readerId,checkpointcode,evCode,swipeTime,swipeDate,gpslat,gpslong);
                        database.insertEventData(evData);
                    }
                }

                UploadEventData();
            }
        });




        if (eventsArrayList != null) {
            if (eventsArrayList.size() > 0) {
                adapterEventList = new AdapterEventList(eventsArrayList, activityWelcome);
                listView.setAdapter(adapterEventList);

            } else {
                if (ClassConnectivity.isConnected(activityWelcome)) {
                    // GetEventsDetails();

                    eventsArrayList = database.getAllEvents();
                    if (eventsArrayList != null) {
                        if (eventsArrayList.size() > 0) {
                            adapterEventList = new AdapterEventList(eventsArrayList, activityWelcome);
                            listView.setAdapter(adapterEventList);
                        }
                        else{

                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    activityWelcome.getResources().getString(R.string.noeventitem_text),activityWelcome.getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();
                            }
                    }
                } else {
                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            activityWelcome.getResources().getString(R.string.nonetwork_text),activityWelcome.getResources().getString(R.string.msg_title_information));
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    cdd.show();


                }
                progressBar.setVisibility(View.GONE);

            }
        }
        else{

            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                    activityWelcome.getResources().getString(R.string.nonetwork_text),activityWelcome.getResources().getString(R.string.msg_title_information));
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            cdd.show();

        }

        progressBar.setVisibility(View.GONE);
    }


//    @Override
//    public void onLocationChanged(Location location) {
//
//        gpslat = String.valueOf(location.getLatitude());
//        gpslong = String.valueOf(location.getLongitude());
//
//    }


    void getLocation() {
        try {
//            locationManager = (LocationManager) activityWelcome.getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    public void GetEventsDetails() {

        String data = globalVariable.getImei();

        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_ALLEVENTS+"data="+data;

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(activityWelcome, activityWelcome.getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
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

                        //Parse ModelSubscription Rate List
                        JSONArray resultArr = responseObj.getJSONArray("RESULT");
                        if(resultArr.length() > 0) {
                            database.deleteAllEvents();
                            for(int objCount=0; objCount< resultArr.length(); objCount++){
                                JSONObject eventsObj = resultArr.getJSONObject(objCount);
                                if(eventsObj!= null) {
                                    UpdateEventsData(eventsObj);
                                }
                            }
                        }
                        else {

                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    activityWelcome.getResources().getString(R.string.verification_fail),activityWelcome.getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }

                    }
                    else{

                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                activityWelcome.getResources().getString(R.string.verification_fail),activityWelcome.getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();

                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception",""+e.getMessage());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);
                ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                        activityWelcome.getResources().getString(R.string.server_issue),activityWelcome.getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();
            }
        });

    }

    private void UploadEventData(){
        ArrayList<ModelEventData> eventsRecords = new ArrayList<>();

        eventsRecords = database.getEventDataList(globalVariable.getReaderCode());
        String dataUpload = "data=";

        if(eventsRecords.size() > 0) {
            for(int objCount=0; objCount< eventsRecords.size(); objCount++){
                ModelEventData evRecord = eventsRecords.get(objCount);
                if(evRecord!= null) {
                    dataUpload += evRecord.getReaderID()+","+evRecord.getCheckpoint()+","+evRecord.getEventcode()+","+evRecord.getSwipeDate()+","+evRecord.getSwipeTime()+",I;";
                }
            }
            String serverURL = ClassCommon.BASE_URL+ClassCommon.SET_EVENTDATA+dataUpload;
            if (ClassConnectivity.isConnected(activityWelcome)) {
                if(!dataUpload.equalsIgnoreCase("data=")) {
                    UploadToServer(serverURL);
                }
            }
        }
        else{

            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                    activityWelcome.getResources().getString(R.string.noeventdata_txt),activityWelcome.getResources().getString(R.string.msg_title_information));
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            cdd.show();


        }
    }

    private void UploadToServer(String checkInURL) {

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(activityWelcome, activityWelcome.getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(activityWelcome, activityWelcome.getResources().getString(R.string.checkinSuccess), Toast.LENGTH_SHORT).show();
                            database.deleteAllEventData(globalVariable.getReaderCode());
                        }
                    }
                    else{
                        String strResponseCode = responseObj.getString("STATUS");
                        if (strResponseCode.equals(ClassCommon.INVALIDCHECKPOINT)) {
                            Toast.makeText(activityWelcome, activityWelcome.getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(activityWelcome, activityWelcome.getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activityWelcome, activityWelcome.getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activityWelcome, activityWelcome.getResources().getString(R.string.checkinFail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                //Move to the old fragement which was loaded..
//                activityWelcome.getSupportFragmentManager().popBackStack();
                dismiss();
            }

        });

    }

    public void UpdateEventsData(JSONObject resultObj){

        try {

            String society_id = resultObj.getString("society_id");
            String user_id = resultObj.getString("user_id");
            String eventcode = resultObj.getString("eventcode");
            String eventname = resultObj.getString("eventname");
            String eventdescription = resultObj.getString("eventdescription");
            String eventperiod = resultObj.getString("eventperiod");
            String eventfrequency = resultObj.getString("eventfrequency");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");


            ModelEvents events = new ModelEvents();

            events.setSociety_id(society_id);
            events.setUser_id(user_id);
            events.setEventcode(eventcode);
            events.setEventname(eventname);
            events.setEventdescription(eventdescription);
            events.setEventperiod(eventperiod);
            events.setEventfrequency(eventfrequency);
            events.setOrganizationId(organization_id);
            events.setFacilityId(facility_id);

            database.insertEvents(events);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

}
