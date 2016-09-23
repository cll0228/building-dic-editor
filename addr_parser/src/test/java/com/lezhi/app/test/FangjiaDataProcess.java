package com.lezhi.app.test;

import com.lezhi.app.test.model.FangjiaBuildingInfo;
import com.lezhi.app.test.model.FangjiaResidenceInfo;
import com.lezhi.app.test.mapper.FangjiaHangMapper;
import com.lezhi.app.test.model.FangjiaRoomInfo;
import com.lezhi.app.test.model.ResidenceFjInfo;
import com.lezhi.app.test.util.AddressUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendl on 2016/9/6.
 */
@Component
public class FangjiaDataProcess {

    @Autowired
    private FangjiaHangMapper fangjiaHangMapper;

    // 根据房价网数据补全楼盘字典数据库中的数据（楼栋坐标/总楼层/楼栋总房间数）
    public void startBuilding() throws IOException {
        List<FangjiaResidenceInfo> fjResidenceNames = fangjiaHangMapper.getFjResidenceInfo();
        System.out.println(fjResidenceNames.size());
        for(int i=0;i<fjResidenceNames.size();i++) {
            FangjiaResidenceInfo fangjiaResidenceInfo = fjResidenceNames.get(i);
            System.out.println("房价网ID:"+fangjiaResidenceInfo.getResidenceId());
            String residenceName = fangjiaResidenceInfo.getResidenceName();
            List<FangjiaResidenceInfo> residenceInfos = fangjiaHangMapper.getResidenceByName(residenceName);
            if(residenceInfos.size() == 0 || residenceInfos == null){
                continue;
            }
            FangjiaResidenceInfo residenceInfo = new FangjiaResidenceInfo();
            if (residenceInfos.size() == 1){
                residenceInfo = residenceInfos.get(0);
            } else{
                for(FangjiaResidenceInfo fjr: residenceInfos) {
                    String[] addressArr = fjr.getAddress().split("、");
                    for(String add: addressArr) {
                        if(fangjiaResidenceInfo.getAddress().equals(add)){
                            residenceInfo = fjr;
                            continue;
                        }
                    }
                }
            }
            System.out.println("ID:"+residenceInfo.getResidenceId());
            Integer fjResidenceId = fangjiaResidenceInfo.getResidenceId();
            // 房价网楼栋信息
            List<FangjiaBuildingInfo> fangjiaBuildingInfoList = fangjiaHangMapper.getFjBuildingInfo(fjResidenceId);
            if(fangjiaBuildingInfoList.size() == 0) {
                continue;
            }
            // 楼盘字典漏洞信息
            List<FangjiaBuildingInfo> buildingInfoList = fangjiaHangMapper.getBuildingInfo(
                    residenceInfo.getResidenceId());
            if(buildingInfoList.size()==0){
                continue;
            }
            for(int j=0;j<buildingInfoList.size();j++){
                FangjiaBuildingInfo buildingInfo = buildingInfoList.get(j);
                for(FangjiaBuildingInfo fangjiaBuildingInfo: fangjiaBuildingInfoList){
                    if(fangjiaBuildingInfo.getBlockNumber().equals(buildingInfo.getBlockNumber())){
                        Integer totalFloor = 0;
                        if(buildingInfo.getFloorTotal() == null){
                            totalFloor = fangjiaBuildingInfo.getFloorTotal();
                        }
                        // 更新楼盘字典楼栋信息
                        fangjiaHangMapper.updateBuildingInfo(buildingInfo.getId(),totalFloor,
                                fangjiaBuildingInfo.getLon(),fangjiaBuildingInfo.getLat(),
                                fangjiaBuildingInfo.getRoomTotal());
                        // 更新成功
                        System.out.println("更新成功！");
                    }
                }
            }

        }
    }

    // 根据房价网数据补全楼盘字典数据库中的数据（小区楼栋数/房屋类型）
    public void startResidence() throws IOException {
        List<FangjiaResidenceInfo> fjResidenceNames = fangjiaHangMapper.getAllFjResidenceInfo(1);
        System.out.println(fjResidenceNames.size());
        for (int i = 0; i < fjResidenceNames.size(); i++) {
            FangjiaResidenceInfo fangjiaResidenceInfo = fjResidenceNames.get(i);
            System.out.println("房价网ID:" + fangjiaResidenceInfo.getResidenceId());
            String residenceName = fangjiaResidenceInfo.getResidenceName();
            List<FangjiaResidenceInfo> residenceInfos = fangjiaHangMapper.getResidenceByName(residenceName);
            if (residenceInfos.size() == 0 || residenceInfos == null) {
                continue;
            }
            FangjiaResidenceInfo residenceInfo = new FangjiaResidenceInfo();
            if (residenceInfos.size() == 1) {
                residenceInfo = residenceInfos.get(0);
                System.out.println("ID:"+residenceInfo.getResidenceId());
                // 更新楼盘字典小区楼栋数
                fangjiaHangMapper.updateResidenceBuildingNum(residenceInfo.getResidenceId(),
                        fangjiaResidenceInfo.getBuildingNum());
                System.out.println("更新成功！");
            } else {
                for (FangjiaResidenceInfo fjr : residenceInfos) {
                    String[] addressArr = fjr.getAddress().split("、");
                    for (String add : addressArr) {
                        if (fangjiaResidenceInfo.getAddress().equals(add)) {
                            residenceInfo = fjr;
                            // 更新楼盘字典小区楼栋数
                            System.out.println("ID:"+residenceInfo.getResidenceId());
                            fangjiaHangMapper.updateResidenceBuildingNum(residenceInfo.getResidenceId(),
                                    fangjiaResidenceInfo.getBuildingNum());
                            System.out.println("更新成功！");
                            continue;
                        }
                    }
                }
            }
        }
    }

    // 根据房价网数据补全楼盘字典数据库中的数据（房屋类型）
    public void startRoom() throws IOException {
        List<FangjiaResidenceInfo> fjResidenceNames = fangjiaHangMapper.getAllFjResidenceInfoForRoom();
        System.out.println(fjResidenceNames.size());
        int n=0;
        for (int i = 0; i < fjResidenceNames.size(); i++) {
            FangjiaResidenceInfo fangjiaResidenceInfo = fjResidenceNames.get(i);
            System.out.println("房价网ID:" + fangjiaResidenceInfo.getResidenceId());
            String residenceName = fangjiaResidenceInfo.getResidenceName();
            List<FangjiaResidenceInfo> residenceInfos = fangjiaHangMapper.getResidenceByName(residenceName);
            if (residenceInfos.size() == 0 || residenceInfos == null) {
                continue;
            }
            FangjiaResidenceInfo residenceInfo = new FangjiaResidenceInfo();
            if (residenceInfos.size() == 1) {
                residenceInfo = residenceInfos.get(0);
                // 更新楼盘字典小区楼栋数
                fangjiaHangMapper.updateResidenceBuildingNum(residenceInfo.getResidenceId(),
                        fangjiaResidenceInfo.getBuildingNum());
            } else {
                for (FangjiaResidenceInfo fjr : residenceInfos) {
                    String[] addressArr = fjr.getAddress().split("、");
                    for (String add : addressArr) {
                        if (fangjiaResidenceInfo.getAddress().equals(add)) {
                            residenceInfo = fjr;
                            continue;
                        }
                    }
                }
            }
            System.out.println("楼盘字典ID:"+residenceInfo.getResidenceId());
            // 房价网房屋信息
            List<FangjiaRoomInfo> fangjiaRoomInfoes = fangjiaHangMapper.getFjRoomInfo(
                    fangjiaResidenceInfo.getResidenceId());
            if(fangjiaRoomInfoes == null){
                continue;
            }
            // 楼盘字典房屋信息
            List<FangjiaRoomInfo> roomInfos = fangjiaHangMapper.getRoomInfo(residenceInfo.getResidenceId());
            if(roomInfos == null){
                continue;
            }
            for(int j=0;j<fangjiaRoomInfoes.size();j++){
                FangjiaRoomInfo fangjiaRoomInfo = fangjiaRoomInfoes.get(j);
                for(FangjiaRoomInfo roomInfo: roomInfos) {
                    if(fangjiaRoomInfo.getBlockNumber().equals(roomInfo.getBlockNumber()) &&
                            fangjiaRoomInfo.getRoomNumber().equals(roomInfo.getRoomNumber())){
                        // 楼栋，房间号一致，更新房间类型
                        fangjiaHangMapper.updateRommType(roomInfo.getId(),fangjiaRoomInfo.getRoomType());
                        System.out.println("更新成功！"+(n++));
                    }
                }
            }
        }
    }

    // 获取小区小区信息
    public void getResidenceRelation() throws Exception {
        // 获取房价点评网小区信息
        List<FangjiaResidenceInfo> fjResidenceNames = fangjiaHangMapper.getAllFjResidence();
        // 名称相同地址解析相同的小区
        List<ResidenceFjInfo> sameNameAddrList = new ArrayList<ResidenceFjInfo>();
        // 名称相同和地址解析不一样的小区
        List<ResidenceFjInfo> sameNameNotAddrList = new ArrayList<ResidenceFjInfo>();
        // 名称不一样根据地址能匹配到的小区
        List<ResidenceFjInfo> notSameNameAddrList = new ArrayList<ResidenceFjInfo>();
        // 名称不一样根据地址不能匹配到的小区
        List<ResidenceFjInfo> notSameNameNotAddrList = new ArrayList<ResidenceFjInfo>();
        System.out.println(fjResidenceNames.size());
        int num = 0;
        for(int i=0;i<fjResidenceNames.size();i++) {
            FangjiaResidenceInfo fangjiaResidenceInfo = fjResidenceNames.get(i);
            String fjResidenceName = fangjiaResidenceInfo.getResidenceName();
            String fjResidenceAddr = fangjiaResidenceInfo.getAddress();
            System.out.println("房价网ID:"+fangjiaResidenceInfo.getResidenceId());

            ResidenceFjInfo residenceFjInfo = new ResidenceFjInfo();
            residenceFjInfo.setFjResidenceName(fjResidenceName);
            residenceFjInfo.setFjResidenceId(fangjiaResidenceInfo.getResidenceId());
            residenceFjInfo.setFjAddress(fjResidenceAddr);

            // 名称匹配
            List<FangjiaResidenceInfo> residenceInfos = fangjiaHangMapper.getResidenceByName(fjResidenceName);
            FangjiaResidenceInfo residenceInfo = new FangjiaResidenceInfo();

            if (residenceInfos.size() == 1) {
                residenceInfo = residenceInfos.get(0);
                System.out.println("ID:"+residenceInfo.getResidenceId());
                // 地址解析
                Boolean result = checkAddress(fjResidenceAddr,residenceInfo.getAddress());
                if(result == true){
                    residenceFjInfo.setAddress(residenceInfo.getAddress());
                    residenceFjInfo.setResidenceId(residenceInfo.getResidenceId());
                    residenceFjInfo.setResidenceName(residenceInfo.getResidenceName());
                    sameNameAddrList.add(residenceFjInfo);
                    // 更新房价点评网小区关系表
//                    addResidenceFj(residenceInfo.getResidenceId(),
//                            fangjiaResidenceInfo.getResidenceId());
//                    System.out.println("更新成功！");
                    System.out.println("数据："+(num++));
                } else {
                    residenceFjInfo.setAddress(residenceInfo.getAddress());
                    residenceFjInfo.setResidenceId(residenceInfo.getResidenceId());
                    residenceFjInfo.setResidenceName(residenceInfo.getResidenceName());
                    sameNameNotAddrList.add(residenceFjInfo);
                }
            } else if (residenceInfos.size()>1) {
                Boolean result = false;
                for (FangjiaResidenceInfo rinfo : residenceInfos) {
                    result = checkAddress(fjResidenceAddr,rinfo.getAddress());
                    if(result == true){
                        residenceFjInfo.setAddress(rinfo.getAddress());
                        residenceFjInfo.setResidenceId(rinfo.getResidenceId());
                        residenceFjInfo.setResidenceName(rinfo.getResidenceName());
                        sameNameAddrList.add(residenceFjInfo);
                        // 更新房价点评网小区关系表
//                        addResidenceFj(rinfo.getResidenceId(),
//                                fangjiaResidenceInfo.getResidenceId());
//                        System.out.println("更新成功！");
                        System.out.println("数据："+(num++));
                        break;
                    }
                }
                if(result == false){
                    for (FangjiaResidenceInfo fjr : residenceInfos) {
                        residenceFjInfo.setAddress(fjr.getAddress());
                        residenceFjInfo.setResidenceId(fjr.getResidenceId());
                        residenceFjInfo.setResidenceName(fjr.getResidenceName());
                        sameNameNotAddrList.add(residenceFjInfo);
                    }
                }
            } else if(fjResidenceAddr != null && !"".equals(fjResidenceAddr)) {
                    List<FangjiaResidenceInfo> residenceInfoList = fangjiaHangMapper.getResidenceByAddress(
                            fjResidenceAddr);
                    if(residenceInfoList.size()>0){
                        Boolean flag = false;
                        for(FangjiaResidenceInfo residence: residenceInfoList){
                            flag = checkAddress(fjResidenceAddr,residence.getAddress());
                            if(flag){
                                ResidenceFjInfo residenceFjInfo1 = new ResidenceFjInfo();
                                residenceFjInfo1.setFjResidenceName(fjResidenceName);
                                residenceFjInfo1.setFjResidenceId(fangjiaResidenceInfo.getResidenceId());
                                residenceFjInfo1.setFjAddress(fjResidenceAddr);
                                residenceFjInfo1.setResidenceId(residence.getResidenceId());
                                residenceFjInfo1.setResidenceName(residence.getResidenceName());
                                residenceFjInfo1.setAddress(residence.getAddress());
                                notSameNameAddrList.add(residenceFjInfo1);
                            }
                        }
                        if(!flag){
                            notSameNameNotAddrList.add(residenceFjInfo);
                        }
                    } else {
                        notSameNameNotAddrList.add(residenceFjInfo);
                    }
                } else {
                notSameNameNotAddrList.add(residenceFjInfo);
            }
        }

        // 名字不同的小区
        final String SPILT = "\t";

        StringBuilder sb = new StringBuilder();
        sb.append("房价点评小区ID\t房价点评小区名字\t房价点评小区地址\t小区ID\t小区名字\t小区地址" + "\n");
        sb.append("名称相同和地址解析一样");
        sb.append("\n");
        for (ResidenceFjInfo fjresidence : sameNameAddrList) {
            sb.append(fjresidence.getFjResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getFjResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getFjAddress());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getAddress());
            sb.append("\n");
        }
        sb.append("名称相同和地址解析不一样");
        sb.append("\n");
        for (ResidenceFjInfo fjresidence : sameNameNotAddrList) {
            sb.append(fjresidence.getFjResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getFjResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getFjAddress());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getAddress());
            sb.append("\n");
        }
        sb.append("名称不一样根据地址能匹配到");
        sb.append("\n");
        for (ResidenceFjInfo fjresidence : notSameNameAddrList) {
            sb.append(fjresidence.getFjResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getFjResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getFjAddress());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getAddress());
            sb.append("\n");
        }
        sb.append("名称不一样根据地址不能匹配到");
        sb.append("\n");
        for (ResidenceFjInfo fjresidence : notSameNameNotAddrList) {
            sb.append(fjresidence.getFjResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getFjResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getFjAddress());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceId());
            sb.append(SPILT);
            sb.append(fjresidence.getResidenceName());
            sb.append(SPILT);
            sb.append(fjresidence.getAddress());
            sb.append("\n");
        }

        FileUtils.writeStringToFile(
                new File("房价点评网小区匹配output_" + (System.currentTimeMillis()) + ".excel导入(tab分割)"),
                sb.toString(), "gbk");
    }

    // 更新名字不匹配地址解析匹配相关小区的楼栋数
    public void getBuildingNum() throws Exception {
        // 更新全部小区楼栋
        fangjiaHangMapper.updateResidenceBuildingNumFromNotNameSameAddr();
        // 获取小区id有重复的小区
        List<ResidenceFjInfo> rfList = fangjiaHangMapper.getSameResidenceList();
        // 更新重复数据的小区楼栋
        for(ResidenceFjInfo fjInfo :rfList) {
            fangjiaHangMapper.updateResidenceBuildingNum(fjInfo.getResidenceId(),String.valueOf(
                    fjInfo.getBuildingNum()));
            System.out.println("更新成功！");
        }
    }

    // 根据银估通业务数据补全楼盘字典
    public void updateHouseInfoFromYGT() throws Exception {
        // 更新全部小区楼栋
        fangjiaHangMapper.updateResidenceBuildingNumFromNotNameSameAddr();
        // 获取小区id有重复的小区
        List<ResidenceFjInfo> rfList = fangjiaHangMapper.getSameResidenceList();
        // 更新重复数据的小区楼栋
        for(ResidenceFjInfo fjInfo :rfList) {
            fangjiaHangMapper.updateResidenceBuildingNum(fjInfo.getResidenceId(),String.valueOf(
                    fjInfo.getBuildingNum()));
            System.out.println("更新成功！");
        }
    }


    public void addResidenceFj(Integer residenceId, Integer fjResidenceId){
        fangjiaHangMapper.updateResidenceFj(residenceId, fjResidenceId);
    }

    // 地址解析
    public static Boolean checkAddress(String fjaddr,String addr) {
        Boolean result = false;
        if(fjaddr == null || "".equals(fjaddr) || addr == null || "".equals(addr)){
            return result;
        }
        // 楼盘字典地址
        String add = AddressUtil.analyzeAddress(addr);
        String[] addrs = null;
        if(addr.contains("，")){
            addrs = addr.split("，");
        } else if (addr.contains("、")){
            addrs = addr.split("、");
        }

        // 房价点评网地址
        String[] fjAddrs = null;
        String fjadd = AddressUtil.analyzeAddress(fjaddr);
        if(fjaddr.contains("，")){
            fjAddrs = fjaddr.split("，");
        } else if (fjaddr.contains("、")){
            fjAddrs = fjaddr.split("、");
        } else {
            String lpad = null;
            if(addrs != null){
                for(String ad: addrs){
                    System.out.println("ad:"+ad);
                    lpad = AddressUtil.analyzeAddress(ad);
                    if (fjaddr.equals(lpad)) {
                        result = true;
                        return result;
                    }
                }
            } else {
                if(fjadd.equals(add)) {
                    result = true;
                    return result;
                }
            }
        }

        String fjAds = null;
        String ads = null;
        if(fjAddrs != null) {
            for(String dpadd: fjAddrs){
                fjAds = AddressUtil.analyzeAddress(dpadd);
                if(addrs != null){
                    for(String ad: fjAddrs){
                        ads = AddressUtil.analyzeAddress(add);
                        if (fjAds.equals(ads)) {
                            result = true;
                            return result;
                        }
                    }
                } else {
                    if(fjAds.equals(add)) {
                        result = true;
                        return result;
                    }
                }
            }
        }
        return result;
    }




    public static void main(String[] args) {

        String address = "宝昌路550弄";
        String address1 = "闸北宝昌路550弄、芷江中路口";
        System.out.println(checkAddress(address, address1));
    }

}
