/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackTechPackaging;
import com.base.sbc.module.pack.entity.PackingDictionary;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private PackTechPackagingService packTechPackagingService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private PackInfoStatusService packInfoStatusService;
    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    private PackingDictionaryService packingDictionaryService;
    @Autowired
    private UserUtils userUtils;
    final String lockField = "techSpecLockFlag";

    @ApiOperation(value = "列表")
    @GetMapping
    public List<PackTechSpecVo> page(PackTechSpecSearchDto dto) {
        return packTechSpecService.list(dto);
    }


    @ApiOperation(value = "文件列表(图片)")
    @GetMapping("/picList")
    public List<PackTechAttachmentVo> picList(PackTechSpecSearchDto dto) {
        return packTechSpecService.picList(dto);
    }

    @ApiOperation(value = "保存图片")
    @PostMapping("/savePic")
    public AttachmentVo savePic(@Valid @RequestBody PackTechSpecSavePicDto dto) {
        packInfoStatusService.checkLock(dto.getForeignId(), dto.getPackType(), lockField);
        return packTechSpecService.savePic(dto);
    }


    @ApiOperation(value = "删除图片")
    @DeleteMapping("/delPic")
    public boolean del(@Valid IdsDto ids) {
        return attachmentService.del(ids.getId());
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping()
    public Boolean removeById(@Valid IdsDto dto) {
        return packTechSpecService.removeById(dto);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public PackTechSpecVo save(@Valid @RequestBody PackTechSpecDto dto) {
        return packTechSpecService.saveByDto(dto);
    }

    @ApiOperation(value = "批量保存")
    @PostMapping("/batchSave")
    public List<PackTechSpecVo> batchSave(@Valid @RequestBody PackTechSpecBatchSaveDto dto) {
        return packTechSpecService.batchSave(dto);
    }

    @ApiOperation(value = "复制")
    @PostMapping("/copyOther")
    public List<PackTechSpecVo> copyOther(@Valid @RequestBody List<PackTechSpecDto> list) {
        return packTechSpecService.copyOther(list);
    }

    @ApiOperation(value = "排序")
    @GetMapping("/sort")
    public boolean sort(@Valid IdsDto dto) {
        return packTechSpecService.sort(dto.getId(), "sort");
    }

    @ApiOperation(value = "变更日志")
    @GetMapping("/operationLog")
    public PageInfo<OperaLogEntity> operationLog(@Valid PackTechSpecPageDto pageDto) {
        return packTechSpecService.operationLog(pageDto);
    }

    @ApiOperation(value = "保存包装方式和体积重量")
    @PostMapping("/packaging")
    public PackTechPackaging savePackaging(@RequestBody PackTechPackaging packaging) {
        return packTechPackagingService.savePackaging(packaging);
    }



    @ApiOperation(value = "保存包装方式长宽高回显")
    @PostMapping("/packagingEcho")
    public PackingDictionary Packaging(@RequestParam("parentId") String parentId,@RequestParam("name") String name) {
        PackingDictionary packingDictionary = packingDictionaryService.queryPackingDictionary(parentId, name);
        return packingDictionary;
    }


    @ApiOperation(value = "查询包装方式和体积重量")
    @GetMapping("/packaging")
    public PackTechPackaging getPackaging(@Valid PackCommonSearchDto dto) {
        PackTechPackaging packTechPackaging = packTechPackagingService.get(dto.getForeignId(), dto.getPackType());
        if (packTechPackaging == null) {
            return packTechPackagingService.savePackaging(BeanUtil.copyProperties(dto, PackTechPackaging.class));
        }
        return packTechPackaging;
    }

    @ApiOperation(value = "锁定")
    @GetMapping("/lock")
    public boolean lock(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.lockTechSpec(dto.getForeignId(), dto.getPackType());
    }

    @ApiOperation(value = "解锁")
    @GetMapping("/unlock")
    public boolean unlock(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.unlockTechSpec(dto.getForeignId(), dto.getPackType());
    }

    @ApiOperation(value = "提交审批")
    @GetMapping("/startApproval")
    public boolean startReverseApproval(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.startApprovalForTechSpec(dto.getForeignId(), dto.getPackType());
    }

    @ApiIgnore
    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        return packInfoStatusService.approvalForTechSpec(dto);
    }


    @ApiOperation(value = "生成工艺说明文件")
    @PostMapping("/genTechSpecFile")
    public AttachmentVo genTechSpecFile(Principal user, @Valid PackCommonSearchDto dto) {
//        return packInfoService.genTechSpecFile(dto);
        GroupUser groupUser = userUtils.getUserBy(user);
        return packInfoService.genTechSpecFile2(groupUser, dto);
    }

    @ApiOperation(value = "保存工艺视频")
    @GetMapping("/saveVideoFile")
    public AttachmentVo saveVideoFile(@Valid PackCommonSearchDto dto, String fileId) {
        return packInfoService.saveVideoFile(dto.getForeignId(), dto.getPackType(), fileId);
    }

    @ApiOperation(value = "删除工艺说明文件")
    @DeleteMapping("/delTechSpecFile")
    public boolean delTechSpecFile(@Valid PackCommonSearchDto dto) {
        return packInfoService.delTechSpecFile(dto);
    }

    @GetMapping("/references")
    @ApiOperation(value = "引用")
    public boolean references(PackTechSpecReferencesDto dto) {
        return packTechSpecService.references(dto);
    }
}































