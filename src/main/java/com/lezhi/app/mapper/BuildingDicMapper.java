package com.lezhi.app.mapper;

import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.Residence;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public interface BuildingDicMapper {

    int insert(BuildingDic buildingDic);

    List<BuildingDic> find(@Param("residenceId") Integer residenceId, @Param("name") String name);

    int count();
    
    int updateBuildingStatus(Integer buildingId);
}
