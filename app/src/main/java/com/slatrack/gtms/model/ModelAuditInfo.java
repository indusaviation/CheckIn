package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelAuditInfo implements Parcelable {


    private String id;
    private String lastlogintime;
    private String lastvalidationdate;
    private String latestupdate;
    private String totalrecordsupdated;
    private String firstinstalldate;
    private String lastsynchronizationtime;
    private String lastregrequested;
    private String lastsubrequested;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(String lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getLastvalidationdate() {
        return lastvalidationdate;
    }

    public void setLastvalidationdate(String lastvalidationdate) {
        this.lastvalidationdate = lastvalidationdate;
    }

    public String getLatestupdate() {
        return latestupdate;
    }

    public void setLatestupdate(String latestupdate) {
        this.latestupdate = latestupdate;
    }

    public String getTotalrecordsupdated() {
        return totalrecordsupdated;
    }

    public void setTotalrecordsupdated(String totalrecordsupdated) {
        this.totalrecordsupdated = totalrecordsupdated;
    }

    public String getFirstinstalldate() {
        return firstinstalldate;
    }

    public void setFirstinstalldate(String firstinstalldate) {
        this.firstinstalldate = firstinstalldate;
    }

    public String getLastsynchronizationtime() {
        return lastsynchronizationtime;
    }

    public void setLastsynchronizationtime(String lastsynchronizationtime) {
        this.lastsynchronizationtime = lastsynchronizationtime;
    }

    public String getLastregrequested() {
        return lastregrequested;
    }

    public void setLastregrequested(String lastregrequested) {
        this.lastregrequested = lastregrequested;
    }

    public String getLastsubrequested() {
        return lastsubrequested;
    }

    public void setLastsubrequested(String lastsubrequested) {
        this.lastsubrequested = lastsubrequested;
    }

    public ModelAuditInfo() {
    }

    private ModelAuditInfo(Parcel in) {
        lastlogintime = in.readString();
        lastvalidationdate = in.readString();
        latestupdate = in.readString();
        totalrecordsupdated = in.readString();
        firstinstalldate = in.readString();
        lastsynchronizationtime = in.readString();
        lastregrequested = in.readString();
        lastsubrequested =in.readString();
    }

    public static final Creator<ModelAuditInfo> CREATOR = new Creator<ModelAuditInfo>() {
        @Override
        public ModelAuditInfo createFromParcel(Parcel in) {
            return new ModelAuditInfo(in);
        }

        @Override
        public ModelAuditInfo[] newArray(int size) {
            return new ModelAuditInfo[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(lastlogintime);
        parcel.writeString(lastvalidationdate);
        parcel.writeString(latestupdate);
        parcel.writeString(totalrecordsupdated);
        parcel.writeString(firstinstalldate);
        parcel.writeString(lastsynchronizationtime);
        parcel.writeString(lastregrequested);
        parcel.writeString(lastsubrequested);

    }
}
