/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.service.impl;

import com.alibaba.fastjson.JSON;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangtag.dto.HangTagIngredientDTO;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.mapper.HangTagIngredientMapper;
import com.base.sbc.module.hangtag.service.HangTagIngredientService;
import com.base.sbc.module.hangtag.vo.HangTagIngredientVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：吊牌成分表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.service.HangTagIngredientService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:55
 */
@Service
public class HangTagIngredientServiceImpl extends BaseServiceImpl<HangTagIngredientMapper, HangTagIngredient> implements HangTagIngredientService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(HangTagIngredientService.class);
    @Autowired
    private HangTagIngredientMapper hangTagIngredientMapper;


    @Override
    public void save(List<HangTagIngredientDTO> hangTagIngredients, String hangTagId, String userCompany) {
        if (CollectionUtils.isEmpty(hangTagIngredients)) {
            return;
        }
        logger.info("HangTagIngredientService#save 保存成分信息，hangTagIngredients:{}, hangTagId:{}, userCompany:{}",
                JSON.toJSONString(hangTagIngredients), hangTagId, userCompany);
        List<HangTagIngredient> hangTagIngredientList = hangTagIngredients.stream()
                .map(e -> {
                    HangTagIngredient hangTagIngredient = new HangTagIngredient();
                    BeanUtils.copyProperties(e, hangTagIngredient);
                    hangTagIngredient.setHangTagId(hangTagId);
                    hangTagIngredient.setCompanyCode(userCompany);
                    return hangTagIngredient;
                }).collect(Collectors.toList());
        super.saveOrUpdateBatch(hangTagIngredientList);
    }

    @Override
    public List<HangTagIngredientVO> getIngredientListByHangTagId(String hangTagId, String userCompany) {
        return hangTagIngredientMapper.getIngredientListByHangTagId(hangTagId, userCompany);
    }


// 自定义方法区 不替换的区域【other_end】

}

