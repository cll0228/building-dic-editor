package com.lezhi.app.test.util;

/**
 * Created by Colin Yan on 2016/8/31.
 */
public class PropertyTypeUtil {

    public static Integer parse(String ss) {
        if (ss == null)
            return null;
        switch (ss) {
            case "里弄房":
                return 1;
            case "新工房":
                return 2;
            case "公寓":
                return 3;
            case "非叠加别墅":
                return 4;
            case "独栋别墅":
                return 5;
            case "双拼别墅":
                return 6;
            case "联排别墅":
            case "别墅":
                return 7;
            case "叠加别墅":
                return 8;
            case "商铺":
                return 9;
            case "办公楼":
                return 10;
            case "工厂":
                return 11;
            case "新里洋房":
                return 12;
        }
        return null;
    }
}
