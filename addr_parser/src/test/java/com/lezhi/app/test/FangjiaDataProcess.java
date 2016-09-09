package com.lezhi.app.test;

import com.lezhi.app.test.model.FangjiaBuildingInfo;
import com.lezhi.app.test.model.FangjiaResidenceInfo;
import com.lezhi.app.test.mapper.FangjiaHangMapper;
import com.lezhi.app.test.model.FangjiaRoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        List<FangjiaResidenceInfo> fjResidenceNames = fangjiaHangMapper.getAllFjResidenceInfo();
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

}
