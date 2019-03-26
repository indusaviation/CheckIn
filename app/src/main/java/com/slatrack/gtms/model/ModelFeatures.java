package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelFeatures implements Parcelable{
    private String enable_camera;
    private String enable_gps;
    private String enable_sig;
    private String enable_selfie;
    private String enable_overide;
    private String scan_mode;
    private String user_id;
    private String society_id;

//    public String getEnable_email() {
//        return enable_email;
//    }
//
//    public void setEnable_email(String enable_email) {
//        this.enable_email = enable_email;
//    }
//
//    public String getEnable_sms() {
//        return enable_sms;
//    }
//
//    public void setEnable_sms(String enable_sms) {
//        this.enable_sms = enable_sms;
//    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public void setFacility_id(String facility_id) {
        this.facility_id = facility_id;
    }

//    private String enable_email;
//    private String enable_sms;
//

    private String organization_id;
    private String facility_id;


    public String getOrganization_id() {
        return organization_id;
    }


    public String getFacility_id() {
        return facility_id;
    }



    public ModelFeatures() {
    }

    private ModelFeatures(Parcel in) {
        enable_camera = in.readString();
        enable_gps = in.readString();
        enable_sig = in.readString();
        enable_selfie = in.readString();
        enable_overide = in.readString();
        scan_mode = in.readString();
        user_id = in.readString();
        society_id = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();
//        enable_email =in.readString();
//        enable_sms = in.readString();
    }

    public static final Creator<ModelFeatures> CREATOR = new Creator<ModelFeatures>() {
        @Override
        public ModelFeatures createFromParcel(Parcel in) {
            return new ModelFeatures(in);
        }

        @Override
        public ModelFeatures[] newArray(int size) {
            return new ModelFeatures[size];
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

    public String getEnable_camera() {
        return enable_camera;
    }

    public void setEnable_camera(String enable_camera) {
        this.enable_camera = enable_camera;
    }

    public String getEnable_gps() {
        return enable_gps;
    }

    public void setEnable_gps(String enable_gps) {
        this.enable_gps = enable_gps;
    }

    public String getEnable_sig() {
        return enable_sig;
    }

    public void setEnable_sig(String enable_sig) {
        this.enable_sig = enable_sig;
    }

    public String getEnable_selfie() {
        return enable_selfie;
    }

    public void setEnable_selfie(String enable_selfie) {
        this.enable_selfie = enable_selfie;
    }

    public String getEnable_overide() {
        return enable_overide;
    }

    public void setEnable_overide(String enable_overide) {
        this.enable_overide = enable_overide;
    }

    public String getScan_mode() {
        return scan_mode;
    }

    public void setScan_mode(String scan_mode) {
        this.scan_mode = scan_mode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(enable_camera);
        parcel.writeString(enable_gps);
        parcel.writeString(enable_overide);
        parcel.writeString(enable_selfie);
        parcel.writeString(enable_sig);
        parcel.writeString(scan_mode);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);
//        parcel.writeString(enable_email);
//        parcel.writeString(enable_sms);

    }
}
