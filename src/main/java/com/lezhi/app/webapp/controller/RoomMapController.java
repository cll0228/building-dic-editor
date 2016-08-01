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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
    	Integer userId = Integer.parseInt(request.getSession(true).getAttribute("userId").toString());
        Map<String, String> result = new HashMap<>();
        Date date = new Date();       
        Timestamp modifyTime = new Timestamp(date.getTime());
        boolean success = 1 == roomDicMapper.updateStatus(id, userId, modifyTime, RoomDicStatus.CONFIRMED);
        result.put("status", success ? "success" : "failed");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "addBuilding")
    public Map<String, String> addBuilding(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "rid", required = true) int residenceId,
                                       @RequestParam(value = "no", required = true) String buildingName
                                           ) {
    	//TODO
    	Integer userId = Integer.parseInt(request.getSession(true).getAttribute("userId").toString());
        Map<String, String> result = new HashMap<>();
        Date date = new Date();       
        Timestamp modifyTime = new Timestamp(date.getTime());
        BuildingDic dic = new BuildingDic();
        dic.setName(buildingName);
        dic.setResidenceId(residenceId);
//        dic.setOperatorId(userId);
//        dic.setModifyTime(modifyTime);
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
    	Integer userId = Integer.parseInt(request.getSession(true).getAttribute("userId").toString());
        Map<String, String> result = new HashMap<>();
        Date date = new Date();       
        Timestamp modifyTime = new Timestamp(date.getTime());

        RoomDic ric = new RoomDic();
        ric.setName(roomName);
        ric.setBuildingId(buildingId);
        ric.setArea(area);
        ric.setOperatorId(userId);
        ric.setModifyTime(modifyTime);
        int countRoom = roomDicMapper.countOldRoom(ric);
        boolean success = false;
        if(countRoom>0){
            ric.setDelStatus(0);
            success = 1 == roomDicMapper.updateNewRoomStatus(ric);
        } else {
            ric.setStatus(RoomDicStatus.MANUAL_CREATE);
            ric.setDelStatus(0);
            success = 1 == roomDicMapper.insertNewRoom(ric);
        }
        int rid = roomDicMapper.getNewRoomId(ric);
        result.put("rid",String.valueOf(rid));
        result.put("status", success ? "success" : "failed");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "addNewRoom")
    public Map<String, String> addNewRoom(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "bid", required = true) int buildingId,
                                       @RequestParam(value = "rname", required = true) String roomName,
                                       @RequestParam(value = "rarea", required = true) BigDecimal area
    ) {

    	Integer userId = Integer.parseInt(request.getSession(true).getAttribute("userId").toString());
        Map<String, String> result = new HashMap<>();
        Date date = new Date();       
        Timestamp modifyTime = new Timestamp(date.getTime());

        RoomDic ric = new RoomDic();
        ric.setName(roomName);
        ric.setBuildingId(buildingId);
        ric.setArea(area);
        ric.setOperatorId(userId);
        ric.setModifyTime(modifyTime);
        int countRoom = roomDicMapper.countOldRoom(ric);
        boolean success = false;
        if(countRoom>0){
            ric.setDelStatus(0);
            success = 1 == roomDicMapper.updateNewRoomStatus(ric);
        } else {
            ric.setStatus(RoomDicStatus.MANUAL_CREATE);
            ric.setDelStatus(0);
            success = 1 == roomDicMapper.insertNewRoom(ric);
            result.put("status", success ? "success" : "failed");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "deleteRoom")
    public Map<String, String> deleteRoom(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam(value = "rid", required = true) int id
    ) {
    	Integer userId = Integer.parseInt(request.getSession(true).getAttribute("userId").toString());
        Map<String, String> result = new HashMap<>();
        Date date = new Date();       
        Timestamp modifyTime = new Timestamp(date.getTime());
        
        RoomDic ric = new RoomDic();
        ric.setId(id);
        ric.setDelStatus(1);
        ric.setOperatorId(userId);
        ric.setModifyTime(modifyTime);
        boolean success = 1 == roomDicMapper.deleteRoom(ric);
        result.put("status", success ? "success" : "failed");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "editTotalFloor")
    public Map<String, String> editTotalFloor(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam(value = "bid", required = true) int id,
                                          @RequestParam(value = "topfloor", required = true) int topfloor
    ) {

        Map<String, String> result = new HashMap<>();

        BuildingDic bic = new BuildingDic();
        bic.setId(id);
        bic.setTopFloor(topfloor);
        boolean success = 1 == buildingDicMapper.updateTopFloor(bic);
        result.put("status", success ? "success" : "failed");
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = "deleteBuilding")
    public Map<String, String> deleteBuilding(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam(value = "buildingId", required = true) int buildingId
    ) {
    	Integer userId = Integer.parseInt(request.getSession(true).getAttribute("userId").toString());
        Map<String, String> result = new HashMap<>();
        Date date = new Date();       
        Timestamp modifyTime = new Timestamp(date.getTime());
        //取得相对应的roomId
        List<RoomDic> ricList = new ArrayList<RoomDic>();
        ricList = roomDicMapper.queryRoomId(buildingId);
        String id = "";
        for (RoomDic roomDic : ricList) {
        	if(id=="") {
        		id = roomDic.getId().toString();
        	} else {
        		id += "," + roomDic.getId().toString();
        	}
		}
        roomDicMapper.updateRoomStatus(id,userId, modifyTime);
        //更新楼栋状态为已删除
        boolean success = 1 == buildingDicMapper.updateBuildingStatus(buildingId,userId,modifyTime);
        result.put("status", success ? "success" : "failed");
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = "newArea")
    public Map<String, String> newArea(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "id", required = true) int id,
                                       @RequestParam(value = "newArea", required = true) BigDecimal newArea
                                           ) {
    	Integer userId = Integer.parseInt(request.getSession(true).getAttribute("userId").toString());
        Map<String, String> result = new HashMap<>();
        Date date = new Date();       
        Timestamp modifyTime = new Timestamp(date.getTime());
        RoomDic dic = new RoomDic();
        dic.setId(id);
        dic.setArea(newArea);
        dic.setOperatorId(userId);
        dic.setModifyTime(modifyTime);
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
