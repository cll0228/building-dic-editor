package com.lezhi.app.test.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Colin Yan on 2016/9/1.
 */
public class TimeUtil {

    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String now() {
        return df.format(new Date());
    }
}
