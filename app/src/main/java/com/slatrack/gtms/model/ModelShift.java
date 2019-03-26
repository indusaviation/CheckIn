package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ModelShift implements Parcelable {
    private String slashift_id;
    private String society_id;
    private String user_id;
    private String shiftname;
    private String shiftfrom;
    private String shiftto;

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

    public String getShiftfrom() {
        return shiftfrom;
    }

    public void setShiftfrom(String shiftfrom) {
        this.shiftfrom = shiftfrom;
    }

    public String getShiftto() {
        return shiftto;
    }

    public void setShiftto(String shiftto) {
        this.shiftto = shiftto;
    }



    public ModelShift() {
    }

    private ModelShift(Parcel in) {
        slashift_id = in.readString();
        user_id = in.readString();
        society_id = in.readString();
        shiftname = in.readString();
        shiftfrom = in.readString();
        shiftto = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();
    }

    public static final Creator<ModelShift> CREATOR = new Creator<ModelShift>() {
        @Override
        public ModelShift createFromParcel(Parcel in) {
            return new ModelShift(in);
        }

        @Override
        public ModelShift[] newArray(int size) {
            return new ModelShift[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(slashift_id);
        parcel.writeString(user_id);
        parcel.writeString(society_id);
        parcel.writeString(shiftname);
        parcel.writeString(shiftfrom);
        parcel.writeString(shiftto);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);
    }
}
