package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.basicsdatum.service.ProcessDatabaseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;


/**
 * @author 卞康
 * @date 2023/6/5 9:21:38
 * @mail 247967116@qq.com
 * 工艺资料库
 */
@Api(tags = "工艺资料库")
@RequestMapping(value = BaseController.SAAS_URL + "/processDatabase", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@RestController
public class ProcessDatabaseController extends BaseController {

    private final ProcessDatabaseService processDatabaseService;

    /**
     * 导入工艺资料库
     */
    @ApiOperation(value = "导入工艺资料库")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        Boolean b = processDatabaseService.importExcel(file);
        return insertSuccess(b);
    }

    /**
     * 根据列表进行分页查询
     */
    @ApiOperation(value = "导入工艺资料库")
    @PostMapping("/queryPage")
    public ApiResult listPage(ProcessDatabasePageDto pageDto) throws Exception {
        PageInfo<ProcessDatabase> pageInfo= processDatabaseService.listPage(pageDto);
        return insertSuccess(pageInfo);
    }

    /**
     * 新增或者修改
     */
    @ApiOperation(value = "新增或者修改工艺资料库")
    @PostMapping("/save")
    public ApiResult save(ProcessDatabasePageDto pageDto) throws Exception {
        PageInfo<ProcessDatabase> pageInfo= processDatabaseService.listPage(pageDto);
        return insertSuccess(pageInfo);
    }

    /**
     * 根据id删除
     */
    @ApiOperation(value = "根据id删除")
    @PostMapping("/delById")
    public ApiResult delById(String id) throws Exception {
        return deleteSuccess(processDatabaseService.removeById(id));
    }
    /**
     * 根据id数组批量删除删除
     */
    @ApiOperation(value = "根据id数组批量删除删除")
    @PostMapping("/delByIds")
    public ApiResult delById(String[] ids) throws Exception {
        return deleteSuccess(processDatabaseService.removeBatchByIds(Arrays.asList(ids)));
    }


}
