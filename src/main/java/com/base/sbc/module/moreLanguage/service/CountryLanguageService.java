/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.CountryTypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.standard.entity.StandardColumn;

import java.util.List;

/** 
 * 类描述：国家地区表 service类
 * @address com.base.sbc.module.moreLanguage.service.CountryService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:37
 * @version 1.0  
 */
public interface CountryLanguageService extends BaseService<CountryLanguage>{
    List<CountryLanguageDto> listQuery(CountryQueryDto countryQueryDto);

    String save(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto, boolean cache);

    String review(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto);

    CountryTypeLanguageSaveDto detail(MoreLanguageQueryDto queryDto);

    List<StandardColumn> findStandardColumnList(String code, CountryLanguageType type, boolean cache);

    List<String> findStandardColumnCodeList(String code, CountryLanguageType type, boolean cache);

    String cancelSave(String code);

    void initLanguage(List<BasicBaseDict> dictList);
    List<CountryDTO> getAllCountry(String code);
    long getAllCountrySize();

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

	
}
