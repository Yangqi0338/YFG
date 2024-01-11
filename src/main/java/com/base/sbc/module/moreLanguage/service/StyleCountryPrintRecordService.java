/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.moreLanguage.dto.StyleCountryPrintRecordDto;
import com.base.sbc.module.moreLanguage.entity.StyleCountryPrintRecord;

import java.util.Map;

/** 
 * 类描述：款式国家打印表 service类
 * @address com.base.sbc.module.moreLanguage.service.CountryService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:37
 * @version 1.0  
 */
public interface StyleCountryPrintRecordService extends BaseService<StyleCountryPrintRecord>{
    StyleCountryPrintRecordDto findPrintRecordByStyleNo(HangTagMoreLanguageDTO languageDTO);

    void savePrintRecord(HangTagMoreLanguageSystemDTO languageDTO);

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

	
}
