package com.lezhi.app.test.mapper;

import com.lezhi.app.model.ResolvedAddress;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.test.model.FixPlaceFloorModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/8/8.
 * <p>
 * 此Mapper 读取目标表原始字段，前解析地址后，把解析出来的3个字段，更新到数据表
 */
public interface AddrParserMapper {

    int count(
            @Param("addressTable") String addressTable,
            @Param("whereClause") String whereClause
    );

    List<ResolvedAddress> selectAddress(@Param("primaryKey") String primaryKey,
                                        @Param("addressTable") String addressTable,
                                        @Param("addressColumn") String addressColumn,
                                        @Param("residenceIdColumn") String residenceIdColumn,
                                        @Param("scoreColumn") String scoreColumn,
                                        @Param("whereClause") String whereClause,
                                        RowBounds rowBounds
    );

    void updateAddress(@Param("resolvedAddressList") List<ResolvedAddress> resolvedAddresses,
                       @Param("primaryKey") String primaryKey,
                       @Param("addressTable") String addressTable,
                       @Param("addressColumn") String addressColumn,
                       @Param("residenceColumn") String residence,
                       @Param("residenceIdColumn") String residenceIdColumn,
                       @Param("buildingColumn") String buildingColumn,
                       @Param("roomColumn") String roomColumn,
                       @Param("scoreColumn") String scoreColumn,
                       @Param("lastParsedTime") String lastParsedTime
    );

    //void exportToPopular();

    List<FixPlaceFloorModel> findPlaceFloorModels(@Param("tableName") String tableName,
                                                  @Param("primaryKey") String primaryKey,
                                                  @Param("roomNoColumn") String roomNoColumn,
                                                  @Param("placeFloorColumn") String placeFloorColumn,
                                                  RowBounds rowBounds);


    int batchUpdatePlaceFloor(@Param("tableName") String tableName,
                    @Param("primaryKey") String primaryKey,
                    @Param("placeFloorColumn") String placeFloorColumn,
                    @Param("params") Set<FixPlaceFloorModel> params);
}
