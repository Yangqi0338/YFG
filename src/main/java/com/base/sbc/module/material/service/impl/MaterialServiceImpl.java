package com.base.sbc.module.material.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.Pinyin4jUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.*;
import com.base.sbc.module.material.mapper.MaterialMapper;
import com.base.sbc.module.material.service.*;
import com.base.sbc.module.material.vo.AssociationMaterialVo;
import com.base.sbc.module.material.vo.MaterialChildren;
import com.base.sbc.module.material.vo.MaterialLinkageVo;
import com.base.sbc.module.material.vo.MaterialVo;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * 类描述：素材库 service实现类
 *
 * @author 卞康
 * @version 1.0
 * @date 创建时间：2023-4-1 16:26:15
 */
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl extends BaseServiceImpl<MaterialMapper, Material> implements MaterialService {

    private final MaterialMapper materialMapper;

    private final UserUtils userUtils;

    private final MaterialLabelService materialLabelService;

    private final MaterialCollectService materialCollectService;

    private final MaterialSizeService materialSizeService;

    private final MaterialColorService materialColorService;

    private final AmcFeignService amcFeignService;
    private final CcmFeignService ccmFeignService;
    private final MinioUtils minioUtils;

    private final PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AmcService amcService;


    /**
     * 为了解决太多表关联查询太慢的问题
     * 相关联的表，生成查询条件
     */
    @Transactional
    public void addQuery(MaterialQueryDto materialQueryDto) {
        // TODO: 2023/3/31 后期优化，写sql语句优化查询返回字段
        HashSet<String> collectSet = null;
        HashSet<String> labelSet = null;
        HashSet<String> sizeSet = null;
        HashSet<String> colorSet = null;
        //我的收藏筛选条件
        if (materialQueryDto.isCollect()) {
            collectSet = new HashSet<>();
            QueryWrapper<MaterialCollect> qc = new QueryWrapper<>();
            qc.eq("user_id", materialQueryDto.getUserId());
            List<MaterialCollect> materialCollects = materialCollectService.list(qc);
            for (MaterialCollect materialCollect : materialCollects) {
                collectSet.add(materialCollect.getMaterialId());
            }
        }

        //标签筛选条件
        if (materialQueryDto.getLabelIds() != null && materialQueryDto.getLabelIds().length > 0) {
            labelSet = new HashSet<>();
            List<MaterialLabel> materialLabels = materialLabelService.getByLabelIds(Arrays.asList(materialQueryDto.getLabelIds()));
            for (MaterialLabel materialLabel : materialLabels) {
                labelSet.add(materialLabel.getMaterialId());
            }
        }

        //标签名称筛选条件
        if (StringUtils.isNotEmpty(materialQueryDto.getLabelNames())) {
            labelSet = new HashSet<>();
            List<MaterialLabel> materialLabels = materialLabelService.getByLabelNames(StringUtils.convertList(materialQueryDto.getLabelNames()));
            for (MaterialLabel materialLabel : materialLabels) {
                labelSet.add(materialLabel.getMaterialId());
            }
        }

        //标签名称模糊筛选条件
        if (StringUtils.isNotEmpty(materialQueryDto.getContent())) {
            List<MaterialLabel> materialLabels = materialLabelService.getLikeLabelNames(materialQueryDto.getContent());
            if (CollUtil.isNotEmpty(materialLabels)){
                labelSet = new HashSet<>();
                for (MaterialLabel materialLabel : materialLabels) {
                    labelSet.add(materialLabel.getMaterialId());
                }
            }
        }

        //尺码筛选条件
        if (!StringUtils.isEmpty(materialQueryDto.getSizeId())) {
            sizeSet = new HashSet<>();
            List<MaterialSize> materialSizes = materialSizeService.getBySizeId(materialQueryDto.getSizeId());
            for (MaterialSize materialSize : materialSizes) {
                sizeSet.add(materialSize.getMaterialId());
            }
        }

        //颜色筛选条件
        if (!StringUtils.isEmpty(materialQueryDto.getColorId())) {
            colorSet = new HashSet<>();
            List<MaterialColor> materialColors = materialColorService.getColorId(materialQueryDto.getColorId());
            for (MaterialColor materialColor : materialColors) {
                colorSet.add(materialColor.getMaterialId());
            }
        }

        //如果有集合不为null，则说明有筛选条件
        if (collectSet != null || labelSet != null || sizeSet != null || colorSet != null) {
            //取所有条件相交的
            Set<String> ids = CommonUtils.findCommonElements(collectSet, labelSet, sizeSet);
            if (ids.size() == 0) {
                //说明无筛选结果，给个0让查询不到数据
                ids = new HashSet<>();
                ids.add("0");
            }
            materialQueryDto.setIds(new ArrayList<>(ids));
        }

        if (StringUtils.isNotEmpty(materialQueryDto.getPatternTypes())){
            materialQueryDto.setPatternTypeList(StringUtils.convertList(materialQueryDto.getPatternTypes()));
        }

        if (StringUtils.isNotEmpty(materialQueryDto.getMaterialNames())){
            materialQueryDto.setMaterialNameList(StringUtils.convertList(materialQueryDto.getMaterialNames()));
        }

    }

    /**
     * 条件查询
     *
     * @param materialQueryDto 请求封装对象
     * @return 返回的封装对象
     */
    @Override
    public PageInfo<MaterialVo> listQuery(MaterialQueryDto materialQueryDto) {
        materialQueryDto.setCompanyCode(userUtils.getCompanyCode());
        materialQueryDto.setUserId(userUtils.getUserId());
        this.addQuery(materialQueryDto);
        PageHelper.startPage(materialQueryDto);
        if (StringUtils.isBlank(materialQueryDto.getCreateId()) && null != materialQueryDto.getStatusList() && 1 == materialQueryDto.getStatusList().length && "2".equals(materialQueryDto.getStatusList()[0])){
            //获取用户组的品牌权限列表
            ApiResult<Map<String,String>> brandList = amcService.getByUserDataPermissionsAll("materialLibrary", "read",companyUserInfo.get().getUserId(),"brand");
            if (Objects.nonNull(brandList.getData())){
                materialQueryDto.setBrandList(Lists.newArrayList(brandList.getData().values()));
            }

        }
        List<MaterialVo> materialAllDtolist = materialMapper.listQuery(materialQueryDto);

        if (materialAllDtolist == null || materialAllDtolist.size() == 0) {
            return new PageInfo<>();
        }

        List<String> ids = new ArrayList<>();
        List<String> userIds = new ArrayList<>();

        List<String> materialCategoryIds=new ArrayList<>(16);
        for (MaterialVo materialVo : materialAllDtolist) {
            userIds.add(materialVo.getCreateId());
            ids.add(materialVo.getId());
            materialCategoryIds.add(materialVo.getMaterialCategoryId());
        }

        //查询关联标签
        List<MaterialLabel> materialLabelList = materialLabelService.getByMaterialIds(ids);
        //查询关联尺码信息
        List<MaterialSize> materialSizeList = materialSizeService.getByMaterialIds(ids);
        //查询关联颜色信息
        List<MaterialColor> materialColorList = materialColorService.getByMaterialIds(ids);
        //查询收藏数量
        List<Map<String, Integer>> mapList = materialCollectService.numList(ids);
        //查询引用数量
        List<Map<String, Integer>> maps = planningCategoryItemMaterialService.numList(ids);
        //获取用户头像
        Map<String, String> userAvatarMap = amcFeignService.getUserAvatar(CollUtil.join(userIds, ","));

        // 获取素材库功能名称
        Map<String, String> materialCategoryNames = ccmFeignService.findStructureTreeNameByCategoryIds(CollUtil.join(materialCategoryIds, ","), "功能");

        for (MaterialVo materialVo : materialAllDtolist) {
            materialVo.setUserAvatar(userAvatarMap.get(materialVo.getCreateId()));
            materialVo.setMaterialCategoryName(Optional.ofNullable(materialCategoryNames.get(materialVo.getMaterialCategoryId())).orElse(materialVo.getMaterialCategoryName()));
            //标签放入对象
            List<MaterialLabel> labels = new ArrayList<>();
            for (MaterialLabel materialLabel : materialLabelList) {
                if (materialVo.getId().equals(materialLabel.getMaterialId())) {
                    labels.add(materialLabel);
                }
            }
            materialVo.setLabels(labels);

            //尺码放入对象
            List<MaterialSize> materialSizes = new ArrayList<>();
            for (MaterialSize materialSize : materialSizeList) {
                if (materialVo.getId().equals(materialSize.getMaterialId())) {
                    materialSizes.add(materialSize);
                }
            }
            materialVo.setSizes(materialSizes);

            //颜色放入对象
            List<MaterialColor> materialColors = new ArrayList<>();
            for (MaterialColor materialColor : materialColorList) {
                if (materialVo.getId().equals(materialColor.getMaterialId())) {
                    materialColors.add(materialColor);
                }
            }
            materialVo.setColors(materialColors);

            //收藏数量
            materialVo.setCollectNum("0");
            for (Map<String, Integer> map : mapList) {
                if (materialVo.getId().equals(String.valueOf(map.get("materialId")))) {
                    materialVo.setCollectNum(String.valueOf(map.get("collectNum")));
                }
            }

            //引用数量
            materialVo.setQuoteNum("0");
            for (Map<String, Integer> map : maps) {
                if (materialVo.getId().equals(String.valueOf(map.get("materialId")))) {
                    materialVo.setQuoteNum(String.valueOf(map.get("collectNum")));
                }
            }
        }
        minioUtils.setObjectUrlToList(materialAllDtolist, "picUrl");
        return new PageInfo<>(materialAllDtolist);
    }

    @Override
    @Transactional
    public boolean toExamine(AnswerDto dto) {
        Material material = this.getById(dto.getBusinessKey());
        if (BaseConstant.APPROVAL_PASS.equals(dto.getApprovalType())) {

            //审核通过
            material.setStatus("4");
            String[] split = Pinyin4jUtil.converterToFirstSpell(material.getMaterialBrandName()).split(",");
            String time = String.valueOf(System.currentTimeMillis());
            String materialCode = split[0] + time.substring(time.length() - 6) + ThreadLocalRandom.current().nextInt(100000, 999999);
            material.setMaterialCode(materialCode);
            this.updateById(material);
            redisTemplate.delete("MTUP:" + material.getId());
            return true;
        } else {
            // TODO: 2023/5/20 临时逻辑，恢复原来的
            Object o = redisTemplate.opsForValue().get("MTUP:" + material.getId());
            if (o != null) {
                MaterialSaveDto materialSaveDto = (MaterialSaveDto) o;
                QueryWrapper<MaterialLabel> labelQueryWrapper = new QueryWrapper<>();
                labelQueryWrapper.eq("material_id", materialSaveDto.getId());
                materialLabelService.addAndUpdateAndDelList(materialSaveDto.getLabels(), labelQueryWrapper);
                redisTemplate.delete("MTUP:" + material.getId());
                this.updateById(materialSaveDto);

            } else {
                material.setStatus("3");
                this.updateById(material);
            }

            return false;
        }

    }

    /**
     * 新增
     *
     * @param materialSaveDto materialSaveDto类
     */
    @Override
    @Transactional
    public String add(MaterialSaveDto materialSaveDto) {
        this.save(materialSaveDto);

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

        return materialSaveDto.getId();
    }

    /**
     * 批量修改素材
     *
     * @param materialSaveDtoList 素材列表
     * @return 影响的条数
     */
    @Override
    @Transactional
    public Integer updateList(List<MaterialSaveDto> materialSaveDtoList) {
        for (MaterialSaveDto materialSaveDto : materialSaveDtoList) {
            //修改关联标签
            QueryWrapper<MaterialLabel> labelQueryWrapper = new QueryWrapper<>();
            labelQueryWrapper.eq("material_id", materialSaveDto.getId());
            materialLabelService.addAndUpdateAndDelList(materialSaveDto.getLabels(), labelQueryWrapper);
            materialSaveDto.setPicUrl(CommonUtils.removeQuery(materialSaveDto.getPicUrl()));
            this.updateById(materialSaveDto);
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
        return materialSaveDtoList.size();
    }

    @Override
    public List<AssociationMaterialVo> getAssociationMaterial(List<String> ids) {
        QueryWrapper<Material> qw=new QueryWrapper();
        qw.in("id",ids);
        List<Material> materials = list(qw);
        if(CollUtil.isEmpty(materials)){
            return null;
        }
        // 获取素材库
        List<String> materialCategoryIds = materials.stream().filter(item -> StrUtil.isNotBlank(item.getMaterialCategoryId()))
                .map(Material::getMaterialCategoryId).collect(Collectors.toList());
        //查询素材库
        Map<String, String> materialCategoryNames = ccmFeignService.findStructureTreeNameByCategoryIds(CollUtil.join(materialCategoryIds, ","), "功能");
        Map<String, AssociationMaterialVo> result = new LinkedHashMap<>(16);
        for (Material material : materials) {
            String key=material.getMaterialCategoryId();
            if(StrUtil.isBlank(key)){
                key="-1";
            }
            List<MaterialVo> materialList=null;
            if(result.containsKey(key)){
                 materialList = result.get(key).getMaterialList();
            }else{
                materialList=new ArrayList<>(16);
                AssociationMaterialVo r=new AssociationMaterialVo();
                r.setMaterialCategoryId(key);
                r.setMaterialList(materialList);
                r.setName(Optional.ofNullable(materialCategoryNames.get(key)).orElse("其他"));
                result.put(key,r);
            }
            materialList.add(BeanUtil.copyProperties(material,MaterialVo.class));
            minioUtils.setObjectUrlToList(materialList, "picUrl");
        }
        List<AssociationMaterialVo> collect = result.values().stream().collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<MaterialLinkageVo> linkageQuery(String search, String materialCategoryIds) {
        List<MaterialLinkageVo> list = Lists.newArrayList();
        //素材标签相关
        List<String> materialCategoryIdList = StringUtils.convertList(materialCategoryIds);
        List<MaterialChildren> labelList = materialLabelService.linkageQuery(search,materialCategoryIdList);
        if (CollUtil.isNotEmpty(labelList)){
            MaterialLinkageVo materialLinkageVo = new MaterialLinkageVo();
            materialLinkageVo.setChildren(labelList);
            materialLinkageVo.setGroup("标签名称");
            list.add(materialLinkageVo);
        }
        // 素材名称相关
        List<MaterialChildren> materialNameList = this.getBaseMapper().linkageQueryName(search,materialCategoryIdList);
        if (CollUtil.isNotEmpty(materialNameList)){
            MaterialLinkageVo materialLinkageVo = new MaterialLinkageVo();
            materialLinkageVo.setChildren(materialNameList);
            materialLinkageVo.setGroup("素材名称");
            list.add(materialLinkageVo);
        }
        return list;
    }
}
