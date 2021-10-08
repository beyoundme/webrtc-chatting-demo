package com.beyoundme.bean;

public class Depart {

    /**
     * 组织名称
     */
    private String uuid;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 上级组织
     */
    private String orgRelation;


    public Depart(String uuid, String orgName, String orgRelation) {
        this.uuid = uuid;
        this.orgName = orgName;
        this.orgRelation = orgRelation;
    }

    public Depart() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgRelation() {
        return orgRelation;
    }

    public void setOrgRelation(String orgRelation) {
        this.orgRelation = orgRelation;
    }
}
