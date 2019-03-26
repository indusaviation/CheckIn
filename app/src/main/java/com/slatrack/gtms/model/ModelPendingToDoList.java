package com.slatrack.gtms.model;

import java.util.List;

public class ModelPendingToDoList {

    private boolean ERROR;
    private String STATUS;
    private List<ModelPendingItem> RESULT;

    public ModelPendingToDoList(boolean ERROR, String STATUS, List<ModelPendingItem> RESULT) {
        this.ERROR = ERROR;
        this.STATUS = STATUS;
        this.RESULT = RESULT;
    }

    public boolean isERROR() {
        return ERROR;
    }

    public void setERROR(boolean ERROR) {
        this.ERROR = ERROR;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public List<ModelPendingItem> getRESULT() {
        return RESULT;
    }

    public void setRESULT(List<ModelPendingItem> RESULT) {
        this.RESULT = RESULT;
    }
}
