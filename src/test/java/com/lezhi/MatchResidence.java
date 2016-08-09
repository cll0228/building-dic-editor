package com.lezhi;

import com.lezhi.app.mapper.ResidenceMapper;
import com.lezhi.app.mapper.ResolvedAddrMapper;
import com.lezhi.app.model.Residence;
import com.lezhi.app.model.ResolvedAddress;
import com.lezhi.app.util.PagingUtil;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Colin Yan on 2016/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/conf/applicationContext.xml"})
public class MatchResidence {

    @Autowired
    private ResidenceMapper residenceMapper;
    @Autowired
    private ResolvedAddrMapper resolvedAddrMapper;

    private List<Residence> residences;

    @Test
    public void test1() throws IOException {
        initResidences();

        final String tableName = "resolved_address_except_deal";

        final int PAGE_SIZE = 10000;
        int count = resolvedAddrMapper.countAll(tableName);
        System.out.println("总数量:" + count);
        Set<ResolvedAddress> resolvedAddressSet = new HashSet<>();

        PagingUtil.pageIndex(count, PAGE_SIZE, (pageNo, begin, end, realPageSize, pageSize, isFirst, isLast, totalSize, pageCount) -> {

            RowBounds rowBounds = new RowBounds(begin, realPageSize);
            List<ResolvedAddress> list = resolvedAddrMapper.findAll(tableName, rowBounds);
            System.out.println("[" +pageNo + "/" + pageCount + "]分布查询结果数量:" + list.size());
            int needToFillLen = 0;

            resolvedAddressSet.clear();
            for (ResolvedAddress address : list) {
                if (address.getResidenceId() == null || address.getResidenceId() <= 0) {
                    Residence residence = match(address.getResidence());
                    if (residence != null) {
                        ResolvedAddress r = new ResolvedAddress();
                        r.setResidenceId(residence.getId());
                        r.setId(address.getId());
                        resolvedAddressSet.add(r);
                    }
                    needToFillLen++;
                }
            }

            batchUpdate(resolvedAddressSet);
            System.out.println("需要补全的数量:" + needToFillLen + ",实际补全的数量:" + resolvedAddressSet.size());
            return true;
        });

    }

    private void batchUpdate(Set<ResolvedAddress> resolvedAddressSet) {
        PagingUtil.pageCollection(resolvedAddressSet, 1000, (pageNo, sub, begin, end, countInPage, pageSize, isFirst, isLast, totalSize, pageCount) -> {
            resolvedAddrMapper.fillRid("resolved_address_except_deal", (Set<ResolvedAddress>) sub);
            return true;
        });
    }

    private void initResidences() {
        residences = residenceMapper.findAll();
    }

    private Residence match(String residenceKeyword) {
        for (Residence r : residences) {
            if (r.getName().equals(residenceKeyword)) {
                return r;
            }
            if (r.getAddress().contains(residenceKeyword)) {
                return r;
            }
        }
        return null;
    }


}
