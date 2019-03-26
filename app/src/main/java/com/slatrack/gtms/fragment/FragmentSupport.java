package com.slatrack.gtms.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;
import android.location.LocationManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivitySlatrack;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.json.JSONObject;
import java.util.regex.Pattern;
import cz.msebera.android.httpclient.Header;

public class FragmentSupport extends Fragment implements LocationListener {
    Toolbar topToolBar;
    ClassDatabase database;
    private SLAGTMS globalVariable;
    private ProgressBar progressBar;
    private EditText txtSupportName;
    private EditText txtSupportOrgName;
    private EditText txtSupportEmail;
    private EditText txtSupportPhone;
    private EditText txtRequestText;
    private TextView txtSupportText;
    private String gpslat = "0.00";
    private String gpslong= "0.00";

    private LocationManager locationManager=null;
    private LocationListener locationListener=null;
    private Boolean flag = false;

    ActivityWelcome activityWelcome;

    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView  = inflater.inflate(R.layout.activity_support,container,false);

        activityWelcome = (ActivityWelcome) getActivity();


        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarSupport);

        //Initialize all form controls
        txtSupportName = (EditText)rootView.findViewById(R.id.txtUserNameSupport);
        txtSupportOrgName = (EditText)rootView.findViewById(R.id.txtOrgNameSupport);
        txtSupportPhone = (EditText)rootView.findViewById(R.id.txtPhoneSupport);
        txtSupportEmail = (EditText)rootView.findViewById(R.id.txtEmailSupport);
        txtRequestText = (EditText)rootView.findViewById(R.id.txtRequestText);
        txtSupportText = (TextView)rootView.findViewById(R.id.supportText);

        globalVariable  = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);

//        String msgString = String.format(getResources().getString(R.string.support_text), globalVariable.getSupportphone());

        txtSupportText.setText(String.valueOf(globalVariable.getSupportphone()));

        ModelReader objReader = database.getUser();
        if(objReader != null){
            if(database.userCount() == 1){
                txtSupportName.setText(objReader.getFirstname().toString()+" "+objReader.getLastname().toString());
                txtSupportOrgName.setText(objReader.getName().toString());
            }
        }
        final Button btnSubmitTicket = rootView.findViewById(R.id.btnTicketSubmit);
        btnSubmitTicket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(ValidatePhone() == 1){
                    if(ValidateEmail() == 1){

                        if(txtSupportName.getText().toString().isEmpty())
                        {
                            Toast.makeText(activityWelcome,"Please enter your name", Toast.LENGTH_SHORT).show();
                        }
                        else if(txtSupportOrgName.getText().toString().isEmpty())
                        {
                            Toast.makeText(activityWelcome,"Please enter your organization name", Toast.LENGTH_SHORT).show();
                        }
                        else if(txtRequestText.getText().toString().isEmpty())
                        {
                            Toast.makeText(activityWelcome,"Please enter the request for fast processing", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            getLocation();
                        }
                    }
                }
                else{
                    //
                }


            }
        });

        //GPS
        if (ContextCompat.checkSelfPermission(activityWelcome, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activityWelcome, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activityWelcome, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        progressBar.setVisibility(View.GONE);

        return rootView;
    }



    void getLocation() {
        try {
            locationManager = (LocationManager) activityWelcome.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        gpslat = String.valueOf(location.getLatitude());
        gpslong = String.valueOf(location.getLongitude());
        SubmitTicket();

//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            txtSupportText.setText(txtSupportText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
//                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
//        }catch(Exception e)
//        {
//
//        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(activityWelcome, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }



    private int ValidateEmail(){

        int iResult = 0;

        String email = txtSupportEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern))
        {
            iResult =1;
        }
        else
        {
            iResult = 0;
            Toast.makeText(activityWelcome,"Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        return iResult;

    }
    private int ValidatePhone() {
        String phone = txtSupportPhone.getText().toString().trim();
        int iResult=0;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                iResult = 0;
                Toast.makeText(activityWelcome,"Please enter valid phone number", Toast.LENGTH_SHORT).show();
            } else {
                iResult = 1;
            }
        } else {
            iResult = 0;
            Toast.makeText(activityWelcome,"Please enter valid phone number", Toast.LENGTH_SHORT).show();
        }
        return iResult;
    }

    public void SubmitTicket() {

        String sName = txtSupportName.getText().toString();
        String sOrgName = txtSupportOrgName.getText().toString();
        String sPhone = txtSupportPhone.getText().toString();
        String sEmail = txtSupportEmail.getText().toString();


        String request_text = txtRequestText.getText().toString();


        //GPS and Request Time..

        String dataURL = "data="+globalVariable.getImei()+","+sName+","+sOrgName+","+sPhone+","+sEmail+","+gpslat+","+gpslong+","+request_text;

        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(activityWelcome)){
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }
        String configURL = ClassCommon.BASE_URL + ClassCommon.SET_SUBMITTICKET + dataURL;

        client.get(configURL, new AsyncHttpResponseHandler() {


            @Override
            public void onStart() {

            }
            @Override
            public void onFinish() {
                txtSupportPhone.setText("");
                txtSupportEmail.setText("");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        String strResponseCode = responseObj.getString("STATUS");

                        if (strResponseCode.equals(ClassCommon.SUCCESS)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(activityWelcome);
                            builder.setMessage(getResources().getString(R.string.thankyou_ticketreceived));
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    FragmentTransaction ft = activityWelcome.getSupportFragmentManager().beginTransaction();
                                    FragmentDashboard dashboard = new FragmentDashboard();
                                    ft.replace(R.id.fragmentContainer,dashboard);
                                    ft.addToBackStack("DashBoard");
                                    ft.commit();

                                }
                            });

                            builder.show();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activityWelcome);
                            String msgString = String.format(getResources().getString(R.string.verification_fail), globalVariable.getSupportphone());
                            String errorcode = "ERROR 1018 :";
                            msgString = errorcode + msgString;
                            builder.setMessage(msgString);
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(activityWelcome, ActivitySlatrack.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent); //TODO Ideally we need to move to About SLATRACK screen
                                }
                            });
                            builder.show();
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
                        getResources().getString(R.string.unknown_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }

}
