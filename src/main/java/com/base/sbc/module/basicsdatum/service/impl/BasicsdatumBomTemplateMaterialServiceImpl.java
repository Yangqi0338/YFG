/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.QueryBomTemplateDto;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialMapper;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumRangeDifferenceVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumBomTemplateMaterialMapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/** 
 * 类描述：基础资料-BOM模板与物料档案中间表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-8-22 17:27:44
 * @version 1.0  
 */
@Service
public class BasicsdatumBomTemplateMaterialServiceImpl extends BaseServiceImpl<BasicsdatumBomTemplateMaterialMapper, BasicsdatumBomTemplateMaterial> implements BasicsdatumBomTemplateMaterialService {


// 自定义方法区 不替换的区域【other_start】

    /**
     * 查询分页查询模板物料信息
     *
     * @param queryBomTemplateDto
     * @return
     */
    @Override
    public PageInfo getBomTemplateMateriaList(QueryBomTemplateDto queryBomTemplateDto) {
        if (StringUtils.isBlank(queryBomTemplateDto.getBomTemplateId())) {
            throw new OtherException("缺少bom模板id");
        }
        /*查询*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("tbbt.id", queryBomTemplateDto.getBomTemplateId());

        /*查询基础资料-档差数据*/
        Page<BasicsdatumMaterialPageVo> objects = PageHelper.startPage(queryBomTemplateDto);
        baseMapper.getBomTemplateMateriaList(queryWrapper);
        return objects.toPageInfo();
    }


// 自定义方法区 不替换的区域【other_end】

}
