package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.base.sbc.module.patternlibrary.constants.GeneralConstant;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import com.base.sbc.module.patternlibrary.dto.AuditsDTO;
import com.base.sbc.module.patternlibrary.dto.ExcelImportDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.entity.*;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.listener.PatterLibraryListener;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryMapper;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryTemplateMapper;
import com.base.sbc.module.patternlibrary.service.*;
import com.base.sbc.module.patternlibrary.vo.CategoriesTypeVO;
import com.base.sbc.module.patternlibrary.vo.ExcelExportVO;
import com.base.sbc.module.patternlibrary.vo.FilterCriteriaVO;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 版型库-主表 服务实现类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Service
public class PatternLibraryServiceImpl extends BaseServiceImpl<PatternLibraryMapper, PatternLibrary> implements PatternLibraryService {

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

    @Autowired
    private CcmFeignService ccmFeignService;

    @Autowired
    private OperaLogService operaLogService;

    @Value("${brand.puts:A01,A02,A03}")
    private String brandPuts;

    @Value("${brand.bottoms:A04}")
    private String brandBottoms;

    @Override
    public PageInfo<PatternLibraryVO> listPages(PatternLibraryPageDTO patternLibraryPageDTO) {
        // 筛选条件
        QueryWrapper<PatternLibraryVO> queryWrapper = getPatternLibraryVOQueryWrapper(patternLibraryPageDTO);
        // 权限设置
        dataPermissionsService.getDataPermissionsForQw(
                queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tpl"
        );
        // 列表分页 不是用作导出时分页
        if (patternLibraryPageDTO.getIsExcel().equals(0)) {
            PageHelper.startPage(patternLibraryPageDTO.getPageNum(), patternLibraryPageDTO.getPageSize());
        }
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
                List<PatternLibraryTemplate> patternLibraryTemplateList
                        = patternLibraryTemplateMapper.listByCodes(templateCodeList);
                // 模板数据根据模板 code 转成 map
                if (ObjectUtil.isNotEmpty(patternLibraryTemplateList)) {
                    patternLibraryTemplateMap = patternLibraryTemplateList
                            .stream().collect(Collectors.toMap(PatternLibraryTemplate::getCode, item -> item));
                }
                // 如果模板数据不为空
                if (ObjectUtil.isNotEmpty(patternLibraryTemplateList)) {
                    setPatternLibraryTemplateItem(patternLibraryTemplateList);
                }
            }

            for (PatternLibraryVO patternLibraryVO : patternLibraryVOList) {
                setPatternLibraryVO(patternLibraryVO, patternLibraryTemplateMap, patternLibraryBrandMap, patternLibraryItemMap);
            }
        }
        return new PageInfo<>(patternLibraryVOList);
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
        PatternLibraryVO oldPatternLibraryVO = new PatternLibraryVO();
        if (ObjectUtil.isNotEmpty(patternLibraryDTO.getId())) {
            oldPatternLibrary = getById(patternLibraryDTO.getId());
            if (!(oldPatternLibrary.getStatus().equals(PatternLibraryStatusEnum.NO_SUBMIT.getCode())
                    || oldPatternLibrary.getStatus().equals(PatternLibraryStatusEnum.REJECTED.getCode()))) {
                throw new OtherException(ResultConstant.CURRENT_STATE_DATA_NOT_DO_IT);
            }

            // 查询旧数据 用作日志匹配
            oldPatternLibraryVO = getDetail(patternLibraryDTO.getId());
        }
        PatternLibrary patternLibrary = new PatternLibrary();
        BeanUtil.copyProperties(patternLibraryDTO, patternLibrary);
        // 判断设计款的大类和选择的大类是否都属于上装或者下装
        {
            Style style = styleService.getById(patternLibraryDTO.getStyleId());
            if (!isBelong(patternLibraryDTO.getProdCategory1st(), Collections.singletonList(style))) {
                throw new OtherException(ResultConstant.CATEGORY_PUT_BOTTOMS_MISMATCH);
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
        PatternLibraryVO newPatternLibraryVO = getDetail(patternLibraryDTO.getId());
        // 保存日志
        this.saveOperaLog("新增/编辑", GeneralConstant.LOG_NAME, patternLibrary.getId(), patternLibrary.getCode(), patternLibrary.getCode(), newPatternLibraryVO, oldPatternLibraryVO);

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
        PatternLibraryVO oldPatternLibraryVO = getDetail(patternLibraryDTO.getId());
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
        PatternLibraryVO newPatternLibraryVO = getDetail(patternLibraryDTO.getId());
        // 保存日志
        this.saveOperaLog("新增/编辑", GeneralConstant.LOG_NAME, patternLibrary.getId(), patternLibrary.getCode(), patternLibrary.getCode(), newPatternLibraryVO, oldPatternLibraryVO);
        return true;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDetails(List<PatternLibraryDTO> patternLibraryDTOList) {
        if (ObjectUtil.isEmpty(patternLibraryDTOList)) {
            throw new OtherException(ResultConstant.OPERATION_DATA_NOT_EMPTY);
        }
        if (patternLibraryDTOList
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
        // 查询旧数据 用作日志匹配
        List<PatternLibrary> oldPatternLibraryList = listDetail(patternLibraryDTOList.stream().map(PatternLibraryDTO::getId).collect(Collectors.toList()));
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
        // 查询新数据 用作日志匹配
        List<PatternLibrary> newPatternLibraryList = listDetail(patternLibraryDTOList.stream().map(PatternLibraryDTO::getId).collect(Collectors.toList()));
        // 保存日志
        this.saveOperaLogBatch("批量编辑", GeneralConstant.LOG_NAME, newPatternLibraryList, oldPatternLibraryList);
        return true;
    }

    @Override
    public PatternLibraryVO getDetail(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        // 初始化返回数据
        PatternLibraryVO patternLibraryVO = new PatternLibraryVO();
        // 根据版型库主表 ID 查询版型库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
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
        return patternLibraryVO;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDetail(String patternLibraryId) {
        if (ObjectUtil.isEmpty(patternLibraryId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        // 查询新数据 用作日志匹配
        PatternLibraryVO oldPatternLibraryVO = getDetail(patternLibraryId);
        // 根据版型库主表 ID 查询版型库主表信息
        PatternLibrary patternLibrary = getById(patternLibraryId);
        if (ObjectUtil.isEmpty(patternLibrary)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        if (patternLibrary.getStatus().equals(PatternLibraryStatusEnum.REVIEWED.getCode())) {
            throw new OtherException(ResultConstant.CURRENT_STATE_DATA_NOT_DO_IT);
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
        this.saveOperaLog("删除", GeneralConstant.LOG_NAME, patternLibraryId, patternLibrary.getCode(), patternLibrary.getCode(), new PatternLibrary(), oldPatternLibraryVO);
        return true;
    }

    @Override
    @DuplicationCheck
    public Boolean updateAudits(AuditsDTO auditsDTO) {
        List<String> patternLibraryIdList = auditsDTO.getPatternLibraryIdList();
        if (ObjectUtil.isEmpty(patternLibraryIdList)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
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
        // 查询新数据 用作日志匹配
        List<PatternLibrary> oldPatternLibraryList = listDetail(patternLibraryIdList);
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
        List<PatternLibrary> newPatternLibraryList = listDetail(patternLibraryIdList);
        // 保存日志
        this.saveOperaLogBatch("批量审核", GeneralConstant.LOG_NAME, newPatternLibraryList, oldPatternLibraryList);
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
        PatternLibraryVO oldPatternLibraryVO = getDetail(patternLibraryDTO.getId());
        patternLibrary.setEnableFlag(patternLibraryDTO.getEnableFlag());
        updateById(patternLibrary);
        // 查询新数据 用作日志匹配
        PatternLibraryVO newPatternLibraryVO = getDetail(patternLibraryDTO.getId());
        // 保存日志
        this.saveOperaLog("启用/禁用", GeneralConstant.LOG_NAME, patternLibrary.getId(), patternLibrary.getCode(), patternLibrary.getCode(), newPatternLibraryVO, oldPatternLibraryVO);
        return true;
    }

    @Override
    @DuplicationCheck
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
        // 没有拿到数据也返回一场
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
        List<String> styleNoList = cachedDataList.stream().map(ExcelImportDTO::getCode).collect(Collectors.toList());
        Set<String> styleNoSet = cachedDataList.stream().map(ExcelImportDTO::getCode).collect(Collectors.toSet());
        if (styleNoSet.size() != styleNoList.size()) {
            throw new OtherException(ResultConstant.LAYOUT_CODES_CANNOT_BE_REPEATED);
        }
        // 根据设计编号查询款式信息
        List<Style> styleList = listStyle(null, styleNoList);
        if (ObjectUtil.isEmpty(styleList) || (styleNoList.size() != styleList.size())) {
            throw new OtherException(ResultConstant.CODE_NO_CORRESPONDING_STYLE_OR_ALREADY_EXISTS);
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
            PatternLibraryVO patternLibraryVO = new PatternLibraryVO();
            Style style = styleMap.get(excelImportDTO.getCode());
            setValue(patternLibraryVO, style);
            BeanUtil.copyProperties(patternLibraryVO, patternLibrary);
            // 默认状态是待补齐
            patternLibrary.setStatus(PatternLibraryStatusEnum.NO_PADDED.getCode());

            String[] brandNameList = excelImportDTO.getBrandName().split("/");
            List<PatternLibraryBrand> patternLibraryBrandList = new ArrayList<>();
            for (String name : brandNameList) {
                String code = brandMap.get(name);
                if (ObjectUtil.isEmpty(code)) {
                    throw new OtherException(ResultConstant.BRAND_DATA_NOT_EXIST);
                }
                PatternLibraryBrand patternLibraryBrand = new PatternLibraryBrand();
                patternLibraryBrand.setBrand(code);
                patternLibraryBrand.setBrandName(name);
                patternLibraryBrandList.add(patternLibraryBrand);
            }

            patternLibrary.setPatternLibraryBrandList(patternLibraryBrandList);
            if (!"启用".equals(excelImportDTO.getEnableFlag()) && !"禁用".equals(excelImportDTO.getEnableFlag())) {
                // 默认启用
                patternLibrary.setEnableFlag(1);
            } else {
                patternLibrary.setEnableFlag("启用".equals(excelImportDTO.getEnableFlag()) ? 1 : 0);

            }
            patternLibraryList.add(patternLibrary);
        }
        // 查询旧数据 用作日志匹配
        List<PatternLibrary> oldPatternLibraryList = listDetail(patternLibraryList.stream().map(PatternLibrary::getId).collect(Collectors.toList()));
        // 保存日志
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
        this.saveOperaLogBatch("导入", GeneralConstant.LOG_NAME, newPatternLibraryList, oldPatternLibraryList);
        return true;
    }

    @Override
    public Boolean excelExport(PatternLibraryPageDTO patternLibraryPageDTO, HttpServletResponse response) {
        // 设置为导出的类型
        patternLibraryPageDTO.setIsExcel(1);
        PageInfo<PatternLibraryVO> patternLibraryVOPageInfo = listPages(patternLibraryPageDTO);
        List<PatternLibraryVO> list = patternLibraryVOPageInfo.getList();
        System.out.println(JSONUtil.toJsonStr(list));
        if (ObjectUtil.isEmpty(list)) {
            throw new OtherException(ResultConstant.NO_DATA_EXPORT);
        }
        // 转成导出的数据
        try {
            List<ExcelExportVO> excelExportVOList = new ArrayList<>(list.size());
            for (PatternLibraryVO patternLibraryVO : list) {
                ExcelExportVO excelExportVO = new ExcelExportVO();
                BeanUtil.copyProperties(patternLibraryVO, excelExportVO);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                excelExportVO.setCreateDate(simpleDateFormat.format(patternLibraryVO.getCreateDate()));
                excelExportVO.setUpdateDate(simpleDateFormat.format(patternLibraryVO.getUpdateDate()));
                excelExportVO.setStatus(PatternLibraryStatusEnum.getValueByCode(patternLibraryVO.getStatus()));
                excelExportVO.setPicUrl(
                        ObjectUtil.isNotEmpty(patternLibraryVO.getPicUrl()) ? new URL(patternLibraryVO.getPicUrl()) : null
                );
                excelExportVO.setEnableFlag(
                        patternLibraryVO.getEnableFlag().equals(0)
                                ? "禁用" : patternLibraryVO.getEnableFlag().equals(1) ? "启用" : "未知");
                excelExportVOList.add(excelExportVO);
            }
            System.out.println(JSONUtil.toJsonStr(excelExportVOList));
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
    public List<Style> listStyle(String search, List<String> styleNoList) {
        QueryWrapper<Style> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.del_flag", BaseGlobal.DEL_FLAG_NORMAL)
                .eq("s.enable_status", BaseGlobal.NO)
                .eq("s.status", "1")
                .like(ObjectUtil.isNotEmpty(search), "s.design_no", search)
                .in(ObjectUtil.isNotEmpty(styleNoList), "s.design_no", styleNoList)
                .orderByDesc("s.create_date");
        // 获取还没有生成版型库的数据
        return baseMapper.listStyle(queryWrapper);
    }

    @Override
    public PatternLibraryVO getInfoByDesignNo(String designNo) {
        // 初始化返回的封装数据
        PatternLibraryVO patternLibraryVO = new PatternLibraryVO();
        if (ObjectUtil.isEmpty(designNo)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        Style style = styleService.getOne(
                new LambdaQueryWrapper<Style>()
                        .eq(Style::getDesignNo, designNo)
                        .eq(Style::getEnableStatus, "0")
                        .eq(Style::getDelFlag, "0")
                        .eq(Style::getConfirmStatus, "2")
        );
        if (ObjectUtil.isEmpty(style)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        setValue(patternLibraryVO, style);
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

    /**
     * @param type 筛选条件类型（1-版型编码 2-品牌 3-所属品类 4-廓形 5-所属版型库 6-设计部件 7-审核状态 8-是否启用）
     * @return
     */
    @Override
    public List<FilterCriteriaVO> getAllFilterCriteria(Integer type) {
        if (ObjectUtil.isEmpty(type) || type < 1 || type > 8) {
            throw new OtherException(ResultConstant.FILTER_TYPE_DOES_NOT_EXIST);
        }
        QueryWrapper<PatternLibraryVO> queryWrapper = new QueryWrapper<>();

        // 权限设置
        dataPermissionsService.getDataPermissionsForQw(
                queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tpl"
        );
        // 得到版型库主表数据集合
        return baseMapper.getAllFilterCriteria(queryWrapper, type);
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
     * @param patternLibraryVO 版型库对象数据
     * @param style            款式信息
     */
    private static void setValue(PatternLibraryVO patternLibraryVO, Style style) {
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
    }

    /**
     * 设置模板子表的数据
     *
     * @param patternLibraryTemplateList
     */
    private void setPatternLibraryTemplateItem(List<PatternLibraryTemplate> patternLibraryTemplateList) {
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


    /**
     * 设置返回值
     *
     * @param patternLibraryVO          主表返回对象
     * @param patternLibraryTemplateMap 模板表 map
     * @param patternLibraryBrandMap    品类表 map
     * @param patternLibraryItemMap     子表 map
     */
    private static void setPatternLibraryVO(PatternLibraryVO patternLibraryVO,
                                            Map<String, PatternLibraryTemplate> patternLibraryTemplateMap,
                                            Map<String, List<PatternLibraryBrand>> patternLibraryBrandMap,
                                            Map<String, List<PatternLibraryItem>> patternLibraryItemMap) {
        // 设置所属版型库数据
        patternLibraryVO.setPatternLibraryTemplate(
                patternLibraryTemplateMap.get(patternLibraryVO.getTemplateCode())
        );
        // 设置版型库品牌数据
        List<PatternLibraryBrand> brands = patternLibraryBrandMap.get(patternLibraryVO.getId());
        if (ObjectUtil.isNotEmpty(brands)) {
            patternLibraryVO.setPatternLibraryBrandList(brands);
            patternLibraryVO.setBrandNames(
                    brands.stream().map(PatternLibraryBrand::getBrandName).collect(Collectors.joining("/")));
        }
        // 设置模板子表数据
        PatternLibraryTemplate patternLibraryTemplate = patternLibraryVO.getPatternLibraryTemplate();
        if (ObjectUtil.isNotEmpty(patternLibraryTemplate)) {
            patternLibraryVO.setPatternLibraryTemplateItem(patternLibraryTemplate.getPatternLibraryTemplateItem());
        }
        // 设置品类
        String prodCategory1stName = patternLibraryVO.getProdCategory1stName();
        String prodCategoryName = patternLibraryVO.getProdCategoryName();
        String prodCategory2ndName = patternLibraryVO.getProdCategory2ndName();
        String prodCategory3rdName = patternLibraryVO.getProdCategory3rdName();
        patternLibraryVO.setAllProdCategoryNames(
                (ObjectUtil.isNotEmpty(prodCategory1stName) ? prodCategory1stName : "无") + "/"
                        + (ObjectUtil.isNotEmpty(prodCategoryName) ? prodCategoryName : "无") + "/"
                        + (ObjectUtil.isNotEmpty(prodCategory2ndName) ? prodCategory2ndName : "无") + "/"
                        + (ObjectUtil.isNotEmpty(prodCategory3rdName) ? prodCategory3rdName : "无")
        );
        // 设置子表数据
        List<PatternLibraryItem> patternLibraryItemLists = patternLibraryItemMap.get(patternLibraryVO.getId());
        if (ObjectUtil.isNotEmpty(patternLibraryItemLists)) {
            patternLibraryVO.setPatternLibraryItemList(patternLibraryItemLists);
            // 设置格式化成前端的子表数据
            // 围度
            patternLibraryVO.setPatternLibraryItemPattern(
                    patternLibraryItemLists.stream()
                            .filter(item -> item.getType().equals(1))
                            .map(item -> item.getName()
                                    + "："
                                    + Optional.ofNullable(item.getStructureValue()).orElse("暂无") + "\n")
                            .collect(Collectors.joining("")).trim()
            );
            // 长度
            patternLibraryVO.setPatternLibraryItemLength(
                    patternLibraryItemLists.stream()
                            .filter(item -> item.getType().equals(2))
                            .map(item -> item.getName()
                                    + "："
                                    + Optional.ofNullable(item.getStructureValue()).orElse("暂无") + "\n")
                            .collect(Collectors.joining("")).trim()
            );
            // 部位
            patternLibraryVO.setPatternLibraryItemPosition(
                    patternLibraryItemLists.stream()
                            .filter(item -> item.getType().equals(3))
                            .map(item -> item.getName() + "：纸样实际尺寸 " + item.getPatternSize() + "\n")
                            .collect(Collectors.joining("")).trim()
            );
            // 部件
            patternLibraryVO.setPatternLibraryItemParts(
                    patternLibraryItemLists.stream()
                            .filter(item -> item.getType().equals(4))
                            .map(PatternLibraryItem::getName)
                            .distinct()
                            .collect(Collectors.joining("/")).trim()

            );
        }
    }

    /**
     * 筛选条件设置值
     *
     * @param patternLibraryPageDTO
     * @return
     */
    private QueryWrapper<PatternLibraryVO> getPatternLibraryVOQueryWrapper(PatternLibraryPageDTO patternLibraryPageDTO) {
        QueryWrapper<PatternLibraryVO> queryWrapper = new QueryWrapper<>();
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
                        .eq(PatternLibraryBrand::getPatternLibraryId, patternLibraryIdList)
                        .eq(PatternLibraryBrand::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        Map<String, List<PatternLibraryBrand>> patternLibraryBrandMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(patternLibraryBrandList)) {
            patternLibraryBrandMap = patternLibraryBrandList.stream().collect(Collectors.groupingBy(PatternLibraryBrand::getPatternLibraryId));
        }
        // 查询版型库子表信息
        List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                new LambdaQueryWrapper<PatternLibraryItem>()
                        .eq(PatternLibraryItem::getPatternLibraryId, patternLibraryIdList)
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
