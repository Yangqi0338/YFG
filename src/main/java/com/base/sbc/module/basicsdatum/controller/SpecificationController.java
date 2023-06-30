package com.base.sbc.module.basicsdatum.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.basicsdatum.dto.SpecificationDto;
import com.base.sbc.module.basicsdatum.entity.Specification;
import com.base.sbc.module.basicsdatum.service.SpecificationService;
import com.base.sbc.module.basicsdatum.service.impl.SpecificationExcelDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/6/27 10:45
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@Api(tags = "基础资料-规格/门幅")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/specification", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SpecificationController extends BaseController {

    private final SpecificationService specificationService;
    @GetMapping("/queryPage")
    @ApiOperation(value = "条件分页查询")
    public ApiResult queryPage(SpecificationDto specificationDto){
        BaseQueryWrapper<Specification> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("status",specificationDto.getStatus());
        queryWrapper.notEmptyEq("type",specificationDto.getType());
        queryWrapper.notEmptyLike("name",specificationDto.getName());
        queryWrapper.notEmptyLike("code",specificationDto.getCode());

        queryWrapper.notEmptyLike("create_name",specificationDto.getCreateName());
        queryWrapper.notEmptyLike("create_date",specificationDto.getCreateDate());
        PageHelper.startPage(specificationDto);
        List<Specification> list = specificationService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }
    @PostMapping("/save")
    @ApiOperation(value = "修改或者新增")
    public ApiResult save(@RequestBody Specification specification){

        boolean save = specificationService.saveSpecification(specification);
        return selectSuccess(save);
    }
    @PutMapping("/startStop")
    @ApiOperation(value = "启用或者停用")
    public ApiResult startStop(@RequestBody SpecificationDto specificationDto){
        UpdateWrapper<Specification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", specificationDto.getStatus());
        updateWrapper.in("id", Arrays.asList(specificationDto.getIds().split(",")));
        specificationService.update(updateWrapper);
        return updateSuccess("操作成功");
    }

    /**
     * 根据Ids删除
     */
    @PutMapping("/detByIds")
    @ApiOperation(value = "根据数组删除")
    public ApiResult detByIds(String[] ids) {
        return deleteSuccess(specificationService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导入
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        Boolean b = specificationService.importExcel(file);
        return insertSuccess(b);
    }

    /**
     * 导出
     */
    @ApiOperation(value = "导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        List<SpecificationExcelDto> list = BeanUtil.copyToList(specificationService.list(), SpecificationExcelDto.class);
        ExcelUtils.exportExcel(list,  SpecificationExcelDto.class, "规格/门幅.xlsx",new ExportParams() ,response);
    }
}
