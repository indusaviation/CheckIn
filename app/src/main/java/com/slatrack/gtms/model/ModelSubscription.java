package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ModelSubscription implements Parcelable {

    private String subid;
    private String subname;
    private String subrates;
    private String subperiod;
    private String substatus;
    private String suboffertext;
    private String subamount;


    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubrates() {
        return subrates;
    }

    public void setSubrates(String subrates) {
        this.subrates = subrates;
    }

    public String getSubperiod() {
        return subperiod;
    }

    public void setSubperiod(String subperiod) {
        this.subperiod = subperiod;
    }

    public String getSubstatus() {
        return substatus;
    }

    public void setSubstatus(String substatus) {
        this.substatus = substatus;
    }

    public String getSuboffertext() {
        return suboffertext;
    }

    public void setSuboffertext(String suboffertext) {
        this.suboffertext = suboffertext;
    }

    public String getSubamount() {
        return subamount;
    }

    public void setSubamount(String subamount) {
        this.subamount = subamount;
    }



    public ModelSubscription() {
    }

    private ModelSubscription(Parcel in) {
        //user_id = in.readString();
        subid = in.readString();;
        subname = in.readString();;
        subrates = in.readString();;
        subperiod = in.readString();;
        substatus = in.readString();;
        suboffertext = in.readString();;
        subamount = in.readString();;

    }

    public static final Creator<ModelSubscription> CREATOR = new Creator<ModelSubscription>() {
        @Override
        public ModelSubscription createFromParcel(Parcel in) {
            return new ModelSubscription(in);
        }

        @Override
        public ModelSubscription[] newArray(int size) {
            return new ModelSubscription[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
     //   parcel.writeString(checkpointcode);
        parcel.writeString(subid);
        parcel.writeString(subname);
        parcel.writeString(subrates);
        parcel.writeString(subperiod);
        parcel.writeString(substatus);
        parcel.writeString(suboffertext);
        parcel.writeString(subamount);

    }
}
