package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.basicsdatum.vo.ColorModelNumberBaseSelectVO;
import com.base.sbc.module.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ColorModelNumberService extends BaseService<ColorModelNumber> {
    Boolean saveColorModelNumber(ColorModelNumber colorModelNumber);

    Boolean importExcel(MultipartFile file) throws Exception;

    /**
     * 查询色号和型号拉下框组件基本信息
     * @param distCode
     * @param fileName
     * @param userCompany
     * @return
     */
    List<ColorModelNumberBaseSelectVO> getByDistCode(String distCode, String fileName, String userCompany);
}
