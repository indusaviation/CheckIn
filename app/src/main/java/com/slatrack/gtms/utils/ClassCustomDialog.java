package com.slatrack.gtms.utils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.support.v7.widget.AppCompatTextView;

import com.slatrack.gtms.R;
import com.slatrack.gtms.activity.ActivitySlatrack;

public class ClassCustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public String msgDiagText = "";
    public String msgDiagType = "";
    private AppCompatTextView msgTextView ;
    private AppCompatTextView msgTitleText ;
    private AppCompatButton btnOK;
    private String okaction = "NOACTION";

    private String digtype = "";
    private String digtext = "";

    public String getOkaction() {
        return okaction;
    }

    public void setOkaction(String okaction) {
        this.okaction = okaction;
    }

    public String getDigtype() {
        return digtype;
    }

    public void setDigtype(String digtype) {
        this.digtype = digtype;
        this.msgDiagType = digtype;
    }

    public String getDigtext() {
        return digtext;
    }

    public void setDigtext(String digtext) {
        this.digtext = digtext;
        this.msgDiagText = digtext;
    }

    public ClassCustomDialog(Activity a) {
        super(a);
        this.c = a;
    }


    public ClassCustomDialog(Activity a,String strText,String diagType) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        msgDiagText = strText;
        msgDiagType = diagType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert);
        msgTextView = (AppCompatTextView)findViewById(R.id.message);
        msgTitleText = (AppCompatTextView)findViewById(R.id.messageTitle);

        btnOK = (AppCompatButton) findViewById(R.id.btnOK);
        msgTextView.setText(msgDiagText);
        msgTitleText.setText(msgDiagType);

        btnOK.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnOK:
                dismiss();
                if(okaction.equalsIgnoreCase(ClassCommon.MOVETOWELCOME)){
                    Intent intent = new Intent(this.c, ActivitySlatrack.class);
                    this.c.startActivity(intent);
                    this.c.finish();
                }
                break;
            default:
                break;
        }
        dismiss();
    }



}