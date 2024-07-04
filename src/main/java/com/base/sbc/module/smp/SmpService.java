package com.base.sbc.module.smp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.JsonStringUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.RFIDProperties;
import com.base.sbc.config.constant.SmpProperties;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.resttemplate.RestTemplateService;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthQueryDto;
import com.base.sbc.module.basicsdatum.dto.SecondIngredientSyncDto;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialWidthPageVo;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formtype.dto.FieldBusinessSystemQueryDto;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldBusinessSystemService;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldBusinessSystemVo;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.formtype.vo.GoodsDynamicFieldDto;
import com.base.sbc.module.hangtag.dto.SmpHangTagIngredientDTO;
import com.base.sbc.module.hangtag.dto.UpdatePriceDto;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.enums.HangTagDeliverySCMStatusEnum;
import com.base.sbc.module.hangtag.service.HangTagIngredientService;
import com.base.sbc.module.hangtag.service.impl.HangTagServiceImpl;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.vo.OrderBookSimilarStyleVo;
import com.base.sbc.module.orderbook.vo.StyleSaleIntoDto;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.smp.dto.*;
import com.base.sbc.module.smp.entity.*;
import com.base.sbc.module.smp.impl.SaleProductIntoService;
import com.base.sbc.module.style.entity.*;
import com.base.sbc.module.style.service.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH;
import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.module.common.convert.ConvertContext.ORDER_BOOK_CV;
import static com.base.sbc.module.hangtag.enums.HangTagDeliverySCMStatusEnum.HANG_TAG_PRICING_LINE;


/**
 * @author 卞康
 * @date 2023/5/8 15:27:43
 * @mail 247967116@qq.com
 * 对接下发Smp主数据
 */
@Service
@RequiredArgsConstructor
@Configuration
public class SmpService {

    private final DataSourceTransactionManager dataSourceTransactionManager;

    private final TransactionDefinition transactionDefinition;

    private final RestTemplateService restTemplateService;

    private final PushRecordsService pushRecordsService;

    @Resource
    @Lazy
    private  BasicsdatumMaterialService basicsdatumMaterialService;

    private final BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;

    private final AmcService amcService;

    private final UserUtils userUtils;

    @Resource
    @Lazy
    private final PackInfoService packInfoService;

    @Resource
    @Lazy
    private final PackTechSpecService packTechSpecService;

    @Resource
    @Lazy
    private StyleSpecFabricService styleSpecFabricService;

    private final PackBomService packBomService;
    private final PackBomVersionService packBomVersionService;

    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;

    private final PackBomSizeService packBomSizeService;

    private final BasicsdatumSizeService basicsdatumSizeService;

    private final StyleService styleService;

    private final StyleColorAgentService styleColorAgentService;;
    @Resource
    @Lazy
    private StyleColorService styleColorService;

    private final CcmFeignService ccmFeignService;

    @Resource
    @Lazy
    private PackSizeService packSizeService;

    private final AttachmentService attachmentService;

    private final PackInfoStatusService packInfoStatusService;

    private final PackPricingService packPricingService;

    private final PackTechPackagingService packTechPackagingService;

    private final StylePricingService stylePricingService;

    private final PatternMakingService patternMakingService;

    private final BasicsdatumIngredientService basicsdatumIngredientService;
    private final BasicsdatumMaterialColorService basicsdatumMaterialColorService;
    private final PreProductionSampleTaskService preProductionSampleTaskService;
    private final UploadFileService uploadFileService;
    private final BasicsdatumMaterialPriceService basicsdatumMaterialPriceService;
    private final StyleMainAccessoriesService styleMainAccessoriesService;

    private final BasicsdatumSupplierService basicsdatumSupplierService;
    private final HangTagServiceImpl hangTagService;
    private final FieldValService fieldValService;
    private final SaleProductIntoService saleProductIntoService;
    private final DataPermissionsService dataPermissionsService;
    @Lazy
    private final HangTagIngredientService hangTagIngredientService;

    @Resource
    @Lazy
    private StyleColorCorrectInfoService styleColorCorrectInfoService;


    @Value("${interface.smpUrl:http://10.98.250.31:7006/pdm}")
    private String SMP_URL;

    @Value("${interface.scmUrl:http://10.8.250.100:1980/escm-app/information/pdm}")
    private String SCM_URL;

    @Value("${interface.oaUrl:http://10.8.240.161:40002/mps-interfaces/sample}")
    private String OA_URL;

    @Autowired
    private FieldBusinessSystemService fieldBusinessSystemService;

    @Resource
    public OperaLogService operaLogService;

    public Integer goods(String[] ids) {
        return goods(ids,null,null);
    }

    public Integer goods(String[] ids,String targetBusinessSystem,String yshBusinessSystem) {
        return goods(ids,targetBusinessSystem,yshBusinessSystem,null,null);
    }

    @Async
    public void goodsAsync(String[] ids, String targetBusinessSystem, String yshBusinessSystem) {
        List<String> msg = new ArrayList<>();
        StringBuffer sbMsg1 = new StringBuffer();
        int i =goods(ids,targetBusinessSystem,yshBusinessSystem,1,msg);
        if (ids.length == i) {
            sbMsg1.append("下发：").append(ids.length).append("条,成功：").append(i).append("条");
        } else {
            sbMsg1.append("下发：").append(ids.length).append("条,成功：").append(i).append("条,失败：").append(ids.length - i).append("条;失败原因如下:").append(String.join(";", msg));
        }
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setType("导入-下发");
        operaLogEntity.setDocumentId("导入下发结果");
        operaLogEntity.setName("款式打标-批量导入修改");
        operaLogEntity.setDocumentName("导入结果");
        operaLogEntity.setContent(sbMsg1.toString());
        operaLogService.save(operaLogEntity);
    }

    /**
     * 商品主数据下发
     */
    public Integer goods(String[] ids,String targetBusinessSystem,String yshBusinessSystem,Integer type,List<String> msg) {
        int i = 0;

        List<StyleColor> styleColors = styleColorService.listByIds(Arrays.asList(ids));
        if (CollUtil.isEmpty(styleColors)) {
            return i;
        }
        /*for (StyleColor styleColor : styleColors) {
            //判断是否是旧系统数据
            if ("1".equals(styleColor.getHistoricalData())) {
                // throw new OtherException("旧系统数据不允许下发");
            }
        }*/
        List<String> styleColorIds = styleColors.stream().map(BaseEntity::getId).distinct().collect(Collectors.toList());

        //批量查询配饰款
        List<StyleMainAccessories> styleMainAccessories = styleMainAccessoriesService.styleMainAccessoriesListBatch(styleColorIds, null);
        Map<String, List<StyleMainAccessories>> mainAccessoriesListMap = styleMainAccessories.stream().collect(Collectors.groupingBy(StyleMainAccessories::getStyleColorId));

        //查询品牌字典
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Band");
        Map<String, String> map = dictInfoToMap.get("C8_Band");

        //这里读取各个系统的动态字段配置
        List<FieldBusinessSystemVo> businessSystemList = fieldBusinessSystemService.findList(new FieldBusinessSystemQueryDto());
        Map<String, List<FieldBusinessSystemVo>> collect = businessSystemList.stream().collect(Collectors.groupingBy(FieldBusinessSystemVo::getBusinessType));

        //查询动态字段
        List<FieldVal> fieldValList = fieldValService.list(styleColorIds, FieldValDataGroupConstant.STYLE_MARKING_ORDER);
        Map<String, List<FieldVal>> fieldValMap = fieldValList.stream().collect(Collectors.groupingBy(FieldVal::getForeignId));

        //添加配色指定面料下发 huangqiang
        QueryWrapper<StyleSpecFabric> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("style_color_id", styleColorIds);
        queryWrapper.eq("del_flag","0");
        List<StyleSpecFabric> styleSpecFabricList = styleSpecFabricService.list(queryWrapper);
        Map<String, List<StyleSpecFabric>> styleSpecFabricMap = styleSpecFabricList.stream().collect(Collectors.groupingBy(StyleSpecFabric::getStyleColorId));

        for (StyleColor styleColor : styleColors) {
            if (mainAccessoriesListMap.containsKey(styleColor.getId())) {
                List<StyleMainAccessories> mainAccessoriesList = mainAccessoriesListMap.get(styleColor.getId());
                String styleNos = mainAccessoriesList.stream().map(StyleMainAccessories::getStyleNo).collect(Collectors.joining(","));
                String colorName = mainAccessoriesList.stream().map(StyleMainAccessories::getColorName).collect(Collectors.joining(","));
                if (StringUtils.equals(styleColor.getIsTrim(), BaseGlobal.NO)) {
                    styleColor.setAccessory(colorName);
                    styleColor.setAccessoryNo(styleNos);
                } else {
                    styleColor.setPrincipalStyle(colorName);
                    styleColor.setPrincipalStyleNo(styleNos);
                }
            }


            SmpGoodsDto smpGoodsDto = styleColor.toSmpGoodsDto();
            //吊牌价为空或者等于0
            if (styleColor.getTagPrice()==null || styleColor.getTagPrice().compareTo(BigDecimal.ZERO)==0){
                if(type != null && type == 1){
                    //不抛出异常  保存到msg中
                    msg.add(styleColor.getStyleNo()+"吊牌价不能为空或者等于0");
                    continue;
                }else{
                    throw new OtherException(styleColor.getStyleNo()+"吊牌价不能为空或者等于0");
                }
            }
            PackInfoListVo packInfo = packInfoService.getByQw(new QueryWrapper<PackInfo>().eq("code", styleColor.getBom()).eq("pack_type", "0".equals(styleColor.getBomStatus()) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS));
            Style style = new Style();
            if (packInfo!=null){
                //产前样查询 拿最早的工艺部接收正确样时间
                List<PreProductionSampleTask> sampleTaskList = preProductionSampleTaskService.list(new QueryWrapper<PreProductionSampleTask>().eq("pack_info_id", packInfo.getId()).orderByAsc("tech_receive_date"));
//                PreProductionSampleTask preProductionSampleTask = preProductionSampleTaskService.getOne(new QueryWrapper<PreProductionSampleTask>().eq("pack_info_id", packInfo.getId()));
                if (CollUtil.isNotEmpty(sampleTaskList)){
                    smpGoodsDto.setTechReceiveDate(sampleTaskList.get(0).getTechReceiveDate());
                    smpGoodsDto.setProcessDepartmentDate(sampleTaskList.get(0).getProcessDepartmentDate());
                }
                style = styleService.getById(packInfo.getStyleId());
                if (style==null){
                    style= styleService.getById(styleColor.getStyleId());
                }
            }

            smpGoodsDto.setSendMainFabricDate(styleColor.getSendMainFabricDate());
            smpGoodsDto.setColorCrash(styleColor.getColorCrash());
            smpGoodsDto.setMaxClassName(style.getProdCategory1stName());
            smpGoodsDto.setStyleBigClass(style.getProdCategory1st());
            smpGoodsDto.setCategoryName(style.getProdCategoryName());
            smpGoodsDto.setStyleCategory(style.getProdCategory());
            smpGoodsDto.setMiddleClassName(style.getProdCategory2ndName());
            smpGoodsDto.setStyleMiddleClass(style.getProdCategory2nd());
            smpGoodsDto.setMinClassName(style.getProdCategory3rdName());
            smpGoodsDto.setStyleSmallClass(style.getProdCategory3rd());

            smpGoodsDto.setBrandId(style.getBrand());
            smpGoodsDto.setBrandName(style.getBrandName());
            //String sizeRange = sampleDesign.getSizeRange();
            //BasicsdatumModelType basicsdatumModelType = basicsdatumModelTypeService.getById(sizeRange);
            //PlmStyleSizeParam param = new PlmStyleSizeParam();
            //param.setSizeCategory(sizeRange);
            //param.setSizeNum(basicsdatumModelType.getSizeIds().split(",").length);
            //param.setStyleNo(sampleDesign.getStyleNo());
            //Boolean style = this.style(param);
            //if(!style){
            //    throw new OtherException("当前号型类型已在单据中，不允许修改");
            //}

            // 款式图片
            List<AttachmentVo> stylePicList = attachmentService.findByforeignId(style.getId(), AttachmentTypeConstant.STYLE_MASTER_DATA_PIC);
            List<String> imgList = new ArrayList<>();
            for (AttachmentVo attachmentVo : stylePicList) {
                imgList.add(attachmentVo.getUrl());
            }
            smpGoodsDto.setImgList(imgList);



            //设计师id,下稿设计师,工艺员,工艺员id,版师名称,版师ID
            String designer = style.getDesigner();
            smpGoodsDto.setDesigner(StringUtils.isNotEmpty(designer) ? designer.split(",")[0] : "");
            smpGoodsDto.setTechnician(style.getTechnicianName());
            smpGoodsDto.setPatternMakerName(style.getPatternDesignName());

            String designerId = style.getDesignerId();
            String technicianId = style.getTechnicianId();
            String patternDesignId = style.getPatternDesignId();

            ArrayList<String> list = new ArrayList<>();
            list.add(designerId);
            list.add(technicianId);
            list.add(patternDesignId);

            Map<String, String> usernamesByIds = amcService.getUsernamesByIds(StringUtils.join(list, ","));
            smpGoodsDto.setDesignerId(usernamesByIds.get(designerId));
            smpGoodsDto.setTechnicianId(usernamesByIds.get(technicianId));
            smpGoodsDto.setYear(style.getYearName());
            smpGoodsDto.setPatternName("常规");
            smpGoodsDto.setPriorityId(style.getTaskLevel());
            smpGoodsDto.setPriorityName(style.getTaskLevelName());
            smpGoodsDto.setSeason(style.getSeason());
            smpGoodsDto.setTheme(style.getSubject());
            smpGoodsDto.setStyleName(style.getStyleName());
            smpGoodsDto.setTargetCost(style.getProductCost());
            smpGoodsDto.setShapeName(style.getPlateType());
            smpGoodsDto.setUniqueCode(styleColor.getWareCode());
            smpGoodsDto.setBandName(map.get(styleColor.getBandCode()));

            //List<FieldVal> list1 = fieldValService.list(sampleDesign.getId(), FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
            List<GoodsDynamicFieldDto> goodsDynamicFieldDtos = new ArrayList<>();

            //动态字段

            //目标系统
            smpGoodsDto.setTargetBusinessSystem(targetBusinessSystem);
            //目标系统
            smpGoodsDto.setYshBusinessSystem(yshBusinessSystem);

            DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
            BeanUtil.copyProperties(style, dto);
            dto.setId(style.getId());
            dto.setForeignId(styleColor.getId());
            dto.setDataGroup(FieldValDataGroupConstant.STYLE_MARKING_ORDER);
            List<FieldManagementVo> fieldManagementVoList = styleService.queryDimensionLabels(dto);
            //List<FieldManagementVo> fieldManagementVoList = styleColorService.getStyleColorDynamicDataById(styleColor.getId());
            Map<String, FieldManagementVo> collect1 = fieldManagementVoList.stream().collect(Collectors.toMap(FieldManagementVo::getFieldName, o -> o, (v1, v2) -> v1));

            Map<String,List<GoodsDynamicFieldDto>>  goodsDynamicFieldMap = new HashMap<>();
            for (Map.Entry<String, List<FieldBusinessSystemVo>> entry : collect.entrySet()) {
                List<FieldBusinessSystemVo> value = entry.getValue();
                List<GoodsDynamicFieldDto> goodsDynamicFieldDtos1 = new ArrayList<>();
                for (FieldBusinessSystemVo fieldBusinessSystemVo : value) {
                    if(collect1.containsKey(fieldBusinessSystemVo.getFieldName())){
                        FieldManagementVo fieldManagementVo = collect1.get(fieldBusinessSystemVo.getFieldName());
                        GoodsDynamicFieldDto goodsDynamicFieldDto = BeanUtil.copyProperties(fieldManagementVo, GoodsDynamicFieldDto.class);
                        goodsDynamicFieldDtos1.add(goodsDynamicFieldDto);
                    }
                }
                goodsDynamicFieldMap.put(entry.getKey(),goodsDynamicFieldDtos1);
            }
            smpGoodsDto.setGoodsDynamicFieldMap(goodsDynamicFieldMap);

            if (!CollectionUtils.isEmpty(fieldManagementVoList)) {
                fieldManagementVoList.forEach(m -> {
                    if ("SSLevel".equals(m.getFieldName()) || "StyleFabricCycle".equals(m.getFieldName()) || "StyleProcessingCycle".equals(m.getFieldName())
                            || "StylePursuit".equals(m.getFieldName()) || "StyleRegion".equals(m.getFieldName())
                            || "StyleFashion".equals(m.getFieldName()) || "styleScene".equals(m.getFieldName())  || "distributionChannel".equals(m.getFieldName())) {
                        GoodsDynamicFieldDto goodsDynamicFieldDto = BeanUtil.copyProperties(m, GoodsDynamicFieldDto.class);
                        goodsDynamicFieldDtos.add(goodsDynamicFieldDto);
                    }
                    if ("衣长分类".equals(m.getFieldName())) {
                        smpGoodsDto.setLengthRangeId(m.getVal());
                        smpGoodsDto.setLengthRangeName(m.getValName());
                    }
                    if ("衣长".equals(m.getFieldName())) {
                        smpGoodsDto.setCoatLength(m.getValName());
                    }
                    if ("腰型".equals(m.getFieldName())) {
                        smpGoodsDto.setWaistTypeId(m.getVal());
                        smpGoodsDto.setWaistTypeName(m.getValName());
                    }
                    if ("袖长".equals(m.getFieldName())) {
                        smpGoodsDto.setSleeveLengthId(m.getVal());
                        smpGoodsDto.setSleeveLengthName(m.getValName());
                    }
                    if ("袖型".equals(m.getFieldName())) {
                        smpGoodsDto.setSleeveId(m.getVal());
                        smpGoodsDto.setSleeveName(m.getValName());
                    }
                    //胸围
                    if ("Bust".equals(m.getFieldName())) {
                        smpGoodsDto.setBust(m.getVal());
                    }
                    if ("门襟".equals(m.getFieldName())) {
                        smpGoodsDto.setPlacketId(m.getVal());
                        smpGoodsDto.setPlacketName(m.getValName());
                    }
                    if ("毛纱针型".equals(m.getFieldName())) {
                        smpGoodsDto.setYarnNeedleTypeId(m.getVal());
                        smpGoodsDto.setYarnNeedleTypeName(m.getValName());
                    }
                    if ("毛纱针法".equals(m.getFieldName())) {
                        smpGoodsDto.setYarnNeedleId(m.getVal());
                        smpGoodsDto.setYarnNeedleName(m.getValName());
                    }
                    //廓形
                    if ("Profile".equals(m.getFieldName())) {
                        smpGoodsDto.setProfileId(m.getVal());
                        smpGoodsDto.setProfileName(m.getValName());
                    }
                    if ("花型".equals(m.getFieldName())) {
                        smpGoodsDto.setFlowerId(m.getVal());
                        smpGoodsDto.setFlowerName(m.getValName());
                    }
                    //领型
                    if ("Collar".equals(m.getFieldName())) {
                        smpGoodsDto.setLingXingId(m.getVal());
                        smpGoodsDto.setLingXingName(m.getValName());
                    }
                    if ("材质".equals(m.getFieldName())) {
                        smpGoodsDto.setTextureId(m.getVal());
                        smpGoodsDto.setTextureName(m.getValName());
                    }
                });
            }
            smpGoodsDto.setGoodsDynamicFieldList(goodsDynamicFieldDtos);

            //查询下单阶段动态字段  取 水洗字段和自主研发版型字段
            List<FieldVal> fvList = fieldValMap.getOrDefault(styleColor.getId(),new ArrayList<>());
            Map<String, String> oldFvMap = fvList.stream().collect(CollectorUtil.toMap(FieldVal::getFieldName, FieldVal::getVal,(a, b) -> b));
            if(oldFvMap.containsKey("plateType")){
                smpGoodsDto.setPlateType(oldFvMap.get("plateType"));
            }
            if(oldFvMap.containsKey("GarmentWash")){
                smpGoodsDto.setGarmentWash(oldFvMap.get("GarmentWash"));
            }

            //生产类型
            smpGoodsDto.setProductionType(style.getDevtType());
            smpGoodsDto.setProductionTypeName(style.getDevtTypeName());
            smpGoodsDto.setBandName(style.getBandName());
            smpGoodsDto.setAccessories("配饰".equals(style.getProdCategory1stName()));

            // 资料包
            //region 资料包取值逻辑错误
            /*PackTechPackaging packTechPackaging = packTechPackagingService.getOne(new QueryWrapper<PackTechPackaging>().eq("foreign_id", style.getId()).eq("pack_type", "packBigGoods"));
            if (packTechPackaging != null) {
                smpGoodsDto.setPackageType(packTechPackaging.getPackagingForm());
                smpGoodsDto.setPackageSize(packTechPackaging.getPackagingBagStandard());
            }*/
            //endregion

            smpGoodsDto.setProductTypeId(style.getStyleType());
            smpGoodsDto.setProductType(style.getStyleTypeName());
            smpGoodsDto.setProductName(styleColor.getProductName());
            smpGoodsDto.setSaleTime(styleColor.getNewDate());
            smpGoodsDto.setProdSeg(styleColor.getSubdivide());
            smpGoodsDto.setSizeGroupId(style.getSizeRange());
            smpGoodsDto.setSizeGroupName(style.getSizeRangeName());
            smpGoodsDto.setStyleCode(style.getDesignNo());

            //工艺说明
            long count = packTechSpecService.count(new QueryWrapper<PackTechSpec>().eq("pack_type", "packBigGoods").eq("foreign_id", style.getId()).eq("spec_type", "外辅工艺"));
            smpGoodsDto.setAuProcess(count > 0);



            smpGoodsDto.setUnit(style.getStyleUnitCode());
            String downContent = "";
            if (packInfo != null) {
                PackPricing packPricing = packPricingService.get(packInfo.getId(), "0".equals(styleColor.getBom()) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS);
                // 核价
                if (packPricing != null) {
                    JSONObject jsonObject = JSON.parseObject(packPricing.getCalcItemVal());
                    //smpGoodsDto.setCost(jsonObject.getBigDecimal("成本价") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("成本价"));
                    //车缝教加工费
                    smpGoodsDto.setLaborCosts(jsonObject.getBigDecimal("车缝加工费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("车缝加工费"));
                    //物料费
                    smpGoodsDto.setMaterialCost(jsonObject.getBigDecimal("物料费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("物料费"));
                    //总成本
                    smpGoodsDto.setCost(packPricingService.countTotalPrice(packInfo.getId(), null,2));
                    //设计Bom总成本
                    smpGoodsDto.setDesignPackCost(packPricingService.countTotalPrice(packInfo.getId(),BaseGlobal.YES,2));
                    //外协加工费
                    smpGoodsDto.setOutsourcingProcessingCost((jsonObject.getBigDecimal("外协加工费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("外协加工费")));
                    //包装费
                    smpGoodsDto.setPackagingCost((jsonObject.getBigDecimal("包装费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("包装费")));
                    //检测费
                    smpGoodsDto.setTestCost((jsonObject.getBigDecimal("检测费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("检测费")));
                    //毛纱加工费
                    smpGoodsDto.setSweaterProcessingCost((jsonObject.getBigDecimal("毛纱加工费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("毛纱加工费")));
                }

                //款式定价
                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), style.getCompanyCode());


                if (stylePricingVO != null) {
                    smpGoodsDto.setBomPhase("0".equals(stylePricingVO.getBomStage()) ? "Sample" : "Production");
                    smpGoodsDto.setPriceConfirm("1".equals(stylePricingVO.getProductTagPriceConfirm()));
                    smpGoodsDto.setCost(stylePricingVO.getTotalCost());
                    //计控实际成本
                    if ("1".equals(stylePricingVO.getPlanCostConfirm())) {
                        smpGoodsDto.setPlanCost(stylePricingVO.getPlanCost());
                    }

                    smpGoodsDto.setPlanningRate(stylePricingVO.getPlanningRatio());
                    try {

                        smpGoodsDto.setActualRate(stylePricingVO.getActualMagnification());

                    } catch (Exception ignored) {

                    }
                    downContent = stylePricingVO.getDownContent();
                    smpGoodsDto.setPlanActualRate(stylePricingVO.getPlanActualMagnification());
                    smpGoodsDto.setProcessCost(stylePricingVO.getCoordinationProcessingFee().add(stylePricingVO.getWoolenYarnProcessingFee()).add(stylePricingVO.getSewingProcessingFee()));
                    smpGoodsDto.setProductName(stylePricingVO.getProductName());
                    //吊牌
                    smpGoodsDto.setSeries(stylePricingVO.getSeries());
                    smpGoodsDto.setSeriesId(stylePricingVO.getSeries());
                    smpGoodsDto.setSeriesName(stylePricingVO.getSeriesName());
                    smpGoodsDto.setComposition(stylePricingVO.getIngredient());
                }
                String packType = "0".equals(styleColor.getBomStatus()) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS;
                PackBomVersion enableVersion = packBomVersionService.getEnableVersion(packInfo.getId(), packType);
                if (enableVersion != null) {
                    List<PackBom> packBoms = packBomService.list(new QueryWrapper<PackBom>().eq("bom_version_id", enableVersion.getId()));
                    //所有物料是否下发
                    boolean b =true;
                    if (packBoms != null && !packBoms.isEmpty()) {
                        for (PackBom packBom : packBoms) {
                            if ( !"1".equals(packBom.getScmSendFlag()) && !"3".equals(packBom.getScmSendFlag())) {
                                b=false;
                                break;
                            }
                        }
                    }else {
                        b=false;
                    }
                    PackInfoStatus packInfoStatus = packInfoStatusService.getOne(new QueryWrapper<PackInfoStatus>().eq("foreign_id", packInfo.getId()).eq("pack_type", packType));
                    smpGoodsDto.setIntegrityProduct("1".equals(packInfoStatus.getBulkOrderClerkConfirm()) && b);
                    smpGoodsDto.setIntegritySample(b);
                }
            }

            //废弃
            //smpGoodsDto.setSeriesId(null);
            //smpGoodsDto.setSeriesName(null);
            //smpGoodsDto.setRegion(null);
            //smpGoodsDto.setSalesGroup(null);
            List<String> sizeCodes = StringUtils.convertList(style.getSizeCodes());
            if (sizeCodes.isEmpty()){
                if(type != null && type == 1){
                    //不抛出异常  保存到msg中
                    msg.add(styleColor.getStyleNo()+"尺码不能为空");
                    continue;
                }else{
                    throw new OtherException("尺码不能为空");
                }
            }
            List<BasicsdatumSize> basicsdatumSizes = basicsdatumSizeService.listByField("code", sizeCodes);


            List<SmpSize> smpSizes = new ArrayList<>();
            for (BasicsdatumSize basicsdatumSize : basicsdatumSizes) {
                SmpSize smpSize = new SmpSize();
                smpSize.setSize(basicsdatumSize.getModel());
                smpSize.setSizeNumber(basicsdatumSize.getSort());
                smpSize.setCode(basicsdatumSize.getCode());
                smpSize.setProductSizeName(basicsdatumSize.getHangtags());
                smpSize.setBaseSize(StringUtils.isNoneBlank(style.getDefaultSize()) && style.getDefaultSize().equals(basicsdatumSize.getHangtags()));

                smpSize.setSkuFiller(downContent);
                smpSize.setSpecialSpec(basicsdatumSize.getInternalSize());
                smpSizes.add(smpSize);
            }
            smpGoodsDto.setItemList(smpSizes);

            //region 添加配色指定面料下发 huangqiang
            smpGoodsDto.setStyleSpecFabricList(styleSpecFabricMap.getOrDefault(styleColor.getId(),new ArrayList<>()));
            //endregion


            //region 增加二检包装形式
            //
            QueryWrapper<HangTag> hangTagQueryWrapper = new QueryWrapper();
            hangTagQueryWrapper.eq("bulk_style_no",styleColor.getStyleNo());
            hangTagQueryWrapper.eq("del_flag","0");
            hangTagQueryWrapper.last("limit 1");
            HangTag hangTag = hangTagService.getOne(hangTagQueryWrapper);
            if (hangTag != null) {
                smpGoodsDto.setSecondPackagingForm(hangTag.getSecondPackagingForm());
                smpGoodsDto.setSecondPackagingFormCode(hangTag.getSecondPackagingFormCode());

                smpGoodsDto.setPackageType(hangTag.getPackagingFormCode());
                smpGoodsDto.setPackageSize(hangTag.getPackagingBagStandardCode());

                //增加吊牌成分明细数据，用于易尚货
                String hangTagId = hangTag.getId();
                QueryWrapper<HangTagIngredient> hangTagIngredientQueryWrapper = new QueryWrapper<>();
                hangTagIngredientQueryWrapper.eq("hang_tag_id",hangTagId);
                hangTagIngredientQueryWrapper.eq("del_flag","0");
                hangTagIngredientQueryWrapper.select("type, ingredient_second_name, percentage_str, ingredient_name, ingredient_description");
                List<HangTagIngredient> hangTagIngredients = hangTagIngredientService.list(hangTagIngredientQueryWrapper);
                List<SmpHangTagIngredientDTO> smpHangTagIngredientDTOList = new ArrayList();
                if (CollUtil.isNotEmpty(hangTagIngredients)) {
                    for (HangTagIngredient hangTagIngredient : hangTagIngredients) {
                        SmpHangTagIngredientDTO hangTagIngredientDTO = BeanUtil.copyProperties(hangTagIngredient, SmpHangTagIngredientDTO.class);
                        smpHangTagIngredientDTOList.add(hangTagIngredientDTO);
                    }
                }
                smpGoodsDto.setHangTagIngredientList(smpHangTagIngredientDTOList);

            }
            //endregion

            // if (true){
            //     return null;
            // }
            String jsonString = JsonStringUtils.toJSONString(smpGoodsDto);
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/goods", jsonString,
                    Pair.of("moduleName","smp"),
                    Pair.of("functionName","商品主数据下发"),
                    Pair.of("code",smpGoodsDto.getCode()),
                    Pair.of("name",smpGoodsDto.getColorName()),
                    Pair.of("businessId",smpGoodsDto.getId()),
                    Pair.of("businessCode",smpGoodsDto.getTargetBusinessSystem())
            );
            if (httpResp.isSuccess()) {
                i++;
                styleColor.setScmSendFlag("1");
            } else {
                styleColor.setScmSendFlag("2");
            }
            styleColorService.updateById(styleColor);
        }
        return i;
    }

    /**
     * 物料主数据下发
     */
    public Integer materials(String[] ids) {
        if (ids.length == 0) {
            return 0;
        }
        List<BasicsdatumMaterial> list = basicsdatumMaterialService.listByIds(Arrays.asList(ids));
        int i = 0;
        for (BasicsdatumMaterial basicsdatumMaterial : list) {
            if (this.sendMaterials(basicsdatumMaterial)) {
                i++;
            }
        }

        return i;
    }

    public Boolean sendMaterials(BasicsdatumMaterial basicsdatumMaterial) {
        TransactionStatus transactionStatus = null;
        try {
            SmpMaterialDto smpMaterialDto = basicsdatumMaterial.toSmpMaterialDto();

            List<FieldManagementVo> fieldManagementVos = basicsdatumMaterialService.queryCoefficient(BeanUtil.copyProperties(basicsdatumMaterial, BasicsdatumMaterialPageVo.class));
            smpMaterialDto.setDynamicFieldList(fieldManagementVos);

            //获取颜色集合
            BasicsdatumMaterialColorQueryDto basicsdatumMaterialColorQueryDto = new BasicsdatumMaterialColorQueryDto();
            basicsdatumMaterialColorQueryDto.setMaterialCode(basicsdatumMaterial.getMaterialCode());
            List<BasicsdatumMaterialColorPageVo> list1 = basicsdatumMaterialService.getBasicsdatumMaterialColorList(basicsdatumMaterialColorQueryDto).getList();
            List<SmpColor> colorList = new ArrayList<>();
            for (BasicsdatumMaterialColorPageVo basicsdatumMaterialColorPageVo : list1) {
                SmpColor smpColor = new SmpColor();
                smpColor.setColorCode(basicsdatumMaterialColorPageVo.getColorCode());
                smpColor.setColorName(basicsdatumMaterialColorPageVo.getColorName());
                smpColor.setActive("0".equals(basicsdatumMaterialColorPageVo.getStatus()));
                smpColor.setSupplierMatColor(basicsdatumMaterialColorPageVo.getSupplierColorCode());
                colorList.add(smpColor);
            }
            if (colorList.size() == 0) {
                throw new OtherException("颜色不能为空");
            }
            smpMaterialDto.setColorList(colorList);

            //修改颜色为下发状态
            List<String> collect = list1.stream().map(BasicsdatumMaterialColorPageVo::getId).collect(Collectors.toList());
            UpdateWrapper<BasicsdatumMaterialColor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("scm_status", "1").in("id", collect);
            basicsdatumMaterialColorService.update(updateWrapper);


            //获取材料尺码集合
            QueryWrapper<BasicsdatumMaterialWidth> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("material_code", basicsdatumMaterial.getMaterialCode());
            queryWrapper1.eq("company_code", userUtils.getCompanyCode());
            List<SmpModuleSize> moduleSizeList = new ArrayList<>();
            List<BasicsdatumMaterialWidth> list2 = basicsdatumMaterialWidthService.list(queryWrapper1);
            for (BasicsdatumMaterialWidth basicsdatumMaterialWidth : list2) {
                SmpModuleSize smpModuleSize = new SmpModuleSize();
                smpModuleSize.setActive("0".equals(basicsdatumMaterialWidth.getStatus()));
                smpModuleSize.setSizeCode(basicsdatumMaterialWidth.getName());
                smpModuleSize.setSizeUrl(basicsdatumMaterialWidth.getWidthCode());

                smpModuleSize.setSizeName(basicsdatumMaterialWidth.getSizeName());
                smpModuleSize.setSortCode(basicsdatumMaterialWidth.getSortCode());
                smpModuleSize.setCode(basicsdatumMaterialWidth.getCode());
                moduleSizeList.add(smpModuleSize);
            }


            if (moduleSizeList.size() == 0) {
                throw new OtherException("规格不能为空");
            }
            smpMaterialDto.setModuleSizeList(moduleSizeList);

            //修改规格为下发状态
            List<String> collect1 = list2.stream().map(BasicsdatumMaterialWidth::getId).collect(Collectors.toList());
            UpdateWrapper<BasicsdatumMaterialWidth> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.set("scm_status", "1").in("id", collect1);
            basicsdatumMaterialWidthService.update(updateWrapper1);

            //获取报价集合

            BasicsdatumMaterialPriceQueryDto dto = new BasicsdatumMaterialPriceQueryDto();
            dto.setMaterialCode(basicsdatumMaterial.getMaterialCode());

            List<SmpQuot> quotList = new ArrayList<>();

            List<BasicsdatumMaterialPricePageVo> materialPricePageVoList = new ArrayList<>();
            List<BasicsdatumMaterialPricePageVo> list = basicsdatumMaterialService.getBasicsdatumMaterialPriceList(dto).getList();
            List<String> priceIds = list.stream().map(BasicsdatumMaterialPricePageVo::getId).collect(Collectors.toList());
            //拆分sku
            for (BasicsdatumMaterialPricePageVo basicsdatumMaterialPricePageVo : list) {
                String[] widths = basicsdatumMaterialPricePageVo.getWidth().split(",");
                String[] colors = basicsdatumMaterialPricePageVo.getColor().split(",");

                for (String color : colors) {
                    for (String width : widths) {
                        BasicsdatumMaterialPricePageVo basicsdatumMaterialPricePageVo1 = new BasicsdatumMaterialPricePageVo();
                        BeanUtil.copyProperties(basicsdatumMaterialPricePageVo, basicsdatumMaterialPricePageVo1);
                        basicsdatumMaterialPricePageVo1.setColor(color);
                        basicsdatumMaterialPricePageVo1.setWidth(width);
                        try {
                            for (SmpColor smpColor : colorList) {
                                if (color.equals(smpColor.getColorCode())) {
                                    basicsdatumMaterialPricePageVo1.setColorName(smpColor.getColorName());
                                }
                            }
                            for (SmpModuleSize smpModuleSize : moduleSizeList) {
                                if (width.equals(smpModuleSize.getSizeUrl())) {
                                    basicsdatumMaterialPricePageVo1.setWidthName(smpModuleSize.getSizeCode());
                                }
                            }

                        } catch (Exception ignored) {

                        }
                        materialPricePageVoList.add(basicsdatumMaterialPricePageVo1);
                    }
                }
            }

            for (BasicsdatumMaterialPricePageVo basicsdatumMaterialPricePageVo : materialPricePageVoList) {
                SmpQuot smpQuot = new SmpQuot();
                smpQuot.setSupplierSize(basicsdatumMaterialPricePageVo.getWidthName().replaceAll("<->", ","));
                smpQuot.setSizeUrl(basicsdatumMaterialPricePageVo.getWidth());
                smpQuot.setSupplierColorId(basicsdatumMaterialPricePageVo.getColor());
                smpQuot.setSupplierColorName(basicsdatumMaterialPricePageVo.getColorName());
                smpQuot.setOrderGoodsDay(basicsdatumMaterialPricePageVo.getOrderDay());
                smpQuot.setProductionDay(basicsdatumMaterialPricePageVo.getProductionDay());
                smpQuot.setMoqInitial(basicsdatumMaterialPricePageVo.getMinimumOrderQuantity());
                smpQuot.setFobFullPrice(basicsdatumMaterialPricePageVo.getQuotationPrice());
                smpQuot.setSupplierMaterial(basicsdatumMaterialPricePageVo.getSupplierMaterialCode());
                smpQuot.setSupplierCode(basicsdatumMaterialPricePageVo.getSupplierId());
                smpQuot.setSupplierName(basicsdatumMaterialPricePageVo.getSupplierName());
                smpQuot.setComment(basicsdatumMaterialPricePageVo.getRemarks());
                smpQuot.setTradeTermKey(null);
                smpQuot.setTradeTermName(null);
                smpQuot.setDefaultQuote(basicsdatumMaterialPricePageVo.getSelectFlag());


                smpQuot.setMaterialUom(basicsdatumMaterial.getStockUnitCode());
                quotList.add(smpQuot);
            }
            if (quotList.size() == 0) {
                throw new OtherException("报价不能为空");
            }
            smpMaterialDto.setQuotList(quotList);

            String jsonString = JsonStringUtils.toJSONString(smpMaterialDto);
            //获取事务
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);

            //下发并记录推送日志
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/materials", jsonString,
                    Pair.of("moduleName","smp"),
                    Pair.of("functionName","物料主数据下发")
            );

            //修改状态为已下发
            basicsdatumMaterial.setDistribute("1");
            basicsdatumMaterialService.updateById(basicsdatumMaterial);
            basicsdatumMaterialPriceService.update(new UpdateWrapper<BasicsdatumMaterialPrice>().set("scm_status", "1").in("id", priceIds));
            //提交事务
            dataSourceTransactionManager.commit(transactionStatus);
        } catch (OtherException otherException) {
            if (transactionStatus != null) {
                //回滚事务
                dataSourceTransactionManager.rollback(transactionStatus);
            }
            throw new OtherException(otherException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            if (transactionStatus != null) {
                //回滚事务
                dataSourceTransactionManager.rollback(transactionStatus);
            }
            return false;
        }

        return true;
    }

    /**
     * bom下发
     */
    public Integer bom(String[] ids) {

        int i = 0;
        List<PackBom> list = packBomService.listByIds(Arrays.asList(ids));
        StyleColor styleColor = null;
        /*过滤已下发的数据*/
        list = list.stream().filter(p -> !StringUtils.equals(p.getScmSendFlag(),BaseGlobal.STATUS_CLOSE)).collect(Collectors.toList());
        if(CollUtil.isEmpty(list)){
            throw new OtherException("已下发数据不在重复下发");
        }
        List<String> materialCodeList = list.stream().map(PackBom::getMaterialCode).distinct().collect(Collectors.toList());
        List<BasicsdatumMaterial> materialList = basicsdatumMaterialService.listByField("material_code", materialCodeList);
        Map<String, BasicsdatumMaterial> materialMap = materialList.stream().collect(Collectors.toMap(BasicsdatumMaterial::getMaterialCode,o->o,(v1,v2)->v1));
        for (PackBom packBom : list) {
            //验证如果是大货类型则判断大货用量是否为空或者0
            if (PackUtils.PACK_TYPE_BIG_GOODS.equals(packBom.getPackType())) {
                if (packBom.getBulkUnitUse() == null || packBom.getBulkUnitUse().compareTo(BigDecimal.ZERO) == 0) {
                    throw new OtherException(packBom.getMaterialName() + "大货用量不能为空或者0");
                }
            }else {
                //验证如果是设计类型则判断设计用量是否为空或者0
                if (packBom.getDesignUnitUse() == null || packBom.getDesignUnitUse().compareTo(BigDecimal.ZERO) == 0) {
                    throw new OtherException(packBom.getMaterialName() + "设计用量不能为空或者0");
                }
            }
            //判断行Id是否为空
            if (StringUtils.isEmpty(packBom.getCode())) {
                throw new OtherException(packBom.getMaterialName() +":"+packBom.getMaterialCode()+ "行Id不能为空,请联系运维人员");
            }
            //旧数据不允许下发
//            if ("1".equals(packBom.getHistoricalData())) {
//                throw new OtherException(packBom.getMaterialName() + "为旧数据,不允许下发");
//            }
            //如果是主面料,则判断是否和配色颜色一致 ,已改为前端提示,不做必须校验
            // if ("1".equals(packBom.getMainFlag())){
            //     PackInfo packInfo = packInfoService.getById(packBom.getForeignId());
            //     //样衣-款式配色
            //     styleColor = styleColorService.getById(packInfo.getStyleColorId());
            //     if (styleColor == null) {
            //         throw new OtherException(packBom.getMaterialName()+":未关联配色,无法下发");
            //     }
            //     if(!styleColor.getColorCode().equals(packBom.getColorCode())){
            //         throw new OtherException(packBom.getMaterialName()+":主面料颜色和配色颜色不一致,无法下发");
            //     }
            // }

            //校验颜色是否存在
            BasicsdatumMaterialColorQueryDto queryDto = new BasicsdatumMaterialColorQueryDto();
            queryDto.setMaterialCode(packBom.getMaterialCode());
            PageInfo<BasicsdatumMaterialColorPageVo> basicsdatumMaterialColorList = basicsdatumMaterialService.getBasicsdatumMaterialColorList(queryDto);
            if (CollUtil.isEmpty(basicsdatumMaterialColorList.getList())) {
                throw new OtherException(packBom.getMaterialCode() + "_" + packBom.getMaterialName() + " 没有找到颜色信息");
            } else {
                long count = basicsdatumMaterialColorList.getList().stream().filter(o -> o.getColorCode().equals(packBom.getColorCode())).count();
                if (count == 0) {
                    throw new OtherException(packBom.getMaterialCode() + "_" + packBom.getMaterialName() + " 没有找到 " + packBom.getColorCode() + packBom.getColor() + "颜色信息");
                }
            }

            //校验规格是否存在
            BasicsdatumMaterialWidthQueryDto queryDto1 = new BasicsdatumMaterialWidthQueryDto();
            queryDto1.setMaterialCode(packBom.getMaterialCode());
            PageInfo<BasicsdatumMaterialWidthPageVo> basicsdatumMaterialWidthList = basicsdatumMaterialService.getBasicsdatumMaterialWidthList(queryDto1);
            if (CollUtil.isEmpty(basicsdatumMaterialWidthList.getList())) {
                throw new OtherException(packBom.getMaterialCode() + "_" + packBom.getMaterialName() + " 没有找到规格信息");
            } else {
                long count = basicsdatumMaterialWidthList.getList().stream().filter(o -> o.getWidthCode().equals(packBom.getTranslateCode())).count();
                if (count == 0) {
                    throw new OtherException(packBom.getMaterialCode() + "_" + packBom.getMaterialName() + " 没有找到 " + packBom.getTranslateCode() + packBom.getTranslate() + "规格信息");
                }
            }


            /*判断供应商报价是否停用
            * 1.报次款无需校验供应商停用
            * 2.停用状态下的BOM物料无需要校验供应商停用
            */
            PackInfo packInfo = packInfoService.getById(packBom.getForeignId());
            styleColor = styleColorService.getById(packInfo.getStyleColorId());
            if (styleColor == null) {
                throw new OtherException("未关联配色,无法下发");
            }
            String category2Code = "";
            if(materialMap.containsKey(packBom.getMaterialCode())){
                BasicsdatumMaterial basicsdatumMaterial = materialMap.get(packBom.getMaterialCode());
                category2Code = basicsdatumMaterial.getCategory2Code();
            }
            if (StrUtil.isEmpty(styleColor.getDefectiveNo()) && BaseGlobal.NO.equals(packBom.getUnusableFlag()) && !"FB".equals(category2Code)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("is_supplier",BaseGlobal.YES);
                queryWrapper.eq("supplier_code",packBom.getSupplierId());
                List<BasicsdatumSupplier> basicsdatumSupplierList = basicsdatumSupplierService.list(queryWrapper);

                if (CollUtil.isNotEmpty(basicsdatumSupplierList)) {
                    String collect = basicsdatumSupplierList.stream().map(BasicsdatumSupplier::getSupplier).collect(Collectors.joining(","));
                    throw new OtherException(packBom.getMaterialCode()+"_"+packBom.getMaterialName()+" "+collect+"供应商已停用");
                }
            }

            packBomVersionService.checkBomDataEmptyThrowException(packBom);
        }

        //bom主表
        List<PackInfo> packInfoList = packInfoService.listByIds(list.stream().map(PackBom::getForeignId).collect(Collectors.toList()));

        List<BomSelMaterialVo> defaultToBomSel = basicsdatumMaterialWidthService.findDefaultToBomSel(materialCodeList);
        List<String> materialWidthList = defaultToBomSel.stream().map(o -> o.getMaterialCode() + "_" + o.getTranslate() + "_" + o.getTranslateCode()).collect(Collectors.toList());

        for (PackBom packBom : list) {
            /*判断物料是否是代用材料的编码
            * 代用材料编码：0000*/
            if("0000".equals(packBom.getMaterialCode())){
                throw new OtherException(packBom.getMaterialName() + "选择的是代用材料,请联系设计工艺员!代用材料不允许下发");
            }


            //bom主表
            PackInfo packInfo = packInfoList.stream().filter(it -> it.getId().equals(packBom.getForeignId())).findFirst().get();
            if (StringUtils.isEmpty(packInfo.getStyleNo())) {
                throw new OtherException(packBom.getMaterialName() + "未关联大货(配色)信息,无法下发");
            }
            Style style = styleService.getOne(new QueryWrapper<Style>().eq("id", packInfo.getForeignId()));
            StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), style.getCompanyCode());

            SmpBomDto smpBomDto = packBom.toSmpBomDto(stylePricingVO.getBomStage());
            //"0".equals(stylePricingVO.getBomStage()) ? "Sample" : "Production"
            //样衣-款式配色
             styleColor = styleColorService.getById(packInfo.getStyleColorId());
            if (styleColor == null) {
                throw new OtherException("未关联配色,无法下发");
            }
            /*判断配送是否下发*/
            if(!styleColor.getScmSendFlag().equals(BaseGlobal.STATUS_CLOSE)){
                throw new OtherException("请下发关联的配色");
            }
            smpBomDto.setPColorCode(styleColor.getColorCode());
            smpBomDto.setPColorName(styleColor.getColorName());
            smpBomDto.setBulkNumber(packInfo.getStyleNo());
            smpBomDto.setBomCode(packInfo.getCode());
            smpBomDto.setCode(packInfo.getCode());
            //List<BomMaterial> bomMaterials = new ArrayList<>();
            //
            //BomMaterial bomMaterial = packBom.toBomMaterial();
            //bomMaterial.setBomId(packInfo.getCode());
            //bomMaterials.add(bomMaterial);
            //smpBomDto.setBomMaterials(bomMaterials);

            List<SmpSizeQty> sizeQtyList = new ArrayList<>();
            for (PackBomSize packBomSize : packBomSizeService.list(new QueryWrapper<PackBomSize>().eq("bom_id", packBom.getId()).eq("bom_version_id",packBom.getBomVersionId()))) {
                packBomVersionService.checkBomSizeDataEmptyThrowException(packBomSize);
                SmpSizeQty smpSizeQty = packBomSize.toSmpSizeQty();
                //根据尺码id查询尺码
                BasicsdatumSize basicsdatumSize = basicsdatumSizeService.getById(packBomSize.getSizeId());
                if (basicsdatumSize != null) {
                    smpSizeQty.setPSizeCode(basicsdatumSize.getCode());
                    smpSizeQty.setItemSize(basicsdatumSize.getInternalSize());

                    sizeQtyList.add(smpSizeQty);
                }
                //校验物料规格 是否存在于t_basicsdatum_material_width表中
                if (!materialWidthList.contains(packBom.getMaterialCode() + "_" + packBomSize.getWidth() + "_" + packBomSize.getWidthCode())) {
                    throw new OtherException("物料：" + packBom.getMaterialCodeName() + ",门幅/规格：" + packBomSize.getSize() + "不存在");
                }
            }
            if (sizeQtyList.isEmpty()){
                throw new OtherException("尺码信息为空");
            }
            if (StringUtils.isEmpty(smpBomDto.getColorName()) || StringUtils.isEmpty(smpBomDto.getColorCode())){
                throw new OtherException("颜色信息为空");
            }
            smpBomDto.setSizeQtyList(sizeQtyList);
            smpBomDto.setRfidFlag(styleColor.getRfidFlag());
            String category3Code = basicsdatumMaterialService.findOneField(new LambdaQueryWrapper<BasicsdatumMaterial>()
                    .eq(BasicsdatumMaterial::getMaterialCode, smpBomDto.getMaterialCode()), BasicsdatumMaterial::getCategory3Code);
            RFIDProperties.categoryRfidMapping.forEach((categoryCode, RFIDType)-> {
                if (categoryCode.equals(category3Code)) {
                    smpBomDto.setRfidType(RFIDType.ordinal() + "");
                }
            });

            /*如果物料是未下发状态或者是发送失败 就是新增的物料*/
            if (StrUtil.equals(packBom.getScmSendFlag(), BaseGlobal.NO) || StrUtil.equals(packBom.getScmSendFlag(), BaseGlobal.STOCK_STATUS_CHECKED)) {
                packBomService.costUpdate(list.get(0).getForeignId(), null);
            }

            String jsonString = JsonStringUtils.toJSONString(smpBomDto);
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/bom", jsonString,
                    Pair.of("moduleName","smp"),
                    Pair.of("functionName","bom下发")
            );

            if (httpResp.isSuccess()) {
                packBom.setScmSendFlag("1");
                i++;
            } else {
                packBom.setScmSendFlag("2");
            }

            //PackInfoStatus packInfoStatus = packInfoStatusService.getOne(new QueryWrapper<PackInfoStatus>().eq("foreign_id", packInfo.getId()).eq("pack_type", packBom.getPackType()));
            //long count = packBomService.count(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()).eq("scm_send_flag", "1"));
            //
            //long count1 = packBomService.count(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()));
            //if (count == 0) {
            //    packInfoStatus.setScmSendFlag("0");
            //} else if (count1 == count) {
            //    packInfoStatus.setScmSendFlag("1");
            //} else {
            //    packInfoStatus.setScmSendFlag("2");
            //}
            //packInfoStatusService.updateById(packInfoStatus);
        }
        packBomService.updateBatchById(list);

        if (styleColor != null) {
            this.goods(new String[]{styleColor.getId()});
        }

        String value = list.stream().map(PackBom::getMaterialCodeName).collect(Collectors.joining("\n"));
        String packType = list.stream().map(PackBom::getPackType).distinct().collect(Collectors.joining(COMMA));
        // 3093 添加日志
        packInfoList.stream().findFirst().ifPresent(packInfo-> {
            com.alibaba.fastjson2.JSONArray jsonArray = new com.alibaba.fastjson2.JSONArray();
            OperaLogEntity log = new OperaLogEntity();
            log.setType("下发同步");
            log.setName("物料清单");
            log.setPath(packType);
            log.setParentId(packInfo.getForeignId());
            com.alibaba.fastjson2.JSONObject jsonObject = new com.alibaba.fastjson2.JSONObject();
            jsonObject.put("name", "物料名称");
            jsonObject.put("oldStr", "");
            jsonObject.put("newStr", value);
            jsonArray.add(jsonObject);
            log.setJsonContent(jsonArray.toJSONString());
            log.setPath(packType);
            packInfoService.saveOperaLog(log);
        });

        return i;
    }

    /**
     * 颜色主数据下发
     */
    public Integer color(String[] ids) {
        if (ids.length == 0) {
            return 0;
        }
        List<BasicsdatumColourLibrary> basicsdatumColourLibraries = basicsdatumColourLibraryService.list(new QueryWrapper<BasicsdatumColourLibrary>().in("id", Arrays.asList(ids)));
        Boolean issuedToExternalSmpSystemSwitch = ccmFeignService.getSwitchByCode(ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH.getKeyCode());

        int i = 0;
        for (BasicsdatumColourLibrary basicsdatumColourLibrary : basicsdatumColourLibraries) {
            if (issuedToExternalSmpSystemSwitch) {
                SmpColorDto smpColorDto = basicsdatumColourLibrary.toSmpColorDto();
                String jsonString = JsonStringUtils.toJSONString(smpColorDto);
                HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/color", jsonString,
                        Pair.of("moduleName","smp"),
                        Pair.of("functionName","颜色主数据下发")
                );

                if (httpResp.isSuccess()) {
                    i++;
                    basicsdatumColourLibrary.setScmSendFlag("1");
                } else {
                    basicsdatumColourLibrary.setScmSendFlag("2");
                }
            } else {
                basicsdatumColourLibrary.setScmSendFlag("1");
            }

            basicsdatumColourLibraryService.updateById(basicsdatumColourLibrary);
        }
        return i;
    }

    /**
     * 工艺单下发
     */
    public Integer processSheet(List<SmpProcessSheetDto> sheetDtoList) {
        int i = 0;
        IdGen idGen = new IdGen();
        for (SmpProcessSheetDto smpProcessSheetDto : sheetDtoList) {
            String id = String.valueOf(idGen.nextId());
            PackInfoStatus packInfoStatus = packInfoStatusService.get(smpProcessSheetDto.getForeignId(), smpProcessSheetDto.getPackType());
            smpProcessSheetDto.setCode(smpProcessSheetDto.getBulkNumber());
            smpProcessSheetDto.setSyncId(id);
            CommonUtils.removeQuery(smpProcessSheetDto, "pdfUrl");
            smpProcessSheetDto.setId(id);
            String jsonString = JsonStringUtils.toJSONString(smpProcessSheetDto);
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/processSheet", jsonString,
                    Pair.of("moduleName","smp"),
                    Pair.of("functionName","工艺单下发")
            );
            if (httpResp.isSuccess()) {
                i++;
                packInfoStatus.setTechScmSendFlag("1");
            } else {
                packInfoStatus.setTechScmSendFlag("2");
            }
            packInfoStatusService.updateById(packInfoStatus);
        }
        return i;
    }

    /**
     * 样衣下发,打板管理
     */
    public Integer sample(String[] ids, String env) {
        if (ids.length == 0 ||  !StrUtil.equals(env, "yfg")) {
            return 0;
        }
        int i = 0;
        for (PatternMaking patternMaking : patternMakingService.listByIds(Arrays.asList(ids))) {
            if ("1".equals(patternMaking.getHistoricalData())){
                return 0;
            }

            Style style = styleService.getById(patternMaking.getStyleId());
            SmpSampleDto smpSampleDto = style.toSmpSampleDto();
            //QueryWrapper<Sample> queryWrapper = new QueryWrapper<Sample>().eq("pattern_making_id", patternMaking.getId());
            //queryWrapper.orderByAsc("update_date");
            //queryWrapper.last("limit 1");
            //Sample sample = sampleService.getOne(queryWrapper);
            //if (sample == null) {
            //    throw new OtherException("请先在样衣编辑界面绑定打板指令");
            //}
            try {
                UploadFile uploadFile = uploadFileService.getById(style.getStylePic());
                smpSampleDto.setImgList(Arrays.asList(uploadFile.getUrl().split(",")));
            } catch (Exception ignored) {
            }

            smpSampleDto.setSampleType(patternMaking.getSampleType());
            smpSampleDto.setSampleTypeName(patternMaking.getSampleTypeName());

            //取跟款设计师，如果跟款设计师不存在就取设计师
            smpSampleDto.setProofingDesigner(style.getMerchDesignName() == null ? style.getDesigner() : style.getMerchDesignName());
            smpSampleDto.setPatDiff(style.getPatDiff());
            smpSampleDto.setPatDiffName(style.getPatDiffName());
            smpSampleDto.setSampleNumber(patternMaking.getCode());
            // smpSampleDto.setColorwayCode(style.getStyleNo());
            smpSampleDto.setCode(patternMaking.getCode());
            // smpSampleDto.setColorwayPlmId(style.getStyleNo());
            smpSampleDto.setSampleStatus(style.getStatus());
            smpSampleDto.setSampleStatusName("0".equals(style.getStatus()) ? "未开款" : "1".equals(style.getStatus()) ? "已开款" : "已下发打板(完成)");
            smpSampleDto.setBrandCode(style.getBrand());
            smpSampleDto.setBrandName(style.getBrandName());

            String designerId = style.getDesignerId();
            String technicianId = style.getTechnicianId();
            String patternDesignId = style.getPatternDesignId();
            smpSampleDto.setYear(style.getYear());
            smpSampleDto.setDesigner(style.getDesigner());
            smpSampleDto.setTechnician(style.getTechnicianName());
            smpSampleDto.setStyleUrl(style.getId());


            smpSampleDto.setNodeName(style.getStyleName());


            String merchDesignId = style.getMerchDesignId();
            if (merchDesignId == null) {
                merchDesignId = designerId;
            }
            ArrayList<String> list = new ArrayList<>();
            list.add(designerId);
            list.add(technicianId);
            list.add(patternDesignId);

            list.add(merchDesignId);

            Map<String, String> usernamesByIds = amcService.getUsernamesByIds(StringUtils.join(list, ","));
            smpSampleDto.setDesignerId(usernamesByIds.get(designerId));
            smpSampleDto.setTechnicianId(usernamesByIds.get(technicianId));
            smpSampleDto.setPatternMakerId(usernamesByIds.get(patternDesignId));
            smpSampleDto.setProofingDesignerId(usernamesByIds.get(merchDesignId));

            smpSampleDto.setSupplier(patternMaking.getPatternRoom());

            //写死
            smpSampleDto.setSupplierNumber("99CY0017");

            smpSampleDto.setPatSeqName(patternMaking.getPatSeqName());
            smpSampleDto.setPatSeq(patternMaking.getPatSeq());
            smpSampleDto.setEAValidFromTime(patternMaking.getExtAuxiliaryDispatchDate());
            smpSampleDto.setEAValidToTime(patternMaking.getExtAuxiliaryReceiveDate());
            smpSampleDto.setFinished("1".equals(patternMaking.getEndFlg()));
            smpSampleDto.setMCDate(patternMaking.getSglKittingDate());
            smpSampleDto.setPmlId(patternMaking.getId());
            smpSampleDto.setBExtAuxiliary("1".equals(patternMaking.getExtAuxiliary()));
            smpSampleDto.setSampleNumberName(patternMaking.getCode());
            smpSampleDto.setBarcode(patternMaking.getSampleBarCode());
            SampleBean sampleBean = smpSampleDto.toSampleBean();
            sampleBean.setColorway(new SampleBean.Colorway());
            sampleBean.setC8_ProductSample_ProofingDesigner(patternMaking.getPatternDesignerName());
            sampleBean.setC8_ProductSample_ProofingDesignerID(patternMaking.getPatternDesignerId());
            String jsonString = JsonStringUtils.toJSONString(sampleBean);
            HttpResp httpResp = restTemplateService.spmPost(OA_URL + "/setSampleTask", jsonString,
                    Pair.of("moduleName","oa"),
                    Pair.of("functionName","样衣下发")
            );

            if (httpResp.isSuccess()) {
                i++;
                patternMaking.setScmSendFlag("1");
            } else {
                patternMaking.setScmSendFlag("2");
            }
            patternMakingService.updateById(patternMaking);
        }
        return i;
    }

    /**
     * 面料成分名称码表下发
     */
    public Integer fabricComposition(String[] ids) {
        int i = 0;
        for (BasicsdatumIngredient basicsdatumIngredient : basicsdatumIngredientService.listByIds(Arrays.asList(ids))) {
            FabricCompositionDto fabricCompositionDto = new FabricCompositionDto();
            fabricCompositionDto.setName(basicsdatumIngredient.getIngredient());
            fabricCompositionDto.setCode(basicsdatumIngredient.getCode());
            fabricCompositionDto.setId(fabricCompositionDto.getId());
            fabricCompositionDto.setIngredient(basicsdatumIngredient.getIngredient());
            fabricCompositionDto.setStatus(basicsdatumIngredient.getStatus());
            String jsonString = JsonStringUtils.toJSONString(fabricCompositionDto);
            HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/materialElement", jsonString,
                    Pair.of("moduleName","scm"),
                    Pair.of("functionName","面料成分名称码表下发")
            );
            if (!httpResp.isSuccess()) {
                throw new OtherException(httpResp.getMessage());
            }
            i++;
            basicsdatumIngredient.setScmSendFlag("1");
            basicsdatumIngredientService.updateById(basicsdatumIngredient);
        }
        return i;
    }

    /**
     * 修改商品尺码的时候验证
     */
    public Boolean checkStyleSize(PlmStyleSizeParam param) {
        param.setCode(param.getStyleNo());
        String jsonString = JsonStringUtils.toJSONString(param);
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkStyleGroup", jsonString,
                Pair.of("moduleName","scm"),
                Pair.of("functionName","修改尺码的时候验证")
        );
        return httpResp.isSuccess();
    }

    /**
     * 修改商品颜色的时候验证
     */
    public Boolean checkColorSize(PdmStyleCheckParam param) {
        String jsonString = JsonStringUtils.toJSONString(param);
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkColorSize", jsonString,
                Pair.of("moduleName","scm"),
                Pair.of("functionName","修改商品颜色的时候验证")
        );
        return httpResp.isSuccess();
    }

    /**
     * 停用物料的规格和颜色的时候校验
     */
    public Boolean checkSizeAndColor(String materialCode, String type, String code) {
        CheckMaterial checkMaterial = new CheckMaterial();
        List<CheckMaterial.CheckSku> checkSkuList = new ArrayList<>();
        checkMaterial.setMaterialCode(materialCode);
        checkMaterial.setCode(materialCode);
        checkMaterial.setType(type);

        if ("1".equals(type)) {
            for (BasicsdatumMaterialColor basicsdatumMaterialColor : basicsdatumMaterialColorService.list(new QueryWrapper<BasicsdatumMaterialColor>().eq("material_code", materialCode))) {
                CheckMaterial.CheckSku checkSku = new CheckMaterial.CheckSku();
                checkSku.setColorCode(basicsdatumMaterialColor.getColorCode());
                checkSku.setSizeCode(code);
                checkSkuList.add(checkSku);
            }
            checkMaterial.setCheckSkuList(checkSkuList);
        }
        if ("2".equals(type)) {
            for (BasicsdatumMaterialWidth basicsdatumMaterialWidth : basicsdatumMaterialWidthService.list(new QueryWrapper<BasicsdatumMaterialWidth>().eq("material_code", materialCode))) {
                CheckMaterial.CheckSku checkSku = new CheckMaterial.CheckSku();
                checkSku.setColorCode(code);
                checkSku.setSizeCode(basicsdatumMaterialWidth.getWidthCode());
                checkSkuList.add(checkSku);
            }
            checkMaterial.setCheckSkuList(checkSkuList);
        }
        String jsonString = JsonStringUtils.toJSONString(checkMaterial);
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkMaterialsStopAndStock", jsonString,
                Pair.of("moduleName","scm"),
                Pair.of("functionName","停用物料尺码和颜色的时候验证")
        );
        if (!httpResp.isSuccess()) {
            throw new OtherException(httpResp.getMessage());
        }
        return true;

    }


    /**
     * 修改吊牌价的时候验证(暂不需要)
     */
    public Boolean checkUpdatePrice(UpdatePriceDto updatePriceDto) {
        String jsonString = JsonStringUtils.toJSONString(updatePriceDto);
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkUpdatePrice", jsonString,
                Pair.of("moduleName","smp"),
                Pair.of("functionName","修改吊牌价的时候验证")
        );
        return httpResp.isSuccess();
    }

    /**
     * 产前样下发
     *
     * @param ids 产前样任务id集合
     * @return 下发成功的数量
     */
    public Integer antenatalSample(String[] ids) {
        int i = 0;
        IdGen idGen = new IdGen();
        for (PreProductionSampleTask preProductionSampleTask : preProductionSampleTaskService.listByIds(Arrays.asList(ids))) {
            if ("1".equals(preProductionSampleTask.getHistoricalData())) {
                return 0;
            }

            Style style = styleService.getById(preProductionSampleTask.getStyleId());
            SmpSampleDto smpSampleDto = new SmpSampleDto();
            // 跟款设计师
            smpSampleDto.setProofingDesigner(style.getMerchDesignName() == null ? style.getDesigner() : style.getMerchDesignName());
            // 跟款设计师工号
            smpSampleDto.setProofingDesignerId(style.getMerchDesignId() == null ? style.getDesignerId() : style.getMerchDesignId());

            // 样衣编号
            smpSampleDto.setBarcode(preProductionSampleTask.getSampleBarCode());


            // 样衣结束标志
            smpSampleDto.setFinished("1".equals(preProductionSampleTask.getFinishFlag()));

            // 打样URL
            smpSampleDto.setPmlId(preProductionSampleTask.getId());

            // 打样顺序名称
            smpSampleDto.setPatSeqName(preProductionSampleTask.getPatSeq());
            // 版号
            smpSampleDto.setSampleNumber(preProductionSampleTask.getCode());
            // 版号名称
            smpSampleDto.setSampleNumberName(preProductionSampleTask.getCode());
            PackInfo packInfo = packInfoService.getById(preProductionSampleTask.getPackInfoId());
            StyleColor styleColor = styleColorService.getById(packInfo.getStyleColorId());
            SampleBean.Colorway colorway =new SampleBean.Colorway();
            colorway.setC8_Colorway_PLMID(styleColor.getId());
            colorway.setC8_Colorway_Code(styleColor.getStyleNo());


            // 大货款号
            smpSampleDto.setColorwayCode(styleColor.getStyleNo());
            // 大货款号唯一标识
            smpSampleDto.setColorwayPlmId(styleColor.getId());
            // 样衣名
            smpSampleDto.setNodeName(style.getStyleName());
            // 设计收样日期
            smpSampleDto.setSampleReceivedDate(style.getCreateDate());
            // 状态
            smpSampleDto.setSampleStatus(style.getStatus());
            // 状态名称
            smpSampleDto.setSampleStatusName("0".equals(style.getStatus()) ? "未开款" : "1".equals(style.getStatus()) ? "已开款" : "已下发打板(完成)");
            // 样衣类型
            smpSampleDto.setSampleType("PP样");
            // 样衣类型名称
            smpSampleDto.setSampleTypeName("产前样");
            // 大类
            smpSampleDto.setMajorCategories(style.getProdCategory1st());
            // 大类名称
            smpSampleDto.setMajorCategoriesName(style.getProdCategory1stName());
            // 品类
            smpSampleDto.setCategory(style.getProdCategory());
            // 品类名称
            smpSampleDto.setCategoryName(style.getProdCategoryName());
            // 品牌code
            smpSampleDto.setBrandCode(style.getBrand());
            // 品牌名称
            smpSampleDto.setBrandName(style.getBrandName());
            // 季节code
            smpSampleDto.setQuarterCode(style.getSeason());
            // 年份
            smpSampleDto.setYear(style.getYearName());
            // 设计师
            smpSampleDto.setDesigner(style.getDesigner());
            // 设计师工号
            smpSampleDto.setDesignerId(style.getDesignerId());
            // 版师
            smpSampleDto.setPatternMaker(style.getPatternDesignName());
            // 版师id
            smpSampleDto.setPatternMakerId(style.getPatternDesignId());
            // 工艺员
            smpSampleDto.setTechnician(preProductionSampleTask.getTechnologistName());
            // 工艺员id
            smpSampleDto.setTechnicianId(preProductionSampleTask.getTechnologistId());
            // 中类id
            smpSampleDto.setMiddleClassId(style.getProdCategory2nd());
            // 中类名称
            smpSampleDto.setMiddleClassName(style.getProdCategory2ndName());
            // 款式URL
            smpSampleDto.setStyleUrl(style.getId());
            // 设计款号
            smpSampleDto.setStyleCode(style.getDesignNo());
            // 供应商：打样部门
            smpSampleDto.setSupplier(preProductionSampleTask.getPatternRoom());
            // 打样部门编号
            smpSampleDto.setSupplierNumber(preProductionSampleTask.getPatternRoomId());
            // 图片集合
            UploadFile uploadFile = uploadFileService.getById(style.getStylePic());
            if (uploadFile != null){
                smpSampleDto.setImgList(Arrays.asList(uploadFile.getUrl().split(",")));
            }


            smpSampleDto.setId(preProductionSampleTask.getId()); // ID
            smpSampleDto.setCode(preProductionSampleTask.getCode()); // Code
            smpSampleDto.setName(null); // Name
            smpSampleDto.setCreator(preProductionSampleTask.getCreateName()); // 创建人
            smpSampleDto.setCreateTime(preProductionSampleTask.getCreateDate()); // 创建时间
            smpSampleDto.setModifiedPerson(preProductionSampleTask.getUpdateName()); // 修改人
            smpSampleDto.setModifiedTime(preProductionSampleTask.getUpdateDate()); // 修改时间
            String id = String.valueOf(idGen.nextId());
            smpSampleDto.setPlmId(preProductionSampleTask.getId()); // PLMID
            smpSampleDto.setSyncId(id); // 同步ID
            smpSampleDto.setActive(true); // 是否启用

            //样衣条码    款式id 打版编码
            PatternMaking patternMaking = patternMakingService.getOne(new QueryWrapper<PatternMaking>().eq("style_id", style.getId()).orderByDesc("create_date").last("limit 1"));
            if (patternMaking != null) {
                // 打版难度编号
                smpSampleDto.setPatDiff(patternMaking.getPatDiff());
                // 打版难度名称
                smpSampleDto.setPatDiffName(patternMaking.getPatDiffName());
                // 打样顺序编号
                smpSampleDto.setPatSeq(patternMaking.getPatSeq());
                // 外辅标志
                smpSampleDto.setBExtAuxiliary("1".equals(patternMaking.getExtAuxiliary()));
                // 外辅发出时间
                smpSampleDto.setEAValidFromTime(patternMaking.getExtAuxiliaryDispatchDate());
                // 外辅接收时间
                smpSampleDto.setEAValidToTime(patternMaking.getExtAuxiliaryReceiveDate());
            }
            // 齐套时间
            smpSampleDto.setMCDate("1".equals(preProductionSampleTask.getKitting()) ? preProductionSampleTask.getKittingTime() : null);
            SampleBean sampleBean = smpSampleDto.toSampleBean();
            sampleBean.setColorway(colorway);
            String jsonString = JsonStringUtils.toJSONString(sampleBean);
            HttpResp httpResp = restTemplateService.spmPost(OA_URL + "/setSampleTask",jsonString,
                    Pair.of("moduleName","oa"),
                    Pair.of("functionName","样衣下发")
            );
            if (httpResp.isSuccess()) {
                i++;
                preProductionSampleTask.setScmSendFlag("1");
            } else {
                preProductionSampleTask.setScmSendFlag("2");
            }
            preProductionSampleTaskService.updateById(preProductionSampleTask);
        }

        return i;
    }

    //下发吊牌成分
    public int sendTageComposition(List<String> ids){
        int i =0;
        List<HangTag> hangTags = hangTagService.listByIds(ids);
        for (HangTag hangTag : hangTags) {
            TagCompositionDto tagCompositionDto = new TagCompositionDto();
            tagCompositionDto.setComposition(hangTag.getIngredient());
            tagCompositionDto.setStyleNo(hangTag.getBulkStyleNo());
            String jsonString = JsonStringUtils.toJSONString(tagCompositionDto);
            HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/tagComposition",jsonString,
                    Pair.of("moduleName","scm"),
                    Pair.of("functionName","下发吊牌成分")
            );
            if (httpResp.isSuccess()) {
                i++;
            }

        }
        return i;
    }


    /**
     * 尺寸和外辅工艺明细数据
     *
     * @param id
     * @return
     */
    public int checkProcessSize(String id) {
        int i =0;
        BomSizeAndProcessDto bomSizeAndProcessDto = new BomSizeAndProcessDto();
        PackInfoListVo infoListVo = packInfoService.getDetail(id, PackUtils.PACK_TYPE_BIG_GOODS);
        if (ObjectUtil.isNotEmpty(infoListVo)) {
            if (StrUtil.isNotBlank(infoListVo.getStyleNo())) {
                bomSizeAndProcessDto.setStyleNo(infoListVo.getStyleNo());
                List<PackSize> packSizeList = packSizeService.list(infoListVo.getId(), PackUtils.PACK_TYPE_BIG_GOODS);
                if (CollUtil.isNotEmpty(packSizeList)) {
                    List<BomSizeAndProcessDto.BomSize> bomSizeList = new ArrayList<>();
                    for (PackSize packSize : packSizeList) {
                        if("1".equals(packSize.getStatus())){
                            continue;
                        }
                        BomSizeAndProcessDto.BomSize bomSize = new BomSizeAndProcessDto.BomSize();
                        bomSize.setId(packSize.getId());
                        bomSize.setPartName(packSize.getPartName());
                        bomSize.setMinus(packSize.getMinus());
                        bomSize.setMethod(packSize.getMethod());
                        bomSize.setPositive(packSize.getPositive());
                        bomSize.setPartCode(packSize.getPartCode());
                        bomSize.setStandard(packSize.getStandard());
                        bomSize.setSize(packSize.getSize());
                        bomSizeList.add(bomSize);
                    }
                    bomSizeAndProcessDto.setBomSizeList(bomSizeList);
                }
                List<PackTechSpec> packTechSpecList = packTechSpecService.list(infoListVo.getId(), PackUtils.PACK_TYPE_BIG_GOODS);
                //过滤外辅数据
                packTechSpecList = packTechSpecList.stream().filter(p -> StrUtil.equals(p.getSpecType(), "外辅工艺")).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(packTechSpecList)) {
                    List<BomSizeAndProcessDto.BomProcess> bomProcessList = new ArrayList<>();
                    for (PackTechSpec packSize : packTechSpecList) {
                        BomSizeAndProcessDto.BomProcess bomProcess = new BomSizeAndProcessDto.BomProcess();
                        bomProcess.setId(packSize.getId());
                        bomProcess.setItem(packSize.getItem());
                        bomProcess.setItemCode(packSize.getItemCode());
                        bomProcess.setSort(packSize.getSort());
                        bomProcess.setContent(packSize.getContent());
                        bomProcessList.add(bomProcess);
                    }
                    bomSizeAndProcessDto.setBomProcessList(bomProcessList);
                }

            }
            String jsonString = JsonStringUtils.toJSONString(bomSizeAndProcessDto);
            HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/bomSizeAndProcess", jsonString,
                    Pair.of("moduleName","scm"),
                    Pair.of("functionName","下发尺寸和外辅工艺明细数据")
            );
            if (httpResp.isSuccess()) {
                i++;
            }
        }
        return i;

    }

    /**
     * @param ids           多个吊牌信息
     * @param type          哪个阶段确认
     * @param confirmStatus 确认状态
     * @return
     */
    public void tagConfirmDates(List<String> ids, HangTagDeliverySCMStatusEnum type, Integer confirmStatus) {
        List<TagConfirmDateDto> list = new ArrayList<>();

        Date date = confirmStatus.equals(0) ? null : new Date();
        if (type.lessThan(HANG_TAG_PRICING_LINE)) {
            List<HangTag> hangTags = hangTagService.listByIds(ids);
            for (HangTag hangTag : hangTags) {
                TagConfirmDateDto tagConfirmDateDto = new TagConfirmDateDto();
                String bulkStyleNo = hangTag.getBulkStyleNo();
                if (HangTagDeliverySCMStatusEnum.TAG_LIST_CANCEL == type) {
                    //当status 等于4 待品控确认反审核只取消上一级
                    if(HangTagStatusEnum.TRANSLATE_CHECK == hangTag.getStatus()){
                        //反审
                        tagConfirmDateDto.setStyleNo(bulkStyleNo);
                        tagConfirmDateDto.setTechnicalConfirm(0);
                        tagConfirmDateDto.setTechnicalConfirmDate(null);
                        list.add(tagConfirmDateDto);
                    }else{
                        //反审
                        tagConfirmDateDto.setStyleNo(bulkStyleNo);
                        tagConfirmDateDto.setTechnologistConfirm(0);
                        tagConfirmDateDto.setTechnicalConfirm(0);
                        tagConfirmDateDto.setQualityControlConfirm(0);
                        tagConfirmDateDto.setTechnologistConfirmDate(null);
                        tagConfirmDateDto.setTechnicalConfirmDate(null);
                        tagConfirmDateDto.setQualityControlConfirmDate(null);
                        list.add(tagConfirmDateDto);
                    }
                }
                if (HangTagDeliverySCMStatusEnum.TECHNOLOGIST_CONFIRM == type) {
                    //工艺员确认
                    tagConfirmDateDto.setStyleNo(bulkStyleNo);
                    tagConfirmDateDto.setTechnologistConfirm(1);
                    tagConfirmDateDto.setTechnologistConfirmDate(date);
                    list.add(tagConfirmDateDto);
                } else if (HangTagDeliverySCMStatusEnum.TECHNICAL_CONFIRM == type) {
                    //技术确认
                    tagConfirmDateDto.setStyleNo(bulkStyleNo);
                    tagConfirmDateDto.setTechnicalConfirm(1);
                    tagConfirmDateDto.setTechnicalConfirmDate(date);
                    list.add(tagConfirmDateDto);
                } else if (HangTagDeliverySCMStatusEnum.QUALITY_CONTROL_CONFIRM == type) {
                    //品控确认
                    tagConfirmDateDto.setStyleNo(bulkStyleNo);
                    tagConfirmDateDto.setQualityControlConfirm(1);
                    tagConfirmDateDto.setQualityControlConfirmDate(date);
                    list.add(tagConfirmDateDto);
                }
            }
        } else {
            List<StylePricing> stylePricings = stylePricingService.listByIds(ids);
            List<String> packIds = stylePricings.stream().map(o -> o.getPackId()).distinct().collect(Collectors.toList());
            List<PackInfo> packInfos = packInfoService.listByIds(packIds);
            for (PackInfo packInfo : packInfos) {
                String styleNo = packInfo.getStyleNo();
                if (StringUtils.isEmpty(styleNo)){
                    continue;
                }
                TagConfirmDateDto tagConfirmDateDto = new TagConfirmDateDto();
                if (HangTagDeliverySCMStatusEnum.PLAN_COST_CONFIRM == type) {
                    //计控成本确认
                    tagConfirmDateDto.setStyleNo(styleNo);
                    tagConfirmDateDto.setPlanCostConfirm(confirmStatus);
                    tagConfirmDateDto.setPlanCostConfirmDate(date);
                    list.add(tagConfirmDateDto);
                } else if (HangTagDeliverySCMStatusEnum.PRODUCT_TAG_PRICE_CONFIRM == type) {
                    //商品吊牌确认
                    tagConfirmDateDto.setStyleNo(styleNo);
                    tagConfirmDateDto.setProductTagPriceConfirm(confirmStatus);
                    tagConfirmDateDto.setProductTagPriceConfirmDate(date);
                    list.add(tagConfirmDateDto);
                } else if (HangTagDeliverySCMStatusEnum.PLAN_TAG_PRICE_CONFIRM == type) {
                    //计控吊牌确认
                    tagConfirmDateDto.setStyleNo(styleNo);
                    tagConfirmDateDto.setPlanTagPriceConfirm(confirmStatus);
                    tagConfirmDateDto.setPlanTagPriceConfirmDate(date);
                    list.add(tagConfirmDateDto);
                }
            }
        }
        String params = JSONArray.toJSONString(list);

        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/tagConfirmDate", params,
                Pair.of("moduleName","scm"),
                Pair.of("functionName","下发尺寸和外辅工艺明细数据")
        );

        for (TagConfirmDateDto tagConfirmDateDto1 : list) {
            pushRecordsService.pushRecordSave(httpResp, JSONArray.toJSONString(tagConfirmDateDto1));
        }
    }

    /**
     * 正确样下发
     */
    public void styleColorCorrectInfoDate(TagConfirmDateDto tagConfirmDateDto) {
        String params = JSONArray.toJSONString(Arrays.asList(tagConfirmDateDto));
        restTemplateService.spmPost(SCM_URL + "/tagConfirmDate", params,
                Pair.of("moduleName","scm"),
                Pair.of("functionName","下发吊牌和款式定价确认信息")
        );
    }

    /**
     * 修改吊牌价的时候验证(暂不需要)
     */
    public void secondIngredient(List<SecondIngredientSyncDto> secondIngredientSyncDtoList) {
        String jsonString = JsonStringUtils.toJSONString(secondIngredientSyncDtoList);
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/materialElementKind", jsonString,
                Pair.of("moduleName","scm"),
                Pair.of("functionName","下发吊牌和款式定价确认信息")
        );
        if (!httpResp.isSuccess()) {
            throw new OtherException("同步失败");
        }
        for (SecondIngredientSyncDto secondIngredientSyncDto : secondIngredientSyncDtoList) {
            pushRecordsService.pushRecordSave(httpResp, JSONArray.toJSONString(secondIngredientSyncDto));
        }
    }

    /**
     *
     * @param ids   styleColorId
     * @return
     */
    public int goodsAgent(String[] ids,boolean isSync) {
        int i = 0;

        List<StyleColor> styleColors = styleColorService.listByIds(Arrays.asList(ids));
        if (CollUtil.isEmpty(styleColors)) {
            return i;
        }
        //查询style
        List<String> styleIds = styleColors.stream().map(StyleColor::getStyleId).collect(Collectors.toList());
        List<Style> styles = styleService.listByIds(styleIds);
        Map<String, Style> styleMap = styles.stream().collect(Collectors.toMap(BaseEntity::getId, o -> o));
        //查询尺码信息
        List<StyleColorAgent> styleColorAgents = styleColorAgentService.listByField("style_color_id", Arrays.asList(ids));

        for (StyleColorAgent styleColorAgent : styleColorAgents) {
            if(!StrUtil.equals(styleColorAgent.getStatus(),"0") && !StrUtil.equals(styleColorAgent.getStatus(),"2") && !StrUtil.equals(styleColorAgent.getStatus(),"3")){
                throw new OtherException("只有状态为未下发、重新打开、可编辑时才能下发");
            }
        }

        Map<String, StyleColorAgent> sizeMap = styleColorAgents.stream().collect(Collectors.toMap(o -> o.getStyleColorId() + o.getSizeId(), o->o));

        for (StyleColor styleColor : styleColors) {
            //判断是否是旧系统数据
            if ("1".equals(styleColor.getHistoricalData())) {
                throw new OtherException("旧系统数据不允许下发");
            }
        }

        for (StyleColor styleColor : styleColors) {
            Style style = styleMap.get(styleColor.getStyleId());
            SmpGoodsDto smpGoodsDto = styleColor.toSmpGoodsDto();
            //吊牌价为空或者等于0
            if (styleColor.getTagPrice()==null || styleColor.getTagPrice().compareTo(BigDecimal.ZERO)==0){
                throw new OtherException(styleColor.getStyleNo()+"吊牌价不能为空或者等于0");
            }

            PackInfoListVo packInfo = packInfoService.getByQw(new QueryWrapper<PackInfo>().eq("foreign_id", style.getId()));
            if (packInfo != null) {
                //款式定价
                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), style.getCompanyCode());
                smpGoodsDto.setPlanCost(stylePricingVO.getControlPlanCost());
            }

            if (StrUtil.equals(styleColor.getStatus(), BaseGlobal.YES)) {
                smpGoodsDto.setBomPhase("Sample");
            } else {
                smpGoodsDto.setBomPhase("Production");
            }

            smpGoodsDto.setColorCrash(styleColor.getColorCrash());
            smpGoodsDto.setMaxClassName(style.getProdCategory1stName());
            smpGoodsDto.setStyleBigClass(style.getProdCategory1st());
            smpGoodsDto.setCategoryName(style.getProdCategoryName());
            smpGoodsDto.setStyleCategory(style.getProdCategory());
            smpGoodsDto.setMiddleClassName(style.getProdCategory2ndName());
            smpGoodsDto.setStyleMiddleClass(style.getProdCategory2nd());
            smpGoodsDto.setMinClassName(style.getProdCategory3rdName());
            smpGoodsDto.setStyleSmallClass(style.getProdCategory3rd());
            smpGoodsDto.setBrandId(style.getBrand());
            smpGoodsDto.setBrandName(style.getBrandName());
            // 款式图片
            List<AttachmentVo> stylePicList = attachmentService.findByforeignId(style.getId(), AttachmentTypeConstant.STYLE_MASTER_DATA_PIC);
            List<String> imgList = new ArrayList<>();
            for (AttachmentVo attachmentVo : stylePicList) {
                imgList.add(attachmentVo.getUrl());
            }
            smpGoodsDto.setImgList(imgList);

            //设计师id,下稿设计师,工艺员,工艺员id,版师名称,版师ID
            String designer = style.getDesigner();
            smpGoodsDto.setDesigner(StringUtils.isNotEmpty(designer) ? designer.split(",")[0] : "");
            smpGoodsDto.setTechnician(style.getTechnicianName());
            smpGoodsDto.setPatternMakerName(style.getPatternDesignName());

            String designerId = style.getDesignerId();
            String technicianId = style.getTechnicianId();
            String patternDesignId = style.getPatternDesignId();

            ArrayList<String> list = new ArrayList<>();
            list.add(designerId);
            list.add(technicianId);
            list.add(patternDesignId);

            Map<String, String> usernamesByIds = amcService.getUsernamesByIds(StringUtils.join(list, ","));
            smpGoodsDto.setDesignerId(usernamesByIds.get(designerId));
            smpGoodsDto.setTechnicianId(usernamesByIds.get(technicianId));
            smpGoodsDto.setYear(style.getYearName());
            smpGoodsDto.setPatternName("常规");
            smpGoodsDto.setPriorityId(style.getTaskLevel());
            smpGoodsDto.setPriorityName(style.getTaskLevelName());
            smpGoodsDto.setSeason(style.getSeason());
            smpGoodsDto.setTheme(style.getSubject());
            smpGoodsDto.setStyleName(style.getStyleName());
            smpGoodsDto.setTargetCost(style.getProductCost());
            smpGoodsDto.setShapeName(style.getPlateType());
            smpGoodsDto.setUniqueCode(styleColor.getWareCode());

            Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Band");
            Map<String, String> map = dictInfoToMap.get("C8_Band");
            smpGoodsDto.setBandName(map.get(style.getBandCode()));

            //生产类型
            smpGoodsDto.setProductionType(style.getDevtType());
            smpGoodsDto.setProductionTypeName(style.getDevtTypeName());
            smpGoodsDto.setBandName(style.getBandName());
            smpGoodsDto.setAccessories("配饰".equals(style.getProdCategory1stName()));

            smpGoodsDto.setProductTypeId(style.getStyleType());
            smpGoodsDto.setProductType(style.getStyleTypeName());
            smpGoodsDto.setProductName(styleColor.getProductName());
            smpGoodsDto.setSaleTime(styleColor.getNewDate());
            smpGoodsDto.setProdSeg(styleColor.getSubdivide());
            smpGoodsDto.setSizeGroupId(style.getSizeRange());
            smpGoodsDto.setSizeGroupName(style.getSizeRangeName());
            smpGoodsDto.setStyleCode(style.getDesignNo());

            //region 增加二检包装形式
            //
            QueryWrapper<HangTag> hangTagQueryWrapper = new QueryWrapper();
            hangTagQueryWrapper.eq("bulk_style_no",styleColor.getStyleNo());
            hangTagQueryWrapper.eq("del_flag","0");
            hangTagQueryWrapper.last("limit 1");
            HangTag hangTag = hangTagService.getOne(hangTagQueryWrapper);
            if (hangTag != null) {
                smpGoodsDto.setSecondPackagingForm(hangTag.getSecondPackagingForm());
                smpGoodsDto.setSecondPackagingFormCode(hangTag.getSecondPackagingFormCode());

                smpGoodsDto.setPackageType(hangTag.getPackagingFormCode());
                smpGoodsDto.setPackageSize(hangTag.getPackagingBagStandardCode());
            }
            //endregion

            //工艺说明
            long count = packTechSpecService.count(new QueryWrapper<PackTechSpec>().eq("pack_type", "packBigGoods").eq("foreign_id", style.getId()).eq("spec_type", "外辅工艺"));
            smpGoodsDto.setAuProcess(count > 0);

            smpGoodsDto.setUnit(style.getStyleUnitCode());
            String downContent = "";

            List<String> sizeCodes = StringUtils.convertList(style.getSizeCodes());
            if (sizeCodes.isEmpty()){
                throw new OtherException("尺码不能为空");
            }
            List<BasicsdatumSize> basicsdatumSizes = basicsdatumSizeService.listByField("code", sizeCodes);

            List<SmpSize> smpSizes = new ArrayList<>();
            for (BasicsdatumSize basicsdatumSize : basicsdatumSizes) {
                SmpSize smpSize = new SmpSize();
                smpSize.setSize(basicsdatumSize.getModel());
                smpSize.setSizeNumber(basicsdatumSize.getSort());
                smpSize.setCode(basicsdatumSize.getCode());
                smpSize.setProductSizeName(basicsdatumSize.getHangtags());
                smpSize.setBaseSize(StringUtils.isNoneBlank(style.getDefaultSize()) && style.getDefaultSize().equals(basicsdatumSize.getHangtags()));
                smpSize.setSkuFiller(downContent);
                smpSize.setSpecialSpec(basicsdatumSize.getInternalSize());
                if(sizeMap.containsKey(styleColor.getId()+basicsdatumSize.getCode())){
                    StyleColorAgent styleColorAgent = sizeMap.get(styleColor.getId() + basicsdatumSize.getCode());
                    smpSize.setOutsideBarcode(styleColorAgent.getOutsideBarcode());
                    smpSize.setOutsideSizeCode(styleColorAgent.getOutsideSizeCode());
                    smpGoodsDto.setOutsideColorCode(styleColorAgent.getOutsideColorCode());
                    smpGoodsDto.setOutsideColorName(styleColorAgent.getOutsideColorName());
                }
                smpSizes.add(smpSize);
            }
            smpGoodsDto.setItemList(smpSizes);

            String jsonString = JsonStringUtils.toJSONString(smpGoodsDto);
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/goods", jsonString,
                    Pair.of("moduleName","smp"),
                    Pair.of("functionName","商品主数据下发-MANGO")
            );
            if (httpResp.isSuccess()) {
                i++;
                styleColor.setScmSendFlag("1");
                if(isSync){
                    //只有同步时，才修改所有状态为 已下发，   停用或者启用时，单独修改状态
                    LambdaUpdateWrapper<StyleColorAgent> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.set(StyleColorAgent::getStatus,"1");
                    updateWrapper.eq(StyleColorAgent::getStyleColorId,styleColor.getId());
                    styleColorAgentService.update(updateWrapper);
                }
            } else {
                styleColor.setScmSendFlag("2");
            }
            styleColorService.updateById(styleColor);
        }
        return i;
    }

    /**
     * 查询相似款的款号
     */
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public PageInfo<OrderBookSimilarStyleVo> querySaleIntoPageTotal(SaleProductIntoDto saleProductIntoDto) {
        Page<Object> page = saleProductIntoDto.startPage();
        BaseQueryWrapper qw = new BaseQueryWrapper<>();
        dataPermissionsService.getDataPermissionsForNameQw(qw, DataPermissionsBusinessTypeEnum.style_order_book.getK(), "tobl.", new String[]{"brand"}, true);
        qw.notEmptyLike("tobl.PROD_CODE", saleProductIntoDto.getBulkStyleNo());
        qw.in(!CollectionUtils.isEmpty(saleProductIntoDto.getBulkStyleNoList()),"tobl.PROD_CODE", saleProductIntoDto.getBulkStyleNoList());
        qw.notEmptyIn("tobl.YEARS", saleProductIntoDto.getYear());
        qw.in("tobl.CHANNEL_TYPE", saleProductIntoDto.getChannelList());
        qw.notEmptyEq("tobl.PROD_CODE", saleProductIntoDto.getSimilarBulkStyleNo());
        qw.in(CollUtil.isNotEmpty(saleProductIntoDto.getSimilarBulkStyleNos()),"tobl.PROD_CODE", saleProductIntoDto.getSimilarBulkStyleNos());
        List<Map<String, Object>> totalMaps = this.querySaleIntoPage(qw, 1);
        List<OrderBookSimilarStyleVo> dtoList = ORDER_BOOK_CV.copyList2SimilarStyleVo(totalMaps);

        return CopyUtil.copy(page.toPageInfo(), dtoList);
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public List<Map<String, Object>> querySaleIntoPage(BaseQueryWrapper qw, Integer total) {
        return saleProductIntoService.querySaleIntoPage(qw, total);
    }

    /**
     * 查询相似款
     */
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<StyleSaleIntoDto> querySaleIntoPage(SaleProductIntoDto saleProductIntoDto) {
        if (ObjectUtil.isEmpty(saleProductIntoDto.getBulkStyleNoList())) {
            return CollUtil.newArrayList();
        }
        BaseQueryWrapper<OrderBookDetail> qw = new BaseQueryWrapper<>();
        qw.notEmptyIn("tobl.PROD_CODE", saleProductIntoDto.getBulkStyleNoList());
        qw.notEmptyIn("tobl.YEARS", saleProductIntoDto.getYear());
        qw.in("tobl.CHANNEL_TYPE", saleProductIntoDto.getChannelList());
        qw.notEmptyIn("tobl.RESULTTYPE", saleProductIntoDto.getResultTypeList());
        dataPermissionsService.getDataPermissionsForNameQw(qw, DataPermissionsBusinessTypeEnum.style_order_book.getK(), "tobl.", new String[]{"brand"}, true);
        List<Map<String, Object>> detailMaps = this.querySaleIntoPage(qw, 0);
        // 封装数据并转化Bean
        detailMaps.forEach(it-> it.put("sizeMap",new HashMap<>(it)));
        return ORDER_BOOK_CV.copyList2StyleSaleInto(detailMaps);
    }

    public ApiResult<List<ScmProductionBudgetDto>> productionBudgetList(ScmProductionBudgetQueryDto productionBudgetQueryDto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",productionBudgetQueryDto);
        HttpResp httpResp = restTemplateService.spmPost(SmpProperties.SCM_APP_BILL_PRODUCTION_BUDGET_LIST_URL, jsonObject.toJSONString(),
                Pair.of("moduleName","scm"),
                Pair.of("functionName","预算号查询")
        );
        ApiResult<List<ScmProductionBudgetDto>> result = ApiResult.success(httpResp.getMessage(), httpResp.getData(ScmProductionBudgetDto.class));
        result.setSuccess(httpResp.isSuccess());
        return result;
    }

    /**
     * 订货本一键投产
     */
    public HttpResp saveFacPrdOrder(ScmProductionDto scmProductionDto) {
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(scmProductionDto));
        jsonObject.put("code",scmProductionDto.getOrderBookDetailId());
        return restTemplateService.spmPost(SmpProperties.SCM_NEW_MF_FAC_PRODUCTION_IN_URL, jsonObject.toJSONString(),
                Pair.of("moduleName", "scm"),
                Pair.of("functionName", "订货本一键投产"),
                Pair.of("code", scmProductionDto.getOrderBookDetailId()),
                Pair.of("name", scmProductionDto.getName())
        );
    }

    /**
     * 反审核投产单
     */
    public HttpResp facPrdOrderUpCheck(String orderNo, String loginName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNo",orderNo);
        jsonObject.put("loginName",loginName);
        return restTemplateService.spmPost(SmpProperties.SCM_NEW_MF_FAC_CANCEL_PRODUCTION_URL, jsonObject.toJSONString(),
                Pair.of("moduleName","scm"),
                Pair.of("functionName","反审核投产单"),
                Pair.of("code", orderNo),
                Pair.of("name", loginName)
        );
    }
}

