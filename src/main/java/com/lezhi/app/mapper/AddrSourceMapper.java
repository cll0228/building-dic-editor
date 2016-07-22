package com.lezhi.app.mapper;

import com.lezhi.app.model.Address;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Colin Yan on 2016/7/12.
 */
public interface AddrSourceMapper {

    Integer countAll2();

    List<Address> findAllAddress2(RowBounds rowBounds);

    Integer countAll_deal();

    List<Address> findAllAddress_deal(RowBounds rowBounds);

    Integer countAll_300w();

    List<Address> findAllAddress_300w(RowBounds rowBounds);
}
