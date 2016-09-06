package com.lezhi.app.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by Colin Yan on 2016/8/9.
 */
public class BatchApp {
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

        ParseAddressDb parseAddressDb = context.getBean(ParseAddressDb.class);

        CreateBuildingDicTest createBuildingDicTest = context.getBean(CreateBuildingDicTest.class);

        FixTopFloor fixTopFloor = context.getBean(FixTopFloor.class);
        
        FixPlaceFloor fixPlaceFloor = context.getBean(FixPlaceFloor.class);

        HangProcess hangProcess = context.getBean(HangProcess.class);

        boolean parse = false;
        boolean create = false;
        boolean top = false;
        boolean hang = false;
        boolean placeFloor = true;

        if (args != null && args.length > 0) {
            for (String s : args) {
                if (s.equals("parse")) {
                    parse = true;
                } else if (s.equals("create")) {
                    create = true;
                } else if (s.equals("top")) {
                    top = true;
                } else if (s.equals("hang")) {
                    hang = true;
                }
            }
        }

        if (parse)
            parseAddressDb.start();
        if (create) {
            createBuildingDicTest.start();
        }
        if (top) {
            fixTopFloor.start();
        }
        if (hang) {
            hangProcess.start();
        }
        if (placeFloor) {
        	fixPlaceFloor.start();
        }
    }
}
