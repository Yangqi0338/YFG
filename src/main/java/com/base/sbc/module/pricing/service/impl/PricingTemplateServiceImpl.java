/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.EditPermission;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pricing.dto.*;
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
public class PricingTemplateServiceImpl extends BaseServiceImpl<PricingTemplateMapper, PricingTemplate> implements PricingTemplateService {
    // 自定义方法区 不替换的区域【other_start】
    private static final Logger logger = LoggerFactory.getLogger(PricingTemplateService.class);


    @Autowired
    private PricingTemplateItemService pricingTemplateItemService;
    @Autowired
    private PricingTemplateMapper pricingTemplateMapper;

    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Override
    @EditPermission(type = DataPermissionsBusinessTypeEnum.pricingTemplate)
    public PageInfo<PricingTemplateVO> queryPageInfo(PricingTemplateSearchDTO dto, String userCompany) {
        BaseQueryWrapper<PricingTemplate> qc = new BaseQueryWrapper<>();
        qc.like(StringUtils.isNotEmpty(dto.getTemplateCode()), "template_code", dto.getTemplateCode());
        qc.like(StringUtils.isNotEmpty(dto.getTemplateName()), "template_name", dto.getTemplateName());
        qc.eq(StringUtils.isNotEmpty(dto.getStatus()), "status", dto.getStatus());
        qc.notEmptyEq("brand", dto.getBrand());
        qc.notEmptyEq("devt_type", dto.getDevtType());
        qc.eq("company_code", userCompany);
        qc.eq("del_flag", YesOrNoEnum.NO.getValueStr());
        qc.orderByDesc("create_date");
        QueryGenerator.initQueryWrapperByMap(qc,dto);

        dataPermissionsService.getDataPermissionsForQw(qc, DataPermissionsBusinessTypeEnum.pricingTemplate.getK());
        PageHelper.startPage(dto);
        com.github.pagehelper.Page<PricingTemplateVO> basicLabelUseScopePage = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        super.list(qc);
        return basicLabelUseScopePage.toPageInfo();
    }

    @Override
    public PricingTemplateVO getDetailsById(String id, String userCompany) {
        if (!this.exists(id)) throw new OtherException("无效的核价模板");
        PricingTemplate pricingTemplate = super.getById(id);
        if (Objects.isNull(pricingTemplate)) {
            throw new BusinessException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        PricingTemplateVO pricingTemplateVO = new PricingTemplateVO();
        BeanUtils.copyProperties(pricingTemplate, pricingTemplateVO);
        pricingTemplateVO.setPricingTemplateItems(pricingTemplateItemService.getByPricingTemplateId(id, userCompany));
        return pricingTemplateVO;
    }

    /**
     * 通过id查询核价模板详情
     *
     * @param id
     * @return
     */
    @Override
    public PricingTemplateVO getDetailsById(String id) {
        if (!this.exists(id)) throw new OtherException("无效的核价模板");
        PricingTemplate pricingTemplate = super.getById(id);
        if (Objects.isNull(pricingTemplate)) {
            throw new BusinessException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        PricingTemplateVO pricingTemplateVO = new PricingTemplateVO();
        BeanUtils.copyProperties(pricingTemplate, pricingTemplateVO);
        pricingTemplateVO.setPricingTemplateItems(pricingTemplateItemService.getByPricingTemplateId(id));
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
    public void delById(PricingDelDTO pricingDelDTO) {
        logger.info("PricingTemplateService#deleteByIdDelFlag 删除 pricingDelDTO:{}", JSON.toJSONString(pricingDelDTO));
        List<String> ids = pricingDelDTO.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(BaseErrorEnum.ERR_DELETE_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        pricingTemplateMapper.deleteBatchIds(ids);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void defaultSetting(String id, String userCompany) {
        logger.info("PricingTemplateService#defaultSetting 默认设置 id:{}, userCompany:{}", id, userCompany);
        /*查询模板详情*/
        PricingTemplate pricing =new PricingTemplate();
        if(ccmFeignService.getSwitchByCode("PRICING_DEVT_TYOE_DEFAULT_TEMPLATE")){
            pricing = baseMapper.selectById(id);
        }

        super.update(new LambdaUpdateWrapper<PricingTemplate>()
                .set(PricingTemplate::getDefaultFlag, YesOrNoEnum.NO.getValueStr())
                .eq(PricingTemplate::getCompanyCode, userCompany)
                .eq(PricingTemplate::getDefaultFlag, YesOrNoEnum.YES.getValueStr())
                .eq(StrUtil.isNotBlank(pricing.getBrand()),PricingTemplate::getBrand, pricing.getBrand())
                .eq(StrUtil.isNotBlank(pricing.getDevtType()),PricingTemplate::getDevtType, pricing.getDevtType()));

        super.update(new LambdaUpdateWrapper<PricingTemplate>()
                .set(PricingTemplate::getDefaultFlag, YesOrNoEnum.YES.getValueStr())
                .in(PricingTemplate::getId, id));
    }

    @Override
    public void updateStatus(PricingUpdateStatusDTO dto) {
        logger.info("PricingTemplateService#updateStatus 更新状态 dto:{}", JSON.toJSONString(dto));
        if (CollectionUtils.isEmpty(dto.getIds())) {
            throw new BusinessException(BaseErrorEnum.ERR_UPDATE_ATTRIBUTE_NOT_REQUIREMENTS);
        }

        LambdaUpdateWrapper<PricingTemplate> updateWrapper = new LambdaUpdateWrapper<PricingTemplate>()
                .set(PricingTemplate::getStatus, dto.getStatus())
                .in(PricingTemplate::getId, dto.getIds());
        super.update(updateWrapper);
    }

    @Override
    public List<PricingTemplateItemVO> formulaCount(FormulaCountDTO formulaCountDTO, String userCompany) {
        PricingTemplateVO templateVO = this.getDetailsById(formulaCountDTO.getId(), userCompany);
        List<PricingTemplateItemVO> pricingTemplateItems = templateVO.getPricingTemplateItems();
        if (CollectionUtils.isEmpty(pricingTemplateItems)) {
            return null;
        }

        Map<String, PricingTemplateItemVO> nameMap = pricingTemplateItems.stream()
                .peek(item -> {
                    Object o = formulaCountDTO.getMap().get(item.getName());
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
     * 获取默认模板
     *
     * @param brand
     * @param devtType
     * @param userCompany
     * @return
     */
    @Override
    public PricingTemplateVO getDefaultPricingTemplate(String brand, String devtType,String userCompany) {
        PricingTemplateVO pricingTemplateVO=new PricingTemplateVO();
        QueryWrapper<PricingTemplate> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("company_code", userCompany);
        queryWrapper.eq("default_flag", BaseGlobal.YES);
        queryWrapper.eq("brand", brand);
        /*是否使用生产类型判断*/
        if(ccmFeignService.getSwitchByCode("PRICING_DEVT_TYOE_DEFAULT_TEMPLATE")){
            queryWrapper.eq(StrUtil.isNotBlank(devtType) ,"devt_type",devtType);
        }
        List<PricingTemplate> templateList = baseMapper.selectList(queryWrapper);
        if(CollUtil.isEmpty(templateList)){
            throw new OtherException("无默认模板");
        }
        BeanUtils.copyProperties(templateList.get(0), pricingTemplateVO);
        return pricingTemplateVO;
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

