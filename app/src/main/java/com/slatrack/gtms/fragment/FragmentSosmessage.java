package com.slatrack.gtms.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.telephony.SmsManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.adapter.AdapterSosMessage;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassConnectivity;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelOrganization;
import com.slatrack.gtms.model.ModelSosMessage;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import java.util.regex.Pattern;

import static com.slatrack.gtms.utils.ClassCommon.INDUSSOSCONST;


public class FragmentSosmessage extends Fragment {

    View rootView;

    public ClassDatabase database;
    public SLAGTMS globalVariable;
    ActivityWelcome activityWelcome;

    private boolean isOneAttempt;
    private ProgressBar progressBar;
    private ListView sosMessagesListView;
    public TextView sosScreenText;


    AdapterSosMessage adapterSosMessages;
    ArrayList<ModelSosMessage> sosMsgArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_sosmessage, container, false);

        activityWelcome = (ActivityWelcome) getActivity();


        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarSos);
        sosMessagesListView = (ListView) rootView.findViewById(R.id.sosmsglist);
        sosScreenText = (TextView) rootView.findViewById(R.id.sosscreentext);


        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);

     //   checkPermissionForSMS();

        sosMsgArrayList = database.getAllSOSMessage();
        if (sosMsgArrayList != null) {
            if (sosMsgArrayList.size() > 0) {
                adapterSosMessages = new AdapterSosMessage(sosMsgArrayList, activityWelcome, FragmentSosmessage.this);
                sosMessagesListView.setAdapter(adapterSosMessages);
            } else {
                //if there is a connectivity - download the list from the server
                //Else show the keep trying screen.
                if (ClassConnectivity.isConnected(activityWelcome)) {
                    GetSosMessageList();
                    sosMsgArrayList = database.getAllSOSMessage();
                    if (sosMsgArrayList != null) {
                        if (sosMsgArrayList.size() > 0) {
                            adapterSosMessages = new AdapterSosMessage(sosMsgArrayList, activityWelcome, FragmentSosmessage.this);
                            sosMessagesListView.setAdapter(adapterSosMessages);
                        } else {

                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.noitem_text),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();
                        }

                    } else {

                    }
                } else {

                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            getResources().getString(R.string.nonetwork_text),getResources().getString(R.string.msg_title_information));
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
        return rootView;

    }


    private void GetSosMessageList() {

        String data = globalVariable.getImei();
        String configURL = ClassCommon.BASE_URL + ClassCommon.GET_ALLSOSMESSAGE + "data=" + data;
        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        //Parse ModelSubscription Rate List
                        JSONArray resultArr = responseObj.getJSONArray("RESULT");
                        if (resultArr.length() > 0) {
                            database.getAllSOSMessage();
                            for (int objCount = 0; objCount < resultArr.length(); objCount++) {
                                JSONObject sosObj = resultArr.getJSONObject(objCount);
                                if (sosObj != null) {
                                    UpdateSOSMessages(sosObj);
                                }
                            }
                        } else {
                            ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();
                        }

                    } else {
                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail),getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());

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

    public void UpdateSOSMessages(JSONObject resultObj) {

        try {

            String id = resultObj.getString("id");
            String msgconst = resultObj.getString("msgconst");
            String msgdetails = resultObj.getString("msgdetails");
            String society_id = resultObj.getString("society_id");

            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelSosMessage Sosmessage = new ModelSosMessage();
            Sosmessage.setId(id);
            Sosmessage.setMsgconst(msgconst);
            Sosmessage.setMsgdetails(msgdetails);
            Sosmessage.setSociety_id(society_id);
            Sosmessage.setFacilityId(organization_id);
            Sosmessage.setOrganizationId(facility_id);
            database.insertSOSMessage(Sosmessage);

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    public void SendSOSMessage(String msgConst, String message) {

        ModelOrganization objOrganization = database.getOrganization();


        String phoneNumber = objOrganization.getPrimarysos();

        if (phoneNumber.isEmpty()) {
            phoneNumber = globalVariable.getSupportphone();

        }
      //  String msgConst = txtMsgConst.getText().toString().trim();
        if (msgConst.equalsIgnoreCase(ClassCommon.INDUSSOSCONST)) {
            phoneNumber = globalVariable.getSupportphone();
        }

//        String message = txtMsgDetails.getText().toString();
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(activityWelcome, 0,
                new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(activityWelcome, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        activityWelcome.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(activityWelcome, "SOS sent successfully",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(activityWelcome, "Generic failure in sending. Please try",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(activityWelcome, "No service. Not enough SIGNAL strength",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(activityWelcome, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(activityWelcome, "Radio off. Error with SIM",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        activityWelcome.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(activityWelcome, "SOS delivered successfully",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(activityWelcome, "SOS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));


        if (!phoneNumber.isEmpty()) {
            if (isValidMobile(phoneNumber)) {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            } else {
                Toast.makeText(activityWelcome, "Phone number is not valid",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                    if (!isOneAttempt) {
                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.permissionMsg),getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();

                    } else {

                        ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.app_close),getResources().getString(R.string.msg_title_information));
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        cdd.show();
                    }
                }
                return;
            }

        }
    }

//    private void checkPermissionForSMS() {
//        if (ContextCompat.checkSelfPermission(activityWelcome, Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activityWelcome,
//                    new String[]{Manifest.permission.SEND_SMS},
//                    101);
//        } else {
//            try {
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

}
