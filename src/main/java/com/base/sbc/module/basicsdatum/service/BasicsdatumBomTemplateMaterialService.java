/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBomTemplateMaterialVo;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/** 
 * 类描述：基础资料-BOM模板与物料档案中间表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-8-22 17:27:44
 * @version 1.0  
 */
public interface BasicsdatumBomTemplateMaterialService extends BaseService<BasicsdatumBomTemplateMaterial>{

// 自定义方法区 不替换的区域【other_start】
    /**
     * 查询分页查询模板物料信息
     * @param queryBomTemplateDto
     * @return
     */
    PageInfo getBomTemplateMateriaList(QueryBomTemplateDto queryBomTemplateDto);


    /**
     * 添加一行物料
     * @param bomTemplateId
     * @return
     */
    Boolean addMateria(String bomTemplateId);

    /**
     * 选择物料
     * @param list
     * @return
     */
    Boolean selectMateria(List<AddRevampBomTemplateMaterialDto> list);

    /**
     * 查询bom模板下的物料id
     * @param bomTemplateId bom模板id
     * @return
     */
     List<String> getTemplateMateriaId(String bomTemplateId);

    /**
     * 删除-BOM模板物料
     * @param id bom模板物料id
     * @return
     */
    Boolean delBomTemplateMateria(String id);

    /**
     * 修改BOM模板物料
     * @param addRevampBomTemplateMaterialDto
     * @return
     */
    Boolean revampBomTemplateMaterial(AddRevampBomTemplateMaterialDto addRevampBomTemplateMaterialDto);


    /**
     * 批量启用/停用-BOM模板物料
     * @param startStopDto
     * @return
     */
    Boolean startStopBomTemplateMateria(StartStopDto startStopDto);

    /**
     * 复制BOM模板物料
     * @param id
     * @return
     */
    Boolean copyBomTemplateMateria(String id);

    /**
     * 修改顺序
     * @param revampSortDto
     * @return
     */
    Boolean revampSort(RevampSortDto revampSortDto);

    /**
     *查询模板下的物料
     * @param bomTemplateId
     * @return
     */
    List<BasicsdatumBomTemplateMaterialVo> getTemplateMateria(String bomTemplateId);

    /**
     * 批量新增修改物料
     * @param list
     * @return
     */
    Boolean batchSaveBomTemplate(List<AddRevampBomTemplateMaterialDto> list);

// 自定义方法区 不替换的区域【other_end】

	
}
