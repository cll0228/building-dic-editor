package com.lezhi.app.util;

/**
 * Created by Colin Yan on 2016/7/18.
 */
public class Address2 implements AddressModel {

    // XXX(小区名)XXX号XXX室
    private String residence;
    private String building;
    private String room;


    @Override
    public int getScore() {
        if (residence.matches("^[\\u4E00-\\u9FA5]+$") && building.matches("^\\d+$") && building.matches("^\\d+$") && room.matches("^\\d{3,4}$")) {
            return 99;
        }
        return 30;
    }

    public Address2(String residence, String building, String room) {
        this.residence = residence;
        this.building = building;
        this.room = room;
    }

    @Override
    public String toString() {
        return residence + "<小区>" + building + "<号>" + room +"<室>";
    }

    @Override
    public String binTab() {
        return residence + "\t" + building + "\t" + room;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
