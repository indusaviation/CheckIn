package com.slatrack.gtms.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.slatrack.gtms.fragment.FragmentAboutsla;
import com.slatrack.gtms.fragment.FragmentChartData;
import com.slatrack.gtms.fragment.FragmentDiagnostics;
import com.slatrack.gtms.fragment.FragmentGuide;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.fragment.FragmentSosmessage;
import com.slatrack.gtms.fragment.FragmentSubList;
import com.slatrack.gtms.fragment.FragmentSupport;
import com.slatrack.gtms.fragment.FragmentDashboard;
import com.slatrack.gtms.R;
import com.slatrack.gtms.fragment.FragmentTxnReport;
import com.slatrack.gtms.utils.ClassCommon;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.utils.ClassRdStatusObservable;
import com.slatrack.gtms.utils.SLAGTMS;

public class ActivityWelcome extends AppCompatActivity {

    private boolean isOneAttempt;
    private ProgressBar progressBar;
    private AppCompatTextView txtRecCount;
    private AppCompatTextView txtTotalSwipeCount;
    private AppCompatTextView txtTotalMissedCount;
    private AppCompatTextView txtTotalPercentageCount;
    private AppCompatTextView txtTotalExpectedCount;

    private static final String ACTION_USB_PERMISSION = "com.slatrack.checkin.USB_PERMISSION";
    private ImageView imgOrgLogo;
    Toolbar topToolBar;

    public ClassDatabase database;
    public SLAGTMS globalVariable;
    boolean doubleBackToExitPressedOnce = false;


    private Bitmap mBitmap;
    private ClassRdStatusObservable objRdStatus;

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;


//    private AppCompatTextView orgName,userName,subDate,subType,orgStatus,subStatus,noHistoryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        isOneAttempt = false;

        topToolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.sla_logo);

        progressBar = (ProgressBar) findViewById(R.id.progressBarWelcome);
        txtRecCount = (AppCompatTextView) findViewById(R.id.recCount);

        objRdStatus = new ClassRdStatusObservable();
        objRdStatus.setRdstatus("NO");


        database = new ClassDatabase(ActivityWelcome.this);
        globalVariable = (SLAGTMS) getApplicationContext();

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_nv);
        bnv.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        getSelectedBottomItemView(item);
                        return false;
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabwelcome);
        FloatingActionButton fabChart = (FloatingActionButton) findViewById(R.id.fabChart);

        if (globalVariable.getUserRole().equalsIgnoreCase(ClassCommon.PREMISEADMIN)) {
            fab.setVisibility(View.GONE);
            fabChart.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
            fabChart.setVisibility(View.GONE);
        }

        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(globalVariable.getScanningMode().equalsIgnoreCase(ClassCommon.QRMODE)){

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    FragmentScan scanfragment = new FragmentScan();
                    ft.addToBackStack(ClassCommon.FRAG_CHECKPOINT_LIST);
                    ft.replace(R.id.fragmentContainer, scanfragment);
                    ft.commit();
                }
                else {
                  //  if (globalVariable.getIsReaderConnected().equalsIgnoreCase("YES")) {

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        FragmentScan scanfragment = new FragmentScan();
                        ft.addToBackStack(ClassCommon.FRAG_CHECKPOINT_LIST);
                        ft.replace(R.id.fragmentContainer, scanfragment);
                        ft.commit();
                  //  }

//                    else {
//
//                        ClassCustomDialog cdd = new ClassCustomDialog(ActivityWelcome.this,
//                                getResources().getString(R.string.reader_notconnected), getResources().getString(R.string.msg_title_information));
//                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        cdd.show();
//
//                        if (getFragmentRefreshListener() != null) {
//                            getFragmentRefreshListener().onRefresh();
//                        }
//                    }
                }


            }
        });

        fabChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentChartData fragmentChartData = new FragmentChartData();
                ft.addToBackStack(ClassCommon.FRAG_TXNSUMMARY);
                ft.replace(R.id.fragmentContainer, fragmentChartData);
                ft.commit();

            }
        });


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentDashboard dashboard = new FragmentDashboard();
        ft.addToBackStack(ClassCommon.FRAG_DASHBOARD);
        ft.replace(R.id.fragmentContainer, dashboard);
        ft.commit();

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);

    }

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            globalVariable.setIsReaderConnected("YES");
                            Toast.makeText(context, "Reader Connected", Toast.LENGTH_SHORT).show();
                            if (getFragmentRefreshListener() != null) {
                                getFragmentRefreshListener().onRefresh();
                            }
                        }
                    } else {
                        Toast.makeText(context, "08: DETTACHED Disconnected", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", "Permission denied for device " + device);
                    }
                }
            }

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                globalVariable.setIsReaderConnected("NO");
                Toast.makeText(context, "Reader Disconnected", Toast.LENGTH_SHORT).show();

            }

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if (device != null) {
                    globalVariable.setIsReaderConnected("YES");
                    Toast.makeText(context, "Reader Connected", Toast.LENGTH_SHORT).show();
                    if (getFragmentRefreshListener() != null) {
                        getFragmentRefreshListener().onRefresh();
                    }

                } else {
                    globalVariable.setIsReaderConnected("NO");
                }
            }
        }
    };

    protected void getSelectedBottomItemView(MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();

        if (id == R.id.action_sos) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentSosmessage sosfragment = new FragmentSosmessage();
            ft.addToBackStack(ClassCommon.FRAG_SOSMSGS);
            ft.replace(R.id.fragmentContainer, sosfragment);
            ft.commit();
        }
        if (id == R.id.action_guide) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentGuide guide = new FragmentGuide();
            ft.addToBackStack(ClassCommon.FRAG_GUIDE);
            ft.replace(R.id.fragmentContainer, guide);
            ft.commit();
        }

        if (id == R.id.action_about) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentAboutsla about = new FragmentAboutsla();
            ft.addToBackStack(ClassCommon.FRAG_ABOUT_SLA);
            ft.replace(R.id.fragmentContainer, about);
            ft.commit();
        }
        if (id == R.id.action_support) {


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentSupport support = new FragmentSupport();
            ft.addToBackStack(ClassCommon.FRAG_SUPPORT);
            ft.replace(R.id.fragmentContainer, support);
            ft.commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_welcome) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentDashboard dashboard = new FragmentDashboard();
            ft.addToBackStack(ClassCommon.FRAG_DASHBOARD);
            ft.replace(R.id.fragmentContainer, dashboard);
            ft.commit();
        }

        if (id == R.id.action_report) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentTxnReport txnreport = new FragmentTxnReport();
            ft.addToBackStack(ClassCommon.FRAG_TXNREPORT);
            ft.replace(R.id.fragmentContainer, txnreport);
            ft.commit();
        }
        if (id == R.id.action_subscription) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentSubList subrate = new FragmentSubList();
            ft.addToBackStack(ClassCommon.FRAG_SUBLIST);
            ft.replace(R.id.fragmentContainer, subrate);
            ft.commit();
        }
        if (id == R.id.action_diagnostics) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentDiagnostics diagnostics = new FragmentDiagnostics();
            ft.addToBackStack(ClassCommon.FRAG_DIAGNOSTIC);
            ft.replace(R.id.fragmentContainer, diagnostics);
            ft.commit();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        int lastFragmentIndex = getSupportFragmentManager().getBackStackEntryCount() - 1;
        String tag = getSupportFragmentManager().getBackStackEntryAt(lastFragmentIndex).getName();
//        Toast.makeText(this, "tag = "+tag, Toast.LENGTH_SHORT).show();
        if (tag.equalsIgnoreCase(ClassCommon.FRAG_DASHBOARD)) {

            if (doubleBackToExitPressedOnce) {
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.press_back_again), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentDashboard dashboard = new FragmentDashboard();
            ft.replace(R.id.fragmentContainer, dashboard);
            ft.addToBackStack(ClassCommon.FRAG_DASHBOARD);
            ft.commit();
        }

    }
}
