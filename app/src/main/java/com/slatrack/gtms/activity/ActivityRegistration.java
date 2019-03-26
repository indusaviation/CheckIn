package com.slatrack.gtms.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONObject;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class ActivityRegistration extends AppCompatActivity {

    Toolbar topToolBar;
    private ProgressBar progressBar;
    private SLAGTMS globalVariable; //  = (SLAGTMS) getApplicationContext();
    private EditText txtName;
    private EditText txtOrgName;
    private EditText txtEmail;
    private EditText txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        topToolBar = (Toolbar) findViewById(R.id.welcomeToolBar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.sla_logo);
       // topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));

        progressBar = (ProgressBar) findViewById(R.id.progressBarReg);
        globalVariable  = (SLAGTMS) getApplicationContext();

        //Initialize all form controls
        txtName = (EditText)findViewById(R.id.txtUserName);
        txtOrgName = (EditText)findViewById(R.id.txtOrgName);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        txtEmail = (EditText)findViewById(R.id.txtEmail);

        final TextView registrationText = (TextView) findViewById(R.id.registrationText);

        String msgString = String.format(getResources().getString(R.string.registration_text), globalVariable.getSupportphone(),globalVariable.getSupportemail());


        Spanned html = Html.fromHtml(

                msgString
        );

        // Set TextView text from html
        registrationText.setText(html);

        final Button btnRegistration = findViewById(R.id.btnRegistrationSubmit);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int iValidate = 0;

                if(ValidateEmail() == 1){
                    if(ValidatePhone() == 1){

                        if(txtName.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Please enter your name", Toast.LENGTH_SHORT).show();
                        }
                        else if(txtOrgName.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Please enter your organization name", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            SubmitRegistration();
                        }
                    }
                }
                else{
                   //
                }

            }
        });
        progressBar.setVisibility(View.GONE);

    }

    private int ValidateEmail(){

        int iResult = 0;

        String email = txtEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern))
        {
            iResult =1;
        }
        else
        {
            iResult = 0;
            Toast.makeText(getApplicationContext(),"Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        return iResult;

    }
    private int ValidatePhone() {
        String phone = txtPhone.getText().toString().trim();
        int iResult=0;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                iResult = 0;
                Toast.makeText(getApplicationContext(),"Please enter valid phone number", Toast.LENGTH_SHORT).show();
            } else {
                iResult = 1;
            }
        } else {
            iResult = 0;
            Toast.makeText(getApplicationContext(),"Please enter valid phone number", Toast.LENGTH_SHORT).show();
        }
        return iResult;
    }

    public void SubmitRegistration() {

        String sName = txtName.getText().toString();
        String sOrgName = txtOrgName.getText().toString();
        String sPhone = txtPhone.getText().toString();
        String sEmail = txtEmail.getText().toString();

        String dataURL = "data="+globalVariable.getImei()+","+sName+","+sOrgName+","+sPhone+","+sEmail;

        //Validation of the controls


        AsyncHttpClient client = new AsyncHttpClient();

        if(!ClassUtility.IsConnectedToNetwork(ActivityRegistration.this)){
            Toast.makeText(ActivityRegistration.this, getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL + ClassCommon.SET_REGISTRATION + dataURL;

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        String strResponseCode = responseObj.getString("STATUS");

                        if (strResponseCode.equals(ClassCommon.SUCCESS)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRegistration.this);
                            builder.setMessage(getResources().getString(R.string.thankyou_registration));
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                             Intent intent = new Intent(ActivityRegistration.this, ActivitySlatrack.class);
                             startActivity(intent); //TODO Ideally we need to move to About SLATRACK screen

                                }
                            });

                            builder.show();

                        } else if (strResponseCode.equals(ClassCommon.REGREQUESTRECEIVED)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRegistration.this);
                            String msgString = String.format(getResources().getString(R.string.registration_pending), globalVariable.getSupportphone());
                            builder.setMessage(msgString);

                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(ActivityRegistration.this, ActivitySlatrack.class);
                                    startActivity(intent); //TODO Ideally we need to move to About SLATRACK screen
                                }
                            });

                            builder.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRegistration.this);
                            String msgString = String.format(getResources().getString(R.string.verification_fail), globalVariable.getSupportphone());
                            String errorcode = "ERROR 1003 :";
                            msgString = errorcode + msgString;
                            builder.setMessage(msgString);
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ActivityRegistration.this, ActivitySlatrack.class);
                                    startActivity(intent); //TODO Ideally we need to move to About SLATRACK screen
                                }
                            });
                            builder.show();
                        }

                    } else {

                        ClassCustomDialog cdd=new ClassCustomDialog(ActivityRegistration.this,
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

                ClassCustomDialog cdd=new ClassCustomDialog(ActivityRegistration.this,
                        getResources().getString(R.string.unknown_issue),getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }


}
