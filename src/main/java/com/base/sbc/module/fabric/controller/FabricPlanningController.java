/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.fabric.dto.FabricPlanningSaveDTO;
import com.base.sbc.module.fabric.dto.FabricPlanningSearchDTO;
import com.base.sbc.module.fabric.service.FabricPlanningItemService;
import com.base.sbc.module.fabric.service.FabricPlanningService;
import com.base.sbc.module.fabric.vo.FabricPlanningListVO;
import com.base.sbc.module.fabric.vo.FabricPlanningVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 类描述：面料企划 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.web.FabricPlanningController
 * @email your email
 * @date 创建时间：2023-8-23 11:03:00
 */
@RestController
@Api(tags = "面料企划")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricPlanningController extends BaseController {

    @Autowired
    private FabricPlanningService fabricPlanningService;
    @Autowired
    private FabricPlanningItemService fabricPlanningItemService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/getFabricPlanningList")
    public PageInfo<FabricPlanningListVO> getFabricPlanningList(@Valid @RequestBody FabricPlanningSearchDTO dto) {
        return fabricPlanningService.getFabricPlanningList(dto);
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/getDetail")
    public ApiResult getDetail(@Valid @NotBlank(message = "面料企划id不可为空") String id) {
        return selectSuccess(fabricPlanningService.getDetail(id));
    }


    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ApiResult save(@Valid @RequestBody FabricPlanningSaveDTO dto) {
        fabricPlanningService.save(dto);
        return updateSuccess("操作成功");
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
        return fabricPlanningService.approval(dto);
    }

    @ApiOperation(value = "获取面料企划明细")
    @GetMapping("/getByFabricPlanningId")
    public ApiResult getByFabricPlanningId(@Valid @NotBlank(message = "面料企划id不可为空") String id, String materialFlag) {
        return selectSuccess(fabricPlanningItemService.getByFabricPlanningId(id, materialFlag));
    }

    @ApiOperation(value = "删除-通过id查询")
    @DeleteMapping
    public ApiResult delete(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) {
            return deleteAttributeNotRequirements("id");
        }
        FabricPlanningVO fabricPlanningVO = fabricPlanningService.getDetail(id);
        if (CollectionUtil.isNotEmpty(fabricPlanningVO.getFabricPlanningItems())) {
            return ApiResult.error("该面料企划还存在明细数据无法删除，请处理！", 500);
        }

        boolean result = fabricPlanningService.removeById(id);
        if (result) {
            return ApiResult.success("删除成功！");
        }
        return deleteNotFound();
    }

    @ApiOperation(value = "/导入")
    @PostMapping("/fabricPlanningItemImportExcel")
    public ApiResult fabricPlanningItemImportExcel(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("fabricPlanningId") String fabricPlanningId) {
        return ApiResult.success("操作成功", fabricPlanningItemService.fabricPlanningItemImportExcel(file, fabricPlanningId));
    }

}































