package com.lezhi;

import com.lezhi.app.mapper.AddrSourceMapper;
import com.lezhi.app.model.Address;
import com.lezhi.app.model.Residence;
import com.lezhi.app.service.ResidenceMatch;
import com.lezhi.app.util.AddressExtractor;
import com.lezhi.app.util.AddressModel;
import com.lezhi.app.util.PagingUtil;
import com.lezhi.app.util.PreHandle;
import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.List;

/**
 * Created by Colin Yan on 2016/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/conf/applicationContext.xml"})
public class ParseAddressTestNew {

    @Autowired
    private AddrSourceMapper addrSourceMapper;

    private static final File PATH = new File("D:\\房屋地址数据");

    @Autowired
    private ResidenceMatch residenceMatch;

    private long success = 0;
    private long failed = 0;

    @Before
    public void begin() {
    }

    @Test
    public void testMatchResidence() {
        //靖宇东路55弄
        Residence r = residenceMatch.match("靖宇东路55弄");
        System.out.println(r);

    }
    @Test
    public void start() throws IOException {

        final String fromTable[] = new String[]{"address_unique_deal", "address_unique_except_deal"};
        final String toTable[] = new String[]{"resolved_address_deal", "resolved_address_except_deal"};
        final int[] level = new int[]{10, 20};
        final int[] begins = new int[]{600000 , 0};

        for (int i = 0; i < fromTable.length; i++) {
            final File RESOLVED_ADDRESS_INSERT_SQL = new File(PATH, "地址解析输出" + File.separator + fromTable[i] + ".sql");
            final File RESOLVED_ADDRESS_INSERT_SQL_FAILED = new File(PATH, "地址解析输出" + File.separator + fromTable[i] + ".failed");
            run(fromTable[i], toTable[i], level[i], RESOLVED_ADDRESS_INSERT_SQL, RESOLVED_ADDRESS_INSERT_SQL_FAILED, begins[i]);
        }

    }

    public void run(String fromTable, String toTable, int level, File successOutput, File failedOutput, int position) throws IOException {
        long b = System.currentTimeMillis();

        if (!successOutput.getParentFile().exists())
            successOutput.getParentFile().mkdirs();

        if (!failedOutput.getParentFile().exists())
            failedOutput.getParentFile().mkdirs();

        final int PAGE_SIZE = 1000000;
        int count = addrSourceMapper.count(fromTable);
        System.out.println("总数量:" + count);

        String sql = "INSERT INTO " + toTable + "(ref_id, ori_address, residence, building, room, residence_id, area, `level`, is_available, `src`)" +
                "VALUES (%ref_id%, '%ori_address%', '%residence%', '%building%', '%room%', %residence_id%, %area%, " + level + ", %is_available%, '%src%');\n";

        BufferedWriter bwSuccess = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(successOutput, false), "utf-8"));
        BufferedWriter bwFailed = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(failedOutput, false), "utf-8"));

        bwSuccess.write("use all_address;truncate " + toTable +";\n");

        PagingUtil.pageIndex(count, 100000, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, PAGE_SIZE);
            List<Address> list = addrSourceMapper.findAddress(fromTable, rowBounds);
            long start = System.currentTimeMillis();
            long x = 0;
            for (Address a : list) {
                final String oriAddress = a.getAddress();
                if (oriAddress == null)
                    throw new NullPointerException("原始地址不能为空");

                String address = PreHandle.handle(oriAddress);
                AddressModel r = AddressExtractor.parseAll(address);

                if (r == null) {
                    failed++;
                    bwFailed.write(address);
                    bwFailed.newLine();
                } else {

                    int is_available = 1;
                    Integer residenceId = a.getResidenceId();
                    if (residenceId == null) {
                        Residence residence = residenceMatch.match(r.getResidence());
                        if (residence != null) {
                            residenceId = residence.getId();
                        }
                    }
                    if (residenceId == null)
                        is_available = 0;

                    bwSuccess.write(sql.replace("%ref_id%", String.valueOf(a.getRefId())).replace("%ori_address%", a.getAddress())
                            .replace("%residence_id%", String.valueOf(residenceId))
                            .replace("%is_available%", String.valueOf(is_available))
                            .replace("%residence%", r.getResidence()).replace("%building%", r.getBuilding()).replace("%room%", r.getRoom())
                            .replace("%area%", String.valueOf(a.getArea())).replace("%src%", a.getSrc()));
                    bwFailed.newLine();
                    success++;
                }
                if (x % 10000 == 0) {
                    long now = System.currentTimeMillis();
                    double past = (now - start) / 1000.0;
                    double cp = 0;
                    double fcf = 0;
                    if (success > 0 && failed > 0) {
                        cp = success / 1.0 / failed;
                        fcf = failed / 1.0 / (success + failed);
                    }
                    System.out.println("处理" + x + "条数据，花费时间 " + past + "秒" + "，平均每秒" + (x / past) + "条" + ",成功/失败" + success + "/" + failed + " -> " + cp + ":1" +
                            "错误率:" + fcf);
                }
                x++;
            }

            return true;
        });

        long e = System.currentTimeMillis();
        double past = (e - b) / 1000.0;
        System.out.println("处理" + count + "条数据，花费时间 " + past + "秒" + "，平均每秒" + (count / past) + "条" + ",成功/失败" + success + "/" + failed);
    }

}
