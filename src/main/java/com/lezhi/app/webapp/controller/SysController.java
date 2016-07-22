package com.lezhi.app.webapp.controller;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.ResidenceMapper;
import com.lezhi.app.mapper.ResolvedAddrMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import com.lezhi.app.model.Residence;
import com.lezhi.app.model.ResolvedAddress;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Colin Yan on 2016/7/15.
 */
@Controller
@RequestMapping("/")
public class SysController {

    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;

    @Deprecated
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public String resultPage(HttpServletRequest request, HttpServletResponse response) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        servletContext.setAttribute("buildingCount", buildingDicMapper.count());
        servletContext.setAttribute("roomCount", roomDicMapper.count());
        servletContext.setAttribute("lastUpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return "success";
    }

}
