package com.base.sbc.module.material.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.material.entity.*;
import com.base.sbc.module.material.dto.MaterialDto;
import com.base.sbc.module.material.service.*;
import com.base.sbc.module.material.dto.MaterialAllDto;
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
    private MaterialDetailsService materialDetailsService;
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
    public ApiResult add(@RequestBody MaterialDto materialDto) {

        materialDto.getMaterial().setStatus(BasicNumber.ONE.getNumber());
        materialService.save(materialDto.getMaterial());

        List<MaterialLabel> labels = materialDto.getMaterial().getLabels();

        //新增关联标签
        if (labels != null) {
            for (MaterialLabel label : labels) {
                label.setMaterialId(materialDto.getMaterial().getId());
                materialLabelService.save(label);
            }
        }

        //新增关联尺码
        List<MaterialSize> sizes = materialDto.getMaterial().getSizes();
        if (sizes != null) {
            for (MaterialSize size : sizes) {
                size.setMaterialId(materialDto.getMaterial().getId());
                materialSizeService.save(size);
            }
        }

        //新增关联颜色
        List<MaterialColor> colors = materialDto.getMaterial().getColors();
        if (colors != null) {
            for (MaterialColor color : colors) {
                color.setMaterialId(materialDto.getMaterial().getId());
                materialColorService.save(color);
            }
        }

        materialDto.getMaterialDetails().setMaterialId(materialDto.getMaterial().getId());
        materialDetailsService.save(materialDto.getMaterialDetails());
        return insertSuccess(materialDto.getMaterial().getId());
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

        List<MaterialDetails> materialDetailsList = new ArrayList<>();
        for (Material material : materialList) {
            material.setStatus(BasicNumber.ZERO.getNumber());
            MaterialDetails materialDetails = new MaterialDetails();
            materialDetails.setMaterialId(material.getId());
            materialDetailsList.add(materialDetails);
        }

        boolean b = materialService.saveBatch(materialList);
        materialDetailsService.saveBatch(materialDetailsList);
        return insertSuccess(b);
    }

    /**
     * 单个修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改素材", notes = "修改素材")
    public ApiResult update(@RequestBody MaterialDto materialDto) {
        Material material1 = materialService.getById(materialDto.getMaterial().getId());
        //是否修改了文件名或者所属素材库
        if (!material1.getMaterialLibrary().equals(materialDto.getMaterial().getMaterialLibrary()) || !material1.getMaterialName().equals(materialDto.getMaterial().getMaterialName())) {

            QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("material_name", materialDto.getMaterial().getMaterialName());
            queryWrapper.eq("material_library", materialDto.getMaterial().getMaterialLibrary());
            Material material = materialService.getOne(queryWrapper);

            if (material != null && !Objects.equals(material.getId(), materialDto.getMaterial().getId())) {
                throw new OtherException("已存在相同名称素材");
            }
        }

        //删除关联标签
        QueryWrapper<MaterialLabel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_id", materialDto.getMaterial().getId());
        materialLabelService.remove(queryWrapper);

        List<MaterialLabel> labels = materialDto.getMaterial().getLabels();
        //新增关联标签
        if (labels != null) {
            for (MaterialLabel label : labels) {
                label.setMaterialId(materialDto.getMaterial().getId());
                materialLabelService.save(label);
            }
        }

        //删除关联尺码
        materialSizeService.removeById(materialDto.getMaterial().getId());
        //新增关联尺码
        List<MaterialSize> sizes = materialDto.getMaterial().getSizes();
        if (sizes != null) {
            for (MaterialSize size : sizes) {
                size.setMaterialId(materialDto.getMaterial().getId());
                materialSizeService.save(size);
            }
        }

        //删除关联颜色
        materialColorService.removeById(materialDto.getMaterial().getId());
        //新增关联颜色
        List<MaterialColor> colors = materialDto.getMaterial().getColors();
        if (colors != null) {
            for (MaterialColor color : colors) {
                color.setMaterialId(materialDto.getMaterial().getId());
                materialColorService.save(color);
            }
        }

        boolean b = materialService.updateById(materialDto.getMaterial());
        materialDetailsService.updateById(materialDto.getMaterialDetails());
        return updateSuccess(b);
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String[] ids) {
        return deleteSuccess(materialService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 查询列表
     */
    @GetMapping("/listQuery")
    public PageInfo<MaterialDto> listQuery(@RequestHeader("Authorization") String token, MaterialAllDto materialAllDto, Page page) {
        if (materialAllDto == null) {
            throw new OtherException("参数不能为空");
        }
        return materialService.listQuery(token, materialAllDto, page);
    }

    /**
     * 根据id单个查询
     */
    @GetMapping("/getById")
    public MaterialDto getById(String id) {
        Material material = materialService.getById(id);
        QueryWrapper<MaterialDetails> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("material_id",material.getId());
        MaterialDetails materialDetails = materialDetailsService.getOne(queryWrapper);
        MaterialDto materialDto = new MaterialDto();
        materialDto.setMaterial(material);
        materialDto.setMaterialDetails(materialDetails);
        return materialDto;
    }
}
