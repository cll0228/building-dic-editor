package com.lezhi.app.test;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.ResolvedAddrMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.ResolvedAddress;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.util.PagingUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Colin Yan on 2016/8/5.
 */
@Component
public class CreateBuildingDicTest {

    @Autowired
    private ResolvedAddrMapper resolvedAddrMapper;
    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;

    private static final String tableName = "address_unique";

    private Map<Integer, Set<BuildingDic>> roomTree = new HashMap<>();

    private int building_dic_id_base = 100;

    private Date now;

    public void start() throws Exception {
        final int count = resolvedAddrMapper.countAll(tableName);
        PagingUtil.pageIndex(count, 100000, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<ResolvedAddress> list = resolvedAddrMapper.findAll(tableName, rowBounds);
            for (ResolvedAddress r : list) {
                createRoom(r);
            }
            list.clear();
            System.out.println("(1/2) mem create progress:" + pageNo + "/" + pageCount);
            return true;
        });

        now = new Date();
        genInsertSql();
    }

    private void genInsertSql() throws Exception {

        Set<BuildingDic> buildingBuffer = new HashSet<>();
        Set<RoomDic> roomBuffer = new HashSet<>();

        int totalBuildingCount = 0;

        for (Integer rid : roomTree.keySet()) {
            totalBuildingCount += roomTree.get(rid).size();
        }

        int curBuilding = 1;

        for (Integer rid : roomTree.keySet()) {

            for (BuildingDic b : roomTree.get(rid)) {
                buildingBuffer.add(b);
                if (buildingBuffer.size() >= 1000) {
                    this.buildingDicMapper.batchInsert(buildingBuffer);
                    buildingBuffer.clear();
                }
                for (RoomDic r : b.getRoomDics()) {
                    roomBuffer.add(r);
                    if (roomBuffer.size() >= 1000) {
                        this.roomDicMapper.batchInsert(roomBuffer);
                        roomBuffer.clear();
                    }
                }

                System.out.println("(2/2)insert building (with room) " + curBuilding + "/" + totalBuildingCount);
                curBuilding++;
            }
        }

        if (!buildingBuffer.isEmpty()) {
            this.buildingDicMapper.batchInsert(buildingBuffer);
            buildingBuffer.clear();
        }
        if (!roomBuffer.isEmpty()) {
            this.roomDicMapper.batchInsert(roomBuffer);
            roomBuffer.clear();
        }
    }

    private void createRoom(ResolvedAddress resolvedAddress) {
        Integer residenceId = resolvedAddress.getResidenceId();
        String buildingName = resolvedAddress.getBuilding();
        String roomNo = resolvedAddress.getRoom();

        if (residenceId == null || buildingName == null || roomNo == null)
            return;

        Set<BuildingDic> buildings = null;

        if (!roomTree.containsKey(residenceId)) {
            buildings = new HashSet<>();
            roomTree.put(residenceId, buildings);
        } else {
            buildings = roomTree.get(residenceId);
        }

        BuildingDic building = null;

        for (BuildingDic b : buildings) {
            if (b.getName().equals(buildingName)) {
                building = b;
                break;
            }
        }

        if (building == null) {
            building = new BuildingDic();
            building.setId(++this.building_dic_id_base);
            building.setResidenceId(residenceId);
            building.setName(buildingName);
            building.setUrefId(resolvedAddress.getId());
            buildings.add(building);
        }

        Set<RoomDic> roomSet = building.getRoomDics();

        for (RoomDic r : roomSet) {
            if (r.getName().equals(roomNo)) {
                return;
            }
        }

        RoomDic r = new RoomDic();
        r.setName(roomNo);
        r.setResidenceId(residenceId);
        r.setArea(resolvedAddress.getArea());
        r.setBuildingName(buildingName);
        r.setBuildingId(building.getId());
        r.setOriAddress(resolvedAddress.getOriAddress());
        r.setRefId(resolvedAddress.getRefId());
        r.setSrc(resolvedAddress.getSrc());
        if (resolvedAddress.getIsDeal() != null && resolvedAddress.getIsDeal() && resolvedAddress.getParsedScore() != null && resolvedAddress.getParsedScore() > 60) {
            r.setStatus(40);
        } else {
            r.setStatus(20);
        }
        r.setUrefId(resolvedAddress.getId());
        r.setOperatorId(1);
        r.setModifyTime(now);
        roomSet.add(r);
        //createXml(residenceId.toString(), building, roomNo, params);
    }

}
