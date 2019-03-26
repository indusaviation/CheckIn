package com.slatrack.gtms.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.adapter.AdapterScheduleList;
import com.slatrack.gtms.adapter.AdapterSosMessage;
import com.slatrack.gtms.model.ModelSosMessage;
import com.slatrack.gtms.utils.ClassBottomNVFragment;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelSchedule;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentSchedules extends Fragment {

    View rootView;

    public ClassDatabase database;
    public SLAGTMS globalVariable;
    ActivityWelcome activityWelcome;

    private ProgressBar progressBar;
    private ListView listView;
    ArrayList<ModelSchedule> scheduleArrayList = new ArrayList<>();
    AdapterScheduleList adapterScheduleList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_shift_schedules, container, false);

        activityWelcome = (ActivityWelcome) getActivity();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarSchedule);
        listView = (ListView) rootView.findViewById(R.id.schedulelist);

        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);
        scheduleArrayList =  database.getAllSchedules();

        if(scheduleArrayList !=null){
            if(scheduleArrayList.size() > 0){
                adapterScheduleList = new AdapterScheduleList(scheduleArrayList,activityWelcome);
                listView.setAdapter(adapterScheduleList);
            }
            else{
                if (ClassConnectivity.isConnected(activityWelcome)){
                    GetShiftDetails();
                    scheduleArrayList =  database.getAllSchedules();
                    if(scheduleArrayList !=null){
                        if(scheduleArrayList.size() > 0){
                            adapterScheduleList = new AdapterScheduleList(scheduleArrayList,activityWelcome);
                            listView.setAdapter(adapterScheduleList);
                        }
                        else{
                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.noitem_text),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }
                    }
                }
                else{
                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            getResources().getString(R.string.nonetwork_text),getResources().getString(R.string.msg_title_information));
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    cdd.show();

                }

            }
        }
        else{

            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                    getResources().getString(R.string.nonetwork_text),getResources().getString(R.string.msg_title_information));
            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            cdd.show();

        }

//        Button btnSwipeCheckpoint = (Button)findViewById(R.id.swipecheckpoint);
//        btnSwipeCheckpoint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(FragmentSchedules.this, FragmentScan.class);
//                startActivity(intent);
//            }
//        });

        progressBar.setVisibility(View.GONE);
        return rootView;

    }

    public void GetShiftDetails() {

        String data = globalVariable.getImei();
        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_GETALLSCHEDULE+"data="+data;

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
                            database.deleteAllSchedule();
                            for(int objCount=0; objCount< resultArr.length(); objCount++){
                                JSONObject scheduleObj = resultArr.getJSONObject(objCount);
                                if(scheduleObj!= null) {
                                    UpdateSchduleData(scheduleObj);
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


    public void UpdateSchduleData(JSONObject resultObj)
    {
        try {

            String society_id = resultObj.getString("society_id");
            String user_id = "0";//resultObj.getString("user_id");
            String slashift_id = resultObj.getString("shift_id");
            String slaschedule_id = resultObj.getString("id");
            String shiftname = "Test Name";//resultObj.getString("shiftname"); TODO
            String schedulefrom = resultObj.getString("schedule_from");
            String scheduleto = resultObj.getString("schedule_to");

            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelSchedule schduleUpdate = new ModelSchedule();

            schduleUpdate.setSociety_id(society_id);
            schduleUpdate.setUser_id(user_id);
            schduleUpdate.setSlashift_id(slashift_id);
            schduleUpdate.setSlaschedule_id(slaschedule_id);
            schduleUpdate.setShiftname(shiftname);
            schduleUpdate.setSchedulefrom(schedulefrom);
            schduleUpdate.setScheduleto(scheduleto);
            schduleUpdate.setOrganizationId(organization_id);
            schduleUpdate.setFacilityId(facility_id);

            database.insertSchedule(schduleUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }


}
