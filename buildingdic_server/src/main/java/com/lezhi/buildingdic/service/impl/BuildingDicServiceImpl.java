package com.lezhi.buildingdic.service.impl;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import com.lezhi.app.model.BuildingDic;
import com.lezhi.app.model.Residence;
import com.lezhi.app.model.RoomEx;
import com.lezhi.app.service.ResidenceMatch;
import com.lezhi.app.util.AddressExtractor;
import com.lezhi.app.util.AddressModel;
import com.lezhi.buildingdic.model.BuildingModel;
import com.lezhi.buildingdic.model.HouseModel;
import com.lezhi.buildingdic.model.ResidenceModel;
import com.lezhi.buildingdic.service.BuildingDicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin Yan on 2016/8/22.
 */
@Service("buildingDicService")
public class BuildingDicServiceImpl implements BuildingDicService {

    @Autowired
    private BuildingDicMapper buildingDicMapper;
    @Autowired
    private RoomDicMapper roomDicMapper;
    @Autowired
    private ResidenceMatch residenceMatch;

    @Override
    public ResidenceModel parseResidence(String address) {
        if (StringUtils.isBlank(address))
            return null;
        AddressModel addressModel = null;
        try {
            addressModel = AddressExtractor.parseAll(address);
        } catch (Exception ignore) {}

        String inputAddr = null;

        if (addressModel != null && addressModel.getResidence() != null) {
            inputAddr = addressModel.getResidence();
        } else {
            inputAddr = address;
        }

        Residence r = residenceMatch.match(inputAddr);
        if (r != null) {
            ResidenceModel model = new ResidenceModel();
            model.setResidenceId(r.getId());
            model.setResidenceAddress(r.getAddress());
            model.setResidenceName(r.getName());
            return model;
        }
        return null;
    }

    @Override
    public HouseModel houseDetail(Integer houseId, String houseAddress, Integer residenceId) {
        if (houseId != null) {
            RoomEx room = roomDicMapper.findById(houseId);
            if (room != null) {
                HouseModel houseModel = new HouseModel();
                BeanUtils.copyProperties(room, houseModel);
                return houseModel;
            }
        } else if (houseAddress != null) {
            AddressModel addressModel = null;
            try {
                addressModel = AddressExtractor.parseAll(houseAddress);
            } catch (Exception ignore) {}

            if (addressModel != null) {

                Residence r = residenceMatch.match(addressModel.getResidence());
                if (r != null) {
                    if (residenceId == null) {
                        residenceId = r.getId();
                    }
                }

                if (residenceId != null) {
                    String buildingNo = addressModel.getBuilding();
                    String roomNo = addressModel.getRoom();

                    RoomEx room = this.roomDicMapper.findByNames(residenceId, buildingNo, roomNo);
                    if (room != null) {
                        HouseModel houseModel = new HouseModel();
                        BeanUtils.copyProperties(room, houseModel);
                        return houseModel;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<BuildingModel> buildings(Integer residenceId) {
        if (residenceId == null)
            return new ArrayList<>();

        List<BuildingDic> list = this.buildingDicMapper.findBuildingsByResidenceId(residenceId);
        if (list == null || list.isEmpty())
            return new ArrayList<>();


        List<BuildingModel> result = new ArrayList<>();

        for (BuildingDic b : list) {
            BuildingModel b1 = new BuildingModel();
            b1.setTotalFloor(b.getTotalFloor());
            b1.setBuildingId(b.getId());
            b1.setBuildingNo(b.getName());
            b1.setBaiduLat(b.getLat());
            b1.setBaiduLon(b.getLon());
            b1.setTotalRoomNum(b.getTotalRoomNum());
            result.add(b1);
        }

        return result;
    }

    @Override
    public List<HouseModel> houses(Integer buildingId) {

        if (buildingId == null)
            return new ArrayList<>();

        List<RoomEx> list = this.roomDicMapper.findByBuildingId(buildingId);
        if (list == null || list.isEmpty())
            return new ArrayList<>();

        List<HouseModel> result = new ArrayList<>();

        for (RoomEx b : list) {
            HouseModel h1 = new HouseModel();
            BeanUtils.copyProperties(b, h1);
            result.add(h1);
        }

        return result;
    }


}
