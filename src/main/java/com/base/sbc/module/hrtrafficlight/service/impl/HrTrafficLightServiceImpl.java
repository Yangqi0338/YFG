package com.base.sbc.module.hrtrafficlight.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.client.amc.entity.User;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.module.hrtrafficlight.dto.AddOrUpdateHrTrafficLightDTO;
import com.base.sbc.module.hrtrafficlight.dto.HrTrafficLightDetailDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightVersionsDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightsDTO;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLight;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLightData;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLightVersion;
import com.base.sbc.module.hrtrafficlight.enums.HrTrafficLightVersionTypeEnum;
import com.base.sbc.module.hrtrafficlight.mapper.HrTrafficLightMapper;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightDataService;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightService;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightVersionService;
import com.base.sbc.module.hrtrafficlight.vo.HrTrafficLightsDetailVO;
import com.base.sbc.module.hrtrafficlight.vo.ListHrTrafficLightsVO;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;
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
    private DataPermissionsService dataPermissionsService;
    private RedisUtils redisUtils;

    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrUpdateHrTrafficLight(AddOrUpdateHrTrafficLightDTO addOrUpdateHrTrafficLightDTO) {
        HrTrafficLight hrTrafficLight = BeanUtil.copyProperties(addOrUpdateHrTrafficLightDTO, HrTrafficLight.class);
        String id = hrTrafficLight.getId();
        if (ObjectUtil.isNotEmpty(id) && "1".equals(addOrUpdateHrTrafficLightDTO.getDelFlag())) {
            // 删除主表
            removeById(id);
            // 删除版本表
            LambdaQueryWrapper<HrTrafficLightVersion> hrTrafficLightVersionQueryWrapper = new LambdaQueryWrapper<>();
            hrTrafficLightVersionQueryWrapper.eq(HrTrafficLightVersion::getHrTrafficLightId, id);
            List<HrTrafficLightVersion> hrTrafficLightVersionList = hrTrafficLightVersionService.list(hrTrafficLightVersionQueryWrapper);
            if (ObjectUtil.isNotEmpty(hrTrafficLightVersionList)) {
                List<String> versionIdList = hrTrafficLightVersionList.stream().map(HrTrafficLightVersion::getId).collect(Collectors.toList());
                hrTrafficLightVersionService.removeBatchByIds(versionIdList);

                // 删除数据表
                LambdaQueryWrapper<HrTrafficLightData> hrTrafficLightDataQueryWrapper = new LambdaQueryWrapper<>();
                hrTrafficLightDataQueryWrapper.in(HrTrafficLightData::getHrTrafficLightVersionId, versionIdList);
                hrTrafficLightDataService.remove(hrTrafficLightDataQueryWrapper);
            }
            return;
        }
        try {
            saveOrUpdate(hrTrafficLight);
        } catch (DuplicateKeyException e) {
            throw new OtherException("相同年份下名称不能重复！");
        }
    }

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
        hrTrafficLightQueryWrapper.eq(
                ObjectUtil.isNotEmpty(listHrTrafficLightsDTO.getDisableFlag()),
                HrTrafficLight::getDisableFlag,
                listHrTrafficLightsDTO.getDisableFlag());
        List<HrTrafficLight> hrTrafficLightList = list(hrTrafficLightQueryWrapper);
        return BeanUtil.copyToList(hrTrafficLightList, ListHrTrafficLightsVO.class);
    }

    @Override
    public List<HrTrafficLightVersion> listHrTrafficLightVersions(ListHrTrafficLightVersionsDTO listHrTrafficLightVersionsDTO) {
        LambdaQueryWrapper<HrTrafficLightVersion> hrTrafficLightVersionQueryWrapper = new LambdaQueryWrapper<>();
        hrTrafficLightVersionQueryWrapper.eq(HrTrafficLightVersion::getHrTrafficLightId, listHrTrafficLightVersionsDTO.getHrTrafficLightId());
        hrTrafficLightVersionQueryWrapper.eq(HrTrafficLightVersion::getType, listHrTrafficLightVersionsDTO.getType());
        hrTrafficLightVersionQueryWrapper.orderByDesc(HrTrafficLightVersion::getCreateDate);
        return hrTrafficLightVersionService.list(hrTrafficLightVersionQueryWrapper);
    }

    @Override
    public HrTrafficLightsDetailVO getHrTrafficLightDetail(HrTrafficLightDetailDTO hrTrafficLightDetailDTO) {
        HrTrafficLightVersion hrTrafficLightVersion = hrTrafficLightVersionService.getById(hrTrafficLightDetailDTO.getHrTrafficLightVersionId());
        if (ObjectUtil.isEmpty(hrTrafficLightVersion)) {
            throw new OtherException("当前版本数据不存在，请刷新后重试");
        }
        // 双层表头的类型
        List<Integer> twoHeadType = CollUtil.newArrayList(3, 12, 13, 14);
        HrTrafficLightsDetailVO hrTrafficLightsDetailVO = new HrTrafficLightsDetailVO();
        QueryWrapper<HrTrafficLightData> hrTrafficLightDataQueryWrapper = new QueryWrapper<>();
        hrTrafficLightDataQueryWrapper.eq("hr_traffic_light_version_id", hrTrafficLightDetailDTO.getHrTrafficLightVersionId());
        hrTrafficLightDataQueryWrapper.like(
                ObjectUtil.isNotEmpty(hrTrafficLightDetailDTO.getSearch()),
                "column_value",
                hrTrafficLightDetailDTO.getSearch());
        hrTrafficLightDataQueryWrapper.eq(
                ObjectUtil.isNotEmpty(hrTrafficLightDetailDTO.getUsername()),
                "username",
                hrTrafficLightDetailDTO.getUsername());

        dataPermissionsService.getDataPermissionsForQw(hrTrafficLightDataQueryWrapper, DataPermissionsBusinessTypeEnum.hrTrafficLight.getK());

        // 查询数据
        List<HrTrafficLightData> dataList = hrTrafficLightDataService.list(hrTrafficLightDataQueryWrapper);
        // 格式化数据集合
        List<Map<String, Map<String, String>>> dataMapList = new ArrayList<>(dataList.size());
        if (ObjectUtil.isNotEmpty(dataList)) {
            Map<Integer, List<HrTrafficLightData>> map = dataList
                    .stream().collect(Collectors.groupingBy(HrTrafficLightData::getRowIdx, LinkedHashMap::new, Collectors.toList()));
            for (List<HrTrafficLightData> value : map.values()) {
                Map<String, Map<String, String>> dataMap = new LinkedHashMap<>();
                for (HrTrafficLightData hrTrafficLightData : value) {
                    String columnNameOne = hrTrafficLightData.getColumnNameOne();
                    String columnNameTwo = hrTrafficLightData.getColumnNameTwo();
                    String columnValue = hrTrafficLightData.getColumnValue();
                    String color = hrTrafficLightData.getColor();
                    String backColor = hrTrafficLightData.getBackColor();
                    Map<String, String> dataItemMap = new LinkedHashMap<>();
                    dataItemMap.put("value", ObjectUtil.isNotEmpty(columnValue) ? columnValue : "");
                    dataItemMap.put("color", ObjectUtil.isNotEmpty(color) ? color : "");
                    dataItemMap.put("backColor", ObjectUtil.isNotEmpty(backColor) ? backColor : "");
                    if (twoHeadType.contains(hrTrafficLightVersion.getType())) {
                        // 二级表头
                        dataMap.put(columnNameOne + "-"
                                + columnNameTwo, dataItemMap);
                    } else {
                        // 一级表头
                        dataMap.put(columnNameOne, dataItemMap);
                    }
                }
                dataMapList.add(dataMap);
            }
        }
        hrTrafficLightsDetailVO.setDataList(dataMapList);

        // 查询表头
        LambdaQueryWrapper<HrTrafficLightData> hrTrafficLightDataLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrTrafficLightDataLambdaQueryWrapper.eq(HrTrafficLightData::getHrTrafficLightVersionId, hrTrafficLightDetailDTO.getHrTrafficLightVersionId());
        hrTrafficLightDataLambdaQueryWrapper.groupBy(HrTrafficLightData::getColumnNameOne, HrTrafficLightData::getColumnNameTwo);
        hrTrafficLightDataLambdaQueryWrapper.select(
                HrTrafficLightData::getId,
                HrTrafficLightData::getColumnNameOne,
                HrTrafficLightData::getColumnNameTwo,
                HrTrafficLightData::getOneHeadColor,
                HrTrafficLightData::getOneHeadBackColor,
                HrTrafficLightData::getTwoHeadColor,
                HrTrafficLightData::getTwoHeadBackColor
        );
        List<HrTrafficLightData> headList = hrTrafficLightDataService.list(hrTrafficLightDataLambdaQueryWrapper);
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
                oneHeadhMap.put("color", value.get(0).getOneHeadColor());
                oneHeadhMap.put("backColor", value.get(0).getOneHeadBackColor());
                if (twoHeadType.contains(hrTrafficLightVersion.getType())) {
                    // 二级表头
                    List<Map<String, Object>> twoHeadList = new ArrayList<>();
                    for (HrTrafficLightData hrTrafficLightData : value) {
                        Map<String, Object> twoHeadMap = new LinkedHashMap<>();
                        twoHeadMap.put("title", hrTrafficLightData.getColumnNameTwo());
                        twoHeadMap.put("color", hrTrafficLightData.getTwoHeadColor());
                        twoHeadMap.put("backColor", hrTrafficLightData.getTwoHeadBackColor());
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(MultipartFile file, String hrTrafficLightId, Integer trafficLightVersionType) {
        if (ObjectUtil.isEmpty(file)) {
            throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
        }
        // 双层表头的类型
        List<Integer> twoHeadType = CollUtil.newArrayList(3, 12, 13, 14);
        if (ObjectUtil.isNotEmpty(trafficLightVersionType)) {
            // 根据类型导入
            // 读取一个 sheet
            long time = new Date().getTime();
            SpringUtil.getBean(IHrTrafficLightService.class).readExcel(
                    file,
                    hrTrafficLightId,
                    trafficLightVersionType,
                    HrTrafficLightVersionTypeEnum.getValueByCode(trafficLightVersionType),
                    time,
                    twoHeadType);
        } else {
            // 导入所有类型
            // 读取所有类型的 sheet
            long time = new Date().getTime();
            for (HrTrafficLightVersionTypeEnum value : HrTrafficLightVersionTypeEnum.values()) {
                SpringUtil.getBean(IHrTrafficLightService.class).readExcel(
                        file,
                        hrTrafficLightId,
                        value.getCode(),
                        value.getValue(),
                        time++,
                        twoHeadType);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void readExcel(MultipartFile file,
                                       String hrTrafficLightId,
                                       Integer trafficLightVersionType,
                                       String sheetNoName,
                                       Long time,
                                       List<Integer> twoHeadType) {
        // 根据 sheet 名称生成版本号
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String version = "RSHLD" + simpleDateFormat.format(new Date());
        Object no = redisUtils.get("HrTrafficLight:" + sheetNoName + ":" + version);

        if (ObjectUtil.isEmpty(no)) {
            redisUtils.set("HrTrafficLight:" + sheetNoName + ":" + version, 1, 60 * 60 * 25);
            version = version + "01";
        } else {
            Integer intNo = Integer.valueOf(String.valueOf(no));
            intNo++;
            redisUtils.set("HrTrafficLight:" + sheetNoName + ":" + version, intNo, 60 * 60 * 25);
            version = version + String.format("%02d", intNo);
        }
        // 初始化表格数据
        List<Map<Integer, Map<String, String>>> cachedDataList = new ArrayList<>();
        try {
            // 开始读取 Excel 数据
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetNoName); // 获取第一个工作表
            if (ObjectUtil.isEmpty(sheet)) {
                return;
            }
            DataFormatter formatter = new DataFormatter(); // 创建 DataFormatter
            FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook); // 创建 FormulaEvaluator

            for (Row row : sheet) {
                Map<Integer, Map<String, String>> dataMap = new LinkedHashMap<>();
                for (Cell cell : row) {
                    String cellValue = getCellFormattedValue(cell, formatter, evaluator);
                    Map<String, String> map = new LinkedHashMap<>();
                    // 如果当前字体 有颜色 则保存颜色信息
                    if (ObjectUtil.isNotEmpty(cellValue)) {
                        CellStyle cellStyle = cell.getCellStyle();
                        XSSFColor backColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
                        XSSFFont fontAt = workbook.getFontAt(cellStyle.getFontIndexAsInt());
                        XSSFColor fontColor = fontAt.getXSSFColor();
                        String fontColorRgb = null;
                        String backColorRgb = null;
                        // 过滤黑颜色
                        if (ObjectUtil.isNotEmpty(fontColor)) {
                            fontColorRgb = "#" + fontColor.getARGBHex().substring(2, 8);
                            fontColorRgb = "#000000".equals(fontColorRgb) ? null : fontColorRgb;
                        }
                        if (ObjectUtil.isNotEmpty(backColor)) {
                            backColorRgb = "#" + backColor.getARGBHex().substring(2, 8);
                        }
                        map.put("value", cellValue);
                        map.put("fontColor", fontColorRgb);
                        map.put("backColor", backColorRgb);
                        dataMap.put(cell.getColumnIndex(), map);
                    } else {
                        map.put("value", null);
                        map.put("fontColor", null);
                        map.put("backColor", null);
                        dataMap.put(cell.getColumnIndex(), map);
                    }
                }
                cachedDataList.add(dataMap);
            }
        } catch (IOException e) {
            log.error("人事红绿灯导入出错", e);
            throw new OtherException(e.getMessage());
        }
        // 没有拿到数据也返回异常
        if (ObjectUtil.isEmpty(cachedDataList)) {
            throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
        }
        boolean contains = twoHeadType.contains(trafficLightVersionType);
        if (contains) {
            // 双层表头 双层表头的 行数必须大于等于 3 否则视为空
            if (cachedDataList.size() < 3) {
                throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
            }
        } else {
            // 单层表头 单层表头的 行数必须大于等于 2 否则视为空
            if (cachedDataList.size() < 2) {
                throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
            }
        }

        // 获取人事红绿灯主表数据
        HrTrafficLight hrTrafficLight = getById(hrTrafficLightId);
        if (ObjectUtil.isEmpty(hrTrafficLight)) {
            throw new OtherException("数据不存在，请刷新后重新导入");
        }

        // 先生成版本
        HrTrafficLightVersion hrTrafficLightVersion = new HrTrafficLightVersion();
        hrTrafficLightVersion.setHrTrafficLightId(hrTrafficLightId);
        hrTrafficLightVersion.setType(trafficLightVersionType);
        hrTrafficLightVersion.setVersion(version);
        hrTrafficLightVersion.setHrTrafficLightId(hrTrafficLightId);
        try {
            hrTrafficLightVersionService.save(hrTrafficLightVersion);
        } catch (Exception e) {
            log.error("人事红绿灯导入出错", e);
            throw new OtherException("网络波动，读取异常，请重新导入");
        }

        // 获取所有工号信息
        List<User> users = amcService.allUsers();
        List<String> usernameList = users.stream().map(User::getUsername).collect(Collectors.toList());
        // 获取部门-品牌信息
        Map<String, Map<String, String>> brandMaps = ccmFeignService.getDictInfoToMap("C8_Brand");
        Map<String, String> brandMap = brandMaps.get("C8_Brand");
        if (ObjectUtil.isEmpty(brandMap)) {
            throw new OtherException("请维护字典【品牌（C8_Brand）】");
        }
        Map<String, Map<String, String>> deptBrandMaps = ccmFeignService.getDictInfoToMap("dept_brand");
        Map<String, String> deptBrandMap = deptBrandMaps.get("dept_brand");
        if (ObjectUtil.isEmpty(deptBrandMap)) {
            throw new OtherException("请维护字典【部门-品牌（dept_brand）】");
        }
        Collection<String> deptBrandList = deptBrandMap.keySet();

        // 再生成人事红红绿灯数据
        List<HrTrafficLightData> hrTrafficLightDataList = new ArrayList<>(cachedDataList.size() - 1);
        // 拿到一级表头
        Map<Integer, Map<String, String>> oneHeadMap = cachedDataList.get(0);
        // 拿到二级表头 不一定是表头 如果当前是双层表头 那么 这个就是二级表头 如果不是 那么就是数据 不用管
        Map<Integer, Map<String, String>> twoHeadMap = cachedDataList.get(1);
        // 根据一级表头拿列的索引
        Set<Integer> idxSet = oneHeadMap.keySet();
        // 初始化部门的索引列位置
        Integer deptIdx = null;
        // 初始化工号的索引列位置
        Integer usernameIdx = null;

        // 拿到表头
        List<Map<String, String>> headList = CollUtil.newArrayList(contains ? twoHeadMap.values() : oneHeadMap.values());

        for (int head = 0; head < headList.size(); head++) {
            Map<String, String> headMap = headList.get(head);
            String data = headMap.get("value");
            if ("部门".equals(data)) {
                deptIdx = head;
            }
            if ("工号".equals(data)) {
                usernameIdx = head;
            }
        }

        if (ObjectUtil.isEmpty(deptIdx)) {
            throw new OtherException(sheetNoName + "sheet页部门不能为空");
        }
        if (!sheetNoName.equals(HrTrafficLightVersionTypeEnum.BM.getValue()) && ObjectUtil.isEmpty(usernameIdx)) {
            throw new OtherException(sheetNoName + "sheet页工号不能为空");
        }
        for (int i = (contains ? 2 : 1); i < cachedDataList.size(); i++) {
            Map<Integer, Map<String, String>> dataMap = cachedDataList.get(i);

            String username = "";
            // 获取工号
            if (!sheetNoName.equals(HrTrafficLightVersionTypeEnum.BM.getValue())) {
                Map<String, String> usernameDataMap = dataMap.get(usernameIdx);
                if (ObjectUtil.isEmpty(usernameDataMap)) {
                    throw new OtherException(sheetNoName + "sheet页工号不能为空");
                }
                username = usernameDataMap.get("value");
                if (ObjectUtil.isEmpty(username)) {
                    throw new OtherException(sheetNoName + "sheet页工号不能为空");
                }
                if (!usernameList.contains(username)) {
                    throw new OtherException(sheetNoName + "sheet页【" + username + "】" + "工号不存在");
                }
            }

            // 获取部门
            Map<String, String> deptDataMap = dataMap.get(deptIdx);
            if (ObjectUtil.isEmpty(deptDataMap)) {
                throw new OtherException(sheetNoName + "sheet页部门不能为空");
            }
            String deptName = deptDataMap.get("value");
            if (ObjectUtil.isEmpty(deptName)) {
                throw new OtherException(sheetNoName + "sheet页部门不能为空");
            }
            if (!deptBrandList.contains(deptName)) {
                throw new OtherException(sheetNoName + "sheet页【" + deptName + "】部门不存在字典【部门-品牌（dept_brand）】中，请维护后重新导入");
            }
            String oneHeadTemp = "";
            String oneHeadFontColorTemp = "";
            String oneHeadBackColorTemp = "";
            for (Integer idx : idxSet) {
                // 生成认识红绿灯数据集合
                HrTrafficLightData hrTrafficLightData = new HrTrafficLightData();
                hrTrafficLightData.setHrTrafficLightVersionId(hrTrafficLightVersion.getId());
                hrTrafficLightData.setRowIdx(i);
                Map<String, String> inOneHeadMap = oneHeadMap.get(idx);
                String oneHead = inOneHeadMap.get("value");
                String oneHeadFontColor = inOneHeadMap.get("fontColor");
                String oneHeadBackColor = inOneHeadMap.get("backColor");
                Map<String, String> inTwoHeadMap = twoHeadMap.get(idx);
                String twoHead = inTwoHeadMap.get("value");
                String twoHeadFontColor = inTwoHeadMap.get("fontColor");
                String twoHeadBackColor = inTwoHeadMap.get("backColor");
                Map<String, String> inDataMap = dataMap.get(idx);
                String value = "";
                String fontColor = "";
                String backColor = "";
                if (ObjectUtil.isNotEmpty(inDataMap)) {
                    value = inDataMap.get("value");
                    fontColor = inDataMap.get("fontColor");
                    backColor = inDataMap.get("backColor");
                }
                if (StrUtil.isBlank(oneHead)) {
                    oneHead = oneHeadTemp;
                    oneHeadFontColor = oneHeadFontColorTemp;
                    oneHeadBackColor = oneHeadBackColorTemp;
                } else {
                    oneHeadTemp = oneHead;
                    oneHeadFontColorTemp = oneHeadFontColor;
                    oneHeadBackColorTemp = oneHeadBackColor;
                }
                hrTrafficLightData.setColumnNameOne(oneHead);
                if (contains) {
                    hrTrafficLightData.setColumnNameTwo(twoHead);
                    hrTrafficLightData.setTwoHeadColor(twoHeadFontColor);
                    hrTrafficLightData.setTwoHeadBackColor(twoHeadBackColor);
                }
                hrTrafficLightData.setColumnValue(value);
                hrTrafficLightData.setOneHeadColor(oneHeadFontColor);
                hrTrafficLightData.setOneHeadBackColor(oneHeadBackColor);
                hrTrafficLightData.setColor(fontColor);
                hrTrafficLightData.setBackColor(backColor);
                String brandName = deptBrandMap.get(deptName);
                hrTrafficLightData.setBrandName(brandName);
                for (Map.Entry<String, String> item : brandMap.entrySet()) {
                    if (brandName.equals(item.getValue())) {
                        hrTrafficLightData.setBrandCode(item.getKey());
                    }
                }
                hrTrafficLightData.setUsername(username);
                hrTrafficLightDataList.add(hrTrafficLightData);
            }
        }
        hrTrafficLightDataService.saveBatch(hrTrafficLightDataList);
    }

    private static String getCellFormattedValue(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        switch (cell.getCellType()) {
            case FORMULA:
                // 评估公式并获取结果
                CellValue cellValue = evaluator.evaluate(cell);
                return cellValue.formatAsString();
            default:
                // 使用 DataFormatter 格式化非公式单元格内容
                return formatter.formatCellValue(cell);
        }
    }
}
