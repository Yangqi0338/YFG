package com.base.sbc.module.material.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.*;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.service.*;
import com.base.sbc.module.material.vo.MaterialVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 卞康
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与素材库相关的所有接口信息", tags = {"素材库接口"})
@RequestMapping(value = BaseController.SAAS_URL + "/material", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialController extends BaseController {
    @Resource
    private MaterialService materialService;
    @Resource
    private MaterialLabelService materialLabelService;
    @Resource
    private MaterialSizeService materialSizeService;
    @Resource
    private MaterialColorService materialColorService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "新增素材", notes = "新增素材")
    public ApiResult add(@RequestBody MaterialSaveDto materialSaveDto) {

        materialSaveDto.setStatus(BasicNumber.ONE.getNumber());
        materialService.save(materialSaveDto);

        List<MaterialLabel> labels = materialSaveDto.getLabels();

        //新增关联标签
        if (labels != null) {
            for (MaterialLabel label : labels) {
                label.setMaterialId(materialSaveDto.getId());
                materialLabelService.save(label);
            }
        }

        //新增关联尺码
        List<MaterialSize> sizes = materialSaveDto.getSizes();
        if (sizes != null) {
            for (MaterialSize size : sizes) {
                size.setMaterialId(materialSaveDto.getId());
                materialSizeService.save(size);
            }
        }

        //新增关联颜色
        List<MaterialColor> colors = materialSaveDto.getColors();
        if (colors != null) {
            for (MaterialColor color : colors) {
                color.setMaterialId(materialSaveDto.getId());
                materialColorService.save(color);
            }
        }
        return insertSuccess(materialSaveDto.getId());
    }

    /**
     * 批量新增
     */

    @PostMapping("addList")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "批量新增素材", notes = "批量新增素材")
    public ApiResult addList(@RequestBody List<Material> materialList) {
        if (materialList == null || materialList.size() == 0) {
            throw new OtherException("参数错误");
        }

        for (Material material : materialList) {
            material.setStatus(BasicNumber.ZERO.getNumber());
        }

        boolean b = materialService.saveBatch(materialList);
        return insertSuccess(b);
    }

    /**
     * 单个修改
     */
    @PutMapping("/update")

    @ApiOperation(value = "修改素材", notes = "修改素材")
    public ApiResult update(@RequestBody MaterialSaveDto materialSaveDto) {
        //删除关联标签
        QueryWrapper<MaterialLabel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_id", materialSaveDto.getId());
        materialLabelService.remove(queryWrapper);

        List<MaterialLabel> labels = materialSaveDto.getLabels();
        //新增关联标签
        if (labels != null) {
            for (MaterialLabel label : labels) {
                label.setMaterialId(materialSaveDto.getId());
                materialLabelService.save(label);
            }
        }

        //删除关联尺码
        materialSizeService.removeById(materialSaveDto.getId());
        //新增关联尺码
        List<MaterialSize> sizes = materialSaveDto.getSizes();
        if (sizes != null) {
            for (MaterialSize size : sizes) {
                size.setMaterialId(materialSaveDto.getId());
                materialSizeService.save(size);
            }
        }

        //删除关联颜色
        materialColorService.removeById(materialSaveDto.getId());
        //新增关联颜色
        List<MaterialColor> colors = materialSaveDto.getColors();
        if (colors != null) {
            for (MaterialColor color : colors) {
                color.setMaterialId(materialSaveDto.getId());
                materialColorService.save(color);
            }
        }

        boolean b = materialService.updateById(materialSaveDto);
        return updateSuccess(b);
    }

    /**
     * 根据id删除
     */
    @ApiOperation(value = "根据id数组删除", notes = "根据id数组删除")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String[] ids) {
        return deleteSuccess(materialService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public PageInfo<MaterialVo> listQuery(MaterialQueryDto materialQueryDto) {
        if (materialQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        return materialService.listQuery(materialQueryDto);
    }

    /**
     * 根据id单个查询
     */
    @ApiOperation(value = "根据id单个查询", notes = "根据id单个查询")
    @GetMapping("/getById")
    public Material getById(String id) {
        return materialService.getById(id);
    }
}
