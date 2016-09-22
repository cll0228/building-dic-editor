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
        $JAVA_HOME/bin/java -Djava.ext.dirs=.:lib com.lezhi.app.test.BatchApp parse create top floor

    */
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:/conf/applicationContext.xml",
                "classpath:/test/applicationContext-mybatis-test.xml"
        );

        context.start();

        System.out.println("spring framework started");

        Batch parseAddressDb = null;

        Batch createBuildingDicTest = null;

        Batch fixTopFloor = null;

        Batch fixPlaceFloor = null;

        Batch fixArea = null;

        if (args != null && args.length > 0) {
            for (String s : args) {
                switch (s) {
                    case "parse":
                        parseAddressDb = context.getBean(ParseAddressDb.class);
                        parseAddressDb.start();
                        break;
                    case "create":
                        createBuildingDicTest = context.getBean(CreateBuildingDicTest.class);
                        createBuildingDicTest.start();
                        break;
                    case "top":
                        fixTopFloor = context.getBean(FixTopFloor.class);
                        fixTopFloor.start();
                        break;
                    case "floor":
                        fixPlaceFloor = context.getBean(FixPlaceFloor.class);
                        fixPlaceFloor.start();
                        break;
                    case "area":
                        fixArea = context.getBean(FixArea.class);
                        fixArea.start();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
