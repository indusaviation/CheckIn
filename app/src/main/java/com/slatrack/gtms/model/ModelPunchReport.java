package com.slatrack.gtms.model;

public class ModelPunchReport {

    //{"Date":"11\/09\/2018","TotalSwipe":206,"TotalMissed":10,"TotalExpected":216,"SwipePercentage":"95.37"},

    String Date;//":36,
    String TotalSwipe;//":"M10021",
    String TotalMissed;//":68,
    String TotalExpected;//":"2018-07-02 02:17:53",
    String SwipePercentage;//":"800", pay amount

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTotalSwipe() {
        return TotalSwipe;
    }

    public void setTotalSwipe(String totalSwipe) {
        TotalSwipe = totalSwipe;
    }

    public String getTotalMissed() {
        return TotalMissed;
    }

    public void setTotalMissed(String totalMissed) {
        TotalMissed = totalMissed;
    }

    public String getTotalExpected() {
        return TotalExpected;
    }

    public void setTotalExpected(String totalExpected) {
        TotalExpected = totalExpected;
    }

    public String getSwipePercentage() {
        return SwipePercentage;
    }

    public void setSwipePercentage(String swipePercentage) {
        SwipePercentage = swipePercentage;
    }

}
