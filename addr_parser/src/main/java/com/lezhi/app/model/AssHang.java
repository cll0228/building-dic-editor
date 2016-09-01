package com.lezhi.app.model;

import java.util.Date;

/**
 * Created by Colin Yan on 2016/8/31.
 */
public class AssHang {

    /*
    quotation_id	bigint(100)		NO				select,insert,update,references
residence_id	int(30)		NO				select,insert,update,references
property_type_id	tinyint(30)		YES				select,insert,update,references
floor_structure_id	tinyint(30)		YES				select,insert,update,references
quotation_total_price	decimal(9,2)		YES				select,insert,update,references
quotation_unit_price	bigint(16)		YES				select,insert,update,references
quotation_date	date		NO				select,insert,update,references
property_area	decimal(9,2)		YES				select,insert,update,references
basement_area	float		YES				select,insert,update,references
total_floor	smallint(30)		YES				select,insert,update,references
place_floor	smallint(30)		YES				select,insert,update,references
property_room	tinyint(30)		YES				select,insert,update,references
     */

    private Long quotationId;

    private Integer residenceId;

    private Integer propertyTypeId;

    private Integer floorStructureId;

    private Double totalPrice;

    private Integer unitPrice;

    private Date quotationDate;

    private Double propertyArea;

    private Double basementArea;
    private Integer totalFloor;
    private Integer placeFloor;
    private Integer propertyRoom;

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public Integer getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Integer residenceId) {
        this.residenceId = residenceId;
    }

    public Integer getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Integer propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public Integer getFloorStructureId() {
        return floorStructureId;
    }

    public void setFloorStructureId(Integer floorStructureId) {
        this.floorStructureId = floorStructureId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
    }

    public Double getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(Double propertyArea) {
        this.propertyArea = propertyArea;
    }

    public Double getBasementArea() {
        return basementArea;
    }

    public void setBasementArea(Double basementArea) {
        this.basementArea = basementArea;
    }

    public Integer getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }

    public Integer getPlaceFloor() {
        return placeFloor;
    }

    public void setPlaceFloor(Integer placeFloor) {
        this.placeFloor = placeFloor;
    }

    public Integer getPropertyRoom() {
        return propertyRoom;
    }

    public void setPropertyRoom(Integer propertyRoom) {
        this.propertyRoom = propertyRoom;
    }
}
