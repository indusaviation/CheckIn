package com.slatrack.gtms.model;

public class ModelPendingItem {

    private int tdid;
    private String eventname;
    private String chkname;
    private String planneddate;
    private String nextdue;
    private String nextduetime;

    public ModelPendingItem(int tdid, String eventname, String chkname, String planneddate, String nextdue, String nextduetime) {
        this.tdid = tdid;
        this.eventname = eventname;
        this.chkname = chkname;
        this.planneddate = planneddate;
        this.nextdue = nextdue;
        this.nextduetime = nextduetime;
    }

    public int getTdid() {
        return tdid;
    }

    public void setTdid(int tdid) {
        this.tdid = tdid;
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

    public String getPlanneddate() {
        return planneddate;
    }

    public void setPlanneddate(String planneddate) {
        this.planneddate = planneddate;
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
