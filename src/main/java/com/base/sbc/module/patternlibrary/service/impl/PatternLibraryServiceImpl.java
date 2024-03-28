package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.flowable.service.FlowableFeignService;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.entity.*;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryMapper;
import com.base.sbc.module.patternlibrary.service.*;
import com.base.sbc.module.patternlibrary.vo.CategoriesTypeVO;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.task.vo.FlowTaskDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private StyleService styleService;

    @Autowired
    private StyleColorService styleColorService;

    @Autowired
    private StylePicUtils stylePicUtils;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private FlowableFeignService flowableFeignService;

    @Value("${brand.puts}")
    private List<String> brandPuts;

    @Value("${brand.bottoms}")
    private List<String> brandBottoms;

    @Override
    public PageInfo<PatternLibraryVO> listPages(PatternLibraryPageDTO patternLibraryPageDTO) {
        QueryWrapper<PatternLibraryVO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                // 版型编码
                .like(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getCode())
                        , "tpl.code", patternLibraryPageDTO.getCode())
                // 大类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getProdCategory1st())
                        , "tpl.prod_category1st", patternLibraryPageDTO.getProdCategory1st())
                // 品类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getProdCategory())
                        , "tpl.prod_category", patternLibraryPageDTO.getProdCategory())
                // 中类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getProdCategory2nd())
                        , "tpl.prod_category2nd", patternLibraryPageDTO.getProdCategory2nd())
                // 廓形编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getSilhouetteCode())
                        , "tpl.silhouette_code", patternLibraryPageDTO.getSilhouetteCode())
                // 所属版型库
                .in(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getTemplateCodeList())
                        , "tpl.template_code", patternLibraryPageDTO.getTemplateCodeList())
                // 状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getStatus())
                        , "tpl.status", patternLibraryPageDTO.getStatus())
                // 启用状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getEnableFlag())
                        , "tpl.enable_flag", patternLibraryPageDTO.getEnableFlag());
        // 权限设置
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tpl");
        // 列表分页
        PageHelper.startPage(patternLibraryPageDTO.getPageNum(), patternLibraryPageDTO.getPageSize());
        // 得到版型库主表数据集合
        List<PatternLibraryVO> patternLibraryVOList = baseMapper.listPages(queryWrapper, patternLibraryPageDTO);
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

            // 设置图片
            stylePicUtils.setStylePic(patternLibraryVOList, "picFileId");
        }
        return new PageInfo<>(patternLibraryVOList);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateDetails(PatternLibraryDTO patternLibraryDTO) {
        if (ObjectUtil.isEmpty(patternLibraryDTO)) {
            throw new OtherException("新增/编辑数据不能为空！");
        }
        PatternLibrary patternLibrary = new PatternLibrary();
        BeanUtil.copyProperties(patternLibraryDTO, patternLibrary);
        if (ObjectUtil.isEmpty(patternLibraryDTO.getId())) {
            // 新增
            // 新增主表数据
            save(patternLibrary);
            // 新增品牌数据
            List<PatternLibraryBrand> patternLibraryBrandList = patternLibraryDTO.getPatternLibraryBrandList();
            if (ObjectUtil.isNotEmpty(patternLibraryBrandList)) {
                for (PatternLibraryBrand patternLibraryBrand : patternLibraryBrandList) {
                    patternLibraryBrand.setPatternLibraryId(patternLibrary.getId());
                }
                patternLibraryBrandService.saveBatch(patternLibraryBrandList);
            }
            // 新增子表数据
            List<PatternLibraryItem> patternLibraryItemList = patternLibraryDTO.getPatternLibraryItemList();
            if (ObjectUtil.isNotEmpty(patternLibraryItemList)) {
                for (PatternLibraryItem patternLibraryItem : patternLibraryItemList) {
                    patternLibraryItem.setPatternLibraryId(patternLibrary.getId());
                }
                patternLibraryItemService.saveBatch(patternLibraryItemList);
            }
        } else {
            // 编辑
            // 修改主表数据
            updateById(patternLibrary);
            // 修改品牌数据
            List<PatternLibraryBrand> patternLibraryBrandList = patternLibraryDTO.getPatternLibraryBrandList();
            if (ObjectUtil.isNotEmpty(patternLibraryBrandList)) {
                List<String> patternLibraryBrandIdList = patternLibraryBrandList
                        .stream().map(PatternLibraryBrand::getId)
                        .filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
                // 先删除此版型库下的数据且不是此 ID 集合下的数据
                patternLibraryBrandService.remove(
                        new LambdaQueryWrapper<PatternLibraryBrand>()
                                .eq(PatternLibraryBrand::getPatternLibraryId, patternLibrary.getId())
                                .notIn(PatternLibraryBrand::getId, patternLibraryBrandIdList)
                );
                for (PatternLibraryBrand patternLibraryBrand : patternLibraryBrandList) {
                    patternLibraryBrand.setPatternLibraryId(patternLibrary.getId());
                }
                patternLibraryBrandService.saveOrUpdateBatch(patternLibraryBrandList);
            }
            // 修改子表数据
            List<PatternLibraryItem> patternLibraryItemList = patternLibraryDTO.getPatternLibraryItemList();
            if (ObjectUtil.isNotEmpty(patternLibraryItemList)) {
                List<String> patternLibraryItemIdList = patternLibraryItemList
                        .stream().map(PatternLibraryItem::getId)
                        .filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
                // 先删除此版型库下的数据且不是此 ID 集合下的数据
                patternLibraryItemService.remove(
                        new LambdaQueryWrapper<PatternLibraryItem>()
                                .eq(PatternLibraryItem::getPatternLibraryId, patternLibrary.getId())
                                .notIn(PatternLibraryItem::getId, patternLibraryItemIdList)
                );
                for (PatternLibraryItem patternLibraryItem : patternLibraryItemList) {
                    patternLibraryItem.setPatternLibraryId(patternLibrary.getId());
                }
                patternLibraryItemService.saveOrUpdateBatch(patternLibraryItemList);
            }
        }
        if (patternLibraryDTO.getStatus().equals(PatternLibraryStatusEnum.NO_REVIEWED.getCode())) {
            // 如果是提交 那么启动审批流
            flowableService.start(FlowableService.PATTERN_LIBRARY_APPROVAL,
                    FlowableService.PATTERN_LIBRARY_APPROVAL, patternLibrary.getId(),
                    "/pdm/api/saas/patternLibrary/approval",
                    "/pdm/api/saas/patternLibrary/approval",
                    "/pdm/api/saas/patternLibrary/approval",
                    "pdm/api/saas/patternLibrary/getDetail?patternLibraryId=" + patternLibrary.getId(), BeanUtil.beanToMap(patternLibrary));
        }
        return Boolean.TRUE;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDetails(List<PatternLibraryDTO> patternLibraryDTOList) {
        if (ObjectUtil.isEmpty(patternLibraryDTOList)) {
            throw new OtherException("批量编辑数据不能为空！");
        }
        long count = patternLibraryDTOList
                .stream()
                .filter(item -> item.getStatus().equals(PatternLibraryStatusEnum.NO_PADDED.getCode()))
                .count();
        if (patternLibraryDTOList.size() != count) {
            throw new OtherException("批量编辑只能选择待补齐！");
        }
        // 批量编辑
        // 判断设计款的大类和选择的大类是否都属于上装或者下装
        {
            List<String> styleIdList = patternLibraryDTOList.
                    stream().map(PatternLibraryDTO::getStyleId).collect(Collectors.toList());
            List<Style> styleList = styleService.listByIds(styleIdList);
            long num = 0L;
            if (brandPuts.contains(patternLibraryDTOList.get(0).getProdCategory1st())) {
                num = styleList.stream().filter(item -> !brandPuts.contains(item.getProdCategory1st())).count();
            } else {
                num = styleList.stream().filter(item -> !brandBottoms.contains(item.getProdCategory1st())).count();
            }
            if (num > 0L) {
                throw new OtherException("款式所对应的大类和所选大类的上装下装不匹配！");
            }
        }
        // 修改主表数据
        List<PatternLibrary> patternLibraryList = new ArrayList<>(patternLibraryDTOList.size());
        for (PatternLibraryDTO patternLibraryDTO : patternLibraryDTOList) {
            PatternLibrary patternLibrary = new PatternLibrary();
            BeanUtil.copyProperties(patternLibraryDTO, patternLibrary);
            patternLibraryList.add(patternLibrary);
        }
        updateBatchById(patternLibraryList);
        // 修改子表数据
        List<PatternLibraryItem> patternLibraryItemList = new ArrayList<>();
        patternLibraryDTOList.forEach(
                outItem ->
                {
                    List<PatternLibraryItem> list = outItem.getPatternLibraryItemList();
                    if (ObjectUtil.isNotEmpty(list)) {
                        // 给子表数据设置主表 ID
                        list.forEach(inItem -> inItem.setPatternLibraryId(outItem.getId()));
                        // 将子表数据聚合到一个集合中
                        patternLibraryItemList.addAll(list);
                    }
                }
        );
        if (ObjectUtil.isNotEmpty(patternLibraryItemList)) {
            patternLibraryItemService.saveBatch(patternLibraryItemList);
        }
        return true;
    }

    @Override
    public PatternLibraryVO getDetail(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException("请选择数据查看！");
        }
        // 初始化返回数据
        PatternLibraryVO patternLibraryVO = new PatternLibraryVO();
        // 根据版型库主表 ID 查询版型库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        BeanUtil.copyProperties(patternLibrary, patternLibraryVO);
        // 设置图片
        stylePicUtils.setStylePic(Collections.singletonList(patternLibraryVO), "picFileId");

        // 查询品类信息
        List<PatternLibraryBrand> patternLibraryBrandList = patternLibraryBrandService.list(
                new LambdaQueryWrapper<PatternLibraryBrand>()
                        .eq(PatternLibraryBrand::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibraryVO.setPatternLibraryBrandList(patternLibraryBrandList);
        // 查询版型库子表信息
        List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .eq(PatternLibraryItem::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibraryVO.setPatternLibraryItemList(patternLibraryItemList);
        // 查询版型库模板信息
        PatternLibraryTemplate patternLibraryTemplate = patternLibraryTemplateService.getOne(
                new LambdaQueryWrapper<PatternLibraryTemplate>()
                        .eq(PatternLibraryTemplate::getCode, patternLibrary.getTemplateCode())
                        .eq(PatternLibraryTemplate::getEnableFlag, 1)
                        .eq(PatternLibraryTemplate::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibraryVO.setPatternLibraryTemplate(patternLibraryTemplate);
        // 查询版型库模板子表信息
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
        // 根据版型库主表 ID 查询版型库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        // 删除版型库主表数据
        removeById(patternLibraryId);
        // 删除版型库品类数据
        patternLibraryBrandService.remove(
                new LambdaQueryWrapper<PatternLibraryBrand>()
                        .eq(PatternLibraryBrand::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        // 删除版型库子表数据
        patternLibraryItemService.remove(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .eq(PatternLibraryItem::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        return true;
    }

    @Override
    @DuplicationCheck
    public Boolean updateAuditsPass(List<String> patternLibraryIdList) {
        if (ObjectUtil.isEmpty(patternLibraryIdList)) {
            throw new OtherException("请至少选择一条数据进行审核！");
        }
        // 查询到我的待办列表和这边选择的业务 ID 进行匹配，如果根据业务 ID 查询到了待办数据，
        // 那么查询到的待办数据进行批量审核，如果一条都没有，提示没有数据审核
        Map<String, Object> map = new HashMap<>();
        map.put("businessKeyList", patternLibraryIdList);
        ApiResult apiResult = flowableFeignService.todoList(map);
        Map<String, Object> data = (Map<String, Object>) apiResult.getData();
        String jsonString = JSON.toJSONString(data);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        List<FlowTaskDto> flowTaskDtoList = jsonArray.toJavaList(FlowTaskDto.class);
        if (ObjectUtil.isNotEmpty(flowTaskDtoList)) {
            throw new OtherException("勾选数据暂无审核！");
        }
        for (FlowTaskDto flowTaskDto : flowTaskDtoList) {
            flowableFeignService.complete(flowTaskDto.getTaskId(), flowTaskDto.getProcInsId(), "部件库批量审核通过");
        }
        return Boolean.TRUE;
    }

    @Override
    @DuplicationCheck
    public Boolean updateAuditsReject(List<String> patternLibraryIdList) {
        if (ObjectUtil.isEmpty(patternLibraryIdList)) {
            throw new OtherException("请至少选择一条数据进行审核！");
        }
        // 查询到我的待办列表和这边选择的业务 ID 进行匹配，如果根据业务 ID 查询到了待办数据，
        // 那么查询到的待办数据进行批量审核，如果一条都没有，提示没有数据审核
        Map<String, Object> map = new HashMap<>();
        map.put("businessKeyList", patternLibraryIdList);
        ApiResult apiResult = flowableFeignService.todoList(map);
        Map<String, Object> data = (Map<String, Object>) apiResult.getData();
        String jsonString = JSON.toJSONString(data);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        List<FlowTaskDto> flowTaskDtoList = jsonArray.toJavaList(FlowTaskDto.class);
        if (ObjectUtil.isNotEmpty(flowTaskDtoList)) {
            throw new OtherException("勾选数据暂无审核！");
        }
        for (FlowTaskDto flowTaskDto : flowTaskDtoList) {
            flowableFeignService.reject(flowTaskDto.getTaskId(), "部件库批量审核驳回");
        }
        return Boolean.TRUE;
    }


    @Override
    public List<Style> listStyle() {
        return styleService.list(
                new LambdaQueryWrapper<Style>()
                        .eq(Style::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
                        .eq(Style::getEnableStatus, BaseGlobal.NO)
                        .eq(Style::getStatus, "1")
        );
    }

    @Override
    public PatternLibraryVO getInfoByDesignNo(String designNo) {
        // 初始化返回的封装数据
        PatternLibraryVO patternLibraryVO = new PatternLibraryVO();
        if (ObjectUtil.isEmpty(designNo)) {
            throw new OtherException("请选择设计款号！");
        }
        Style style = styleService.getOne(
                new LambdaQueryWrapper<Style>()
                        .eq(Style::getDesignNo, designNo)
                        .eq(Style::getEnableStatus, "0")
                        .eq(Style::getDelFlag, "0")
                        .eq(Style::getConfirmStatus, "2")
        );
        if (ObjectUtil.isEmpty(style)) {
            throw new OtherException("设计款不存在，请刷新后重试！");
        }
        // 设计款号
        patternLibraryVO.setDesignNo(style.getDesignNo());
        // 版型编码 等同于设计款号
        patternLibraryVO.setCode(style.getDesignNo());
        // 款式 ID
        patternLibraryVO.setStyleId(style.getId());
        // 大类 code
        patternLibraryVO.setProdCategory1st(style.getProdCategory1st());
        // 大类名称
        patternLibraryVO.setProdCategory1stName(style.getProdCategory1st());
        // 品类 code
        patternLibraryVO.setProdCategory(style.getProdCategory());
        // 品类名称
        patternLibraryVO.setProdCategoryName(style.getProdCategoryName());
        // 中类 code
        patternLibraryVO.setProdCategory2nd(style.getProdCategory2nd());
        // 中类名称
        patternLibraryVO.setProdCategory2ndName(style.getProdCategory2ndName());
        // 小类 code
        patternLibraryVO.setProdCategory3rd(style.getProdCategory3rd());
        // 小类名称
        patternLibraryVO.setProdCategory3rdName(style.getProdCategory3rdName());
        // 廓形 code
        patternLibraryVO.setSilhouetteCode(style.getSilhouette());
        // 廓形名称
        patternLibraryVO.setSilhouetteName(style.getSilhouetteName());
        // 生成品牌
        PatternLibraryBrand patternLibraryBrand = new PatternLibraryBrand();
        patternLibraryBrand.setBrand(style.getBrand());
        patternLibraryBrand.setBrandName(style.getBrandName());
        // 图片信息 图获取该设计款拍照图（待定） > 大货图（款式配色的图片） > 设计图
        // 先获取款式配色信息
        List<StyleColor> styleColorList = styleColorService.list(
                new LambdaQueryWrapper<StyleColor>()
                        .eq(StyleColor::getStyleId, style.getId())
                        .eq(StyleColor::getDelFlag, "0")
        );
        // 初始化大货的图片 ID-URL 集合
        List<Map<String, String>> picFileIdList = new ArrayList<>(styleColorList.size());

        if (ObjectUtil.isNotEmpty(styleColorList)) {
            for (StyleColor styleColor : styleColorList) {
                // 当作临时变量存储一下图片来源 ID
                styleColor.setStyleNo(styleColor.getStyleColorPic());
            }
            stylePicUtils.setStylePic(styleColorList, "styleColorPic");
            for (StyleColor styleColor : styleColorList) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(" picFileId", styleColor.getStyleNo());
                hashMap.put(" url", styleColor.getStyleColorPic());
                picFileIdList.add(hashMap);
            }
            patternLibraryVO.setPicFileIdList(picFileIdList);
        } else {
            // 如果没有大货信息 那就直接取款式的图片
            patternLibraryVO.setPicFileId(style.getStylePic());
        }
        return patternLibraryVO;
    }

    /**
     * @return
     */
    @Override
    public CategoriesTypeVO getCategoriesType() {
        CategoriesTypeVO categoriesTypeVO = new CategoriesTypeVO();
        categoriesTypeVO.setBrandPuts(brandPuts);
        categoriesTypeVO.setBrandBottoms(brandBottoms);
        return categoriesTypeVO;
    }


}
