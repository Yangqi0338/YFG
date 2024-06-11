package com.base.sbc.module.customFile.controller;


import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.customFile.service.FileTreeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "自定义文件夹相关接口", tags = {"自定义文件夹相关接口"})
@RequestMapping(value = BaseController.SAAS_URL + "/customFile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FileTreeController {

    @Autowired
    private FileTreeService fileTreeService;

}
