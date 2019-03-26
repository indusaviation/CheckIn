package com.slatrack.gtms.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.utils.ClassUtility;
import com.slatrack.gtms.utils.SLAGTMS;

public class FragmentGuide extends Fragment {

    private WebView webView;
    SLAGTMS globalVariable;

    View rootView;

    ActivityWelcome activityWelcome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_guide,container,false);

        activityWelcome = (ActivityWelcome) getActivity();

        globalVariable  = (SLAGTMS) activityWelcome.getApplicationContext();
        webView = (WebView) rootView.findViewById(R.id.webView);

        loadWebView();
        return rootView;
    }

    private void loadWebView() {

        if(ClassUtility.IsConnectedToNetwork(activityWelcome)) {
            webView.loadUrl("http://www.slatrack.com/guide.html");
            webView.reload();
        }
        else{
            webView.loadUrl("file:///android_res/raw/offlineguide.html");
            webView.reload();
        }

    }


}
