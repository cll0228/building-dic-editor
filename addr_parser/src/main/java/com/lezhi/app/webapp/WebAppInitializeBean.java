package com.lezhi.app.webapp;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;

@Component
public class WebAppInitializeBean implements ServletContextAware {

    private File cacheFile;

    @Override
    public void setServletContext(ServletContext context) {
        context.setAttribute("ctx", context.getContextPath());
        cacheFile = new File(context.getRealPath("/"), "stat.cache");
    }

    public File getCacheFile() {
        return cacheFile;
    }

    public void setCacheFile(File cacheFile) {
        this.cacheFile = cacheFile;
    }
}
