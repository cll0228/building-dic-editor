package com.lezhi.app.mapper;

import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.model.map.StdAddr;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public interface RoomDicMapper {

    void insert(RoomDic roomDic);

    int insertNewRoom(RoomDic roomDic);

    int deleteRoom(RoomDic ric);

    List<RoomDic> find(@Param("buildingId") Integer buildingId, @Param("name") String name);

    List<StdAddr> findRoomExists(@Param("rids") int rids[]);

    int count();

    int updateStatus(@Param("id") int id, @Param("status") int status);

    int countDealUnique();
    
    int update(RoomDic roomDic);
}
