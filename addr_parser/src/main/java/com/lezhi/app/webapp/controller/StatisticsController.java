package com.lezhi.app.webapp.controller;

import com.lezhi.app.mapper.StatisticsMapper;
import com.lezhi.app.webapp.WebAppInitializeBean;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Colin Yan on 2016/7/21.
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String statistics(HttpServletRequest request, HttpServletResponse response) {
        return "statistics";
    }

    @Autowired
    private WebAppInitializeBean webAppInitializeBean;

    private Map<String, Object> parseCache() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Map<String, Object> map = null;
        try {
            fis = new FileInputStream(webAppInitializeBean.getCacheFile());
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                map = (Map<String, Object>) obj;
            }
        } catch (Exception ignore) {
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(ois);
        }

        if (map == null) {
            return refresh();
        }
        return map;
    }

    private Map<String, Object> refresh() {
        Map<String, Object> result = new HashMap<>();

        int residenceCount = statisticsMapper.residenceCount();
        result.put("residenceCount", residenceCount);
        int residenceCoverdCount = statisticsMapper.residenceCoverdCount();
        result.put("residenceCoverdCount", residenceCoverdCount);

        result.put("residenceCoverdRate", String.format("%.2f", residenceCoverdCount / 1.0 / residenceCount));

        result.put("buildingCount", statisticsMapper.countBuilding());
        result.put("buildingWithCoordinate", statisticsMapper.buildingWithCoordinate());

        final int countRoom = statisticsMapper.countRoom();
        /*int countRoomDeal = statisticsMapper.countRoomDeal();
        result.put("countRoomDeal", countRoomDeal);

        int countRoomOthers = countRoom - countRoomDeal;
        result.put("countRoomOthers", countRoomOthers);*/

        result.put("countRoom", countRoom);

        result.put("countRoomLocked", statisticsMapper.countRoomLocked());

        int houseWithArea = statisticsMapper.houseWithArea();
        result.put("houseWithArea", houseWithArea);
        result.put("houseWithoutArea", countRoom - houseWithArea);
        result.put("houseWithAreaRate", String.format("%.2f", houseWithArea / 1.0 / countRoom));

        result.put("houseWithTowards", statisticsMapper.houseWithTowards());
//        result.put("houseWithHuxing", statisticsMapper.houseWithHuxing());

//        result.put("parseConfirmedHouseCount", statisticsMapper.parseConfirmedHouseCount());
//        result.put("parseUnconfirmedHouseCount", statisticsMapper.parseUnconfirmedHouseCount());

        result.put("lastUpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        store(result);
        return result;
    }

    private void store(Map<String, Object> map) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(webAppInitializeBean.getCacheFile());
            oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
        } catch (Exception ignore) {
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(oos);
        }
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String statisticsData(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(required = false, value = "refresh") Boolean refresh) {

        Map<String, Object> map;
        if (refresh == null || !refresh) {
            map = parseCache();
        } else {
            map = refresh();
        }
        if (map != null) {
            for (Map.Entry<String, Object> e : map.entrySet()) {
                request.setAttribute(e.getKey(), e.getValue());
            }
        }

        return "statistics-data";
    }

}
