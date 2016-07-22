package com.lezhi.app.mapper;

import com.lezhi.app.model.RoomDic;
import com.lezhi.app.model.map.StdAddr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public interface StatisticsMapper {
    int countDealUnique();
    int countDealResolved();
}
