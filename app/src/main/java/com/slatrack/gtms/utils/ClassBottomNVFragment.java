

package com.slatrack.gtms.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class ClassBottomNVFragment extends Fragment {
    public static ClassBottomNVFragment newInstance(String itemName) {
        ClassBottomNVFragment fragment = new ClassBottomNVFragment();

        Bundle args = new Bundle();
        args.putString("itemName", itemName);
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView scroller = new ScrollView(getActivity());
        TextView text = new TextView(getActivity());
        if(getArguments() != null) {
            text.setText("Selected bottom navigation item " +getArguments().getString("itemName", ""));
        }else{
            text.setText( "Default bottom navigation item");
        }

        scroller.addView(text);
        return scroller;
    }
}