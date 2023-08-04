/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.Style;
import com.base.sbc.module.sample.service.StyleService;
import com.base.sbc.module.sample.vo.DesignDocTreeVo;
import com.base.sbc.module.sample.vo.StyleVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
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
public class StyleController {

    @Autowired
    private StyleService styleService;

    @ApiOperation(value = "分页查询")
    @GetMapping()
    public PageInfo pageInfo(@Valid SampleDesignPageDto dto) {
        return styleService.queryPageInfo(dto);
    }

    @ApiOperation(value = "查询款式设计及款式配色")
    @GetMapping("/sampleSampleStyle")
    public PageInfo sampleSampleStyle(Principal user, @Valid SampleDesignPageDto dto) {
        return styleService.sampleSampleStyle(user, dto);
    }

    @ApiOperation(value = "明细信息")
    @GetMapping("/{id}")
    public StyleVo getDetail(@PathVariable("id") String id) {
        return styleService.getDetail(id);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public StyleVo save(@RequestBody StyleSaveDto dto) {
        Style style = styleService.saveSampleDesign(dto);
        return styleService.getDetail(style.getId());
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


    @ApiOperation(value = "查询款式设计维度数据(新增时使用)", notes = "")
    @PostMapping("/queryDimensionLabels")
    public List<FieldManagementVo> queryDimensionLabels(@Valid @RequestBody DimensionLabelsSearchDto dto) {
        return styleService.queryDimensionLabels(dto);
    }

    @ApiOperation(value = "查询款式设计维度数据(修改时使用)", notes = "")
    @GetMapping("/queryDimensionLabelsBySdId")
    public List<FieldManagementVo> queryDimensionLabelsBySdId(String id) {
        return styleService.queryDimensionLabelsBySdId(id);
    }

    @ApiOperation(value = "设计师列表", notes = "")
    @GetMapping("/getDesignerList")
    public List<SampleUserVo> getDesignerList(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode) {
        return styleService.getDesignerList(companyCode);
    }

    @ApiOperation(value = "物料信息-分页查询", notes = "")
    @GetMapping("/bom")
    public PageInfo<PackBomVo> bomList(SampleDesignBomSearchDto dto) {
        return styleService.bomList(dto);
    }

    @ApiOperation(value = "物料信息-删除", notes = "")
    @DeleteMapping("/bom")
    public Boolean delBom(@Validated IdsDto dto) {
        return styleService.delBom(dto.getId());
    }


    @ApiOperation(value = "物料信息-保存", notes = "")
    @PostMapping("/bom")
    public Boolean saveBom(@RequestBody SampleDesignBomSaveDto dto) {
        return styleService.saveBom(dto);
    }
}































