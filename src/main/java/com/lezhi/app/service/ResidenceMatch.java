package com.lezhi.app.service;

import com.lezhi.app.mapper.ResidenceMapper;
import com.lezhi.app.model.Residence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Colin Yan on 2016/8/8.
 */
@Component
public class ResidenceMatch {

    @Autowired
    private ResidenceMapper residenceMapper;

    private List<Residence> residences;

    @PostConstruct
    public void init() {
        residences = residenceMapper.findAll();
    }

    public Residence match(String residenceKeyword) {
        if (residenceKeyword == null)
            return null;

        for (Residence r : residences) {
            final String ra = r.getAddress();
            final String rn = r.getName();
            if (ra == null || rn == null)
                continue;

            if (rn.equals(residenceKeyword)) {
                return r;
            }
            if (ra.contains(residenceKeyword)) {
                return r;
            }
        }
        return null;
    }

}
