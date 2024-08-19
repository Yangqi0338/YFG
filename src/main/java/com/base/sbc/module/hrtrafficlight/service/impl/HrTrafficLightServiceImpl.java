package com.base.sbc.module.hrtrafficlight.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.amc.entity.User;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.hrtrafficlight.dto.AddOrUpdateHrTrafficLightDTO;
import com.base.sbc.module.hrtrafficlight.dto.HrTrafficLightDetailDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightVersionsDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightsDTO;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLight;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLightData;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLightVersion;
import com.base.sbc.module.hrtrafficlight.enums.HrTrafficLightVersionTypeEnum;
import com.base.sbc.module.hrtrafficlight.listener.HrTrafficLightListener;
import com.base.sbc.module.hrtrafficlight.mapper.HrTrafficLightMapper;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightDataService;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightVersionService;
import com.base.sbc.module.hrtrafficlight.vo.HrTrafficLightsDetailVO;
import com.base.sbc.module.hrtrafficlight.vo.ListHrTrafficLightsVO;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 人事红绿灯服务实现层
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Service
@AllArgsConstructor
public class HrTrafficLightServiceImpl extends ServiceImpl<HrTrafficLightMapper, HrTrafficLight> implements IHrTrafficLightService {

    private IHrTrafficLightVersionService hrTrafficLightVersionService;
    private IHrTrafficLightDataService hrTrafficLightDataService;
    private CcmFeignService ccmFeignService;
    private AmcService amcService;

    /**
     * 新增/更新人事红绿灯
     *
     * @param addOrUpdateHrTrafficLightDTO 新增/更新数据
     */
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrUpdateHrTrafficLight(AddOrUpdateHrTrafficLightDTO addOrUpdateHrTrafficLightDTO) {
        HrTrafficLight hrTrafficLight = BeanUtil.copyProperties(addOrUpdateHrTrafficLightDTO, HrTrafficLight.class);
        try {
            saveOrUpdate(hrTrafficLight);
        } catch (DuplicateKeyException e) {
            throw new OtherException("相同年份下名称不能重复！");
        }
    }

    /**
     * 人事红绿灯列表查询
     *
     * @param listHrTrafficLightsDTO 查询条件
     * @return 人事红绿灯列表
     */
    @Override
    public List<ListHrTrafficLightsVO> listHrTrafficLights(ListHrTrafficLightsDTO listHrTrafficLightsDTO) {
        LambdaQueryWrapper<HrTrafficLight> hrTrafficLightQueryWrapper = new LambdaQueryWrapper<>();
        hrTrafficLightQueryWrapper.eq(
                ObjectUtil.isNotEmpty(listHrTrafficLightsDTO.getName()),
                HrTrafficLight::getName,
                listHrTrafficLightsDTO.getName());
        hrTrafficLightQueryWrapper.like(
                ObjectUtil.isNotEmpty(listHrTrafficLightsDTO.getHrYearCode()),
                HrTrafficLight::getHrYearCode,
                listHrTrafficLightsDTO.getHrYearCode());
        List<HrTrafficLight> hrTrafficLightList = list(hrTrafficLightQueryWrapper);
        return BeanUtil.copyToList(hrTrafficLightList, ListHrTrafficLightsVO.class);
    }

    /**
     * 人事红绿灯版本列表查询
     *
     * @param listHrTrafficLightVersionsDTO 查询条件
     * @return 人事红绿灯版本列表
     */
    @Override
    public List<HrTrafficLightVersion> listHrTrafficLightVersions(ListHrTrafficLightVersionsDTO listHrTrafficLightVersionsDTO) {
        LambdaQueryWrapper<HrTrafficLightVersion> hrTrafficLightVersionQueryWrapper = new LambdaQueryWrapper<>();
        hrTrafficLightVersionQueryWrapper.eq(HrTrafficLightVersion::getHrTrafficLightId, listHrTrafficLightVersionsDTO.getHrTrafficLightId());
        hrTrafficLightVersionQueryWrapper.eq(HrTrafficLightVersion::getType, listHrTrafficLightVersionsDTO.getType());
        return hrTrafficLightVersionService.list(hrTrafficLightVersionQueryWrapper);
    }

    /**
     * 人事红绿灯详情查询
     *
     * @param hrTrafficLightDetailDTO 查询条件
     * @return 人事红绿灯详情
     */
    @Override
    public HrTrafficLightsDetailVO getHrTrafficLightDetail(HrTrafficLightDetailDTO hrTrafficLightDetailDTO) {
        HrTrafficLightVersion hrTrafficLightVersion = hrTrafficLightVersionService.getById(hrTrafficLightDetailDTO.getHrTrafficLightVersionId());
        if (ObjectUtil.isEmpty(hrTrafficLightVersion)) {
            throw new OtherException("当前版本数据不存在，请刷新后重试");
        }
        // 双层表头的类型
        List<Integer> twoHeadType = CollUtil.newArrayList(3, 12, 13, 14);
        HrTrafficLightsDetailVO hrTrafficLightsDetailVO = new HrTrafficLightsDetailVO();
        LambdaQueryWrapper<HrTrafficLightData> hrTrafficLightDataQueryWrapper = new LambdaQueryWrapper<>();
        hrTrafficLightDataQueryWrapper.eq(HrTrafficLightData::getHrTrafficLightVersionId, hrTrafficLightDetailDTO.getHrTrafficLightVersionId());
        // 查询数据
        List<HrTrafficLightData> dataList = hrTrafficLightDataService.list(hrTrafficLightDataQueryWrapper);
        // 格式化数据集合
        List<Map<String, String>> dataMapList = new ArrayList<>(dataList.size());
        if (ObjectUtil.isNotEmpty(dataList)) {
            Map<Integer, List<HrTrafficLightData>> map = dataList
                    .stream().collect(Collectors.groupingBy(HrTrafficLightData::getRowIdx, LinkedHashMap::new, Collectors.toList()));
            for (List<HrTrafficLightData> value : map.values()) {
                Map<String, String> dataMap = new LinkedHashMap<>();
                for (HrTrafficLightData hrTrafficLightData : value) {
                    String columnNameOne = hrTrafficLightData.getColumnNameOne();
                    String columnNameTwo = hrTrafficLightData.getColumnNameTwo();
                    String columnValue = hrTrafficLightData.getColumnValue();
                    if (twoHeadType.contains(hrTrafficLightVersion.getType())) {
                        // 二级表头
                        dataMap.put(columnNameOne + "-"
                                + columnNameTwo, ObjectUtil.isNotEmpty(columnValue) ? columnValue : "");
                    } else {
                        // 一级表头
                        dataMap.put(columnNameOne, ObjectUtil.isNotEmpty(columnValue) ? columnValue : "");
                    }
                }
                dataMapList.add(dataMap);
            }
        }
        hrTrafficLightsDetailVO.setDataList(dataMapList);

        // 查询表头
        hrTrafficLightDataQueryWrapper.groupBy(HrTrafficLightData::getColumnNameOne, HrTrafficLightData::getColumnNameTwo);
        hrTrafficLightDataQueryWrapper.select(
                HrTrafficLightData::getId,
                HrTrafficLightData::getColumnNameOne,
                HrTrafficLightData::getColumnNameTwo);
        List<HrTrafficLightData> headList = hrTrafficLightDataService.list(hrTrafficLightDataQueryWrapper);
        // 格式化表头集合
        List<Map<String, Object>> headMapList = new ArrayList<>(headList.size());
        if (ObjectUtil.isNotEmpty(headList)) {
            Map<String, List<HrTrafficLightData>> map = headList
                    .stream().collect(Collectors.groupingBy(HrTrafficLightData::getColumnNameOne, LinkedHashMap::new, Collectors.toList()));
            for (Map.Entry<String, List<HrTrafficLightData>> item : map.entrySet()) {
                String key = item.getKey();
                List<HrTrafficLightData> value = item.getValue();
                Map<String, Object> oneHeadhMap = new LinkedHashMap<>();
                oneHeadhMap.put("title", key);

                if (twoHeadType.contains(hrTrafficLightVersion.getType())) {
                    // 二级表头
                    List<Map<String, Object>> twoHeadList = new ArrayList<>();
                    for (HrTrafficLightData hrTrafficLightData : value) {
                        Map<String, Object> twoHeadMap = new LinkedHashMap<>();
                        twoHeadMap.put("title", hrTrafficLightData.getColumnNameTwo());
                        twoHeadList.add(twoHeadMap);
                    }
                    oneHeadhMap.put("children", twoHeadList);
                } else {
                    // 一级表头
                    oneHeadhMap.put("children", "");
                }
                headMapList.add(oneHeadhMap);
            }
            hrTrafficLightsDetailVO.setHeadList(headMapList);
        }
        return hrTrafficLightsDetailVO;
    }

    /**
     * 人事红绿灯导入
     *
     * @param file                    文件
     * @param trafficLightVersionType 类型息
     */
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void importExcel(MultipartFile file, String hrTrafficLightId, Integer trafficLightVersionType) {
        if (ObjectUtil.isEmpty(file)) {
            throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
        }
        // 双层表头的类型
        List<Integer> twoHeadType = CollUtil.newArrayList(3, 12, 13, 14);
        if (ObjectUtil.isNotEmpty(trafficLightVersionType)) {
            // 根据类型导入
            // 读取一个 sheet
            SpringUtil.getBean(IHrTrafficLightService.class).readExcel(
                    file,
                    hrTrafficLightId,
                    trafficLightVersionType,
                    HrTrafficLightVersionTypeEnum.getValueByCode(trafficLightVersionType),
                    twoHeadType);
        } else {
            // 导入所有类型
            // 读取所有类型的 sheet
            for (HrTrafficLightVersionTypeEnum value : HrTrafficLightVersionTypeEnum.values()) {
                SpringUtil.getBean(IHrTrafficLightService.class).readExcel(
                        file,
                        hrTrafficLightId,
                        trafficLightVersionType,
                        value.getValue(),
                        twoHeadType);
            }
        }
    }

    /**
     * 生成人事红绿灯数据
     *
     * @param hrTrafficLightId        人事红绿灯主表 ID
     * @param trafficLightVersionType 人事红绿灯版本类型
     * @param file                    Excel 文件
     * @param twoHeadType             双层表头集合
     * @param sheetNoName             Excel sheetNo名称
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void readExcel(MultipartFile file,
                          String hrTrafficLightId,
                          Integer trafficLightVersionType,
                          String sheetNoName,
                          List<Integer> twoHeadType) {
        // 初始化表格数据
        List<Map<Integer, String>> cachedDataList = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetNoName); // 获取第一个工作表
            DataFormatter formatter = new DataFormatter(); // 创建 DataFormatter
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); // 创建 FormulaEvaluator

            for (Row row : sheet) {
                Map<Integer, String> dataMap = new LinkedHashMap<>();
                for (Cell cell : row) {
                    String cellValue = getCellFormattedValue(cell, formatter, evaluator);
                    Map<String, String> map = new HashMap<>();
                    if (ObjectUtil.isNotEmpty(cellValue)) {
                        CellStyle cellStyle = cell.getCellStyle();
                        XSSFFont fontAt = workbook.getFontAt(cellStyle.getFontIndexAsInt());
                        XSSFColor xssfColor = fontAt.getXSSFColor();
                        String colorRgb = ObjectUtil.isNotEmpty(xssfColor) ? xssfColor.getARGBHex().substring(2, 8) : "";
                        map.put("value", cellValue);
                        map.put("color", colorRgb);
                        dataMap.put(cell.getColumnIndex(), JSONUtil.toJsonStr(map));
                    } else {
                        map.put("value", null);
                        map.put("color", null);
                        dataMap.put(cell.getColumnIndex(), JSONUtil.toJsonStr(map));
                    }
                }
                cachedDataList.add(dataMap);
            }
        } catch (IOException e) {
            throw new OtherException(e.getMessage());
        }
        // 没有拿到数据也返回异常
        if (ObjectUtil.isEmpty(cachedDataList)) {
            throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
        }
        boolean contains = twoHeadType.contains(trafficLightVersionType);
        if (contains) {
            // 双层表头
            if (cachedDataList.size() < 3) {
                throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
            }
        } else {
            // 单层表头
            if (cachedDataList.size() < 2) {
                throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
            }
        }
        HrTrafficLight hrTrafficLight = getById(hrTrafficLightId);
        if (ObjectUtil.isEmpty(hrTrafficLight)) {
            throw new OtherException("数据不存在，请刷新后重新导入");
        }

        // 先生成版本
        HrTrafficLightVersion hrTrafficLightVersion = new HrTrafficLightVersion();
        hrTrafficLightVersion.setHrTrafficLightId(hrTrafficLightId);
        hrTrafficLightVersion.setType(trafficLightVersionType);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        hrTrafficLightVersion.setVersion("RSHLD" + simpleDateFormat.format(new Date()));
        hrTrafficLightVersion.setHrTrafficLightId(hrTrafficLightId);
        try {
            hrTrafficLightVersionService.save(hrTrafficLightVersion);
        } catch (Exception e) {
            log.error("人事红绿灯导入出错", e);
            throw new OtherException("网络波动，读取异常，请重新导入");
        }

        // 对数据进行校验
        // 判断工号是否存在
        List<User> users = amcService.allUsers();
        List<String> usernameList = users.stream().map(User::getUsername).collect(Collectors.toList());
        // 判断品牌是否存在
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Brand");
        Map<String, String> brandMap = dictInfoToMap.get("C8_Brand");
        Collection<String> brandValueList = brandMap.values();

        // 再生成人事红红绿灯数据
        List<HrTrafficLightData> hrTrafficLightDataList = new ArrayList<>(cachedDataList.size() - 1);
        Map<Integer, String> oneHeadMap = cachedDataList.get(0);
        Map<Integer, String> twoHeadMap = cachedDataList.get(1);
        Set<Integer> idxSet = oneHeadMap.keySet();
        Integer brandIdx = null;
        Integer usernameIdx = null;
        for (Map.Entry<Integer, String> item : contains ? twoHeadMap.entrySet() : oneHeadMap.entrySet()) {
            JSONObject dataJson = JSONUtil.parseObj(item);
            String data = dataJson.getStr("value");
            if (data.equals("品牌")) {
                brandIdx = item.getKey();
            }
            if (data.equals("工号")) {
                usernameIdx = item.getKey();
            }
        }
        if (ObjectUtil.isEmpty(brandIdx)) {
            throw new OtherException(sheetNoName + "品牌不能为空");
        }
        if (ObjectUtil.isEmpty(usernameIdx)) {
            throw new OtherException(sheetNoName + "工号不能为空");
        }
        for (int i = (contains ? 2 : 1); i < cachedDataList.size(); i++) {
            Map<Integer, String> dataMap = cachedDataList.get(i);

            String username = dataMap.get(usernameIdx);
            if (!usernameList.contains(username)) {
                throw new OtherException(sheetNoName + "【" + username + "】" + "工号不存在");
            }
            String oneHeadTemp = "";
            for (Integer idx : idxSet) {
                HrTrafficLightData hrTrafficLightData = new HrTrafficLightData();
                hrTrafficLightData.setHrTrafficLightVersionId(hrTrafficLightVersion.getId());
                hrTrafficLightData.setRowIdx(i);
                JSONObject oneHeadJson = JSONUtil.parseObj(oneHeadMap.get(idx));
                String oneHead = oneHeadJson.getStr("value");
                JSONObject twoHeadJson = JSONUtil.parseObj(twoHeadMap.get(idx));
                String twoHead = twoHeadJson.getStr("value");
                JSONObject dataJson = JSONUtil.parseObj(dataMap.get(idx));
                String data = dataJson.getStr("value");
                String color = dataJson.getStr("color");
                if (StrUtil.isBlank(oneHead)) {
                    oneHead = oneHeadTemp;
                } else {
                    oneHeadTemp = oneHead;
                }
                hrTrafficLightData.setColumnNameOne(oneHead);
                if (contains) {
                    hrTrafficLightData.setColumnNameTwo(twoHead);
                }
                hrTrafficLightData.setColumnValue(data);
                hrTrafficLightData.setColor(color);
                String brandName = dataMap.get(brandIdx);
                if (ObjectUtil.isEmpty(brandName)) {
                    throw new OtherException(sheetNoName + "表品牌不能为空");
                }
                if (!brandValueList.contains(brandName)) {
                    hrTrafficLightData.setBrandName(brandName);
                } else {
                    throw new OtherException(sheetNoName + "表【" + brandName + "】品牌不存在");
                }
                hrTrafficLightDataList.add(hrTrafficLightData);
            }
        }
        hrTrafficLightDataService.saveBatch(hrTrafficLightDataList);
    }

    private static String getCellFormattedValue(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        switch (cell.getCellType()) {
            case FORMULA:
                // 评估公式并获取结果
                // CellValue cellValue = evaluator.evaluate(cell);
                // return formatter.formatCellValue(cell, evaluator);
            default:
                // 使用 DataFormatter 格式化非公式单元格内容
                return formatter.formatCellValue(cell);
        }
    }
}
