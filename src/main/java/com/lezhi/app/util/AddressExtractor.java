package com.lezhi.app.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddressExtractor {

    private static final Pattern __regex = Pattern.compile(
            "(?:(?<rn3>管弄新村)(?<rn0>.+弄)|(?<rn1>.+?(?:号|村|坊|道|苑|园|城|庭|大厦|湾|公寓|名邸|墅|小区|小区）|东门|西门|南门|北门|东区|西区|南区|北区))|(?<rn2>自定义的小区名称))" +
                    ".*?" +
                    "(?<b>[\\d0-9a-zA-Z]*?[东西南北上中下甲乙丙丁戊己庚辛壬癸一二三四五六七八九十]*?)(?:号楼?|单元|幢|楼|座)(?<be>[东西南北上中下甲乙丙丁戊己庚辛壬癸一二三四五六七八九十]?)" +
                    ".*?" +
                    "(?:\\d+层(?<r0>\\d+)室|(?<r1>[\\d\\-_]+)层?室?|(?<r2>[\\da-zA-Z甲乙丙丁戊己庚辛壬癸一二三四五六七八九十]+)层?室?|(?<r3>全幢)室?)(?<r4>[a-zA-Z甲乙丙丁戊己庚辛壬癸]?)");

    private static final Pattern regex;
    private static final Pattern regex2;

    private static final String RESIDENCE_DIC = "/SH_Residence2016-07-14 16-17-21.csv";

    private static final String residenceNames[];

    private static final String allResidence;

    static {
        Set<String> residenceNameList = new HashSet<>();
        InputStream is = null;
        try {
            is = AddressExtractor.class.getResourceAsStream(RESIDENCE_DIC);
            List<String> lines = IOUtils.readLines(is, "utf-8");
            for (String s : lines) {
                s = StringUtils.substringBetween(s, "\"", "\"");
                if (!s.matches(".+(?:弄|号|村|坊|道|苑|园|城|庭|大厦|湾|公寓|名邸|墅|小区|小区）|东门|西门|南门|北门|东区|西区|南区|北区)$")) {
                    residenceNameList.add(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        residenceNameList.add("南林家港");
        residenceNameList.add("镇坪路赵家宅");
        residenceNameList.add("光复西路西合德里");
        residenceNameList.add("城隍庙");
        residenceNames = residenceNameList.toArray(new String[residenceNameList.size()]);

        StringBuilder sb = new StringBuilder();
        for (String residenceName : residenceNames) {
            sb.append("|");
            sb.append(residenceName);
        }
        allResidence = sb.toString().substring(1);
        regex = Pattern.compile(__regex.pattern().replace("自定义的小区名称", allResidence));
        String rnPart = StringUtils.substringBefore(regex.pattern(), ".*");
        regex2 = Pattern.compile(rnPart + "[\\-/\\\\|](?<b>\\d+)号?[\\-/\\\\|](?<r>\\d+)室?");
    }


    private static boolean isMatch(String str, Pattern p) {
        Matcher m = p.matcher(str);
        return m.find();
    }

    private static String[] regexGroup(String str, Pattern p) {
        try {
            Matcher m = p.matcher(str);
            if (m.find()) {
                List<String> result = new ArrayList<>();
                int gn = m.groupCount();
                for (int i = 1; i <= gn; i++) {
                    String value = m.group(i);
                    result.add(value);
                }
                return result.toArray(new String[result.size()]);
            }
            return null;
        } catch (Throwable t) {
            System.err.println("str:" + str + ",p:" + p);
            t.printStackTrace();
            throw t;
        }
    }

    private static Map<String, String> regexGroup(String str, Pattern p, String... groupNames) {
        try {
            Matcher m = p.matcher(str);
            if (m.find()) {
                Map<String, String> result = new HashMap<>();
                for (String n : groupNames) {
                    result.put(n, m.group(n));
                }
                return result;
            }
            return null;
        } catch (Throwable t) {
            System.err.println("str:" + str + ",p:" + p);
            t.printStackTrace();
            throw t;
        }
    }

    private static String regexGroup(String str, Pattern p, int group) {
        try {
            Matcher m = p.matcher(str);
            String value = null;

            if (m.find()) {
                if (group > m.groupCount())
                    return null;
                value = m.group(group);
            }
            return value;
        } catch (Throwable t) {
            System.err.println("str:" + str + ",p:" + p + ",group:" + group);
            t.printStackTrace();
            throw t;
        }
    }
    public static AddressModel parseAll(String line) {
        return filterResult(parseAll__(line));
    }

    public static AddressModel parseAll__(String line) {

        String[] arr = regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+路)(\\d+)号(\\d+)室?$"));
        if (arr != null) {
            String room = PreHandle.filterReduplicate2_3(arr[2], 4);
            Address2 address = new Address2(arr[0] + arr[1] + "号", arr[1], room);
            address.setScore(60);
            return filterResult(address);
        }
        arr = regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+)路(\\d+)弄(\\d+)号(\\d+)室?$"));
        if (arr != null) {
            String room = PreHandle.filterReduplicate2_3(arr[3], 4);
            Address1 address1 = new Address1(arr[0], arr[1], arr[2], room);
            address1.setScore(99);
            return filterResult(address1);
        }
        arr = regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+)路(\\d+)弄[\\-/]?(\\d+)号?[\\-/](\\d+)$"));
        if (arr != null) {
            String room = PreHandle.filterReduplicate2_3(arr[3], 4);
            Address1 address1 = new Address1(arr[0], arr[1], arr[2], room);
            address1.setScore(90);
            return filterResult(address1);
        }
        arr = regexGroup(line, Pattern.compile("^(.+)村([\\d一二三四五六七八九十]+)[组队](\\d+)号$"));
        if (arr != null) {
            Address3 address3 = new Address3(arr[0], arr[1], arr[2]);
            address3.setScore(99);
            return filterResult(address3);
        }
        Map<String, String> map = regexGroup(line, regex, "rn0", "rn1", "rn2", "b", "be", "r0", "r1", "r2", "r3", "r4");
        if (map != null && !map.isEmpty()) {
            String rn0 = map.get("rn0");
            String rn1 = map.get("rn1");
            String rn2 = map.get("rn2");
            String rn3 = map.get("rn3");
            String residenceName = (String) firstNotNull(rn3, rn0, rn1, rn2);
            Assert.notNull(residenceName);
            String building = map.get("b");
            Assert.notNull(building);
            String buildingExt = map.get("be");
            if (StringUtils.isNotBlank(buildingExt)) {
                building += buildingExt;
            }
            String r0 = map.get("r0");
            String r1 = map.get("r1");
            String r2 = map.get("r2");
            String r3 = map.get("r3");
            String r4 = map.get("r4");
            String room = (String) firstNotNull(r0, r1, r2, r3);
            Assert.notNull(room);
            if (StringUtils.isNotBlank(r4)) {
                room += r4;
            }
            room = PreHandle.filterReduplicate2_3(room, 4);
            if (StringUtils.isNotBlank(residenceName) && StringUtils.isNotBlank(building) && StringUtils.isNotBlank(room))
                return filterResult(new Address2(residenceName, building, room));
        }
        arr = regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+)([\\d一二三四五六七八九十]+)[组队](\\d+)号$"));
        if (arr != null) {
            Address3 address3 = new Address3(arr[0], arr[1], arr[2]);
            address3.setScore(59);
            return filterResult(address3);
        }
        map = regexGroup(line, regex2, "rn0", "rn1", "rn2", "b", "r");
        if (map != null && !map.isEmpty()) {
            String rn0 = map.get("rn0");
            String rn1 = map.get("rn1");
            String rn2 = map.get("rn2");
            String residenceName = (String) firstNotNull(rn1, rn2, rn0);
            Assert.notNull(residenceName);
            String building = map.get("b");
            Assert.notNull(building);
            String room = map.get("r");
            Assert.notNull(room);
            room = PreHandle.filterReduplicate2_3(room, 4);
            if (StringUtils.isNotBlank(residenceName) && StringUtils.isNotBlank(building) && StringUtils.isNotBlank(room))
                return filterResult(new Address2(residenceName, building, room));
        }

        /*
        arr = regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+?)(\\d+)号(\\d+)室$"));
        if (arr != null)
            return new Address2(arr[0], arr[1], arr[2]);
        */
        return null;
    }

    public static void main(String[] args) {
        // 金光村5组2094号
        //    System.out.println(filterReduplicative("金光村5组2094号金光村5组2094号金光村5组2094号"));
        //residenceNameList.add("水仙家");
        //   String line = "奉贤肖塘路146弄60单元602室";
        //  System.out.println(parseAll(line));
        //

        // String[] list = regexGroup("金光村5组2094号", Pattern.compile(".+?(\\d+).+?(\\d+)号"));
        // System.out.println(Arrays.toString(list));

        //    System.out.println(parseAll("奉贤肖塘路146弄/12号/434"));
        //   System.out.println(parseAll("城隍庙甲21号全幢室"));

        //      Map map  = regexGroup("迎春路1355弄21号全幢", Pattern.compile("(?:(?<r1>[\\d\\-_]+)室|(?<r2>[\\da-zA-Z甲乙丙丁戊己庚辛壬癸一二三四五六七八九十]+)室|(?<r3>全幢)室?)"),
        //           "r1", "r2", "r3");
        //   System.out.println(map);

        //System.out.println(__regex.pattern());

        System.out.println(parseAll("三林路1300弄6号23层1102室"));
    }

    private static AddressModel filterResult(AddressModel input) {
        if (input == null || input.isFiltered())
            return input;

        if (input.getBuilding() == null || input.getBuilding().length() > 10)
            return null;

        if (input.getRoom() == null || input.getRoom().length() > 10)
            return null;

        return input;
    }

    private static Object firstNotNull(Object... params) {
        if (params == null)
            return null;

        for (Object o : params) {
            if (o != null)
                return o;
        }
        return null;
    }
}
