package com.base.sbc.module.hrtrafficlight.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.base.sbc.module.patternlibrary.dto.ExcelImportDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data
public class HrTrafficLightListener implements ReadListener<Map<Integer, String>> {

    /**
     * 缓存的数据
     */
    private final List<Map<Integer, String>> cachedDataList = new ArrayList<>();

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param map    每一条数据的对象
     * @param context
     */
    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(map));
        cachedDataList.add(map);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析到数据一共 {} 条", cachedDataList.size());
    }

}