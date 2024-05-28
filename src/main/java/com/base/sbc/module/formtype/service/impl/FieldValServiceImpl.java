/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.mapper.FieldValMapper;
import com.base.sbc.module.formtype.service.FieldValService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：字段值 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.formType.service.FieldValService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-22 19:41:56
 */
@Service
public class FieldValServiceImpl extends BaseServiceImpl<FieldValMapper, FieldVal> implements FieldValService {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/


    @Override
    public List<FieldVal> list(String foreignId, String dataGroup) {
        if (StrUtil.hasBlank(foreignId, dataGroup)) {
            return new ArrayList<>();
        }
        QueryWrapper<FieldVal> fvQw = new QueryWrapper<>();
        fvQw.eq("foreign_id", foreignId);
        fvQw.eq("data_group", dataGroup);
        return list(fvQw);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean save(String foreignId, String dataGroup, List<FieldVal> fieldVals) {
        QueryWrapper<FieldVal> fvQw = new QueryWrapper<>();
        fvQw.eq("foreign_id", foreignId);
        fvQw.eq("data_group", dataGroup);
        this.remove(fvQw);
        if (CollUtil.isNotEmpty(fieldVals)) {
            for (FieldVal fieldVal : fieldVals) {
                fieldVal.setId(null);
                fieldVal.setDataGroup(dataGroup);
                fieldVal.setForeignId(foreignId);
            }
            this.saveBatch(fieldVals);
        }
        return true;
    }


/** 自定义方法区 不替换的区域【other_end】 **/

}
