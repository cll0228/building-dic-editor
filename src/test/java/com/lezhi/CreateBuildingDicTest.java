package com.lezhi;

import com.lezhi.app.mapper.*;
import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.ResolvedAddress;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.util.PagingUtil;
import org.apache.ibatis.session.RowBounds;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Colin Yan on 2016/8/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/conf/applicationContext.xml"})
public class CreateBuildingDicTest {

    @Autowired
    private ResolvedAddrMapper resolvedAddrMapper;
    @Autowired
    private AddrSourceMapper addrSourceMapper;
    @Autowired
    private ResidenceMapper residenceMapper;
    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;

    private static final String tableName = "resolved_address_except_deal";

    private static final File PATH = new File("D:\\房屋地址数据");
    private static final File BUILDING_DIC_FOLDER = new File(PATH, "BUILDING_DIC");

    private static final File BUILDING_DIC_INSERT_SQL = new File(PATH, "resolved_address_except_deal" + File.separator + "building.sql");
    private static final File ROOM_DIC_INSERT_SQL = new File(PATH, "resolved_address_except_deal" + File.separator + "room.sql");

    private Map<Integer, Set<BuildingDic>> roomTree = new HashMap<>();

    private int building_dic_id_base = 10000;

    @Test
    public void test1() throws Exception {
        //-Xms2048m -Xmx4096m -Xss1024K

        /*
        if (BUILDING_DIC_FOLDER.exists()) {
            FileUtils.cleanDirectory(BUILDING_DIC_FOLDER);
        } else {
            BUILDING_DIC_FOLDER.mkdirs();
        }
*/
        final int count = resolvedAddrMapper.countAll(tableName);

        PagingUtil.pageIndex(count, 100000, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<ResolvedAddress> list = resolvedAddrMapper.findAll(tableName, rowBounds);
            for (ResolvedAddress r : list) {
                createRoom(r);
            }
            list.clear();
            return true;
        });

        if (!BUILDING_DIC_INSERT_SQL.getParentFile().exists())
            BUILDING_DIC_INSERT_SQL.getParentFile().mkdirs();

        genInsertSql();
    }

    private void genInsertSql() throws Exception {
        final String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String sqlBuilding = "INSERT INTO t_building_dic(id  ,name  ,residence_id,`exists`  ,del_status  ,operator_id  ,modify_time) " +
                "VALUES (%id,'%name',%residence_id,1,0,1,'" + timeNow + "');\n";

        String sqlRoom = "INSERT INTO t_room_dic" +
                "(name, area, status, `exists`, floor_id, building_id, residence_id, ref_id, src, ori_address, del_status, operator_id, modify_time) " +
                "VALUES ('%name', %area, 20, 1, null, %building_id, %residence_id, %ref_id, '%src', '%ori_address', 0, 1, '" + timeNow + "');\n";
        // STATUS 可信的解析规则变40，不可信变20

        BufferedWriter bwBuilding = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(BUILDING_DIC_INSERT_SQL), "utf-8"));
        BufferedWriter bwRoom = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ROOM_DIC_INSERT_SQL), "utf-8"));

        bwBuilding.write("use all_address;truncate t_building_dic;\n");
        bwRoom.write("use all_address;truncate t_room_dic;\n");

        Set<Integer> residenceIdSet = roomTree.keySet();
        for (Integer rid : residenceIdSet) {
            Set<BuildingDic> buildingDics = roomTree.get(rid);
            for (BuildingDic b : buildingDics) {
                bwBuilding.write(sqlBuilding.replace("%id", b.getId().toString()).replace("%name", b.getName()).replace("%residence_id", b.getResidenceId().toString()));
                //bwBuilding.newLine();
                for (RoomDic r : b.getRoomDics()) {
                    bwRoom.write(sqlRoom.replace("%name", r.getName()).replace("%area", String.valueOf(r.getArea()))
                            .replace("%building_id", r.getBuildingId().toString()).replace("%residence_id", r.getResidenceId().toString()).replace("%ref_id", r.getRefId().toString())
                            .replace("%src", r.getSrc())
                            .replace("%ori_address", r.getOriAddress()));
                }
            }
        }

        bwBuilding.close();
        bwRoom.close();
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
        roomSet.add(r);
        //createXml(residenceId.toString(), building, roomNo, params);
    }

    private void createXml(String residenceIdStr, String building, String roomNo, Map<String, Object> params) {
        File outputFile = new File(BUILDING_DIC_FOLDER, residenceIdStr + File.separator + building + File.separator + roomNo + ".xml");
        if (outputFile.exists())
            return;

        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("room");
        if (params != null) {
            for (Map.Entry<String, Object> p : params.entrySet()) {
                root.addElement(p.getKey()).setText(String.valueOf(p.getValue()));
            }
        }
        XMLWriter writer = null;
        try {

            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            OutputFormat of = new OutputFormat();
            of.setEncoding("UTF-8");
            of.setIndent(true);
            of.setIndent("    ");
            of.setNewlines(true);
            writer = new XMLWriter(osw, of);
            writer.write(document);

            System.out.println("create room:" + outputFile.getAbsolutePath());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException ignore) {
            }
        }
    }
}
