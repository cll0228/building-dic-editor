package com.lezhi.app.test.util;

/**
 * Created by Colin Yan on 2016/8/31.
 */
public class FloorStructureUtil {
    /*
    1	低层	1~3
2	多层	4~7
3	中高层	8~9
4	高层	10~29
5	超高层	30以上
6	未知
     */


    public static Integer parse(String ss) {
        if (ss == null)
            return null;
        if (ss.equals("低层"))
            return 1;
        if (ss.equals("多层") || ss.equals("中层"))
            return 2;
        if (ss.equals("中高层"))
            return 3;
        if (ss.equals("高层"))
            return 4;
        if (ss.equals("超高层"))
            return 5;

        return null;
    }

    public static Integer calc(Short totalFloor, Integer floorStructureId) {
        if (totalFloor <= 1)
            return 1;

        switch (floorStructureId) {
            case 1:
                return 1;
            case 2:
                return totalFloor / 2;
            case 3:
                return totalFloor * 2 /3;
            case 4:
                return totalFloor.intValue();
        }
        if (floorStructureId.equals(1)) {
            return 1;
        }
        return null;
    }
}
