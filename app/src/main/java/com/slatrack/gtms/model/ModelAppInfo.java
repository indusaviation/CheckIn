package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelAppInfo implements Parcelable {



    private String id;
    private String appversion;
    private String apptype;
    private String supportphone;
    private String supportemail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getSupportphone() {
        return supportphone;
    }

    public void setSupportphone(String supportphone) {
        this.supportphone = supportphone;
    }

    public String getSupportemail() {
        return supportemail;
    }

    public void setSupportemail(String supportemail) {
        this.supportemail = supportemail;
    }

    public ModelAppInfo() {
    }

    private ModelAppInfo(Parcel in) {
        appversion = in.readString();
        apptype = in.readString();
        supportphone = in.readString();
        supportemail = in.readString();
    }

    public static final Creator<ModelAppInfo> CREATOR = new Creator<ModelAppInfo>() {
        @Override
        public ModelAppInfo createFromParcel(Parcel in) {
            return new ModelAppInfo(in);
        }

        @Override
        public ModelAppInfo[] newArray(int size) {
            return new ModelAppInfo[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(appversion);
        parcel.writeString(apptype);
        parcel.writeString(supportphone);
        parcel.writeString(supportemail);
    }
}
