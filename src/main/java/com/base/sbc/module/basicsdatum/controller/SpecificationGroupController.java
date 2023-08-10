package com.base.sbc.module.basicsdatum.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.SpecificationGroupDto;
import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import com.base.sbc.module.basicsdatum.service.SpecificationGroupService;
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
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/6/27 17:25
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@Api(tags = "基础资料-规格/门幅")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/specificationGroup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SpecificationGroupController extends BaseController {
    private final SpecificationGroupService specificationGroupService;
    @GetMapping("/queryPage")
    @ApiOperation(value = "条件分页查询")
    public ApiResult queryPage(SpecificationGroupDto specificationGroupDto){
        BaseQueryWrapper<SpecificationGroup> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("status",specificationGroupDto.getStatus());
        queryWrapper.notEmptyLike("type",specificationGroupDto.getType());
        queryWrapper.notEmptyLike("name",specificationGroupDto.getName());
        queryWrapper.notEmptyLike("code",specificationGroupDto.getCode());

        queryWrapper.notEmptyLike("create_name",specificationGroupDto.getCreateName());
        queryWrapper.between("create_date",specificationGroupDto.getCreateDate());
        PageHelper.startPage(specificationGroupDto);
        List<SpecificationGroup> list = specificationGroupService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }

    @ApiOperation(value = "/导入")
    @PostMapping("/specificationGroupImportExcel")
    public Boolean specificationGroupImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
        return specificationGroupService.specificationGroupImportExcel(file);
    }

    @ApiOperation(value = "/导出")
    @GetMapping("/specificationGroupDeriveExcel")
    public void specificationGroupDeriveExcel(HttpServletResponse response ,SpecificationGroupDto specificationGroupDto) throws Exception {
        specificationGroupService.specificationGroupDeriveExcel(response,specificationGroupDto);
    }

    /**
     * 查询id和name列表
     */
    @GetMapping("/listIdName")
    @ApiOperation(value = "查询id和name列表")
    public ApiResult listIdName(){
        List<Map<String,String>> list= specificationGroupService.listIdName();
        return selectSuccess(list);
    }

    @PostMapping("/save")
    @ApiOperation(value = "修改或者新增")
    public ApiResult save(@RequestBody SpecificationGroup specificationGroup){
        boolean save = specificationGroupService.saveSpecification(specificationGroup);
        return selectSuccess(save);
    }
    @PutMapping("/startStop")
    @ApiOperation(value = "启用或者停用")
    public ApiResult startStop(@RequestBody SpecificationGroupDto specificationGroupDto){
        UpdateWrapper<SpecificationGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", specificationGroupDto.getStatus());
        updateWrapper.in("id", Arrays.asList(specificationGroupDto.getIds().split(",")));
        specificationGroupService.update(updateWrapper);
        return updateSuccess("操作成功");
    }

    /**
     * 根据Ids删除
     */
    @PutMapping("/detByIds")
    @ApiOperation(value = "根据数组删除")
    public ApiResult detByIds(String[] ids) {
        return deleteSuccess(specificationGroupService.removeByIds(Arrays.asList(ids)));
    }
}
