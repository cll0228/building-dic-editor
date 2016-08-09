package com.lezhi;

import com.lezhi.app.mapper.AddrSourceMapper;
import com.lezhi.app.model.Address;
import com.lezhi.app.util.AddressExtractor;
import com.lezhi.app.util.AddressModel;
import com.lezhi.app.util.PreHandle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Colin Yan on 2016/7/12.
 */
@Deprecated
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/conf/applicationContext.xml"})
public class ParseAddressTestOld {

    @Autowired
    private AddrSourceMapper addrSourceMapper;

    private static final File PATH = new File("D:\\房屋地址数据");

    private final String tableName = "address_unique_except_deal";

    private File exportFile = new File(PATH, tableName + "(gbk).txt");
    private File matchedResultFile = new File(PATH, tableName + "(matched,tab).txt");
    private File notMatchedResultFile = new File(PATH, tableName + ".not-matched.txt");
    private File notMatchedResultFile_Sorted = new File(PATH, tableName + ".not-matched-sorted.txt");
    private File matchedResultFile_SplitPath = new File(PATH, tableName + "(matched,tab).spilt");

    @Test
    public void exportToFile() throws IOException {
        final int PAGE_SIZE = 1000000;
        int count = addrSourceMapper.count(tableName);
        System.out.println("总数量:" + count);
        FileOutputStream fos = new FileOutputStream(exportFile);
        for (int i = 0; i < count; i += PAGE_SIZE) {
            RowBounds rowBounds = new RowBounds(i, PAGE_SIZE);
            List<Address> list = addrSourceMapper.findAddress(tableName, rowBounds);
            System.out.println("分布查询结果数量:" + list.size());
            for (Address address : list) {
                if (StringUtils.isBlank(address.getAddress()))
                    continue;
                fos.write(String.valueOf(address.getRefId()).getBytes("gbk"));
                fos.write('\t');
                fos.write(address.getAddress().getBytes("gbk"));
                fos.write('\t');
                if (StringUtils.isNotBlank(address.getRoomNo()))
                    fos.write(address.getRoomNo().getBytes("gbk"));
                fos.write('\n');
            }
        }
        fos.close();
    }

    @Test
    public void matchAddress() throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(exportFile), "gbk"));

        FileOutputStream fos = new FileOutputStream(notMatchedResultFile);

        FileOutputStream sqlTab = new FileOutputStream(matchedResultFile);

        long start = System.currentTimeMillis();

        long x = 0;

        long success = 0;
        long failed = 0;

        String line;
        while ((line = br.readLine()) != null) {
            String[] items = line.split("\t");
            final String oriAddress = items[1];

            String address = PreHandle.handle(oriAddress);
            AddressModel r = AddressExtractor.parseAll(address);

            if (r == null) {
                //System.out.println(r +"\t\t\t->\t\t" + line);
                fos.write(line.getBytes("gbk"));
                fos.write('\n');
                failed++;
            } else {
                // System.out.println(r +"\t\t\t->\t\t" + line);
                sqlTab.write((items[0] + "\t" + oriAddress + "\t" + r.binTab() + "\n").getBytes("gbk"));
                success++;
            }

            if (x % 500000 == 0) {
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
        br.close();

        fos.close();
        sqlTab.close();

        split(matchedResultFile);
    }

    @Test
    public void splitManual() throws Exception {
        split(matchedResultFile);
    }

    private void split(File input) throws Exception {
        matchedResultFile_SplitPath.mkdirs();
        FileUtils.cleanDirectory(matchedResultFile_SplitPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input), "gbk"));
        Map<Integer, FileOutputStream> map = new HashMap<>();
        int PAGE_SIZE = 500000;
        int index = 0;
        String line;
        while ((line = br.readLine()) != null) {
            int fileNum = index / PAGE_SIZE;
            FileOutputStream fos;
            if (map.containsKey(fileNum)) {
                fos = map.get(fileNum);
            } else {
                fos = new FileOutputStream(new File(matchedResultFile_SplitPath, fileNum + "(import,tab).txt"));
                map.put(fileNum, fos);
            }
            fos.write(line.getBytes("gbk"));
            fos.write('\n');
            index++;
        }
        map.values().forEach(IOUtils::closeQuietly);
        System.out.printf("文件分割完成");
    }

    @Test
    public void sortFile() throws IOException {
        List<String> lines = FileUtils.readLines(notMatchedResultFile, "gbk");

        Collections.sort(lines, (o1, o2) -> {
            try {
                return StringUtils.split(o1, "\t")[1].compareTo(StringUtils.split(o2, "\t")[1]);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        });

        FileOutputStream fos = new FileOutputStream(notMatchedResultFile_Sorted);
        for (String s : lines) {
            fos.write(s.getBytes("gbk"));
            fos.write('\n');
        }
        fos.close();
    }

    private void convertEncode(File input, String charsetOld, String charsetNew) throws IOException {
        if (charsetOld.equals(charsetNew)) {
            return;
        }
        List<String> lines = FileUtils.readLines(input, charsetOld);
        File file = new File(input.getAbsolutePath() + "." + charsetNew);

        final FileOutputStream fos = new FileOutputStream(file);
        for (String str : lines) {
            fos.write(str.getBytes(charsetNew));
            fos.write('\n');
        }
        IOUtils.closeQuietly(fos);
    }

    @Test
    public void testConvert() throws IOException {
        //convertEncode(new File("D:\\房屋地址数据\\address_unique_deal.txt"), "utf-8", "gbk");
    }
}
