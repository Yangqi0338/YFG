package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.entity.*;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryMapper;
import com.base.sbc.module.patternlibrary.service.*;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 版型库-主表 服务实现类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Service
public class PatternLibraryServiceImpl extends ServiceImpl<PatternLibraryMapper, PatternLibrary> implements PatternLibraryService {

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    private PatternLibraryBrandService patternLibraryBrandService;

    @Autowired
    private PatternLibraryItemService patternLibraryItemService;

    @Autowired
    private PatternLibraryTemplateService patternLibraryTemplateService;

    @Autowired
    private PatternLibraryTemplateItemService patternLibraryTemplateItemService;

    @Override
    public PageInfo<PatternLibraryVO> listPages(PatternLibraryDTO patternLibraryDTO) {
        QueryWrapper<PatternLibraryVO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                // 版型编码
                .like(ObjectUtil.isNotEmpty(patternLibraryDTO.getCode())
                        , "tpl.code", patternLibraryDTO.getCode())
                // 大类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory1st())
                        , "tpl.prod_category1st", patternLibraryDTO.getProdCategory1st())
                // 品类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory())
                        , "tpl.prod_category", patternLibraryDTO.getProdCategory())
                // 中类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory2nd())
                        , "tpl.prod_category2nd", patternLibraryDTO.getProdCategory2nd())
                // 廓形编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getSilhouetteCode())
                        , "tpl.silhouette_code", patternLibraryDTO.getSilhouetteCode())
                // 所属版型库
                .in(ObjectUtil.isNotEmpty(patternLibraryDTO.getTemplateCodeList())
                        , "tpl.template_code", patternLibraryDTO.getTemplateCodeList())
                // 状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getStatus())
                        , "tpl.status", patternLibraryDTO.getStatus())
                // 启用状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getEnableFlag())
                        , "tpl.enable_flag", patternLibraryDTO.getEnableFlag());
        // 权限设置
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tpl");
        // 列表分页
        PageHelper.startPage(patternLibraryDTO.getPageNum(), patternLibraryDTO.getPageSize());
        // 得到部件库主表数据集合
        List<PatternLibraryVO> patternLibraryVOList = baseMapper.listPages(queryWrapper, patternLibraryDTO);
        // 设置子表数据
        if (ObjectUtil.isNotEmpty(patternLibraryVOList)) {
            // *************** 查询子表数据 ***************
            // 拿到分页后的主表 ID
            List<String> patterLibraryIdList = patternLibraryVOList
                    .stream().map(PatternLibraryVO::getId).collect(Collectors.toList());
            // 根据主表 ID 查询子表数据
            List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                    new LambdaQueryWrapper<PatternLibraryItem>()
                            .in(PatternLibraryItem::getPatternLibraryId, patterLibraryIdList)
                            .in(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
            );
            // 子表根据主表 ID 分组转成 map
            Map<String, List<PatternLibraryItem>> patternLibraryItemMap = Collections.emptyMap();
            if (ObjectUtil.isNotEmpty(patternLibraryItemList)) {
                patternLibraryItemMap = patternLibraryItemList
                        .stream()
                        .collect(Collectors.groupingBy(PatternLibraryItem::getPatternLibraryId));
            }

            // *************** 查询模板数据 ****************
            // 拿到分页后的模板 code 集合
            Set<String> templateCodeList = patternLibraryVOList
                    .stream().map(PatternLibraryVO::getTemplateCode).collect(Collectors.toSet());
            // 根据模板 code 集合查询模板数据
            List<PatternLibraryTemplate> patternLibraryTemplateList = patternLibraryTemplateService.list(
                    new LambdaQueryWrapper<PatternLibraryTemplate>()
                            .in(PatternLibraryTemplate::getCode, templateCodeList)
                            .in(PatternLibraryTemplate::getEnableFlag, 1)
                            .in(PatternLibraryTemplate::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
            );
            // 模板数据根据模板 code 转成 map
            Map<String, PatternLibraryTemplate> patternLibraryTemplateMap = Collections.emptyMap();
            if (ObjectUtil.isNotEmpty(patternLibraryTemplateList)) {
                patternLibraryTemplateMap = patternLibraryTemplateList
                        .stream().collect(Collectors.toMap(PatternLibraryTemplate::getCode, item -> item));
            }

            // *************** 查询模板子表数据 ***************
            // 如果模板数据不为空
            if (ObjectUtil.isNotEmpty(patternLibraryTemplateList)) {
                // 拿到模板数据的 ID 集合
                Set<String> patternLibraryTemplateIdSet = patternLibraryTemplateList
                        .stream().map(PatternLibraryTemplate::getId).collect(Collectors.toSet());
                // 根据模板数据的 ID 集合 查询出模板子表的数据
                List<PatternLibraryTemplateItem> patternLibraryTemplateItemList = patternLibraryTemplateItemService.list(
                        new LambdaQueryWrapper<PatternLibraryTemplateItem>()
                                .in(PatternLibraryTemplateItem::getTemplateId, patternLibraryTemplateIdSet)
                                .in(PatternLibraryTemplateItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
                );
                // 按照模板 ID 分组
                Map<String, List<PatternLibraryTemplateItem>> colpatternLibraryTemplateItemMap = Collections.emptyMap();
                if (ObjectUtil.isNotEmpty(patternLibraryTemplateItemList)) {
                    colpatternLibraryTemplateItemMap = patternLibraryTemplateItemList
                            .stream().collect(Collectors.groupingBy(PatternLibraryTemplateItem::getTemplateId));
                }
                // 设置模板子表数据
                for (PatternLibraryTemplate patternLibraryTemplate : patternLibraryTemplateList) {
                    patternLibraryTemplate.setPatternLibraryTemplateItemList(
                                    colpatternLibraryTemplateItemMap.get(patternLibraryTemplate.getId())
                            );
                }
            }

            for (PatternLibraryVO patternLibraryVO : patternLibraryVOList) {
                // 设置所属版型库数据
                patternLibraryVO.setPatternLibraryTemplate(
                        patternLibraryTemplateMap.get(patternLibraryVO.getTemplateCode())
                );
                // 设置子表数据
                patternLibraryVO.setPatternLibraryItemList(patternLibraryItemMap.get(patternLibraryVO.getId()));
            }
        }
        return new PageInfo<>(patternLibraryVOList);
    }

    @Override
    public PatternLibraryVO getDetail(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException("请选择数据查看！");
        }
        // 初始化返回数据
        PatternLibraryVO patternLibraryVO = new PatternLibraryVO();
        // 根据部件库主表 ID 查询部件库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        BeanUtil.copyProperties(patternLibrary, patternLibraryVO);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        // 查询品类信息
        List<PatternLibraryBrand> patternLibraryBrandList = patternLibraryBrandService.list(
                new LambdaQueryWrapper<PatternLibraryBrand>()
                        .eq(PatternLibraryBrand::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibraryVO.setPatternLibraryBrandList(patternLibraryBrandList);
        // 查询部件库子表信息
        List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .eq(PatternLibraryItem::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibraryVO.setPatternLibraryItemList(patternLibraryItemList);
        // 查询部件库模板信息
        PatternLibraryTemplate patternLibraryTemplate = patternLibraryTemplateService.getOne(
                new LambdaQueryWrapper<PatternLibraryTemplate>()
                        .eq(PatternLibraryTemplate::getCode, patternLibrary.getTemplateCode())
                        .eq(PatternLibraryTemplate::getEnableFlag, 1)
                        .eq(PatternLibraryTemplate::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibraryVO.setPatternLibraryTemplate(patternLibraryTemplate);
        // 查询部件库模板子表信息
        if (ObjectUtil.isNotEmpty(patternLibraryTemplate)) {
            List<PatternLibraryTemplateItem> patternLibraryTemplateItemList = patternLibraryTemplateItemService.list(
                    new LambdaQueryWrapper<PatternLibraryTemplateItem>()
                            .eq(PatternLibraryTemplateItem::getTemplateId, patternLibraryTemplate.getId())
                            .eq(PatternLibraryTemplateItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
            );
            patternLibraryTemplate.setPatternLibraryTemplateItemList(patternLibraryTemplateItemList);
        }
        return patternLibraryVO;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDetail(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException("请选择数据删除！");
        }
        // 根据部件库主表 ID 查询部件库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        // 删除部件库主表数据
        removeById(patternLibraryId);
        // 删除部件库品类数据
        patternLibraryBrandService.remove(
                new LambdaQueryWrapper<PatternLibraryBrand>()
                        .eq(PatternLibraryBrand::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        // 删除部件库子表数据
        patternLibraryItemService.remove(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .eq(PatternLibraryItem::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        return true;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAudits(List<String> patternLibraryIdList) {
        if (ObjectUtil.isEmpty(patternLibraryIdList)) {
            throw  new OtherException("请至少选择一条数据进行审核！");
        }
        // 根据部件库主表 ID 集合批量审批数据 非 待审核 数据直接过滤
        List<PatternLibrary> patternLibraryList = list(
                new LambdaQueryWrapper<PatternLibrary>()
                        .in(PatternLibrary::getId, patternLibraryIdList)
                        .eq(PatternLibrary::getStatus, PatternLibraryStatusEnum.NO_REVIEWED.getCode())
        );
        if (ObjectUtil.isEmpty(patternLibraryList)) {
            throw new OtherException("暂无需审核数据！");
        }
        // 批量修改成已审核的状态
        patternLibraryList.forEach(item -> item.setStatus(PatternLibraryStatusEnum.REVIEWED.getCode()));
        // TODO：审批流的操作 ——XHTE
        // 更新修改数据
        boolean updateFlag = updateBatchById(patternLibraryList);
        return updateFlag;
    }


}
