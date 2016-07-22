package com.lezhi.app.model;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public class BuildingDic {

    private Integer id;
    private String name;
    private Integer residenceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Integer residenceId) {
        this.residenceId = residenceId;
    }
}
