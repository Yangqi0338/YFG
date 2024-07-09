package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.service.FlowableFeignService;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailOrderStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.entity.StyleSaleIntoResultType;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.patternlibrary.constants.GeneralConstant;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import com.base.sbc.module.patternlibrary.dto.*;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryBrand;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.listener.PatterLibraryListener;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryMapper;
import com.base.sbc.module.patternlibrary.service.PatternLibraryBrandService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import com.base.sbc.module.patternlibrary.vo.*;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.smp.dto.SaleProductIntoDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.task.vo.FlowTaskDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 版型库-主表 服务实现类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Service
public class PatternLibraryServiceImpl extends BaseServiceImpl<PatternLibraryMapper, PatternLibrary> implements PatternLibraryService {

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private OrderBookDetailService orderBookDetailService;

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    private PatternLibraryBrandService patternLibraryBrandService;

    @Autowired
    private PatternLibraryItemService patternLibraryItemService;

    @Autowired
    private PatternLibraryTemplateService patternLibraryTemplateService;

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

    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private OperaLogService operaLogService;

    @Autowired
    private FieldValService fieldValService;

    @Autowired
    private PlanningDimensionalityService planningDimensionalityService;

    @Value("${brand.puts:A01,A02,A03}")
    private String brandPuts;

    @Value("${brand.bottoms:A04}")
    private String brandBottoms;

    @Autowired
    @Lazy
    private SmpService smpService;

    @Override
    public PageInfo<PatternLibrary> listPages(PatternLibraryPageDTO patternLibraryPageDTO) {
        // 筛选条件
        QueryWrapper<PatternLibrary> queryWrapper = getPatternLibraryQueryWrapper(patternLibraryPageDTO);
        // 权限设置
        QueryWrapper<PatternLibraryBrand> brandQueryWrapper = new QueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(brandQueryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tplb.");
        String sqlSegment = brandQueryWrapper.getSqlSegment();
        if (ObjectUtil.isNotEmpty(sqlSegment)) {
            queryWrapper.exists("select id from t_pattern_library_brand tplb where tplb.pattern_library_id = tpl.id and del_flag='0' and " + sqlSegment);
        } else {
            queryWrapper.exists("select id from t_pattern_library_brand tplb where tplb.pattern_library_id = tpl.id and del_flag='0'");
        }
        // 列表分页 不是用作导出时分页
        if (patternLibraryPageDTO.getIsExcel().equals(0)) {
            PageHelper.startPage(patternLibraryPageDTO.getPageNum(), patternLibraryPageDTO.getPageSize());
        }
        // 得到版型库主表数据集合
        List<PatternLibrary> patternLibraryList = baseMapper.listPages(queryWrapper, patternLibraryPageDTO);
        // 设置子表数据
        if (ObjectUtil.isNotEmpty(patternLibraryList)) {
            // 拿到分页后的主表 ID
            List<String> patterLibraryIdList = patternLibraryList
                    .stream().map(PatternLibrary::getId).collect(Collectors.toList());
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
            // *************** 查询版型库模板数据 ***************
            List<String> templateCodeList = patternLibraryList.stream().map(PatternLibrary::getTemplateCode)
                    .distinct().collect(Collectors.toList());
            // 拿到分页后的主表 ID
            // 根据主表 ID 查询子表数据
            List<PatternLibraryTemplate> patternLibraryTemplateList = patternLibraryTemplateService.list(
                    new LambdaQueryWrapper<PatternLibraryTemplate>()
                            .in(PatternLibraryTemplate::getCode, templateCodeList)
            );
            // 子表根据主表 ID 分组转成 map
            Map<String, PatternLibraryTemplate> patternLibraryTemplateMap = Collections.emptyMap();
            if (ObjectUtil.isNotEmpty(patternLibraryTemplateList)) {
                patternLibraryTemplateMap = patternLibraryTemplateList
                        .stream()
                        .collect(Collectors.toMap(PatternLibraryTemplate::getCode, item -> item));
            }

            List<String> parentIds = patternLibraryList.stream().map(PatternLibrary::getParentIds).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            Set<String> topParentId = new HashSet<>(parentIds.size());
            Map<String, PatternLibrary> patternLibraryMap = new HashMap<>();
            if (ObjectUtil.isNotEmpty(parentIds)) {
                for (String parentId : parentIds) {
                    topParentId.add(parentId.split(",")[0].replace("\"", ""));
                }

                List<PatternLibrary> topParentPatternLibrary = listByIds(topParentId);
                if (ObjectUtil.isNotEmpty(topParentPatternLibrary)) {
                    patternLibraryMap = topParentPatternLibrary.stream().collect(Collectors.toMap(PatternLibrary::getId, item -> item));
                }
            }


            for (PatternLibrary patternLibrary : patternLibraryList) {
                setPatternLibrary(patternLibraryPageDTO.getIsExcel(), patternLibrary, patternLibraryBrandMap, patternLibraryItemMap, patternLibraryTemplateMap);

                if (ObjectUtil.isNotEmpty(patternLibrary.getParentIds())) {
                    String replace = patternLibrary.getParentIds().split(",")[0].replace("\"", "");
                    PatternLibrary parent = patternLibraryMap.get(replace);
                    patternLibrary.setTopParentCode(parent.getCode());
                }
            }
            minioUtils.setObjectUrlToList(patternLibraryList, "picUrl");
        }
        return new PageInfo<>(patternLibraryList);
    }


    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateDetails(PatternLibraryDTO patternLibraryDTO) {
        if (ObjectUtil.isEmpty(patternLibraryDTO)) {
            throw new OtherException(ResultConstant.OPERATION_DATA_NOT_EMPTY);
        }
        // 获取旧数据
        PatternLibrary oldPatternLibrary = new PatternLibrary();
        String patternLibraryId = patternLibraryDTO.getId();
        if (ObjectUtil.isNotEmpty(patternLibraryId)) {
            oldPatternLibrary = getById(patternLibraryId);
            if (!(oldPatternLibrary.getStatus().equals(PatternLibraryStatusEnum.NO_SUBMIT.getCode())
                    || oldPatternLibrary.getStatus().equals(PatternLibraryStatusEnum.REJECTED.getCode())
                    || oldPatternLibrary.getStatus().equals(PatternLibraryStatusEnum.NO_PADDED.getCode()))) {
                throw new OtherException(ResultConstant.CURRENT_STATE_DATA_NOT_DO_IT);
            }

            // 查询旧数据 用作日志匹配
            oldPatternLibrary = getDetail(patternLibraryId);
        }
        PatternLibrary patternLibrary = new PatternLibrary();
        BeanUtil.copyProperties(patternLibraryDTO, patternLibrary);
        // 判断设计款的大类和选择的大类是否都属于上装或者下装
        {
            Style style = styleService.getById(patternLibraryDTO.getStyleId());
            if (ObjectUtil.isNotEmpty(style)) {
                if (!isBelong(patternLibraryDTO.getProdCategory1st(), Collections.singletonList(style))) {
                    throw new OtherException(ResultConstant.CATEGORY_PUT_BOTTOMS_MISMATCH);
                }
            }
        }

        // 新增/修改主表数据
        patternLibrary.setPicUrl(CommonUtils.removeQuery(patternLibrary.getPicUrl()));
        saveOrUpdate(patternLibrary);
        if (ObjectUtil.isEmpty(patternLibraryDTO.getId()) && ObjectUtil.isNotEmpty(patternLibraryDTO.getParentId())) {
            // 如果是新增
            newEverGreenTreeNode(patternLibrary.getId());
        } else {
            // 修改
            if (ObjectUtil.isNotEmpty(patternLibraryDTO.getEverGreenCode())) {
                // 从无到有  从有到有
                newEverGreenTreeNode(patternLibrary.getId());
            } else if (ObjectUtil.isNotEmpty(oldPatternLibrary.getEverGreenCode()) && ObjectUtil.isEmpty(patternLibraryDTO.getEverGreenCode())) {
                // 从有到无
                removeEverGreenTreeNode(patternLibrary.getId());
            }
        }
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
                        patternLibrary.setPicUrl(CommonUtils.removeQuery(attachmentVo.getUrl()));
                        updateById(patternLibrary);
                    } catch (IOException e) {
                        throw new RuntimeException(ResultConstant.FILE_SAVE_FAIL_REFRESH_TRY_AGAIN);
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

        // 查询新数据 用作日志匹配
        PatternLibrary newPatternLibrary = getDetail(patternLibrary.getId());
        // 保存日志
        this.saveOperaLog("新增/编辑", GeneralConstant.LOG_NAME, patternLibrary.getId(), patternLibrary.getCode(), patternLibrary.getCode(), newPatternLibrary, oldPatternLibrary);

        return Boolean.TRUE;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateItemDetail(PatternLibraryDTO patternLibraryDTO) {
        if (ObjectUtil.isEmpty(patternLibraryDTO)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        PatternLibrary patternLibrary = getById(patternLibraryDTO.getId());
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        if (!(patternLibrary.getStatus().equals(PatternLibraryStatusEnum.NO_SUBMIT.getCode())
                || patternLibrary.getStatus().equals(PatternLibraryStatusEnum.REJECTED.getCode()))) {
            throw new OtherException(ResultConstant.CURRENT_STATE_DATA_NOT_DO_IT);
        }
        // 查询旧数据 用作日志匹配
        PatternLibrary oldPatternLibrary = getDetail(patternLibraryDTO.getId());
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
        // 查询新数据 用作日志匹配
        PatternLibrary newPatternLibrary = getDetail(patternLibraryDTO.getId());
        // 保存日志
        this.saveOperaLog("新增/编辑", GeneralConstant.LOG_NAME, patternLibrary.getId(), patternLibrary.getCode(), patternLibrary.getCode(), newPatternLibrary, oldPatternLibrary);
        return true;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDetails(List<PatternLibraryDTO> patternLibraryDTOList) {
        if (ObjectUtil.isEmpty(patternLibraryDTOList)) {
            throw new OtherException(ResultConstant.OPERATION_DATA_NOT_EMPTY);
        }
        // 查询旧数据 用作日志匹配
        List<PatternLibrary> oldPatternLibraryList
                = listDetail(patternLibraryDTOList.stream().map(PatternLibraryDTO::getId).collect(Collectors.toList()));
        if (ObjectUtil.isEmpty(oldPatternLibraryList) || oldPatternLibraryList.size() != patternLibraryDTOList.size()) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        if (oldPatternLibraryList
                .stream()
                .anyMatch(item -> !item.getStatus().equals(PatternLibraryStatusEnum.NO_PADDED.getCode()))) {
            throw new OtherException(ResultConstant.CURRENT_STATE_DATA_NOT_DO_IT);
        }
        // 批量编辑
        // 判断设计款的大类和选择的大类是否都属于上装或者下装
        {
            List<String> styleIdList = patternLibraryDTOList.
                    stream().map(PatternLibraryDTO::getStyleId).collect(Collectors.toList());
            List<Style> styleList = styleService.listByIds(styleIdList);
            if (!isBelong(patternLibraryDTOList.get(0).getProdCategory1st(), styleList)) {
                throw new OtherException(ResultConstant.CATEGORY_PUT_BOTTOMS_MISMATCH);
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

        if (patternLibraryDTOList.get(0).getStatus().equals(PatternLibraryStatusEnum.NO_REVIEWED.getCode())) {
            for (PatternLibrary patternLibrary : patternLibraryList) {
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
        }
        // 查询新数据 用作日志匹配
        List<PatternLibrary> newPatternLibraryList = listDetail(patternLibraryDTOList.stream().map(PatternLibraryDTO::getId).collect(Collectors.toList()));
        // 保存日志
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setName(GeneralConstant.LOG_NAME);
        operaLogEntity.setType("批量编辑");

        operaLogEntity.setDocumentCodeField("code");
        operaLogEntity.setDocumentNameField("code");
        this.updateBatchOperaLog(newPatternLibraryList, oldPatternLibraryList, operaLogEntity);
        return true;
    }

    @Override
    public PatternLibrary getDetail(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        // 初始化返回数据
        // 根据版型库主表 ID 查询版型库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }

        minioUtils.setObjectUrlToObject(patternLibrary, "picUrl");

        if (ObjectUtil.isNotEmpty(patternLibrary.getFileId())) {
            AttachmentVo fileAttachmentVo = attachmentService.getAttachmentByFileId(patternLibrary.getFileId());
            patternLibrary.setFileAttachmentVo(fileAttachmentVo);
        }
        // if (ObjectUtil.isNotEmpty(patternLibrary.getPicId())) {
        //     AttachmentVo picAttachmentVo = attachmentService.getAttachmentByFileId(patternLibrary.getPicId());
        //     patternLibrary.setPicAttachmentVo(picAttachmentVo);
        // }

        // 查询品类信息
        List<PatternLibraryBrand> patternLibraryBrandList = patternLibraryBrandService.list(
                new LambdaQueryWrapper<PatternLibraryBrand>()
                        .eq(PatternLibraryBrand::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibrary.setPatternLibraryBrandList(patternLibraryBrandList);
        // 查询版型库子表信息
        List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .eq(PatternLibraryItem::getPatternLibraryId, patternLibraryId)
                        .eq(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        patternLibrary.setPatternLibraryItemList(patternLibraryItemList);
        // 查询版型库模板信息
        if (ObjectUtil.isNotEmpty(patternLibrary) && ObjectUtil.isNotEmpty(patternLibrary.getTemplateCode())) {
            PatternLibraryTemplate patternLibraryTemplate = patternLibraryTemplateService.getOne(
                    new LambdaQueryWrapper<PatternLibraryTemplate>()
                            .eq(PatternLibraryTemplate::getCode, patternLibrary.getTemplateCode())
            );
            patternLibrary.setPatternLibraryTemplate(patternLibraryTemplate);
        }
        // 查询常青原版树
        EverGreenVO everGreenVO = listEverGreenTree(patternLibraryId);
        patternLibrary.setEverGreenVO(everGreenVO);
        return patternLibrary;
    }

    @Override
    public List<UseStyleVO> listUseStyle(UseStyleDTO useStyleDTO) {
        PatternLibrary patternLibrary = getById(useStyleDTO.getPatternLibraryId());
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException("版型库数据不存在，请刷新后重试！");
        }
        // 列表分页
        QueryWrapper<UseStyleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ts.registering_id", useStyleDTO.getPatternLibraryId());
        queryWrapper.orderByDesc("ts.create_date");
        List<UseStyleVO> useStyleVOList = baseMapper.listUseStyle(queryWrapper);

        /* ----------------------------获取多数据源的销售数据---------------------------- */

        SaleProductIntoDto saleProductIntoDto = new SaleProductIntoDto();
        saleProductIntoDto.setBulkStyleNoList(useStyleVOList.stream().map(UseStyleVO::getStyleNo).collect(Collectors.toList()));
        // 只要销售
        saleProductIntoDto.setResultTypeList(Arrays.stream(StyleSaleIntoResultType.values()).filter(it -> it.getCode().contains("sale")).collect(Collectors.toList()));
        // 根据款号和年限分组
        smpService.querySaleIntoPage(saleProductIntoDto).stream().collect(Collectors.groupingBy(it -> it.getBulkStyleNo() + COMMA + it.getYear()))
                .forEach((key, sameKeyList) -> {
                    String[] keyArray = key.split(COMMA);
                    int saleSum = sameKeyList.stream().flatMapToInt(it -> it.getSizeMap().values().stream().mapToInt(Double::intValue)).sum();
                    useStyleVOList.stream().filter(it -> it.getStyleNo().equals(ArrayUtil.get(keyArray, 0))).findFirst().ifPresent(useStyleVO -> {
                        useStyleVO.setYearSaleNum(ArrayUtil.get(keyArray, 1), saleSum);
                        useStyleVO.setHistorySaleNum(useStyleVO.getHistorySaleNum() + saleSum);
                    });
                });
        return useStyleVOList;
    }

    @Override
    public List<UseStyleVO> listUseStyleByStyle(UseStyleDTO useStyleDTO) {
        Style style = styleService.getById(useStyleDTO.getStyleId());
        if (ObjectUtil.isEmpty(style)) {
            throw new OtherException("款式数据不存在，请刷新后重试！");
        }
        // 列表分页
        QueryWrapper<UseStyleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ts.serial_style_id", useStyleDTO.getStyleId());
        queryWrapper.orderByDesc("ts.create_date");
        List<UseStyleVO> useStyleVOList = baseMapper.listUseStyleByStyle(queryWrapper);

        /* ----------------------------获取多数据源的销售数据---------------------------- */

        SaleProductIntoDto saleProductIntoDto = new SaleProductIntoDto();
        saleProductIntoDto.setBulkStyleNoList(useStyleVOList.stream().map(UseStyleVO::getStyleNo).collect(Collectors.toList()));
        // 只要销售
        saleProductIntoDto.setResultTypeList(Arrays.stream(StyleSaleIntoResultType.values()).filter(it -> it.getCode().contains("sale")).collect(Collectors.toList()));
        // 根据款号和年限分组
        smpService.querySaleIntoPage(saleProductIntoDto).stream().collect(Collectors.groupingBy(it -> it.getBulkStyleNo() + COMMA + it.getYear()))
                .forEach((key, sameKeyList) -> {
                    String[] keyArray = key.split(COMMA);
                    int saleSum = sameKeyList.stream().flatMapToInt(it -> it.getSizeMap().values().stream().mapToInt(Double::intValue)).sum();
                    useStyleVOList.stream().filter(it -> it.getStyleNo().equals(ArrayUtil.get(keyArray, 0))).findFirst().ifPresent(useStyleVO -> {
                        useStyleVO.setYearSaleNum(ArrayUtil.get(keyArray, 1), saleSum);
                        useStyleVO.setHistorySaleNum(useStyleVO.getHistorySaleNum() + saleSum);
                    });
                });
        return useStyleVOList;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDetail(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        // 查询新数据 用作日志匹配
        PatternLibrary oldPatternLibrary = getDetail(patternLibraryId);
        // 根据版型库主表 ID 查询版型库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        if (patternLibrary.getStatus().equals(PatternLibraryStatusEnum.REVIEWED.getCode())) {
            throw new OtherException(ResultConstant.CURRENT_STATE_DATA_NOT_DO_IT);
        }
        // 判断是否有款式关联了此版型 如果关联了 那么不允许删除 并进行提示
        List<Style> styleList = styleService.list(
                new LambdaQueryWrapper<Style>()
                        .eq(Style::getRegisteringId, patternLibrary.getId())
                        .select(Style::getDesignNo)
        );
        if (ObjectUtil.isNotEmpty(styleList)) {
            String styleNames = CollUtil.join(styleList.stream().map(Style::getDesignNo).collect(Collectors.toList()), ",");
            throw new OtherException(ResultConstant.PATTERN_LIBRARY_HAS_BEEN_STYLE_RELEVANCY + "被关联的款号为：「" +
                    styleNames + "」");
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
        this.saveOperaLog("删除", GeneralConstant.LOG_NAME, patternLibraryId, patternLibrary.getCode(), patternLibrary.getCode(), new PatternLibrary(), oldPatternLibrary);
        return true;
    }

    @Override
    @DuplicationCheck
    public Boolean updateAudits(AuditsDTO auditsDTO) {
        List<String> patternLibraryIdList = auditsDTO.getPatternLibraryIdList();
        if (ObjectUtil.isEmpty(patternLibraryIdList)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }

        List<PatternLibrary> patternLibraryList = listByIds(patternLibraryIdList);
        if (ObjectUtil.isEmpty(patternLibraryList) || patternLibraryList.size() != patternLibraryIdList.size()) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
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
            throw new OtherException(ResultConstant.SELECT_DATA_NOT_ACTION);
        }
        // 保存日志
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
        // 查询新数据 用作日志匹配
        List<PatternLibrary> newPatternLibraryList = BeanUtil.copyToList(patternLibraryList, PatternLibrary.class);
        newPatternLibraryList.forEach(item -> item.setStatus(auditsDTO.getType().equals(1) ? 4 : 5));
        // 保存日志
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setName(GeneralConstant.LOG_NAME);
        operaLogEntity.setType("批量审核");

        operaLogEntity.setDocumentCodeField("code");
        operaLogEntity.setDocumentNameField("code");
        this.updateBatchOperaLog(newPatternLibraryList, patternLibraryList, operaLogEntity);
        return Boolean.TRUE;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEnableFlag(PatternLibraryDTO patternLibraryDTO) {
        if (ObjectUtil.isEmpty(patternLibraryDTO.getId())) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        PatternLibrary patternLibrary = getById(patternLibraryDTO.getId());
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        // 查询旧数据 用作日志匹配
        PatternLibrary oldPatternLibrary = getDetail(patternLibraryDTO.getId());
        patternLibrary.setEnableFlag(patternLibraryDTO.getEnableFlag());
        updateById(patternLibrary);
        // 查询新数据 用作日志匹配
        PatternLibrary newPatternLibrary = getDetail(patternLibraryDTO.getId());
        // 保存日志
        this.saveOperaLog("启用/禁用", GeneralConstant.LOG_NAME, patternLibrary.getId(), patternLibrary.getCode(), patternLibrary.getCode(), newPatternLibrary, oldPatternLibrary);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean excelImport(MultipartFile file) {
        if (ObjectUtil.isEmpty(file)) {
            throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
        }
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        PatterLibraryListener patterLibraryListener = new PatterLibraryListener();
        try {
            EasyExcel.read(file.getInputStream(), ExcelImportDTO.class, patterLibraryListener).sheet().doRead();
        } catch (IOException e) {
            throw new OtherException(e.getMessage());
        }
        // 没有拿到数据也返回异常
        List<ExcelImportDTO> cachedDataList = patterLibraryListener.getCachedDataList();
        if (ObjectUtil.isEmpty(cachedDataList)) {
            throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
        }
        // 任意一个字段值为空直接抛出异常
        if (cachedDataList.stream().anyMatch(item ->
                ObjectUtil.isEmpty(item.getCode())
                        || ObjectUtil.isEmpty(item.getBrandName())
                        || ObjectUtil.isEmpty(item.getEnableFlag()))) {
            throw new OtherException(ResultConstant.IMPORT_FIELD_VALUE_NOT_EMPTY);

        }
        // 拿到所有的设计编号
        List<String> styleNoList = cachedDataList.stream().map(ExcelImportDTO::getCode).distinct().collect(Collectors.toList());
        if (cachedDataList.size() != styleNoList.size()) {
            throw new OtherException(ResultConstant.LAYOUT_CODES_CANNOT_BE_REPEATED);
        }
        // 根据设计编号查询款式信息
        List<Style> styleList = listStyle(styleNoList);
        if (ObjectUtil.isEmpty(styleList)) {
            throw new OtherException("款式编码「" + CollUtil.join(styleNoList, ",") + "」不存在！");
        } else if (styleNoList.size() != styleList.size()) {
            List<String> haveStyleNoList = styleList.stream().map(Style::getDesignNo).collect(Collectors.toList());
            styleNoList.removeAll(haveStyleNoList);
            throw new OtherException("款式编码「" + CollUtil.join(styleNoList, ",") + "」不存在！");
        } else {
            List<String> haveStyleIdList = styleList.stream().map(Style::getId).collect(Collectors.toList());
            List<PatternLibrary> patternLibraryList = list(
                    new LambdaQueryWrapper<PatternLibrary>()
                            .eq(PatternLibrary::getDelFlag, BaseGlobal.NO)
                            .in(PatternLibrary::getStyleId, haveStyleIdList)
            );
            if (ObjectUtil.isNotEmpty(patternLibraryList)) {
                List<String> patterLibraryCodeList = patternLibraryList.stream().map(PatternLibrary::getCode).collect(Collectors.toList());
                throw new OtherException("版型库编码「" + CollUtil.join(patterLibraryCodeList, ",") + "」已存在！");
            }
        }
        Map<String, Style> styleMap = styleList.stream().collect(Collectors.toMap(Style::getDesignNo, item -> item));
        // 拿到品牌字典数据
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMapTurnOver("C8_Brand");
        Map<String, String> brandMap = dictInfoToMap.get("C8_Brand");

        // 初始化需要新增的数据
        List<PatternLibrary> patternLibraryList = new ArrayList<>();
        // 组装数据
        for (ExcelImportDTO excelImportDTO : cachedDataList) {
            PatternLibrary patternLibrary = new PatternLibrary();
            Style style = styleMap.get(excelImportDTO.getCode());
            setValue(patternLibrary, style);
            // 默认状态是待补齐
            patternLibrary.setStatus(PatternLibraryStatusEnum.NO_PADDED.getCode());

            String[] brandNameList = excelImportDTO.getBrandName().split("/");
            List<PatternLibraryBrand> patternLibraryBrandList = new ArrayList<>();
            for (String name : brandNameList) {
                String code = brandMap.get(name);
                if (ObjectUtil.isEmpty(code)) {
                    throw new OtherException("「" + name + "」" + ResultConstant.BRAND_DATA_NOT_EXIST);
                }
                PatternLibraryBrand patternLibraryBrand = new PatternLibraryBrand();
                patternLibraryBrand.setBrand(code);
                patternLibraryBrand.setBrandName(name);
                patternLibraryBrandList.add(patternLibraryBrand);
            }

            patternLibrary.setPatternLibraryBrandList(patternLibraryBrandList);
            if (!GeneralConstant.ENABLE.equals(excelImportDTO.getEnableFlag()) && !GeneralConstant.DISABLE.equals(excelImportDTO.getEnableFlag())) {
                // 默认启用
                patternLibrary.setEnableFlag(1);
            } else {
                patternLibrary.setEnableFlag(GeneralConstant.ENABLE.equals(excelImportDTO.getEnableFlag()) ? 1 : 0);

            }
            patternLibraryList.add(patternLibrary);
        }
        saveBatch(patternLibraryList);
        List<PatternLibraryBrand> brandList = new ArrayList<>();
        for (PatternLibrary patternLibrary : patternLibraryList) {
            List<PatternLibraryBrand> patternLibraryBrandList = patternLibrary.getPatternLibraryBrandList();
            for (PatternLibraryBrand patternLibraryBrand : patternLibraryBrandList) {
                patternLibraryBrand.setPatternLibraryId(patternLibrary.getId());
            }
            brandList.addAll(patternLibraryBrandList);
        }
        patternLibraryBrandService.saveBatch(brandList);
        // 查询新数据 用作日志匹配
        List<PatternLibrary> newPatternLibraryList = listDetail(patternLibraryList.stream().map(PatternLibrary::getId).collect(Collectors.toList()));
        // 保存日志
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setName(GeneralConstant.LOG_NAME);
        operaLogEntity.setType("导入");

        operaLogEntity.setDocumentCodeField("code");
        operaLogEntity.setDocumentNameField("code");
        this.saveBatchOperaLog(newPatternLibraryList, operaLogEntity);
        return true;
    }

    @Override
    public Boolean excelExport(PatternLibraryPageDTO patternLibraryPageDTO, HttpServletResponse response) {
        // 设置为导出的类型
        patternLibraryPageDTO.setIsExcel(1);
        PageInfo<PatternLibrary> patternLibraryPageInfo = listPages(patternLibraryPageDTO);
        List<PatternLibrary> list = patternLibraryPageInfo.getList();
        if (ObjectUtil.isEmpty(list)) {
            throw new OtherException(ResultConstant.NO_DATA_EXPORT);
        }
        // 转成导出的数据
        try {
            List<ExcelExportVO> excelExportVOList = new ArrayList<>(list.size());
            minioUtils.setObjectUrlToList(list, "picUrl");
            for (PatternLibrary patternLibrary : list) {
                ExcelExportVO excelExportVO = new ExcelExportVO();
                BeanUtil.copyProperties(patternLibrary, excelExportVO);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                excelExportVO.setCreateDate(simpleDateFormat.format(patternLibrary.getCreateDate()));
                excelExportVO.setUpdateDate(simpleDateFormat.format(patternLibrary.getUpdateDate()));
                excelExportVO.setStatus(PatternLibraryStatusEnum.getValueByCode(patternLibrary.getStatus()));
                excelExportVO.setPicUrl(
                        ObjectUtil.isNotEmpty(patternLibrary.getPicUrl()) ? new URL(patternLibrary.getPicUrl()) : null
                );
                excelExportVO.setEnableFlag(
                        patternLibrary.getEnableFlag().equals(0)
                                ? "禁用" : patternLibrary.getEnableFlag().equals(1) ? "启用" : "未知");
                excelExportVOList.add(excelExportVO);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("版型库数据", "utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelExportVO.class).sheet("版型库数据").doWrite(excelExportVOList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<Style> listStyle(String search) {
        QueryWrapper<Style> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.del_flag", BaseGlobal.DEL_FLAG_NORMAL)
                .eq("s.enable_status", BaseGlobal.NO)
                .ne("s.design_no", "")
                .isNotNull("s.design_no")
                .like(ObjectUtil.isNotEmpty(search), "s.design_no", search)
                .orderByDesc("s.create_date");
        // 获取还没有生成版型库的数据
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "s.");
        List<Style> styleList = baseMapper.listStyleNoCode(queryWrapper);
        if (ObjectUtil.isEmpty(styleList)) {
            long count = count(
                    new LambdaQueryWrapper<PatternLibrary>()
                            .eq(PatternLibrary::getCode, search)
            );
            if (count > 0) {
                throw new OtherException(ResultConstant.LAYOUT_LIBRARY_ENCODING_ALREADY_EXISTS);
            } else {
                throw new OtherException(ResultConstant.THERE_IS_NO_CORRESPONDING_DESIGN_VERSION);
            }
        }
        return styleList;
    }

    @Override
    public List<Style> listStyle(List<String> styleNoList) {
        QueryWrapper<Style> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.del_flag", BaseGlobal.DEL_FLAG_NORMAL)
                .eq("s.enable_status", BaseGlobal.NO)
                .ne("s.design_no", "")
                .isNotNull("s.design_no")
                .in(ObjectUtil.isNotEmpty(styleNoList), "s.design_no", styleNoList)
                .orderByDesc("s.create_date");
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "s.");
        return baseMapper.listStyle(queryWrapper);
    }

    @Override
    public PatternLibrary getInfoByDesignNo(String designNo) {
        // 初始化返回的封装数据
        PatternLibrary patternLibrary = new PatternLibrary();
        if (ObjectUtil.isEmpty(designNo)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        Style style = styleService.getOne(
                new LambdaQueryWrapper<Style>()
                        .eq(Style::getDesignNo, designNo)
                        .eq(Style::getEnableStatus, "0")
                        .eq(Style::getDelFlag, "0")
        );
        if (ObjectUtil.isEmpty(style)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        setValue(patternLibrary, style);
        // 生成品牌
        PatternLibraryBrand patternLibraryBrand = new PatternLibraryBrand();
        patternLibraryBrand.setBrand(style.getBrand());
        patternLibraryBrand.setBrandName(style.getBrandName());
        patternLibrary.setPatternLibraryBrandList(CollUtil.newArrayList(patternLibraryBrand));
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
            // 判断已经投产的大货款号 逻辑是 当前大货款是否存在任意一个状态为已下单的订货本详情 orderStatus = 4
            List<String> styleColorIdList = styleColorList.stream().map(StyleColor::getId).collect(Collectors.toList());
            List<OrderBookDetail> orderBookDetailList = orderBookDetailService.list(
                    new LambdaQueryWrapper<OrderBookDetail>()
                            .in(OrderBookDetail::getStyleColorId, styleColorIdList)
                            .eq(OrderBookDetail::getStatus, OrderBookDetailOrderStatusEnum.ORDER)
            );
            if (ObjectUtil.isNotEmpty(orderBookDetailList)) {
                List<String> styleColorIds = orderBookDetailList.stream().map(OrderBookDetail::getStyleColorId).collect(Collectors.toList());
                // 说明查到了投产的大货款号
                List<String> styleNoList = styleColorList.stream().filter(item -> styleColorIds.contains(item.getId())).map(StyleColor::getStyleNo).collect(Collectors.toList());
                patternLibrary.setPlaceOrderStyleNoList(styleNoList);
            }

            patternLibrary.setAllStyleNoList(styleColorList.stream().map(StyleColor::getStyleNo).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList()));
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
            patternLibrary.setPicIdList(picFileIdList);
        } else {
            // 如果没有大货信息 那就直接取款式的图片
            patternLibrary.setStylePicId(style.getStylePic());
            stylePicUtils.setStylePic(Collections.singletonList(style), "stylePic");
            patternLibrary.setStylePicUrl(style.getStylePic());
        }
        return patternLibrary;
    }

    @Override
    public List<FieldVal> listStyleToPatternLibrarySilhouette() {
        List<FieldVal> fieldValList = fieldValService.list(new LambdaQueryWrapper<FieldVal>()
                .eq(FieldVal::getFieldExplain, "廓形及代码")
                .eq(FieldVal::getDataGroup, "SAMPLE_DESIGN_TECHNOLOGY")
                .isNotNull(FieldVal::getVal)
                .ne(FieldVal::getVal, "")
                .groupBy(FieldVal::getValName)
        );
        return fieldValList;
    }

    @Override
    public List<Style> listStyleToPatternLibraryProdCategory() {
        List<Style> styleList = styleService.list(new LambdaQueryWrapper<Style>()
                .select(Style::getProdCategoryName, Style::getProdCategory)
                .in(Style::getStatus, 1, 2)
                .eq(Style::getEnableStatus, BaseGlobal.NO)
                .isNotNull(Style::getProdCategory)
                .ne(Style::getProdCategory, "")
                .groupBy(Style::getProdCategory)
        );
        return styleList;
    }

    @Override
    public PageInfo<PatternLibrary> listStyleToPatternLibrary(PatternLibraryDTO patternLibraryDTO) {
        QueryWrapper<Style> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.del_flag", BaseGlobal.DEL_FLAG_NORMAL)
                .eq("s.enable_status", BaseGlobal.NO)
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory1st()), "s.prod_category1st", patternLibraryDTO.getProdCategory1st())
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory()), "s.prod_category", patternLibraryDTO.getProdCategory())
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory2nd()), "s.prod_category2nd", patternLibraryDTO.getProdCategory2nd())
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory3rd()), "s.prod_category3rd", patternLibraryDTO.getProdCategory3rd())
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory3rd()), "s.prod_category3rd", patternLibraryDTO.getProdCategory3rd())
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getPlanningSeasonId()), "s.planning_season_id", patternLibraryDTO.getPlanningSeasonId())
                // .in("s.status", "1", "2")
                .like(ObjectUtil.isNotEmpty(patternLibraryDTO.getDesignNo()), "s.design_no", patternLibraryDTO.getDesignNo())
                .like(ObjectUtil.isNotEmpty(patternLibraryDTO.getStyleNo()), "tsc.style_no", patternLibraryDTO.getStyleNo())
                .orderByDesc("s.create_date")
                .groupBy("s.id");
        // 获取还没有生成版型库的数据
        PageHelper.startPage(patternLibraryDTO.getPageNum(), patternLibraryDTO.getPageSize());

        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "s.");

        List<Style> styleList = baseMapper.listStyleToPatternLibrary(queryWrapper, patternLibraryDTO);
        // 查询总的使用记录数
        QueryWrapper<Style> wrapper = new QueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(wrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "ts.");
        String allCount = baseMapper.queryAllUseStyle(wrapper);

        // 初始化返回的封装数据
        List<PatternLibrary> patternLibraryList = new ArrayList<>(styleList.size());
        if (ObjectUtil.isNotEmpty(styleList)) {
            for (Style style : styleList) {
                String prodCategory1stName = style.getProdCategory1stName();
                String prodCategoryName = style.getProdCategoryName();
                String prodCategory2ndName = style.getProdCategory2ndName();
                String prodCategory3rdName = style.getProdCategory3rdName();
                PatternLibrary patternLibrary = new PatternLibrary();
                patternLibrary.setId(style.getRegisteringId());
                patternLibrary.setCode(style.getRegisteringNo());
                patternLibrary.setDesignNo(style.getDesignNo());
                patternLibrary.setStyleId(style.getId());
                PatternLibraryBrand patternLibraryBrand = new PatternLibraryBrand();
                patternLibraryBrand.setBrandName(style.getBrandName());
                patternLibrary.setPatternLibraryBrandList(CollUtil.newArrayList(patternLibraryBrand));
                patternLibrary.setPicUrl(style.getStylePic());
                patternLibrary.setProdCategory1stName(prodCategory1stName);
                patternLibrary.setProdCategoryName(prodCategoryName);
                patternLibrary.setProdCategory2ndName(prodCategory2ndName);
                patternLibrary.setProdCategory3rdName(prodCategory3rdName);
                patternLibrary.setUseStyleNum(style.getUseStyleNum());
                BigDecimal useStyleNum = new BigDecimal(style.getUseStyleNum());
                BigDecimal count = new BigDecimal(allCount);
                patternLibrary.setPatternLibraryUtilization(BigDecimalUtil.dividePercentage(useStyleNum, count, 2, RoundingMode.CEILING).toString());
                patternLibrary.setSilhouetteName(style.getSilhouetteName());
                patternLibrary.setPatternLibraryItemParts(style.getPatternParts());
                patternLibrary.setPlanningSeasonName(style.getPlanningSeasonName());
                patternLibrary.setAllProdCategoryNames(
                        (ObjectUtil.isNotEmpty(prodCategory1stName) ? prodCategory1stName : "无") + "/"
                                + (ObjectUtil.isNotEmpty(prodCategoryName) ? prodCategoryName : "无") + "/"
                                + (ObjectUtil.isNotEmpty(prodCategory2ndName) ? prodCategory2ndName : "无") + "/"
                                + (ObjectUtil.isNotEmpty(prodCategory3rdName) ? prodCategory3rdName : "无")
                );
                patternLibraryList.add(patternLibrary);
            }
            stylePicUtils.setStylePic(patternLibraryList, "picUrl");
        }
        PageInfo<Style> stylePageInfo = new PageInfo<>(styleList);
        PageInfo<PatternLibrary> patternLibraryPageInfo = new PageInfo<>();
        BeanUtil.copyProperties(stylePageInfo, patternLibraryPageInfo);
        patternLibraryPageInfo.setList(patternLibraryList);
        return patternLibraryPageInfo;
    }

    @Override
    public CategoriesTypeVO getCategoriesType() {
        CategoriesTypeVO categoriesTypeVO = new CategoriesTypeVO();
        categoriesTypeVO.setBrandPuts(brandPuts);
        categoriesTypeVO.setBrandBottoms(brandBottoms);
        return categoriesTypeVO;
    }

    /**
     * @param type 筛选条件类型（1-版型编码 2-品牌 3-所属品类 4-廓形 5-所属版型库 6-涉及部件 7-审核状态 8-是否启用）
     * @return
     */
    @Override
    public List<FilterCriteriaVO> getAllFilterCriteria(Integer type) {
        if (ObjectUtil.isEmpty(type) || type < 1 || type > 8) {
            throw new OtherException(ResultConstant.FILTER_TYPE_DOES_NOT_EXIST);
        }
        QueryWrapper<PatternLibrary> queryWrapper = new QueryWrapper<>();

        // 权限设置
        QueryWrapper<PatternLibraryBrand> brandQueryWrapper = new QueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(brandQueryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tplb.");
        String sqlSegment = brandQueryWrapper.getSqlSegment();
        if (ObjectUtil.isNotEmpty(sqlSegment)) {
            queryWrapper.exists("select id from t_pattern_library_brand tplb where tplb.pattern_library_id = tpl.id and del_flag='0' and " + sqlSegment);
        } else {
            queryWrapper.exists("select id from t_pattern_library_brand tplb where tplb.pattern_library_id = tpl.id and del_flag='0'");
        }
        // 得到版型库主表数据集合
        return baseMapper.getAllFilterCriteria(queryWrapper, type);
    }

    @Override
    public EverGreenVO listEverGreenTree(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        // 父级 ID
        String parentIds = patternLibrary.getParentIds();
        PatternLibrary topPatternLibrary = patternLibrary;
        if (ObjectUtil.isNotEmpty(parentIds)) {
            String topParentId = parentIds.split(",")[0].replace("\"", "");
            if (ObjectUtil.isNotEmpty(topParentId)) {
                topPatternLibrary = getById(topParentId);
            }
        }

        // 顶级父节点 -> 常青原版对象
        EverGreenVO everGreen = new EverGreenVO();
        BeanUtil.copyProperties(topPatternLibrary, everGreen);

        // 初始化下级常青原版树
        List<EverGreenVO> bottomEverGreenTree = new ArrayList<>();
        // 查询此版型的所有下级
        List<PatternLibrary> patternLibraryList = list(
                new LambdaQueryWrapper<PatternLibrary>()
                        .eq(PatternLibrary::getDelFlag, "0")
                        .like(PatternLibrary::getParentIds, "\"" + topPatternLibrary.getId() + "\"")
        );


        if (ObjectUtil.isNotEmpty(patternLibraryList)) {
            // 下级版型组装成常青原版
            List<EverGreenVO> everGreenVOList = new ArrayList<>(patternLibraryList.size());
            for (PatternLibrary library : patternLibraryList) {
                EverGreenVO everGreenVO = new EverGreenVO();
                BeanUtil.copyProperties(library, everGreenVO);
                everGreenVOList.add(everGreenVO);
            }

            // 生成常青原版树
            bottomEverGreenTree = createEverGreenTree(everGreenVOList, topPatternLibrary.getId());
        }

        everGreen.setEverGreenVOList(bottomEverGreenTree);
        return everGreen;
    }

    @Override

    public PageInfo<PatternLibrary> listEverGreenCode(PatternLibraryPageDTO patternLibraryPageDTO) {
        String id = patternLibraryPageDTO.getId();
        String code = patternLibraryPageDTO.getCode();
        // 筛选条件
        QueryWrapper<PatternLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper
                // 版型库ID
                .ne(ObjectUtil.isNotEmpty(id), "tpl.id", id)
                .like(ObjectUtil.isNotEmpty(code), "tpl.code", code)
                // 已审核的数据
                .eq("tpl.status", 4)
                .orderByDesc("tpl.serial_number")
                .groupBy("tpl.id");
        // 权限设置
        QueryWrapper<PatternLibraryBrand> brandQueryWrapper = new QueryWrapper<>();
        dataPermissionsService.getDataPermissionsForQw(brandQueryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tplb.");
        String sqlSegment = brandQueryWrapper.getSqlSegment();
        if (ObjectUtil.isNotEmpty(sqlSegment)) {
            queryWrapper.exists("select id from t_pattern_library_brand tplb where tplb.pattern_library_id = tpl.id and del_flag='0' and " + sqlSegment);
        } else {
            queryWrapper.exists("select id from t_pattern_library_brand tplb where tplb.pattern_library_id = tpl.id and del_flag='0'");
        }
        PageHelper.startPage(patternLibraryPageDTO.getPageNum(), patternLibraryPageDTO.getPageSize());
        // 得到版型库主表数据集合
        List<PatternLibrary> patternLibraryList = baseMapper.listEverGreenCode(queryWrapper);
        PageInfo<PatternLibrary> patternLibraryPageInfo = new PageInfo<>(patternLibraryList);
        return patternLibraryPageInfo;
    }

    @Override
    public PatternLibraryTemplate queryPatternTypeByStyleId(String styleId) {
        if (ObjectUtil.isEmpty(styleId)) {
            throw new OtherException("「styleId」不能为空！");
        }
        Style style = styleService.getById(styleId);
        if (ObjectUtil.isNotEmpty(style) && ObjectUtil.isNotEmpty(style.getRegisteringId())) {
            PatternLibrary patternLibrary = getById(style.getRegisteringId());
            if (ObjectUtil.isNotEmpty(patternLibrary) && ObjectUtil.isNotEmpty(patternLibrary.getTemplateCode())) {
                PatternLibraryTemplate patternLibraryTemplate = patternLibraryTemplateService.getOne(
                        new LambdaQueryWrapper<PatternLibraryTemplate>()
                                .eq(PatternLibraryTemplate::getCode, patternLibrary.getTemplateCode())
                );
                if (ObjectUtil.isNotEmpty(patternLibraryTemplate)) {
                    return patternLibraryTemplate;
                }
            }
        }
        return new PatternLibraryTemplate();
    }

    public void removeEverGreenTreeNode(String patternLibraryId) {
        PatternLibrary patternLibrary = getById(patternLibraryId);
        String currParentIds = patternLibrary.getParentIds();
        patternLibrary.setEverGreenCode(null);
        patternLibrary.setParentId(null);
        patternLibrary.setParentIds(null);
        updateById(patternLibrary);
        // 查询当前版型的所有子版型，根据 ID 模糊搜索 parentIds 包含此 ID 的子版型
        List<PatternLibrary> patternLibraryList = list(
                new LambdaQueryWrapper<PatternLibrary>()
                        .eq(PatternLibrary::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
                        .like(PatternLibrary::getParentIds, "\"" + patternLibraryId + "\"")
        );
        if (ObjectUtil.isNotEmpty(patternLibraryList)) {
            // 将直属子版型的 parentId 置空，直属和非直属的子版型的 parentIds 截取掉当前版型 ID 以及前面的所有 ID
            for (PatternLibrary item : patternLibraryList) {
                String nonDirectReportsParentIds = item.getParentIds();
                item.setParentIds(nonDirectReportsParentIds.replace(currParentIds, ""));
            }
            updateBatchById(patternLibraryList);
        }
    }

    public void newEverGreenTreeNode(String patternLibraryId) {
        // 根据版型 ID 查询版型信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isNotEmpty(patternLibrary)) {
            // 获取当前版型的父
            String parentId = patternLibrary.getParentId();
            if (ObjectUtil.isNotEmpty(parentId)) {
                // 获得父级的版型信息
                PatternLibrary parentPatternLibrary = getById(parentId);
                if (ObjectUtil.isNotEmpty(parentPatternLibrary)) {
                    // 设置当前版型的父版型和所有上层版型
                    patternLibrary.setParentIds(
                            ObjectUtil.isEmpty(parentPatternLibrary.getParentIds())
                                    ? "\"" + parentPatternLibrary.getId() + "\""
                                    : parentPatternLibrary.getParentIds() + ",\"" + parentPatternLibrary.getId() + "\"");
                    // 查询上层形成环
                    if (patternLibrary.getParentIds().contains(patternLibrary.getId())) {
                        throw new OtherException(ResultConstant.EVERGREEN_ORIGINALS_DO_NOT_FORM_RINGS);
                    }
                    updateById(patternLibrary);
                    // 查询此版型下面的所有子版型
                    List<PatternLibrary> patternLibraryList = list(
                            new LambdaQueryWrapper<PatternLibrary>()
                                    .eq(PatternLibrary::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
                                    .like(PatternLibrary::getParentIds, "\"" + patternLibrary.getId() + "\"")
                    );
                    if (ObjectUtil.isNotEmpty(patternLibraryList)) {
                        for (PatternLibrary library : patternLibraryList) {
                            // 查询上层形成环
                            if (patternLibrary.getParentIds().contains(library.getId())) {
                                throw new OtherException(ResultConstant.EVERGREEN_ORIGINALS_DO_NOT_FORM_RINGS);
                            }
                            library.setParentIds(patternLibrary.getParentIds() + ",\"" + library.getParentIds() + "\"");
                        }
                        // 修改子版型的上层版型集合
                        updateBatchById(patternLibraryList);
                    }
                } else {
                    throw new OtherException(ResultConstant.EVERGREEN_ORIGINALS_DOES_NOT_EXIST_REFRESH_TRY_AGAIN);
                }
            }
        }
    }

    public List<EverGreenVO> createEverGreenTree(List<EverGreenVO> everGreenVOList, String parentId) {
        List<EverGreenVO> collect = everGreenVOList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getParentId()) && item.getParentId().equals(parentId)).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(collect)) {
            for (EverGreenVO everGreenVO : collect) {
                List<EverGreenVO> everGreenTree = createEverGreenTree(everGreenVOList, everGreenVO.getId());
                everGreenVO.setEverGreenVOList(everGreenTree);
            }
        }
        return collect;
    }

    /**
     * 比较目标大类和来源款式信息集合中的大类是否同属于上装或者下装
     *
     * @param targetProdCategory1st 目标大类
     * @param sourceStyleList       来源款式信息集合
     * @return true 代表是 false 代表否
     */
    private boolean isBelong(String targetProdCategory1st, List<Style> sourceStyleList) {
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
     * 版型库数据设置款式信息
     *
     * @param patternLibrary 版型库对象数据
     * @param style          款式信息
     */
    private static void setValue(PatternLibrary patternLibrary, Style style) {
        // 设计款号
        patternLibrary.setDesignNo(style.getDesignNo());
        // 版型编码 等同于设计款号
        patternLibrary.setCode(style.getDesignNo());
        // 款式 ID
        patternLibrary.setStyleId(style.getId());
        // 大类 code
        patternLibrary.setProdCategory1st(style.getProdCategory1st());
        // 大类名称
        patternLibrary.setProdCategory1stName(style.getProdCategory1stName());
        // 品类 code
        patternLibrary.setProdCategory(style.getProdCategory());
        // 品类名称
        patternLibrary.setProdCategoryName(style.getProdCategoryName());
        // 中类 code
        patternLibrary.setProdCategory2nd(style.getProdCategory2nd());
        // 中类名称
        patternLibrary.setProdCategory2ndName(style.getProdCategory2ndName());
        // 小类 code
        patternLibrary.setProdCategory3rd(style.getProdCategory3rd());
        // 小类名称
        patternLibrary.setProdCategory3rdName(style.getProdCategory3rdName());
        // 廓形 code
        patternLibrary.setSilhouetteCode(style.getSilhouette());
        // 廓形名称
        patternLibrary.setSilhouetteName(style.getSilhouetteName());
    }

    /**
     * 设置返回值
     *
     * @param isExcel                是否是导出
     * @param patternLibrary         主表返回对象
     * @param patternLibraryBrandMap 品类表 map
     * @param patternLibraryItemMap  子表 map
     */
    private static void setPatternLibrary(Integer isExcel,
                                          PatternLibrary patternLibrary,
                                          Map<String, List<PatternLibraryBrand>> patternLibraryBrandMap,
                                          Map<String, List<PatternLibraryItem>> patternLibraryItemMap,
                                          Map<String, PatternLibraryTemplate> patternLibraryTemplateMap) {
        patternLibrary.setPatternLibraryUtilization(patternLibrary.getPatternLibraryUtilization() + "%");
        // 设置版型库模板信息
        PatternLibraryTemplate patternLibraryTemplate = patternLibraryTemplateMap.get(patternLibrary.getTemplateCode());
        patternLibrary.setPatternLibraryTemplate(patternLibraryTemplate);

        // 设置版型库品牌数据
        List<PatternLibraryBrand> brands = patternLibraryBrandMap.get(patternLibrary.getId());
        if (ObjectUtil.isNotEmpty(brands)) {
            patternLibrary.setPatternLibraryBrandList(brands);
            patternLibrary.setBrandNames(
                    brands.stream().map(PatternLibraryBrand::getBrandName).collect(Collectors.joining("/")));
        }
        // 设置品类
        String prodCategory1stName = patternLibrary.getProdCategory1stName();
        String prodCategoryName = patternLibrary.getProdCategoryName();
        String prodCategory2ndName = patternLibrary.getProdCategory2ndName();
        String prodCategory3rdName = patternLibrary.getProdCategory3rdName();
        patternLibrary.setAllProdCategoryNames(
                (ObjectUtil.isNotEmpty(prodCategory1stName) ? prodCategory1stName : "无") + "/"
                        + (ObjectUtil.isNotEmpty(prodCategoryName) ? prodCategoryName : "无") + "/"
                        + (ObjectUtil.isNotEmpty(prodCategory2ndName) ? prodCategory2ndName : "无") + "/"
                        + (ObjectUtil.isNotEmpty(prodCategory3rdName) ? prodCategory3rdName : "无")
        );
        // 设置子表数据
        List<PatternLibraryItem> patternLibraryItemLists = patternLibraryItemMap.get(patternLibrary.getId());
        if (ObjectUtil.isNotEmpty(patternLibraryItemLists)) {
            patternLibrary.setPatternLibraryItemList(patternLibraryItemLists);
            // 设置格式化成前端的子表数据
            // 围度
            patternLibrary.setPatternLibraryItemPattern(
                    patternLibraryItemLists.stream()
                            .filter(item -> item.getType().equals(1))
                            .map(item -> item.getName()
                                    + "："
                                    + (ObjectUtil.isNotEmpty(item.getStructureValue()) ? item.getStructureValue() : "暂无") + "\n")
                            .collect(Collectors.joining("")).trim()
            );
            // 长度
            patternLibrary.setPatternLibraryItemLength(
                    patternLibraryItemLists.stream()
                            .filter(item -> item.getType().equals(2))
                            .map(item -> item.getName()
                                    + "："
                                    + (ObjectUtil.isNotEmpty(item.getStructureValue()) ? item.getStructureValue() : "暂无") + "\n")
                            .collect(Collectors.joining("")).trim()
            );
            // 部位
            patternLibrary.setPatternLibraryItemPosition(
                    patternLibraryItemLists.stream()
                            .filter(item -> item.getType().equals(3))
                            .map(item -> item.getName() + "：纸样实际尺寸 " + item.getPatternSize() + "\n")
                            .collect(Collectors.joining("")).trim()
            );
            List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemLists.stream()
                    .filter(item -> item.getType().equals(4)).collect(Collectors.toList());


            String patternLibraryItemParts = "";
            if (isExcel.equals(0) && patternLibraryItemList.size() > 3) {
                patternLibraryItemParts = patternLibraryItemList
                        .stream().map(item -> item.getName() + ":" + item.getCode() + "\n")
                        .limit(3)
                        .collect(Collectors.joining("")).trim();
                patternLibraryItemParts += "\n" + "……";
            } else {
                patternLibraryItemParts = patternLibraryItemList
                        .stream().map(item -> item.getName() + ":" + item.getCode() + "\n")
                        .collect(Collectors.joining("")).trim();
            }
            // 部件
            patternLibrary.setPatternLibraryItemParts(patternLibraryItemParts);
        }
    }

    /**
     * 筛选条件设置值
     *
     * @param patternLibraryPageDTO
     * @return
     */
    private QueryWrapper<PatternLibrary> getPatternLibraryQueryWrapper(PatternLibraryPageDTO patternLibraryPageDTO) {
        QueryWrapper<PatternLibrary> queryWrapper = new QueryWrapper<>();
        String templateNames = patternLibraryPageDTO.getTemplateName();
        String partsNames = patternLibraryPageDTO.getPartsNames();
        patternLibraryPageDTO.setPartsNameList(ObjectUtil.isNotEmpty(partsNames) ? Arrays.asList(partsNames.split(",")) : null);
        String brandNames = patternLibraryPageDTO.getBrandNames();
        patternLibraryPageDTO.setBrandNameList(ObjectUtil.isNotEmpty(brandNames) ? Arrays.asList(brandNames.split(",")) : null);
        queryWrapper
                // 版型编码
                .like(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getCode())
                        , "tpl.code", patternLibraryPageDTO.getCode())
                // 大类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getProdCategory1stName())
                        , "tpl.prod_category1st_name", patternLibraryPageDTO.getProdCategory1stName())
                // 品类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getProdCategoryName())
                        , "tpl.prod_category_name", patternLibraryPageDTO.getProdCategoryName())
                // 中类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getProdCategory2ndName())
                        , "tpl.prod_category2nd_name", patternLibraryPageDTO.getProdCategory2ndName())
                // 小类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getProdCategory3rdName())
                        , "tpl.prod_category3rd_name", patternLibraryPageDTO.getProdCategory3rdName())
                // 廓形编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getSilhouetteName())
                        , "tpl.silhouette_name", patternLibraryPageDTO.getSilhouetteName())
                // 所属版型库
                .in(ObjectUtil.isNotEmpty(templateNames)
                        , "tpl.template_name",
                        ObjectUtil.isNotEmpty(templateNames) ? Arrays.asList(templateNames.split(",")) : null)
                // 状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getStatus())
                        , "tpl.status", patternLibraryPageDTO.getStatus())
                // 启用状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getEnableFlag())
                        , "tpl.enable_flag", patternLibraryPageDTO.getEnableFlag())
                // 大货款号
                .like(ObjectUtil.isNotEmpty(patternLibraryPageDTO.getStyleNo())
                        , "tsc.style_no", patternLibraryPageDTO.getStyleNo())
                .orderByDesc("tpl.serial_number")
                .groupBy("tpl.id");

        if (patternLibraryPageDTO.getIsExcel().equals(1)) {
            // 导出时最多两万条
            queryWrapper.last("limit 20000");
        }
        return queryWrapper;
    }

    /**
     * 批量查询详情 用作批量修改时的日志记录
     *
     * @param patternLibraryIdList 版型库 id 集合
     * @return 版型库信息集合
     */
    public List<PatternLibrary> listDetail(List<String> patternLibraryIdList) {
        if (ObjectUtil.isEmpty(patternLibraryIdList)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        // 根据版型库主表 ID 查询版型库主表信息
        List<PatternLibrary> patternLibraryList = listByIds(patternLibraryIdList);
        if (ObjectUtil.isEmpty(patternLibraryList) || (patternLibraryList.size() != patternLibraryIdList.size())) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }

        // 查询品类信息
        List<PatternLibraryBrand> patternLibraryBrandList = patternLibraryBrandService.list(
                new LambdaQueryWrapper<PatternLibraryBrand>()
                        .in(PatternLibraryBrand::getPatternLibraryId, patternLibraryIdList)
                        .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        Map<String, List<PatternLibraryBrand>> patternLibraryBrandMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(patternLibraryBrandList)) {
            patternLibraryBrandMap = patternLibraryBrandList.stream().collect(Collectors.groupingBy(PatternLibraryBrand::getPatternLibraryId));
        }
        // 查询版型库子表信息
        List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .in(PatternLibraryItem::getPatternLibraryId, patternLibraryIdList)
                        .eq(PatternLibraryItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        Map<String, List<PatternLibraryItem>> patternLibraryItemMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(patternLibraryItemList)) {
            patternLibraryItemMap = patternLibraryItemList.stream().collect(Collectors.groupingBy(PatternLibraryItem::getPatternLibraryId));
        }

        for (PatternLibrary patternLibrary : patternLibraryList) {
            patternLibrary.setPatternLibraryBrandList(patternLibraryBrandMap.get(patternLibrary.getId()));
            patternLibrary.setPatternLibraryItemList(patternLibraryItemMap.get(patternLibrary.getId()));
        }

        return patternLibraryList;
    }

}
