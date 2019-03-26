package com.slatrack.gtms.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.adapter.AdapterEventList;
import com.slatrack.gtms.adapter.AdapterSubratesList;
import com.slatrack.gtms.fragment.FragmentDiagnostics;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.fragment.FragmentSosmessage;
import com.slatrack.gtms.fragment.FragmentSubList;
import com.slatrack.gtms.fragment.FragmentSupport;
import com.slatrack.gtms.fragment.FragmentTxnReport;
import com.slatrack.gtms.utils.ClassBottomNVFragment;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelEventData;
import com.slatrack.gtms.model.ModelEvents;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.support.v7.widget.AppCompatTextView;

import cz.msebera.android.httpclient.Header;

public class FragmentEventList extends Fragment {


    View rootView;

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

    private LocationManager locationManager=null;
    private LocationListener locationListener=null;

    AppCompatTextView uploadtoserver, saverecords;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        checkpointcode =getArguments().getString("checkpoint");

        rootView = inflater.inflate(R.layout.activity_event_list, container, false);
        activityWelcome = (ActivityWelcome) getActivity();

        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);
        eventsArrayList = database.getAllEvents();



        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarEvent);
        saverecords = (AppCompatTextView) rootView.findViewById(R.id.saverecords);
        listView = (ListView) rootView.findViewById(R.id.eventlist);


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
                                    getResources().getString(R.string.noeventitem_text),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }
                    }
                } else {

                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            getResources().getString(R.string.nonetwork_text),getResources().getString(R.string.msg_title_information));
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    cdd.show();


                }
                progressBar.setVisibility(View.GONE);

            }
        }
        else{

            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                    getResources().getString(R.string.nonetwork_text),getResources().getString(R.string.msg_title_information));
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            cdd.show();

        }

        progressBar.setVisibility(View.GONE);
        return rootView;
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
                                    getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }

                    }
                    else{
                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
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
                        getResources().getString(R.string.server_issue),getResources().getString(R.string.msg_title_information));
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
                    getResources().getString(R.string.noeventdata_txt),getResources().getString(R.string.msg_title_information));
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            cdd.show();

        }
    }

    private void UploadToServer(String checkInURL) {

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
                            database.deleteAllEventData(globalVariable.getReaderCode());
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

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                //Move to the old fragement which was loaded..
                activityWelcome.getSupportFragmentManager().popBackStack();
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
