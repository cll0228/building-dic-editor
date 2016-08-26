import com.lezhi.buildingdic.service.BuildingDicService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Colin Yan on 2016/8/23.
 */
public class App {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dubbo-consumer.xml");
        context.start();
        BuildingDicService service = null;
        service = (BuildingDicService) context.getBean(BuildingDicService.class);
        System.out.println("spring framework started");

        System.out.println(service);
    }
}
