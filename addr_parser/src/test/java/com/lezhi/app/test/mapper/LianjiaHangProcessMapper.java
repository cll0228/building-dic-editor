package com.lezhi.app.test.mapper;

import com.lezhi.app.model.AssHang;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface LianjiaHangProcessMapper {

    int batchInsert(@Param("buildings") Set<AssHang> hang);

}