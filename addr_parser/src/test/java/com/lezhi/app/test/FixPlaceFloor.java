package com.lezhi.app.test;

import com.lezhi.app.test.mapper.AddrParserMapper;
import com.lezhi.app.test.model.FixPlaceFloorModel;
import com.lezhi.app.test.util.FloorUtil;
import com.lezhi.app.util.PagingUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/8/8.
 */
@Component
public class FixPlaceFloor implements Batch {


	@Autowired
	private AddrParserMapper addrParserMapper;

	private final String tableName = "ocn_std_addr_minhang";
	private final String primaryKey = "id";
	private final String roomNoColumn = "room";
	private final String placeFloorColumn = "place_floor";

	// 解析到当前楼层
	@Override
	public void start() throws IOException {
		final int PAGE_SIZE = 100000;
		int houseCount = this.addrParserMapper.count(tableName, null);
		
		PagingUtil.pageIndex(houseCount, PAGE_SIZE,
				(pageNo, begin, end, realPageSize, pageSize, isFirst, isLast,
						totalSize, pageCount) -> {
					RowBounds rowBounds = new RowBounds(begin, realPageSize);
					List<FixPlaceFloorModel> list = addrParserMapper.findPlaceFloorModels(tableName, primaryKey, roomNoColumn,
							placeFloorColumn,
							rowBounds);

					Set<FixPlaceFloorModel> set = new HashSet<>();
					Integer placeFloor = null;
					for (FixPlaceFloorModel b : list) {
                        placeFloor = FloorUtil.parseFloor(b.getRoomNo());
                        if (placeFloor != null) {
                            b.setPlaceFloor(placeFloor);
                            set.add(b);
                        }
					}
					if (!set.isEmpty()) {
						addrParserMapper.batchUpdatePlaceFloor(tableName, primaryKey, placeFloorColumn, set);
					}
					System.out.println("fix place floor progress:" + pageNo
							+ "/" + pageCount);
					return true;
				});

	}

}
