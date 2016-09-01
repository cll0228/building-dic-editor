package com.lezhi.app.test;

import com.lezhi.app.test.mapper.LianjiaHangMapper;
import com.lezhi.app.test.mapper.LianjiaHangProcessMapper;
import com.lezhi.app.model.AssHang;
import com.lezhi.app.test.model.LianjiaHang;
import com.lezhi.app.test.util.FloorStructureUtil;
import com.lezhi.app.test.util.LianjiaResidenceMapping;
import com.lezhi.app.test.util.PropertyTypeUtil;
import com.lezhi.app.util.PagingUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by Colin Yan on 2016/8/8.
 */
@Component
public class HangProcess {

    @Autowired
    private LianjiaHangMapper lianjiaHangMapper;
    @Autowired
    private LianjiaHangProcessMapper lianjiaHangProcessMapper;

    private volatile long idBegin = 155050000;

    public void start() throws IOException {

        System.out.println(LianjiaResidenceMapping.class);

        final int PAGE_SIZE = 10000;

        int count = lianjiaHangMapper.count();

        PagingUtil.pageIndex(count, PAGE_SIZE, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<LianjiaHang> list = lianjiaHangMapper.find(rowBounds);
            process(list);
            System.out.println("progress:" + pageNo + "/" + pageCount);
            return true;
        });

        saveAndClean(map.get(time));
    }

    private volatile Date time = new Date(0);

    private Map<Date, Set<LianjiaHang>> map = new HashMap<>();

    private static final long WEEK_LONG = 1000 * 60 * 60 * 24 * 7;

    // 按日期排序的列表
    private void process(List<LianjiaHang> list) throws IOException {

        for (LianjiaHang h : list) {
            final Date hangDate = h.getQuotationDate();
            Set<LianjiaHang> set = null;
            if (hangDate.getTime() - time.getTime() > WEEK_LONG) {
                saveAndClean(map.get(time));

                set = new HashSet<>();
                map.put(hangDate, set);
                time = hangDate;
            } else {
                set = map.get(time);
                if (set == null)
                    throw new NullPointerException();
            }
            set.add(h);
        }
    }

    private void saveAndClean(Set<LianjiaHang> set) throws IOException {
        if (set == null || set.isEmpty())
            return;

        Set<AssHang> assHangs = new HashSet<>();

        final int PAGE_SIZE = 1000;

        final int count = set.size();

        Iterator<LianjiaHang> it = set.iterator();

        PagingUtil.pageIndex(count, PAGE_SIZE, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            assHangs.clear();
            for (int i = 0; i < realPageSize; i++) {
                LianjiaHang lianjiaHang = it.next();

                if (lianjiaHang.getResidenceId() == null)
                    continue;

                if (lianjiaHang.getTotalFloor() == null)
                    continue;

                AssHang assHang = new AssHang();
                assHang.setTotalFloor(lianjiaHang.getTotalFloor() != null ? lianjiaHang.getTotalFloor().intValue() : null);
                assHang.setPropertyArea(lianjiaHang.getPropertyArea() != null ? Double.valueOf(lianjiaHang.getPropertyArea()) : null);
                assHang.setResidenceId(LianjiaResidenceMapping.getResidenceIdByLianjiaId(lianjiaHang.getResidenceId()));

                if (assHang.getResidenceId() == null)
                    continue;

                assHang.setBasementArea(null);
                assHang.setFloorStructureId(FloorStructureUtil.parse(lianjiaHang.getPlaceFloor()));

                assHang.setPropertyTypeId(PropertyTypeUtil.parse(lianjiaHang.getHouseType()));
                if (assHang.getPropertyTypeId() == null) {
                    assHang.setPropertyTypeId(LianjiaResidenceMapping.getPropertyType(assHang.getResidenceId()));
                    if (assHang.getPropertyTypeId() == null) {
                        continue;
                    }
                }

                assHang.setQuotationDate(lianjiaHang.getQuotationDate());
                assHang.setUnitPrice(lianjiaHang.getQuotationUnitPrice() != null ? lianjiaHang.getQuotationUnitPrice().intValue() : null);
                assHang.setTotalPrice(lianjiaHang.getQuotationTotalPrice() != null ? lianjiaHang.getQuotationTotalPrice().doubleValue() : null);
                assHang.setPropertyRoom(lianjiaHang.getPropertyRoom() != null ? lianjiaHang.getPropertyRoom().intValue() : null);

                if (assHang.getFloorStructureId() == null)
                    continue;

                assHang.setPlaceFloor(FloorStructureUtil.calc(lianjiaHang.getTotalFloor(), assHang.getFloorStructureId()));

                if (assHang.getPlaceFloor() == null)
                    continue;

                assHang.setQuotationId(++idBegin);
                assHangs.add(assHang);
            }
            if (!assHangs.isEmpty())
                lianjiaHangProcessMapper.batchInsert(assHangs);
            return true;
        });

        map.remove(time);
    }
}
