package com.slatrack.gtms.utils;

import java.io.Serializable;
import java.util.Observable;


public class ClassRdStatusObservable extends Observable implements Serializable {

    private String rdstatus;

    public String getRdstatus() {
        return rdstatus;
    }

    public void setRdstatus(String rdstatus) {
        this.rdstatus = rdstatus;
        this.setChanged();
        this.notifyObservers(rdstatus);
    }

}