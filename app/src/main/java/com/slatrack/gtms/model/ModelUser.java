package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelUser implements Parcelable{
    private String name;
    private String imei1;
    private String imei2;
    private String role;
    private String firstname;
    private String middelname;
    private String lastname;
    private String amount;
    private int user_id;
    private int society_id;
    private String readercode;
    private String keya;
    private String keyb;
    private String readerstatus;
    private String subtype;
    private String substatus;
    private String subname;


    public void setReadercode(String readercode) {
        this.readercode = readercode;
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

    public ModelUser() {
    }

    private ModelUser(Parcel in) {
        name = in.readString();
        imei1 = in.readString();
        imei2 = in.readString();
        role = in.readString();
        firstname = in.readString();
        middelname = in.readString();
        lastname = in.readString();
        amount = in.readString();
        user_id = in.readInt();
        society_id = in.readInt();
        readercode = in.readString();
        keya = in.readString();
        readerstatus = in.readString();
        keyb = in.readString();
        subtype = in.readString();
        substatus = in.readString();
        subname = in.readString();
    }

    public static final Creator<ModelUser> CREATOR = new Creator<ModelUser>() {
        @Override
        public ModelUser createFromParcel(Parcel in) {
            return new ModelUser(in);
        }

        @Override
        public ModelUser[] newArray(int size) {
            return new ModelUser[size];
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSociety_id() {
        return society_id;
    }

    public void setSociety_id(int society_id) {
        this.society_id = society_id;
    }

    public String getReadercode() {
        return readercode;
    }

    public void setReadecode(String readercode) {
        this.readercode = readercode;
    }

    public String getKeya() {
        return keya;
    }

    public void setKeya(String keya) {
        this.keya = keya;
    }

    public String getKeyb() {
        return keyb;
    }

    public void setKeyb(String keyb) {
        this.keyb = keyb;
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
        parcel.writeInt(user_id);
        parcel.writeInt(society_id);
        parcel.writeString(readercode);
        parcel.writeString(keya);
        parcel.writeString(keyb);
        parcel.writeString(readerstatus);
        parcel.writeString(subtype);
        parcel.writeString(substatus);
        parcel.writeString(subname);
    }
}
