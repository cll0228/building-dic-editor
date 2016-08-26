package com.lezhi.buildingdic.model;

import java.io.Serializable;

/**
 * 小区Model
 */
public class ResidenceModel implements Serializable {

    /**
     * 小区ID
     */
    private Integer residenceId;

    /**
     * 小区名称
     */
    private String residenceName;

    /**
     * 小区地址
     */
    private String residenceAddress;

    public Integer getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Integer residenceId) {
        this.residenceId = residenceId;
    }

    public String getResidenceName() {
        return residenceName;
    }

    public void setResidenceName(String residenceName) {
        this.residenceName = residenceName;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }
}
