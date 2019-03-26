package com.slatrack.gtms.model;

public class ModelEventData {

    private int autoIncID;
    private String readerID;
    private String checkpoint;
    private String eventcode;
    private String swipeTime;
    private String swipeDate;
    private String society_id;
    private String organization_id;
    private String facility_id;

    private String gpsLat;
    private String gpsLong;

    public String getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(String gpsLat) {
        this.gpsLat = gpsLat;
    }

    public String getGpsLong() {
        return gpsLong;
    }

    public void setGpsLong(String gpsLong) {
        this.gpsLong = gpsLong;
    }

    public String getEventcode() {
        return eventcode;
    }

    public void setEventcode(String eventcode) {
        this.eventcode = eventcode;
    }

    public ModelEventData(String readerID, String checkpoint, String eventcode, String swipeTime, String swipeDate,String gpsLat, String gpsLong) {
        this.readerID = readerID;
        this.checkpoint = checkpoint;
        this.eventcode = eventcode;
        this.swipeTime = swipeTime;
        this.swipeDate = swipeDate;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;

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
}
