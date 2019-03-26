package com.slatrack.gtms.utils;

import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

public class ClassUtility {

    public static boolean IsConnectedToNetwork(Context context){

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
//            Class cmClass = Class.forName(cm.getClass().getName());
//            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
//            method.setAccessible(true); // Make the method callable
//            // get the setting for "mobile data"
//            mobileDataEnabled = (Boolean)method.invoke(cm);

            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            int type = networkInfo.getType();
            String typeName = networkInfo.getTypeName();
            mobileDataEnabled= networkInfo.isConnected();


        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }

        return mobileDataEnabled;
    }


}


