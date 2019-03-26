package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelSosMessage implements Parcelable {

    private String id;
    private String msgconst;
    private String msgdetails;

    private String organization_id;
    private String facility_id;
    private String society_id;

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgconst() {
        return msgconst;
    }

    public void setMsgconst(String msgconst) {
        this.msgconst = msgconst;
    }

    public String getMsgdetails() {
        return msgdetails;
    }

    public void setMsgdetails(String msgdetails) {
        this.msgdetails = msgdetails;
    }

    public ModelSosMessage() {
    }

    private ModelSosMessage(Parcel in) {
        //user_id = in.readString();
        id = in.readString();
        msgconst = in.readString();
        msgdetails = in.readString();
        society_id = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();
    }

    public static final Creator<ModelSosMessage> CREATOR = new Creator<ModelSosMessage>() {
        @Override
        public ModelSosMessage createFromParcel(Parcel in) {
            return new ModelSosMessage(in);
        }

        @Override
        public ModelSosMessage[] newArray(int size) {
            return new ModelSosMessage[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //   parcel.writeString(checkpointcode);
        parcel.writeString(id);
        parcel.writeString(msgconst);
        parcel.writeString(msgdetails);
        parcel.writeString(society_id);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);

    }
}
