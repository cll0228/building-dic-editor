package com.lezhi.app.test;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.test.util.FloorUtil;
import com.lezhi.app.util.PagingUtil;
import org.apache.ibatis.session.RowBounds;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/8/8.
 */
@Component
public class FixTopFloor implements Batch {

    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;

    // 33w/43w 解析到总楼层
    @Override
    public void start() throws IOException {
        final int PAGE_SIZE = 100000;
        int buildingCount = this.buildingDicMapper.count();

        PagingUtil.pageIndex(buildingCount, PAGE_SIZE, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<BuildingDic> list = buildingDicMapper.findAll(rowBounds);

            Set<BuildingDic> set = new HashSet<>();

            for (BuildingDic b : list) {
                List<RoomDic> rooms = roomDicMapper.findTopRooms(b.getId());
                Integer topFloor = parseTop(rooms);
                if (topFloor != null) {
                    b.setTotalFloor(topFloor);
                    set.add(b);
                }
            }
            if (!set.isEmpty()) {
                buildingDicMapper.batchUpdate(set);
            }
            System.out.println("fix total floor progress:" + pageNo + "/" + pageCount);
            return true;
        });
    }

    private Integer parseTop(List<RoomDic> rooms) {
        if (CollectionUtils.isEmpty(rooms))
            return null;
        // roomNo 是从大到小排列的
        String roomNo = rooms.get(0).getName();
        return FloorUtil.parseFloor(roomNo);
    }
}
