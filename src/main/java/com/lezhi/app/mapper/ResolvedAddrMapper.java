package com.lezhi.app.mapper;

import com.lezhi.app.model.Address;
import com.lezhi.app.model.ResolvedAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Colin Yan on 2016/7/12.
 */
public interface ResolvedAddrMapper {

    Integer countAll();
    Integer countAllWithBuilding();

    Integer count(@Param("keywords") String keywords[], @Param("building") String building, @Param("room") String room);

    Integer countByResidences(@Param("rids") int rids[], @Param("building") String building, @Param("room") String room);

    List<ResolvedAddress> findAll(RowBounds rowBounds);
    List<ResolvedAddress> findAllWithBuilding(RowBounds rowBounds);

    List<ResolvedAddress> find(@Param("keywords") String keywords[], @Param("building") String building, @Param("room") String room, RowBounds rowBounds);

    List<ResolvedAddress> findByResidences(@Param("rids") int rids[], @Param("building") String building, @Param("room") String room, RowBounds rowBounds);
}
