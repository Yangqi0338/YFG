package com.base.sbc.module.patternlibrary.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.base.sbc.module.patternlibrary.dto.ExcelImportDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class PatterLibraryListener implements ReadListener<ExcelImportDTO> {

    /**
     * 缓存的数据
     */
    private final List<ExcelImportDTO> cachedDataList = new ArrayList<>();

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param excelImportDTO 每一条数据的对象
     * @param context
     */
    @Override
    public void invoke(ExcelImportDTO excelImportDTO, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(excelImportDTO));
        cachedDataList.add(excelImportDTO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析到数据一共 {} 条", cachedDataList.size());
    }

}