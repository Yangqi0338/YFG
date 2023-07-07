package com.base.sbc.module.pack.controller;


import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackPatternAttachmentSaveDto;
import com.base.sbc.module.pack.dto.PackUpdateRemarksDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 类描述：资料包-图样附件 相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.controller.PackPatternAttachmentController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-04 16:44
 */
@RestController
@Api(tags = "资料包-图样附件")
@RequestMapping(value = BaseController.SAAS_URL + "/patternAttachment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackPatternAttachmentController {


    @Resource
    private AttachmentService attachmentService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<AttachmentVo> pageInfo(@Valid PackCommonPageSearchDto dto) {
        return attachmentService.patternAttachmentPageInfo(dto);
    }

    @ApiOperation(value = "保存")
    @PostMapping()
    @OperaLog(value = "图样附件", operationType = OperationType.INSERT_UPDATE, parentIdSpEl = "#p0.foreignId", service = AttachmentService.class)
    public AttachmentVo save(@Valid @RequestBody PackPatternAttachmentSaveDto dto) {
        return attachmentService.saveByPA(dto);
    }

    @ApiOperation(value = "修改备注")
    @PostMapping("/updateRemarks")
    public boolean updateRemarks(@Valid @RequestBody PackUpdateRemarksDto dto) {
        return attachmentService.updateRemarks(dto.getId(), dto.getRemarks());
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public boolean del(@Valid IdsDto ids) {
        return attachmentService.del(ids.getId());
    }


}
