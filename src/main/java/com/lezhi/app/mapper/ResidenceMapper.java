package com.lezhi.app.mapper;

import com.lezhi.app.model.Residence;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Colin Yan on 2016/7/20.
 */
public interface ResidenceMapper {

    List<Residence> findAll();
    List<Residence> find(@Param("keywords") String keywords[]);
}
