package com.base.sbc.module.workload.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.vo.SelectOptionsChildrenVo;
import com.base.sbc.module.moreLanguage.dto.DataVerifyResultVO;
import com.base.sbc.module.workload.dto.WorkloadRatingItemDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingItemMapExportMapping;
import com.base.sbc.module.workload.dto.WorkloadRatingTitleFieldDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.base.sbc.module.workload.service.WorkloadRatingConfigService;
import com.base.sbc.module.workload.service.WorkloadRatingItemService;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemQO;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.module.common.convert.ConvertContext.WORKLOAD_CV;

/**
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkloadRatingItemImportListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 数据合法性校验结果
     */
    private static final ThreadLocal<List<DataVerifyResultVO>> dataVerifyResults = new TransmittableThreadLocal<>();
    public static ThreadLocal<Map<String, WorkloadRatingItemMapExportMapping>> sheetExportMapping = new TransmittableThreadLocal<>();

    private final WorkloadRatingConfigService workloadRatingConfigService;
    private final WorkloadRatingItemService workloadRatingItemService;
    @Setter
    private WorkloadRatingItemQO excelQueryDto;

//    private ThreadPoolExecutor threadPoolExecutor;
    SFunction<DataVerifyResultVO, String> sheetNameFunc = DataVerifyResultVO::getSheetName;
    SFunction<DataVerifyResultVO, Integer> rowIndexFunc = DataVerifyResultVO::getRowIndex;

//    @PostConstruct
//    public void initThread(){
//        threadPoolExecutor = new ThreadPoolExecutor(8, 8,
//                0L, TimeUnit.MILLISECONDS,
//                new ArrayBlockingQueue<>(16), r -> {
//                    Thread thread = new Thread(r,"工作量导入");
//                    thread.setUncaughtExceptionHandler((Thread t,Throwable e) -> {
//                        if(e != null){
//                            e.printStackTrace();
//                            throw new OtherException(e.getMessage());
//                        }
//                    });
//                    return thread;
//                });
//    }

    /**
     * headRowNumber() 通过这个方法来解析对应的行, 不指定为1, 即第一行数据会进到invokeHeadMap方法
     * */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        WorkloadRatingConfigQO configQO = WORKLOAD_CV.copy2ConfigQO(excelQueryDto);
        configQO.setIsConfigShow(YesOrNoEnum.YES);
        configQO.reset2QueryFirst();
        List<WorkloadRatingConfigVO> list = workloadRatingConfigService.queryList(configQO);
        if (CollUtil.isEmpty(list)) throw new OtherException("未找到工作量配置");
        WorkloadRatingConfigVO configVO = list.get(0);
        getMapping(context,(mapExportMapping)-> {
            mapExportMapping.setHeadMap(headMap);
            mapExportMapping.setConfigVO(configVO);
            mapExportMapping.initByFirstRow(headMap, null);
            return true;
        });
        // 添加检查点, 使用工厂模式
    }

    private <T> T getMapping(AnalysisContext context, Function<WorkloadRatingItemMapExportMapping, T> function){
        Map<String, WorkloadRatingItemMapExportMapping> exportMapping = sheetExportMapping.get();
        if (exportMapping == null) {
            exportMapping = new HashMap<>(16);
        }
        String sheetName = context.readSheetHolder().getSheetName();
        WorkloadRatingItemMapExportMapping mapExportMapping = exportMapping.getOrDefault(sheetName, new WorkloadRatingItemMapExportMapping(sheetName));
        T result = function.apply(mapExportMapping);
        exportMapping.put(sheetName,mapExportMapping);
        sheetExportMapping.set(exportMapping);;
        return result;
    }

    private void removeMapping(AnalysisContext context){
        String sheetName = context.readSheetHolder().getSheetName();
        Map<String, WorkloadRatingItemMapExportMapping> exportMapping = sheetExportMapping.get();
        if (exportMapping == null || !exportMapping.containsKey(sheetName)) {
            return;
        }
        exportMapping.remove(sheetName);
        if (exportMapping.isEmpty()) {
            sheetExportMapping.remove();
        }
    }

    AtomicInteger index = new AtomicInteger();

    /**
     * 调用完invokeHeadMap后会调用
     * */
    @Override
    public void invoke(Map<Integer, String> row, AnalysisContext context) {
        WorkloadRatingItemMapExportMapping exportMapping = getMapping(context, Function.identity());
        Map<String, String> map = exportMapping.buildMap(row);
        WorkloadRatingConfigVO configVO = exportMapping.getConfigVO();

        List<List<SelectOptionsChildrenVo>> structureList = CommonUtils.flattenStructure(configVO.getOptionsList(), SelectOptionsChildrenVo::getChildren);
        AtomicReference<String> itemValue = new AtomicReference<>();
        try {
            configVO.getTitleFieldDTOList().stream()
                    .filter(it -> StrUtil.isBlank(it.getConfigId()))
                    .sorted(Comparator.comparing(WorkloadRatingTitleFieldDTO::getIndex)).forEach(titleFieldDTO -> {
                        String key = map.get(titleFieldDTO.getCode());
                        if (StrUtil.isNotBlank(key)) {
                            List<SelectOptionsChildrenVo> optionsList = CollUtil.get(structureList, titleFieldDTO.getIndex());
                            SelectOptionsChildrenVo structure = CollUtil.findOne(optionsList, (it) -> it.getLabel().equals(key));
                            itemValue.set(Opt.ofNullable(structure).map(SelectOptionsChildrenVo::getValue).orElseThrow(()-> new OtherException("找不到" + key)));
                        }
                    });
        }catch (OtherException e) {
            return;
        }

        WorkloadRatingItemDTO itemDTO = BeanUtil.toBean(map,WorkloadRatingItemDTO.class);
        map.forEach((key,value)-> itemDTO.getExtend().put(key,value));

        itemDTO.setBrand(excelQueryDto.getBrand());
        itemDTO.setType(excelQueryDto.getType());
        itemDTO.setConfigId(configVO.getId());
        itemDTO.setConfigName(configVO.getItemName());
        itemDTO.setItemValue(itemValue.get());
        if (StrUtil.isNotBlank(itemDTO.getItemValue()) && exportMapping.getSourceData().stream().noneMatch(it-> itemDTO.getItemValue().equals(it.getItemValue()))) {
            exportMapping.getSourceData().add(itemDTO);
        }
        index.incrementAndGet();
    }

    /**
     * 解析完所有行后会调用
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println(index.get());
        WorkloadRatingItemMapExportMapping exportMapping = getMapping(context, Function.identity());
        removeMapping(context);
        List<WorkloadRatingItemDTO> sourceData = exportMapping.getSourceData();
        if (CollUtil.isNotEmpty(sourceData)) {
            workloadRatingItemService.save(sourceData);
        }
        index.set(0);
    }



    public void addDataVerifyResult(AnalysisContext context, Integer rowIndex, String key, String value) {
        addDataVerifyResult(true, context, rowIndex, key, value);
    }

    /**
     * 展示验证信息
     * */
    public void addDataVerifyResult(Boolean addDataVerifyResult, AnalysisContext context, Integer rowIndex, String key, String value) {
        if (!addDataVerifyResult) return;
        List<DataVerifyResultVO> dataVerifyResultVOS = dataVerifyResults.get();
        if (dataVerifyResultVOS == null) {
            dataVerifyResultVOS = new ArrayList<>();
        }
        String sheetName = context.readSheetHolder().getSheetName();

        DataVerifyResultVO dataVerifyResultVO = dataVerifyResultVOS.stream()
                .filter(it -> sheetName.equals(it.getSheetName()) && rowIndex.equals(it.getRowIndex()))
                .findFirst().orElse(new DataVerifyResultVO(sheetName, rowIndex, new HashMap<>()));

        dataVerifyResultVO.getErrorMessageList().put(key,value);
        dataVerifyResultVOS.add(dataVerifyResultVO);

        dataVerifyResults.set(dataVerifyResultVOS);
    }

    /**
     * 展示验证信息
     * */
    public String dataVerifyHandler() {
        List<DataVerifyResultVO> dataVerifyResultVOS = dataVerifyResults.get();
        StringJoiner joiner = new StringJoiner("、");
        if (CollectionUtil.isNotEmpty(dataVerifyResultVOS)) {
            dataVerifyResultVOS.stream().collect(Collectors.groupingBy(sheetNameFunc)).forEach((sheetName, sameSheetList)-> {
                sameSheetList.stream().min(Comparator.comparing(rowIndexFunc)).ifPresent(dataVerifyResultVO -> {
                    List<String> keyList = new ArrayList<>(dataVerifyResultVO.getErrorMessageList().keySet());
                    if (CollectionUtil.isNotEmpty(keyList)) {
                        String key = keyList.get(0);
                        String value = dataVerifyResultVO.getErrorMessageList().get(key);
                        String msg = sheetName + "有" + key + value;
                        joiner.add(msg);
                    }

                });
            });
        }
        dataVerifyResults.remove();
        return joiner.toString();
    }

}

