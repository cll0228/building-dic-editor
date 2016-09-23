package com.lezhi.app.test;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.test.util.FloorUtil;
import com.lezhi.app.test.util.TimeUtil;
import com.lezhi.app.util.PagingUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Colin Yan on 2016/9/14.
 */
@Component
public class FixArea implements Batch {

    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;

    private static final AtomicInteger bi = new AtomicInteger(0);

    private int updateCount = 0;

    //  未填充前，有面积的数量 2722729，补了61159
    @Override
    public void start() throws IOException {

        final int PAGE_SIZE = 100000;
        int buildingCount = this.buildingDicMapper.count();

        PagingUtil.pageIndex(buildingCount, PAGE_SIZE, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<BuildingDic> list = buildingDicMapper.findAll(rowBounds);

            for (BuildingDic b : list) {
                List<RoomDic> rooms = roomDicMapper.findTopRooms(b.getId());
                parseAreaMap(rooms);
                System.out.println(TimeUtil.now() + " fix area progress:" + bi.addAndGet(1) + "/" + buildingCount);
            }

            return true;
        });

        System.out.println("update:" + updateCount);
    }

    private void parseAreaMap(List<RoomDic> rooms) {
        if (rooms == null || rooms.isEmpty())
            return;

        // floor (1 based), room index (1 based), room id
        Map<Integer, Map<Integer, Integer>> reverseMap = new HashMap<>();

        // 1-based
        int maxFloor = -1;
        // 1-based
        int maxRoomIndex = -1;

        for (RoomDic r : rooms) {
            Integer placeFloor = FloorUtil.parseFloor(r.getName());
            r.setFloorInt(placeFloor);
            Integer roomIndex = FloorUtil.parseRoomIndex(r.getName());
            r.setRoomIndex(roomIndex);

            if (placeFloor == null || roomIndex == null) {
                continue;
            }

            Map<Integer, Integer> inner = null;
            if (reverseMap.containsKey(placeFloor)) {
                inner = reverseMap.get(placeFloor);
            } else {
                inner = new HashMap<>();
                reverseMap.put(placeFloor, inner);
            }
            inner.put(roomIndex, r.getId());


            if (placeFloor > maxFloor) {
                maxFloor = placeFloor;
            }

            if (roomIndex > maxRoomIndex) {
                maxRoomIndex = roomIndex;
            }
        }

        if (maxFloor <= 0 || maxRoomIndex <= 0)
            return;

        if (maxFloor < 5)
            return;

        Double map[][] = new Double[maxFloor][maxRoomIndex];

        for (RoomDic r : rooms) {
            Integer placeFloor = r.getFloorInt();
            Integer roomIndex = r.getRoomIndex();
            if (placeFloor == null || roomIndex == null) {
                continue;
            }
            Double area = r.getArea();
            map[placeFloor - 1][roomIndex - 1] = area == null ? 0 : area;
        }

        Set<RoomDic> set = new HashSet<>();

        for (int i = 0; i < maxRoomIndex; i++) {

            int hasAreaFloors = 0;
            double minArea = 10000000000D;
            double maxArea = 0;
            double totalArea = 0;

            for (int j = 1; j < maxFloor - 1; j++) {
                if (map[j][i] != null && map[j][i] > 0) {
                    hasAreaFloors++;

                    if (map[j][i] > maxArea) {
                        maxArea = map[j][i];
                    }
                    if (map[j][i] < minArea) {
                        minArea = map[j][i];
                    }
                    totalArea += map[j][i];
                }
            }

            if (hasAreaFloors * 2 > maxFloor - 2 && minArea >= 30 && maxArea <= 200 && (maxArea - minArea) / maxArea <= .1) {
                double calcArea = totalArea / hasAreaFloors;

                for (int j = 1; j < maxFloor - 1; j++) {
                    // notice , compare with 0, not null
                    if (map[j][i] != null && map[j][i] == 0) {

                        map[j][i] = calcArea;
                        //  找回roomId ，设置area
                        Integer roomId = reverseMap.get(j + 1).get(i + 1);
                        //System.out.println(roomId);
                        //System.out.println(roomId + "," + (j + 1) + "F" + (i + 1) + "," + calcArea);
                        RoomDic roomDic = new RoomDic();
                        roomDic.setId(roomId);
                        roomDic.setArea(calcArea);
                        set.add(roomDic);
                    }
                }
            }
        }

        if (!set.isEmpty()) {
            roomDicMapper.batchUpdateArea(set);
            updateCount += set.size();
        }
    }

}
