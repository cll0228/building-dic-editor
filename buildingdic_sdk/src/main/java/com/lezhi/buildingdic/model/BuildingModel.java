package com.lezhi.buildingdic.model;//

import java.io.Serializable;

/**
 * 楼栋Model
 */
public class BuildingModel implements Serializable {

    /**
     * 楼栋号
     */
    private String buildingNo;
    /**
     * 楼栋Id
     */
    private Integer buildingId;
    /**
     * 总楼层
     */
    private Integer totalFloor;
    /**
     * 总户数
     */
    private Integer totalRoomNum;
    /**
     * 百度经度
     */
    private Double baiduLon;
    /**
     * 百度纬度
     */
    private Double baiduLat;


    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public Integer getTotalRoomNum() {
        return totalRoomNum;
    }

    public void setTotalRoomNum(Integer totalRoomNum) {
        this.totalRoomNum = totalRoomNum;
    }

    public Double getBaiduLon() {
        return baiduLon;
    }

    public void setBaiduLon(Double baiduLon) {
        this.baiduLon = baiduLon;
    }

    public Double getBaiduLat() {
        return baiduLat;
    }

    public void setBaiduLat(Double baiduLat) {
        this.baiduLat = baiduLat;
    }
}
