package com.slatrack.gtms.model;

import java.util.List;

public class ModelDoneToDoList {

    private boolean ERROR;
    private String STATUS;
    private List<ModelDoneItem> RESULT;

    public ModelDoneToDoList(boolean ERROR, String STATUS, List<ModelDoneItem> RESULT) {
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

    public List<ModelDoneItem> getRESULT() {
        return RESULT;
    }

    public void setRESULT(List<ModelDoneItem> RESULT) {
        this.RESULT = RESULT;
    }
}
