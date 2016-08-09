package com.lezhi.app.mapper;

import com.lezhi.app.model.Address;
import com.lezhi.app.model.ResolvedAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/7/12.
 */
public interface AddrSourceMapper {

    Integer count(@Param("tableName") String tableName);

    List<Address> findAddress(@Param("tableName") String tableName, RowBounds rowBounds);

    Integer countIncludeDel(@Param("tableName") String tableName);

    List<Address> findAddressIncludeDel(@Param("tableName") String tableName, RowBounds rowBounds);

}
