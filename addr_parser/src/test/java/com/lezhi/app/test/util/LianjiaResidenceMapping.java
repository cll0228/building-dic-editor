package com.lezhi.app.test.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Colin Yan on 2016/8/31.
 */
public class LianjiaResidenceMapping {

    private static Map<Integer, Long> our_lianjia = new HashMap<>();

    private static Map<Long, Integer> lianjia_our = new HashMap<>();

    private static Map<Integer, String> outTypes = new HashMap<>();

    static {
        try {
            List<String> list =IOUtils.readLines(LianjiaResidenceMapping.class.getResourceAsStream("/residence_lianjia.csv"), "utf-8");

            for (String s : list) {
                if (StringUtils.isBlank(s)) {
                    continue;
                }
                String arr[] = s.split(",");
                our_lianjia.put(Integer.valueOf(arr[0]), Long.valueOf(arr[1]));
                lianjia_our.put(Long.valueOf(arr[1]), Integer.valueOf(arr[0]));
                if (arr.length >= 3) {
                    String type = arr[2];
                    if (type != null && type.contains("\"")) {
                        outTypes.put(Integer.valueOf(arr[0]), StringUtils.substringBetween(type, "\"", "\""));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }
    public static Integer getResidenceIdByLianjiaId(Long id) {
        if (id == null)
            return null;

        return lianjia_our.get(id);
    }

    public static Integer getPropertyType(Integer rid) {
        if (rid == null)
            return null;

        String typeName = outTypes.get(rid);
        if ("一般住宅".equals(typeName)) {
            return 3;
        } else if ("商住混合".equals(typeName)) {
            return 3;
        } else if ("纯别墅".equals(typeName)) {
            return 7;
        }
        return null;
    }
}
