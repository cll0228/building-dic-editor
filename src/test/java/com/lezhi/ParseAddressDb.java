package com.lezhi;

import com.lezhi.app.model.Residence;
import com.lezhi.app.model.ResolvedAddress;
import com.lezhi.app.service.ResidenceMatch;
import com.lezhi.app.util.AddressExtractor;
import com.lezhi.app.util.AddressModel;
import com.lezhi.app.util.PagingUtil;
import com.lezhi.app.util.PreHandle;
import com.lezhi.test.mapper.AddrParserMapper;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Colin Yan on 2016/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/conf/applicationContext.xml", "classpath:/test/applicationContext-mybatis-test.xml"})
public class ParseAddressDb {

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

    @Test
    public void start() throws IOException {
        final int PAGE_SIZE = 100000;

        final String whereClause = pkBegin == null ? null : primaryKey + ">=" + pkBegin;
        int count = addrParserMapper.count(fromTable, whereClause);

        PagingUtil.pageIndex(count, PAGE_SIZE, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<ResolvedAddress> list = addrParserMapper.selectAddress(primaryKey, fromTable, addressColumn, residenceIdColumn, scoreColumn, whereClause, rowBounds);
            updateResolved(list);
            return true;
        });

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
                    String address = PreHandle.handle(oriAddress);
                    AddressModel am = AddressExtractor.parseAll(address);
                    if (am != null && am.getResidence() != null && am.getBuilding() != null && am.getRoom() != null) {
                        r.setResidence(am.getResidence());
                        r.setBuilding(am.getBuilding());
                        r.setRoom(am.getRoom());
                        r.setParsedScore(am.getScore());

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
                    } else {
                        it.remove();
                    }
                }
            }

            addrParserMapper.updateAddress((List<ResolvedAddress>) sub, primaryKey, fromTable, addressColumn, residenceColumn, residenceIdColumn, buildingColumn, roomColumn, scoreColumn);
            return true;
        });
    }
}
