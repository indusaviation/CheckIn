package com.slatrack.gtms.model;

public class ModelDoneItem {

    private int tdid;
    private String username;
    private String eventname;
    private String chkname;
    private String punchdate;
    private String punchtime;
    private String gpsLat;
    private String gpsLong;
    private String punchmonth;
    private String planneddate;
    private String actualdate;
    private String nextdue;
    private String nextduetime;

    public ModelDoneItem(int tdid, String username, String eventname,
                         String chkname, String punchdate, String punchtime,
                         String gpsLat, String gpsLong, String punchmonth,
                         String planneddate, String actualdate, String nextdue,
                         String nextduetime) {

        this.tdid = tdid;
        this.username = username;
        this.eventname = eventname;
        this.chkname = chkname;
        this.punchdate = punchdate;
        this.punchtime = punchtime;
        this.gpsLat = gpsLat;
        this.gpsLong = gpsLong;
        this.punchmonth = punchmonth;
        this.planneddate = planneddate;
        this.actualdate = actualdate;
        this.nextdue = nextdue;
        this.nextduetime = nextduetime;
    }

    public int getTdid() {
        return tdid;
    }

    public void setTdid(int tdid) {
        this.tdid = tdid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getChkname() {
        return chkname;
    }

    public void setChkname(String chkname) {
        this.chkname = chkname;
    }

    public String getPunchdate() {
        return punchdate;
    }

    public void setPunchdate(String punchdate) {
        this.punchdate = punchdate;
    }

    public String getPunchtime() {
        return punchtime;
    }

    public void setPunchtime(String punchtime) {
        this.punchtime = punchtime;
    }

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

    public String getPunchmonth() {
        return punchmonth;
    }

    public void setPunchmonth(String punchmonth) {
        this.punchmonth = punchmonth;
    }

    public String getPlanneddate() {
        return planneddate;
    }

    public void setPlanneddate(String planneddate) {
        this.planneddate = planneddate;
    }

    public String getActualdate() {
        return actualdate;
    }

    public void setActualdate(String actualdate) {
        this.actualdate = actualdate;
    }

    public String getNextdue() {
        return nextdue;
    }

    public void setNextdue(String nextdue) {
        this.nextdue = nextdue;
    }

    public String getNextduetime() {
        return nextduetime;
    }

    public void setNextduetime(String nextduetime) {
        this.nextduetime = nextduetime;
    }
}
