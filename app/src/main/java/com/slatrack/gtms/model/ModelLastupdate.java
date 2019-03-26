package com.slatrack.gtms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelLastupdate implements Parcelable{

    private String id;
    private String society_id;
    private String lastupdate;
    private String needupdate;
    private String deviceupdate;
    private String serverupdate;
    private String organization_id;
    private String facility_id;

    public String getDeviceupdate() {
        return deviceupdate;
    }

    public void setDeviceupdate(String deviceupdate) {
        this.deviceupdate = deviceupdate;
    }

    public String getServerupdate() {
        return serverupdate;
    }

    public void setServerupdate(String serverupdate) {
        this.serverupdate = serverupdate;
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

    public ModelLastupdate() {
    }

    private ModelLastupdate(Parcel in) {
        id = in.readString();
        society_id = in.readString();
        lastupdate = in.readString();
        needupdate = in.readString();
        organization_id =in.readString();
        facility_id = in.readString();
        serverupdate = in.readString();
        deviceupdate = in.readString();
    }

    public static final Creator<ModelLastupdate> CREATOR = new Creator<ModelLastupdate>() {
        @Override
        public ModelLastupdate createFromParcel(Parcel in) {
            return new ModelLastupdate(in);
        }

        @Override
        public ModelLastupdate[] newArray(int size) {
            return new ModelLastupdate[size];
        }
    };

    public String getNeedupdate() {
        return needupdate;
    }

    public void setNeedupdate(String needupdate) {
        this.needupdate = needupdate;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(society_id);
        parcel.writeString(lastupdate);
        parcel.writeString(needupdate);
        parcel.writeString(organization_id);
        parcel.writeString(facility_id);
        parcel.writeString(serverupdate);
        parcel.writeString(deviceupdate);

    }
}
