/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricPlanningItemImportDTO;
import com.base.sbc.module.fabric.dto.FabricPlanningItemSaveDTO;
import com.base.sbc.module.fabric.entity.FabricPlanningItem;
import com.base.sbc.module.fabric.mapper.FabricPlanningItemMapper;
import com.base.sbc.module.fabric.service.BasicFabricLibraryService;
import com.base.sbc.module.fabric.service.FabricPlanningItemService;
import com.base.sbc.module.fabric.vo.BasicFabricLibraryListVO;
import com.base.sbc.module.fabric.vo.FabricPlanningItemVO;
import com.beust.jcommander.internal.Lists;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.base.sbc.module.basicsdatum.enums.BasicsdatumMaterialBizTypeEnum.*;

/**
 * 类描述：面料企划明细 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPlanningItemService
 * @email your email
 * @date 创建时间：2023-8-23 11:02:55
 */
@Service
public class FabricPlanningItemServiceImpl extends BaseServiceImpl<FabricPlanningItemMapper, FabricPlanningItem> implements FabricPlanningItemService {
    // 自定义方法区 不替换的区域【other_start】
    protected static final Logger logger = LoggerFactory.getLogger(FabricPlanningItemService.class);

    @Autowired
    private BasicFabricLibraryService basicFabricLibraryService;


    @Override
    public void saveItem(List<FabricPlanningItemSaveDTO> dto, String fabricPlanningId) {
        if (CollUtil.isEmpty(dto)) {
            LambdaQueryWrapper<FabricPlanningItem> queryWrapper = new QueryWrapper<FabricPlanningItem>()
                    .lambda()
                    .eq(FabricPlanningItem::getFabricPlanningId, fabricPlanningId)
                    .eq(FabricPlanningItem::getDelFlag, "0");
            super.getBaseMapper().delete(queryWrapper);
            return;
        }
        List<String> ids = this.getIdByFabricPlanningId(fabricPlanningId);
        String companyCode = super.getCompanyCode();
        IdGen idGen = new IdGen();
        List<FabricPlanningItem> fabricPlanningItems = dto.stream()
                .map(e -> {
                    FabricPlanningItem fabricPlanningItem = CopyUtil.copy(e, FabricPlanningItem.class);
                    if (StringUtils.isEmpty(fabricPlanningItem.getId()) || StringUtils.contains(fabricPlanningItem.getId(), StrUtil.DASHED)) {
                        fabricPlanningItem.setId(idGen.nextIdStr());
                        fabricPlanningItem.setCompanyCode(companyCode);
                        fabricPlanningItem.insertInit();
                    } else {
                        fabricPlanningItem.updateInit();
                    }
                    fabricPlanningItem.setFabricPlanningId(fabricPlanningId);
                    ids.remove(fabricPlanningItem.getId());
                    return fabricPlanningItem;
                }).collect(Collectors.toList());

        super.saveOrUpdateBatch(fabricPlanningItems);
        if (CollUtil.isNotEmpty(ids)) {
            super.getBaseMapper().deleteBatchIds(ids);
        }
    }

    @Override
    public List<FabricPlanningItemVO> getByFabricPlanningId(String fabricPlanningId, String materialFlag) {
        return super.getBaseMapper().getByFabricPlanningId(fabricPlanningId, materialFlag);
    }

    @Override
    public String fabricPlanningItemImportExcel(MultipartFile file, String fabricPlanningId) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        try {
            List<FabricPlanningItemImportDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), FabricPlanningItemImportDTO.class, params);
            if (CollUtil.isEmpty(list) || list.size() > 200) {
                return "导入失败，数据为空或超出最大限制（200条）";
            }
            List<String> materialCodes = list.stream()
                    .map(FabricPlanningItemImportDTO::getMaterialCode)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
            Map<String, BasicFabricLibraryListVO> basicFabricLibraryListVOMap = basicFabricLibraryService.getByMaterialCodes(materialCodes);

            List<String> errorMsg = new ArrayList<>();
            String companyCode = super.getCompanyCode();
            List<FabricPlanningItem> fabricPlanningItems = materialCodes.stream()
                    .map(materialCode -> {
                        BasicFabricLibraryListVO basicFabricLibraryListVO = basicFabricLibraryListVOMap.get(materialCode);
                        if (Objects.isNull(basicFabricLibraryListVO)) {
                            errorMsg.add(materialCode + ":物料数据不存在");
                            return null;
                        }
                        FabricPlanningItem fabricPlanningItem = new FabricPlanningItem();
                        String source = basicFabricLibraryListVO.getBizType().equals(MATERIAL.getK()) ? "4" :
                                basicFabricLibraryListVO.getBizType().equals(DEV_APPLY.getK()) ? "3" :
                                        basicFabricLibraryListVO.getBizType().equals(FABRIC_LIBRARY.getK()) ? "2" : "1";
                        fabricPlanningItem.setSource(source);
                        fabricPlanningItem.setSourceId(basicFabricLibraryListVO.getId());
                        fabricPlanningItem.setCompanyCode(companyCode);
                        fabricPlanningItem.insertInit();
                        fabricPlanningItem.setFabricPlanningId(fabricPlanningId);
                        return fabricPlanningItem;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(fabricPlanningItems)) {
                super.saveBatch(fabricPlanningItems);
            }
            return String.join(",", errorMsg);
        } catch (Exception e) {
            logger.error("FabricPlanningItemService#fabricPlanningItemImportExcel throw error ", e);
            return "导入失败";
        }
    }

    @Override
    public Map<String, List<String>> getFabricPlanningId(String materialCode) {
        List<FabricPlanningItem> fabricPlanningItems = super.getBaseMapper().getFabricPlanningId(materialCode);
        return fabricPlanningItems.stream()
                .collect(Collectors.groupingBy(FabricPlanningItem::getFabricPlanningId, Collectors.mapping(FabricPlanningItem::getId, Collectors.toList())));
    }

    private List<String> getIdByFabricPlanningId(String fabricPlanningId) {
        LambdaQueryWrapper<FabricPlanningItem> qw = new QueryWrapper<FabricPlanningItem>().lambda()
                .eq(FabricPlanningItem::getFabricPlanningId, fabricPlanningId)
                .eq(FabricPlanningItem::getDelFlag, "0")
                .select(FabricPlanningItem::getId);
        List<FabricPlanningItem> list = super.list(qw);
        return CollUtil.isEmpty(list) ? Lists.newArrayList() : list.stream()
                .map(FabricPlanningItem::getId)
                .collect(Collectors.toList());
    }


// 自定义方法区 不替换的区域【other_end】

}
