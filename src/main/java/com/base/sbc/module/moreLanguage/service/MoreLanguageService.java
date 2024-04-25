/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusQueryDto;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface MoreLanguageService {

    List<StandardColumnDto> queryCountryTitle(MoreLanguageQueryDto moreLanguageQueryDto);

    void exportExcel(MoreLanguageExcelQueryDto excelQueryDto);

    PageInfo<Map<String,Object>> listQuery(MoreLanguageQueryDto moreLanguageQueryDto);
    List<Map<String, Object>> listAllByTable(String field, String table, String where);

    // 不要删,通过反射调用
    List<StandardColumnDto> findStandardColumn(String code);
    // 不要删,通过反射调用
    List<BasicsdatumSize> findSize(String code);

    void exportMergeExcel(String bulkStyleNoList);

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

	
}
