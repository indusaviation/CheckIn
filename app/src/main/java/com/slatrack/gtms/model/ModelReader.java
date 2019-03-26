package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelReader implements Parcelable{
    private String name;
    private String imei1;
    private String imei2;
    private String role;
    private String firstname;
    private String middelname;
    private String lastname;
    private String amount;
    private String user_id;
    private String society_id;
    private String expiry_date;
    private String readercode;
    private String organization_id;
    private String facility_id;
    private String readerstatus;
    private String subtype;
    private String substatus;
    private String subname;


    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(String facility_id) {
        this.facility_id = facility_id;
    }



    public String getReaderstatus() {
        return readerstatus;
    }

    public void setReaderstatus(String readerstatus) {
        this.readerstatus = readerstatus;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSubstatus() {
        return substatus;
    }

    public void setSubstatus(String substatus) {
        this.substatus = substatus;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }


    public ModelReader() {
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getReadercode() {
        return readercode;
    }

    public void setReadercode(String readercode) {
        this.readercode = readercode;
    }

    private ModelReader(Parcel in) {
        name = in.readString();
        imei1 = in.readString();
        imei2 = in.readString();
        role = in.readString();
        firstname = in.readString();
        middelname = in.readString();
        lastname = in.readString();
        amount = in.readString();
        user_id = in.readString();
        society_id = in.readString();
        expiry_date = in.readString();
        readercode = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();

        //RD.subtype,RD.substatus,RD.subname,RD.readerstatus
        subtype = in.readString();
        substatus = in.readString();
        subname = in.readString();
        readerstatus = in.readString();
    }

    public static final Creator<ModelReader> CREATOR = new Creator<ModelReader>() {
        @Override
        public ModelReader createFromParcel(Parcel in) {
            return new ModelReader(in);
        }

        @Override
        public ModelReader[] newArray(int size) {
            return new ModelReader[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei1() {
        return imei1;
    }

    public void setImei1(String imei1) {
        this.imei1 = imei1;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddelname() {
        return middelname;
    }

    public void setMiddelname(String middelname) {
        this.middelname = middelname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getExpiryDate() {
        return expiry_date;
    }

    public void setExpiryDate(String expiry_date) {
        this.expiry_date = expiry_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imei1);
        parcel.writeString(imei2);
        parcel.writeString(role);
        parcel.writeString(firstname);
        parcel.writeString(middelname);
        parcel.writeString(lastname);
        parcel.writeString(amount);
        parcel.writeString(user_id);
        parcel.writeString(society_id);
        parcel.writeString(expiry_date);
        parcel.writeString(readercode);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);

        parcel.writeString(subtype);
        parcel.writeString(substatus);
        parcel.writeString(subname);
        parcel.writeString(readerstatus);

        //RD.subtype,RD.substatus,RD.subname,RD.readerstatus
    }
}
