import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.base.sbc.PdmApplication;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.SystemSource;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailAuditDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailOldDTO;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;

/**
 * {@code 描述：添加数据}
 * @author KC
 * @since 2024/3/18
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PdmApplication.class)
public class AddData {

    @Autowired
    private StyleCountryStatusService styleCountryStatusService;

    @Test
    public void setPdmHangTagDbV2(){
        List<StyleCountryStatus> countryStatusList = styleCountryStatusService.list();
        List<StyleCountryStatus> countryStatusList1 = new ArrayList<>();
        countryStatusList.forEach(it-> {
            String checkDetailJson = it.getCheckDetailJson();
            List<MoreLanguageStatusCheckDetailOldDTO> list = JSONUtil.toList(checkDetailJson, MoreLanguageStatusCheckDetailOldDTO.class);
            list.stream().collect(Collectors.groupingBy(MoreLanguageStatusCheckDetailOldDTO::getType)).forEach((type,oldDTOList)-> {
                StyleCountryStatus newStatus = it;
                if (type.equals(CountryLanguageType.WASHING.getCode())) {
                    newStatus = MORE_LANGUAGE_CV.copyMyself(it);
                    newStatus.setId(null);
                    newStatus.updateClear();
                    newStatus.insertInit();
                }
                newStatus.setType(CountryLanguageType.findByCode(type));
                List<MoreLanguageStatusCheckDetailDTO> checkDetailDTOS = new ArrayList<>();
                oldDTOList.stream().collect(Collectors.groupingBy(MoreLanguageStatusCheckDetailOldDTO::getLanguageCode)).forEach((languageCode,sameCodeList)-> {
                    MoreLanguageStatusCheckDetailDTO checkDetailDTO = new MoreLanguageStatusCheckDetailDTO();
                    checkDetailDTO.setLanguageCode(languageCode);

                    List<MoreLanguageStatusCheckDetailAuditDTO> auditDTOList = new ArrayList<>();
                    sameCodeList.get(0).getStandardColumnCodeList().forEach(standardColumnCode-> {
                        MoreLanguageStatusCheckDetailAuditDTO auditDTO = new MoreLanguageStatusCheckDetailAuditDTO();
                        auditDTO.setStatus(it.getStatus());
                        auditDTO.setStandardColumnCode(standardColumnCode);
                        auditDTO.setContent("");
                        auditDTOList.add(auditDTO);
                    });
                    checkDetailDTO.setAuditList(auditDTOList);
                    checkDetailDTOS.add(checkDetailDTO);
                });
                newStatus.setCheckDetailJson(JSONUtil.toJsonStr(checkDetailDTOS));
                countryStatusList1.add(newStatus);
            });
        });

        styleCountryStatusService.saveOrUpdateBatch(countryStatusList1);
    }
}
