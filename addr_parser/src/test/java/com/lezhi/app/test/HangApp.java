package com.lezhi.app.test;


import com.lezhi.app.test.HangProcess;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Colin Yan on 2016/8/31.
 */
public class HangApp {

    /*
        export JAVA_HOME=/usr/local/jdk1.8.0_91; \
        export PATH=$JAVA_HOME/bin:$PATH; \
        export JAVA_OPTS="-Xms2048m -Xmx4096m -Xss1024K"; \
        /home/sysadmin/jrunner/jrunner.sh -cp "/home/sysadmin/addr_parser;/home/sysadmin/addr_parser/lib" -c BatchApp parse create

    */
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:/conf/applicationContext.xml",
                "classpath:/test/applicationContext-mybatis-test.xml"
        );

        context.start();

        System.out.println("spring framework started");

        HangProcess hangProcess = context.getBean(HangProcess.class);

        hangProcess.start();
    }
}
