package com.lezhi.app.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lezhi.app.test.util.FloorUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import com.lezhi.app.model.RoomDic;
import com.lezhi.app.util.PagingUtil;

/**
 * Created by Colin Yan on 2016/8/8.
 */
@Component
public class FixPlaceFloor implements Batch {

	@Autowired
	private BuildingDicMapper buildingDicMapper;
	@Autowired
	private RoomDicMapper roomDicMapper;

	// 解析到当前楼层
	@Override
	public void start() throws IOException {
		final int PAGE_SIZE = 100000;
		int houseCount = this.roomDicMapper.count();
		
		PagingUtil.pageIndex(houseCount, PAGE_SIZE,
				(pageNo, begin, end, realPageSize, pageSize, isFirst, isLast,
						totalSize, pageCount) -> {
					RowBounds rowBounds = new RowBounds(begin, realPageSize);
					List<RoomDic> list = roomDicMapper.findAll(rowBounds);

					Set<RoomDic> set = new HashSet<>();
					Integer placeFloor = null;
					for (RoomDic b : list) {
                        placeFloor = FloorUtil.parseFloor(b.getName());
                        if (placeFloor != null) {
                            b.setPlaceFloor(placeFloor.toString());
                            set.add(b);
                        }
					}
					if (!set.isEmpty()) {
						roomDicMapper.batchUpdate(set);
					}
					System.out.println("fix place floor progress:" + pageNo
							+ "/" + pageCount);
					return true;
				});

	}

}
