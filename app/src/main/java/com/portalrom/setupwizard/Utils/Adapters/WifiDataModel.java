package com.portalrom.setupwizard.Utils.Adapters;

public class WifiDataModel {

    String name;
    Integer level;
    boolean lock;

    public WifiDataModel(String name, Integer level, boolean lock) {
        this.name=name;
        this.level=level;
        this.lock=lock;
    }

    public String getName() {
        return name;
    }

    public Integer getLevel() {
        return level;
    }

    public boolean getLock() {
        return lock;
    }
}