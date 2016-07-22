package com.lezhi.app.webapp;

import com.lezhi.app.mapper.BuildingDicMapper;
import com.lezhi.app.mapper.RoomDicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class WebAppInitializeBean implements ServletContextAware {


    @Override
    public void setServletContext(ServletContext context) {
        context.setAttribute("ctx", context.getContextPath());
    }

}
