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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.adapter.AdapterScheduleList;
import com.slatrack.gtms.adapter.AdapterShiftList;
import com.slatrack.gtms.utils.ClassBottomNVFragment;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelShift;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentShifts extends Fragment {

    View rootView;

    public ClassDatabase database;
    public SLAGTMS globalVariable;
    ActivityWelcome activityWelcome;
    private ListView listView;
    private Button btnSchedules;

    private ProgressBar progressBar;
    ArrayList<ModelShift> shiftArrayList = new ArrayList<>();
    AdapterShiftList adapterShiftList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_shift_schedules, container, false);

        activityWelcome = (ActivityWelcome) getActivity();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarShifts);
        listView = (ListView) rootView.findViewById(R.id.shiftlist);
        btnSchedules = (Button)rootView.findViewById(R.id.submitshifts);

        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);


        btnSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //
            }
        });


        if(shiftArrayList !=null){
            if(shiftArrayList.size() > 0){
                adapterShiftList = new AdapterShiftList(shiftArrayList,activityWelcome);
                listView.setAdapter(adapterShiftList);
            }
            else{
                if (ClassConnectivity.isConnected(activityWelcome)){
                    GetShiftDetails();

                    shiftArrayList =  database.getAllShifts();
                    if(shiftArrayList !=null){
                        if(shiftArrayList.size() > 0){
                            adapterShiftList = new AdapterShiftList(shiftArrayList,activityWelcome);
                            listView.setAdapter(adapterShiftList);
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

        progressBar.setVisibility(View.GONE);
        return rootView;

    }


    public void GetShiftDetails() {


        String data = globalVariable.getImei();
        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_GETALLSHIFTS+"data="+data;
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
                            database.deleteAllShift();
                            for(int objCount=0; objCount< resultArr.length(); objCount++){
                                JSONObject shiftObj = resultArr.getJSONObject(objCount);
                                if(shiftObj!= null) {
                                    UpdateShiftData(shiftObj);
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


    public void UpdateShiftData(JSONObject resultObj)
    {
        try {
            String society_id = resultObj.getString("society_id");
            String user_id = "0"; //resultObj.getString("user_id");
            String slashift_id = resultObj.getString("id");
            String shiftname = resultObj.getString("shiftname");
            String shiftfrom = resultObj.getString("shift_time_from");
            String shiftto = resultObj.getString("shift_time_to");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");


            ModelShift shiftUpdate = new ModelShift();

            shiftUpdate.setSociety_id(society_id);
            shiftUpdate.setUser_id(user_id);
            shiftUpdate.setSlashift_id(slashift_id);
            shiftUpdate.setShiftname(shiftname);
            shiftUpdate.setShiftfrom(shiftfrom);
            shiftUpdate.setShiftto(shiftto);
            shiftUpdate.setOrganizationId(organization_id);
            shiftUpdate.setFacilityId(facility_id);


            database.insertShift(shiftUpdate);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }


}
