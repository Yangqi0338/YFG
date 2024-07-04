package com.base.sbc.module.material.controller;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.Pinyin4jUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.material.dto.CategoryIdDto;
import com.base.sbc.module.material.dto.MaterialEnableDto;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.dto.MaterialSubmitDto;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.Test;
import com.base.sbc.module.material.service.MaterialLabelService;
import com.base.sbc.module.material.service.MaterialService;
import com.base.sbc.module.material.vo.AssociationMaterialVo;
import com.base.sbc.module.material.vo.MaterialLinkageVo;
import com.base.sbc.module.material.vo.MaterialSpaceInfoVo;
import com.base.sbc.module.material.vo.MaterialVo;
import com.base.sbc.module.planning.entity.PlanningCategoryItemMaterial;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.storageSpace.service.StorageSpacePersonService;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

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

    private final PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    @Autowired
    private StorageSpacePersonService storageSpacePersonService;

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

        //检查空间
        checkSpace(materialList);

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
        if (!userUtils.getUserId().equals(materialSaveDto.getCreateId()) && !"1".equals(materialSaveDto.getMaterialManagerStaff())) {
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
        Material material = materialService.getById(materialSaveDto.getId());
        //如果仅仅是保存则不提交审核
        if (!materialSaveDto.isSave()){
            //从公司素材管理提交审批，静默审批，不用走审批流
            if ( "2".equals(materialSaveDto.getStatus())){
                if (!"1".equals(materialSaveDto.getCompanyFlag()) && !"4".equals(material.getStatus())){
                    throw new OtherException("未审核过的素材，不允许提交！");
                }
                if (StringUtils.isBlank(materialSaveDto.getMaterialCode())){
                    String[] split = Pinyin4jUtil.converterToFirstSpell(materialSaveDto.getMaterialBrandName()).split(",");
                    String time = String.valueOf(System.currentTimeMillis());
                    String materialCode = split[0] + time.substring(time.length() - 6) + ThreadLocalRandom.current().nextInt(100000, 999999);
                    materialSaveDto.setMaterialCode(materialCode);
                }
            }else {
                // TODO: 2023/5/20 临时修改，保留之前的素材状态信息，驳回则恢复
                MaterialSaveDto materialSaveDto1=new MaterialSaveDto();
                BeanUtil.copyProperties(materialSaveDto,materialSaveDto1);
                BeanUtil.copyProperties(material,materialSaveDto1);
                if ("2".equals(material.getStatus())) {
                    redisTemplate.opsForValue().set("MTUP:"+materialSaveDto.getId(),materialSaveDto1);
                }
                flowableService.start(FlowableService.MATERIAL + materialSaveDto.getMaterialCategoryName(), FlowableService.MATERIAL, materialSaveDto.getId(), "/pdm/api/saas/material/toExamine",
                        "/pdm/api/saas/material/toExamine", "/pdm/api/saas/material/getById?id=" + materialSaveDto.getId(), null, BeanUtil.beanToMap(materialSaveDto));

            }
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
        if (ids.length < 1){
            return deleteSuccess(true);
        }
        QueryWrapper<Material> qw = new QueryWrapper<>();
        qw.lambda().in(Material::getId, Lists.newArrayList(ids));
        qw.lambda().eq(Material::getStatus,"2");
        List<Material> list = materialService.list(qw);
        if (CollUtil.isNotEmpty(list)){
            //检查是否被引用
            QueryWrapper<PlanningCategoryItemMaterial> qw1 = new QueryWrapper<>();
            qw1.lambda().eq(PlanningCategoryItemMaterial::getDelFlag,"0");
            qw1.lambda().in(PlanningCategoryItemMaterial::getMaterialId, list.stream().map(Material::getId).collect(Collectors.toList()));
            long count = planningCategoryItemMaterialService.count(qw1);
            if (count > 0){
                return ApiResult.error("此素材有被引用，不允许删除！",500);
            }
        }
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

    @PostMapping("/agentEnable")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "素材的启用/停用", notes = "素材的启用/停用")
    public ApiResult agentEnable(@RequestBody @Valid MaterialEnableDto dto) {
        if (StringUtils.isBlank(dto.getEnableFlag())){
            throw new OtherException("无启用/停用信息");
        }
        if (StringUtils.isBlank(dto.getId())){
            return ApiResult.success();
        }
        UpdateWrapper<Material> uw = new UpdateWrapper<>();
        uw.lambda().in(Material::getId, StringUtils.convertList(dto.getId()));
        uw.lambda().set(Material::getEnableFlag,dto.getEnableFlag());
        boolean b = materialService.update(uw);
        return b ? ApiResult.success("修改成功") :  ApiResult.success("修改失败",500);
    }


    /**
     * 模糊联动查询
     */
    @GetMapping("/linkageQuery")
    @ApiOperation(value = "模糊联动查询", notes = "模糊联动查询")
    public ApiResult linkageQuery(String search, String materialCategoryIds) {
        if (StringUtils.isEmpty(search)){
            return ApiResult.success();
        }
        List<MaterialLinkageVo> list = materialService.linkageQuery(search,materialCategoryIds);
        return updateSuccess(list);
    }

    @PutMapping("/batchSubmit")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "批量提交", notes = "批量提交")
    public ApiResult batchSubmit(@RequestBody @Valid MaterialSubmitDto dto){
        if (CollUtil.isEmpty(dto.getIdList())){
            return ApiResult.success();
        }
        List<Material> materials = materialService.listByIds(dto.getIdList());
        //检查参数
        checkMaterial(materials, dto.getType());
        //参数补全
        fillMaterial(materials,dto.getType());
        materialService.saveOrUpdateBatch(materials);
        return ApiResult.success("发布成功");
    }

    @GetMapping("/details")
    @ApiOperation(value = "查看详情", notes = "查看详情")
    public ApiResult details( String id){
        if (StringUtils.isEmpty(id)){
            throw new OtherException("查询id不能为空");
        }
        MaterialQueryDto materialQueryDto = new MaterialQueryDto();
        materialQueryDto.setIds(Lists.newArrayList(id));
        List<MaterialVo> list = materialService.listQuery(materialQueryDto).getList();
        return ApiResult.success("查询成功",CollUtil.isEmpty(list) ? null : list.get(0));
    }

    @PutMapping("/batchUpdate")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "批量更新", notes = "批量更新")
    public ApiResult batchSubmit(@RequestBody @Valid List<MaterialSaveDto> list){
        list.forEach(this::update);
        return ApiResult.success("更新成功");
    }


    @GetMapping("/spaceUsage")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "空间使用情况", notes = "空间使用情况")
    public ApiResult spaceUsage(){
        String userId = userUtils.getUserCompany().getUserId();
        MaterialSpaceInfoVo materialSpaceInfoVo = new MaterialSpaceInfoVo();
        materialSpaceInfoVo.setUsageSpaceSize(materialService.getFileSize(userId, null));
        Long personSpace = storageSpacePersonService.getPersonSpace(userId, "1");
        materialSpaceInfoVo.setTotalSpaceSize(personSpace*1073741824);
        return ApiResult.success("更新成功",materialSpaceInfoVo);
    }


    private void checkMaterial(List<Material> list, String type) {
        if ("1".equals(type)){
            for (Material material : list) {
                if (!"0".equals(material.getStatus()) && !"3".equals(material.getStatus())){
                    throw new OtherException("只有未提交或者审核不通过的才可以提交审核！");
                }
            }

        }
        if ("2".equals(type)){
            for (Material saveDto : list) {
                if (!"1".equals(saveDto.getCompanyFlag()) && !"4".equals(saveDto.getStatus())){
                    throw new OtherException("未审核过的素材，不允许提交！");
                }
                if (StringUtils.isEmpty(saveDto.getMaterialName())){
                    throw new OtherException("素材名称不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getMaterialCategoryId())){
                    throw new OtherException("素材分类不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getMaterialBrand())){
                    throw new OtherException("素材品牌不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getBrand())){
                    throw new OtherException("品牌不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getMarketLevel())){
                    throw new OtherException("市场等级不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getFame())){
                    throw new OtherException("知名度不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getYear()) || StringUtils.isEmpty(saveDto.getMonth()) || StringUtils.isEmpty(saveDto.getSeason())){
                    throw new OtherException("年/季/月不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getSourcePerson())){
                    throw new OtherException("来源人不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getSourceWay())){
                    throw new OtherException("来源方式不能为空！");
                }
                if (StringUtils.isEmpty(saveDto.getSourceDepartment())){
                    throw new OtherException("来源部门不能为空！");
                }
            }
        }
    }
    private void fillMaterial(List<Material> materials, String type) {

        //提交审核
        if ("1".equals(type)){
            materials.forEach(item ->{
                item.setStatus("1");
                flowableService.start(FlowableService.MATERIAL + item.getMaterialCategoryName(), FlowableService.MATERIAL, item.getId(), "/pdm/api/saas/material/toExamine",
                        "/pdm/api/saas/material/toExamine", "/pdm/api/saas/material/getById?id=" + item.getId(), null, BeanUtil.beanToMap(item));
            });

        }

        //提交发布
        if ("2".equals(type)){
            materials.forEach(item ->{
                item.setStatus("2");
                if (StringUtils.isEmpty(item.getMaterialCode())){
                    String[] split = Pinyin4jUtil.converterToFirstSpell(item.getMaterialBrandName()).split(",");
                    String time = String.valueOf(System.currentTimeMillis());
                    String materialCode = split[0] + time.substring(time.length() - 6) + ThreadLocalRandom.current().nextInt(100000, 999999);
                    item.setMaterialCode(materialCode);
                }
            });
        }

    }

    private void checkSpace(List<Material> materials) {
        if (CollUtil.isEmpty(materials)){
            return;
        }
        if (Constants.ONE_STR.equals(materials.get(0).getCompanyFlag())){
            return;
        }
        String userId = companyUserInfo.get().getUserId();
        Long fileSize = materialService.getFileSize(userId, null);
        if (fileSize != null){
            long sum = materials.stream().mapToLong(Material::getFileSize).sum();
            storageSpacePersonService.checkPersonSpacer(fileSize + sum,"1",userId);
        }
    }

}
