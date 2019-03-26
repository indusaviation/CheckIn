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
import android.support.v7.widget.AppCompatTextView;
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
import com.slatrack.gtms.adapter.AdapterSubratesList;
import com.slatrack.gtms.fragment.FragmentDiagnostics;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.fragment.FragmentSosmessage;
import com.slatrack.gtms.fragment.FragmentSupport;
import com.slatrack.gtms.utils.ClassBottomNVFragment;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelOrganization;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.model.ModelSubscription;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentSubList extends Fragment {


    View rootView;

    public ClassDatabase database;
    public SLAGTMS globalVariable;
    ActivityWelcome activityWelcome;

    public ProgressBar progressBar;
    public ListView listView;


   // private Button btnsubscribe;
    ArrayList<ModelSubscription> subratesArrayList = new ArrayList<>();
    AdapterSubratesList adapterSubrateList;
    private AppCompatTextView orgName,userName,subDate,subType,orgStatus,subStatus,noHistoryText,refreshsub;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_subscription_list, container, false);

        activityWelcome = (ActivityWelcome) getActivity();
        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);

        subratesArrayList =  database.getAllSubscriptionRates();

        noHistoryText = (AppCompatTextView) rootView.findViewById(R.id.noHistoryTextSub);
//        orgName = (AppCompatTextView) findViewById(R.id.txnorgnamesub);
//        userName = (AppCompatTextView) findViewById(R.id.txnusernamesub);
//        subDate = (AppCompatTextView) findViewById(R.id.txnsubdatesub);
//        subType = (AppCompatTextView) findViewById(R.id.txnsubtypesub);
//        orgStatus = (AppCompatTextView) findViewById(R.id.txnorgstatussub);
//        subStatus = (AppCompatTextView) findViewById(R.id.txnsubstatussub);
//        refreshsub = (AppCompatTextView) findViewById(R.id.refreshsub);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarSub);
        listView = (ListView) rootView.findViewById(R.id.subratelist);




        if(subratesArrayList !=null){
            if(subratesArrayList.size() > 0){
                adapterSubrateList = new AdapterSubratesList(subratesArrayList,activityWelcome);
                listView.setAdapter(adapterSubrateList);
            }
            else{
                if (ClassConnectivity.isConnected(activityWelcome)){
                    GetSubScriptionRates();
                    subratesArrayList =  database.getAllSubscriptionRates();
                    if(subratesArrayList !=null){
                        if(subratesArrayList.size() > 0){
                            adapterSubrateList = new AdapterSubratesList(subratesArrayList,activityWelcome);
                            listView.setAdapter(adapterSubrateList);
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



    public void GetSubScriptionRates() {

        String configURL = ClassCommon.BASE_URL+ClassCommon.GET_SUBSCRIPTIONRATES+"data=1234";

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
                            database.deleteAllSubscriptionRates();
                            for(int objCount=0; objCount< resultArr.length(); objCount++){
                                JSONObject subrateeObj = resultArr.getJSONObject(objCount);
                                if(subrateeObj!= null) {
                                   UpdateSubScriptionRates(subrateeObj);
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


    public void UpdateSubScriptionRates(JSONObject resultObj)
    {
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

}
