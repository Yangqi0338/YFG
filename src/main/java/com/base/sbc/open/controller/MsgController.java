package com.base.sbc.open.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.customFile.service.FileTreeService;
import com.base.sbc.open.dto.MsgHignConditionDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/msg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MsgController {

    @Autowired
    private FileTreeService fileTreeService;

    @ApiOperation(value = "sql条件查询")
    @PostMapping("/getSqlConditionVerify")
    public ApiResult getSqlConditionVerify(@RequestBody MsgHignConditionDto dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean b = encoder.matches(dto.getConditionSql()+"02c7822b-138c-4176-b3c7-c21f1cc51fbf", dto.getSecret());
        if (!b){
            return ApiResult.success("ok", false);
        }
        String regex = "^\\s*select";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(dto.getConditionSql().toLowerCase());
        if (!matcher.find()){
            return ApiResult.success("ok", false);
        }
        boolean result = fileTreeService.getConditionSql(dto.getConditionSql());
        return ApiResult.success("ok", result);
    }




}
