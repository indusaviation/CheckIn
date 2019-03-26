package com.slatrack.gtms.model;

public class ModelCheckINData {

    private int autoIncID;
    private String readerID;
    private String checkpoint;
    private String swipeTime;
    private String swipeDate;



    private String society_id;
    private String organization_id;
    private String facility_id;

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


    public ModelCheckINData(String readerID, String checkpoint, String swipeTime, String swipeDate) {
        this.readerID = readerID;
        this.checkpoint = checkpoint;
        this.swipeTime = swipeTime;
        this.swipeDate = swipeDate;

    }

    public int getAutoIncID() {
        return autoIncID;
    }

    public void setAutoIncID(int autoIncID) {
        this.autoIncID = autoIncID;
    }

    public void setReaderID(String readerID) {
        this.readerID = readerID;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public void setSwipeTime(String swipeTime) {
        this.swipeTime = swipeTime;
    }

    public void setSwipeDate(String swipeDate) {
        this.swipeDate = swipeDate;
    }

    public String getReaderID() {
        return readerID;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public String getSwipeTime() {
        return swipeTime;
    }

    public String getSwipeDate() {
        return swipeDate;
    }


}
