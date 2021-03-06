package com.lezhi.app.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddressExtractor {

    private static final String allResidence;

    static {
        String RESIDENCE_DIC = "/SH_Residence2016-07-14 16-17-21.csv";

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
        residenceNameList.add("管弄新村");
        residenceNameList.add("维园道");
        residenceNameList.add("仓城七村");
        residenceNameList.add("公园新村");
        residenceNameList.add("凉城二村");
        residenceNameList.add("东新路《新湖明珠城一期》");
        residenceNameList.add("《春申景城(一期)》");
        residenceNameList.add("《金沙雅苑-舒诗康庭》");
        residenceNameList.add("凤城五村");
        final String residenceNames[] = residenceNameList.toArray(new String[residenceNameList.size()]);

        StringBuilder sb = new StringBuilder();
        for (String residenceName : residenceNames) {
            sb.append("|");
            sb.append(residenceName);
        }
        allResidence = sb.toString().substring(1);
    }

    // partial
    private static final Pattern __buildingRegex = Pattern.compile(
            "(?<b>[\\d0-9a-zA-Z\\-_]*?[东西南北上中下甲乙丙丁戊己庚辛壬癸一二三四五六七八九十]*?)" +
                    "(?:号楼?|单元|幢|楼|座)" +
                    "(?<be>[东西南北上中下甲乙丙丁戊己庚辛壬癸]?)");

    private static final Pattern residenceBuilding_regex = Pattern.compile(
            "(?:(?<rn2>" + allResidence + ")|(?<rn0>.+弄))" +
                    ".*?" +
                    __buildingRegex.pattern());

    private static final Pattern residenceBuilding_lowThanRoad_regex = Pattern.compile(
            "(?<rn1>.+?(?:号|村|坊|道|苑|园|城|庭|大厦|湾|公寓|名邸|墅|小区|小区）|东门|西门|南门|北门|东区|西区|南区|北区))" +
                    ".*?" +
                    __buildingRegex.pattern());

    // 直接解析出室号
    private static final Pattern residenceBuildingRoom_regex = Pattern.compile(
            "(?:(?<rn2>" + allResidence + ")|(?<rn0>.+弄)|(?<rn1>.+?(?:号|村|坊|道|苑|园|城|庭|大厦|湾|公寓|名邸|墅|小区|小区）|东门|西门|南门|北门|东区|西区|南区|北区)))" +
                    ".*?" +
                    "[\\-/\\\\|](?<b>\\d+)号?[\\-/\\\\|](?<r>\\d+)室?");

    private static final Pattern roadBuilding_regex = Pattern.compile(
            "(?:(?<rn0>.+[路街]))" +
                    ".*?" +
                    __buildingRegex.pattern());

    private static final Pattern roadBuildingRoom_regex = Pattern.compile(
            "(?:(?<rn0>.+[路街]))" +
                    ".*?" +
                    "[\\-/\\\\|](?<b>\\d+)号?[\\-/\\\\|](?<r>\\d+)室?");

    // private static final Pattern
    private static String extractRoomNo(String input) {

        Pattern p = Pattern.compile("[^\\d](?<r0>\\d{1,2})[楼层]+(?<r1>\\d{2})室?");
        Matcher m = p.matcher(input);
        String n = null;

        if (m.find()) {
            n = m.group("r0") + m.group("r1") ;
        }

        if (n == null) {
            //静安延平路区123弄5号3层I室
            p = Pattern.compile("(?<r0>\\d+[楼层]+[a-zA-Z])室?");
            m = p.matcher(input);

            if (m.find()) {
                n = m.group("r0");
            }
        }
        if (n == null) {
            //甲乙丙丁置前会与楼栋号的标记混淆
            p = Pattern.compile("(?<r0>[\\d\\-－_a-zA-Z一二三四五六七八九十/\\\\()]+[甲乙丙丁戊己庚辛壬癸]?)室(?<r4>[a-zA-Z甲乙丙丁戊己庚辛壬癸]?)");
            m = p.matcher(input);
            if (m.find()) {  //出现多次要第一个，如 ...503室3室
                n = m.group("r0");
                String r4 = m.group("r4");
                if (r4 != null) {
                    n += r4;
                }
            }
        }
        /*
        七莘路3885弄1-84号6-10幢全幢
三新北路1333弄17号1层全幢室

         */
        if (n == null) {
            p = Pattern.compile("(?<r10>(?:[\\d\\-/\\\\]+幢)?(?:[\\d\\-－/\\\\]+层)?全幢)室?");
            m = p.matcher(input);
            if (m.find()) {
                n = m.group("r10");//"全幢";
            }
        }

        if (n == null) {
            p = Pattern.compile("(?<r1>[\\d\\-_a-zA-Z一二三四五六七八九十]+[甲乙丙丁戊己庚辛壬癸]?层)室$");
            m = p.matcher(input);
            while (m.find()) {  //出现多次要最后一个
                n = m.group("r1");
            }
        }

        if (n == null) {
            p = Pattern.compile("(?<r1>(?<u>地下)?[\\d\\-_a-zA-Z一二三四五六七八九十]+[层楼])$");
            m = p.matcher(input);
            while (m.find()) {  //出现多次要最后一个
                n = m.group("r1");
            }
        }
        if (n == null) {
            p = Pattern.compile(".+层(?<room>[\\d\\-－_a-zA-Z]+)");
            m = p.matcher(input);
            if (m.find()) {
                n = m.group("room");
            }
        }
        if (n == null) {
            p = Pattern.compile(__buildingRegex.pattern() + "(?<room>[\\d\\-－_a-zA-Z]+)");
            m = p.matcher(input);
            if (m.find()) {
                n = m.group("room");
            }
        }
        if (n == null) {
            if (input.endsWith("全")) {
                n = "全";
            }
        }

        if (n != null && n.startsWith("-")) {
            return n.substring(1);
        }
        return n;
    }

    public static void main(String[] args) {
        //10层全幢室
        //    System.out.println(extractRoomNo("石门路39弄89号9层"));
    	//静安区万航渡路458弄6号 分不了
    	//康桥镇沪南路3468弄25幢65号6层602室 可以分
        System.out.println(parseAll("静安区万航渡路458弄6号"));
    }


    private static boolean isMatch(String str, Pattern p) {
        Matcher m = p.matcher(str);
        return m.find();
    }

    private static String preProcess(String line) {

        if (line.contains("车库")) {
            return null;
        }

        line = StringUtils.removeStart(line, "：");
        line = StringUtils.removeStart(line, ".");

        // 预处理
        line = line.replaceAll("区\\(县\\)", "区");

        Pattern pattern = Pattern.compile("^(-)[\\u4E00-\\u9FA5]"); //TODO
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            line = line.replaceFirst(matcher.group(1), "");
        }

        line = PreHandle.handle(line);

        pattern = Pattern.compile(".+?([\\d]{1,2}层[\\d]{1,2}室)");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            String re = matcher.group(0);
            line = line.replaceFirst(matcher.group(0), re.replace("层", ""));
        }


        line = line.replace("号幢", "号");

        pattern = Pattern.compile(".+?(\\d+层)([a-zA-Z\\d_一二三四五六七八九十]+.*层?)室");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            if (matcher.group(2).length() > 2) {
                line = line.replaceFirst(matcher.group(1), "");
            }
        }

        //康桥镇沪南路3468弄25幢65号6层602室
        pattern = Pattern.compile(".+?([a-zA-Z\\d_]+幢)[a-zA-Z\\d东西南北上中下甲乙丙丁戊己庚辛壬癸一二三四五六七八九十]+号");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            line = line.replaceFirst(matcher.group(1), "");
        }

        pattern = Pattern.compile(".+?[a-zA-Z\\d]+号([a-zA-Z\\d_]+幢)");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            line = line.replaceFirst(matcher.group(1), "");
        }

        pattern = Pattern.compile(".+?[a-zA-Z\\d]+号(.+单元)");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            line = line.replaceFirst(matcher.group(1), "");
        }

        pattern = Pattern.compile(".+?[a-zA-Z\\d]+号([a-zA-Z\\d_一二三四五六七八九十]+层)[\\d\\-/\\\\]{3,}室");
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            line = line.replaceFirst(matcher.group(1), "");
        }

        return line;
    }

    public static AddressModel parseAll(String line) {
        if (line != null)
            line = line.trim();
        else
            return null;

        line = preProcess(line);
        if (line == null)
            return null;

        return filterResult(parseAll__(line));
    }

    private static AddressModel parseAll__(String line) {

        String[] arr = RegexUtil.regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+[路街])(\\d+)号楼?(\\d+)室?$"));
        if (arr != null) {
            String room = PreHandle.filterReduplicate2_3(arr[2], 4);
            Address2 address = new Address2(arr[0] + arr[1] + "号", arr[1], room);
            address.setScore(80);
            return filterResult(address);
        }
        arr = RegexUtil.regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+)路(\\d+)弄(\\d+)号(\\d+)室?$"));
        if (arr != null) {
            String room = PreHandle.filterReduplicate2_3(arr[3], 4);
            Address1 address1 = new Address1(arr[0], arr[1], arr[2], room);
            address1.setScore(95);
            return filterResult(address1);
        }
        arr = RegexUtil.regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+)路(\\d+)弄[\\-/]?(\\d+)号?[\\-/](\\d+)$"));
        if (arr != null) {
            String room = PreHandle.filterReduplicate2_3(arr[3], 4);
            Address1 address1 = new Address1(arr[0], arr[1], arr[2], room);
            address1.setScore(90);
            return filterResult(address1);
        }
        arr = RegexUtil.regexGroup(line, Pattern.compile("^(?:五村村|(.+))村([\\d一二三四五六七八九十]+)[组队](\\d+)号([a-zA-Z]?).*$"));
        if (arr != null) {
            Address3 address3 = new Address3(arr[0], arr[1], arr[2]);
            address3.setScore(99);
            return filterResult(address3);
        }

        //七莘路3333号12区17号202
        arr = RegexUtil.regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+路\\d+号).*?(\\d+)号([\\-/\\d\\\\]+)室?$"));
        if (arr != null) {
            Address2 address3 = new Address2(arr[0], arr[1], arr[2]);
            address3.setScore(99);
            return filterResult(address3);
        }
        //
        arr = RegexUtil.regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+路\\d+号)(\\d+)号"));
        if (arr != null) {
            String room = extractRoomNo(line);

            Address2 address3 = new Address2(arr[0], arr[1], room);
            address3.setScore(99);
            return filterResult(address3);
        }

        //西闸路75号别墅23幢全幢室
        arr = RegexUtil.regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+路\\d+号).*?(\\d+)幢"));
        if (arr != null) {
            String room = extractRoomNo(line);

            Address2 address3 = new Address2(arr[0], arr[1], room);
            address3.setScore(90);
            return filterResult(address3);
        }

        Map<String, String> map = RegexUtil.regexGroup(line, residenceBuilding_regex, "rn0", "rn2", "b", "be");
        if (map != null && !map.isEmpty()) {
            String rn0 = map.get("rn0");
            String rn2 = map.get("rn2");

            String residenceName = (String) firstNotNull(rn0, rn2);
            Assert.notNull(residenceName);
            String building = map.get("b");
            Assert.notNull(building);
            String buildingExt = map.get("be");
            if (StringUtils.isNotBlank(buildingExt)) {
                building += buildingExt;
            }

            String room = extractRoomNo(line);
            if (room != null) {
                room = PreHandle.filterReduplicate2_3(room, 4);
                if (StringUtils.isNotBlank(residenceName) && StringUtils.isNotBlank(building) && StringUtils.isNotBlank(room))
                    return filterResult(new Address2(residenceName, building, room));
            }
        }
        arr = RegexUtil.regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+)([\\d一二三四五六七八九十]+)[组队](\\d+)号$"));
        if (arr != null) {
            Address3 address3 = new Address3(arr[0], arr[1], arr[2]);
            address3.setScore(59);
            return filterResult(address3);
        }
        map = RegexUtil.regexGroup(line, residenceBuildingRoom_regex, "rn0", "rn1", "rn2", "b", "r");
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

        map = RegexUtil.regexGroup(line, roadBuilding_regex, "rn0", "b", "be");
        if (map != null && !map.isEmpty()) {
            String residenceName = map.get("rn0");

            Assert.notNull(residenceName);
            String building = map.get("b");
            Assert.notNull(building);
            String buildingExt = map.get("be");
            if (StringUtils.isNotBlank(buildingExt)) {
                building += buildingExt;
            }

            String room = extractRoomNo(line);
            if (room != null) {

                room = PreHandle.filterReduplicate2_3(room, 4);
                if (StringUtils.isNotBlank(residenceName) && StringUtils.isNotBlank(building) && StringUtils.isNotBlank(room))
                    return filterResult(new Address2(residenceName + building + "号", building, room));
            }
        }

        map = RegexUtil.regexGroup(line, residenceBuilding_lowThanRoad_regex, "rn1", "b", "be");
        if (map != null && !map.isEmpty()) {
            String rn1 = map.get("rn1");
            String residenceName = (String) firstNotNull(rn1);
            Assert.notNull(residenceName);
            String building = map.get("b");
            Assert.notNull(building);
            String buildingExt = map.get("be");
            if (StringUtils.isNotBlank(buildingExt)) {
                building += buildingExt;
            }

            String room = extractRoomNo(line);
            if (room != null) {
                room = PreHandle.filterReduplicate2_3(room, 4);
                if (StringUtils.isNotBlank(residenceName) && StringUtils.isNotBlank(building) && StringUtils.isNotBlank(room))
                    return filterResult(new Address2(residenceName, building, room));
            }
        }
        /*
        arr = regexGroup(line, Pattern.compile("^([\\u4E00-\\u9FA5]+?)(\\d+)号(\\d+)室$"));
        if (arr != null)
            return new Address2(arr[0], arr[1], arr[2]);
        */
        return null;
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
