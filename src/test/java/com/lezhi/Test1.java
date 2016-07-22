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
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Colin Yan on 2016/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/conf/applicationContext.xml"})
public class Test1 {

    @Autowired
    private AddrSourceMapper addrSourceMapper;

    private static final File PATH = new File("D:\\房屋地址数据");

    private File exportFile = new File(PATH, "address_unique_deal.txt.gbk");

    @Test
    public void exportToFile() throws IOException {
        final int PAGE_SIZE = 1000000;
        int count = addrSourceMapper.countAll_300w();
        FileOutputStream fos = new FileOutputStream(exportFile);
        for (int i = 0; i < count; i += PAGE_SIZE) {
            RowBounds rowBounds = new RowBounds(i, PAGE_SIZE);
            List<Address> list = addrSourceMapper.findAllAddress_300w(rowBounds);
            for (Address address : list) {
                if (StringUtils.isBlank(address.getAddress()))
                    continue;
                fos.write(String.valueOf(address.getId()).getBytes("gbk"));
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

        FileOutputStream fos = new FileOutputStream(new File(exportFile.getAbsolutePath() + ".not-matched3(new version)"));

        File sqlTabFile = new File(exportFile.getAbsolutePath() + ".import.tab");
        FileOutputStream sqlTab = new FileOutputStream(sqlTabFile);

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

        split(sqlTabFile);
    }

    @Test
    public void splitManual() throws Exception {
        split(new File("D:\\房屋地址数据\\address_unique_deal.txt.import.tab"));
    }


    private void split(File input) throws Exception {
        File folder = new File(PATH, "import_" + input.getName());
        folder.mkdirs();
        FileUtils.cleanDirectory(folder);
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
                fos = new FileOutputStream(new File(folder, fileNum + ".import.tab"));
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
        File input = new File(PATH, "address_unique.txt.not-matched.not-matched");
        List<String> lines = FileUtils.readLines(input, "gbk");


        Collections.sort(lines, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    return StringUtils.split(o1, "\t")[1].compareTo(StringUtils.split(o2, "\t")[1]);
                } catch (Exception e) {
                    throw e;
                }
            }
        });

        FileOutputStream fos = new FileOutputStream(new File(input.getAbsolutePath() + ".sort"));
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
        convertEncode(new File("D:\\房屋地址数据\\address_unique_deal.txt"), "utf-8", "gbk");
    }
}
