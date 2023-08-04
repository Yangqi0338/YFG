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
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.vo.DesignDocTreeVo;
import com.base.sbc.module.sample.vo.SampleDesignVo;
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
 * 类描述：样衣设计 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.web.SampleDesignController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@RestController
@Api(tags = "样衣设计相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleDesign", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleDesignController {

    @Autowired
    private SampleDesignService sampleDesignService;

    @ApiOperation(value = "分页查询")
    @GetMapping()
    public PageInfo pageInfo(@Valid SampleDesignPageDto dto) {
        return sampleDesignService.queryPageInfo(dto);
    }

    @ApiOperation(value = "查询样衣设计及款式配色")
    @GetMapping("/sampleSampleStyle")
    public PageInfo sampleSampleStyle(Principal user, @Valid SampleDesignPageDto dto) {
        return sampleDesignService.sampleSampleStyle(user,dto);
    }

    @ApiOperation(value = "明细信息")
    @GetMapping("/{id}")
    public SampleDesignVo getDetail(@PathVariable("id") String id) {
        return sampleDesignService.getDetail(id);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public SampleDesignVo save(@RequestBody SampleDesignSaveDto dto) {
        SampleDesign sampleDesign = sampleDesignService.saveSampleDesign(dto);
        return sampleDesignService.getDetail(sampleDesign.getId());
    }

    @ApiOperation(value = "发送打板指令")
    @PostMapping("/sendMaking")
    public boolean sendMaking(@Valid @RequestBody SendSampleMakingDto dto) {
        return sampleDesignService.sendMaking(dto);
    }

    @ApiOperation(value = "发起审批")
    @GetMapping("/startApproval")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")})
    public boolean startApproval(@NotBlank(message = "编号不能为空") String id) {
        return sampleDesignService.startApproval(id);
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
        return sampleDesignService.approval(dto);
    }


    @ApiOperation(value = "设计档案左侧树", notes = "（0级:年份季节,1级:波段,2级:大类,3级:品类）")
    @PostMapping("/queryDesignDocTree")
    public List<DesignDocTreeVo> queryDesignDocTree(@RequestBody DesignDocTreeVo designDocTreeVo) {
        return sampleDesignService.queryDesignDocTree(designDocTreeVo);
    }


    @ApiOperation(value = "查询样衣设计维度数据(新增时使用)", notes = "")
    @PostMapping("/queryDimensionLabels")
    public List<FieldManagementVo> queryDimensionLabels(@Valid @RequestBody DimensionLabelsSearchDto dto) {
        return sampleDesignService.queryDimensionLabels(dto);
    }

    @ApiOperation(value = "查询样衣设计维度数据(修改时使用)", notes = "")
    @GetMapping("/queryDimensionLabelsBySdId")
    public List<FieldManagementVo> queryDimensionLabelsBySdId(String id) {
        return sampleDesignService.queryDimensionLabelsBySdId(id);
    }

    @ApiOperation(value = "设计师列表", notes = "")
    @GetMapping("/getDesignerList")
    public List<SampleUserVo> getDesignerList(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode) {
        return sampleDesignService.getDesignerList(companyCode);
    }

    @ApiOperation(value = "物料信息-分页查询", notes = "")
    @GetMapping("/bom")
    public PageInfo<PackBomVo> bomList(SampleDesignBomSearchDto dto) {
        return sampleDesignService.bomList(dto);
    }

    @ApiOperation(value = "物料信息-删除", notes = "")
    @DeleteMapping("/bom")
    public Boolean delBom(@Validated IdsDto dto) {
        return sampleDesignService.delBom(dto.getId());
    }


    @ApiOperation(value = "物料信息-保存", notes = "")
    @PostMapping("/bom")
    public Boolean saveBom(@RequestBody SampleDesignBomSaveDto dto) {
        return sampleDesignService.saveBom(dto);
    }
}































