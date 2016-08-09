package com.lezhi;

import com.lezhi.app.mapper.*;
import com.lezhi.app.model.ResolvedAddress;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Colin Yan on 2016/7/13.
 */
@Deprecated
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/conf/applicationContext.xml"})
public class Test3 {

    @Autowired
    private ResolvedAddrMapper resolvedAddrMapper;

    private static final File PATH = new File("D:\\房屋地址数据");


    //@Test
    public void createBuildingDic() throws Exception {
        final int PAGE_SIZE = 1000000;
        final int count = resolvedAddrMapper.countAll("resolved_address_deal");

        final File outputBuilding = new File(PATH, "t_building_dic.txt");
        final FileOutputStream fosBuilding = new FileOutputStream(outputBuilding);
        for (int i = 0; i < count; i += PAGE_SIZE) {
            RowBounds rowBounds = new RowBounds(i, PAGE_SIZE);
            List<ResolvedAddress> list = resolvedAddrMapper.findAll("resolved_address_deal", rowBounds);

            for (ResolvedAddress address : list) {

                // List<BuildingDic> buildingDics = buildingDicMapper.find(address.getResidenceId(), address.getBuilding());
                // if (buildingDics == null || buildingDics.isEmpty()) {
                // BuildingDic buildingDic = new BuildingDic();
                //buildingDic.setName(address.getBuilding());
                //buildingDic.setResidenceId(address.getResidenceId());
                //buildingDicMapper.insert(buildingDic);
                //有重复的
                fosBuilding.write(address.getBuilding().getBytes("gbk"));
                fosBuilding.write('\t');
                fosBuilding.write(String.valueOf(address.getResidenceId()).getBytes("gbk"));
                fosBuilding.write('\t');
                fosBuilding.write("0".getBytes("gbk"));
                fosBuilding.write('\n');
                //}
            }
        }
        fosBuilding.close();
        delRepeat(outputBuilding);
    }

    //@Test
    public void createRoomDic() throws Exception {
        final int PAGE_SIZE = 1000000;
        final int count = resolvedAddrMapper.countAll("resolved_address_deal"); //WithBuilding();

        final File outputRoom = new File(PATH, "t_room_dic.txt");
        final FileOutputStream fosRoom = new FileOutputStream(outputRoom);
        for (int i = 0; i < count; i += PAGE_SIZE) {
            RowBounds rowBounds = new RowBounds(i, PAGE_SIZE);
            List<ResolvedAddress> list = resolvedAddrMapper.findAllWithBuilding("resolved_address_deal",rowBounds);
            for (ResolvedAddress address : list) {
                /*
                //有重复的
                fosRoom.write(address.getRoom().getBytes("gbk"));
                fosRoom.write('\t');
                Integer buildingId = address.getBuildingId();
                fosRoom.write(String.valueOf(buildingId).getBytes("gbk"));
                fosRoom.write('\t');
                fosRoom.write(String.valueOf(address.getArea()).getBytes("gbk"));
                fosRoom.write('\n');
                //}*/
            }
        }
        fosRoom.close();
        delRepeat(outputRoom);
    }

    //@Test
    public void manualDelRepeat() throws IOException {
        File output = new File(PATH, "t_building_dic.txt");
        delRepeat(output);
    }

    //@Test
    public void manualDelRepeatRoom() throws IOException {
        File output = new File(PATH, "t_room_dic.txt");
        delRepeat(output);
    }

    private void delRepeat(File input) throws IOException {
        List<String> lines = FileUtils.readLines(input, "gbk");
        Map<String, String> map = new HashMap<>();

        for (String l : lines) {
            l = l.toUpperCase();
            String arr[] = l.split("\t");
            map.put(arr[0] + "\t" + arr[1], arr[2]);
        }

        File file = new File(input.getAbsolutePath() + ".去重");
        final FileOutputStream fos = new FileOutputStream(file);
        for (String str : map.keySet()) {
            fos.write((str + "\t" + map.get(str)).getBytes("gbk"));
            fos.write('\n');
        }
        IOUtils.closeQuietly(fos);
        System.out.println("已经去重");
    }

}
