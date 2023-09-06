package com.base.sbc.client.amc.entity;

public class Team {


    /**
     * 产品季id
     */
    private String seasonId;
    /**
     * 团队名称
     */
    private String name;
    /**
     * 状态正常(0),停用(1)
     */
    private String status;

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
