package com.portalrom.setupwizard.Utils.DataModel;

import android.net.wifi.ScanResult;

public class WifiDataModel {

    String name;
    int state;
    Integer level;
    boolean lock;
    ScanResult result;

    public WifiDataModel(String name, int state, Integer level, boolean lock, ScanResult result) {
        this.name=name;
        this.state=state;
        this.level=level;
        this.lock=lock;
        this.result = result;
    }


    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public Integer getLevel() {
        return level;
    }

    public boolean getLock() {
        return lock;
    }

    public ScanResult getResult() {
        return result;
    }

}