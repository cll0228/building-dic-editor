package com.lezhi.app.webapp.controller;

import com.lezhi.app.enums.RoomDicStatus;
import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.ResidenceMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.model.map.MapBuilder;
import com.lezhi.app.model.map.Residence;
import com.lezhi.app.model.map.StdAddr;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Colin Yan on 2016/7/15.
 */
@Controller
@RequestMapping("/")
public class RoomMapController {

    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;
    @Autowired
    private ResidenceMapper residenceMapper;

    @RequestMapping(value = "roomMap", method = RequestMethod.GET)
    public String roomMapPage(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "keyword", required = false) String keyword) {
        return "roomMap";
    }

    @ResponseBody
    @RequestMapping(value = "approve", method = RequestMethod.GET)
    public Map<String, String> approve(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "id", required = true) int id) {

        Map<String, String> result = new HashMap<>();
        boolean success = 1 == roomDicMapper.updateStatus(id, RoomDicStatus.CONFIRMED);
        result.put("status", success ? "success" : "failed");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "addBuilding")
    public Map<String, String> addBuilding(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "rid", required = true) int residenceId,
                                       @RequestParam(value = "no", required = true) String buildingName
                                           ) {

        Map<String, String> result = new HashMap<>();

        BuildingDic dic = new BuildingDic();
        dic.setName(buildingName);
        dic.setResidenceId(residenceId);
        boolean success = 1 == buildingDicMapper.insert(dic);
        result.put("status", success ? "success" : "failed");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "addRoom")
    public Map<String, String> addRoom(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "bid", required = true) int buildingId,
                                       @RequestParam(value = "rname", required = true) String roomName,
                                       @RequestParam(value = "rarea", required = true) BigDecimal area
    ) {

        Map<String, String> result = new HashMap<>();

        RoomDic ric = new RoomDic();
        ric.setName(roomName);
        ric.setBuildingId(buildingId);
        ric.setArea(area);
        ric.setStatus(RoomDicStatus.MANUAL_CREATE);
        boolean success = 1 == roomDicMapper.insertNewRoom(ric);
        int rid = roomDicMapper.getNewRoomId(ric);
        result.put("rid",String.valueOf(rid));
        result.put("status", success ? "success" : "failed");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "deleteRoom")
    public Map<String, String> deleteRoom(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam(value = "rid", required = true) int id
    ) {

        Map<String, String> result = new HashMap<>();

        RoomDic ric = new RoomDic();
        ric.setId(id);
        boolean success = 1 == roomDicMapper.deleteRoom(ric);
        result.put("status", success ? "success" : "failed");
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = "newArea")
    public Map<String, String> newArea(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "id", required = true) int id,
                                       @RequestParam(value = "newArea", required = true) BigDecimal newArea
                                           ) {

        Map<String, String> result = new HashMap<>();

        RoomDic dic = new RoomDic();
        dic.setId(id);
        dic.setArea(newArea);
        boolean success = 1 == roomDicMapper.update(dic);
        result.put("status", success ? "success" : "failed");
        return result;
    }

    @RequestMapping(value = "roomMap", method = RequestMethod.POST)
    public String roomMap(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "keywords", required = false) String __keywords) {
        request.setAttribute("keywords", __keywords);
        if (StringUtils.isNotBlank(__keywords)) {
            String keywords[] = __keywords.split(",");
            List<com.lezhi.app.model.Residence> residences = residenceMapper.find(keywords);
            if (residences != null && !residences.isEmpty()) {
                request.setAttribute("residences", residences);
                int rids[] = new int[residences.size()];
                for (int i = 0; i < residences.size(); i++) {
                    rids[i] = residences.get(i).getId();
                }

                MapBuilder builder = new MapBuilder();
                List<StdAddr> stdAddrs = roomDicMapper.findRoomExists(rids);
                for (StdAddr stdAddr : stdAddrs) {
                    builder.add(stdAddr);
                }

                builder.sortAll();
                List<Residence> residenceModels = builder.getResidenceList();
                request.setAttribute("residenceModels", residenceModels);

                for (com.lezhi.app.model.Residence r : residences) {
                    boolean exists = false;
                    for (Residence rm : residenceModels) {
                        if (r.getId().intValue() == rm.getResidenceId()) {
                            exists = true;
                            break;
                        }
                    }
                    r.setExists(exists);
                }
            }
        }

        return "roomMap";
    }
}
