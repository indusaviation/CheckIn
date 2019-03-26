package com.slatrack.gtms.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.adapter.AdapterTodoListDone;
import com.slatrack.gtms.adapter.AdapterTodoListPending;
import com.slatrack.gtms.adapter.AdapterTransactions;
import com.slatrack.gtms.fragment.FragmentDiagnostics;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.fragment.FragmentSosmessage;
import com.slatrack.gtms.fragment.FragmentSubList;
import com.slatrack.gtms.fragment.FragmentSupport;
import com.slatrack.gtms.model.ModelDoneToDoList;
import com.slatrack.gtms.model.ModelPendingToDoList;
import com.slatrack.gtms.utils.ClassBottomNVFragment;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelOrganization;
import com.slatrack.gtms.model.ModelPunchReport;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.model.ModelUser;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentTxnReport extends Fragment {


    View rootView;

    public ClassDatabase database;
    public SLAGTMS globalVariable;
    ActivityWelcome activityWelcome;

    public ProgressBar progressBar;
    public ListView listTransaction;
    private AppCompatTextView noHistoryText;

    private LinearLayout pendingDoneLayout;
    private SwitchCompat switchTodo;

    private int pageIndex = 0, pageSize = 0;

    private AppCompatTextView orgName, userName, subDate, subType, orgStatus, subStatus;
//    AppCompatButton btnRefresh;

    private ArrayList<ModelPunchReport> modelTransactionHistoryArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_txnsummary, container, false);

        activityWelcome = (ActivityWelcome) getActivity();
        listTransaction = (ListView) rootView.findViewById(R.id.listTransactions);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarTxn);

        noHistoryText = (AppCompatTextView) rootView.findViewById(R.id.noHistoryText);
        pendingDoneLayout = (LinearLayout) rootView.findViewById(R.id.pendingDone);
        switchTodo = (SwitchCompat) rootView.findViewById(R.id.switchTodo);
//        orgName = (AppCompatTextView) findViewById(R.id.txnorgname);
//        userName = (AppCompatTextView) findViewById(R.id.txnusername);
//        subDate = (AppCompatTextView) findViewById(R.id.txnsubdate);
//        subType = (AppCompatTextView) findViewById(R.id.txnsubtype);
//        orgStatus = (AppCompatTextView) findViewById(R.id.txnorgstatus);
//        subStatus = (AppCompatTextView) findViewById(R.id.txnsubstatus);
//        btnRefresh = (AppCompatButton) rootView.findViewById(R.id.refresh);

        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        database = new ClassDatabase(activityWelcome);


        if (database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")) {
            pendingDoneLayout.setVisibility(View.VISIBLE);
            getToDoList(true);
        } else {
            getComplianceSummary();
        }


        progressBar.setVisibility(View.GONE);

        switchTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getToDoList(!b);
            }
        });

        return rootView;

    }


    public void refresh() {
        getComplianceSummary();
        getRefreshOrgData();
    }


    private void getComplianceSummary() {

        progressBar.setVisibility(View.VISIBLE);
        listTransaction.setAdapter(null);
        listTransaction.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String imei = globalVariable.getImei();

        String configURL = "";
        if (database.getOrganization().getSubtype().equalsIgnoreCase("EVENTS")) {

            configURL = ClassCommon.BASE_URL + ClassCommon.GET_TODOMONTHTXN + "data=" + imei;
        } else {
            configURL = ClassCommon.BASE_URL + ClassCommon.GET_MONTHTXN + "data=" + imei;
        }


        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        JSONArray resultArr = responseObj.getJSONArray("RESULT");

                        if (resultArr.length() > 0) {
                            modelTransactionHistoryArrayList = new ArrayList<>();
                            for (int index = 0; index < resultArr.length(); index++) {
                                JSONObject resultObj = resultArr.getJSONObject(index);

                                //{"Date":"11\/09\/2018","TotalSwipe":206,"TotalMissed":10,"TotalExpected":216,"SwipePercentage":"95.37"},
                                String swipedate = resultObj.getString("Date");
                                String totalswipe = resultObj.getString("TotalSwipe");
                                String totalmissed = resultObj.getString("TotalMissed");
                                String totalexpected = resultObj.getString("TotalExpected");
                                String swipepercent = resultObj.getString("SwipePercentage");


                                ModelPunchReport transactionHistory = new ModelPunchReport();
                                transactionHistory.setDate(swipedate);
                                transactionHistory.setTotalSwipe(totalswipe);
                                transactionHistory.setTotalMissed(totalmissed);
                                transactionHistory.setTotalExpected(totalexpected);
                                transactionHistory.setSwipePercentage(swipepercent);


                                modelTransactionHistoryArrayList.add(transactionHistory);
                            }
                            AdapterTransactions adapterTransactions = new AdapterTransactions(activityWelcome, modelTransactionHistoryArrayList);

                            listTransaction.setAdapter(adapterTransactions);
                            listTransaction.setVisibility(View.VISIBLE);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            noHistoryText.setVisibility(View.VISIBLE);
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        noHistoryText.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    noHistoryText.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);
                ClassCustomDialog cdd = new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue), getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getToDoList(final boolean isPending) {

        progressBar.setVisibility(View.VISIBLE);
        listTransaction.setAdapter(null);
        listTransaction.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String imei = globalVariable.getImei();

        String configURL = "";

        if (isPending)
            configURL = ClassCommon.BASE_URL + ClassCommon.GET_TODOPENDINGTXN + "data=" + imei + "," + pageIndex + "," + pageSize;
        else
            configURL = ClassCommon.BASE_URL + ClassCommon.GET_TODODONETXN + "data=" + imei + "," + pageIndex + "," + pageSize;

        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");

                    Gson gson = new Gson();

                    if (isPending) {
                        ModelPendingToDoList modelPendingToDoList = gson.fromJson(response, ModelPendingToDoList.class);
                        if (!modelPendingToDoList.isERROR()) {

                            AdapterTodoListPending adapterTodoListPending = new AdapterTodoListPending(activityWelcome, modelPendingToDoList.getRESULT());

                            listTransaction.setAdapter(adapterTodoListPending);
                            listTransaction.setVisibility(View.VISIBLE);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            noHistoryText.setVisibility(View.VISIBLE);

                        }

                    } else {
                        ModelDoneToDoList modelDoneToDoList = gson.fromJson(response, ModelDoneToDoList.class);
                        if (!modelDoneToDoList.isERROR()) {


                            AdapterTodoListDone adapterTodoListDone = new AdapterTodoListDone(activityWelcome, modelDoneToDoList.getRESULT());

                            listTransaction.setAdapter(adapterTodoListDone);
                            listTransaction.setVisibility(View.VISIBLE);


                        } else {
                            progressBar.setVisibility(View.GONE);
                            noHistoryText.setVisibility(View.VISIBLE);

                        }

                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    noHistoryText.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Log.e("exception", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);
                ClassCustomDialog cdd = new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue), getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getRefreshOrgData() {

        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String imei = globalVariable.getImei();
        String configURL = ClassCommon.BASE_URL + ClassCommon.GET_CONFIG + "data=" + imei;
        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                        JSONArray resultArr = responseObj.getJSONArray("RESULT");
                        if (resultArr.length() > 0) {

                            JSONObject resultObj = resultArr.getJSONObject(0);
                            InserUserData(resultObj);
                        } else {

                            ClassCustomDialog cdd = new ClassCustomDialog(activityWelcome,
                                    getResources().getString(R.string.verification_fail), getResources().getString(R.string.msg_title_information));
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            cdd.show();

                        }

                    } else {
                        ClassCustomDialog cdd = new ClassCustomDialog(activityWelcome,
                                getResources().getString(R.string.verification_fail), getResources().getString(R.string.msg_title_information));
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
                ClassCustomDialog cdd = new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue), getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }

    private void InserUserData(JSONObject resultObj) {

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(activityWelcome);
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


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.e("exception", "" + e.getMessage());
        }

    }

    public void UnMapUser(String imei) {

        AsyncHttpClient client = new AsyncHttpClient();

        if (!ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.nonetwork_mobiledataoff), Toast.LENGTH_SHORT).show();
            return;
        }

        String configURL = ClassCommon.BASE_URL + ClassCommon.SET_UNMAPUSER + "data=" + imei;
        client.get(configURL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    progressBar.setVisibility(View.GONE);
                    String response = new String(responseBody, "UTF-8");
                    JSONObject responseObj = new JSONObject(response);

                    if (!responseObj.getBoolean("ERROR")) {

                    } else {

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

                ClassCustomDialog cdd = new ClassCustomDialog(activityWelcome,
                        getResources().getString(R.string.server_issue), getResources().getString(R.string.msg_title_information));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });
    }


}
