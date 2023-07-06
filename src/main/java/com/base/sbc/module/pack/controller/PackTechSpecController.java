/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.PackTechSpecDto;
import com.base.sbc.module.pack.dto.PackTechSpecPageDto;
import com.base.sbc.module.pack.dto.PackTechSpecSavePicDto;
import com.base.sbc.module.pack.dto.PackTechSpecSearchDto;
import com.base.sbc.module.pack.service.PackTechSpecService;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：资料包-工艺说明 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackTechSpecController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 15:41:45
 */
@RestController
@Api(tags = "资料包-工艺说明")
@RequestMapping(value = BaseController.SAAS_URL + "/packTechSpec", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackTechSpecController {

    @Autowired
    private PackTechSpecService packTechSpecService;
    @Autowired
    private AttachmentService attachmentService;

    @ApiOperation(value = "列表")
    @GetMapping
    public List<PackTechSpecVo> page(@Valid PackTechSpecSearchDto dto) {
        return packTechSpecService.list(dto);
    }


    @ApiOperation(value = "文件列表(图片)")
    @GetMapping("/picList")
    public List<AttachmentVo> picList(@Valid PackTechSpecSearchDto dto) {
        return packTechSpecService.picList(dto);
    }

    @ApiOperation(value = "保存图片")
    @PostMapping("/savePic")
    public AttachmentVo savePic(@Valid @RequestBody PackTechSpecSavePicDto dto) {
        return packTechSpecService.savePic(dto);
    }

    @ApiOperation(value = "删除图片")
    @DeleteMapping("/delPic")
    public boolean del(@Valid IdsDto ids) {
        return attachmentService.del(ids.getId());
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping()
    @OperaLog(value = "'资料包-工艺说明-'+#search.packType+'-'+#search.specType+'-'+#search.foreignId", delIdSpEL = "#dto.id", operationType = OperationType.DELETE, service = PackTechSpecService.class, SqEL = true)
    public Boolean removeById(@Valid PackTechSpecSearchDto search, @Valid IdsDto dto) {
        return packTechSpecService.removeByIds(StringUtils.convertList(dto.getId()));
    }

    @ApiOperation(value = "保存")
    @PostMapping
    @OperaLog(value = "'资料包-工艺说明-'+#dto.packType+'-'+#dto.specType+'-'+#dto.foreignId", operationType = OperationType.INSERT_UPDATE, service = PackTechSpecService.class, SqEL = true)
    public PackTechSpecVo save(@Valid @RequestBody PackTechSpecDto dto) {
        return packTechSpecService.saveByDto(dto);
    }

    @ApiOperation(value = "排序")
    @GetMapping("/sort")
    public boolean sort(@Valid IdsDto dto) {
        return packTechSpecService.sort(dto.getId());
    }

    @ApiOperation(value = "变更日志")
    @GetMapping("/operationLog")
    public PageInfo<OperaLogEntity> operationLog(@Valid PackTechSpecPageDto pageDto) {
        return packTechSpecService.operationLog(pageDto);
    }
}































