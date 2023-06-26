/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pricing.dto.PricingDelDTO;
import com.base.sbc.module.pricing.dto.PricingTemplateDTO;
import com.base.sbc.module.pricing.dto.PricingTemplateSearchDTO;
import com.base.sbc.module.pricing.dto.PricingUpdateStatusDTO;
import com.base.sbc.module.pricing.entity.PricingTemplate;
import com.base.sbc.module.pricing.mapper.PricingTemplateMapper;
import com.base.sbc.module.pricing.service.PricingTemplateItemService;
import com.base.sbc.module.pricing.service.PricingTemplateService;
import com.base.sbc.module.pricing.vo.PricingTemplateItemVO;
import com.base.sbc.module.pricing.vo.PricingTemplateVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nfunk.jep.JEP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public class PricingTemplateBaseServiceImpl extends BaseServiceImpl<PricingTemplateMapper, PricingTemplate> implements PricingTemplateService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(PricingTemplateService.class);


    @Autowired
    private PricingTemplateItemService pricingTemplateItemService;
    @Autowired
    private PricingTemplateMapper pricingTemplateMapper;

    @Override
    public PageInfo<PricingTemplateVO> queryPageInfo(PricingTemplateSearchDTO dto, String userCompany) {
        QueryWrapper<PricingTemplate> qc = new QueryWrapper<>();
        qc.like(StringUtils.isNotEmpty(dto.getTemplateCode()), "template_code", dto.getTemplateCode());
        qc.like(StringUtils.isNotEmpty(dto.getTemplateName()), "template_name", dto.getTemplateName());
        qc.eq(StringUtils.isNotEmpty(dto.getStatus()), "status", dto.getStatus());
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

    @Override
    public List<PricingTemplateItemVO> formulaCount(String id, Map<String, Object> map, String userCompany) {
        PricingTemplateVO templateVO = this.getDetailsById(id, userCompany);
        List<PricingTemplateItemVO> pricingTemplateItems = templateVO.getPricingTemplateItems();
        if (CollectionUtils.isEmpty(pricingTemplateItems)) {
            return null;
        }
        Map<String, PricingTemplateItemVO> nameMap = pricingTemplateItems.stream()
                .peek(item -> {
                    Object o = map.get(item.getName());
                    if (Objects.nonNull(o)) {
                        item.setDefaultNum(o.toString());
                    }
                }).collect(Collectors.toMap(PricingTemplateItemVO::getName, Function.identity(), (k1, k2) -> k2));

        for (Map.Entry<String, PricingTemplateItemVO> item : nameMap.entrySet()) {
            count(item.getKey(), nameMap);
        }
        return new ArrayList<>(nameMap.values());
    }

    /**
     * 用于递归计算
     *
     * @param name    子表中自定义字段名称
     * @param nameMap key -> name value -> 子表对象
     */
    private void count(String name, Map<String, PricingTemplateItemVO> nameMap) {
        if (CollectionUtil.isEmpty(nameMap)) {
            return;
        }
        // 使用jep进行公式解析
        JEP jep = new JEP();
        PricingTemplateItemVO pri = nameMap.get(name);
        if (com.base.sbc.config.utils.StringUtils.isBlank(pri.getExpressionShow())) {
            pri.setCountResult(StringUtils.isEmpty(pri.getDefaultNum()) ? 0 : Double.parseDouble(pri.getDefaultNum()));
            return;
        }
        // 已经计算过
        if (pri.getCountResult() != null) {
            return;
        }
        // 取出公式中字段
        String[] fields = pri.getExpressionShow().split(",");
        for (String field : fields) {
            PricingTemplateItemVO pricingTemplateItemVO = nameMap.get(field);
            if (pricingTemplateItemVO == null) {
                continue;
            }
            // 如果是公式 递归解析
            if (StringUtils.isNotBlank(pricingTemplateItemVO.getExpressionShow()) && (pricingTemplateItemVO.getCountResult() == null)) {
                this.count(field, nameMap);
                // 有默认值不是公式 结果就是自己的值
            } else if (StringUtils.isNotBlank(pricingTemplateItemVO.getDefaultNum())) {
                pricingTemplateItemVO.setCountResult(Double.parseDouble(pricingTemplateItemVO.getDefaultNum()));
            } else if (pricingTemplateItemVO.getCountResult() == null) {
                pricingTemplateItemVO.setCountResult(0d);
            }
            // 设置公式中拆分字段的 计算结果值-> 不是嵌套公式 计算结果就是自己的默认值
            jep.addVariable(field, pricingTemplateItemVO.getCountResult());
        }
        // jep 计算
        jep.parseExpression(pri.getExpression());
        // 设置计算结果
        pri.setCountResult(jep.getValue());
    }


// 自定义方法区 不替换的区域【other_end】

}

