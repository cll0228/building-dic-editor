package com.lezhi.app.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public class BuildingDic {

    private Integer id;
    private String name;
    private Integer residenceId;
    private Integer totalFloor;
    private Integer delStatus;
    private Integer operatorId;
    private Timestamp modifyTime;

    private Double lat;
    private Double lon;

    private Integer totalRoomNum;

    public Integer getTotalRoomNum() {
        return totalRoomNum;
    }

    public void setTotalRoomNum(Integer totalRoomNum) {
        this.totalRoomNum = totalRoomNum;
    }

    private Long urefId;

    public Long getUrefId() {
        return urefId;
    }

    public void setUrefId(Long urefId) {
        this.urefId = urefId;
    }

    private Set<RoomDic> roomDics = new HashSet<>();

    public Set<RoomDic> getRoomDics() {
        return roomDics;
    }

    public void setRoomDics(Set<RoomDic> roomDics) {
        this.roomDics = roomDics;
    }

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

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
