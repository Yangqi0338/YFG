/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.enums.CcmBaseSettingEnum;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.nodestatus.dto.NodestatusPageSearchDto;
import com.base.sbc.module.nodestatus.dto.ResearchProgressPageDto;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.patternmaking.dto.AssignmentUserDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingCommonPageSearchDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingReferSampleDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingTaskSearchDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingWeekMonthViewDto;
import com.base.sbc.module.patternmaking.dto.SamplePicUploadDto;
import com.base.sbc.module.patternmaking.dto.SaveAttachmentDto;
import com.base.sbc.module.patternmaking.dto.ScoreDto;
import com.base.sbc.module.patternmaking.dto.SetKittingDto;
import com.base.sbc.module.patternmaking.dto.SetPatternDesignDto;
import com.base.sbc.module.patternmaking.dto.SetSampleBarCodeDto;
import com.base.sbc.module.patternmaking.dto.SetSortDto;
import com.base.sbc.module.patternmaking.dto.StyleSendDto;
import com.base.sbc.module.patternmaking.dto.SuspendDto;
import com.base.sbc.module.patternmaking.dto.TechnologyCenterTaskSearchDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.enums.EnumNodeStatus;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.NodeListVo;
import com.base.sbc.module.patternmaking.vo.PatternDesignVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingCommonPageSearchVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingListVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingTaskListVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingVo;
import com.base.sbc.module.patternmaking.vo.PatternUserSearchVo;
import com.base.sbc.module.patternmaking.vo.SampleBoardVo;
import com.base.sbc.module.patternmaking.vo.StylePmDetailVo;
import com.base.sbc.module.patternmaking.vo.StyleResearchProcessVo;
import com.base.sbc.module.patternmaking.vo.TechnologyCenterTaskVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 类描述：打版管理 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.web.PatternMakingController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 */
@RestController
@Api(tags = "打版管理")
@RequestMapping(value = BaseController.SAAS_URL + "/patternMaking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@RequiredArgsConstructor
public class PatternMakingController {

    @Autowired
    private PatternMakingService patternMakingService;
    @Autowired
    private UserUtils userUtils;

    @Lazy
    @Autowired
    private final SmpService smpService;

    @Autowired
    private CcmFeignService ccmFeignService;

    @ApiOperation(value = "技术中心看板-任务列表")
    @GetMapping("/technologyCenterTaskList")
    public PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto) {
        return patternMakingService.technologyCenterTaskList(dto);
    }

    @ApiOperation(value = "/滞留款导出")
    @GetMapping("/technologyCenterTaskListDeriveExcel")
    @DuplicationCheck(type = 1,message = "正在导出中，请稍后...")
    public void technologyCenterTaskListDeriveExcel(HttpServletResponse response, TechnologyCenterTaskSearchDto dto) {
        patternMakingService.technologyCenterTaskListExcel(response,dto);
    }

    @ApiOperation(value = "通过款式设计id查询")
    @GetMapping("/findBySampleDesignId")
    public List<PatternMakingListVo> findBySampleDesignId(@NotBlank(message = "(styleId)款式设计id不能为空") String styleId) {
        List<PatternMakingListVo> list = patternMakingService.findBySampleDesignId(styleId);
        return list;
    }

    @ApiOperation(value = "打版指令明细", notes = "通过id查询")
    @GetMapping("/{id}")
    public StylePmDetailVo getById(@PathVariable("id") String id) {
        return patternMakingService.getDetailById(id);
    }

    @ApiOperation(value = "保存附件-纸样文件")
    @PostMapping("/saveAttachment")
    public boolean saveAttachment(@Valid @RequestBody SaveAttachmentDto dto) {
        return patternMakingService.saveAttachment(dto);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping()
    public Boolean removeById(RemoveDto removeDto) {
        return patternMakingService.removeByIds(removeDto);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public PatternMaking save(@Valid @RequestBody PatternMakingDto dto) {
        PatternMaking patternMaking = patternMakingService.savePatternMaking(dto);
        OperaLogEntity operaLogEntity =new OperaLogEntity();
        operaLogEntity.setType("新增");
        operaLogEntity.setName("打板指令");
        operaLogEntity.setDocumentId(patternMaking.getId());
        operaLogEntity.setDocumentCode(patternMaking.getCode());
        operaLogEntity.setParentId(patternMaking.getStyleId());
        operaLogEntity.setDocumentName(patternMaking.getPatternNo());
        patternMakingService.saveOrUpdateOperaLog(patternMaking, null, operaLogEntity);
        return patternMaking;
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public PatternMaking update(@RequestBody PatternMakingDto dto) {
        PatternMaking patternMaking = BeanUtil.copyProperties(dto, PatternMaking.class);


//        patternMakingService.checkPatternNoRepeat(dto.getId(), dto.getPatternNo());
        patternMakingService.checkPatSeqRepeat(dto.getStyleId(), dto.getId(), dto.getPatSeq());
        PatternMaking old = patternMakingService.getById(dto.getId());

        if (StrUtil.isAllNotBlank(dto.getNode(), dto.getStatus())) {
            patternMakingService.sort(old, true);
        }
        if (!ObjectUtil.equal(old.getRequirementNum(), dto.getRequirementNum())) {
            BigDecimal sampleFinishNum = Optional.ofNullable(dto.getSampleFinishNum()).orElse(old.getSampleFinishNum());
            patternMaking.setSampleFinishNum(Optional.ofNullable(sampleFinishNum).orElse(dto.getRequirementNum()));
            patternMaking.setCutterFinishNum(Optional.ofNullable(old.getCutterFinishNum()).orElse(dto.getRequirementNum()));
        }
        patternMakingService.updateById(patternMaking);
        if (StrUtil.isAllNotBlank(dto.getNode(), dto.getStatus())) {
            patternMakingService.sort(dto, false);
        }
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setType("修改");
        operaLogEntity.setName("打板指令");
        operaLogEntity.setParentId(dto.getStyleId());
        operaLogEntity.setDocumentId(dto.getId());
        operaLogEntity.setDocumentCode(dto.getCode());
        operaLogEntity.setDocumentName(dto.getPatternNo());
        patternMakingService.saveOrUpdateOperaLog(patternMaking, old, operaLogEntity);
        return patternMaking;
    }

    @ApiOperation(value = "修改二次加工信息")
    @PutMapping("/updateSecondProcessing")
    public boolean updateSecondProcessing(@RequestBody PatternMakingDto dto) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.lambda().set(PatternMaking::getSecondProcessing, dto.getSecondProcessing())
                .eq(PatternMaking::getId, dto.getId());
        patternMakingService.update(uw);
        return true;
    }

    @ApiOperation(value = "款式设计下发", notes = "打版指令从款式设计下发到技术中心看板")
    @PostMapping("/sampleDesignSend")
    public boolean sampleDesignSend(@Valid @RequestBody StyleSendDto dto) {
        return patternMakingService.sampleDesignSend(dto);
    }

    @ApiOperation(value = "版房主管下发", notes = "打版指令从技术中心看板下发到打版任务")
    @PostMapping("/prmSend")
    public Integer prmSend(@Valid @RequestBody List<SetPatternDesignDto> dtos) {
        return patternMakingService.prmSendBatch(dtos);
    }


    @ApiOperation(value = "指定版师")
    @PostMapping("/setPatternDesign")
    public boolean setPatternDesign(@Valid @RequestBody SetPatternDesignDto dto) {
        return patternMakingService.setPatternDesign(dto);
    }

    @ApiOperation(value = "指定版师批量")
    @PostMapping("/setPatternDesignBatch")
    public boolean setPatternDesignBatch(@Valid @RequestBody List<SetPatternDesignDto> dto) {
        return patternMakingService.setPatternDesignBatch(dto);
    }

    @ApiOperation(value = "获取版师列表")
    @GetMapping("/getPatternDesignList")
    @ApiImplicitParams({@ApiImplicitParam(name = "planningSeasonId", value = "产品季节id", required = true, paramType = "query")})
    public List<PatternDesignVo> getPatternDesignList(@Valid @NotBlank(message = "产品季节id不能为空") String planningSeasonId) {
        return patternMakingService.getPatternDesignList(planningSeasonId);
    }

    @ApiOperation(value = "中断/启用样衣")
    @GetMapping("/breakOffSample")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean breakOffSample(@Valid @NotBlank(message = "id不能为空") String id, String flag) {
        return patternMakingService.breakOffSample(id, flag);
    }

    @ApiOperation(value = "中断打版")
    @GetMapping("/breakOffPattern")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query"),
                    @ApiImplicitParam(name = "flag", value = "0重启，1中断", required = true, paramType = "query")})
    public boolean breakOffPattern(@Valid @NotBlank(message = "id不能为空") String id, @Valid @NotBlank(message = "flag不能为空") String flag) {
        return patternMakingService.breakOffPattern(id,flag);
    }


    @ApiOperation(value = "打版管理任务-列表", notes = "")
    @GetMapping("/patternMakingTaskList")
    public PageInfo<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto) {
        return patternMakingService.patternMakingTaskList(dto);
    }

    @ApiOperation(value = "设置排序", notes = "")
    @PostMapping("/setSort")
    public Integer setSort(@Valid @RequestBody List<SetSortDto> dtoList) {
        return patternMakingService.setSort(dtoList);
    }

    @ApiOperation(value = "挂起", notes = "")
    @PostMapping("/suspend")
    public boolean suspend(@Valid @RequestBody SuspendDto dto) {
        return patternMakingService.suspend(dto);
    }

    @ApiOperation(value = "取消挂起", notes = "")
    @GetMapping("/cancelSuspend")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean cancelSuspend(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.cancelSuspend(id);
    }

    @ApiOperation(value = "工艺员设置齐套", notes = "")
    @PostMapping("/technicianKitting")
    public boolean technicianKitting(@Valid @RequestBody SetKittingDto dto) {
        return patternMakingService.setKitting("technician_", dto);
    }

    @ApiOperation(value = "样衣组长设置齐套", notes = "")
    @PostMapping("/sglKitting")
    public boolean sglKitting(@Valid @RequestBody SetKittingDto dto) {
        return patternMakingService.setKitting("sgl_", dto);
    }

    @ApiOperation(value = "打版进度列表", notes = "")
    @GetMapping("/patternMakingSteps")
    public PageInfo patternMakingSteps(PatternMakingCommonPageSearchDto dto) {
        return patternMakingService.patternMakingSteps(dto);
    }

    @ApiOperation(value = "研发总进度", notes = "")
    @GetMapping("/allProgressSteps")
    public PageInfo<NodeListVo> allProgressSteps(NodestatusPageSearchDto dto, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        return patternMakingService.allProgressSteps(dto, userCompany);
    }

    @ApiOperation(value = "研发总进度", notes = "研发总进度new")
    @GetMapping("/researchProcessList")
    public PageInfo<StyleResearchProcessVo> researchProcessList(ResearchProgressPageDto dto, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        return patternMakingService.researchProcessList(dto, userCompany);
    }

    @ApiOperation(value = "工作台使用的打版进度列表", notes = "")
    @GetMapping("/work/patternMakingSteps")
    public Map patternMakingSteps0(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        return patternMakingService.patternMakingSteps0(userCompany);
    }

    @ApiOperation(value = "样衣看板列表", notes = "")
    @GetMapping("/sampleBoardList")
    public PatternMakingCommonPageSearchVo sampleBoardList(PatternMakingCommonPageSearchDto dto) {
        return patternMakingService.sampleBoardList(dto);
    }

    @ApiOperation(value = "获取评分", notes = "")
    @GetMapping("/getWorkloadById")
    public SampleBoardVo getWorkloadById(String id) {
        return patternMakingService.getWorkloadById(id);
    }

    @ApiOperation(value = "导出", notes = "")
    @GetMapping("/deriveExcel")
    @DuplicationCheck(type = 1,message = "服务正在导出请稍等",time = 60)
    public void deriveExcel(HttpServletResponse response, PatternMakingCommonPageSearchDto dto) throws IOException, InterruptedException {
         patternMakingService.deriveExcel(response,dto);
    }



    @ApiOperation(value = "确认收到样衣", notes = "")
    @GetMapping("/receiveSample")
    public boolean receiveSample(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.receiveSample(id);
    }

    @ApiOperation(value = "版师列表", notes = "")
    @GetMapping("/getAllPatternDesignList")
    public List<SampleUserVo> getAllPatternDesignList(PatternUserSearchVo vo) {
        return patternMakingService.getAllPatternDesignList(vo);
    }

    @ApiOperation(value = "打样设计师列表", notes = "")
    @GetMapping("/getAllPatternDesignerList")
    public List<SampleUserVo> getAllPatternDesignerList(PatternUserSearchVo vo) {
        return patternMakingService.getAllPatternDesignerList(vo);
    }

    @ApiOperation(value = "所有裁剪工列表", notes = "")
    @GetMapping("/getAllCutterList")
    public List<SampleUserVo> getAllCutterList(PatternUserSearchVo vo) {

        return patternMakingService.getAllCutterList(vo);
    }

    @ApiOperation(value = "所有车缝工列表", notes = "")
    @GetMapping("/getAllStitcherList")
    public List<SampleUserVo> getAllStitcherList(PatternUserSearchVo vo) {
        return patternMakingService.getAllStitcherList(vo);
    }

    @ApiOperation(value = "板房数据总览", notes = "")
    @GetMapping("/prmDataOverview")
    public List prmDataOverview(String time) {
        return patternMakingService.prmDataOverview(time);
    }

    @ApiOperation(value = "版类对比统计", notes = "")
    @GetMapping("/versionComparisonViewWeekMonth")
    public ApiResult versionComparisonViewWeekMonth(@RequestHeader(BaseConstant.AUTHORIZATION) String token, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功", patternMakingService.versionComparisonViewWeekMonth(patternMakingWeekMonthViewDto, token));
    }

    @ApiOperation(value = "获取节点状态配置", notes = "")
    @GetMapping("/getNodeStatusConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "node", value = "节点", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dataId", value = "打版id", required = false, dataType = "String", paramType = "query"),
    })
    public JSONObject getNodeStatusConfig(Principal user, String node, String status, String dataId) {
        GroupUser userBy = userUtils.getUserBy(user);
        JSONObject nodeStatusConfig = patternMakingService.getNodeStatusConfig(userBy, node, status, dataId);
        // 打版管理-样衣任务-是否显示裁剪开始、完成列表
        if (ccmFeignService.getSwitchByCode(CcmBaseSettingEnum.PATTERN_MAKING_SAMPLE_CROPPING_SWITCH.getKeyCode())) {
            nodeStatusConfig.remove(EnumNodeStatus.GARMENT_CUTTING_STARTED.getStatus());
            nodeStatusConfig.remove(EnumNodeStatus.GARMENT_CUTTING_COMPLETE.getStatus());
        }
        return nodeStatusConfig;
    }

    @ApiOperation(value = "分配人员(车缝工)", notes = "")
    @PostMapping("/assignmentUser")
    @Transactional(rollbackFor = Exception.class)
    public boolean assignmentUser(Principal user, @RequestHeader(BaseConstant.ENV) String env, @Valid @RequestBody AssignmentUserDto dto) {
        GroupUser groupUser = userUtils.getUserBy(user);
        boolean b = patternMakingService.assignmentUser(groupUser, dto);

            if (b && StrUtil.isNotBlank(dto.getSampleBarCode())) {
                smpService.sample(new String[]{dto.getId()},env);
            }

        return b;
    }

    @ApiOperation(value = "暂存分配人员(车缝工)信息")
    @PostMapping("/savePatternMaking")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<String> savePatternMaking(@RequestBody AssignmentUserDto dto) {
        patternMakingService.savePatternMaking(dto);
        return ApiResult.success("暂存成功");
    }

    @ApiOperation(value = "版师任务明细", notes = "")
    @GetMapping("/pdTaskDetail")
    public List<PatternDesignVo> pdTaskDetail(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode) {
        return patternMakingService.pdTaskDetail(companyCode);
    }

    @ApiOperation(value = "获取制版单-样衣新增", notes = "")
    @GetMapping("/getAllList")
    public PageInfo getAllList(PatternMakingCommonPageSearchDto dto) {
        return patternMakingService.queryPageInfo(dto);
    }


    @ApiOperation(value = "品类汇总统计", notes = "")
    @PostMapping("/categorySummaryCount")
    public ApiResult categorySummaryCount(@RequestHeader(BaseConstant.AUTHORIZATION) String token, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功", patternMakingService.categorySummaryCount(patternMakingWeekMonthViewDto, token));
    }

    @ApiOperation(value = "统计样衣产能总数", notes = "")
    @PostMapping("/sampleCapacityTotalCount")
    public ApiResult sampleCapacityTotalCount(@RequestHeader(BaseConstant.AUTHORIZATION) String token, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功", patternMakingService.sampleCapacityTotalCount(patternMakingWeekMonthViewDto, token));
    }

    @ApiOperation(value = "产能对比", notes = "")
    @PostMapping("/capacityContrastStatistics")
    public ApiResult capacityContrastStatistics(@RequestHeader(BaseConstant.AUTHORIZATION) String token, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功", patternMakingService.capacityContrastStatistics(patternMakingWeekMonthViewDto, token));
    }

    @ApiOperation(value = "前往下一个节点", notes = "")
    @GetMapping("/next")
    public boolean next(Principal user, @Validated IdDto idDto) {
        return patternMakingService.nextOrPrev(user, idDto.getId(), NodeStatusConfigService.NEXT);
    }

    @ApiOperation(value = "前往上一个节点", notes = "")
    @GetMapping("/prev")
    public boolean prev(Principal user, @Validated IdDto idDto) {
        return patternMakingService.nextOrPrev(user, idDto.getId(), NodeStatusConfigService.PREV);
    }

    @ApiOperation(value = "版师工作量打分", notes = "")
    @PostMapping("/patternMakingScore")
    public boolean patternMakingScore(Principal user, @Validated @RequestBody ScoreDto dto) {
        return patternMakingService.patternMakingScore(user, dto.getId(), dto.getScore());
    }

    @ApiOperation(value = "版师的质量打分", notes = "")
    @PostMapping("/patternMakingQualityScore")
    public boolean patternMakingQualityScore(Principal user, @Validated @RequestBody ScoreDto dto) {
        return patternMakingService.patternMakingQualityScore(user, dto.getId(), dto.getScore());
    }

    @ApiOperation(value = "样衣制作评分", notes = "")
    @PostMapping("/sampleMakingScore")
    public boolean sampleMakingScore(@RequestBody PatternMakingDto dto) {
        return patternMakingService.sampleMakingScore(dto);
    }


    @ApiOperation(value = "样衣工的质量打分", notes = "")
    @PostMapping("/sampleMakingQualityScore")
    public boolean sampleMakingQualityScore(@Validated @RequestBody ScoreDto dto) {
        return patternMakingService.sampleMakingQualityScore(dto.getId(), dto.getScore());
    }

    @ApiOperation(value = "样衣工编辑", notes = "")
    @PostMapping("/sampleMakingEdit")
    public boolean sampleMakingEdit(@Validated @RequestBody PatternMakingDto dto) {
        return patternMakingService.sampleMakingEdit(dto);
    }

    @ApiOperation(value = "设置样衣条码", notes = "")
    @PostMapping("/setSampleBarCode")
    public boolean setSampleBarCode(@Validated @RequestBody SetSampleBarCodeDto dto, @RequestHeader(BaseConstant.ENV) String env) {
        patternMakingService.setSampleBarCode(dto);
        smpService.sample(new String[]{dto.getId()}, env);
        return true;
    }

    @ApiOperation(value = "获取车缝工列表")
    @GetMapping("/getStitcherList")
    @ApiImplicitParams({@ApiImplicitParam(name = "planningSeasonId", value = "产品季节id", required = true, paramType = "query")})
    public List<PatternDesignVo> getStitcherList(@Valid @NotBlank(message = "产品季节id不能为空") String planningSeasonId) {
        return patternMakingService.getStitcherList(planningSeasonId);
    }

    @ApiOperation(value = "完成流程")
    @GetMapping("/finish")
    public boolean finish(IdDto dto) {
        return patternMakingService.finish(dto.getId());
    }

    @ApiOperation(value = "样衣图上传", notes = "")
    @PostMapping("/samplePicUpload")
    public boolean samplePicUpload(@Validated @RequestBody SamplePicUploadDto dto) {
        return patternMakingService.samplePicUpload(dto);
    }


    @PostMapping("/updateGrading")
    @ApiOperation(value = "修改放码师")
    public boolean updateGrading(@Validated @RequestBody PatternMaking patternMaking) {
        CommonUtils.removeQuery(patternMaking, "samplePic");
        LambdaUpdateWrapper<PatternMaking> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PatternMaking::getId,patternMaking.getId());
        updateWrapper.set(PatternMaking::getGradingId,patternMaking.getGradingId());
        updateWrapper.set(PatternMaking::getGradingName, patternMaking.getGradingName());
        updateWrapper.set(PatternMaking::getGradingDate, patternMaking.getGradingDate());
        return patternMakingService.update(updateWrapper);
    }


    @PostMapping("/startStop")
    @ApiOperation(value = "停用启用")
    public boolean startStop(@Valid @RequestBody StartStopDto startStopDto) {
        return patternMakingService.startStop(startStopDto);
    }

    @ApiOperation(value = "滞留款报表保存停留原因")
    @PostMapping("/saveReceiveReason")
    public boolean saveReceiveReason(@RequestBody TechnologyCenterTaskVo dto) {
        patternMakingService.saveReceiveReason(dto);
        return true;
    }


    @ApiOperation(value = "获取款下面的初版车缝工和上次车缝工")
    @GetMapping("/getHeadLastTimeStitcher")
    public Map<String,String> getHeadLastTimeStitcher(PatternMakingDto dto) {
        return patternMakingService.getHeadLastTimeStitcher(dto.getStyleId());
    }

    @ApiOperation(value = "获取到设计款下面的样衣")
    @GetMapping("/getSampleDressByStyleId")
    public List<PatternMakingVo> getSampleDressBydesignNo(PatternMakingDto dto) {
        return patternMakingService.getSampleDressBydesignNo(dto.getStyleId());
    }
    @ApiOperation(value = "样衣任务-待接收下一步-是否参照样衣设置")
    @PostMapping("/updateReferSample")
    public boolean updateReferSample(@RequestBody @Valid PatternMakingReferSampleDto dto) {
        return patternMakingService.updateReferSample(dto);
    }

    @ApiOperation(value = "样衣看板-设计收到时间编辑")
    @PostMapping("/updateDesignReceiptDate")
    public boolean editDesignReceiptDate(@RequestBody PatternMakingDesignReceiptDto dto) {
        patternMakingService.updateDesignReceiptDate(dto);
        return true;
    }



}
