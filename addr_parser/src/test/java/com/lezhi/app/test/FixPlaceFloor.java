package com.lezhi.app.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class FixPlaceFloor {

	@Autowired
	private BuildingDicMapper buildingDicMapper;
	@Autowired
	private RoomDicMapper roomDicMapper;

	// 解析到当前楼层
	public void start() throws IOException {
		final int PAGE_SIZE = 10;
		int buildingCount = this.buildingDicMapper.count();
		
		PagingUtil.pageIndex(buildingCount, PAGE_SIZE,
				(pageNo, begin, end, realPageSize, pageSize, isFirst, isLast,
						totalSize, pageCount) -> {
					RowBounds rowBounds = new RowBounds(begin, realPageSize);
					List<RoomDic> list = roomDicMapper.findAll(rowBounds);

					Set<RoomDic> set = new HashSet<>();
					Integer placeFloor = null;
					for (RoomDic b : list) {
						try {
							int roomNo = Integer.parseInt(b.getName());

							placeFloor = roomNo / 100;
							int r = roomNo % 100;

							if (placeFloor >= 1 && placeFloor < 50) {
								if (r > 0 && r < 20) {
									b.setPlaceFloor(placeFloor.toString());
									set.add(b);
								}
							}

						} catch (Exception ignored) {
						}
					}
					if (!set.isEmpty()) {
						roomDicMapper.batchUpdate(set);
					}
					System.out.println("fix total floor progress:" + pageNo
							+ "/" + pageCount);
					return true;
				});

	}

}
