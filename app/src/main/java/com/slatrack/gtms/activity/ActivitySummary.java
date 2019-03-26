package com.slatrack.gtms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.slatrack.gtms.fragment.FragmentDiagnostics;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.fragment.FragmentSosmessage;
import com.slatrack.gtms.fragment.FragmentSubList;
import com.slatrack.gtms.fragment.FragmentSupport;
import com.slatrack.gtms.fragment.FragmentTxnReport;
import com.slatrack.gtms.utils.ClassBottomNVFragment;
import com.slatrack.gtms.R;

public class ActivitySummary extends AppCompatActivity {

    Toolbar topToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);


        topToolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.sla_logo);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabsummary);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivitySummary.this, FragmentScan.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_nv);
        bnv.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        getSelectedBottomItemView(item);
                        return false;
                    }
                });
    }

    protected void getSelectedBottomItemView(MenuItem item){
        item.setChecked(true);
        int id = item.getItemId();
        ClassBottomNVFragment bnvf = ClassBottomNVFragment.newInstance((String) item.getTitle());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentRoot, bnvf);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        if (id == R.id.action_sos) {
            Intent intent = new Intent(ActivitySummary.this, FragmentSosmessage.class);
            startActivity(intent);
        }
        if(id == R.id.action_support){
            Intent intent = new Intent(ActivitySummary.this, FragmentSupport.class);
            startActivity(intent);
            finish();
        }
//        if(id == R.id.action_guide){
//            Intent intent = new Intent(ActivitySummary.this, FragmentGuide.class);
//            startActivity(intent);
//            finish();
//        }
//        if(id == R.id.action_about){
//            Intent intent = new Intent(ActivitySummary.this, FragmentAboutsla.class);
//            startActivity(intent);
//            finish();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_welcome) {
            Intent intent = new Intent(ActivitySummary.this, ActivityWelcome.class);
            startActivity(intent);
        }

        if (id == R.id.action_report) {
            Intent intent = new Intent(ActivitySummary.this, FragmentTxnReport.class);
            startActivity(intent);
        }
        if(id == R.id.action_subscription){
            Intent intent = new Intent(ActivitySummary.this, FragmentSubList.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.action_diagnostics){
            Intent intent = new Intent(ActivitySummary.this, FragmentDiagnostics.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
