package com.base.sbc.module.common.controller;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.FilesUtils;
import com.base.sbc.config.utils.FtpUtil;
import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@Api(tags = "2.1 文件上传 已登接口[上传]")
@RequestMapping(value = BaseController.SAAS_URL + "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SaasUploadController extends BaseController {
    @Autowired
    private FilesUtils filesUtils;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private FtpUtil util;

    @ApiOperation(value = "产品图片上传", notes = "用于产品图片上传，返回上传成功的地址")
    @RequestMapping(value = "/productPic", method = RequestMethod.POST)
    public ApiResult uploadPicFile(@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request) throws Throwable {
//        return filesUtils.uploadBigData(file, FilesUtils.PRODUCT, request);
        AttachmentVo attachmentVo = uploadFileService.uploadToMinio(file);
        return ApiResult.success(FilesUtils.SUCCESS, attachmentVo.getUrl(), BeanUtil.beanToMap(attachmentVo));
    }
    @ApiOperation(value = "大货款图上传", notes = "用于大货图片上传，返回上传成功的地址")
    @PostMapping(value = "/uploadStylePic")
    public Boolean uploadStylePic( Principal user,UploadStylePicDto uploadStylePicDto) throws Throwable {
        return uploadFileService.uploadStyleImage(uploadStylePicDto,user);
    }
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public AttachmentVo uploadFile(@RequestParam(value = "file", required = true) MultipartFile file) throws Throwable {
        return uploadFileService.uploadToMinio(file);
    }
}
