package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ModelOrganization implements Parcelable {
    private String orgname ="";
    private String user_id="";
    private String society_id="";
    private String expiry_date="";
    private String enablesms="";
    private String enableemail="";
    private String isverified="";
    private String primarysos="";
    private String updatemode="";
    private String secmobno="";
    private String subtype="";
    private String societytype="";
    private String substatus="";
    private String orgstatus="";
    private String organization_id="";
    private String facility_id="";

    public String getSubstatus() {
        return substatus;
    }

    public void setSubstatus(String substatus) {
        this.substatus = substatus;
    }

    public String getOrgstatus() {
        return orgstatus;
    }

    public void setOrgstatus(String orgstatus) {
        this.orgstatus = orgstatus;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public void setFacility_id(String facility_id) {
        this.facility_id = facility_id;
    }




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

    public String getLogopath() {
        return logopath;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    private String logopath;

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

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

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getEnablesms() {
        return enablesms;
    }

    public void setEnablesms(String enablesms) {
        this.enablesms = enablesms;
    }

    public String getEnableemail() {
        return enableemail;
    }

    public void setEnableemail(String enableemail) {
        this.enableemail = enableemail;
    }

    public String getIsverified() {
        return isverified;
    }

    public void setIsverified(String isverified) {
        this.isverified = isverified;
    }

    public String getPrimarysos() {
        return primarysos;
    }

    public void setPrimarysos(String primarysos) {
        this.primarysos = primarysos;
    }

    public String getUpdatemode() {
        return updatemode;
    }

    public void setUpdatemode(String updatemode) {
        this.updatemode = updatemode;
    }

    public String getSecmobno() {
        return secmobno;
    }

    public void setSecmobno(String secmobno) {
        this.secmobno = secmobno;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSocietytype() {
        return societytype;
    }

    public void setSocietytype(String societytype) {
        this.societytype = societytype;
    }




//    SELECT RD.society_id, RD.user_id, RD.readercode,SC.name,RD.imei1,RD.imei2, UR.role, UR.firstname,
//    UR.middelname, UR.lastname, RD.amount, SC.subdate, SC.enablesms, SC.enableemail,SC.isverified,SC.primarysos,
//    SC.updatemode, SC.secmobno,SC.subtyle,SC.societytype FROM gms_readers
//    RD inner join gms_societies SC on RD.society_id = SC.id inner join gms_users UR on RD.user_id = UR.id

    public ModelOrganization() {
    }

    private ModelOrganization(Parcel in) {

        orgname = in.readString();
        user_id = in.readString();
        society_id = in.readString();
        expiry_date = in.readString();
        enablesms = in.readString();
        enableemail = in.readString();
        isverified = in.readString();
        primarysos = in.readString();
        updatemode = in.readString();
        secmobno = in.readString();
        subtype = in.readString();
        societytype = in.readString();
        logopath = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();

    }

    public static final Creator<ModelOrganization> CREATOR = new Creator<ModelOrganization>() {
        @Override
        public ModelOrganization createFromParcel(Parcel in) {
            return new ModelOrganization(in);
        }

        @Override
        public ModelOrganization[] newArray(int size) {
            return new ModelOrganization[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(orgname);
        parcel.writeString(user_id);
        parcel.writeString(society_id);
        parcel.writeString(expiry_date);
        parcel.writeString(enablesms);
        parcel.writeString(enableemail);
        parcel.writeString(isverified);
        parcel.writeString(primarysos);
        parcel.writeString(updatemode);
        parcel.writeString(secmobno);
        parcel.writeString(subtype);
        parcel.writeString(societytype);
        parcel.writeString(logopath);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);

    }
}
