package com.lezhi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Colin Yan on 2016/7/14.
 */
public class Test4 {

    public static void main(String[] args) {

        Pattern bp12 = Pattern.compile("[abc|ef]");

        Matcher m = bp12.matcher("111ef44");

        if (m.find()) {
            System.out.println(m.group(1));
        }

    }
}
