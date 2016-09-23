package com.lezhi.app.mapper;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public interface StatisticsMapper {
    int countBuilding();
    int buildingWithCoordinate();

    int countRoom();
    int countRoomDeal();

    int countRoomLocked();

    int residenceCount();
    int residenceCoverdCount();
    int houseWithArea();
    int houseWithoutArea();
    int houseWithTowards();
    int houseWithHuxing();
    int parseConfirmedHouseCount();
    int parseUnconfirmedHouseCount();
}
