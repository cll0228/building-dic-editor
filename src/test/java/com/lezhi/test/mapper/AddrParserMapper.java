package com.lezhi.test.mapper;

import com.lezhi.app.model.ResolvedAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

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
                                        @Param("whereClause") String whereClause,
                                        RowBounds rowBounds
    );

    void updateAddress(@Param("resolvedAddressList") List<ResolvedAddress> resolvedAddresses,
                       @Param("primaryKey") String primaryKey,
                       @Param("addressTable") String addressTable,
                       @Param("addressColumn") String addressColumn,
                       @Param("residenceColumn") String residence,
                       @Param("residenceIdColumn") String residenceIdColumn,
                       @Param("buildingColumn") String building,
                       @Param("roomColumn") String room
    );


}
