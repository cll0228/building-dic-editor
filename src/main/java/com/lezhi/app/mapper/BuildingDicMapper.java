package com.lezhi.app.mapper;

import com.lezhi.app.model.BuildingDic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public interface BuildingDicMapper {

    int insert(BuildingDic buildingDic);

    int batchInsert(@Param("buildings") Set<BuildingDic> buildingDic);

    List<BuildingDic> find(@Param("residenceId") Integer residenceId, @Param("name") String name);

    int count();
    
    int updateBuildingStatus(BuildingDic dic);

    int updateTopFloor(BuildingDic bic);

    BuildingDic getBuildingByName(BuildingDic dic);
}
