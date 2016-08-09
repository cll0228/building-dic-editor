package com.lezhi.app.mapper;

import com.lezhi.app.model.Address;
import com.lezhi.app.model.ResolvedAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/7/12.
 */
public interface ResolvedAddrMapper {

    Integer countAll(@Param("tn") String tableName);

    Integer countAllWithBuilding(@Param("tn") String tableName);

    Integer count(@Param("tn") String tableName, @Param("keywords") String keywords[], @Param("building") String building, @Param("room") String room);

    Integer countByResidences(@Param("tn") String tableName, @Param("rids") int rids[], @Param("building") String building, @Param("room") String room);

    List<ResolvedAddress> findAll(@Param("tn") String tableName, RowBounds rowBounds);

    List<ResolvedAddress> findAllWithBuilding(@Param("tn") String tableName, RowBounds rowBounds);

    List<ResolvedAddress> find(@Param("tn") String tableName, @Param("keywords") String keywords[], @Param("building") String building, @Param("room") String room, RowBounds rowBounds);

    List<ResolvedAddress> findByResidences(@Param("tn") String tableName, @Param("rids") int rids[], @Param("building") String building, @Param("room") String room, RowBounds rowBounds);

    int fillRid(@Param("tn") String tableName, @Param("resolvedAddressList") Set<ResolvedAddress> resolvedAddressList);

}
