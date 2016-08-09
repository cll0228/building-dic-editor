package com.lezhi.app.webapp.controller;

import com.lezhi.app.mapper.ResidenceMapper;
import com.lezhi.app.mapper.ResolvedAddrMapper;
import com.lezhi.app.model.Residence;
import com.lezhi.app.model.ResolvedAddress;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Colin Yan on 2016/7/15.
 */
@Controller
@RequestMapping("/")
public class ResultController {

    @Autowired
    private ResolvedAddrMapper resolvedAddrMapper;
    @Autowired
    private ResidenceMapper residenceMapper;

    @RequestMapping(value = "result", method = RequestMethod.GET)
    public String resultPage(HttpServletRequest request, HttpServletResponse response) {
        return "result";
    }

    @RequestMapping(value = "result", method = RequestMethod.POST)
    public String result(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "keywords", required = false) String __keywords,
                         @RequestParam(required = false) Integer page,
                         @RequestParam(required = false) String building,
                         @RequestParam(required = false) String room
    ) {

        if (page == null || page < 1) {
            page = 1;
        }

        if (StringUtils.isNotBlank(__keywords)) {
            String keywords[] = __keywords.split(",");
            List<Residence> residences = residenceMapper.find(keywords);
            int count = 0;
            int totalPage = 0;
            if (residences != null && !residences.isEmpty()) {
                request.setAttribute("residences", residences);
                int rids[] = new int[residences.size()];
                for (int i = 0; i < residences.size(); i++) {
                    rids[i] = residences.get(i).getId();
                }
                count = resolvedAddrMapper.countByResidences("resolved_address_deal", rids, building, room);
                if (count > 0) {
                    totalPage = count / PAGE_SIZE + (count % PAGE_SIZE == 0 ? 0 : 1);
                    if (page > totalPage) {
                        page = totalPage;
                    }
                    RowBounds rowBounds = new RowBounds((page - 1) * PAGE_SIZE, PAGE_SIZE);
                    List<ResolvedAddress> list = resolvedAddrMapper.findByResidences("resolved_address_deal", rids, building, room, rowBounds);
                    request.setAttribute("list", list);

                    for (com.lezhi.app.model.Residence r : residences) {
                        boolean exists = false;
                        for (ResolvedAddress rm : list) {
                            if (r.getId().intValue() == rm.getResidenceId()) {
                                exists = true;
                                break;
                            }
                        }
                        r.setExists(exists);
                    }
                }
            }
            request.setAttribute("count", count);
            request.setAttribute("cur_page", page);
            request.setAttribute("totalPage", totalPage);
        }

        request.setAttribute("keywords", __keywords);
        request.setAttribute("building", building);
        request.setAttribute("room", room);
        return "result";
    }

    private int PAGE_SIZE = 100;

}
