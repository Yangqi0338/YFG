/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.pack.vo.PackBomVo;
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

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：资料包-物料清单 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackBomController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:22
 */
@RestController
@Api(tags = "资料包-物料清单")
@RequestMapping(value = BaseController.SAAS_URL + "/packBom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackBomController {

    @Autowired
    private PackBomService packBomService;

    @Autowired
    private PackBomVersionService packBomVersionService;

    @ApiOperation(value = "版本列表")
    @GetMapping("/version")
    public PageInfo<PackBomVersionVo> versionPage(@Valid PackCommonPageSearchDto dto) {
        return packBomVersionService.pageInfo(dto);
    }

    @ApiOperation(value = "保存版本信息")
    @PostMapping("/version")
    public PackBomVersionVo saveVersion(@Valid @RequestBody PackBomVersionDto dto) {
        return packBomVersionService.saveVersion(dto);
    }

    @ApiOperation(value = "版本锁定")
    @GetMapping("/version/lock")
    public boolean versionLock(@Valid IdsDto ids) {
        return packBomVersionService.lockChange(ids.getId(), BaseGlobal.YES);
    }

    @ApiOperation(value = "版本解锁")
    @GetMapping("/version/unlock")
    public boolean versionUnlock(@Valid IdsDto ids) {
        return packBomVersionService.lockChange(ids.getId(), BaseGlobal.NO);
    }

    @ApiOperation(value = "版本启用/停用")
    @GetMapping("/version/changeStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "版本id", required = true, dataType = "String", paramType = "query"),
    })
    public boolean changeVersionStatus(@Valid @NotBlank(message = "id不能为空") String id) {
        return packBomVersionService.changeVersionStatus(id);
    }


    @ApiOperation(value = "物料清单分页查询")
    @GetMapping()
    public PageInfo<PackBomVo> packBomPage(@Valid PackBomPageSearchDto dto) {
        return packBomService.pageInfo(dto);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存单个物料清单")
    @OperaLog(value = "物料清单", operationType = OperationType.INSERT_UPDATE, pathSpEL = PackUtils.pathSqEL, parentIdSpEl = "#p0.foreignId", service = PackBomService.class)
    public PackBomVo save(@Valid @RequestBody PackBomDto dto) {
        return packBomService.saveByDto(dto);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "保存全部物料清单")
    public boolean save(@Valid PackBomSearchDto search, @RequestBody List<PackBomDto> dtoList) {
        return packBomService.saveBatchByDto(search.getBomVersionId(), search.getOverlayFlg(), dtoList);
    }

    @ApiOperation(value = "物料不可用")
    @GetMapping("/unusable")
    public boolean bomUnusable(@Valid IdsDto dto) {
        return packBomService.unusableChange(dto.getId(), BaseGlobal.YES);
    }

    @ApiOperation(value = "物料可用")
    @GetMapping("/usable")
    public boolean usable(@Valid IdsDto dto) {
        return packBomService.unusableChange(dto.getId(), BaseGlobal.NO);
    }

    @ApiOperation(value = "删除物料清单")
    @DeleteMapping("/delBom")
    public boolean delBom(@Valid IdsDto dto) {
        return packBomService.delByIds(dto.getId());
    }

    @ApiOperation(value = "转大货")
    @GetMapping("/version/toBigGoods")
    public boolean toBigGoods(@Valid IdDto idDto) {
        return packBomVersionService.toBigGoods(idDto.getId());
    }
}































