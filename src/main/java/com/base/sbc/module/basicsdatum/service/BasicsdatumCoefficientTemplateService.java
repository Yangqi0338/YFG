/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.dto.AddUpdateCoefficientTemplateDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumCoefficientTemplateDto;
import com.base.sbc.module.basicsdatum.dto.IdDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumCoefficientTemplateVo;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCoefficientTemplate;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/** 
 * 类描述：基础资料-纬度系数模板 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCoefficientTemplateService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-15 14:34:41
 * @version 1.0  
 */
public interface BasicsdatumCoefficientTemplateService extends BaseService<BasicsdatumCoefficientTemplate>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取模板列表
     * @param dto
     * @return
     */
    PageInfo getCoefficientTemplateList(BasicsdatumCoefficientTemplateDto dto);

    /**
     * 新增保存
     * @param dto
     * @return
     */
    BasicsdatumCoefficientTemplate addUpdateCoefficientTemplate(@RequestBody AddUpdateCoefficientTemplateDto dto);

    /**
     * 停用启用
     * @param startStopDto
     * @return
     */
    Boolean startStopCoefficientTemplate(@Valid @RequestBody StartStopDto startStopDto);

    /**
     * 复制模板
     * @param dto
     * @return
     */
    Boolean copyTemplate(BasicsdatumCoefficientTemplateDto dto);

    /**
     * 获取模板详情
     * @param idDto
     * @return
     */
    BasicsdatumCoefficientTemplateVo  getTemplateDetails(IdDto idDto);


    /**
     * 导出
     * @param dto
     * @param response
     */
    void deriveExcel(BasicsdatumCoefficientTemplateDto dto, HttpServletResponse response) throws IOException;

// 自定义方法区 不替换的区域【other_end】

	
}
