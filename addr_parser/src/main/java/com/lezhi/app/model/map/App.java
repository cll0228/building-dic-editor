package com.lezhi.app.model.map;

import java.util.List;

/**
 * Created by Colin Yan on 2016/7/19.
 */
public class App {


    public static void main(String[] args) {
        MapBuilder builder = new MapBuilder();

        builder.add(new StdAddr("三门路358弄", "9", "604"));
        builder.add(new StdAddr("三门路358弄", "9", "304"));
        builder.add(new StdAddr("三门路358弄", "10", "203"));
        builder.add(new StdAddr("三门路358弄", "8", "302"));
        builder.add(new StdAddr("三门路358弄", "8", "301"));
        builder.sortAll();
        List<Residence> residences = builder.getResidenceList();
        System.out.println(residences);
    }



}
