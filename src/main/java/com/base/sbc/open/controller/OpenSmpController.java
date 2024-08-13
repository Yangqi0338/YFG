package com.base.sbc.open.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialIngredient;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialIngredientService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.basicsdatum.vo.ConfigPrintVo;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.base.sbc.module.orderbook.entity.OrderBook;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.entity.PatternMakingBarCode;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingBarCodeService;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.PatternMakingListVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.sample.entity.PreProductionSampleTaskFob;
import com.base.sbc.module.sample.service.PreProductionSampleTaskFobService;
import com.base.sbc.module.smp.dto.SmpSampleDto;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleSupplierVo;
import com.base.sbc.open.dto.BasicsdatumGarmentInspectionDto;
import com.base.sbc.open.dto.DesignStyleOverdueReasonDto;
import com.base.sbc.open.dto.MtBpReqDto;
import com.base.sbc.open.dto.OrderBookDto;
import com.base.sbc.open.dto.*;
import com.base.sbc.open.entity.*;
import com.base.sbc.open.service.BasicsdatumGarmentInspectionService;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.base.sbc.open.service.MtBqReqService;
import com.base.sbc.open.service.OpenSmpService;
import com.base.sbc.open.vo.DesignStyleOverdueReasonVo;
import com.base.sbc.open.vo.OrderBookDetailDataVo;
import com.base.sbc.open.vo.OrderBookNameVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/5/10 9:27:47
 * @mail 247967116@qq.com
 * smp开放接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/smp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OpenSmpController extends BaseController {

    private final MtBqReqService mtBqReqService;

    private final BasicsdatumSupplierService basicsdatumSupplierService;

    private final AmcService amcService;

    private final HangTagService hangTagService;

    private final StyleColorService styleColorService;

    private final EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;

    private final OpenSmpService openSmpService;

    private final BasicsdatumMaterialIngredientService basicsdatumMaterialIngredientService;

    private final BasicsdatumMaterialService basicsdatumMaterialService;

    private final CcmService ccmService;

    private final BasicsdatumGarmentInspectionService garmentInspectionService;
    private final OrderBookService orderBookService;

    private final OrderBookDetailService orderBookDetailService;
    private final PlanningSeasonService planningSeasonService;
    private final MoreLanguageService moreLanguageService;
    private final PreProductionSampleTaskFobService preProductionSampleTaskFobService;
    private final PatternMakingBarCodeService patternMakingBarCodeService;
    private final StyleService styleService;
    private final RedisUtils redisUtils;
    private final PatternMakingService patternMakingService;
    private final PatternMakingMapper patternMakingMapper;
    private final UploadFileService uploadFileService;
    private final NodeStatusService nodeStatusService;
    private final OperaLogService operaLogService;

    private final BasicsdatumSupplierService supplierService;

    /**
     * bp供应商
     */
    @PostMapping("/supplierSave")
    @ApiOperation(value = "bp供应商新增或者修改", notes = "bp供应商新增或者修改")
    @Transactional(rollbackFor = {Throwable.class})
    public ApiResult supplierSave(@RequestBody JSONObject jsonObject) {
        MtBpReqDto mtBpReqDto = JSONObject.parseObject(jsonObject.toJSONString(), MtBpReqDto.class);


        //先保存传过来的数据对象
        MtBqReqEntity mtBqReqEntity = mtBpReqDto.toMtBqReqEntity();
        mtBqReqService.saveOrUpdate(mtBqReqEntity, new QueryWrapper<MtBqReqEntity>().eq("partner", mtBqReqEntity.getPartner()));
        //再存入供应商
        BasicsdatumSupplier basicsdatumSupplier = mtBqReqEntity.toBasicsdatumSupplier();
        basicsdatumSupplierService.saveOrUpdate(basicsdatumSupplier, new QueryWrapper<BasicsdatumSupplier>().eq("supplier_code", basicsdatumSupplier.getSupplierCode()));
        return insertSuccess(null);
    }

    /**
     * hr-人员
     */
    @PostMapping("/hrUserSave")
    @ApiOperation(value = "hr-人员新增或者修改", notes = "hr-人员新增或者修改")
    public ApiResult hrSave(@RequestBody JSONObject jsonObject) {
        SmpUser smpUser = JSONObject.parseObject(jsonObject.getJSONObject("content").toJSONString(), SmpUser.class);
        smpUser.preInsert();
        smpUser.setCreateName("smp请求");
        smpUser.setUpdateName("smp请求");
        smpUser.setCompanyCode(smpUser.getCompanyId());
        amcService.hrUserSave(smpUser);
        return insertSuccess(null);
    }


    /**
     * hr-部门
     */
    @PostMapping("/hrDeptSave")
    @ApiOperation(value = "hr-部门新增或者修改", notes = "hr-部门新增或者修改")
    public ApiResult hrDeptSave(@RequestBody JSONObject jsonObject) {
        SmpDept smpDept = JSONObject.parseObject(jsonObject.getJSONObject("content").toJSONString(), SmpDept.class);
        smpDept.preInsert();
        smpDept.setCreateName("smp请求");
        smpDept.setUpdateName("smp请求");
        if (smpDept != null) {
            String depGroup = smpDept.getDepGroup();
            if (!"终端办公室".equals(depGroup) && !"终端店铺".equals(depGroup) && !"终端管理".equals(depGroup) && !"托管店铺".equals(depGroup) ) {
                amcService.hrDeptSave(smpDept);
            }
        }
        return insertSuccess(null);
    }

    /**
     * hr-岗位
     */
    @PostMapping("/hrPostSave")
    @ApiOperation(value = "hr-岗位新增或者修改", notes = "hr-岗位新增或者修改")
    public ApiResult hrPostSave(@RequestBody JSONObject jsonObject) {
        SmpPost smpPost = JSONObject.parseObject(jsonObject.getJSONObject("content").toJSONString(), SmpPost.class);
        amcService.hrPostSave(smpPost);
        return insertSuccess(null);
    }


    /**
     * smp-样衣（第一期不做）
     */
    @PostMapping("/smpSampleSave")
    @ApiOperation(value = "smp-样衣新增或者修改", notes = "smp-样衣新增或者修改")
    public ApiResult smpSampleSave(@RequestBody JSONObject jsonObject) {
        SmpSampleDto smpSampleDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpSampleDto.class);
        System.out.println(smpSampleDto);
        return insertSuccess(null);
    }

    /**
     * smp-物料
     */
    @PostMapping("/smpMaterial")
    @ApiOperation(value = "smp-物料", notes = "smp-物料")

    public ApiResult smpMaterial(@RequestBody JSONObject jsonObject) {
        openSmpService.smpMaterial(jsonObject);
        return insertSuccess(null);
    }


    /**
     * 吊牌打印
     */
    @GetMapping("/TagPrinting")
    @ApiOperation(value = "吊牌打印获取", notes = "吊牌打印获取")
    public ApiResult tagPrinting(String id, boolean bl) {
        List<TagPrinting> tagPrintings1 = hangTagService.hangTagPrinting(id, bl);
        return selectSuccess(tagPrintings1);
    }

    /**
     * 代理吊牌打印
     */
    @GetMapping("/agentTagPrinting")
    @ApiOperation(value = "吊牌打印获取", notes = "吊牌打印获取")
    public ApiResult agentTagPrinting(String id, boolean bl) {
        List<TagPrinting> tagPrintings = styleColorService.agentListByStyleNo(id, bl);
        return selectSuccess(tagPrintings);
    }


    /**
     * 面料成分检测数据接口
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/escmMaterialCompnentInspectCompany")
    public ApiResult EscmMaterialCompnentInspectCompanyDto(@RequestBody JSONObject jsonObject){
        EscmMaterialCompnentInspectCompanyDto escmMaterialCompnentInspectCompanyDto = jsonObject.toJavaObject(EscmMaterialCompnentInspectCompanyDto.class);

        /*String materialsNo = escmMaterialCompnentInspectCompanyDto.getMaterialsNo();
        String year = escmMaterialCompnentInspectCompanyDto.getYear();

        QueryWrapper<EscmMaterialCompnentInspectCompanyDto> compnentInspectCompanyDtoQueryWrapper = new QueryWrapper<>();
        compnentInspectCompanyDtoQueryWrapper.eq("year",year);
        compnentInspectCompanyDtoQueryWrapper.eq("materials_no",materialsNo);
        compnentInspectCompanyDtoQueryWrapper.last("limit 1");
        EscmMaterialCompnentInspectCompanyDto inspectCompanyDto = escmMaterialCompnentInspectCompanyService.getOne(compnentInspectCompanyDtoQueryWrapper);
        if (inspectCompanyDto == null) {
            escmMaterialCompnentInspectCompanyService.save(escmMaterialCompnentInspectCompanyDto);
        }else{
            escmMaterialCompnentInspectCompanyService.updateById(escmMaterialCompnentInspectCompanyDto);
        }*/
        escmMaterialCompnentInspectCompanyService.saveOrUpdate(escmMaterialCompnentInspectCompanyDto);



        /*escmMaterialCompnentInspectCompanyService.saveOrUpdate(escmMaterialCompnentInspectCompanyDto,
                new QueryWrapper<EscmMaterialCompnentInspectCompanyDto>()
                        .eq("materials_no",escmMaterialCompnentInspectCompanyDto.getMaterialsNo())
                        .eq("year",escmMaterialCompnentInspectCompanyDto.getYear())
        );*/

        basicsdatumMaterialIngredientService.remove(new QueryWrapper<BasicsdatumMaterialIngredient>().eq("material_code",escmMaterialCompnentInspectCompanyDto.getMaterialsNo()));

        String quanlityInspectContent="";

        List<BasicBaseDict> pd021DictList = new ArrayList<>();
        String dictInfo = ccmService.getOpenDictInfo(BaseConstant.DEF_COMPANY_CODE, "pd021");
        JSONObject dictJsonObject = JSON.parseObject(dictInfo);
        if (dictJsonObject.getBoolean(BaseConstant.SUCCESS)) {
            pd021DictList = dictJsonObject.getJSONArray("data").toJavaList(BasicBaseDict.class);
        }
        for (int i = 0; i < escmMaterialCompnentInspectCompanyDto.getDetailList().size(); i++) {
            EscmMaterialCompnentInspectContent inspectContent = escmMaterialCompnentInspectCompanyDto.getDetailList().get(i);

            BasicsdatumMaterialIngredient basicsdatumMaterialIngredient =new BasicsdatumMaterialIngredient();
            basicsdatumMaterialIngredient.setMaterialCode(escmMaterialCompnentInspectCompanyDto.getMaterialsNo());
            basicsdatumMaterialIngredient.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
            String say = "";
            //1.成分备注不带括号，默认给它加英文括号；
            //2.成分带括号时，默认改为英文括号；
            //3.成分带英文括号，不做处理
            if(StringUtils.isNotEmpty(inspectContent.getRemark())){
                String remark = inspectContent.getRemark();
                if (remark.contains("（") || remark.contains("）")) {
                    say = inspectContent.getRemark().replace("（", "(").replace("）", ")");
                }

                if (!remark.contains("(") && !remark.contains(")")) {
                    say = "("+inspectContent.getRemark()+")";
                }
//                 say = inspectContent.getRemark().replace("（", "(").replace("）", ")");
                basicsdatumMaterialIngredient.setSay(inspectContent.getRemark());
            }
            String contentProportion = inspectContent.getContentProportion().replace("%", "");
            if (StrUtil.isNotBlank(contentProportion)) {
                basicsdatumMaterialIngredient.setRatio(BigDecimal.valueOf(Float.parseFloat(contentProportion)));
            }
/*清掉%*/
            if(StrUtil.equals(inspectContent.getContentProportion(), "%")){
                inspectContent.setContentProportion("");
            }
            basicsdatumMaterialIngredient.setType("0");
            basicsdatumMaterialIngredient.setName(inspectContent.getInspectContent());
            String kindCode = inspectContent.getKindCode();
            String kindName = inspectContent.getKindName();
            kindName = Optional.ofNullable(kindName).orElse(
                    pd021DictList.stream().filter(it -> it.getValue().equals(kindCode)).findFirst().map(BasicBaseDict::getName).orElse("")
            );
            basicsdatumMaterialIngredient.setMaterialKindCode(kindCode);
            basicsdatumMaterialIngredient.setMaterialKindName(kindName);
            basicsdatumMaterialIngredientService.save(basicsdatumMaterialIngredient);

            quanlityInspectContent+= kindName + inspectContent.getContentProportion()+
                    inspectContent.getInspectContent();
            if (StringUtils.isNotEmpty( say)){
                quanlityInspectContent+=say;
            }



            if (i!=escmMaterialCompnentInspectCompanyDto.getDetailList().size()-1){
                quanlityInspectContent+=",\n";
            }
        }

        BasicsdatumMaterial basicsdatumMaterial = basicsdatumMaterialService.getOne(new QueryWrapper<BasicsdatumMaterial>().eq("material_code", escmMaterialCompnentInspectCompanyDto.getMaterialsNo()));

         // quanlityInspectContent = escmMaterialCompnentInspectCompanyDto.getQuanlityInspectContent();
        // if (!StringUtils.isEmpty(quanlityInspectContent)){
        //     quanlityInspectContent = quanlityInspectContent.replace(" ", ",");
        // }
        basicsdatumMaterial.setIngredient(quanlityInspectContent);
        basicsdatumMaterial.setCheckFileUrl(escmMaterialCompnentInspectCompanyDto.getFileUrl());
        basicsdatumMaterialService.updateById(basicsdatumMaterial);
        return insertSuccess(null);
    }


    /**
     * 获取大货款，设计师，版师，样衣工
     */
    @GetMapping("/getStyleDesignerInfo")
    @ApiOperation(value = "根据大货款号获取，设计师，版师，样衣工", notes = "根据大货款号获取，设计师，版师，样衣工")
    public ApiResult getStyleDesignerInfo(String styleNo) {
        if (StrUtil.isBlank(styleNo)) {
            throw new OtherException("大货款号不允许为空");
        }
        return selectSuccess(styleColorService.getDesignerInfo(styleNo));
    }

    /**
     * 接收成衣成分送检数据
     */
    @PostMapping("/garmentInspection")
    @ApiOperation(value = "PDM获取SCM的成分送检数据", notes = "PDM获取SCM的成分送检数据")
    public ApiResult garmentInspection(@RequestBody BasicsdatumGarmentInspectionDto garmentInspectionDto) {
        garmentInspectionService.saveGarmentInspection(garmentInspectionDto);
        return insertSuccess(null);
    }

    /**
     * 按产品季、查询订货本名称列表
     */
    @PostMapping("/getOrderBookList")
    @ApiOperation(value = "按产品季、查询订货本名称列表", notes = "按产品季、查询订货本名称列表")
    public ApiResult getOrderBookList(@RequestBody OrderBookDto orderBookDto) {
        QueryWrapper<PlanningSeason> seasonQueryWrapper = new QueryWrapper();
        seasonQueryWrapper.eq("year",orderBookDto.getYear());
        seasonQueryWrapper.eq("season",orderBookDto.getSeason());
        seasonQueryWrapper.eq("brand",orderBookDto.getBrand());
        seasonQueryWrapper.eq("del_flag","0");
        seasonQueryWrapper.last(" limit 1 ");
        PlanningSeason planningSeason = planningSeasonService.getOne(seasonQueryWrapper);
        List<OrderBookNameVo> orderBookNameVos = null;
        if (planningSeason != null) {
            QueryWrapper<OrderBook> orderBookQueryWrapper = new QueryWrapper();
            orderBookQueryWrapper.eq("season_id", planningSeason.getId());
            List<OrderBook> list = orderBookService.list(orderBookQueryWrapper);
            orderBookNameVos = BeanUtil.copyToList(list, OrderBookNameVo.class);
        }
        return selectSuccess(orderBookNameVos);
    }
    /**
     * 按产品季、查询订货本名称列表
     */
    @PostMapping("/getOrderBookDetails")
    @ApiOperation(value = "查询订货本下大货款列表信息", notes = "查询订货本下大货款列表信息")
    public ApiResult getOrderBookDetails(@RequestBody OrderBookDto orderBookDto) {
        String orderBookId = orderBookDto.getOrderBookId();
        QueryWrapper<OrderBookDetail> orderBookDetailQueryWrapper = new QueryWrapper();
        orderBookDetailQueryWrapper.eq("order_book_id", orderBookId);
        OrderBook orderBook = orderBookService.getById(orderBookId);
        if (orderBook != null) {
            List<OrderBookDetailVo> querylist = orderBookDetailService.querylist(orderBookDetailQueryWrapper, 0, 1);
            List<OrderBookDetailDataVo> orderBookDetailDataVos = BeanUtil.copyToList(querylist, OrderBookDetailDataVo.class);
            PlanningSeason planningSeason = planningSeasonService.getById(orderBook.getSeasonId());
            if (planningSeason != null) {
                for (OrderBookDetailDataVo orderBookDetailDataVo : orderBookDetailDataVos) {
                    orderBookDetailDataVo.setYear(planningSeason.getYear());
                    orderBookDetailDataVo.setSeasonName(planningSeason.getSeasonName());
                    orderBookDetailDataVo.setBrandName(planningSeason.getBrandName());
                }
            }
            return selectSuccess(orderBookDetailDataVos);
        }
        return selectSuccess(null);
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult listQuery(String standardColumnCode) {
        MoreLanguageQueryDto moreLanguageQueryDto = new MoreLanguageQueryDto();
        moreLanguageQueryDto.setPageNum(1);
        moreLanguageQueryDto.setPageSize(Integer.MAX_VALUE);
        moreLanguageQueryDto.setType(CountryLanguageType.TAG);
        moreLanguageQueryDto.setSingleLanguageFlag(YesOrNoEnum.YES);
        moreLanguageQueryDto.setCache(YesOrNoEnum.NO.getValueStr());
        moreLanguageQueryDto.setStandardColumnCode(standardColumnCode);
        List<Map<String, Object>> list = moreLanguageService.listQuery(moreLanguageQueryDto).getList();
        return selectSuccess(list);
    }

    /**
     * 根据物料编码获取图片
     */
    @GetMapping("/pdmImgUrlByMaterialCode")
    @ApiOperation(value = "物料图片获取", notes = "物料图片获取")
    public ApiResult getMaterialImageUrl(String code) {
        QueryWrapper<BasicsdatumMaterial> materialQueryWrapper = new BaseQueryWrapper<>();
        materialQueryWrapper.eq("material_code",code);
        materialQueryWrapper.orderByAsc("del_flag,status");
        materialQueryWrapper.last("limit 1");
        BasicsdatumMaterial basicsdatumMaterial = basicsdatumMaterialService.getOne(materialQueryWrapper);
        if (basicsdatumMaterial == null) {
            return ApiResult.error(code+"：找不到物料图片！",404);
        }
        return selectSuccess(basicsdatumMaterial.getImageUrl());
    }

    @PostMapping("/styleOverDueResaonList")
    @ApiOperation(value = "设计下明细单逾期原因", notes = "设计下明细单逾期原因")
    public ApiResult styleOverDueResaon(@RequestBody DesignStyleOverdueReasonDto designStyleOverdueReasonDto) {
        if (CollUtil.isNotEmpty(designStyleOverdueReasonDto.getStyleNos())) {
            //超过1000个款号，返回null
            if (designStyleOverdueReasonDto.getStyleNos().size() > 1000) {
                return selectSuccess(null);
            }
            QueryWrapper<StyleColor> styleColorQueryWrapper = new QueryWrapper<>();
            styleColorQueryWrapper.in("style_no", designStyleOverdueReasonDto.getStyleNos());
            styleColorQueryWrapper.select("style_no,send_main_fabric_overdue_reason,design_detail_overdue_reason,design_correct_overdue_reason");
            List<StyleColor> list = styleColorService.list(styleColorQueryWrapper);
            return selectSuccess(CopyUtil.copy(list, DesignStyleOverdueReasonVo.class));
        }
        return selectSuccess(null);
    }

    @PostMapping("/productionSampleTask")
    @ApiOperation(value = "推送FOB产前样数据" , notes = "推送FOB产前样数据")
    public ApiResult productionSampleTask(@RequestBody List<PreProductionSampleTaskFob> fobs){
        StringBuilder msg = new StringBuilder();
        List<String> codes = new ArrayList<>();
        try{
            codes = fobs.stream().map(PreProductionSampleTaskFob::getCode).distinct().collect(Collectors.toList());

            LambdaQueryWrapper<StyleColor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(StyleColor::getStyleNo, codes);
            List<StyleColor> list = styleColorService.list(queryWrapper);
            Map<String, StyleColor> styleColorMap = list.stream().collect(Collectors.toMap(StyleColor::getStyleNo, o -> o, (v1, v2) -> v1));

            List<PreProductionSampleTaskFob> saveList = new ArrayList<>();


            List<String> ids = new ArrayList<>();
            for (PreProductionSampleTaskFob fob : fobs) {
                //对面会给id过来，  根据对方的id进行新增或修改操作

                //大货款号 code
                String code = fob.getCode();
                if(!styleColorMap.containsKey(code)){
                    msg.append("大货款号:").append(code).append("没有找到;");
                    continue;
                }
                StyleColor styleColor = styleColorMap.get(code);
                fob.setStyleId(styleColor.getId());
                fob.setPlanningSeasonId(styleColor.getPlanningSeasonId());
                fob.setNode("产前样衣任务FOB");

                String sampleBarCode = fob.getSampleBarCode();
                //样衣码 sampleBarCode
                //供应商 patternRoom patternRoomId
                //外发部发出时间 designDetailTime
                //外发部绑定时间 techReceiveTime
                //通过状态 status
                //改版意见 tech_remarks
                //确认时间 processDepartmentDate
                saveList.add(fob);
                ids.add(fob.getId());
            }
            if(CollUtil.isNotEmpty(saveList)){
                preProductionSampleTaskFobService.saveOrUpdateBatch(saveList);

                List<PatternMakingBarCode> patternMakingBarCodes = patternMakingBarCodeService.listbyHeadId(ids);
                List<String> dbIds = patternMakingBarCodes.stream().map(PatternMakingBarCode::getHeadId).collect(Collectors.toList());

                List<PatternMakingBarCode> saveBarCodeList = new ArrayList<>();
                List<NodeStatus> nodeList = new ArrayList<>();
                for (PreProductionSampleTaskFob preProductionSampleTaskFob : saveList) {
                    if(!dbIds.contains(preProductionSampleTaskFob.getId())){
                        PatternMakingBarCode barCode = new PatternMakingBarCode();
                        barCode.setBarCode(preProductionSampleTaskFob.getSampleBarCode());
                        barCode.setStatus("10");
                        barCode.setHeadId(preProductionSampleTaskFob.getId());
                        //添加新的绑样时间
                        NodeStatus nodeStatus = new NodeStatus();
                        nodeStatus.setDataId(barCode.getHeadId());
                        nodeStatus.setNode("FOB");
                        nodeStatus.setStatus("绑样");
                        nodeStatus.setStartDate(preProductionSampleTaskFob.getDesignDetailTime());
                        nodeStatus.setEndDate(preProductionSampleTaskFob.getDesignDetailTime());
                        saveBarCodeList.add(barCode);
                        nodeList.add(nodeStatus);
                    }
                }
                if(CollUtil.isNotEmpty(saveBarCodeList)){
                    patternMakingBarCodeService.saveBatch(saveBarCodeList);
                    nodeStatusService.saveBatch(nodeList);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return ApiResult.error(e.getMessage(),500);
        }finally {
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setType("推送FOB产前样数据");
            operaLogEntity.setDocumentId(String.join(",", codes));
            operaLogEntity.setName("推送FOB产前样数据");
            operaLogEntity.setDocumentName(String.join(",", codes));
            operaLogEntity.setContent(msg.toString());
            operaLogService.save(operaLogEntity);
        }

        return ApiResult.success("保存成功;"+msg);
    }

    /**
     * 供应商获取款式信息
     * @param code 用于校验时效
     * @return
     */
    @GetMapping("/getStyleByCode")
    public ApiResult<StyleSupplierVo> getStyleByCode(String code){
        //校验是否失效
        String key = "FOB_SUPPLIER_URL:" + code;
        if (!redisUtils.hasKey(key)) {
            //链接已失效
            throw new OtherException("链接已失效，请重新获取链接");
        }
        //打版id
        String id = redisUtils.get(key).toString();
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("m.id", id);
        qw.eq("m.del_flag", BaseGlobal.NO);
        List<PatternMakingListVo> patternMakingListVos = patternMakingMapper.findBySampleDesignId(qw);
        uploadFileService.setObjectUrlToList(patternMakingListVos,"samplePicUrl", "sampleVideoUrl");
        PatternMakingListVo patternMakingListVo = patternMakingListVos.get(0);
        //设计款id
        String styleId = patternMakingListVo.getStyleId();
        StyleSupplierVo styleVo = BeanUtil.copyProperties(styleService.getDetail(styleId, null), StyleSupplierVo.class);

        //设计款动态字段
        DimensionLabelsSearchDto dto = new DimensionLabelsSearchDto();
        dto.setForeignId(styleId);
        dto.setDataGroup(FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        Map<String, List<FieldManagementVo>> filedMap = styleService.queryCoefficientByStyle(dto);

        styleVo.setPatternMaking(patternMakingListVo);
        styleVo.setFiledMap(filedMap);

        return ApiResult.success("查询成功", styleVo);
    }



    @PostMapping("/receiveScmSupplier")
    @ApiOperation(value = "接收SCM传输过来的供应商", notes = "接收SCM传输过来的供应商")
    public ApiResult receiveScmSupplier(@RequestBody TempSupplierDto tempSupplierDto) {
        String supplierCode = tempSupplierDto.getSupplierCode();
        if (StrUtil.isNotEmpty(supplierCode)) {
            throw new OtherException("临时供应商编号不能为空！");
        }
        BasicsdatumSupplier basicsdatumSupplier = BeanUtil.copyProperties(tempSupplierDto, BasicsdatumSupplier.class);

        QueryWrapper<BasicsdatumSupplier> basicsdatumSupplierQueryWrapper = new QueryWrapper<>();
        basicsdatumSupplierQueryWrapper.eq("supplier_code",supplierCode);
        BasicsdatumSupplier supplier = supplierService.getOne(basicsdatumSupplierQueryWrapper);
        if (supplier == null) {
            supplierService.save(basicsdatumSupplier);
        }else{
            //将数据copy已存在实体
            BeanUtil.copyProperties(tempSupplierDto, supplier);
            supplierService.updateById(supplier);
        }

        return selectSuccess(tempSupplierDto);
    }
}
