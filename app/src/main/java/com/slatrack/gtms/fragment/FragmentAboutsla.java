package com.slatrack.gtms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ScrollView;
import android.text.method.ScrollingMovementMethod;

import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.fragment.FragmentDiagnostics;
import com.slatrack.gtms.fragment.FragmentScan;
import com.slatrack.gtms.fragment.FragmentSosmessage;
import com.slatrack.gtms.fragment.FragmentSubList;
import com.slatrack.gtms.fragment.FragmentSupport;
import com.slatrack.gtms.fragment.FragmentTxnReport;
import com.slatrack.gtms.utils.ClassBottomNVFragment;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

public class FragmentAboutsla extends Fragment {


    private WebView webView;
    SLAGTMS globalVariable;

    View rootView;

    ActivityWelcome activityWelcome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_aboutsla,container,false);

        activityWelcome = (ActivityWelcome) getActivity();

        globalVariable  = (SLAGTMS) activityWelcome.getApplicationContext();

        webView = (WebView) rootView.findViewById(R.id.aboutwebview);
        loadWebView();
        return rootView;
    }

    private void loadWebView() {

        if(ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            webView.loadUrl("http://www.slatrack.com/about.html");
            webView.reload();
        }
        else{
            webView.loadUrl("file:///android_res/raw/offlineguide.html");
            webView.reload();
        }



    }

}
