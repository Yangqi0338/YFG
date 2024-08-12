package com.base.sbc.module.basicsdatum.controller;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.business.ProcessDatabaseType;
import com.base.sbc.module.basicsdatum.dto.AddRevampProcessDatabaseDto;
import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.basicsdatum.service.ProcessDatabaseService;
import com.base.sbc.module.basicsdatum.vo.ProcessDatabaseSelectVO;
import com.base.sbc.module.common.dto.RemoveDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    public void deriveExcel(HttpServletResponse response, ProcessDatabaseType type) throws Exception {
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
     * 根据列表
     */
    @ApiOperation(value = "查询工艺资料库")
    @GetMapping("/listAllDistinct")
    public ApiResult listAllDistinct(ProcessDatabasePageDto pageDto) {
        Map<String, String> map= processDatabaseService.listAllDistinct(pageDto);
        return selectSuccess(map);
    }

    /**
     * 新增或者修改
     */
    @ApiOperation(value = "新增或者修改工艺资料库")
    @PostMapping("/save")
    public Boolean save(@Valid @RequestBody AddRevampProcessDatabaseDto addRevampProcessDatabaseDto) {
        return processDatabaseService.save(addRevampProcessDatabaseDto);
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
    public ApiResult delById(RemoveDto removeDto) {
        return deleteSuccess(processDatabaseService.removeByIds(removeDto));
    }

    /**
     * 根据id数组批量删除删除
     */
    @ApiOperation(value = "根据id数组批量删除删除")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(RemoveDto removeDto) {
        return deleteSuccess(processDatabaseService.removeByIds(removeDto));
    }

    @ApiOperation(value = "获取所有模板部件")
    @GetMapping("/getAllPatternPartsCode")
    public List<String> getAllPatternPartsCode() {
        ProcessDatabasePageDto pageDto = new ProcessDatabasePageDto();
        pageDto.setType(ProcessDatabaseType.mbbj);
        return processDatabaseService.list(pageDto).stream().map(ProcessDatabase::getCode).distinct().collect(Collectors.toList());
    }

    @ApiOperation(value = "获取所有模板部件")
    @GetMapping("/getAll")
    public List<ProcessDatabase> getAll() {
        ProcessDatabasePageDto pageDto = new ProcessDatabasePageDto();
        pageDto.setType(ProcessDatabaseType.mbbj);
        return processDatabaseService.list(pageDto);
    }

    @ApiOperation(value = "通过类型查询")
    @GetMapping("/selectProcessDatabase")
    public ApiResult<List<ProcessDatabaseSelectVO>> selectProcessDatabase(@Valid @NotNull(message = "类型不可为空") ProcessDatabaseType type, String categoryName) {
        ProcessDatabasePageDto pageDto = new ProcessDatabasePageDto();
        pageDto.setType(type);
        pageDto.setCategoryName(categoryName);
        return selectSuccess(BeanUtil.copyToList(processDatabaseService.list(pageDto), ProcessDatabaseSelectVO.class));
    }

    @ApiOperation(value = "获取部件查询数据")
    @GetMapping("/getQueryList")
    public List<ProcessDatabase> getQueryList(@Valid
                                              @NotNull(message = "类型不可为空") ProcessDatabaseType type,
                                              @NotBlank(message = "去重字段不能为空") String field
    ) {
        ProcessDatabasePageDto pageDto = new ProcessDatabasePageDto();
        pageDto.setType(type);
        return processDatabaseService.getQueryList(pageDto, field);
    }
}
