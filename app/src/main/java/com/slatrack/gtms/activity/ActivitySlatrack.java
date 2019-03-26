package com.slatrack.gtms.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;
import android.os.AsyncTask;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.provider.Settings;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.fragment.FragmentAboutsla;
import com.slatrack.gtms.fragment.FragmentSubList;
import com.slatrack.gtms.model.ModelSlaParam;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.model.ModelAppInfo;
import com.slatrack.gtms.model.ModelAuditInfo;
import com.slatrack.gtms.model.ModelCheckpoints;
import com.slatrack.gtms.model.ModelEvents;
import com.slatrack.gtms.model.ModelFeatures;
import com.slatrack.gtms.model.ModelLastupdate;
import com.slatrack.gtms.model.ModelOrganization;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.model.ModelSchedule;
import com.slatrack.gtms.model.ModelShift;
import com.slatrack.gtms.model.ModelSosMessage;
import com.slatrack.gtms.model.ModelSubscription;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

import java.io.InputStream;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivitySlatrack extends AppCompatActivity {

    private boolean isOneAttempt;
    private SLAGTMS globalVariable; //  = (SLAGTMS) getApplicationContext();
    private ClassDatabase database;
    private int recordCount = 0;
    PendingIntent mPermissionIntent;
    UsbManager manager;
    private String mSocietyId;
    private String mDeviceImei;
    private ProgressBar progressBar;
    private WebView webView;
    Toolbar topToolBar;
    AppCompatTextView launchMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ActivitySlatrack.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        isOneAttempt = false;
        setContentView(R.layout.activity_slatrack);

        progressBar = (ProgressBar) findViewById(R.id.progressBarSla);
        launchMessage = (AppCompatTextView) findViewById(R.id.launchmessage);


        database = new ClassDatabase(ActivitySlatrack.this);
        globalVariable = (SLAGTMS) getApplicationContext();

        boolean isDataAvailable = PopulateGlobalVariable();
        final boolean isDeviceSynched = IsDeviceSynchedUp();

        // listen for new devices
        IntentFilter filter = new IntentFilter();
        filter.addAction(ClassCommon.IMEIVALIDATIONSUCCESS);
        registerReceiver(mDownloadData, filter);

        //  String sourceString = getResources().getString(R.string.slatrack_text);
        String sourceString = String.format(getResources().getString(R.string.slatrack_text), globalVariable.getSupportphone(), globalVariable.getSupportemail());

        launchMessage.setText(sourceString);
        launchMessage.setMovementMethod(new ScrollingMovementMethod());

        //checkPermissionWRITESETTINGS();
        checkPermissionAndGetIMEI();

        final Button btnValidate = findViewById(R.id.btnValidate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ModelReader objReader = database.getUser();
                if (objReader != null) {
                    if (database.userCount() > 1) {
                        globalVariable.setUser(objReader);
                    }
                    if (globalVariable.getImei() != null) {
                        progressBar.setVisibility(View.VISIBLE);

                        if (ClassUtility.IsConnectedToNetwork(ActivitySlatrack.this)) {
                            ValidateIMEIUser(mDeviceImei);
                        } else {
                            if (isDeviceSynched) {
                                Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.mobiledataoff), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(ActivitySlatrack.this, ActivityWelcome.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.mobiledataoffcannotproceed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } //Endif
                    else {
                        Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.permissionnotgranted), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        loadWebView();

    }

    private void loadWebView() {

        //  webView.loadUrl("file:///android_res/raw/launch.html");

        // webView.loadUrl("http://www.slatrack.com/guide.html");

    }

    public boolean IsDeviceSynchedUp() {

        boolean isglobalset = true;

        ModelLastupdate objLastUpdate = database.getLastupdate();

        if (objLastUpdate != null) {
            if (database.lastupdateCount() > 0) {

                globalVariable.setLastUpdate(objLastUpdate.getLastupdate());

                globalVariable.setDeviceUpdate(objLastUpdate.getDeviceupdate());
                if (objLastUpdate.getDeviceupdate().isEmpty()) {
                    globalVariable.setUpdateNeeded("YES");
                }


                globalVariable.setServerUpdate(objLastUpdate.getServerupdate());


            } else {
                isglobalset = false;
            }
        }

        return isglobalset;
    }


    public boolean PopulateGlobalVariable() {

        boolean isglobalset = true;

        ModelLastupdate objLastUpdate = database.getLastupdate();

        if (objLastUpdate != null) {
            if (database.lastupdateCount() > 0) {

                globalVariable.setLastUpdate(objLastUpdate.getLastupdate());

                globalVariable.setDeviceUpdate(objLastUpdate.getDeviceupdate());

                if (objLastUpdate.getDeviceupdate().isEmpty()) {
                    globalVariable.setUpdateNeeded("YES");
                }
                globalVariable.setServerUpdate(objLastUpdate.getServerupdate());


            } else {
                isglobalset = false;
            }
        }

        ModelOrganization objOrg = database.getOrganization();
        if (objOrg != null) {
            if (database.organizationCount() > 0) {
                globalVariable.setOrganizationName(objOrg.getOrgname());
                globalVariable.setLogoPath(objOrg.getLogopath());
                globalVariable.setIsIMEIValidated(ClassCommon.IMEIVALIDATIONERROR);
            } else {
                isglobalset = false;
            }
        }

        ModelReader objReader = database.getUser();
        if (objReader != null) {
            if (database.userCount() > 0) {
                globalVariable.setUserRole(objReader.getRole());
            } else {
                isglobalset = false;
            }
        }


        ModelAppInfo objAppInfo = database.getAppInfo();
        if (objAppInfo != null) {
            if (database.appInfoCount() > 0) {


                globalVariable.setSupportphone(objAppInfo.getSupportphone());
                globalVariable.setSupportemail(objAppInfo.getSupportemail());

            } else {
                isglobalset = false;
            }
        }

        ModelSlaParam objSlaParam = database.getSlaParam();
        if (objSlaParam != null) {
            if (database.slaParamCount() > 0) {

                globalVariable.setEnc_key(objSlaParam.getEnc_key());
                globalVariable.setEnc_vi(objSlaParam.getEnc_vi());
                globalVariable.setSla_percent(objSlaParam.getSla_percent());

            } else {
                isglobalset = false;
            }
        }


        ModelFeatures objFeatures = database.getFeature();
        if (objFeatures != null) {
            if (database.featuresCount() > 0) {
                globalVariable.setScanningMode(objFeatures.getScan_mode());
                if(objFeatures.getScan_mode() !=null){
                    if(objFeatures.getScan_mode().equalsIgnoreCase(ClassCommon.QRMODE)) {
                        globalVariable.setIsReaderConnected("YES");
                    }
                }
            } else {
                isglobalset = false;
            }
        }

        return isglobalset;
    }

    @Override
    public void onDestroy() {

        try {
            if (mDownloadData != null)
                unregisterReceiver(mDownloadData);

        } catch (Exception e) {
        }

        super.onDestroy();
    }


    private final BroadcastReceiver mDownloadData = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.getAction() != null) {
                if (ClassCommon.IMEIVALIDATIONSUCCESS.equalsIgnoreCase(action)) {

                    if (globalVariable.getIsIMEIValidated() != null) {
                        if (globalVariable.getIsIMEIValidated().equalsIgnoreCase(ClassCommon.IMEIVALIDATIONSUCCESS)) {
                            progressBar.setVisibility(View.VISIBLE);
                            DownloadOrganizationData(globalVariable.getImei());


                        }
                    }

                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {

            if (intent.getAction() != null) {

                if (intent.getAction().equalsIgnoreCase(ClassCommon.IMEIVALIDATIONSUCCESS)) {

                    if (globalVariable.getIsIMEIValidated() != null) {
                        if (globalVariable.getIsIMEIValidated().equalsIgnoreCase(ClassCommon.IMEIVALIDATIONSUCCESS)) {
                            progressBar.setVisibility(View.VISIBLE);
                            DownloadOrganizationData(globalVariable.getImei());
                        }
                    }

                }
            }
        }
    }

    private void checkPermissionAndGetIMEI() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    101);
        } else {
            try {
                // TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                //String imei = tel.getDeviceId().toString();
                String imei = getIMEINo();
                globalVariable.setImei(imei);
                mDeviceImei = imei;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }



    public String getIMEINo() {

//        return "357277081038955";

        try {
            String imeiNo = "";

            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                imeiNo = tm.getDeviceId();
            }
            if (imeiNo == null || imeiNo.length() == 0) {
                imeiNo = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return imeiNo;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
                        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String imei = getIMEINo();
                        globalVariable.setImei(imei);
                        mDeviceImei = imei;
                        //TODO call service
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                                finish();
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

    public void ValidateIMEIUser(String imei) {


        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(ActivitySlatrack.this)) {
            Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL + ClassCommon.GET_USERDETAILS + "data=" + imei;

        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

                progressBar.setVisibility(View.GONE);
                if (globalVariable.getIsIMEIValidated().equalsIgnoreCase(ClassCommon.IMEIVALIDATIONSUCCESS)) {
                    if (globalVariable.getUpdateNeeded().equalsIgnoreCase("YES")) {
                        DownloadOrganizationData(globalVariable.getImei());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(ActivitySlatrack.this, ActivityWelcome.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        String strResponseCode = responseObj.getString("STATUS");
                        if (strResponseCode.equals(ClassCommon.SUBSCRIPTIONEXPIRED)) {

                            globalVariable.setIsIMEIValidated(ClassCommon.SUBSCRIPTIONEXPIRED);
                            Intent intent = new Intent(ActivitySlatrack.this, ActivitySubList.class);
                            startActivity(intent);
                            finish();

                        } else if (strResponseCode.equals(ClassCommon.SUCCESS)) {

                            JSONArray resultArr = responseObj.getJSONArray("RESULT");
                            if (resultArr.length() > 0) {
                                JSONObject resultObj = resultArr.getJSONObject(0);
                                if (resultObj != null) {
                                    if (globalVariable.getUpdateNeeded().equalsIgnoreCase("YES")) {

                                        UpdateUserData(resultObj);
                                    } else {
                                        if (database.userCount() == 0) {
                                            UpdateUserData(resultObj);
                                        } else {
                                            ModelReader currentReader = database.getUser();
                                            ModelOrganization orgData = database.getOrganization();
                                            globalVariable.setReaderCode(currentReader.getReadercode());
                                            globalVariable.setLogoPath(orgData.getLogopath());
                                        }
                                    }
                                    globalVariable.setIsIMEIValidated(ClassCommon.IMEIVALIDATIONSUCCESS);
                                } else {
                                    globalVariable.setIsIMEIValidated(ClassCommon.IMEIVALIDATIONERROR);
                                }


                            }
                        } else if (strResponseCode.equals(ClassCommon.REGREQUESTRECEIVED)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                            String msgString = String.format(getResources().getString(R.string.registration_pending), globalVariable.getSupportphone());
                            builder.setMessage(msgString);
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    finish();
                                    globalVariable.setIsIMEIValidated(ClassCommon.REGREQUESTRECEIVED);
                                    Intent intent = new Intent(ActivitySlatrack.this, FragmentAboutsla.class);
                                    startActivity(intent);

                                }
                            });

                            builder.show();
                        } else {
                            // progressBar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                            String msgString = String.format(getResources().getString(R.string.verification_fail), globalVariable.getSupportphone());
                            String errorcode = "ERROR 1011 :";
                            msgString = errorcode + msgString;
                            builder.setMessage(msgString);
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    globalVariable.setIsIMEIValidated(ClassCommon.IMEIVALIDATIONERROR);
                                    finish();

                                }
                            });
                            builder.show();
                        }

                    } else {

                        String strResponseCode = responseObj.getString("STATUS");

                        if (strResponseCode.equals(ClassCommon.USERNOTREGISTERED)) {
                            // progressBar.setVisibility(View.GONE);
                            globalVariable.setIsIMEIValidated(ClassCommon.USERNOTREGISTERED);
                            Intent intent = new Intent(ActivitySlatrack.this, ActivityRegistration.class);
                            startActivity(intent);


                        } else {
                            // progressBar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                            String msgString = String.format(getResources().getString(R.string.unknown_issue), globalVariable.getSupportphone());
                            builder.setMessage(msgString);
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    globalVariable.setIsIMEIValidated(ClassCommon.IMEIVALIDATIONERROR);

                                }
                            });

                            builder.show();
                        }
                    }


                } catch (Exception e) {

                    progressBar.setVisibility(View.GONE);


                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        };
        client.get(configURL, handler);

    }


    private String CreateAuditString(String imei) {
        String uploadString = "";

        ModelAuditInfo objAudit = database.getAuditInfo();
        if (objAudit != null) {
            if (database.getAuditInfoCount() > 0) {

                uploadString = imei + "," + objAudit.getLastlogintime() + "," + objAudit.getLastvalidationdate() + "," + objAudit.getLatestupdate();
                uploadString += "," + objAudit.getTotalrecordsupdated() + "," + objAudit.getFirstinstalldate() + "," + objAudit.getLastsynchronizationtime();
                uploadString += "," + objAudit.getLastregrequested() + "," + objAudit.getLastsubrequested();

            }

        }


        return uploadString;

    }

    public void UploadAuditInfo() {

        String uploadString = CreateAuditString(globalVariable.getImei());

        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(ActivitySlatrack.this)) {
            Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL + ClassCommon.SET_AUDITINFO + "data=" + uploadString;
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        String strResponseCode = responseObj.getString("STATUS");
                        if (strResponseCode.equals(ClassCommon.SUCCESS)) {
                            //NO ACTION
                        }
                    } else {

                        String strResponseCode = responseObj.getString("STATUS");
                        if (strResponseCode.equals(ClassCommon.USERNOTREGISTERED)) {
                            //NO ACTION
                        }
                    }

                } catch (Exception e) {

                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                // progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                String msgString = String.format(getResources().getString(R.string.unknown_issue), globalVariable.getSupportphone());
                builder.setMessage(msgString);
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        };
        client.get(configURL, handler);
    }

    private void UpdateUserData(JSONObject resultObj) {

        // final ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);

        try {

            database.deleteAllUsers();
            database.deleteAllOrganization();

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
            // String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            //Organization Data
            String orgname = resultObj.getString("name");
            String enablesms = resultObj.getString("enablesms");
            String enableemail = resultObj.getString("enableemail");
            String isverified = resultObj.getString("isverified");
            String primarysos = resultObj.getString("primarysos");
            String updatemode = resultObj.getString("updatemode");
            String secmobno = resultObj.getString("secmobno");
            String subtype = resultObj.getString("subtype");
            String factype = resultObj.getString("factype");
            String logopath = resultObj.getString("logopath");

            String refresh_required = resultObj.getString("refresh_required");
            String rd_eventcode = resultObj.getString("eventcode");



            if(refresh_required.equalsIgnoreCase("YES")){
                globalVariable.setUpdateNeeded("YES");
            }
//            else{
//                globalVariable.setUpdateNeeded("NO");
//            }

            globalVariable.setEventcode(rd_eventcode);

            //RD.subtype,RD.substatus,RD.subname,RD.readerstatus

            String substatus = resultObj.getString("substatus");
            String readerstatus = resultObj.getString("readerstatus");
            String subname = resultObj.getString("subname");
            String orgstatus = resultObj.getString("status");


            // String refUserId = "";
            ModelReader refReader = database.getUser();
            if (refReader != null) {
                if (database.userCount() > 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                    builder.setMessage(getResources().getString(R.string.multiple_user));
                    builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            while (database.userCount() > 0) {
                                ModelReader objReader = database.getUser();
                                UnMapUser(objReader.getImei1());
                                UnMapUser(objReader.getImei2());
                                database.deleteUser(objReader.getUser_id());
                            }
                            dialogInterface.dismiss();
                            finish();

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
            //  objReader.setSociety_id(society_id);
            objReader.setReadercode(readercode);
            objReader.setExpiryDate(expiry_date);
            objReader.setOrganization_id(organization_id);
            objReader.setFacility_id(facility_id);

            objReader.setSubtype(subtype);
            objReader.setSubstatus(substatus);
            objReader.setSubname(subname);
            objReader.setReaderstatus(readerstatus);

            database.insertUser(objReader);

//            if (user_id.equals(refReader.getUser_id())) {
//                database.updateUser(objReader);
//            } else {
//

//            }

            globalVariable.setReaderCode(readercode);
            globalVariable.setLogoPath(logopath);

            ModelOrganization objOrganization = new ModelOrganization();

            //Update Organization Data


            objOrganization.setOrgname(name);
            objOrganization.setUser_id(user_id);
            //  objOrganization.setSociety_id(society_id);
            objOrganization.setExpiry_date(expiry_date);
            objOrganization.setEnablesms(enablesms);
            objOrganization.setEnableemail(enableemail);
            objOrganization.setIsverified(isverified);
            objOrganization.setPrimarysos(primarysos);
            objOrganization.setUpdatemode(updatemode);
            objOrganization.setSecmobno(secmobno);
            objOrganization.setSubtype(subtype);
            objOrganization.setSocietytype(factype);
            objOrganization.setLogopath(logopath);
            objOrganization.setOrganizationId(organization_id);
            objOrganization.setFacilityId(facility_id);
            objOrganization.setSubstatus(substatus);
            objOrganization.setOrgstatus(orgstatus);

            database.insertOrganization(objOrganization);

//           if(database.organizationCount() > 1) {
//               database.deleteAllOrganization();
//               database.insertOrganization(objOrganization);
//           }
//           else{
//
//           }

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());
        }

    }

    private void InserUserData(JSONObject resultObj) {

        final ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);

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

            String org_id = resultObj.getString("organization_id");
            String fac_id = resultObj.getString("facility_id");
            String subtype = resultObj.getString("subtype");
            String substatus = resultObj.getString("substatus");
            String readerstatus = resultObj.getString("readerstatus");
            String subname = resultObj.getString("subname");
            String orgstatus = resultObj.getString("status");


            // String refUserId = "";
            ModelReader refReader = database.getUser();
            if (refReader != null) {
                if (database.userCount() > 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                    builder.setMessage(getResources().getString(R.string.multiple_user));
                    builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            while (database.userCount() > 0) {
                                ModelReader objReader = database.getUser();
                                UnMapUser(objReader.getImei1());
                                UnMapUser(objReader.getImei2());
                                database.deleteUser(objReader.getUser_id());
                            }
                            dialogInterface.dismiss();
                            finish();

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
            objReader.setExpiryDate(expiry_date);

            objReader.setOrganization_id(org_id);
            objReader.setFacility_id(fac_id);

            objReader.setSubtype(subtype);
            objReader.setSubstatus(substatus);
            objReader.setSubname(subname);
            objReader.setReaderstatus(readerstatus);

            if (user_id.equals(refReader.getUser_id())) {
                database.updateUser(objReader);
            } else {

                database.insertUser(objReader);
            }
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());
        }

    }

    private void onComplete() {
        //show success image and btn
        // placeCardText.setVisibility(View.GONE);
    }

    public void UnMapUser(String imei) {

        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(ActivitySlatrack.this)) {
            Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL + ClassCommon.SET_UNMAPUSER + "data=" + imei;
        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        String strResponseCode = responseObj.getString("STATUS");

                        if (strResponseCode.equals(ClassCommon.SUCCESS)) {
                            //  Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.unmapSuccess), Toast.LENGTH_SHORT).show();
                            onComplete();
                        }

                    } else {
                        Toast.makeText(ActivitySlatrack.this, getResources().getString(R.string.unmapFailure), Toast.LENGTH_SHORT).show();
                        onComplete();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                builder.setMessage(getResources().getString(R.string.server_issue));
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

                builder.show();
            }
        });
    }

    //TODO - Can this moved to the common file...

    public void DownloadOrganizationData(String strImei) {

        AsyncHttpClient client1 = new AsyncHttpClient();

        String configURL1 = ClassCommon.BASE_URL + ClassCommon.GET_ALLDATA + "data=" + strImei;

        AsyncHttpResponseHandler handler1 = new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // Toast.makeText(ActivitySlatrack.this, "started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {

                //Download Organization Logo and save to the file as orglogo.png
                //Toast.makeText(ActivitySlatrack.this, "finished", Toast.LENGTH_SHORT).show();
                ModelOrganization orginfo = database.getOrganization();
                if (orginfo != null) {
                    if (database.organizationCount() > 0) {
                        if (orginfo.getLogopath().isEmpty()) {
                            new DownloadOrgLogo().execute(ClassCommon.DEFAULT_LOGOPATH);
                        } else {
                            new DownloadOrgLogo().execute(orginfo.getLogopath());
                        }
                    }
                } else {
                    new DownloadOrgLogo().execute(ClassCommon.DEFAULT_LOGOPATH);
                }

                //     UploadAuditInfo();
                progressBar.setVisibility(View.GONE);
                globalVariable.setUpdateNeeded("NO");
                Intent intent = new Intent(ActivitySlatrack.this, ActivityWelcome.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

//                    Toast.makeText(ActivitySlatrack.this, "Success", Toast.LENGTH_SHORT).show();
                    String response = new String(responseBody, "UTF-8");
                    Log.e("res",response);
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        JSONArray resultArr = responseObj.getJSONArray("LASTUPDATE");

                        if (resultArr.length() > 0) {

                            JSONObject resultObj = resultArr.getJSONObject(0);
                            int NeedToUpdate = ValidateLastUpdate(resultObj);
                            if (NeedToUpdate > 0) {
                                globalVariable.setUpdateNeeded("YES");

                                JSONArray appinfoArr = responseObj.getJSONArray("APPINFO");
                                if (appinfoArr.length() > 0) {
                                    //  Toast.makeText(ActivitySlatrack.this, "Receied App Info", Toast.LENGTH_SHORT).show();
                                    database.deleteAllAllInfo();
                                    // Toast.makeText(ActivitySlatrack.this, "App Info deleted", Toast.LENGTH_SHORT).show();
                                    for (int objCount = 0; objCount < appinfoArr.length(); objCount++) {
                                        JSONObject appinfoObj = appinfoArr.getJSONObject(objCount);
                                        if (appinfoObj != null) {
                                            // Toast.makeText(ActivitySlatrack.this, "Before app info update", Toast.LENGTH_SHORT).show();
                                            UpdateAppInfoData(appinfoObj);
                                            // Toast.makeText(ActivitySlatrack.this, "After app info update", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                JSONArray featuresArr = responseObj.getJSONArray("FEATURES");
                                if (featuresArr.length() > 0) {

                                    database.deleteAllFeatures();
                                    for (int objCount = 0; objCount < featuresArr.length(); objCount++) {
                                        JSONObject featureObj = featuresArr.getJSONObject(objCount);
                                        if (featureObj != null) {
                                            UpdateFeaturesData(featureObj);
                                        }
                                    }

                                }

                                JSONArray checkPointsArr = responseObj.getJSONArray("CHECKPOINTS");
                                if (checkPointsArr.length() > 0) {
                                    database.deleteAllCheckpoints();
                                    for (int objCount = 0; objCount < checkPointsArr.length(); objCount++) {
                                        JSONObject checkpointObj = checkPointsArr.getJSONObject(objCount);
                                        if (checkpointObj != null) {
                                            UpdateCheckpointData(checkpointObj);
                                        }
                                    }
                                }

                                JSONArray eventsArr = responseObj.getJSONArray("EVENTS");
                                if (eventsArr.length() > 0) {
                                    database.deleteAllEvents();
                                    for (int objCount = 0; objCount < eventsArr.length(); objCount++) {
                                        JSONObject eventsObj = eventsArr.getJSONObject(objCount);
                                        if (eventsObj != null) {
                                            UpdateEventsData(eventsObj);
                                        }
                                    }

                                }

                                JSONArray shiftsArr = responseObj.getJSONArray("SHIFTS");
                                if (shiftsArr.length() > 0) {
                                    database.deleteAllShift();
                                    for (int objCount = 0; objCount < shiftsArr.length(); objCount++) {
                                        JSONObject shiftsObj = shiftsArr.getJSONObject(objCount);
                                        if (shiftsObj != null) {
                                            UpdateShiftData(shiftsObj);
                                        }
                                    }

                                }

                                JSONArray scheduleArr = responseObj.getJSONArray("SCHEDULES");
                                if (scheduleArr.length() > 0) {
                                    database.deleteAllSchedule();
                                    for (int objCount = 0; objCount < scheduleArr.length(); objCount++) {
                                        JSONObject scheduleObj = scheduleArr.getJSONObject(objCount);
                                        if (scheduleObj != null) {
                                            UpdateScheduleData(scheduleObj);
                                        }
                                    }

                                }

                                JSONArray sosMsgArr = responseObj.getJSONArray("SOSMESSAGES");
                                if (sosMsgArr.length() > 0) {
                                    database.deleteAllSOSMessage();
                                    for (int objCount = 0; objCount < sosMsgArr.length(); objCount++) {
                                        JSONObject sosMsgObj = sosMsgArr.getJSONObject(objCount);
                                        if (sosMsgObj != null) {
                                            UpdateSosMessageData(sosMsgObj);
                                        }
                                    }

                                }

                                JSONArray subscriptionArr = responseObj.getJSONArray("SUBSCRIPTION");
                                if (subscriptionArr.length() > 0) {
                                    database.deleteAllSubscriptionRates();
                                    for (int objCount = 0; objCount < subscriptionArr.length(); objCount++) {
                                        JSONObject subratesObj = subscriptionArr.getJSONObject(objCount);
                                        if (subratesObj != null) {
                                            UpdateSubScriptionRates(subratesObj);
                                        }
                                    }

                                }

                            } else {
                                globalVariable.setUpdateNeeded("NO");
                            }

                            progressBar.setVisibility(View.GONE);

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                            String msgString = String.format(getResources().getString(R.string.verification_fail), globalVariable.getSupportphone());
                            String errorcode = "ERROR 1012 :";
                            msgString = errorcode + msgString;
                            builder.setMessage(msgString);
                            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            });

                            builder.show();
                            // progressBar.setVisibility(View.GONE);
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                        String msgString = String.format(getResources().getString(R.string.verification_fail), globalVariable.getSupportphone());
                        String errorcode = "ERROR 1013 :";
                        msgString = errorcode + msgString;
                        builder.setMessage(msgString);
                        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();

                            }
                        });

                        builder.show();
                        //progressBar.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    //progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                if (error.getCause() instanceof ConnectTimeoutException) {

                    Toast.makeText(ActivitySlatrack.this, "Connection timeout", Toast.LENGTH_SHORT).show();
                }

                //  progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySlatrack.this);
                builder.setMessage(getResources().getString(R.string.server_issue));
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                builder.show();
            }


        };
        client1.get(configURL1, handler1);
    }

    public void UpdateAppInfoData(JSONObject resultObj) {

        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
        try {


            String _id = resultObj.getString("id");
            String _appversion = resultObj.getString("appversion");
            String _apptype = resultObj.getString("apptype");
            String _supportphone = resultObj.getString("supportphone");
            String _supportemail = resultObj.getString("supportemail");


            ModelAppInfo objAppInfo = new ModelAppInfo();

            objAppInfo.setId(_id);
            objAppInfo.setAppversion(_appversion);
            objAppInfo.setApptype(_apptype);
            objAppInfo.setSupportemail(_supportemail);
            objAppInfo.setSupportphone(_supportphone);

            // database.deleteAllAllInfo();
            database.insertAppInfo(objAppInfo);

            UpdateAppInfoToGlobal();

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    public void UpdateAppInfoToGlobal() {

        ModelAppInfo objAppInfo = database.getAppInfo();

        globalVariable.setAppversion(objAppInfo.getAppversion());
        globalVariable.setApptype(objAppInfo.getApptype());
        globalVariable.setSupportemail(objAppInfo.getSupportemail());
        globalVariable.setSupportphone(objAppInfo.getSupportphone());
    }

    public void UpdateSubScriptionRates(JSONObject resultObj) {
        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
        try {

            String subid = resultObj.getString("id");
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

    public int ValidateLastUpdate(JSONObject resultObj) {

        int UpdatedNeeded = 0;
        int serverUpdate = 0;
        int mobileUpdate = 0;
        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
        try {

            String currVersion = "0";
            String id = resultObj.getString("id");
            String society_id = resultObj.getString("society_id");
            String lastupdate = resultObj.getString("lastupdate");
            String needupdate = resultObj.getString("needupdate");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");

            ModelLastupdate currentUpdate = database.getLastupdate();
            if (currentUpdate != null) {
                if (currentUpdate.getLastupdate() != null) {
                    currVersion = currentUpdate.getNeedupdate();
                }
            }

            try {
                serverUpdate = Integer.parseInt(needupdate);
                mobileUpdate = Integer.parseInt(currVersion);
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }


            if (serverUpdate > mobileUpdate) {
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
            } else {
                UpdatedNeeded = 0;
            }

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }
        return UpdatedNeeded;
    }

    public void UpdateFeaturesData(JSONObject resultObj) {
        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
        try {

            String enable_camera = resultObj.getString("enable_camera");
            String enable_gps = resultObj.getString("enable_gps");
            String enable_sig = resultObj.getString("enable_sig");
            String enable_selfie = resultObj.getString("enable_selfie");
            String enable_overide = resultObj.getString("enable_override");
            String scan_mode = resultObj.getString("scan_mode");
            String user_id = "0"; //TODO in future, please add the user_id mapping
            String society_id = resultObj.getString("society_id");
            String organization_id = resultObj.getString("organization_id");
            String facility_id = resultObj.getString("facility_id");


            ModelFeatures featuresUpdate = new ModelFeatures();
            featuresUpdate.setEnable_camera(enable_camera);
            featuresUpdate.setEnable_gps(enable_gps);
            featuresUpdate.setEnable_sig(enable_sig);
            featuresUpdate.setEnable_selfie(enable_selfie);
            featuresUpdate.setEnable_overide(enable_overide);
            featuresUpdate.setScan_mode(scan_mode);
            featuresUpdate.setUser_id(user_id);
            featuresUpdate.setSociety_id(society_id);
            featuresUpdate.setOrganization_id(organization_id);
            featuresUpdate.setFacility_id(facility_id);

            database.insertFeatures(featuresUpdate);
            globalVariable.setScanningMode(scan_mode);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());

        }

    }

    public void UpdateCheckpointData(JSONObject resultObj) {


        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
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

    public void UpdateShiftData(JSONObject resultObj) {


        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
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

    public void UpdateScheduleData(JSONObject resultObj) {

        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
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

    public void UpdateEventsData(JSONObject resultObj) {


        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
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

    public void UpdateSosMessageData(JSONObject sosMsgObject) {

        ClassDatabase database = new ClassDatabase(ActivitySlatrack.this);
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

    private class DownloadAllOrgInformation extends AsyncTask<String, String, String> {
        ProgressBar pb;


        @Override
        protected String doInBackground(String... urls) {

            // DownloadOrganizationData(globalVariableOld.getImei());
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pb = (ProgressBar) findViewById(R.id.progressBarSla);
            pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String result) {

            pb = (ProgressBar) findViewById(R.id.progressBarSla);
            pb.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private class DownloadOrgLogo extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getApplicationContext(), result, ClassCommon.ORGLOGO_NAME);
        }
    }

    public void saveImage(Context context, Bitmap b, String imageName) {


        File file = getApplicationContext().getFileStreamPath(ClassCommon.ORGLOGO_NAME);
        if (file.exists()) {
            if (file.delete()) {
                FileOutputStream foStream;
                try {
                    foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
                    b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
                    foStream.close();
                } catch (Exception e) {
                    Log.d("saveImage", "Exception 2, Something went wrong!");
                    e.printStackTrace();
                }
            }
        } else {
            FileOutputStream foStream;
            try {
                foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
                b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
                foStream.close();
            } catch (Exception e) {
                Log.d("saveImage", "Exception 2, Something went wrong!");
                e.printStackTrace();
            }
        }


    }

}


