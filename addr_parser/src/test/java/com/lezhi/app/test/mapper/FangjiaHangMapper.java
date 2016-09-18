package com.lezhi.app.test.mapper;

import com.lezhi.app.test.model.FangjiaBuildingInfo;
import com.lezhi.app.test.model.FangjiaResidenceInfo;
import com.lezhi.app.test.model.FangjiaRoomInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FangjiaHangMapper {

    List<FangjiaResidenceInfo> getFjResidenceInfo();

    List<FangjiaResidenceInfo> getResidenceByName(@Param("residenceName")String residenceName);

    List<FangjiaBuildingInfo> getFjBuildingInfo(@Param("fjResidenceId")Integer fjResidenceId);

    List<FangjiaBuildingInfo> getBuildingInfo(@Param("residenceId")Integer residenceId);

    int updateBuildingInfo(@Param("id")Integer id, @Param("totalFloor")Integer totalFloor, @Param("lon")Double lon,
                           @Param("lat")Double lat, @Param("roomTotal")Integer roomTotal);

    void updateResidenceBuildingNum(@Param("residenceId")Integer residenceId,
                                    @Param("buildingNum")String buildingNum);

    List<FangjiaResidenceInfo> getAllFjResidenceInfo(@Param("buildingNum")Integer buildingNum);

    List<FangjiaResidenceInfo> getAllFjResidenceInfoForRoom();

    List<FangjiaRoomInfo> getFjRoomInfo(@Param("residenceId")Integer residenceId);

    List<FangjiaRoomInfo> getRoomInfo(@Param("residenceId")Integer residenceId);

    void updateRommType(@Param("id")Integer id, @Param("roomType")String roomType);

    void updateResidenceFj(@Param("residenceId")Integer residenceId,
                             @Param("fjresidenceId")Integer fjresidenceId);

    List<FangjiaResidenceInfo> getAllFjResidence();

    List<FangjiaResidenceInfo> getResidenceByAddress(@Param("fjaddress")String fjaddress);
}