package com.lezhi.buildingdic.service;

import com.lezhi.buildingdic.model.BuildingModel;
import com.lezhi.buildingdic.model.HouseModel;
import com.lezhi.buildingdic.model.ResidenceModel;

import java.util.List;

/**
 * 地址解析
 */
public interface BuildingDicService {

    /**
     * 根据输入的地址，返回小区对象，返回的是简单小区对象
     *
     * @param address 一个地址，包含小区名称/地址楼栋号、室号等信息
     * @return 小区对象，返回的是简单小区对象
     */
    ResidenceModel parseResidence(String address);

    /**
     * @param houseId      房屋Id	二选一	Integer	不限
     * @param houseAddress 房源所在地址	二选一	String	不限
     *                     <br/><font color="red">如果以上两个字段都不为空，则忽略第二个并且不校验</font>
     * @param residenceId  小区ID	否	Integer	不限
     * @return 房屋详情
     */
    HouseModel houseDetail(Integer houseId, String houseAddress, Integer residenceId);

    /**
     * 获取小区下所有楼栋列表
     *
     * @param residenceId 小区ID
     * @return 楼栋列表
     */
    List<BuildingModel> buildings(Integer residenceId);

    /**
     * 获取楼栋下所有房屋
     *
     * @param buildingId 楼栋ID
     * @return 房屋列表
     */
    List<HouseModel> houses(Integer buildingId);
}
