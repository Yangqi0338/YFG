package com.base.sbc.module.common.controller;

import com.base.sbc.config.CustomStylePicUpload;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.FilesUtils;
import com.base.sbc.config.utils.FtpUtil;
import com.base.sbc.module.common.dto.DelStylePicDto;
import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.utils.ImageUtils;
import com.base.sbc.module.common.vo.AttachmentVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
    @Autowired
    private CustomStylePicUpload customStylePicUpload;


    @ApiOperation(value = "产品图片上传", notes = "用于产品图片上传，返回上传成功的地址")
    @RequestMapping(value = "/productPic", method = RequestMethod.POST)
    public ApiResult uploadPicFile(@RequestParam(value = "file", required = true) MultipartFile file, String type, String code, String compress) {
//        return filesUtils.uploadBigData(file, FilesUtils.PRODUCT, request);
        CommonUtils.isImage(file.getOriginalFilename(), true);
        AttachmentVo attachmentVo = uploadFileService.uploadToMinio(Constants.ONE_STR.equals(compress) ? ImageUtils.compressImage(file) : file, type, code);
        return ApiResult.success(FilesUtils.SUCCESS, attachmentVo.getUrl(), BeanUtil.beanToMap(attachmentVo));
    }

    @ApiOperation(value = "大货款图,设计款图上传", notes = "用于大货图片、设计款图上传")
    @PostMapping(value = "/uploadStylePic")
    public Object uploadStylePic(Principal user, UploadStylePicDto uploadStylePicDto) {
        CommonUtils.isImage(uploadStylePicDto.getFile().getOriginalFilename(), true);
        if (!customStylePicUpload.isOpen()) {
            ApiResult apiResult = uploadPicFile(uploadStylePicDto.getFile(), null, null,null);
            return apiResult.getAttributes();
        }
        try {
            //上传大货款图
            if (StrUtil.isNotBlank(uploadStylePicDto.getStyleColorId())) {
                return uploadFileService.uploadStyleImage(uploadStylePicDto, user);
            }
            //上传设计款图
            if (StrUtil.isNotBlank(uploadStylePicDto.getStyleId())) {
                return uploadFileService.uploadDesignImage(uploadStylePicDto, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("上传失败:" + e.getMessage());
        }
        throw new OtherException("缺少参数");
    }

    @ApiOperation(value = "删除设计款图或者大货款图")
    @GetMapping("/delStylePic")
    public boolean delStylePic(Principal user, DelStylePicDto dto) {
        if (!customStylePicUpload.isOpen()) {
            return true;
        }
        //删除大货款图
        if (StrUtil.isNotBlank(dto.getStyleColorId())) {
            return uploadFileService.delStyleImage(dto, user);
        }
        //删除设计款图
        if (StrUtil.isNotBlank(dto.getStyleId())) {
            return uploadFileService.delDesignImage(dto, user);
        }

        throw new OtherException("缺少参数");
    }

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public AttachmentVo uploadFile(@RequestPart(value = "file", required = false) MultipartFile file, String type, String code) throws Throwable {
        if (file == null || file.getSize() == 0) {
            throw new OtherException("上传文件为空");
        }
        return uploadFileService.uploadToMinio(file, type, code);
    }
}
