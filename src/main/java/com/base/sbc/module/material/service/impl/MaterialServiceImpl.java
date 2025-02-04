package com.base.sbc.module.material.service.impl;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.Pinyin4jUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialCollect;
import com.base.sbc.module.material.entity.MaterialColor;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.MaterialSize;
import com.base.sbc.module.material.mapper.MaterialMapper;
import com.base.sbc.module.material.service.MaterialCollectService;
import com.base.sbc.module.material.service.MaterialColorService;
import com.base.sbc.module.material.service.MaterialLabelService;
import com.base.sbc.module.material.service.MaterialService;
import com.base.sbc.module.material.service.MaterialSizeService;
import com.base.sbc.module.material.vo.AssociationMaterialVo;
import com.base.sbc.module.material.vo.MaterialChildren;
import com.base.sbc.module.material.vo.MaterialLinkageVo;
import com.base.sbc.module.material.vo.MaterialVo;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

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
            qc.eq("user_id", StringUtils.isNotEmpty(materialQueryDto.getUserId()) ? materialQueryDto.getUserId() : materialQueryDto.getCreateId());

            if (StringUtils.isNotEmpty(materialQueryDto.getFolderId())){
                qc.in("folder_id",StringUtils.convertList(materialQueryDto.getFolderId()));
            }
            List<MaterialCollect> materialCollects = materialCollectService.list(qc);
            for (MaterialCollect materialCollect : materialCollects) {
                collectSet.add(materialCollect.getMaterialId());
            }
            materialQueryDto.setCreateId(null);
            materialQueryDto.setFolderId(null);
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
            List<MaterialLabel> materialLabels = materialLabelService.getByLabelNames(StringUtils.convertList(materialQueryDto.getLabelNames()),null);
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

        if (StringUtils.isNotEmpty(materialQueryDto.getFolderId())){
            materialQueryDto.setFolderIdList(StringUtils.convertList(materialQueryDto.getFolderId()));
        }

        if (StringUtils.isNotEmpty(materialQueryDto.getDescInfos())){
            materialQueryDto.setDescInfoList(StringUtils.convertList(materialQueryDto.getDescInfos()));
        }

        //品牌
        if (StringUtils.isBlank(materialQueryDto.getCreateId()) && null != materialQueryDto.getStatusList() && 1 == materialQueryDto.getStatusList().length && "2".equals(materialQueryDto.getStatusList()[0])){
            //获取用户组的品牌权限列表
            ApiResult<Map<String,String>> brandList = amcService.getByUserDataPermissionsAll("materialLibrary", "read",companyUserInfo.get().getUserId(),"brand");
            if (Objects.nonNull(brandList.getData())){
                materialQueryDto.setBrandList(Lists.newArrayList(brandList.getData().values()));
            }

        }

        //排序
        BaseQueryWrapper qw = new BaseQueryWrapper<>();

        if (Constants.CREATE_DATE_ASC.equals(materialQueryDto.getShowSort())) {
            qw.orderByAsc("tm.create_date");
        } else {
            qw.orderByDesc("tm.create_date");
        }
        materialQueryDto.setEw(qw);

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
        Map<String, UserCompany> userAvatarMap = amcFeignService.getUserAvatarAndUserName(CollUtil.join(userIds, ","));

        // 获取素材库功能名称
        Map<String, String> materialCategoryNames = ccmFeignService.findStructureTreeNameByCategoryIds(CollUtil.join(materialCategoryIds, ","), "功能");

        for (MaterialVo materialVo : materialAllDtolist) {
            if (null != userAvatarMap.get(materialVo.getCreateId())){
                materialVo.setUserAvatar(userAvatarMap.get(materialVo.getCreateId()).getAliasUserAvatar());
                materialVo.setUserName(userAvatarMap.get(materialVo.getCreateId()).getUsername());
            }

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

            if (StringUtils.isNotEmpty(material.getMaterialBrandName())){
                String[] split = Pinyin4jUtil.converterToFirstSpell(material.getMaterialBrandName()).split(",");
                String time = String.valueOf(System.currentTimeMillis());
                String materialCode = split[0] + time.substring(time.length() - 6) + ThreadLocalRandom.current().nextInt(100000, 999999);
                material.setMaterialCode(materialCode);
            }
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
            if ("2".equals(materialSaveDto.getStatus()) && null != materialSaveDto.getCiteFlag() && materialSaveDto.getCiteFlag()){
                throw new OtherException("该素材已经被引用，暂不允许编辑！");
            }
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
    public List<MaterialLinkageVo> linkageQuery(String search, String materialCategoryIds,String folderId, String personQuery) {

        //personQuery为1的时候，为个人查询
        String status = Constants.ONE_STR.equals(personQuery) ? null : "2";
        String createId = Constants.ONE_STR.equals(personQuery) ? userUtils.getUserId() : null;

        List<MaterialLinkageVo> list = Lists.newArrayList();
        //素材标签相关
        List<String> materialCategoryIdList = StringUtils.convertList(materialCategoryIds);
        List<MaterialChildren> labelList = materialLabelService.linkageQuery(search,materialCategoryIdList,folderId,status,createId);
        if (CollUtil.isNotEmpty(labelList)){
            MaterialLinkageVo materialLinkageVo = new MaterialLinkageVo();
            materialLinkageVo.setChildren(labelList);
            materialLinkageVo.setGroup("公司标签");
            list.add(materialLinkageVo);
        }
        // 素材名称相关
        List<MaterialChildren> materialNameList = this.getBaseMapper().linkageQueryName(search,materialCategoryIdList,folderId,status,createId);
        if (CollUtil.isNotEmpty(materialNameList)){
            MaterialLinkageVo materialLinkageVo = new MaterialLinkageVo();
            materialLinkageVo.setChildren(materialNameList);
            materialLinkageVo.setGroup("素材名称");
            list.add(materialLinkageVo);
        }

        //个人标签相关

        if (Constants.ONE_STR.equals(personQuery)){
            List<MaterialChildren> descInfoList = this.getBaseMapper().linkageDescInfo(search,materialCategoryIdList,folderId,status,createId);
            if (CollUtil.isNotEmpty(descInfoList)){
                MaterialLinkageVo materialLinkageVo = new MaterialLinkageVo();
                materialLinkageVo.setChildren(descInfoList);
                materialLinkageVo.setGroup("个人分类");
                list.add(materialLinkageVo);
            }
        }
        return list;
    }

    @Override
    public boolean checkFolderRelation(List<String> folderIds) {
        QueryWrapper<Material> qw = new QueryWrapper<>();
        qw.lambda().in(Material::getFolderId, folderIds);
        qw.lambda().eq(Material::getDelFlag, "0");
        return count(qw) > 0;
    }

    @Override
    public long getFileCount(String userId,List<String> folderIds) {
        QueryWrapper<Material> qw = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(folderIds)){
            qw.lambda().in(Material::getFolderId, folderIds);
        }
        qw.lambda().eq(Material::getCreateId,userId);
        qw.lambda().eq(Material::getDelFlag,"0");
        return count(qw);
    }

    @Override
    public Long getFileSize(String userId, List<String> folderIds) {
        Long fileSize = baseMapper.getFileSize(userId, folderIds);
        return null == fileSize ? 0L : fileSize;
    }

    @Override
    public void mergeFolderReplace(String id, List<String> byMergeFolderIds) {
        UpdateWrapper<Material> qw = new UpdateWrapper<>();
        qw.lambda().in(Material::getFolderId,byMergeFolderIds);
        qw.lambda().set(Material::getFolderId,id);
        update(qw);
    }

    @Override
    public List<String> listImgQuery(MaterialQueryDto materialQueryDto) {
        materialQueryDto.setCompanyCode(userUtils.getCompanyCode());
        materialQueryDto.setUserId(userUtils.getUserId());
        this.addQuery(materialQueryDto);
        PageHelper.startPage(materialQueryDto);
        List<MaterialVo> materialAllDtolist = materialMapper.listQuery(materialQueryDto);
        minioUtils.setObjectUrlToList(materialAllDtolist, "picUrl");
        if (CollUtil.isEmpty(materialAllDtolist)){
            return null;
        }
        return materialAllDtolist.stream().map(MaterialVo::getPicUrl).collect(Collectors.toList());
    }

    @Override
    public void delMaterialPersonSpace(List<String> userIds) {

        QueryWrapper<Material> qw = new QueryWrapper<>();
        qw.lambda().in(Material::getCreateId,userIds);
        qw.lambda().eq(Material::getDelFlag,"0");
        qw.lambda().notIn(Material::getStatus,Lists.newArrayList("2"));
        List<Material> materials = list(qw);
        if (CollUtil.isEmpty(materials)){
            return;
        }
        //删除个人除了已发布的所有数据
        UpdateWrapper<Material> uw = new UpdateWrapper<>();
        uw.lambda().in(Material::getCreateId,userIds);
        uw.lambda().notIn(Material::getStatus,Lists.newArrayList("2"));
        uw.lambda().eq(Material::getDelFlag,"0");
        uw.lambda().set(Material::getDelFlag,"1");
        boolean b = update(uw);
        if (b){
            //修改成功，删除远端文件
            materials.forEach(item ->minioUtils.delFile(item.getPicUrl()));
        }

    }

    @Override
    public Map<String, List<String>> listImg(MaterialQueryDto materialQueryDto) {
        Map<String, String> basicStructureMap = materialQueryDto.getBasicStructureMap();

        if (CollUtil.isEmpty(basicStructureMap)){
            return Maps.newHashMap();
        }
        materialQueryDto.setCompanyCode(userUtils.getCompanyCode());
        materialQueryDto.setUserId(userUtils.getUserId());
        this.addQuery(materialQueryDto);
        PageHelper.startPage(materialQueryDto);
        Map<String, List<String>> map = Maps.newHashMap();
        for (String key : basicStructureMap.keySet()) {
            List<String> materialBasicStructureIds = ccmFeignService.getMaterialBasicStructureIds(basicStructureMap.get(key),key);
            if (CollUtil.isEmpty(materialBasicStructureIds)){
                continue;
            }
            materialQueryDto.setMaterialCategoryIds(materialBasicStructureIds);
            List<MaterialVo> materialAllDtolist = materialMapper.listQuery(materialQueryDto);
            if (CollUtil.isEmpty(materialAllDtolist)){
                continue;
            }
            minioUtils.setObjectUrlToList(materialAllDtolist, "picUrl");
            map.put(key, materialAllDtolist.stream().map(MaterialVo::getPicUrl).collect(Collectors.toList()));

        }
        return map;
    }

}
