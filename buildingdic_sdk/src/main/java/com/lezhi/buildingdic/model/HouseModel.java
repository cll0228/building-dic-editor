package com.lezhi.buildingdic.model;//

import java.io.Serializable;

/**
 * 房屋Model
 */
public class HouseModel implements Serializable {

    /**
     * 房屋Id
     */
    private Integer houseId;
    /**
     * 朝向ID
     */
    private Integer towardsId;
    /**
     * 房屋类型（住宅等）
     */
    private Integer propertyTypeId;
    /**
     * 楼层
     */
    private Integer placeFloor;
    /**
     * 总楼层
     */
    private Integer totalFloor;
    /**
     * 面积
     */
    private Double propertyArea;
    /**
     * 房间数量
     */
    private Integer roomNum;
    /**
     * 室号，如23
     */
    private String roomNo;
    /**
     * 小区ID
     */
    private Integer residenceId;
    /**
     * 楼栋ID
     */
    private Integer buildingId;
    /**
     * 区县ID
     */
    private Integer districtId;
    /**
     * 板块ID
     */
    private Integer blockId;
    /**
     * 竣工日期
     */
    private String residenceAccomplishData;

    private Integer planeType;

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public Integer getTowardsId() {
        return towardsId;
    }

    public void setTowardsId(Integer towardsId) {
        this.towardsId = towardsId;
    }

    public Integer getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Integer propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public Integer getPlaceFloor() {
        return placeFloor;
    }

    public void setPlaceFloor(Integer placeFloor) {
        this.placeFloor = placeFloor;
    }

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public Double getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(Double propertyArea) {
        this.propertyArea = propertyArea;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Integer residenceId) {
        this.residenceId = residenceId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getResidenceAccomplishData() {
        return residenceAccomplishData;
    }

    public void setResidenceAccomplishData(String residenceAccomplishData) {
        this.residenceAccomplishData = residenceAccomplishData;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getPlaneType() {
        return planeType;
    }

    public void setPlaneType(Integer planeType) {
        this.planeType = planeType;
    }
}
