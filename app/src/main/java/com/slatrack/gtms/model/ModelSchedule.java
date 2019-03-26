package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;



public class ModelSchedule implements Parcelable {
    private String slaschedule_id;
    private String slashift_id;
    private String society_id;
    private String user_id;
    private String shiftname;
    private String schedulefrom;
    private String scheduleto;

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

    public String getSlaschedule_id() {
        return slaschedule_id;
    }

    public void setSlaschedule_id(String slaschedule_id) {
        this.slaschedule_id = slaschedule_id;
    }

    public String getSlashift_id() {
        return slashift_id;
    }

    public void setSlashift_id(String slashift_id) {
        this.slashift_id = slashift_id;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShiftname() {
        return shiftname;
    }

    public void setShiftname(String shiftname) {
        this.shiftname = shiftname;
    }

    public String getSchedulefrom() {
        return schedulefrom;
    }

    public void setSchedulefrom(String schedulefrom) {
        this.schedulefrom = schedulefrom;
    }

    public String getScheduleto() {
        return scheduleto;
    }

    public void setScheduleto(String scheduleto) {
        this.scheduleto = scheduleto;
    }





    public ModelSchedule() {
    }

    private ModelSchedule(Parcel in) {
        slaschedule_id = in.readString();
        slashift_id = in.readString();
        society_id = in.readString();
        user_id = in.readString();
        shiftname = in.readString();
        schedulefrom = in.readString();
        scheduleto = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();
    }

    public static final Creator<ModelSchedule> CREATOR = new Creator<ModelSchedule>() {
        @Override
        public ModelSchedule createFromParcel(Parcel in) {
            return new ModelSchedule(in);
        }

        @Override
        public ModelSchedule[] newArray(int size) {
            return new ModelSchedule[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(slaschedule_id);
        parcel.writeString(slashift_id);
        parcel.writeString(user_id);
        parcel.writeString(society_id);
        parcel.writeString(shiftname);
        parcel.writeString(schedulefrom);
        parcel.writeString(scheduleto);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);
    }
}
