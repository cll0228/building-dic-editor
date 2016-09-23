package com.lezhi.app.test;

import com.lezhi.app.mapper.ResolvedAddrMapper;
import com.lezhi.app.model.Residence;
import com.lezhi.app.model.ResolvedAddress;
import com.lezhi.app.service.ResidenceMatch;

import com.lezhi.app.test.util.TimeUtil;
import com.lezhi.app.util.AddressExtractor;
import com.lezhi.app.util.AddressModel;
import com.lezhi.app.util.PagingUtil;
import com.lezhi.app.test.mapper.AddrParserMapper;
import org.apache.ibatis.session.RowBounds;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Colin Yan on 2016/8/8.
 */
@Component
public class ParseAddressDb implements Batch {

    @Autowired
    private AddrParserMapper addrParserMapper;
    @Autowired
    private ResidenceMatch residenceMatch;

    /**
     * include
     */
    private Integer pkBegin = 0;
    /**
     * not parse residence id if residence id exists
     */
    private boolean skipNotNullResidenceId = true;
    /**
     * erase 'parse column' data if parsed err, residence_id survived
     */
    private boolean clearErrParsed = true;

    private final String fromTable = "address_unique";
    private final String primaryKey = "id";
    private final String addressColumn = "address";
    private final String residenceColumn = "residence";
    private final String residenceIdColumn = "residence_id";
    private final String buildingColumn = "building";
    private final String roomColumn = "room";
    private final String scoreColumn = "parsed_score";
    private final String lastParsedTime = "last_parsed_time";


    // 遇到重复地址，优先使用交易，保证交易记录在前，其它在后即可

    @Override
    public void start() throws IOException {
        final int PAGE_SIZE = 100000;

        final String whereClause = pkBegin == null ? null : primaryKey + ">=" + pkBegin;
        int count = addrParserMapper.count(fromTable, whereClause);

        PagingUtil.pageIndex(count, PAGE_SIZE, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<ResolvedAddress> list = addrParserMapper.selectAddress(primaryKey, fromTable, addressColumn, residenceIdColumn, scoreColumn, whereClause, rowBounds);
            updateResolved(list);

            System.out.println(TimeUtil.now() +" progress:" + pageNo + "/" + pageCount);

            return true;
        });

        //this.addrParserMapper.exportToPopular();

        System.out.println("finish.");
    }

    private void updateResolved(List<ResolvedAddress> list) throws IOException {
        final int PAGE_SIZE = 500;

        PagingUtil.pageCollection(list, PAGE_SIZE, (pageNo, sub, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {

            Iterator<ResolvedAddress> it = sub.iterator();
            while (it.hasNext()) {
                ResolvedAddress r = it.next();
                final String oriAddress = r.getOriAddress();
                boolean skip = false;
                if (oriAddress == null || oriAddress.trim().length() == 0) {
                    skip = true;
                } else {

                    AddressModel am = AddressExtractor.parseAll(oriAddress);
                    if (am != null && am.getResidence() != null && am.getBuilding() != null && am.getRoom() != null) {
                        r.setResidence(am.getResidence());
                        r.setBuilding(am.getBuilding());
                        r.setRoom(am.getRoom());
                        r.setParsedScore(am.getScore());
                        r.setLastParsedTime(new Date());

                        Integer residenceId = r.getResidenceId();
                        if (residenceId == null || !skipNotNullResidenceId) {
                            Residence residence = residenceMatch.match(r.getResidence());
                            if (residence != null) {
                                residenceId = residence.getId();
                            }
                        }
                        r.setResidenceId(residenceId);
                    } else {
                        skip = true;
                    }
                }
                if (skip) {
                    if (clearErrParsed) {
                        r.setResidence(null);
                        r.setBuilding(null);
                        r.setRoom(null);
                        r.setParsedScore(null);
                        r.setLastParsedTime(null);
                    } else {
                        it.remove();
                    }
                }
            }

            addrParserMapper.updateAddress((List<ResolvedAddress>) sub, primaryKey, fromTable, addressColumn, residenceColumn, residenceIdColumn,
                    buildingColumn, roomColumn, scoreColumn, lastParsedTime);
            return true;
        });
    }
}
