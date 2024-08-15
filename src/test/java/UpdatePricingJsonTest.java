import com.base.sbc.PdmApplication;
import com.base.sbc.module.pack.service.PackPricingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PdmApplication.class)
public class UpdatePricingJsonTest {

    @Autowired
    PackPricingService packPricingService;

    @Test
    public void updatePricingJson() {
        packPricingService.updatePricingJson(1, 10);
    }

}
