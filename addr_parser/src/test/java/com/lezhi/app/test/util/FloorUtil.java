package com.lezhi.app.test.util;

/**
 * Created by Colin Yan on 2016/9/14.
 */
public class FloorUtil {

    public static Integer parseFloor(String roomNo) {
        return parse(roomNo, true);
    }

    private static Integer parse(String roomNo, boolean getFloor) {
        int roomInt;

        try {
            roomInt = Integer.parseInt(roomNo);
        } catch (Exception e) {
            return null;
        }

        int floor = roomInt / 100;

        if (floor >= 1 && floor < 50) {

            int r = roomInt % 100;
            if (r > 0 && r < 20) {
                return getFloor ? floor : r;
            }
        }
        return null;
    }

    public static Integer parseRoomIndex(String roomNo) {
        return parse(roomNo, false);
    }
}
