package com.lezhi.app.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Colin Yan on 2016/7/12.
 */
public class Address {

    private long id;

    private long refId;

    private String address;

    private Integer residenceId;
    private Double area;
    private String src;

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        if (roomNo != null) {
            roomNo = roomNo.trim();
            roomNo = StringUtils.removeStart(roomNo, "'");
            roomNo = StringUtils.removeEnd(roomNo, "'");
            if (roomNo.length() == 0)
                roomNo = null;
        }
        this.roomNo = roomNo;
    }

    private String roomNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null) {
            this.address = address.trim();
        }
    }

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }

    public Integer getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Integer residenceId) {
        this.residenceId = residenceId;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
