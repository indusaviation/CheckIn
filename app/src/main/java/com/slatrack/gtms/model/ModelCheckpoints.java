package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelCheckpoints implements Parcelable{
    private String checkpointcode;
    private String checkpointname;
    private String user_id;
    private String society_id;
    private String checked = "NO";

    public String isChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    private String organization_id;
    private String facility_id;


    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganizationId(String organizationId) {
        this.organization_id = organizationId;
    }

    public String getFacility_id() {
        return facility_id;
    }

    public void setFacilityId(String facilityId) {
        this.facility_id = facilityId;
    }


    public ModelCheckpoints() {
    }

    private ModelCheckpoints(Parcel in) {
        user_id = in.readString();
        society_id = in.readString();
        checkpointcode = in.readString();
        checkpointname = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();
        checked = in.readString();


    }

    public static final Creator<ModelCheckpoints> CREATOR = new Creator<ModelCheckpoints>() {
        @Override
        public ModelCheckpoints createFromParcel(Parcel in) {
            return new ModelCheckpoints(in);
        }

        @Override
        public ModelCheckpoints[] newArray(int size) {
            return new ModelCheckpoints[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }


    public String getCheckpointcode() {
        return checkpointcode;
    }

    public void setCheckpointcode(String checkpointcode) {
        this.checkpointcode = checkpointcode;
    }

    public String getCheckpointname() {
        return checkpointname;
    }

    public void setCheckpointname(String checkpointname) {
        this.checkpointname = checkpointname;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(checkpointcode);
        parcel.writeString(checkpointname);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);
        parcel.writeString(checked);

    }
}
