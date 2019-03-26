package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelEvents implements Parcelable{
    private String eventcode;
    private String eventname;
    private String eventdescription;
    private String eventperiod;
    private String eventfrequency;
    private String user_id;
    private String society_id;

    private String organization_id;
    private String facility_id;
    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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


    public ModelEvents() {
    }

    private ModelEvents(Parcel in) {
        eventcode = in.readString();
        eventname = in.readString();
        eventdescription = in.readString();
        eventperiod = in.readString();
        eventfrequency = in.readString();
        user_id = in.readString();
        society_id = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();
    }

    public static final Creator<ModelEvents> CREATOR = new Creator<ModelEvents>() {
        @Override
        public ModelEvents createFromParcel(Parcel in) {
            return new ModelEvents(in);
        }

        @Override
        public ModelEvents[] newArray(int size) {
            return new ModelEvents[size];
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

    public String getEventcode() {
        return eventcode;
    }

    public void setEventcode(String eventcode) {
        this.eventcode = eventcode;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }


    public String getEventperiod() {
        return eventperiod;
    }

    public void setEventperiod(String eventperiod) {
        this.eventperiod = eventperiod;
    }

    public String getEventfrequency() {
        return eventfrequency;
    }

    public void setEventfrequency(String eventfrequency) {
        this.eventfrequency = eventfrequency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventcode);
        parcel.writeString(eventname);
        parcel.writeString(eventdescription);
        parcel.writeString(eventperiod);
        parcel.writeString(eventfrequency);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);

    }
}
