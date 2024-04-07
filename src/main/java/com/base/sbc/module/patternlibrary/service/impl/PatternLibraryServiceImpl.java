package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.patternlibrary.dto.AuditsDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.entity.*;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryMapper;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryTemplateMapper;
import com.base.sbc.module.patternlibrary.service.*;
import com.base.sbc.module.patternlibrary.vo.CategoriesTypeVO;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private PatternLibraryTemplateMapper patternLibraryTemplateMapper;

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

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private UploadFileService uploadFileService;

    @Value("${brand.puts:A01,A02,A03}")
    private String brandPuts;

    @Value("${brand.bottoms:A04}")
    private String brandBottoms;

    @Override
    public PageInfo<PatternLibraryVO> listPages(PatternLibraryPageDTO patternLibraryPageDTO) {
        QueryWrapper<PatternLibraryVO> queryWrapper = new QueryWrapper<>();
        String templateCodes = patternLibraryPageDTO.getTemplateCodes();
        String partsCodes = patternLibraryPageDTO.getPartsCodes();
        patternLibraryPageDTO.setPartsCodeList(ObjectUtil.isNotEmpty(partsCodes) ? Arrays.asList(partsCodes.split(",")) : null);
        String brands = patternLibraryPageDTO.getBrands();
        patternLibraryPageDTO.setBrandList(ObjectUtil.isNotEmpty(brands) ? Arrays.asList(brands.split(",")) : null);
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
                .in(ObjectUtil.isNotEmpty(templateCodes)
                        , "tpl.template_code", ObjectUtil.isNotEmpty(templateCodes) ? Arrays.asList(templateCodes.split(",")) : null)
                // 状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getStatus())
                        , "tpl.status", patternLibraryPageDTO.getStatus())
                // 启用状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getEnableFlag())
                        , "tpl.enable_flag", patternLibraryPageDTO.getEnableFlag());
        // 权限设置
        dataPermissionsService.getDataPermissionsForQw(
                queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tpl"
        );
        // 列表分页
        PageHelper.startPage(patternLibraryPageDTO.getPageNum(), patternLibraryPageDTO.getPageSize());
        // 得到版型库主表数据集合
        List<PatternLibraryVO> patternLibraryVOList = baseMapper.listPages(queryWrapper, patternLibraryPageDTO);
        // 设置子表数据
        if (ObjectUtil.isNotEmpty(patternLibraryVOList)) {
            // 拿到分页后的主表 ID
            List<String> patterLibraryIdList = patternLibraryVOList
                    .stream().map(PatternLibraryVO::getId).collect(Collectors.toList());
            // *************** 查询品牌数据 ***************
            // 根据主表 ID 查询品类数据
            List<PatternLibraryBrand> patternLibraryBrandList = patternLibraryBrandService.list(
                    new LambdaQueryWrapper<PatternLibraryBrand>()
                            .in(PatternLibraryBrand::getPatternLibraryId, patterLibraryIdList)
                            .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
            );
            // 子表根据主表 ID 分组转成 map
            Map<String, List<PatternLibraryBrand>> patternLibraryBrandMap = Collections.emptyMap();
            if (ObjectUtil.isNotEmpty(patternLibraryBrandList)) {
                patternLibraryBrandMap = patternLibraryBrandList
                        .stream()
                        .collect(Collectors.groupingBy(PatternLibraryBrand::getPatternLibraryId));
            }
            // *************** 查询子表数据 ***************
            // 拿到分页后的主表 ID
            // 根据主表 ID 查询子表数据
            List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                    new LambdaQueryWrapper<PatternLibraryItem>()
                            .in(PatternLibraryItem::getPatternLibraryId, patterLibraryIdList)
                            .eq(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
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
            List<String> templateCodeList = patternLibraryVOList
                    .stream().map(PatternLibraryVO::getTemplateCode).distinct().collect(Collectors.toList());
            // 初始化模板 Map 数据
            Map<String, PatternLibraryTemplate> patternLibraryTemplateMap = new HashMap<>();
            if (ObjectUtil.isNotEmpty(templateCodeList)) {
                // 根据模板 code 集合查询模板数据
                List<PatternLibraryTemplate> patternLibraryTemplateList = patternLibraryTemplateMapper.listByCodes(templateCodeList);
                // 模板数据根据模板 code 转成 map
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
                        List<PatternLibraryTemplateItem> patternLibraryTemplateItems =
                                colpatternLibraryTemplateItemMap.get(patternLibraryTemplate.getId());
                        if (ObjectUtil.isNotEmpty(patternLibraryTemplateItems)) {
                            // 设置原始数据
                            patternLibraryTemplate.setPatternLibraryTemplateItemList(patternLibraryTemplateItems);
                            // 格式化成前端所需要的数据
                            List<String> modifiableList = patternLibraryTemplateItems.stream()
                                    .filter(item -> item.getType().equals(1))
                                    .map(PatternLibraryTemplateItem::getPatternTypeName).collect(Collectors.toList());
                            List<String> notModifiableList = patternLibraryTemplateItems.stream()
                                    .filter(item -> item.getType().equals(2))
                                    .map(PatternLibraryTemplateItem::getPatternTypeName).collect(Collectors.toList());
                            if (ObjectUtil.isNotEmpty(modifiableList) && ObjectUtil.isNotEmpty(notModifiableList)) {
                                patternLibraryTemplate.setPatternLibraryTemplateItem(
                                        StringUtils.join(modifiableList, "/")
                                                + "可修改\n" + StringUtils.join(notModifiableList, "/")
                                                + "不可修改"
                                );
                            } else if (ObjectUtil.isNotEmpty(notModifiableList)) {
                                patternLibraryTemplate.setPatternLibraryTemplateItem(
                                        StringUtils.join(notModifiableList, "/") + "不可修改"
                                );
                            } else if (ObjectUtil.isNotEmpty(modifiableList)) {
                                patternLibraryTemplate.setPatternLibraryTemplateItem(
                                        StringUtils.join(modifiableList, "/") + "可修改"
                                );
                            }
                        }
                    }
                }
            }

            for (PatternLibraryVO patternLibraryVO : patternLibraryVOList) {
                // 设置所属版型库数据
                patternLibraryVO.setPatternLibraryTemplate(
                        patternLibraryTemplateMap.get(patternLibraryVO.getTemplateCode())
                );
                // 设置版型库品牌数据
                patternLibraryVO.setPatternLibraryBrandList(patternLibraryBrandMap.get(patternLibraryVO.getId()));
                // 设置子表数据
                List<PatternLibraryItem> patternLibraryItemLists = patternLibraryItemMap.get(patternLibraryVO.getId());
                if (ObjectUtil.isNotEmpty(patternLibraryItemLists)) {
                    patternLibraryVO.setPatternLibraryItemList(patternLibraryItemLists);
                    // 设置格式化成前端的子表数据
                    // 围度
                    patternLibraryVO.setPatternLibraryItemPattern(
                            patternLibraryItemLists.stream()
                                    .filter(item -> item.getType().equals(1))
                                    .map(item -> item.getName() + "：" + Optional.ofNullable(item.getStructureValue()).orElse("暂无") + "\n")
                                    .collect(Collectors.joining(""))
                    );
                    // 长度
                    patternLibraryVO.setPatternLibraryItemLength(
                            patternLibraryItemLists.stream()
                                    .filter(item -> item.getType().equals(2))
                                    .map(item -> item.getName() + "：" + Optional.ofNullable(item.getStructureValue()).orElse("暂无") + "\n")
                                    .collect(Collectors.joining(""))
                    );
                    // 部位
                    patternLibraryVO.setPatternLibraryItemPosition(
                            patternLibraryItemLists.stream()
                                    .filter(item -> item.getType().equals(3))
                                    .map(item -> item.getName() + "：纸样实际尺寸 " + item.getPatternSize() + "\n")
                                    .collect(Collectors.joining(""))
                    );
                    // 部件
                    patternLibraryVO.setPatternLibraryItemParts(
                            patternLibraryItemLists.stream()
                                    .filter(item -> item.getType().equals(4))
                                    .map(PatternLibraryItem::getName)
                                    .distinct()
                                    .collect(Collectors.joining("/"))

                    );
                }
            }
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
        // 获取旧数据
        PatternLibrary oldPatternLibrary = new PatternLibrary();
        if (ObjectUtil.isNotEmpty(patternLibraryDTO.getId())) {
            oldPatternLibrary = getById(patternLibraryDTO.getId());
            if (!(oldPatternLibrary.getStatus().equals(PatternLibraryStatusEnum.NO_SUBMIT.getCode())
                    || oldPatternLibrary.getStatus().equals(PatternLibraryStatusEnum.REJECTED.getCode()))) {
                throw new OtherException("当前状态不能编辑！");
            }
        }
        PatternLibrary patternLibrary = new PatternLibrary();
        BeanUtil.copyProperties(patternLibraryDTO, patternLibrary);
        // 判断设计款的大类和选择的大类是否都属于上装或者下装
        {
            Style style = styleService.getById(patternLibraryDTO.getStyleId());
            if (!isFlag(patternLibraryDTO.getProdCategory1st(), Collections.singletonList(style))) {
                throw new OtherException("款式所对应的大类和所选大类的上装下装不匹配！");
            }
        }

        // 新增/修改主表数据
        saveOrUpdate(patternLibrary);
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
                            .notIn(ObjectUtil.isNotEmpty(patternLibraryBrandIdList), PatternLibraryBrand::getId, patternLibraryBrandIdList)
            );
            for (PatternLibraryBrand patternLibraryBrand : patternLibraryBrandList) {
                patternLibraryBrand.setPatternLibraryId(patternLibrary.getId());
            }
            patternLibraryBrandService.saveOrUpdateBatch(patternLibraryBrandList);
        } else {
            patternLibraryBrandService.remove(
                    new LambdaQueryWrapper<PatternLibraryBrand>()
                            .eq(PatternLibraryBrand::getPatternLibraryId, patternLibrary.getId())
            );
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
                            .notIn(ObjectUtil.isNotEmpty(patternLibraryItemIdList), PatternLibraryItem::getId, patternLibraryItemIdList)
            );
            for (PatternLibraryItem patternLibraryItem : patternLibraryItemList) {
                patternLibraryItem.setPatternLibraryId(patternLibrary.getId());
            }
            patternLibraryItemService.saveOrUpdateBatch(patternLibraryItemList);
        } else {
            patternLibraryItemService.remove(
                    new LambdaQueryWrapper<PatternLibraryItem>()
                            .eq(PatternLibraryItem::getPatternLibraryId, patternLibrary.getId())
            );
        }

        if (ObjectUtil.isNotEmpty(patternLibraryDTO.getFileId())) {
            // 先判断文件是否修改 如果没修改 就不要重新上传
            if (!patternLibraryDTO.getFileId().equals(oldPatternLibrary.getFileId())) {
                SampleAttachmentDto fileSampleAttachmentDto = new SampleAttachmentDto();
                fileSampleAttachmentDto.setFileId(patternLibraryDTO.getFileId());
                // 保存文件附件信息
                attachmentService.saveFiles(
                        patternLibrary.getId(),
                        Collections.singletonList(fileSampleAttachmentDto),
                        AttachmentTypeConstant.PATTERN_LIBRARY_FILE
                );
            }
        }

        if (ObjectUtil.isNotEmpty(patternLibraryDTO.getPicId())) {
            // 先判断图片是否修改 如果没修改 就不要重新上传
            if (!patternLibraryDTO.getPicId().equals(oldPatternLibrary.getPicId())) {
                // 初始化附件信息
                SampleAttachmentDto picSampleAttachmentDto = new SampleAttachmentDto();
                if (patternLibraryDTO.getPicSource().equals(1)) {
                    // 说明是文件上传的
                    picSampleAttachmentDto.setFileId(patternLibraryDTO.getPicId());
                } else {
                    // 说明是选择已有图片的 此时需要重新下载图片并且上传
                    try {
                        MultipartFile multipartFile = uploadFileService.downloadImage(
                                patternLibraryDTO.getPicId(), IdUtil.fastSimpleUUID() + ".png"
                        );
                        AttachmentVo attachmentVo = uploadFileService.uploadToMinio(
                                multipartFile,
                                "patternLibraryPic",
                                patternLibraryDTO.getPatternLibraryBrandList().get(0).getBrandName()
                        );
                        picSampleAttachmentDto.setFileId(attachmentVo.getFileId());
                        patternLibrary.setPicId(attachmentVo.getFileId());
                        patternLibrary.setPicUrl(attachmentVo.getUrl());
                        updateById(patternLibrary);
                    } catch (IOException e) {
                        throw new RuntimeException("文件保存失败，请刷新后重试！");
                    }
                }

                // 保存图片附件信息
                attachmentService.saveFiles(
                        patternLibrary.getId(),
                        Collections.singletonList(picSampleAttachmentDto),
                        AttachmentTypeConstant.PATTERN_LIBRARY_PIC
                );
            }
        }

        if (patternLibraryDTO.getStatus().equals(PatternLibraryStatusEnum.NO_REVIEWED.getCode())) {
            // 如果是提交 那么启动审批流
            flowableService.start(FlowableService.PATTERN_LIBRARY_APPROVAL,
                    FlowableService.PATTERN_LIBRARY_APPROVAL, patternLibrary.getId(),
                    "/pdm/api/saas/patternLibrary/approval",
                    "/pdm/api/saas/patternLibrary/approval",
                    "/pdm/api/saas/patternLibrary/approval",
                    "/productLibrary/model?patternLibraryId=" + patternLibrary.getId(),
                    BeanUtil.beanToMap(patternLibrary)
            );
        }
        return Boolean.TRUE;
    }

    /**
     * 比较目标大类和来源款式信息集合中的大类是否同属于上装或者下装
     *
     * @param targetProdCategory1st 目标大类
     * @param sourceStyleList       来源款式信息集合
     * @return true 代表是 false 代表否
     */
    private boolean isFlag(String targetProdCategory1st, List<Style> sourceStyleList) {
        // 默认是否
        boolean flag = false;
        // 获取上装的数据
        List<String> brandPuts = Arrays.asList(this.brandPuts.split(","));
        // 获取下装的数据
        List<String> brandBottoms = Arrays.asList(this.brandBottoms.split(","));
        if (brandPuts.contains(targetProdCategory1st)) {
            flag = sourceStyleList.stream().allMatch(item -> brandPuts.contains(item.getProdCategory1st()));
        } else if (brandBottoms.contains(targetProdCategory1st)) {
            flag = sourceStyleList.stream().allMatch(item -> brandBottoms.contains(item.getProdCategory1st()));
        }
        return flag;
    }

    /**
     * @param patternLibraryDTO 新增/编辑数据
     * @return
     */
    @Override
    public Boolean saveOrUpdateItemDetail(PatternLibraryDTO patternLibraryDTO) {
        if (ObjectUtil.isEmpty(patternLibraryDTO)) {
            throw new OtherException("数据不存在，请刷新后重试！");
        }
        PatternLibrary patternLibrary = getById(patternLibraryDTO.getId());
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException("数据不存在，请刷新后重试！");
        }
        if (!(patternLibrary.getStatus().equals(PatternLibraryStatusEnum.NO_SUBMIT.getCode())
                || patternLibrary.getStatus().equals(PatternLibraryStatusEnum.REJECTED.getCode()))) {
            throw new OtherException("当前状态不能编辑！");
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
                            .notIn(ObjectUtil.isNotEmpty(patternLibraryItemIdList), PatternLibraryItem::getId, patternLibraryItemIdList)
                            .eq(PatternLibraryItem::getType, patternLibraryDTO.getPatternLibraryItemType())
            );
            for (PatternLibraryItem patternLibraryItem : patternLibraryItemList) {
                patternLibraryItem.setPatternLibraryId(patternLibrary.getId());
            }
            patternLibraryItemService.saveOrUpdateBatch(patternLibraryItemList);
        } else {
            patternLibraryItemService.remove(
                    new LambdaQueryWrapper<PatternLibraryItem>()
                            .eq(PatternLibraryItem::getPatternLibraryId, patternLibrary.getId())
                            .eq(PatternLibraryItem::getType, patternLibraryDTO.getPatternLibraryItemType())
            );
        }
        return true;
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
            boolean flag = isFlag(patternLibraryDTOList.get(0).getProdCategory1st(), styleList);
            if (!flag) {
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
        if (ObjectUtil.isNotEmpty(patternLibraryVO.getFileId())) {
            AttachmentVo fileAttachmentVo = attachmentService.getAttachmentByFileId(patternLibraryVO.getFileId());
            patternLibraryVO.setFileAttachmentVo(fileAttachmentVo);
        }

        if (ObjectUtil.isNotEmpty(patternLibraryVO.getPicId())) {
            AttachmentVo picAttachmentVo = attachmentService.getAttachmentByFileId(patternLibraryVO.getPicId());
            patternLibraryVO.setPicAttachmentVo(picAttachmentVo);
        }

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
        if (patternLibrary.getStatus().equals(PatternLibraryStatusEnum.REVIEWED.getCode())) {
            throw new OtherException("已审核数据不能删除！");
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
    public Boolean updateAudits(AuditsDTO auditsDTO) {
        List<String> patternLibraryIdList = auditsDTO.getPatternLibraryIdList();
        if (ObjectUtil.isEmpty(patternLibraryIdList)) {
            throw new OtherException("请至少选择一条数据进行审核！");
        }
        // 查询到我的待办列表和这边选择的业务 ID 进行匹配，如果根据业务 ID 查询到了待办数据，
        // 那么查询到的待办数据进行批量审核，如果一条都没有，提示没有数据审核
        Map<String, Object> map = new HashMap<>();
        map.put("businessKeyList", patternLibraryIdList);
        map.put("pageNum", 1);
        map.put("pageSize", patternLibraryIdList.size());
        ApiResult<Map<String, Object>> apiResult = flowableFeignService.todoList(map);
        JSONObject jsonObject = JSONUtil.parseObj(apiResult.getData());
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        List<FlowTaskDto> flowTaskDtoList = jsonArray.toList(FlowTaskDto.class);
        if (ObjectUtil.isEmpty(flowTaskDtoList)) {
            throw new OtherException("勾选数据暂无审核！");
        }
        for (FlowTaskDto flowTaskDto : flowTaskDtoList) {
            if (auditsDTO.getType().equals(1)) {
                // 通过
                flowableFeignService.complete(
                        flowTaskDto.getTaskId(), flowTaskDto.getProcInsId(), auditsDTO.getComment()
                );
            } else {
                // 驳回
                flowableFeignService.reject(flowTaskDto.getTaskId(), auditsDTO.getComment());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateEnableFlag(PatternLibraryDTO patternLibraryDTO) {
        if (ObjectUtil.isEmpty(patternLibraryDTO.getId())) {
            throw new OtherException("请选择数据启用/禁用！");
        }
        PatternLibrary patternLibrary = getById(patternLibraryDTO.getId());
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        patternLibrary.setEnableFlag(patternLibraryDTO.getEnableFlag());
        updateById(patternLibrary);
        return true;
    }

    @Override
    public List<Style> listStyle(String search) {
        QueryWrapper<Style> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.del_flag", BaseGlobal.DEL_FLAG_NORMAL)
                .eq("s.enable_status", BaseGlobal.NO)
                .eq("s.status", "1")
                .like("s.design_no", search)
                .orderByDesc("s.create_date");
        // 获取已经生成版型库的数据
        return baseMapper.listStyle(queryWrapper);
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
        patternLibraryVO.setProdCategory1stName(style.getProdCategory1stName());
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
                        .isNotNull(StyleColor::getStyleColorPic)
                        .ne(StyleColor::getStyleColorPic, "")
                        .groupBy(StyleColor::getStyleColorPic)
        );

        if (ObjectUtil.isNotEmpty(styleColorList)) {
            // 初始化大货的图片 ID-URL 集合
            List<Map<String, String>> picFileIdList = new ArrayList<>(styleColorList.size());
            for (StyleColor styleColor : styleColorList) {
                // 当作临时变量存储一下图片来源 ID
                styleColor.setStyleNo(styleColor.getStyleColorPic());
            }
            stylePicUtils.setStylePic(styleColorList, "styleColorPic");
            for (StyleColor styleColor : styleColorList) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("picId", styleColor.getStyleNo());
                hashMap.put("url", styleColor.getStyleColorPic());
                picFileIdList.add(hashMap);
            }
            patternLibraryVO.setPicIdList(picFileIdList);
        } else {
            // 如果没有大货信息 那就直接取款式的图片
            patternLibraryVO.setPicId(style.getStylePic());
            stylePicUtils.setStylePic(Collections.singletonList(style), "stylePic");
            patternLibraryVO.setPicUrl(style.getStylePic());
        }
        return patternLibraryVO;
    }

    @Override
    public CategoriesTypeVO getCategoriesType() {
        CategoriesTypeVO categoriesTypeVO = new CategoriesTypeVO();
        categoriesTypeVO.setBrandPuts(brandPuts);
        categoriesTypeVO.setBrandBottoms(brandBottoms);
        return categoriesTypeVO;
    }


}
