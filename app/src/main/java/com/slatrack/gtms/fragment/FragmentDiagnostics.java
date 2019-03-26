package com.slatrack.gtms.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slatrack.gtms.activity.ActivitySlatrack;
import com.slatrack.gtms.activity.ActivityWelcome;
import com.slatrack.gtms.model.ModelFeatures;
import com.slatrack.gtms.utils.ClassCustomDialog;
import com.slatrack.gtms.utils.ClassDatabase;
import com.slatrack.gtms.model.ModelAppInfo;
import com.slatrack.gtms.model.ModelLastupdate;
import com.slatrack.gtms.model.ModelOrganization;
import com.slatrack.gtms.model.ModelReader;
import com.slatrack.gtms.R;
import com.slatrack.gtms.utils.SLAGTMS;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;

public class FragmentDiagnostics extends Fragment {

    SLAGTMS globalVariable;
    ClassDatabase database;
    private ProgressBar progressBar;
    View rootView;

    ActivityWelcome activityWelcome;

    AppCompatTextView Organization_Name;
    AppCompatTextView Enable_SMS;
    AppCompatTextView Enable_Email;
    AppCompatTextView Facility_Type;
    AppCompatTextView Update_Mode;
    AppCompatTextView Verification_Status;
    AppCompatTextView Subscription_Type;
    AppCompatTextView Subscription_Status;
    AppCompatTextView Subscription_Name;
    AppCompatTextView Subscription_Validity;
    AppCompatTextView Reader_Code;
    AppCompatTextView Reader_Status;
    AppCompatTextView Reader_Name;
    AppCompatTextView User_Name;
    AppCompatTextView Primary_SOS;
    AppCompatTextView Server_Update;
    AppCompatTextView Device_Update;
    AppCompatTextView Need_Update;
    AppCompatTextView App_Version;
    AppCompatTextView Application_Type;
    AppCompatTextView Support_Email;
    AppCompatTextView Support_Phone;
    CardView cardOrgInfo;
    CardView cardServerInfo;
    CardView cardAppInfo;
    AppCompatButton shareSupport;
    LinearLayout cardLayout;

    private AppCompatTextView refreshdiag;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_diagnostics, container, false);

        activityWelcome = (ActivityWelcome) getActivity();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarDiag);
        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();

        database = new ClassDatabase(activityWelcome);
        globalVariable = (SLAGTMS) activityWelcome.getApplicationContext();
        refreshdiag = (AppCompatTextView) rootView.findViewById(R.id.refreshdiag);
//        cardOrgInfo = (CardView) rootView.findViewById(R.id.cardOrgInfo);
//        cardServerInfo = (CardView) rootView.findViewById(R.id.cardServerInfo);
//        cardAppInfo = (CardView) rootView.findViewById(R.id.cardAppInfo);
        shareSupport = (AppCompatButton) rootView.findViewById(R.id.shareSupport);
        cardLayout = (LinearLayout) rootView.findViewById(R.id.cardLayout);

        getAllTextViews();

        refreshdiag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ModelLastupdate lastdata = database.getLastupdate();
                if (lastdata != null) {
                    if (database.lastupdateCount() > 0) {
                        String id = lastdata.getId();
                        database.resetLastUpdate(id);
                        globalVariable.setUpdateNeeded("YES");

                    } else {
                        globalVariable.setUpdateNeeded("YES");
                    }
                } else {
                    globalVariable.setUpdateNeeded("YES");
                }

                ClassCustomDialog cdd = new ClassCustomDialog((ActivityWelcome) getActivity());
                cdd.setDigtext(getResources().getString(R.string.refresh_datamsg));
                cdd.setDigtype(getResources().getString(R.string.msg_title_information));
                cdd.setOkaction(getResources().getString(R.string.okaction_movetowelcome));
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.show();

            }
        });


        ModelOrganization orgdata = database.getOrganization();
        ModelReader readerdata = database.getUser();
        ModelFeatures featuredata = database.getFeature();

        String isverified = "NO";
        String substatus = "NOT ACTIVE";
        String readerstatus = "NOT ACTIVE";
        String needupdate = "YES";
        String scanmode = "NFC";

        if (orgdata.getIsverified().equalsIgnoreCase("1")) {
            isverified = "YES";
        }
        if (readerdata.getSubstatus().equalsIgnoreCase("1")) {
            substatus = "ACTIVE";
        }
        if (readerdata.getReaderstatus().equalsIgnoreCase("1")) {
            readerstatus = "ACTIVE";
        }

        if(featuredata != null) {
            if(database.featuresCount() > 0) {
                if (featuredata.getScan_mode().equalsIgnoreCase("0")) {
                    scanmode = "QR";
                } else {
                    scanmode = "NFC";
                }
            }
        }
        else{
            scanmode = "NFC";
        }

        String appinfomsg = "";
        ModelAppInfo appinfodata = database.getAppInfo();
        if (appinfodata != null) {
            if (database.appInfoCount() > 0) {

                App_Version.setText(appinfodata.getAppversion().toUpperCase());
                Application_Type.setText(appinfodata.getApptype().toUpperCase());
                Support_Email.setText(appinfodata.getSupportemail().toUpperCase());
                Support_Phone.setText(appinfodata.getSupportphone().toUpperCase());
            }
        }

        ModelLastupdate lastdata = database.getLastupdate();
        if (lastdata != null) {
            if (database.lastupdateCount() > 0) {

                if (lastdata.getNeedupdate().equalsIgnoreCase("1")) {
                    needupdate = "NO";
                }
                Server_Update.setText(getFormattedDate(lastdata.getServerupdate()));
                Device_Update.setText(getFormattedDate(lastdata.getDeviceupdate()));
                Need_Update.setText(needupdate.toUpperCase());
            }
        }

        Organization_Name.setText(orgdata.getOrgname().toUpperCase());
        Enable_SMS.setText(orgdata.getEnablesms().toUpperCase());
        Enable_Email.setText(orgdata.getEnableemail().toUpperCase());
        Facility_Type.setText(orgdata.getSocietytype().toUpperCase());
       // Update_Mode.setText(orgdata.getUpdatemode().toUpperCase());
        Update_Mode.setText(scanmode.toUpperCase());
        Verification_Status.setText(isverified.toUpperCase());
        Subscription_Type.setText(orgdata.getSubtype().toUpperCase());
        Subscription_Status.setText(substatus.toUpperCase());
        Subscription_Name.setText(readerdata.getSubname().toUpperCase());
        Subscription_Validity.setText(readerdata.getExpiry_date().toUpperCase());
        Reader_Code.setText(readerdata.getReadercode().toUpperCase());
        Reader_Status.setText(readerstatus.toUpperCase());
        Reader_Name.setText(readerdata.getName().toUpperCase());
        User_Name.setText(readerdata.getFirstname().toUpperCase() + " " + readerdata.getLastname().toUpperCase());
        Primary_SOS.setText(orgdata.getPrimarysos().toUpperCase());

        progressBar.setVisibility(View.GONE);


        return rootView;
    }

    private void getAllTextViews() {

        Organization_Name = (AppCompatTextView) rootView.findViewById(R.id.Organization_Name);
        Enable_SMS = (AppCompatTextView) rootView.findViewById(R.id.Enable_SMS);
        Enable_Email = (AppCompatTextView) rootView.findViewById(R.id.Enable_Email);
        Facility_Type = (AppCompatTextView) rootView.findViewById(R.id.Facility_Type);
        Update_Mode = (AppCompatTextView) rootView.findViewById(R.id.Update_Mode);
        Verification_Status = (AppCompatTextView) rootView.findViewById(R.id.Verification_Status);
        Subscription_Type = (AppCompatTextView) rootView.findViewById(R.id.Subscription_Type);
        Subscription_Status = (AppCompatTextView) rootView.findViewById(R.id.Subscription_Status);
        Subscription_Name = (AppCompatTextView) rootView.findViewById(R.id.Subscription_Name);
        Subscription_Validity = (AppCompatTextView) rootView.findViewById(R.id.Subscription_Validity);
        Reader_Code = (AppCompatTextView) rootView.findViewById(R.id.Reader_Code);
        Reader_Status = (AppCompatTextView) rootView.findViewById(R.id.Reader_Status);
        Reader_Name = (AppCompatTextView) rootView.findViewById(R.id.Reader_Name);
        User_Name = (AppCompatTextView) rootView.findViewById(R.id.User_Name);
        Primary_SOS = (AppCompatTextView) rootView.findViewById(R.id.Primary_SOS);
        Server_Update = (AppCompatTextView) rootView.findViewById(R.id.Server_Update);
        Device_Update = (AppCompatTextView) rootView.findViewById(R.id.Device_Update);
        Need_Update = (AppCompatTextView) rootView.findViewById(R.id.Need_Update);
        App_Version = (AppCompatTextView) rootView.findViewById(R.id.App_Version);
        Application_Type = (AppCompatTextView) rootView.findViewById(R.id.Application_Type);
        Support_Email = (AppCompatTextView) rootView.findViewById(R.id.Support_Email);
        Support_Phone = (AppCompatTextView) rootView.findViewById(R.id.Support_Phone);


        shareSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted()) {
                    shareSupportDialog();
                } else {
                    AlertDialog.Builder permissionAlert = new AlertDialog.Builder(activityWelcome);
                    permissionAlert.setMessage(getResources().getString(R.string.permissionExternal));
                    permissionAlert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            getPermission();
                        }
                    });
                    permissionAlert.setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    permissionAlert.show();
                }
            }
        });


    }

    private void getPermission() {

        ActivityCompat.requestPermissions(activityWelcome,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                103);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 103: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            getResources().getString(R.string.permissionAccepted),getResources().getString(R.string.msg_title_information));
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    cdd.show();


                } else {
                    ClassCustomDialog cdd=new ClassCustomDialog(activityWelcome,
                            getResources().getString(R.string.permissionDenied),getResources().getString(R.string.msg_title_information));
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    cdd.show();

                }
            }
            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request.
    }


    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(activityWelcome, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activityWelcome, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private void shareSupportDialog() {

        final Bitmap cardLayoutBitmap = loadBitmapFromView(cardLayout);


        AlertDialog.Builder alert = new AlertDialog.Builder(activityWelcome);
        alert.setMessage(getResources().getString(R.string.shareVia));
        alert.setPositiveButton(getResources().getString(R.string.email), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                shareViaEmail(cardLayoutBitmap );
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.whatsapp), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                shareViaWhatsApp(cardLayoutBitmap );
            }
        });

        alert.show();

    }

    private void shareViaEmail(Bitmap cardLayout) {

        String defaultEmail = "info@slatrack.com";
        String supportEmail = Support_Email.getText().toString().trim();
        if (TextUtils.isEmpty(supportEmail)) {
            supportEmail = defaultEmail;
        }
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            cardLayout.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path1 = MediaStore.Images.Media.insertImage(activityWelcome.getContentResolver(), cardLayout, "Organization information", "Organization Information screen");


            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("*/*");
            String to[] = {supportEmail};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            ArrayList<Uri> screenList = new ArrayList<Uri>();

            screenList.add(Uri.parse(path1));
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, screenList);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Diagnostic Information");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find attached screen for diagnostic information.");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(activityWelcome, "App not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaWhatsApp(Bitmap cardLayout) {

        PackageManager pm = activityWelcome.getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            cardLayout.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path1 = MediaStore.Images.Media.insertImage(activityWelcome.getContentResolver(), cardLayout, "Organization information", "Organization Information screen");

            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            waIntent.setType("image/*");
            waIntent.setPackage("com.whatsapp");
            ArrayList<Uri> screenList = new ArrayList<Uri>();

            screenList.add(Uri.parse(path1));
            waIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, screenList);
//            String formattedNumber = Util.formatPhone(phone);
//            waIntent.putExtra("jid", "8308004952" + "@s.whatsapp.net");

            waIntent.putExtra(Intent.EXTRA_TEXT, "Please see the attached screens for diagnostic information");
            activityWelcome.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(activityWelcome, "App not Installed", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFormattedDate(String tran_date) {
        String formattedDate = tran_date;
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
            Date transactionDate = originalFormat.parse(tran_date);
            formattedDate = targetFormat.format(transactionDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Bitmap loadBitmapFromView(LinearLayout cardLayout) {
        Bitmap b = Bitmap.createBitmap(cardLayout.getMeasuredWidth(), cardLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        cardLayout.layout(cardLayout.getLeft(), cardLayout.getTop(), cardLayout.getRight(), cardLayout.getBottom());
        cardLayout.draw(c);
        return b;
    }

}
