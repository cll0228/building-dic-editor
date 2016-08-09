package com.lezhi.app.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Colin Yan on 2016/8/9.
 */
public class BatchApp {

    // export JAVA_OPTS="-Xms2048m -Xmx4096m -Xss1024K"
    // ./jrunner/jrunner.sh -cp "/home/sysadmin/addr_parser;/home/sysadmin/addr_parser/lib" -c com.lezhi.app.test.BatchApp

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/conf/applicationContext.xml",
                "classpath:/test/applicationContext-mybatis-test.xml");

        context.start();

        System.out.println("spring framework started");

        ParseAddressDb parseAddressDb = context.getBean(ParseAddressDb.class);

        CreateBuildingDicTest createBuildingDicTest = context.getBean(CreateBuildingDicTest.class);

        boolean parse = false;
        boolean create = false;

        if (args != null && args.length > 0) {
            for (String s : args) {
                if (s.equals("parse")) {
                    parse = true;
                } else if (s.equals("create")) {
                    create = true;
                }
            }
        }
        if (parse)
            parseAddressDb.start();
        if (create)
            createBuildingDicTest.start();
    }
}
