package com.base.sbc.module.material.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.material.dto.CategoryIdDto;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.Test;
import com.base.sbc.module.material.service.MaterialLabelService;
import com.base.sbc.module.material.service.MaterialService;
import com.base.sbc.module.material.vo.AssociationMaterialVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author 卞康
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与素材库相关的所有接口信息", tags = {"素材库接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/material", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialController extends BaseController {

    private final MaterialService materialService;

    private final MaterialLabelService materialLabelService;

    private final UserUtils userUtils;


    private final FlowableService flowableService;

    private final RedisTemplate<String,Object> redisTemplate;

    /**
     * 新增
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "新增素材", notes = "新增素材")
    public ApiResult add(@RequestBody MaterialSaveDto materialSaveDto) {
        String id = materialService.add(materialSaveDto);
        return insertSuccess(id);
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
            material.setPicUrl(CommonUtils.removeQuery(material.getPicUrl()));
        }

        materialService.saveBatch(materialList);
        return insertSuccess(materialList.size());
    }

    /**
     * 单个修改
     */
    @PutMapping("/update")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "修改素材", notes = "修改素材")
    public ApiResult update(@RequestBody MaterialSaveDto materialSaveDto) {
        if (!userUtils.getUserId().equals(materialSaveDto.getCreateId())) {
            throw new OtherException("只有创建人才能修改");
        }
        //if (BasicNumber.ZERO.getNumber().equals(materialSaveDto.getStatus())){
        //    materialSaveDto.setStatus(BasicNumber.ONE.getNumber());
        //
        // }
        CommonUtils.removeQuery(materialSaveDto, "picUrl");

        //修改关联标签
        QueryWrapper<MaterialLabel> labelQueryWrapper = new QueryWrapper<>();
        labelQueryWrapper.eq("material_id", materialSaveDto.getId());
        materialLabelService.addAndUpdateAndDelList(materialSaveDto.getLabels(), labelQueryWrapper);

        //如果仅仅是保存则不提交审核
        if (!materialSaveDto.isSave()){
            // TODO: 2023/5/20 临时修改，保留之前的素材状态信息，驳回则恢复
            Material material = materialService.getById(materialSaveDto.getId());
            MaterialSaveDto materialSaveDto1=new MaterialSaveDto();
            BeanUtil.copyProperties(materialSaveDto,materialSaveDto1);
            BeanUtil.copyProperties(material,materialSaveDto1);
            if ("2".equals(material.getStatus())) {
                redisTemplate.opsForValue().set("MTUP:"+materialSaveDto.getId(),materialSaveDto1);
            }
            flowableService.start(FlowableService.MATERIAL + materialSaveDto.getMaterialCategoryName(), FlowableService.MATERIAL, materialSaveDto.getId(), "/pdm/api/saas/material/toExamine",
                    "/pdm/api/saas/material/toExamine", "/pdm/api/saas/material/getById?id=" + materialSaveDto.getId(), null, BeanUtil.beanToMap(materialSaveDto));

        }

        ////修改关联尺码
        //QueryWrapper<MaterialSize> sizeQueryWrapper = new QueryWrapper<>();
        //sizeQueryWrapper.eq("material_id", materialSaveDto.getId());
        //materialSizeService.addAndUpdateAndDelList(materialSaveDto.getSizes(),sizeQueryWrapper);
        //
        ////修改关联颜色
        //QueryWrapper<MaterialColor> colorQueryWrapper = new QueryWrapper<>();
        //colorQueryWrapper.eq("material_id", materialSaveDto.getId());
        //materialColorService.addAndUpdateAndDelList(materialSaveDto.getColors(),colorQueryWrapper);
        materialService.updateById(materialSaveDto,"创意素材库");
        boolean b = materialService.updateById(materialSaveDto);
            return updateSuccess(b);
    }

    /**
     * 批量修改
     */
    @PutMapping("/updateList")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "修改素材", notes = "修改素材")
    public ApiResult updateList(@RequestBody List<MaterialSaveDto> materialSaveDtoList) {
        materialService.updateList(materialSaveDtoList);
        return updateSuccess(materialSaveDtoList.size());
    }

    /**
     * 根据id删除
     */
    @ApiOperation(value = "根据id数组删除", notes = "根据id数组删除")
    @DeleteMapping("/delByIds")
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult delByIds(String[] ids) {
        return deleteSuccess(materialService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult  listQuery(MaterialQueryDto materialQueryDto) {
        if (materialQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        return selectSuccess(materialService.listQuery(materialQueryDto));
    }

    /**
     * 查询传入的素材库下的品类数量
     */
    @PostMapping("/countByCategoryIds")
    public ApiResult countByCategoryIds(@RequestBody List<CategoryIdDto> categoryIdDtoList) {
        Map<String, Object> map = new HashMap<>(5);
        for (CategoryIdDto categoryIdDto : categoryIdDtoList) {
            QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", BasicNumber.TWO.getNumber());
            queryWrapper.in("category_id", categoryIdDto.getCategoryIds());
            long count = materialService.count(queryWrapper);
            map.put(categoryIdDto.getId(), count);
        }

        return selectSuccess(map);
    }

    /**
     * 根据id单个查询
     */
    @ApiOperation(value = "根据id单个查询", notes = "根据id单个查询")
    @GetMapping("/getById")
    public Material getById(String id) {
        return materialService.getById(id);
    }


    @PostMapping("upTest")
    public void upTest(MultipartFile file) throws IOException {

        List<Test> test = EasyExcel.read(file.getInputStream()).head(Test.class).sheet().headRowNumber(3).doReadSync();
        System.out.println(test);
    }


    /**
     * 审核回调地址
     */
    @PostMapping("/toExamine")
    public boolean toExamine(@RequestBody AnswerDto dto) {
        return materialService.toExamine(dto);
    }

    @ApiOperation(value = "获取关联素材库的详细信息", notes = "组件展示用")
    @PostMapping("/getAssociationMaterial")
    public List<AssociationMaterialVo> getAssociationMaterial(@RequestBody List<String> ids){
        if(CollUtil.isEmpty(ids)){
            return null;
        }
        return materialService.getAssociationMaterial(ids);
    }


    /**
     * 根据标签id集合查询关联标签
     */
    @GetMapping("/getByIds")
    public ApiResult getByIds(String ids) {
        List<Material> materials = materialService.listByIds(Collections.singletonList(ids));
        return selectSuccess(materials);
    }
}
