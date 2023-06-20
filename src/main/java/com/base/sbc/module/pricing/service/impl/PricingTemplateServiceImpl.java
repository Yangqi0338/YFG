/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.pricing.dto.PricingDelDTO;
import com.base.sbc.module.pricing.dto.PricingTemplateDTO;
import com.base.sbc.module.pricing.dto.PricingTemplateSearchDTO;
import com.base.sbc.module.pricing.dto.PricingUpdateStatusDTO;
import com.base.sbc.module.pricing.entity.PricingTemplate;
import com.base.sbc.module.pricing.mapper.PricingTemplateMapper;
import com.base.sbc.module.pricing.service.PricingTemplateItemService;
import com.base.sbc.module.pricing.service.PricingTemplateService;
import com.base.sbc.module.pricing.vo.PricingTemplateVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 类描述：核价模板 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingTemplateService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:21:48
 */
@Slf4j
@Service
public class PricingTemplateServiceImpl extends ServicePlusImpl<PricingTemplateMapper, PricingTemplate> implements PricingTemplateService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(PricingTemplateService.class);


    @Autowired
    private PricingTemplateItemService pricingTemplateItemService;
    @Autowired
    private PricingTemplateMapper pricingTemplateMapper;

    @Override
    public PageInfo<PricingTemplateVO> queryPageInfo(PricingTemplateSearchDTO dto, String userCompany) {
        QueryWrapper<PricingTemplate> qc = new QueryWrapper<>();
        qc.like(StrUtil.isNotBlank(dto.getTemplateCode()), "template_code", dto.getTemplateCode());
        qc.like(StrUtil.isNotBlank(dto.getTemplateName()), "template_name", dto.getTemplateName());
        qc.eq("company_code", userCompany);
        qc.eq("del_flag", YesOrNoEnum.NO.getValueStr());
        qc.orderByDesc("create_date");
        PageHelper.startPage(dto);
        com.github.pagehelper.Page<PricingTemplateVO> basicLabelUseScopePage = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        super.list(qc);
        return basicLabelUseScopePage.toPageInfo();
    }

    @Override
    public PricingTemplateVO getDetailsById(String id, String userCompany) {
        LambdaQueryWrapper<PricingTemplate> queryWrapper = new QueryWrapper<PricingTemplate>().lambda()
                .eq(PricingTemplate::getId, id)
                .eq(PricingTemplate::getCompanyCode, userCompany)
                .eq(PricingTemplate::getDelFlag, YesOrNoEnum.NO.getValueStr());
        PricingTemplate pricingTemplate = super.getOne(queryWrapper);
        if (Objects.isNull(pricingTemplate)) {
            throw new BusinessException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        PricingTemplateVO pricingTemplateVO = new PricingTemplateVO();
        BeanUtils.copyProperties(pricingTemplate, pricingTemplateVO);
        pricingTemplateVO.setPricingTemplateItems(pricingTemplateItemService.getByPricingTemplateId(id, userCompany));
        return pricingTemplateVO;
    }

    @Transactional
    @Override
    public String save(PricingTemplateDTO pricingTemplateDTO, String userCompany) {
        log.info("PricingTemplateService#save 新增或保存核价模板，pricingTemplateDTO：{},userCompany:{}", JSON.toJSONString(pricingTemplateDTO), userCompany);
        String pricingTemplateId = pricingTemplateDTO.getId();
        if (StringUtils.isEmpty(pricingTemplateId)) {
            PricingTemplate pricingTemplate = new PricingTemplate();
            pricingTemplate.insertInit();
            pricingTemplate.setCompanyCode(userCompany);
            BeanUtils.copyProperties(pricingTemplateDTO, pricingTemplate);
            super.save(pricingTemplate);
            pricingTemplateId = pricingTemplate.getId();
        } else {
            PricingTemplate pricingTemplate = new PricingTemplate();
            pricingTemplate.updateInit();
            BeanUtils.copyProperties(pricingTemplateDTO, pricingTemplate);
            LambdaUpdateWrapper<PricingTemplate> wrapper = new UpdateWrapper<PricingTemplate>().lambda()
                    .eq(PricingTemplate::getId, pricingTemplateId)
                    .eq(PricingTemplate::getCompanyCode, userCompany);
            super.update(pricingTemplate, wrapper);
//            pricingTemplateItemService.delByPricingTemplateId(pricingTemplateDTO.getId(), userCompany);
        }
        pricingTemplateItemService.saveBatch(pricingTemplateDTO.getPricingTemplateItems(), pricingTemplateId, userCompany);
        return pricingTemplateId;
    }

    @Override
    public void delById(PricingDelDTO pricingDelDTO, String userCompany) {
        logger.info("PricingTemplateService#deleteByIdDelFlag 删除 pricingDelDTO:{}, userCompany:{}", JSON.toJSONString(pricingDelDTO), userCompany);
        if (CollectionUtils.isEmpty(pricingDelDTO.getIds())) {
            throw new BusinessException(BaseErrorEnum.ERR_DELETE_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        LambdaUpdateWrapper<PricingTemplate> wrapper = new LambdaUpdateWrapper<PricingTemplate>()
                .set(PricingTemplate::getDelFlag, BaseEntity.DEL_FLAG_NORMAL)
                .in(PricingTemplate::getId, pricingDelDTO.getIds())
                .eq(PricingTemplate::getCompanyCode, userCompany);
        pricingTemplateMapper.delete(wrapper);
    }

    @Transactional
    @Override
    public void defaultSetting(String id, String userCompany) {
        logger.info("PricingTemplateService#defaultSetting 默认设置 id:{}, userCompany:{}", id, userCompany);
        PricingTemplate pricingTemplate = new PricingTemplate();
        pricingTemplate.setDefaultFlag(YesOrNoEnum.NO.getValueStr());
        LambdaUpdateWrapper<PricingTemplate> wrapper = new UpdateWrapper<PricingTemplate>().lambda()
                .eq(PricingTemplate::getCompanyCode, userCompany)
                .eq(PricingTemplate::getDefaultFlag, YesOrNoEnum.YES.getValueStr())
                .eq(PricingTemplate::getDelFlag, YesOrNoEnum.NO.getValueStr());
        super.update(pricingTemplate, wrapper);

        PricingTemplate template = new PricingTemplate();
        template.setDefaultFlag(YesOrNoEnum.YES.getValueStr());
        LambdaUpdateWrapper<PricingTemplate> updateWrapper = new UpdateWrapper<PricingTemplate>().lambda()
                .in(PricingTemplate::getId, id)
                .eq(PricingTemplate::getCompanyCode, userCompany)
                .eq(PricingTemplate::getDelFlag, YesOrNoEnum.NO.getValueStr());
        super.update(template, updateWrapper);

    }

    @Override
    public void updateStatus(PricingUpdateStatusDTO dto, String userCompany) {
        logger.info("PricingTemplateService#updateStatus 更新状态 dto:{}, userCompany:{}", JSON.toJSONString(dto), userCompany);
        if (CollectionUtils.isEmpty(dto.getIds())) {
            throw new BusinessException(BaseErrorEnum.ERR_UPDATE_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        PricingTemplate pricingTemplate = new PricingTemplate();
        pricingTemplate.setStatus(dto.getStatus());

        LambdaUpdateWrapper<PricingTemplate> updateWrapper = new UpdateWrapper<PricingTemplate>().lambda()
                .in(PricingTemplate::getId, dto.getIds())
                .eq(PricingTemplate::getCompanyCode, userCompany)
                .eq(PricingTemplate::getDelFlag, YesOrNoEnum.NO.getValueStr());
        super.update(pricingTemplate, updateWrapper);
    }


// 自定义方法区 不替换的区域【other_end】

}

