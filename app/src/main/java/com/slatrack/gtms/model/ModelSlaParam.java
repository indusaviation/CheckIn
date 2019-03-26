package com.slatrack.gtms.model;
import android.os.Parcel;
import android.os.Parcelable;

public class ModelSlaParam implements Parcelable {


    private String id;
    private String enc_vi;
    private String enc_key;
    private String sla_percent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSla_percent() {
        return sla_percent;
    }

    public void setSla_percent(String sla_percent) {
        this.sla_percent = sla_percent;
    }

    public String getEnc_key() {
        return enc_key;
    }

    public void setEnc_key(String enc_key) {
        this.enc_key = enc_key;
    }

    public String getEnc_vi() {
        return enc_vi;
    }

    public void setEnc_vi(String enc_vi) {
        this.enc_vi = enc_vi;
    }

    public ModelSlaParam() {
    }

    private ModelSlaParam(Parcel in) {
        id = in.readString();
        enc_key = in.readString();
        enc_vi = in.readString();
        sla_percent = in.readString();
    }

    public static final Creator<ModelSlaParam> CREATOR = new Creator<ModelSlaParam>() {
        @Override
        public ModelSlaParam createFromParcel(Parcel in) {
            return new ModelSlaParam(in);
        }

        @Override
        public ModelSlaParam[] newArray(int size) {
            return new ModelSlaParam[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(enc_key);
        parcel.writeString(enc_vi);
        parcel.writeString(sla_percent);
    }



}
