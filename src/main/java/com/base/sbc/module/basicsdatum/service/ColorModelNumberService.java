package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ColorModelNumberService extends BaseService<ColorModelNumber> {
    Boolean saveColorModelNumber(ColorModelNumber colorModelNumber);

    Boolean importExcel(MultipartFile file) throws Exception;
}
