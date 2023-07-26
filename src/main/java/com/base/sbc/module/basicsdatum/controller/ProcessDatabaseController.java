package com.base.sbc.module.basicsdatum.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampProcessDatabaseDto;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCompanyRelationService;
import com.base.sbc.module.basicsdatum.service.ProcessDatabaseService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


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


    @ApiOperation(value = "/导出")
    @GetMapping("/deriveExcel")
    public void deriveExcel(HttpServletResponse response,String type) throws Exception {
        processDatabaseService.deriveExcel(response ,type);
    }


    /**
     * 根据列表进行分页查询
     */
    @ApiOperation(value = "查询工艺资料库")
    @GetMapping("/listPage")
    public ApiResult listPage(ProcessDatabasePageDto pageDto) {
        PageInfo<ProcessDatabase> pageInfo= processDatabaseService.listPage(pageDto);
        return selectSuccess(pageInfo);
    }

    /**
     * 新增或者修改
     */
    @ApiOperation(value = "新增或者修改工艺资料库")
    @PostMapping("/save")
    @OperaLog(value = "工艺资料库", operationType = OperationType.INSERT_UPDATE, pathSpEL = PackUtils.pathSqEL, service = ProcessDatabaseService.class)
    public Boolean save(@RequestBody AddRevampProcessDatabaseDto addRevampProcessDatabaseDto){
        return   processDatabaseService.save(addRevampProcessDatabaseDto);
    }


    /**
     * 批量或者修改
     */
    @ApiOperation(value = "批量或者修改")
    @PostMapping("/saveList")
    public ApiResult saveList(@RequestBody List<ProcessDatabase> processDatabaseList){
        boolean b = processDatabaseService.saveOrUpdateBatch(processDatabaseList);
        return insertSuccess(b);
    }

    /**
     * 根据id删除
     */
    @ApiOperation(value = "根据id删除")
    @DeleteMapping("/delById")
    @OperaLog(value = "工艺资料库", operationType = OperationType.DELETE, service = ProcessDatabaseService.class)
    public ApiResult delById(String id) {
        return deleteSuccess(processDatabaseService.removeById(id));
    }

    /**
     * 根据id数组批量删除删除
     */
    @ApiOperation(value = "根据id数组批量删除删除")
    @DeleteMapping("/delByIds")
    @OperaLog(value = "工艺资料库", operationType = OperationType.DELETE, service = ProcessDatabaseService.class)
    public ApiResult delById(String[] ids) {
        return deleteSuccess(processDatabaseService.removeBatchByIds(Arrays.asList(ids)));
    }

    @ApiOperation(value = "获取所有模板部件")
    @GetMapping("/getAllPatternPartsCode")
    public List<String> getAllPatternPartsCode() {
        return processDatabaseService.getAllPatternPartsCode();
    }

    @ApiOperation(value = "通过类型查询")
    @GetMapping("/selectProcessDatabase")
    public ApiResult selectProcessDatabase(@Valid @NotBlank(message = "类型不可为空") String type, String categoryName) {
        return selectSuccess(processDatabaseService.selectProcessDatabase(type, categoryName, super.getUserCompany()));
    }
}
