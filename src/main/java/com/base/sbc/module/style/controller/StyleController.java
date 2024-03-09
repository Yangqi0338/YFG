/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.formtype.entity.FieldValOld;
import com.base.sbc.module.formtype.service.FieldValOldService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.pack.dto.PackInfoDto;
import com.base.sbc.module.pack.dto.PlanningDemandStatisticsResultVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.DesignDocTreeVo;
import com.base.sbc.module.style.vo.StyleVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：款式设计 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.web.SampleDesignController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@RestController
@Api(tags = "款式设计相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/style", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleController extends BaseController {

    @Autowired
    private StyleService styleService;
    @Autowired
    private FieldValOldService fieldValOldService;


    @ApiOperation(value = "分页查询(设计任务,设计档案)")
    @GetMapping()
    public PageInfo pageInfo(Principal user, @Valid StylePageDto dto) {
        return styleService.queryPageInfo(user, dto);
    }

    @ApiOperation(value = "旧维度查询(维度信息)")
    @GetMapping("/fieid/{dataGroup}/{id}")
    public List<FieldValOld> fieIdOldInfo(@PathVariable("dataGroup") String dataGroup, @PathVariable("id") String id) {
        return fieldValOldService.list(id,dataGroup);
    }
    /**
     * @param id
     * @param historyStyleId 引用历史款时使用
     * @return
     */
    @ApiOperation(value = "明细信息")
    @GetMapping("/{id}")
    public StyleVo getDetail(@PathVariable("id") String id, String historyStyleId) {
        StyleVo detail = styleService.getDetail(id, historyStyleId);

        return detail;
    }

    @ApiOperation(value = "保存")
    @PostMapping
    @DuplicationCheck
    public StyleVo save(@RequestBody StyleSaveDto dto) {
        Style style = styleService.saveStyle(dto);
        return styleService.getDetail(style.getId());
    }

    @ApiOperation(value = "设置主图")
    @PostMapping("/setMainPic")
    public boolean setMainPic(@RequestBody StyleSaveDto dto) {
        return styleService.setMainPic(dto);
    }

    @ApiOperation(value = "发送打板指令")
    @PostMapping("/sendMaking")
    public boolean sendMaking(@Valid @RequestBody SendSampleMakingDto dto) {
        return styleService.sendMaking(dto);
    }

    @ApiOperation(value = "发起审批")
    @GetMapping("/startApproval")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")})
    public boolean startApproval(@NotBlank(message = "编号不能为空") String id) {
        return styleService.startApproval(id);
    }

    /**
     * 处理审批
     *
     * @param dto
     * @return
     */
    @ApiIgnore
    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        return styleService.approval(dto);
    }


    @ApiOperation(value = "设计档案左侧树", notes = "（0级:年份季节,1级:波段,2级:大类,3级:品类）")
    @PostMapping("/queryDesignDocTree")
    public List<DesignDocTreeVo> queryDesignDocTree(@RequestBody DesignDocTreeVo designDocTreeVo) {
        return styleService.queryDesignDocTree(designDocTreeVo);
    }


    @ApiOperation(value = "查询款式设计维度数据", notes = "")
    @GetMapping("/queryDimensionLabelsByStyle")
    public List<FieldManagementVo> queryDimensionLabelsByStyle(DimensionLabelsSearchDto dto) {
        if (dto == null) {
            return null;
        }
        dto.setForeignId(dto.getId());
        return styleService.queryDimensionLabelsByStyle(dto);
    }


    @ApiOperation(value = "查询款式设计围度系数", notes = "")
    @GetMapping("/queryCoefficientByStyle")
    public Map<String,List<FieldManagementVo>> queryCoefficientByStyle(DimensionLabelsSearchDto dto) {
        if (dto == null) {
            return null;
        }
        dto.setForeignId(dto.getId());
        return styleService.queryCoefficientByStyle(dto);
    }


    @ApiOperation(value = "设计师列表", notes = "")
    @GetMapping("/getDesignerList")
    public List<SampleUserVo> getDesignerList(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode) {
        return styleService.getDesignerList(companyCode);
    }

    @ApiOperation(value = "物料信息-分页查询", notes = "")
    @GetMapping("/bom")
    public PageInfo<PackBomVo> bomList(StyleBomSearchDto dto) {
        return styleService.bomList(dto);
    }

    @ApiOperation(value = "物料信息-删除", notes = "")
    @DeleteMapping("/bom")
    public Boolean delBom(@Validated IdsDto dto) {
        return styleService.delBom(dto.getId());
    }


    @ApiOperation(value = "物料信息-保存", notes = "")
    @PostMapping("/bom")
    public Boolean saveBom(@RequestBody StyleBomSaveDto dto) {
        return styleService.saveBom(dto);
    }

    /**
     * 验证款式下的尺码是否可修改
     * @param publicStyleColorDto
     * @return
     */
    @ApiOperation(value = "验证尺码组是否可修改")
    @PostMapping("/checkColorSize")
    public Boolean checkColorSize(@Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
        return styleService.checkColorSize(publicStyleColorDto);
    }


    @ApiOperation(value = "企划需求统计")
    @PostMapping("/planningDemandStatistics")
    public PlanningDemandStatisticsResultVo planningDemandStatistics(@Valid @RequestBody IdDto idDto) {
        return styleService.planningDemandStatistics(idDto.getId());
    }
    @ApiOperation(value = "保存款式详情颜色并生成SKU")
    @PostMapping("/saveBomInfoColorList")
    public ApiResult saveBomInfoColorList(@RequestBody PackInfoDto packInfoDto) {
        if (null == packInfoDto || CollectionUtil.isEmpty(packInfoDto.getStyleInfoColorDtoList())  || StringUtils.isEmpty(packInfoDto.getProductSizes())
                || StringUtils.isEmpty(packInfoDto.getId()) || StringUtils.isEmpty(packInfoDto.getSizeCodes()) ) {
           return ApiResult.error("相关参数不能为空：款式详情颜色列表不能为空 || 尺码集合不能为空 ||  尺码Code集合不能为空", 500);
        }
        return ApiResult.success("新增成功" , styleService.saveBomInfoColorList(packInfoDto));
    }

    @ApiOperation(value = "生成设计款号")
    @PostMapping("/genDesignNo")
    public ApiResult genDesignNo(@RequestBody Style style) {
        if (null == style || StringUtils.isBlank(style.getSeriesId()) || StringUtils.isBlank(style.getDesignChannelId()) || StringUtils.isBlank(style.getYear())
                || StringUtils.isBlank(style.getSeason()) || StringUtils.isBlank(style.getProdCategory3rd())) {
            return ApiResult.error("相关参数不能为空：款式详情系列不能为空 || 设计渠道不能为空 || 年份不能为空 || 季节不能为空 || 小类不能为空", 500);
        }
        return ApiResult.success("新增成功" , styleService.genDesignNo(style));
    }

    @ApiOperation(value = "获取设计款号最大流水号")
    @PostMapping("/maxDesignNo")
    public String maxDesignNo(@RequestBody GetMaxCodeRedis data) {
        if (ObjectUtil.isEmpty(data.getValueMap())) {
            throw new OtherException("条件不能为空");
        }
        return styleService.getMaxDesignNo(data,getUserCompany());
    }


    @ApiOperation(value = "保存设计款号")
    @PostMapping("/saveDesignNo")
    public ApiResult saveDesignNo(@RequestBody Style style) {
        if (null == style || StringUtils.isBlank(style.getId()) || StringUtils.isBlank(style.getDesignNo())) {
            return ApiResult.error("相关参数不能为空：款式详情id不能为空 || 设计款号不能为空 ", 500);
        }
        styleService.saveDesignNo(style);
        return ApiResult.success("新增成功");
    }

    @ApiOperation(value = "启用/停用", notes = "ids:, status:0启用1停用")
    @PostMapping("/startStopStyle")
    public Boolean startStopBasicsdatumWashIcon(@Valid @RequestBody StartStopDto startStopDto) {
        return styleService.startStopStyle(startStopDto);
    }

    @ApiOperation(value = "发起审批-款式打标-设计阶段")
    @GetMapping("/startMarkingApproval")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")})
    public boolean startMarkingApproval(@NotBlank(message = "编号不能为空") String id, String showFOB, String styleColorId) {
        return styleService.startMarkingApproval(id, showFOB, styleColorId);
    }
    /**
     * 根据设计款号获取详情
     */
    @ApiOperation(value = "获取详情")
    @GetMapping("/getByDesignNo")
    public ApiResult getByDesignNo(String designNo) {
        Style style = styleService.getByOne("design_no", designNo);
        return selectSuccess(style);
    }



    /**
     * 处理审批-款式打标-设计阶段
     *
     * @param dto
     * @return
     */
    @ApiIgnore
    @PostMapping("/approvalMarking")
    public boolean approvalMarking(@RequestBody AnswerDto dto) {
        return styleService.approvalMarking(dto);
    }

    @ApiOperation(value = "发起审批-款式打标-下单阶段")
    @GetMapping("/startMarkingOrderApproval")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")})
    public boolean startMarkingOrderApproval(@NotBlank(message = "编号不能为空") String id, String showFOB, String styleColorId) {
        return styleService.startMarkingOrderApproval(id, showFOB, styleColorId);
    }

    /**
     * 处理审批-款式打标-下单阶段
     *
     * @param dto
     * @return
     */
    @ApiIgnore
    @PostMapping("/approvalMarkingOrder")
    public boolean approvalMarkingOrder(@RequestBody AnswerDto dto) {
        return styleService.approvalMarkingOrder(dto);
    }


    @ApiOperation(value = "打板保存系数")
    @PostMapping("/saveCoefficient")
    public boolean saveCoefficient(@RequestBody SaveCoefficientDto saveCoefficientDto) {
        return styleService.saveCoefficient(saveCoefficientDto.getFieldValList(),saveCoefficientDto.getStyleId());
    }

}
