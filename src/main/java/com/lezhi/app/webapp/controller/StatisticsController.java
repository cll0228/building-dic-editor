package com.lezhi.app.webapp.controller;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Colin Yan on 2016/7/21.
 */
@Controller
@RequestMapping("/")
public class StatisticsController {

    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;

    @RequestMapping(value = "statistics", method = RequestMethod.GET)
    public String roomMapPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("buildingCount", buildingDicMapper.count());
        request.setAttribute("roomCount", roomDicMapper.count());
        request.setAttribute("lastUpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return "statistics";
    }

}
