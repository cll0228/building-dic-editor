import com.lezhi.buildingdic.model.ResidenceModel;
import com.lezhi.buildingdic.service.BuildingDicService;
import org.apache.commons.io.FileUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Colin Yan on 2016/8/23.
 */
public class App {

    private static BuildingDicService buildingDicService;

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dubbo-consumer.xml");
        context.start();
        BuildingDicService service = null;
        buildingDicService = (BuildingDicService) context.getBean(BuildingDicService.class);
        System.out.println("spring framework started");

        List<String> lines = FileUtils.readLines(new File("E:\\1111.txt"), "gbk");
        for (String s : lines) {
            String arr[]= s.split("\t");

            ResidenceModel r= service.parseResidence(arr[1]);


            if (r != null) {
                s += "\t" + r.getResidenceId();
            } else {
                s +="\t";
            }

            System.out.println(s);
        }
        System.out.println(service);
    }

    class Input {
        //13011	现代华庭	东北	3	302	58.35	1	公寓	非临街	低层	平面

        private int excelId;

        private String residenceName;
        private int towards;

        private int placeFloor;

        private String roomNo;

        private double area;

        private int totalFloor;

        private int propertyType;

        private Integer nearStreet;

        private Integer planeType;


    }
}
