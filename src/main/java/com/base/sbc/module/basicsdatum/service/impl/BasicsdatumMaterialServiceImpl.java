/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IWriter;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.ureport.minio.MinioConfig;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.*;
import com.base.sbc.config.vo.ExcelTableCodeVO;
import com.base.sbc.module.basicsdatum.constant.MaterialConstant;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.enums.BasicsdatumMaterialBizTypeEnum;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialMapper;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.basicsdatum.vo.*;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.service.BasicFabricLibraryService;
import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.MaterialSupplierInfo;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.mapper.PlanningDimensionalityMapper;
import com.base.sbc.module.planning.vo.PlanningDimensionalityVo;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.service.MaterialStockService;
import com.base.sbc.module.report.dto.MaterialColumnHeadDto;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH;
import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * 类描述：基础资料-物料档案 service类
 *
 * @author shenzhixiong
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:17
 */
@Service
@RequiredArgsConstructor
public class BasicsdatumMaterialServiceImpl extends BaseServiceImpl<BasicsdatumMaterialMapper, BasicsdatumMaterial>
        implements BasicsdatumMaterialService {
    Logger log = LoggerFactory.getLogger(getClass());
    private final SpecificationService specificationService;
    private final SpecificationGroupService specificationGroupService;
    private final BasicsdatumMaterialOldService materialOldService;
    private final BasicsdatumMaterialWidthService materialWidthService;
    private final BasicsdatumMaterialColorService materialColorService;
    private final BasicsdatumMaterialPriceService materialPriceService;
    private final BasicsdatumMaterialIngredientService materialIngredientService;
    private final BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;
    private final EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;
    private final FlowableService flowableService;
    @Lazy
    private final PackBomService packBomService;


    @Autowired
    private BasicFabricLibraryService basicFabricLibraryService;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private MinioConfig minioConfig;
    /**
     * 解决循环依赖报错的问题
     */
    @Lazy
    @Resource
    private SmpService smpService;
    @Autowired
    private CcmService ccmService;
    @Autowired
    CcmFeignService ccmFeignService;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Resource
    private BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;

    @Resource
    private BasicsdatumMaterialMapper basicsdatumMaterialMapper;

    @Resource
    private MaterialStockService materialStockService;

    @Resource
    private StylePicUtils stylePicUtils;

    @Autowired
    private FieldValService fieldValService;

    @Autowired
    private PlanningDimensionalityMapper planningDimensionalityMapper;

    @ApiOperation(value = "主物料成分转换")
    @GetMapping("/formatIngredient")
    public List<BasicsdatumMaterialIngredient> formatIngredient(
            @RequestParam(value = "value", required = false) String value) {
        String str = IngredientUtils.format(value);
        return formatToList(str);
    }

    /**
     * 转换为对象集合
     *
     * @param str
     * @return
     */
    private List<BasicsdatumMaterialIngredient> formatToList(String str) {
        String[] strs = str.split(",");
        List<BasicsdatumMaterialIngredient> list = new ArrayList<>();
        for (String ingredients : strs) {
            BasicsdatumMaterialIngredient in = new BasicsdatumMaterialIngredient();
            in.setRatio(BigDecimalUtil.valueOf(ingredients.split("%")[0]));
            String nameSay = ingredients.split("%")[1];
            int kidx = nameSay.indexOf("(");
            String name = nameSay;
            String say = "";
            if (kidx != -1) {
                name = nameSay.substring(0, kidx);
                say = nameSay.substring(kidx + 1, nameSay.length() - 1);
            }
            in.setName(name);
            in.setSay(say);
            list.add(in);
        }
        return list;
    }

    @Override
    public PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialList(BasicsdatumMaterialQueryDto dto) {
        if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
            PageHelper.startPage(dto);
        }
        BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
        qc.eq("tbm.company_code", this.getCompanyCode());
        qc.andLike(dto.getSearch(), "tbm.material_code", "tbm.material_name");
        qc.notEmptyEq("tbm.status", dto.getStatus());
        qc.notEmptyLike("tbm.material_code_name", dto.getMaterialCodeName());
        qc.notEmptyLike("tbm.supplier_fabric_code", dto.getSupplierFabricCode());
        qc.notEmptyLike("tbm.supplier_name", dto.getSupplierName());
        qc.notEmptyLike("tbm.material_code", dto.getMaterialCode());
        qc.notEmptyLike("tbm.material_name", dto.getMaterialName());
        qc.notEmptyLike("tbmc.color_name", dto.getColorName());
        qc.notEmptyLike("tbmc.color_code", dto.getColorCode());
        qc.notEmptyLike("tbmc.supplier_color_code", dto.getSupplierColorCode());
        if (StringUtils.isNotEmpty(dto.getCategoryId())) {
            qc.and(Wrapper -> Wrapper.eq("tbm.category_id", dto.getCategoryId()).or()
                    .eq("tbm.category1_code ", dto.getCategoryId()).or().eq("tbm.category2_code", dto.getCategoryId()).or()
                    .eq("tbm.category3_code", dto.getCategoryId()));
        }
        qc.eq("tbm.biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());
        if (StringUtils.isNotEmpty(dto.getConfirmStatus())) {
            List<String> confirmStatus = Arrays.stream(dto.getConfirmStatus().split(",")).collect(Collectors.toList());
            qc.in("tbm.confirm_status", confirmStatus);
        } else {
            qc.eq("tbm.confirm_status", "2");
        }
        qc.notEmptyIn("brand",dto.getBrandList());
        qc.orderByDesc("tbm.create_date");
        qc.eq("tbm.del_flag", "0");
        dataPermissionsService.getDataPermissionsForQw(qc, DataPermissionsBusinessTypeEnum.material.getK());
        List<BasicsdatumMaterialPageVo> list;
        if ("1".equals(dto.getMergeMaterialColor())){
            list = baseMapper.listMaterialPage(qc);
        }else{
            list = baseMapper.listSku(qc);
        }

        // PageInfo<BasicsdatumMaterialPageVo> copy = CopyUtil.copy(new PageInfo<>(list), BasicsdatumMaterialPageVo.class);
        List<String> stringList = IdGen.getIds(list.size());
        int index = 0;
        for (BasicsdatumMaterialPageVo basicsdatumMaterialPageVo : list) {
            basicsdatumMaterialPageVo.setIds(stringList.get(index));
            index++;
            String materialCode = basicsdatumMaterialPageVo.getMaterialCode();
            /*查询物料的最新检测报告*/
           List<EscmMaterialCompnentInspectCompanyDto>  escmMaterialCompnentInspectCompanyDto = escmMaterialCompnentInspectCompanyService.getListByMaterialsNo(new QueryWrapper<EscmMaterialCompnentInspectCompanyDto>().eq("materials_no", materialCode), new ArrayList<>());
            List<BasicsdatumMaterialWidth> basicsdatumMaterialWidths = materialWidthService.list(new QueryWrapper<BasicsdatumMaterialWidth>().eq("material_code", materialCode));
            List<String> collect = basicsdatumMaterialWidths.stream().map(BasicsdatumMaterialWidth::getName).collect(Collectors.toList());
            basicsdatumMaterialPageVo.setWidthName(String.join(",", collect));

            if ( CollUtil.isNotEmpty(escmMaterialCompnentInspectCompanyDto)) {


                basicsdatumMaterialPageVo.setFabricEvaluation(escmMaterialCompnentInspectCompanyDto.get(0).getRemark());
                basicsdatumMaterialPageVo.setCheckCompanyName(escmMaterialCompnentInspectCompanyDto.get(0).getCompanyFullName());
                basicsdatumMaterialPageVo.setCheckDate(escmMaterialCompnentInspectCompanyDto.get(0).getArriveDate());
                basicsdatumMaterialPageVo
                        .setCheckValidDate(Integer.valueOf(escmMaterialCompnentInspectCompanyDto.get(0).getValidityTime()));
                basicsdatumMaterialPageVo.setCheckItems(escmMaterialCompnentInspectCompanyDto.get(0).getSendInspectContent());
                basicsdatumMaterialPageVo.setCheckOrderUserName(escmMaterialCompnentInspectCompanyDto.get(0).getMakerByName());
                basicsdatumMaterialPageVo.setCheckFileUrl(escmMaterialCompnentInspectCompanyDto.get(0).getFileUrl());
            }
            if ("1".equals(dto.getMergeMaterialColor())){
                // 补充合并颜色信息
                fullMaterialColor(basicsdatumMaterialPageVo);
            }

        }
        minioUtils.setObjectUrlToList(list, "imageUrl");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialNewList(MaterialColumnHeadDto dto) {

        BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
        //qc.eq("tbm.company_code", this.getCompanyCode());
        qc.notEmptyLike("tbm.material_code_name", dto.getMaterialCodeName());
        //增加多物料编号查询
        if (StrUtil.isNotEmpty(dto.getMaterialCodesHead())) {
            if (dto.getMaterialCodesHead().contains(",")) {
                qc.in("tbm.material_code", StringUtils.convertList(dto.getMaterialCodesHead()));
            }else{
                qc.like("tbm.material_code",dto.getMaterialCodesHead());
            }
        }
        qc.notEmptyLike("tbm.supplier_fabric_code", dto.getSupplierMaterialCode());
        qc.orderByDesc("tbm.create_date");
        qc.eq("tbm.del_flag", "0");

        if (StringUtils.isNotEmpty(dto.getCategoryId())) {
            qc.and(Wrapper -> Wrapper.eq("tbm.category_id", dto.getCategoryId()).or()
                    .eq("tbm.category1_code ", dto.getCategoryId()).or().eq("tbm.category2_code", dto.getCategoryId()).or()
                    .eq("tbm.category3_code", dto.getCategoryId()));
        }



        dataPermissionsService.getDataPermissionsForQw(qc, DataPermissionsBusinessTypeEnum.material.getK());

        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qc, dto);
        PageHelper.startPage(dto);
        List<BasicsdatumMaterialPageVo> list = baseMapper.getMaterialSkuList(qc);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }

        List<FieldManagementVo> fieldManagementVos = queryCoefficient(list.get(0));
        list.get(0).setFieldValList(fieldManagementVos);

        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        minioUtils.setObjectUrlToList(list, "imageUrl");
        return new PageInfo<>(list);
    }

    @Override
    public List<FieldManagementVo> queryCoefficient(BasicsdatumMaterialPageVo pageVo) {
        //查询动态字段
        BaseQueryWrapper<PlanningDimensionality> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.in("tpd.prod_category1st", Arrays.asList(pageVo.getCategory1Code(), pageVo.getCategory2Code(), pageVo.getCategory3Code()));
        queryWrapper.eq("tpd.coefficient_flag", BaseGlobal.YES);
        queryWrapper.eq("tpd.del_flag", BaseGlobal.NO);
        queryWrapper.orderByAsc("tpd.group_sort", "tpd.sort");
        List<PlanningDimensionalityVo> coefficientList = planningDimensionalityMapper.getMaterialCoefficient(queryWrapper);
        Map<String, List<PlanningDimensionalityVo>> collect = coefficientList.stream().collect(Collectors.groupingBy(PlanningDimensionality::getProdCategory1st));

        List<PlanningDimensionalityVo> planningDimensionalityVos = new ArrayList<>();
        if (collect.containsKey(pageVo.getCategory3Code())) {
            planningDimensionalityVos = collect.get(pageVo.getCategory3Code());
        } else if (collect.containsKey(pageVo.getCategory2Code())) {
            planningDimensionalityVos = collect.get(pageVo.getCategory2Code());
        } else if (collect.containsKey(pageVo.getCategory1Code())) {
            planningDimensionalityVos = collect.get(pageVo.getCategory1Code());
        }
        List<FieldManagementVo> fieldManagementVos = BeanUtil.copyToList(planningDimensionalityVos, FieldManagementVo.class);

        List<FieldVal> fvList = fieldValService.list(pageVo.getId(), FieldValDataGroupConstant.MATERIAL);

        if (CollUtil.isNotEmpty(fieldManagementVos)) {
            Map<String, FieldVal> valMap = Optional.ofNullable(fvList).orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FieldVal::getFieldName, v -> v, (a, b) -> b));
            for (FieldManagementVo vo : fieldManagementVos) {
                vo.setFieldId(vo.getId());
                vo.setId(Optional.ofNullable(valMap.get(vo.getFieldName())).map(FieldVal::getId).orElse(null));
                vo.setVal(Optional.ofNullable(valMap.get(vo.getFieldName())).map(FieldVal::getVal).orElse(null));
                vo.setValName(Optional.ofNullable(valMap.get(vo.getFieldName())).map(FieldVal::getValName).orElse(null));
                vo.setSelected(valMap.containsKey(vo.getFieldName()));
            }

        }

        return fieldManagementVos;
    }

    @Transactional
    @Override
    public BasicsdatumMaterialVo saveBasicsdatumMaterial(BasicsdatumMaterialSaveDto dto) {
         CommonUtils.removeQuery(dto, "imageUrl");
        CommonUtils.removeQuerySplit(dto, ",", "attachment");
        BasicsdatumMaterial entity = CopyUtil.copy(dto, BasicsdatumMaterial.class);
        if (StrUtil.isEmptyIfStr(entity.getId())) {
            entity.setStatus("0");
            String categoryCode = entity.getCategoryId();
            // 获取并放入最大code(且需要满足自动生成物料编码的开关为空或者未启动)
            // && !ccmFeignService.getSwitchByCode("AUTO_GEN_MATERIAL_CODE")
            if (BasicsdatumMaterialBizTypeEnum.MATERIAL.getK().equals(dto.getBizType())) {
                //新的生成逻辑
                //region 新的物料编号生成逻辑
                entity.setMaterialCode(generateMaterialCode(categoryCode));
                //endregion

                //entity.setMaterialCode(getMaxCode(categoryCode));
            }
        }

        if (StrUtil.isNotEmpty(entity.getMaterialName())) {
            entity.setMaterialCodeName(entity.getMaterialCode() + "_" + entity.getMaterialName());
        }else{
            entity.setMaterialCodeName(entity.getMaterialCode());
        }

        // 特殊逻辑： 如果是面料的时候，需要增加门幅幅宽的数据 给到物料规格
        // if ("fabric".equals(entity.getMaterialType())) {
        // this.saveFabricWidth(entity.getMaterialCode(),
        // BigDecimalUtil.convertString(entity.getTranslate()));
        // }
        // 如果成分不为空,则清理替换成分信息
        this.saveIngredient(dto);

        /*if (entity.getId() != null && "1".equals(entity.getDistribute())) {
            smpService.materials(entity.getId().split(","));
        }*/
        // 保存主信息
        this.saveOrUpdate(entity, "物料档案", entity.getMaterialCodeName(), entity.getMaterialCode());

        //保存动态字段
        fieldValService.save(entity.getId(),FieldValDataGroupConstant.MATERIAL,dto.getFieldValList());

        return getBasicsdatumMaterial(entity.getId());
    }


    /**
     * 生成物料编码
     * @param categoryCode
     */
    private String generateMaterialCode(String categoryCode) {
        String categoryMaxCode = this.getBaseMapper().getCategoryMaxCode(categoryCode);
        if (StrUtil.isEmpty(categoryMaxCode)) {
            categoryMaxCode = "00001";
        }
        return categoryCode + categoryMaxCode;
    }

    /**
     * 保存成分数据
     *
     * @param dto
     */
    private void saveIngredient(BasicsdatumMaterialSaveDto dto) {
        UserCompany userCompany = companyUserInfo.get();
        String userName = userCompany.getAliasUserName();
        String userId = userCompany.getUserId();
        if (dto.getIngredientList() != null) {
            materialIngredientService.remove(new QueryWrapper<BasicsdatumMaterialIngredient>()
                    .eq(COMPANY_CODE, getCompanyCode()).eq("material_code", dto.getMaterialCode()).eq("type", "0"));
            for (BasicsdatumMaterialIngredient item : dto.getIngredientList()) {
                item.setCompanyCode(this.getCompanyCode());
                item.setCreateId(userId);
                item.setCreateName(userName);

            }
            materialIngredientService.saveBatch(dto.getIngredientList());
        }
        if (dto.getFactoryCompositionList() != null) {
            materialIngredientService.remove(new QueryWrapper<BasicsdatumMaterialIngredient>()
                    .eq(COMPANY_CODE, getCompanyCode()).eq("material_code", dto.getMaterialCode()).eq("type", "1"));
            for (BasicsdatumMaterialIngredient item : dto.getFactoryCompositionList()) {
                item.setCompanyCode(this.getCompanyCode());
                item.setCreateId(userId);
                item.setCreateName(userName);
            }
            materialIngredientService.saveBatch(dto.getFactoryCompositionList());
        }

    }

    /**
     * 如果是面料的时候，需要增加门幅幅宽的数据 给到物料规格并保持一个规格
     *
     * @param materialCode
     * @param widthCode
     */
    private void saveFabricWidth(String materialCode, String widthCode) {
        List<BasicsdatumMaterialWidth> list = this.materialWidthService
                .list(new QueryWrapper<BasicsdatumMaterialWidth>().eq("company_code", this.getCompanyCode())
                        .eq("material_code", materialCode));
        if (list != null && list.size() > 0) {
            // 如果存在多个，第一个如果不同则修改
            BasicsdatumMaterialWidth width = list.get(0);
            if (!width.getWidthCode().equals(widthCode)) {
                width.setWidthCode(widthCode);
                this.materialWidthService.updateById(width);
            }
            // 如果还有其他的进行移除
            if (list.size() > 1) {
                List<String> ids = new ArrayList<>();
                for (int i = 1; i < list.size(); i++) {
                    ids.add(list.get(i).getId());
                }
                this.materialWidthService.removeBatchByIds(ids);
            }

        } else {
            BasicsdatumMaterialWidth one = new BasicsdatumMaterialWidth();
            one.setCompanyCode(this.getCompanyCode());
            one.setWidthCode(widthCode);
            one.setName(widthCode);
            one.setMaterialCode(materialCode);
            this.materialWidthService.save(one);
        }
    }

    /**
     * 生成物料编码
     *
     * @param categoryCode 物料前缀
     * @return 物料编码
     */
    private String getMaxCode(String categoryCode) {
        // 物料序号
        int num;
        // 生成的物料编码
        String materialCode;
        // 组成物料编码缓存KEY
        String materialCodeKey = MaterialConstant.MATERIAL_CODE_KEY + categoryCode + this.getCompanyCode();
        if (redisUtils.hasKey(materialCodeKey)) {
            // 获取物料编码数值
            String categoryCodeKey = redisUtils.get(materialCodeKey).toString();
            num = Integer.parseInt(categoryCodeKey) + BaseGlobal.ONE;
            // 重新设置编码数值
            redisUtils.set(materialCodeKey, num);
            return ProducerNumUtil.getPrefixNum(categoryCode, num);
        }
        // redis未缓存数据，中数据库查询生最大数值
        BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
        qc.select("SUBSTRING(material_code,-5) AS material_code");
        qc.eq("company_code", this.getCompanyCode());
        qc.eq("biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());
        qc.and(wq -> {
            wq.eq("del_flag", BaseGlobal.ZERO).or().eq("del_flag", BaseGlobal.ONE);
        });
        qc.eq(" length(material_code)", categoryCode.length() + 5);
        qc.likeRight("material_code", categoryCode);
        qc.notLike("material_code", BaseGlobal.H);
        qc.last("AND TRIM(SUBSTRING(material_code,-5)) REGEXP '[^0-9]' = 0 " +
                "ORDER BY SUBSTRING(material_code,-5) DESC  limit 1 ");
        BasicsdatumMaterial one = this.baseMapper.selectOne(qc);
        if (one != null && null != one.getMaterialCode()) {
            num = Integer.parseInt(one.getMaterialCode());
            materialCode = ProducerNumUtil.getPrefixNum(categoryCode, ++num);
        } else {
            num = BaseGlobal.ONE;
            materialCode = ProducerNumUtil.getPrefixNum(categoryCode, BaseGlobal.ONE);
        }
        redisUtils.set(materialCodeKey, num);
        return materialCode;
    }

    @Override
    public Boolean startStopBasicsdatumMaterial(StartStopDto dto) {
        if (StringUtils.isEmpty(dto.getIds())) {
            throw new OtherException("请选择要启用/停用的数据");
        }
        UpdateWrapper<BasicsdatumMaterial> uw = new UpdateWrapper<>();
        uw.in("id", StringUtils.convertList(dto.getIds()));
        uw.set("status", dto.getStatus());

        for (BasicsdatumMaterial basicsdatumMaterial : this.listByIds(Arrays.asList(dto.getIds().split(",")))) {
            if ("1".equals(dto.getStatus())) {
                // smpService.materials(basicsdatumMaterial.getId().split(","));
                boolean b = smpService.checkSizeAndColor(basicsdatumMaterial.getMaterialCode(), "0", null);
                if (!b) {
                    throw new OtherException("物料编码为" + basicsdatumMaterial.getMaterialCode() + "的物料下有未审核的规格或颜色，无法t停用");
                }
            }
        }

        this.startStopLog(dto);
        return this.update(null, uw);
    }

    @Override
    @Transactional
    public Boolean delBasicsdatumMaterial(RemoveDto removeDto) {
        List<String> materialIds = StringUtils.convertList(removeDto.getIds());
        BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
        qc.in("id", materialIds);
        qc.eq("del_flag","0");
        qc.select("material_code");
        List<BasicsdatumMaterial> materialList = this.list(qc);
        if (CollUtil.isEmpty(materialList)) {
            throw new OtherException("数据库找不到该物料信息，无法做删除操作！");
        }

        List<String> materialsCodes = this.list(qc).stream().map(BasicsdatumMaterial::getMaterialCode)
                .collect(Collectors.toList());

        /*控制是否下发外部SMP系统开关*/
        Boolean systemSwitch = ccmFeignService.getSwitchByCode(ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH.getKeyCode());
        if (systemSwitch) {
            qc.in(systemSwitch, "distribute", StringUtils.convertList("1,3"));
            List<BasicsdatumMaterial> checkMaterials = this.list(qc);
            if (CollUtil.isNotEmpty(checkMaterials) && systemSwitch) {
                throw new OtherException("存在已下发数据无法删除!");
            }
        }



        if (CollUtil.isNotEmpty(materialsCodes)) {
            BaseQueryWrapper<BasicsdatumMaterialPageAndStyleDto> checkBomQc = new BaseQueryWrapper<>();
            checkBomQc.notEmptyIn("t.materialsCode", materialsCodes);
            List<BasicsdatumMaterialPageAndStyleVo> list = this.getBaseMapper().getBasicsdatumMaterialAndStyleList(checkBomQc);
            if (CollUtil.isNotEmpty(list)) {
                throw new OtherException("该物料规格已被BOM使用，无法修改。详情查看物料报表!");
            }
        }

        // 删除主表
        this.removeByIds(removeDto);

        // 删除子表颜色和规格及报价
        this.materialWidthService.remove(new QueryWrapper<BasicsdatumMaterialWidth>()
                .eq("company_code", this.getCompanyCode()).in("material_code", materialsCodes));
        this.materialColorService.remove(new QueryWrapper<BasicsdatumMaterialColor>()
                .eq("company_code", this.getCompanyCode()).in("material_code", materialsCodes));
        this.materialPriceService.remove(new QueryWrapper<BasicsdatumMaterialPrice>()
                .eq("company_code", this.getCompanyCode()).in("material_code", materialsCodes));
        return true;
    }

    @Override
    public void exportBasicsdatumMaterial(HttpServletResponse response, BasicsdatumMaterialQueryDto dto)
            throws IOException {
        /*BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
        qc.eq("company_code", this.getCompanyCode());
        qc.notEmptyEq("status", dto.getStatus());
        qc.notEmptyLike("material_code_name", dto.getMaterialCodeName());
        qc.notEmptyLike("supplier_name", dto.getSupplierName());
        qc.notEmptyLike("material_code", dto.getMaterialCode());
        qc.notEmptyLike("material_name", dto.getMaterialName());
        if (StringUtils.isNotEmpty(dto.getCategoryId())) {
            qc.and(Wrapper -> Wrapper.eq("category_id", dto.getCategoryId()).or().like("category_ids ",
                    dto.getCategoryId()));
        }
        List<BasicsdatumMaterial> list = this.list(qc);*/
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<BasicsdatumMaterialPageVo> list = getBasicsdatumMaterialList(dto).getList();
        List<BasicsdatumMaterialExcelVo> list1 = CopyUtil.copy(list, BasicsdatumMaterialExcelVo.class);
        ExcelUtils.exportExcel(list1, BasicsdatumMaterialExcelVo.class, "物料档案.xls", new ExportParams(), response);
    }

    @Override
    public void exportBasicsdatumNewMaterial(HttpServletResponse response, MaterialColumnHeadDto dto) throws IOException {
//        dto.setPageNum(0);
//        dto.setPageSize(0);
//        List<BasicsdatumMaterialPageVo> list = getBasicsdatumMaterialNewList(dto).getList();
//        List<BasicsdatumMaterialExcelVo> list1 = CopyUtil.copy(list, BasicsdatumMaterialExcelVo.class);
//        ExcelUtils.exportExcel(list1, BasicsdatumMaterialExcelVo.class, "物料档案.xls", new ExportParams(), response);
//        ExcelUtils.exportExcelByTableCode(list, "物料档案", response, dto);

        int pageNum = 0;
        int pageSize = 10000;
        ExcelTableCodeVO excelTableCodeVO = ExcelUtils.exportExcelByTableCodeVo(dto);
        IWriter<Workbook> workbookIWriter = ExcelExportUtil.exportBigExcel(new ExportParams("物料档案", "物料档案", ExcelType.HSSF), excelTableCodeVO.getExcelParams());
        Workbook workbook = null;
        for (int i = 0; i < 5; i++) {
            dto.setPageNum(pageNum);
            List<BasicsdatumMaterialPageVo> list = getBasicsdatumMaterialNewList(dto).getList();
            if (CollUtil.isEmpty(list)){
                if (null == workbook){
                    return;
                }
                ExcelUtils.downLoadExcel("物料档案", response, workbook);
                return;
            }
            int size = list.size();
            JSONArray jsonArray = ExcelUtils.exportExcelByTableCodeList(list, excelTableCodeVO, dto);
            workbookIWriter.write(jsonArray);
            workbook = workbookIWriter.get();
            workbookIWriter.close();
            jsonArray.clear();
            list.clear();
            if (size < pageSize){
                ExcelUtils.downLoadExcel("物料档案", response, workbook);
                return;
            }
            pageNum++;
        }

    }

    @Override
    public void exportBasicsdatumMaterialAndStyle(HttpServletResponse response, BasicsdatumMaterialPageAndStyleDto dto){
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<BasicsdatumMaterialPageAndStyleVo> list = materialsBomStylePage(dto).getList();
        List<BasicsdatumMaterialStyleExcel> excelVoList = CopyUtil.copy(list, BasicsdatumMaterialStyleExcel.class);
        ExcelUtils.executorExportExcel(excelVoList, BasicsdatumMaterialStyleExcel.class,"物料BOM档案",dto.getImgFlag(),2000,response,"styleImageUrl","materialsImageUrl");
    }

    @Override
    public PageInfo<BomSelMaterialVo> getBomSelMaterialList(BasicsdatumMaterialQueryDto dto) {
        BaseQueryWrapper<BasicsdatumMaterial> qw = new BaseQueryWrapper<>();
        qw.andLike(dto.getSearch(), "bm.material_code", "bm.material_name", "bm.supplier_fabric_code");
        qw.eq("bm.company_code", this.getCompanyCode());
        qw.notEmptyLike("bm.material_code_name", dto.getMaterialCodeName());
        qw.notEmptyLike("bm.material_code", dto.getMaterialCode());
        qw.eq(StringUtils.isNotEmpty(dto.getMaterialCodeNoLike()),"bm.material_code", dto.getMaterialCodeNoLike());
        qw.notEmptyLike("bm.material_name", dto.getMaterialName());
        qw.notEmptyIn("bm.status", dto.getStatus());
        qw.notEmptyLike("bm.supplier_fabric_code", dto.getSupplierMaterialCode());
        qw.eq("bm.del_flag", BaseGlobal.NO);
        qw.andLike(dto.getCategoryId(), "bm.category1_code", "bm.category2_code", "bm.category3_code");
        qw.eq("bm.biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());
        qw.eq("bm.confirm_status", "2");
        qw.eq(StringUtils.isNotEmpty(dto.getStatus()), "bm.status", dto.getStatus());
        qw.in(StringUtils.isNotEmpty(dto.getDistribute()), "bm.distribute", StringUtils.convertList(dto.getDistribute()));
        qw.eq(StringUtils.equals("2", dto.getSource()), "fp.planning_season_id", dto.getPlanningSeasonId());
        Page<BomSelMaterialVo> page = PageHelper.startPage(dto);
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.material.getK(), "bm.");
        List<BomSelMaterialVo> list = getBaseMapper().getBomSelMaterialList(qw, dto.getSource());
        if (CollUtil.isNotEmpty(list)) {
            //查询默认供应商
            List<String> materialCodeList = list.stream().map(BomSelMaterialVo::getMaterialCode).filter(StrUtil::isNotBlank).collect(Collectors.toList());

            BaseQueryWrapper queryWrapper = new BaseQueryWrapper();
            queryWrapper.in("material_code", materialCodeList);
            /*查物料中的规格*/
            List<BasicsdatumMaterialWidth> basicsdatumMaterialWidthList = basicsdatumMaterialWidthService.list(queryWrapper);
            /*查物料中的颜色*/
            queryWrapper.clear();
            queryWrapper.in("t.material_code", materialCodeList);
            List<BasicsdatumMaterialColorPageVo> basicsdatumMaterialColorList = basicsdatumMaterialMapper.getBasicsdatumMaterialColorListQw(queryWrapper);
            Map<String, List<BasicsdatumMaterialWidth>> widthMap = basicsdatumMaterialWidthList.stream().collect(Collectors.groupingBy(BasicsdatumMaterialWidth::getMaterialCode));
            Map<String, List<BasicsdatumMaterialColorPageVo>> colorMap = basicsdatumMaterialColorList.stream().collect(Collectors.groupingBy(BasicsdatumMaterialColorPageVo::getMaterialCode));
            /*获取默认供应商信息*/
            List<BomSelMaterialVo> priceList = materialPriceService.findDefaultToBomSel(materialCodeList);
            Map<String, BomSelMaterialVo> priceMap = Opt.ofEmptyAble(priceList)
                    .map(item -> item.stream().collect(Collectors.toMap(BomSelMaterialVo::getMaterialCode, v -> v, (a, b) -> a)))
                    .orElse(MapUtil.empty());

            /* 获取库存 */
            Map<String, MaterialStock> materialStockMap = new HashMap<>();
            if (StringUtils.equals("1", dto.getIsStock())) {
                BaseQueryWrapper msQw = new BaseQueryWrapper();
                msQw.in("material_code", materialCodeList);
                List<MaterialStock> materialStockList = materialStockService.list(msQw);
                for (MaterialStock materialStock : materialStockList) {
                    MaterialStock oldMaterialStock = materialStockMap.get(materialStock.getMaterialCode());
                    if (oldMaterialStock == null) {
                        materialStockMap.put(materialStock.getMaterialCode(), materialStock);
                    } else {
                        oldMaterialStock.setStockQuantity(BigDecimalUtil.add(oldMaterialStock.getStockQuantity(), materialStock.getStockQuantity()));
                        materialStockMap.put(materialStock.getMaterialCode(), oldMaterialStock);
                    }
                }
            }
            list.forEach(i -> {
                BomSelMaterialVo priceInfo = priceMap.get(i.getMaterialCode());
                BeanUtil.copyProperties(priceInfo, i, CopyOptions.create().ignoreNullValue());
                List<BasicsdatumMaterialWidth> widthList = widthMap.get(i.getMaterialCode());
                if (CollUtil.isNotEmpty(widthList) && widthList.size() == BaseGlobal.ONE) {
                    i.setTranslateCode(widthList.get(0).getWidthCode());
                    i.setTranslate(widthList.get(0).getName());
                }
                List<BasicsdatumMaterialColorPageVo> colorList = colorMap.get(i.getMaterialCode());
                if (CollUtil.isNotEmpty(colorList) && colorList.size() == BaseGlobal.ONE) {
                    i.setColor(colorList.get(0).getColorName());
                    i.setColorCode(colorList.get(0).getColorCode());
                    i.setColorHex(colorList.get(0).getColorHex());
                }
                MaterialStock materialStock = materialStockMap.get(i.getMaterialCode());
                if (materialStock != null) {
                    i.setStockQuantity(materialStock.getStockQuantity());
                }
                i.setId(IdUtil.randomUUID());
            });
        }
        minioUtils.setObjectUrlToList(page.getResult(), "imageUrl");
        return page.toPageInfo();
    }

    /**
     * 查询详情
     */
    @Override
    public BasicsdatumMaterialVo getBasicsdatumMaterial(String id) {
        BasicsdatumMaterial material = this.getById(id);
        List<BasicsdatumMaterialColorSelectVo> select = this.baseMapper
                .getBasicsdatumMaterialColorSelect(this.getCompanyCode(), material.getMaterialCode());
        List<BasicsdatumMaterialWidthSelectVo> select2 = this.baseMapper
                .getBasicsdatumMaterialWidthSelect(this.getCompanyCode(), material.getMaterialCode());
        BasicsdatumMaterialVo copy = CopyUtil.copy(material, BasicsdatumMaterialVo.class);
        // 填充规格规格组
        if (select2 != null && select2.size() > 0) {
            StringBuffer width = new StringBuffer();
            StringBuffer widthName = new StringBuffer();
            select2.forEach(item -> {
                BasicsdatumMaterialWidthSelectVo s = (item);
                width.append(",").append(s.getCode());
                widthName.append(",").append(s.getName());
            });
            copy.setWidth(width.substring(1));
            copy.setWidthName(widthName.substring(1));
        }
        copy.setColorList(select);
        copy.setWidthList(select2);
        // 获取成分子表的数据
        List<BasicsdatumMaterialIngredient> list = materialIngredientService
                .list(new QueryWrapper<BasicsdatumMaterialIngredient>().eq("company_code", this.getCompanyCode())
                        .eq("material_code", material.getMaterialCode()));
        if (list != null && list.size() > 0) {
            copy.setIngredientList(
                    list.stream().filter(item -> "0".equals(item.getType())).collect(Collectors.toList()));
            copy.setFactoryCompositionList(
                    list.stream().filter(item -> "1".equals(item.getType())).collect(Collectors.toList()));
        }
        minioUtils.setObjectUrlToObject(copy, "imageUrl");

        List<FieldManagementVo> fieldManagementVos = queryCoefficient(BeanUtil.copyProperties(copy,BasicsdatumMaterialPageVo.class));
        copy.setFieldValList(fieldManagementVos);
        return copy;
    }

    @Override
    public PageInfo<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(
            BasicsdatumMaterialWidthQueryDto dto) {
        if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
            PageHelper.startPage(dto);
        }
        List<BasicsdatumMaterialWidthPageVo> list = this.baseMapper
                .getBasicsdatumMaterialWidthList(this.getCompanyCode(), dto.getMaterialCode(), dto.getStatus());
        return new PageInfo<>(list);
    }

    @Override
    public Boolean saveBasicsdatumMaterialWidth(BasicsdatumMaterialWidthSaveDto dto) {
        long count = this.materialWidthService.count(new QueryWrapper<BasicsdatumMaterialWidth>().ne("id", dto.getId())
                .eq("company_code", this.getCompanyCode()).eq("Material_Code", dto.getMaterialCode())
                .and(qw -> qw.eq("Width_Code", dto.getWidthCode()).or().eq("name", dto.getName())));
        if (count > 0) {
            throw new OtherException("当前规格已存在!");
        }

        BasicsdatumMaterialWidth entity = CopyUtil.copy(dto, BasicsdatumMaterialWidth.class);
        BasicsdatumMaterialWidth oldEntity = null;
        String type = null;
        if ("-1".equals(entity.getId())) {
            type = "新增";
            entity.setId(null);
            //生成规格编码,先去查询最大的规格编码
            String code = dto.getMaterialCode() + "SP";
            BasicsdatumMaterialWidth one = materialWidthService.getOne(new QueryWrapper<BasicsdatumMaterialWidth>().like("width_code", code).orderByDesc("create_date").last("limit 1"));
            if (one != null) {
                String widthCode = one.getWidthCode();
                String substring = widthCode.substring(widthCode.length() - 3);
                int i = Integer.parseInt(substring);
                i++;
                String formatted = String.format("%03d", i);
                entity.setWidthCode(code + formatted);

            } else {
                entity.setWidthCode(code + "001");

            }
        } else {

            checkMaterialWidth(dto.getMaterialCode(), dto.getWidthCode(), dto.getName(), dto.getOldWidthCode());

            type = "修改";
            oldEntity = materialWidthService.getById(entity.getId());
        }

        boolean b = this.materialWidthService.saveOrUpdate(entity);
        if (b) {
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setName("物料档案-门幅/规格");
            operaLogEntity.setType(type);
            operaLogEntity.setDocumentId(entity.getId());
            operaLogEntity.setDocumentCode(entity.getWidthCode());
            operaLogEntity.setDocumentName(entity.getName());
            operaLogEntity.setParentId(dto.getParentId());

            materialWidthService.saveOrUpdateOperaLog(entity, oldEntity, operaLogEntity);
        }
        return b;
    }
    /**
     * 检查物料规格是否被bom引用，引用了不允许修改，删除
     * @param materialCode 物料编码
     * @param widthCode  物料规格编码
     * @param widthName 物料规格名称
     */
    private void checkMaterialWidth(String materialCode,String widthCode,String widthName,String oldWidthCode) {
        if (StrUtil.isNotEmpty(materialCode)) {
            BaseQueryWrapper<PackBom> qcMaterialQuery = new BaseQueryWrapper<>();
            qcMaterialQuery.eq("material_code", materialCode);
            qcMaterialQuery.eq("translate_code", StrUtil.isNotEmpty(oldWidthCode) ? oldWidthCode : widthCode);
            qcMaterialQuery.eq("del_flag","0");
            List<PackBom> list = packBomService.list(qcMaterialQuery);
            if (CollUtil.isNotEmpty(list)) {
                throw new OtherException("该物料【"+materialCode+"】,物料规格【"+ widthName+"】已被BOM使用，无法修改/删除。详情查看物料BOM报表!");
            }
        }
    }

    @Override
    public Boolean startStopBasicsdatumMaterialWidth(StartStopDto dto) {
        UpdateWrapper<BasicsdatumMaterialWidth> uw = new UpdateWrapper<>();
        uw.in("id", StringUtils.convertList(dto.getIds()));
        uw.set("status", dto.getStatus());

        //停用需要校验下游系统是否引用
        if ("1".equals(dto.getStatus())) {
            //检查物料规格是否被bom引用，引用了不允许修改，删除
            checkMaterialWidth(dto.getMaterialCode(),dto.getCodes(),dto.getNames(),null);

            QueryWrapper<BasicsdatumMaterialWidth> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.in("id", StringUtils.convertList(dto.getIds()));
            List<BasicsdatumMaterialWidth> list = materialWidthService.list(queryWrapper);
            for (BasicsdatumMaterialWidth basicsdatumMaterialWidth : list) {
                Boolean b = smpService.checkSizeAndColor(basicsdatumMaterialWidth.getMaterialCode(), "1", basicsdatumMaterialWidth.getWidthCode());
                if (!b) {
                    throw new OtherException("\"" + basicsdatumMaterialWidth.getName() + "\"下游系统已引用,不允许停用");
                }
            }
        }
        this.startStopLog(dto);
        return this.materialWidthService.update(null, uw);
    }

    @Override
    public Boolean delBasicsdatumMaterialWidth(RemoveDto removeDto) {
        //检查物料规格是否被bom引用，引用了不允许修改，删除
        checkMaterialWidth(removeDto.getMaterialCode(), removeDto.getCodes(), removeDto.getNames(),null);

        return this.materialWidthService.removeByIds(removeDto);
    }

    @Override
    public PageInfo<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorList(
            BasicsdatumMaterialColorQueryDto dto) {
        if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
            PageHelper.startPage(dto);
        }
        List<BasicsdatumMaterialColorPageVo> list = this.baseMapper
                .getBasicsdatumMaterialColorList(this.getCompanyCode(), dto.getMaterialCode(), dto.getStatus());
        return new PageInfo<>(list);
    }

    @Override
    public Boolean saveBasicsdatumMaterialColor(BasicsdatumMaterialColorSaveDto dto) {
        long count = this.materialColorService.count(new QueryWrapper<BasicsdatumMaterialColor>().ne("id", dto.getId())
                .eq("company_code", this.getCompanyCode()).eq("Material_Code", dto.getMaterialCode())
                .eq("color_Code", dto.getColorCode()));
        if (count > 0) {
            throw new OtherException("当前颜色已存在");
        }
        BasicsdatumMaterialColor oldEntity = null;
        String type;
        BasicsdatumMaterialColor entity = CopyUtil.copy(dto, BasicsdatumMaterialColor.class);
        if ("-1".equals(entity.getId())) {
            entity.setId(null);
            type = "新增";
        } else {
            type = "修改";
            oldEntity = materialColorService.getById(entity.getId());
        }
        Boolean b = this.materialColorService.saveOrUpdate(entity);

        if (b) {
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setName("物料档案-颜色");
            operaLogEntity.setType(type);
            operaLogEntity.setDocumentId(entity.getId());
            operaLogEntity.setDocumentCode(entity.getColorCode());
            operaLogEntity.setDocumentName(entity.getColorName());
            operaLogEntity.setParentId(dto.getParentId());
            materialWidthService.saveOrUpdateOperaLog(entity, oldEntity, operaLogEntity);
        }
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBasicsdatumMaterialColorList(List<BasicsdatumMaterialColorSaveDto> dtos) {
        List<BasicsdatumMaterialColor> list = new ArrayList<>();
        for (BasicsdatumMaterialColorSaveDto dto : dtos) {
            long count = this.materialColorService.count(new QueryWrapper<BasicsdatumMaterialColor>().ne("id", dto.getId())
                    .eq("company_code", this.getCompanyCode()).eq("Material_Code", dto.getMaterialCode())
                    .eq("color_Code", dto.getColorCode()));
            if (count > 0) {
                throw new OtherException("颜色" + dto.getColorName() + "已存在");
            }
            BasicsdatumMaterialColor entity = CopyUtil.copy(dto, BasicsdatumMaterialColor.class);
            if ("-1".equals(entity.getId())) {
                entity.setId(null);
                entity.setCreateDate(null);
                entity.setUpdateDate(null);
                entity.setCreateName(null);
                entity.setCreateId(null);
                entity.setUpdateId(null);
                entity.setUpdateName(null);
            }
            list.add(entity);
        }
        boolean b = this.materialColorService.saveOrUpdateBatch(list);
        if (b) {
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setName("物料档案-颜色");
            operaLogEntity.setType("新增");
            operaLogEntity.setParentId(dtos.get(0).getParentId());

            operaLogEntity.setDocumentCodeField("colorCode");
            operaLogEntity.setDocumentNameField("colorName");
            materialColorService.saveBatchOperaLog(list, operaLogEntity);
        }


        return b;
    }

    /**
     * 修改供应商图片
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean updateMaterialPic(BasicsdatumMaterialSaveDto dto) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("image_url", CommonUtils.removeQuery(dto.getImageUrl()));
        updateWrapper.eq("id", dto.getId());
        baseMapper.update(null, updateWrapper);
        return true;
    }

    @Override
    public String getMaterialCodeById(String id) {
        LambdaQueryWrapper<BasicsdatumMaterial> qw = new QueryWrapper<BasicsdatumMaterial>().lambda()
                .eq(BasicsdatumMaterial::getId, id)
                .select(BasicsdatumMaterial::getMaterialCode);
        BasicsdatumMaterial basicsdatumMaterial = super.getBaseMapper().selectOne(qw);
        return Objects.isNull(basicsdatumMaterial) ? null : basicsdatumMaterial.getMaterialCode();
    }

    @Override
    public boolean resetImgUrl(MultipartFile file) {
        try {
            ImportParams params = new ImportParams();
            params.setNeedSave(false);
            String s = IoUtil.readUtf8(file.getInputStream());
            List<String> split = StrUtil.split(s, "\r\n");
            Map<String, String> hz = new HashMap<>();

            for (String materialCode : split) {
                if (StrUtil.isNotBlank(materialCode)) {
                    List<String> split1 = StrUtil.split(materialCode, CharUtil.DOT);
                    hz.put(CollUtil.get(split1, 0), CollUtil.get(split1, 1));
                }
            }
            //Material/2023/秋/FBB00106.jpg
            updateImgUrl(1, 100, hz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("重置失败");
        }

        return true;
    }

    @Override
    public PageInfo matchPic(int pageNum, int pageSize) {
        QueryWrapper<BasicsdatumMaterial> qw = new QueryWrapper<>();
        qw.select("year_name,season_name,material_code,id");
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> maps = listMaps(qw);
        String[] pics = {".jpg", ".jpg", ".jpeg", ".JPEG", ".png", ".PNG"};
        if (CollUtil.isNotEmpty(maps)) {
            String materialCode = null;
            String id = null;

            List<BasicsdatumMaterial> ulist = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                materialCode = MapUtil.getStr(map, "material_code");
                id = MapUtil.getStr(map, "id");
                String object = null;
                for (String pic : pics) {
                    object = StrUtil.format("Material/{}/{}/{}{}", MapUtil.getStr(map, "year_name"), MapUtil.getStr(map, "season_name"), materialCode
                            , pic);
                    boolean b = minioUtils.hasObject(object);
                    if (b) {
                        break;
                    } else {
                        object = null;
                    }
                }
                if (object != null) {
                    BasicsdatumMaterial bm = new BasicsdatumMaterial();
                    bm.setId(id);
                    bm.setImageUrl(StrUtil.format("{}/{}/{}", minioConfig.getEndpoint(), minioConfig.getBucketName(), object));
                    ulist.add(bm);
                }

            }
            System.out.println(pageNum + "/" + page.toPageInfo().getPages() + "匹配:" + ulist.size());
            updateBatchById(ulist);
        }
        return page.toPageInfo();
    }

    @Override
    public PageInfo<BasicsdatumMaterialPageAndStyleVo> materialsBomStylePage(BasicsdatumMaterialPageAndStyleDto dto) {
        BaseQueryWrapper<BasicsdatumMaterialPageAndStyleDto> qc = new BaseQueryWrapper<>();
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qc, dto);
        qc.andLike(dto.getSearch(), "t.materialsCode", "t.materialsColor","t.materialsSpec","t.supperSampleName","t.designNo","t.bulkNo","t.styleColor");
        qc.notEmptyEq("t.bomStatus", dto.getBomPhase());
        qc.notEmptyEq("t.materialsCode", dto.getMaterialsCode());
        qc.notEmptyIn("t.packBomId", dto.getPackBomId());

        if (StringUtils.isNotEmpty(dto.getCategoryId())) {
            qc.and(Wrapper -> Wrapper.eq("t.category_id", dto.getCategoryId()).or()
                    .eq("t.category1_code ", dto.getCategoryId()).or().eq("t.category2_code", dto.getCategoryId()).or()
                    .eq("t.category3_code", dto.getCategoryId()));
        }
        PageHelper.startPage(dto);
        List<BasicsdatumMaterialPageAndStyleVo> list = this.getBaseMapper().getBasicsdatumMaterialAndStyleList(qc);
        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        //物料编号、物料颜色、物料规格、厂家简称、设计款号、大货款号、配色颜色
        for (BasicsdatumMaterialPageAndStyleVo vo : list) {
            getStyleImage(vo);
        }
        minioUtils.setObjectUrlToList(list, "materialsImageUrl");
        return new PageInfo<>(list);
    }

    @Override
    public List<String> getMaterialCodeBySupplierInfo(MaterialSupplierInfo materialSupplierInfo) {
        return materialPriceService.getMaterialCodeBySupplierInfo(materialSupplierInfo);
    }

    @Override
    public BasicsdatumMaterial getMaterialByCode(String materialCode) {
        QueryWrapper<BasicsdatumMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_code", materialCode);
        List<BasicsdatumMaterial> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<BasicsdatumMaterialColorSelectVo> getMaterialCodes(String materialCode) {
        return this.baseMapper.getBasicsdatumMaterialColorSelect(this.getCompanyCode(), materialCode);
    }

    @Override
    public FabricSummary getMaterialSummaryInfo(String materialCode) {
        BaseQueryWrapper baseQueryWrapper = new BaseQueryWrapper<>();
        baseQueryWrapper.eq("tbm.material_code",materialCode);
        List<FabricSummary> list = baseMapper.getMaterialSummaryInfo(baseQueryWrapper);
        return CollectionUtils.isEmpty(list) ? new FabricSummary() : list.get(0);
    }

    @Transactional
    @Override
    public BasicsdatumMaterialUpdateVo updateMaterialProperties(BasicsdatumMaterialUpdateDto basicsdatumMaterialUpdateDto) {
        //物料id
        String id = basicsdatumMaterialUpdateDto.getId();
        if (StrUtil.isEmpty(id)) {
            throw new OtherException("当前id为空，找不到对应的物料信息！");
        }
        BasicsdatumMaterial basicsdatumMaterial = this.getById(id);

        BasicsdatumMaterialUpdateVo basicsdatumMaterialUpdateVo = null;
        if (basicsdatumMaterial != null) {
            String distribute = basicsdatumMaterial.getDistribute();

            basicsdatumMaterialUpdateVo = new BasicsdatumMaterialUpdateVo();
            //三级分类编码
            String materialCode = basicsdatumMaterial.getMaterialCode();
            String materialName = basicsdatumMaterial.getMaterialName();
            String category3Code = basicsdatumMaterial.getCategory3Code();
            String category3Name = basicsdatumMaterial.getCategory3Name();

            String materialCodeParam = basicsdatumMaterialUpdateDto.getMaterialCode();
            String materialNameParam = basicsdatumMaterialUpdateDto.getMaterialName();
            String category3CodeParam = basicsdatumMaterialUpdateDto.getCategory3Code();

            if ("1".equals(distribute)) {
                throw new OtherException("材料编号:" + materialCode + ",已下发到下游系统，不能修改！");
            }
            checkBomRelyOn(materialCode);


            if (StrUtil.isNotEmpty(materialCodeParam)) {
                if (!materialCode.equals(materialCodeParam) && !materialCodeParam.startsWith(category3Code)) {
                    throw new OtherException("材料编号:" + materialCodeParam + "须以" + materialCode + "[" + category3Name + "]开头,请重新定义!");
                }
                //修改物料名称
                //物料名称为null 设置为空字符串
                String materilCodeNameNull = getCodeNameNull(materialName);
                basicsdatumMaterial.setMaterialCodeName(materialCodeParam + materilCodeNameNull);
                basicsdatumMaterial.setMaterialCode(materialCodeParam);
                basicsdatumMaterialUpdateVo.setMaterialCodeName(materilCodeNameNull);
            } else if (StrUtil.isNotEmpty(materialNameParam) || !materialNameParam.equals(materialName)) {
                if (StrUtil.isNotEmpty(materialCode)) {
                    //修改物料名称
                    String materilCodeNameNullParam = getCodeNameNull(materialNameParam);
                    basicsdatumMaterial.setMaterialCodeName(materialCode + materilCodeNameNullParam);
                    basicsdatumMaterial.setMaterialName(materialNameParam);
                    basicsdatumMaterialUpdateVo.setMaterialCodeName(materialCode + materilCodeNameNullParam);
                }
            } else if (StrUtil.isNotEmpty(category3CodeParam)) {
                String value = materialCode.replace(category3Code, category3CodeParam);
                String materialNameNull = getCodeNameNull(materialName);
                //修改材料三级分类
                basicsdatumMaterial.setMaterialCode(value);
                basicsdatumMaterial.setMaterialCodeName(value + materialNameNull);
                basicsdatumMaterial.setCategory2Code(basicsdatumMaterialUpdateDto.getCategory2Code());
                basicsdatumMaterial.setCategory2Name(basicsdatumMaterialUpdateDto.getCategory2Name());
                basicsdatumMaterial.setCategory3Code(basicsdatumMaterialUpdateDto.getCategory3Code());
                basicsdatumMaterial.setCategory3Name(basicsdatumMaterialUpdateDto.getCategory3Name());
                basicsdatumMaterial.setCategoryId(basicsdatumMaterialUpdateDto.getCategory3Code());
                basicsdatumMaterial.setCategoryName(basicsdatumMaterial.getCategory1Name() + "-" + basicsdatumMaterialUpdateDto.getCategory2Name() + "-" + basicsdatumMaterialUpdateDto.getCategory3Name());

                basicsdatumMaterialUpdateVo.setMaterialCodeName(value + materialNameNull);
                basicsdatumMaterialUpdateVo.setMaterialCode(value);

            }
            this.updateById(basicsdatumMaterial);
        } else {
            throw new OtherException("找不到对应的物料信息！");
        }
        //新生成的物料编号，物料库验证是否重复
        String newMaterialCode = basicsdatumMaterialUpdateVo.getMaterialCode();
        if (StrUtil.isNotEmpty(newMaterialCode)) {
            QueryWrapper<BasicsdatumMaterial> basicsdatumMaterialQueryWrapper = new QueryWrapper<>();
            basicsdatumMaterialQueryWrapper.ne("id", id);
            basicsdatumMaterialQueryWrapper.eq("material_code", newMaterialCode);
            basicsdatumMaterialQueryWrapper.eq("del_flag", "0");
            List<BasicsdatumMaterial> list = this.list(basicsdatumMaterialQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new OtherException("该物料编码【" + newMaterialCode + "】数据已存在，不允许修改！");
            }
        }
        return basicsdatumMaterialUpdateVo;
    }

    @NotNull
    private static String getCodeNameNull(String materialName) {
        String materilCodeNameNull = StrUtil.isNotEmpty(materialName) ? "_" + materialName : "";
        return materilCodeNameNull;
    }

    private void checkBomRelyOn(String materialCode) {
        if (StrUtil.isNotEmpty(materialCode)) {
            BaseQueryWrapper<BasicsdatumMaterialPageAndStyleDto> checkBomQc = new BaseQueryWrapper<>();
            checkBomQc.notEmptyEq("t.materialsCode", materialCode);
            List<BasicsdatumMaterialPageAndStyleVo> list = this.getBaseMapper().getBasicsdatumMaterialAndStyleList(checkBomQc);
            if (CollUtil.isNotEmpty(list)) {
                throw new OtherException("该物料已被BOM使用，无法修改。详情查看物料报表!");
            }
        } else {
            throw new OtherException("找不到对应物料库编号!");
        }
    }

    @Override
    public Integer materialRelyOnBom(String materialCode) {
        if (StrUtil.isNotEmpty(materialCode)) {
            BaseQueryWrapper<BasicsdatumMaterialPageAndStyleDto> checkBomQc = new BaseQueryWrapper<>();
            checkBomQc.notEmptyEq("t.materialsCode", materialCode);
            List<BasicsdatumMaterialPageAndStyleVo> list = this.getBaseMapper().getBasicsdatumMaterialAndStyleList(checkBomQc);
            if (CollUtil.isNotEmpty(list)) {
                return 1;
            }
        } else {
            return 1;
        }
        return 0;
    }

    /**
     * 得到商品款图片
     * @param vo
     */
    private void getStyleImage(BasicsdatumMaterialPageAndStyleVo vo) {
        String styleColorPic = vo.getStyleColorPic();
        String stylePic = vo.getStylePic();
        vo.setStyleImageUrl(stylePicUtils.getStyleUrl(styleColorPic));
        if (StrUtil.isEmpty(styleColorPic)) {
            vo.setStyleImageUrl(stylePicUtils.getStyleUrl(stylePic));
        }else{
            vo.setStyleImageUrl(stylePicUtils.getStyleUrl(styleColorPic));
        }
    }

    public void updateImgUrl(int pageNum, int pageSize, Map<String, String> hz) {
        QueryWrapper<BasicsdatumMaterial> qw = new QueryWrapper<>();
        qw.select("year_name,season_name,material_code,id");
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> maps = listMaps(qw);
        String url = "";
        String materialCode = null;
        String id = null;
        if (CollUtil.isNotEmpty(maps)) {
            PageInfo<Object> pageInfo = page.toPageInfo();
            List<BasicsdatumMaterial> ulist = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                materialCode = MapUtil.getStr(map, "material_code");
                id = MapUtil.getStr(map, "id");
                if (!hz.containsKey(materialCode)) {
                    continue;
                }
                url = StrUtil.format("{}/{}/Material/{}/{}/{}.{}", minioConfig.getEndpoint(), minioConfig.getBucketName(),
                        MapUtil.getStr(map, "year_name"), MapUtil.getStr(map, "season_name"), materialCode, hz.get(materialCode));
                BasicsdatumMaterial bm = new BasicsdatumMaterial();
                bm.setId(id);
                bm.setImageUrl(url);
                ulist.add(bm);
            }
            if (CollUtil.isNotEmpty(ulist)) {
                updateBatchById(ulist);
                System.out.println(pageNum + "/" + pageInfo.getPages() + "匹配:" + ulist.size());
            }

            if (pageInfo.isHasNextPage()) {
                updateImgUrl(pageNum + 1, pageSize, hz);
            }
        }
    }

    @Override
    public Boolean delBasicsdatumMaterialColor(RemoveDto removeDto) {

        if (StrUtil.isNotEmpty(removeDto.getMaterialCode())) {
            BaseQueryWrapper<PackBom> qcMaterialQuery = new BaseQueryWrapper<>();
            qcMaterialQuery.eq("material_code", removeDto.getMaterialCode());
            qcMaterialQuery.eq("color_code", removeDto.getCodes());
            qcMaterialQuery.eq("del_flag","0");
            List<PackBom> list = packBomService.list(qcMaterialQuery);
            if (CollUtil.isNotEmpty(list)) {
                throw new OtherException("该物料【"+removeDto.getMaterialCode()+"】,颜色名称【"+ removeDto.getNames()+"】已被BOM使用，无法删除。详情查看物料BOM报表!");
            }
        }

        return this.materialColorService.removeByIds(removeDto);
    }

    @Override
    public Boolean startStopBasicsdatumMaterialColor(StartStopDto dto) {
        if (StrUtil.isNotEmpty(dto.getMaterialCode())) {
            BaseQueryWrapper<PackBom> qcMaterialQuery = new BaseQueryWrapper<>();
            qcMaterialQuery.eq("material_code", dto.getMaterialCode());
            qcMaterialQuery.eq("color_code", dto.getCodes());
            qcMaterialQuery.eq("del_flag","0");
            List<PackBom> list = packBomService.list(qcMaterialQuery);
            if (CollUtil.isNotEmpty(list)) {
                throw new OtherException("该物料【"+dto.getMaterialCode()+"】,颜色名称【"+ dto.getNames()+"】已被BOM使用，无法删除。详情查看物料BOM报表!");
            }
        }

        UpdateWrapper<BasicsdatumMaterialColor> uw = new UpdateWrapper<>();
        uw.in("id", StringUtils.convertList(dto.getIds()));
        uw.set("status", dto.getStatus());

        //停用需要校验下游系统是否引用

        //region pdm系统卡控，无需验证下游系统。
        /*if ("1".equals(dto.getStatus())) {
            QueryWrapper<BasicsdatumMaterialColor> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.in("id", StringUtils.convertList(dto.getIds()));
            List<BasicsdatumMaterialColor> list = materialColorService.list(queryWrapper);
            for (BasicsdatumMaterialColor basicsdatumMaterialColor : list) {
                Boolean b = smpService.checkSizeAndColor(basicsdatumMaterialColor.getMaterialCode(), "2", basicsdatumMaterialColor.getColorCode());
                if (!b) {
                    throw new OtherException("\"" + basicsdatumMaterialColor.getColorName() + "\"下游系统以引用,不允许停用");
                }
            }
        }*/
        //endregion
        this.startStopLog(dto);
        return this.materialColorService.update(null, uw);
    }

    @Override
    public PageInfo<BasicsdatumMaterialPricePageVo> getBasicsdatumMaterialPriceList(
            BasicsdatumMaterialPriceQueryDto dto) {
        if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
            PageHelper.startPage(dto);
        }

        BaseQueryWrapper<BasicsdatumMaterialPrice> qc = new BaseQueryWrapper<>();
        qc.eq("company_code", this.getCompanyCode());
        qc.notEmptyEq("material_code", dto.getMaterialCode());
        qc.notEmptyEq("status", dto.getStatus());
        qc.orderByDesc("select_flag");
        List<BasicsdatumMaterialPrice> list = this.materialPriceService.list(qc);
        return CopyUtil.copy(new PageInfo<>(list), BasicsdatumMaterialPricePageVo.class);
    }

    @Override
    public Map<String, Object> getBasicsdatumMaterialPriceColorWidthSelect(String materialCode) {
        Map<String, Object> map = new HashMap<>();
        List<BasicsdatumMaterialColorSelectVo> select = this.baseMapper
                .getBasicsdatumMaterialColorSelect(this.getCompanyCode(), materialCode);
        List<BasicsdatumMaterialWidthSelectVo> select2 = this.baseMapper
                .getBasicsdatumMaterialWidthSelect(this.getCompanyCode(), materialCode);
        map.put("colorSelect", select);
        map.put("widthSelect", select2);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBasicsdatumMaterialPrice(BasicsdatumMaterialPriceSaveDto dto) {
        BasicsdatumMaterialPrice entity = CopyUtil.copy(dto, BasicsdatumMaterialPrice.class);
        if ("-1".equals(entity.getId())) {
            entity.setId(null);
        }
        this.materialPriceService.saveOrUpdate(entity);
        // 如果当前是默认：批量修改其他物料未非默认，同时修改主信息的默认物料
        if (entity.getSelectFlag() != null && entity.getSelectFlag()) {
            this.materialPriceService.update(new UpdateWrapper<BasicsdatumMaterialPrice>()
                    .eq(COMPANY_CODE, getCompanyCode()).eq("material_code", entity.getMaterialCode())
                    .ne("id", entity.getId()).set("select_flag", "0"));

            this.update(new UpdateWrapper<BasicsdatumMaterial>()
                    .eq(COMPANY_CODE, getCompanyCode())
                    .eq("material_code", entity.getMaterialCode())
//                    .eq("biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK())
                    .set("supplier_id", dto.getSupplierId())
                    .set("supplier_name", dto.getSupplierName())
                    .set("supplier_fabric_code", entity.getSupplierMaterialCode())
                    .set("supplier_quotation_price", entity.getQuotationPrice()));
        }

        basicsdatumMaterialPriceDetailService
                .remove(new QueryWrapper<BasicsdatumMaterialPriceDetail>().eq("price_id", entity.getId()));
        List<BasicsdatumMaterialPriceDetail> basicsdatumMaterialPriceDetails = new ArrayList<>();

        String[] colorCodes = entity.getColor().split(",");
        String[] colorNames = entity.getColorName().split(",");
        String[] widthCodes = entity.getWidth().split(",");
        String[] widthNames = entity.getWidthName().split(",");
        for (int i = 0; i < colorNames.length; i++) {
            for (int j = 0; j < widthNames.length; j++) {
                BasicsdatumMaterialPriceDetail basicsdatumMaterialPriceDetai = new BasicsdatumMaterialPriceDetail();
                BeanUtil.copyProperties(entity, basicsdatumMaterialPriceDetai);
                basicsdatumMaterialPriceDetai.setId(null);
                basicsdatumMaterialPriceDetai.setPriceId(entity.getId());

                basicsdatumMaterialPriceDetai.setColorName(colorNames[i]);
                basicsdatumMaterialPriceDetai.setWidthName(widthNames[j]);
                try {
                    basicsdatumMaterialPriceDetai.setColor(colorCodes[i]);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                try {
                    basicsdatumMaterialPriceDetai.setWidth(widthCodes[j]);
                } catch (Exception e) {

                    // e.printStackTrace();
                }
                basicsdatumMaterialPriceDetails.add(basicsdatumMaterialPriceDetai);
            }
        }
        basicsdatumMaterialPriceDetailService.saveBatch(basicsdatumMaterialPriceDetails);
        return true;
    }

    @Override
    public Boolean startStopBasicsdatumMaterialPrice(StartStopDto dto) {
        UpdateWrapper<BasicsdatumMaterialPrice> uw = new UpdateWrapper<>();
        uw.in("id", StringUtils.convertList(dto.getIds()));
        uw.set("status", dto.getStatus());
        return this.materialPriceService.update(null, uw);
    }

    @Override
    public Boolean delBasicsdatumMaterialPrice(String id) {
        return this.materialPriceService.removeBatchByIds(StringUtils.convertList(id));
    }

    /**
     * 根据规格组绑定规格
     */
    @Transactional
    @Override
    public Boolean saveBasicsdatumMaterialWidthGroup(BasicsdatumMaterialWidthGroupSaveDto dto) {

        // 1、 获取规格组的规格集合
        List<Specification> specifications = null;
        SpecificationGroup specificationGroup = specificationGroupService.getOne(new QueryWrapper<SpecificationGroup>()
                .eq(COMPANY_CODE, getCompanyCode()).eq("code", dto.getWidthGroupCode()));
        BaseQueryWrapper<Specification> queryWrapper = new BaseQueryWrapper<>();
        if (specificationGroup != null && StringUtils.isNotBlank(specificationGroup.getSpecificationIds())) {
            String specificationIds = specificationGroup.getSpecificationIds();
            String[] ids = specificationIds.split(",");
            queryWrapper.in("code", Arrays.asList(ids));
            specifications = specificationService.list(queryWrapper);
        }
        // 2、清理现有物料规格
        this.materialWidthService.remove(new QueryWrapper<BasicsdatumMaterialWidth>().eq(COMPANY_CODE, getCompanyCode())
                .eq("material_code", dto.getMaterialCode()));

        if (StrUtil.isEmpty(dto.getWidthGroupCode())) {
            LambdaUpdateWrapper<BasicsdatumMaterial> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BasicsdatumMaterial::getMaterialCode, dto.getMaterialCode());
            updateWrapper.eq(BasicsdatumMaterial::getDelFlag, "0");
            updateWrapper.set(BasicsdatumMaterial::getWidthGroup,null);
            updateWrapper.set(BasicsdatumMaterial::getWidthGroupName,null);
            this.update(updateWrapper);
        }


        // 3、添加规格组的规格
        if (specifications != null && specifications.size() > 0) {
            List<BasicsdatumMaterialWidth> wList = new ArrayList<>();
            BasicsdatumMaterialWidth width;
            for (Specification specification : specifications) {
                width = new BasicsdatumMaterialWidth();
                width.setCompanyCode(getCompanyCode());
                width.setStatus("0");
                width.setMaterialCode(dto.getMaterialCode());
                width.setWidthCode(specification.getCode());
                width.setName(specification.getName());
                wList.add(width);
            }
            this.materialWidthService.saveBatch(wList);
            BasicsdatumMaterial basicsdatumMaterial = this.getOne(new QueryWrapper<BasicsdatumMaterial>().eq("material_code", dto.getMaterialCode()).eq("del_flag", "0"));
            if (basicsdatumMaterial != null) {
                basicsdatumMaterial.setWidthGroup(specificationGroup.getCode());
                basicsdatumMaterial.setWidthGroupName(specificationGroup.getName());
                this.updateById(basicsdatumMaterial);
            }
        }
        return true;
    }

    /**
     * 根据规格下拉 绑定规格
     */
    @Override
    @Transactional
    public Boolean saveBasicsdatumMaterialWidths(BasicsdatumMaterialWidthsSaveDto dto) {
        // 1、清理现有物料规格
        this.materialWidthService.remove(new QueryWrapper<BasicsdatumMaterialWidth>().eq(COMPANY_CODE, getCompanyCode())
                .eq("material_code", dto.getMaterialCode()));

        //取消全选直接return
        if (StrUtil.isEmpty(dto.getWidthName())) {
            return true;
        }
        String[] codes = dto.getWidth().split(",");
        String[] names = dto.getWidthName().split(",");
        // 2、添加规格组的规格
        if (codes != null && codes.length > 0) {
            List<BasicsdatumMaterialWidth> wList = new ArrayList<>();
            BasicsdatumMaterialWidth width;
            int i = 0;
            for (String code : codes) {
                width = new BasicsdatumMaterialWidth();
                width.setCompanyCode(getCompanyCode());
                width.setStatus("0");
                width.setMaterialCode(dto.getMaterialCode());
                width.setWidthCode(code);
                width.setName(names[i]);
                i++;
                wList.add(width);
            }
            this.materialWidthService.saveBatch(wList);
        }
        return true;
    }

    /**
     * 查询旧料号列表
     */
    @Override
    public PageInfo<BasicsdatumMaterialOldPageVo> getBasicsdatumMaterialOldList(BasicsdatumMaterialOldQueryDto dto) {
        if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
            PageHelper.startPage(dto);
        }
        List<BasicsdatumMaterialOldPageVo> list = baseMapper.getBasicsdatumMaterialOldPage(this.getCompanyCode(),
                dto.getMaterialCode());
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public Boolean saveBasicsdatumMaterialOld(BasicsdatumMaterialOldSaveDto dto) {
        // 清理
//		this.materialOldService.remove(new QueryWrapper<BasicsdatumMaterialOld>().eq(COMPANY_CODE, getCompanyCode())
//				.eq("material_code", dto.getMaterialCode()));
        long count = materialOldService.count(new QueryWrapper<BasicsdatumMaterialOld>()
                .eq(COMPANY_CODE, getCompanyCode()).eq("material_code", dto.getMaterialCode())
                .in("old_Material_code", StringUtils.convertList(dto.getOldMaterialCode())));
        if (count > 0) {
            throw new OtherException("已添加对应的旧料号");
        }
        String[] codes = dto.getOldMaterialCode().split(",");

        List<BasicsdatumMaterialOld> list = new ArrayList<>();
        BasicsdatumMaterialOld old = null;
        for (String code : codes) {
            old = new BasicsdatumMaterialOld();
            old.setCompanyCode(getCompanyCode());
            old.setMaterialCode(dto.getMaterialCode());
            old.setOldMaterialCode(code);
            list.add(old);
        }
        this.materialOldService.saveBatch(list);
        return true;
    }

    @Override
    public Boolean delBasicsdatumMaterialOld(String id) {
        return this.materialOldService.removeBatchByIds(StringUtils.convertList(id));
    }

    @Override
    public PageInfo<WarehouseMaterialVo> getPurchaseMaterialList(BasicsdatumMaterialQueryDto dto) {
        Page<WarehouseMaterialVo> page = null;
        if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
            page = PageHelper.startPage(dto);
        }
        BaseQueryWrapper<BasicsdatumMaterial> qc = new BaseQueryWrapper<>();
        qc.eq("m.company_code", this.getCompanyCode());
        qc.eq("m.status", "0");
        qc.notEmptyLike("m.supplier_id", dto.getSupplierId());
        qc.notEmptyLike("c.color_name", dto.getMaterialColor());
        if (StringUtils.isNotEmpty(dto.getSearch())) {
            qc.and(Wrapper -> Wrapper.like("m.material_code", dto.getSearch()).or().like("m.material_name ",
                    dto.getSearch()));
        }
        qc.eq("m.biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK());
        qc.eq("m.confirm_status", "2");
        qc.eq("m.del_flag", "0");
        List<WarehouseMaterialVo> list = getBaseMapper().getPurchaseMaterialList(qc);
        for (WarehouseMaterialVo item : list) {
            item.setId(IdUtil.randomUUID());
        }
        if (page != null) {
            return page.toPageInfo();
        }
        return new PageInfo<>(list);
    }

    @Override
    public Boolean updateInquiryNumberDeliveryName(BasicsdatumMaterialSaveDto dto) {
        BasicsdatumMaterial basicsdatumMaterial = new BasicsdatumMaterial();
        basicsdatumMaterial.setId(dto.getId());
        basicsdatumMaterial.setInquiryNumber(dto.getInquiryNumber());
        basicsdatumMaterial.setDeliveryName(dto.getDeliveryName());
        int countNum = this.baseMapper.updateById(basicsdatumMaterial);
        return countNum > 0;
    }

    @Override
    public BasicsdatumMaterialVo saveSubmit(BasicsdatumMaterialSaveDto dto) {
        dto.setConfirmStatus("1");
        BasicsdatumMaterialVo basicsdatumMaterialVo = this.saveBasicsdatumMaterial(dto);
        flowableService.start(FlowableService.BASICSDATUM_MATERIAL,
                FlowableService.BASICSDATUM_MATERIAL,        StrUtil.isNotEmpty(dto.getId()) ? dto.getId() : basicsdatumMaterialVo.getId(),
                "/pdm/api/saas/basicsdatumMaterial/approval",
                "/pdm/api/saas/basicsdatumMaterial/approval",
                "/pdm/api/saas/basicsdatumMaterial/approval",
                "pdm/api/saas/basicsdatumMaterial/getBasicsdatumMaterial?id=" + dto.getId(), BeanUtil.beanToMap(dto));
        return basicsdatumMaterialVo;
    }

    @Override
    @Transactional(rollbackFor = {OtherException.class, Exception.class})
    public boolean approval(AnswerDto dto) {
        BasicsdatumMaterialVo basicsdatumMaterialVo = this.getBasicsdatumMaterial(dto.getBusinessKey());
        if (Objects.isNull(basicsdatumMaterialVo)) {
            throw new OtherException("数据不存在");
        }
        if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
            BasicsdatumMaterial basicsdatumMaterial = new BasicsdatumMaterial();
            basicsdatumMaterial.setId(basicsdatumMaterialVo.getId());
            basicsdatumMaterial.updateInit();
            basicsdatumMaterial.setConfirmStatus("2");
            basicsdatumMaterial.setConfirmId(super.getUserId());
            basicsdatumMaterial.setConfirmName(super.getUserName());
            super.updateById(basicsdatumMaterial);
        } else {
            super.getBaseMapper().deleteById(basicsdatumMaterialVo.getId());
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("material_code", basicsdatumMaterialVo.getMaterialCode());
            materialOldService.remove(queryWrapper);
            materialColorService.remove(queryWrapper);
            materialPriceService.remove(queryWrapper);
            materialIngredientService.remove(queryWrapper);
            basicsdatumMaterialPriceDetailService.remove(queryWrapper);
            materialWidthService.remove(queryWrapper);
        }
        basicFabricLibraryService.materialApproveProcessing(basicsdatumMaterialVo.getId(), dto.getApprovalType(), basicsdatumMaterialVo.getMaterialCode());
        return true;
    }

    /**
     * 解锁下发
     *
     * @param id
     * @return
     */
    @Override
    public Boolean unlock(String id) {
        BasicsdatumMaterial basicsdatumMaterial = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(basicsdatumMaterial)) {
            throw new OtherException("数据不存在");
        }
        basicsdatumMaterial.setDistribute("3");
        baseMapper.updateById(basicsdatumMaterial);
        return true;
    }

    @Override
    public String genMaterialCode(BasicsdatumMaterial material) {
        GetMaxCodeRedis getNextCode = new GetMaxCodeRedis(ccmService);
        Map<String, String> params = new HashMap<>(12);
        params.put("category2Code", material.getCategory2Code());
        params.put("category3Code", material.getCategory3Code());
        params.put("year", material.getYear());
        params.put("season", material.getSeason());
        return getNextCode.genCodeExists("MATERIAL_CODE", params);
    }

    @Override
    public String getMaxMaterialCode(GetMaxCodeRedis data, String userCompany) {
        List<String> regexps = new ArrayList<>(12);
        List<String> textFormats = new ArrayList<>(12);
        data.getValueMap().forEach((key, val) -> {
            if (BaseConstant.FLOWING.equals(key)) {
                textFormats.add("(" + val + ")");
            } else {
                textFormats.add(String.valueOf(val));
            }
            regexps.add(String.valueOf(val));
        });
        String regexp = "^" + CollUtil.join(regexps, "");
        QueryWrapper qc = new QueryWrapper();
        qc.eq(COMPANY_CODE, userCompany);
        qc.apply(" material_code REGEXP '" + regexp + "'");
        qc.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        String maxCode = baseMapper.selectMaxMaterialCode(qc);
        if (StrUtil.isBlank(maxCode)) {
            return null;
        }
        // 替换,保留流水号
        String formatStr = CollUtil.join(textFormats, "");
        try {
            String flowing = ReUtil.get(formatStr, maxCode, 1);
            if (StrUtil.isNotBlank(flowing)) {
                return flowing;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, BasicsdatumMaterial> getSourceAndIngredient(List<String> materialCodes) {
        if (CollectionUtils.isEmpty(materialCodes)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<BasicsdatumMaterial> qw = new QueryWrapper<BasicsdatumMaterial>().lambda()
                .in(BasicsdatumMaterial::getMaterialCode, materialCodes)
                .eq(BasicsdatumMaterial::getDelFlag, "0")
                .select(BasicsdatumMaterial::getMaterialCode, BasicsdatumMaterial::getSource, BasicsdatumMaterial::getIngredient,BasicsdatumMaterial::getSupplierFabricCode);
        List<BasicsdatumMaterial> list = super.list(qw);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.toMap(BasicsdatumMaterial::getMaterialCode, v -> v, (k1, k2) -> k2));
    }


    private void fullMaterialColor(BasicsdatumMaterialPageVo basicsdatumMaterialPageVo) {

        QueryWrapper<BasicsdatumMaterialColor> qw = new QueryWrapper<>();
        qw.lambda().eq(BasicsdatumMaterialColor::getMaterialCode,basicsdatumMaterialPageVo.getMaterialCode());
        qw.lambda().eq(BasicsdatumMaterialColor::getDelFlag,"0");
        List<BasicsdatumMaterialColor> list = materialColorService.list(qw);
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        StringBuilder colorCode = new StringBuilder();
        StringBuilder colorName = new StringBuilder();
        StringBuilder supplierColorCode = new StringBuilder();

        for (BasicsdatumMaterialColor materialColor : list) {
            colorCode.append(Opt.ofBlankAble(materialColor.getColorCode()).orElse("")).append(",");
            colorName.append(Opt.ofBlankAble(materialColor.getColorName()).orElse("")).append(",");
            supplierColorCode.append(Opt.ofBlankAble(materialColor.getSupplierColorCode()).orElse("")).append(",");
        }
        if(colorCode.length() > 0) {
            colorCode.deleteCharAt(colorCode.length() - 1);
        }
        if(colorName.length() > 0) {
            colorName.deleteCharAt(colorName.length() - 1);
        }
        if(supplierColorCode.length() > 0) {
            supplierColorCode.deleteCharAt(supplierColorCode.length() - 1);
        }
        basicsdatumMaterialPageVo.setColorCode(colorCode.toString());
        basicsdatumMaterialPageVo.setColorName(colorName.toString());
        basicsdatumMaterialPageVo.setSupplierColorCode(supplierColorCode.toString());
    }

}
