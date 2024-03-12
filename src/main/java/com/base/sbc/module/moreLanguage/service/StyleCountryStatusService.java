/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.moreLanguage.dto.CountryDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelResultDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusQueryDto;
import com.base.sbc.module.moreLanguage.dto.StyleCountryPrintRecordDto;
import com.base.sbc.module.moreLanguage.entity.StyleCountryPrintRecord;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/** 
 * 类描述：国家款式状态表 service类
 * @address com.base.sbc.module.moreLanguage.service.StyleCountryStatusService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:37
 * @version 1.0  
 */
public interface StyleCountryStatusService extends BaseService<StyleCountryStatus>{
// 自定义方法区 不替换的区域【other_start】

    List<MoreLanguageStatusExcelDTO> exportExcel();

    ThreadLocal<List<CountryDTO>> countryList = new TransmittableThreadLocal<>();
    List<MoreLanguageStatusExcelResultDTO> importExcel(List<MoreLanguageStatusExcelDTO> dataList);


    /**
     * 导出导入吊牌款号失败的数据
     * @param uniqueValue 唯一标识 用作 Redis 查询
     */
    void exportImportExcelFailData(String uniqueValue, HttpServletResponse response);

    PageInfo<MoreLanguageStatusDto> listQuery(MoreLanguageStatusQueryDto statusQueryDto);

    boolean updateStatus(List<StyleCountryStatus> updateStatus, List<HangTag> hangTagList, boolean needUpdateHangTag);

// 自定义方法区 不替换的区域【other_end】

	
}
