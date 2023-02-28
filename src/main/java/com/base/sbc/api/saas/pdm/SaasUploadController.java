package com.base.sbc.api.saas.pdm;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.FilesUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "产品图片上传", tags = "2.1 已登接口[上传]")
@RequestMapping(value = BaseController.SAAS_URL + "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SaasUploadController extends BaseController {
    @Autowired
    private FilesUtils filesUtils;

    @ApiOperation(value = "产品图片上传", notes = "用于产品图片上传，返回上传成功的地址")
    @RequestMapping(value = "/productPic", method = RequestMethod.POST)
    public ApiResult uploadFile(@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request) throws Throwable {
        return filesUtils.uploadBigData(file, FilesUtils.PRODUCT, request);
    }
}