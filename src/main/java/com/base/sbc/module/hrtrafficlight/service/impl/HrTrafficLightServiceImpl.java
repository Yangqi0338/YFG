package com.base.sbc.module.hrtrafficlight.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.hrtrafficlight.dto.HrTrafficLightsDetailDTO;
import com.base.sbc.module.hrtrafficlight.dto.ImportExcelDTO;
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
import com.base.sbc.module.patternlibrary.constants.GeneralConstant;
import com.base.sbc.module.patternlibrary.constants.ResultConstant;
import com.base.sbc.module.patternlibrary.dto.ExcelImportDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryBrand;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.listener.PatterLibraryListener;
import com.base.sbc.module.style.entity.Style;
import javassist.Loader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public IHrTrafficLightVersionService hrTrafficLightVersionService;
    public IHrTrafficLightDataService hrTrafficLightDataService;

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
        hrTrafficLightQueryWrapper.eq(
                ObjectUtil.isNotEmpty(listHrTrafficLightsDTO.getHrYearCode()),
                HrTrafficLight::getHrYearCode,
                listHrTrafficLightsDTO.getHrYearCode());
        List<HrTrafficLight> hrTrafficLightList = list(hrTrafficLightQueryWrapper);
        return BeanUtil.copyToList(hrTrafficLightList, ListHrTrafficLightsVO.class);
    }

    /**
     * 人事红绿灯详情查询
     *
     * @param hrTrafficLightsDetailDTO 查询条件
     * @return 人事红绿灯详情
     */
    @Override
    public List<HrTrafficLightsDetailVO> getHrTrafficLightsDetail(HrTrafficLightsDetailDTO hrTrafficLightsDetailDTO) {
        HrTrafficLightVersion hrTrafficLightVersion = hrTrafficLightVersionService.getById(hrTrafficLightsDetailDTO.getHrTrafficLightVersionId());
        if (ObjectUtil.isEmpty(hrTrafficLightVersion)) {
            throw new OtherException("当前版本数据不存在，请刷新后重试");
        }

        LambdaQueryWrapper<HrTrafficLightData> hrTrafficLightDataQueryWrapper = new LambdaQueryWrapper<>();
        hrTrafficLightDataQueryWrapper.eq(HrTrafficLightData::getHrTrafficLightVersionId, hrTrafficLightsDetailDTO.getHrTrafficLightVersionId());
        List<HrTrafficLightData> hrTrafficLightDataList = hrTrafficLightDataService.list(hrTrafficLightDataQueryWrapper);

        return BeanUtil.copyToList(hrTrafficLightDataList, HrTrafficLightsDetailVO.class);
    }

    /**
     * 人事红绿灯导入
     *
     * @param file 文件
     * @param type 类型息
     */
    @Override
    public void importExcel(MultipartFile file, String hrTrafficLightId, Integer type) {
        if (ObjectUtil.isEmpty(file)) {
            throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
        }
        List<Integer> twoHeadType = CollUtil.newArrayList(3, 12, 13, 14);
        if (ObjectUtil.isNotEmpty(type)) {
            // 根据类型导入
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            HrTrafficLightListener hrTrafficLightListener = new HrTrafficLightListener();
            try {
                EasyExcel.read(file.getInputStream(), hrTrafficLightListener).headRowNumber(0).sheet().doRead();
            } catch (IOException e) {
                throw new OtherException(e.getMessage());
            }
            // 没有拿到数据也返回异常
            List<Map<Integer, String>> cachedDataList = hrTrafficLightListener.getCachedDataList();
            if (ObjectUtil.isEmpty(cachedDataList)) {
                throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
            }
            if (twoHeadType.contains(type)) {
                // 双层表头
                if (cachedDataList.size() < 3) {
                    throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
                }
            } else {
                // 单层表头
                if (cachedDataList.size() < 2) {
                    throw new OtherException(ResultConstant.IMPORT_DATA_NOT_EMPTY);
                }
                HrTrafficLight hrTrafficLight = getById(hrTrafficLightId);
                if (ObjectUtil.isEmpty(hrTrafficLight)) {
                    throw new OtherException("数据异常，请刷新后重新导入");
                }
                HrTrafficLightVersion hrTrafficLightVersion = new HrTrafficLightVersion();
                hrTrafficLightVersion.setHrTrafficLightId(hrTrafficLightId);
                hrTrafficLightVersion.setType(type);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                hrTrafficLightVersion.setVersion("RSHLD" + simpleDateFormat.format(new Date()));
                hrTrafficLightVersion.setHrTrafficLightId(hrTrafficLightId);
                try {
                    hrTrafficLightVersionService.save(hrTrafficLightVersion);
                } catch (Exception e) {
                    log.error("人事红绿灯导入出错", e);
                    throw new OtherException("导入出现错误，请重新导入");
                }

                List<HrTrafficLightData> hrTrafficLightDataList = new ArrayList<>(cachedDataList.size() - 1);
                Map<Integer, String> headMap = cachedDataList.get(0);
                Set<Integer> idxSet = headMap.keySet();
                for (int i = 1; i < cachedDataList.size(); i++) {
                    Map<Integer, String> dataMap = cachedDataList.get(i);
                    for (Integer idx : idxSet) {
                        HrTrafficLightData hrTrafficLightData = new HrTrafficLightData();
                        hrTrafficLightData.setHrTrafficLightVersionId(hrTrafficLightVersion.getId());
                        hrTrafficLightData.setRowIdx(i);
                        hrTrafficLightData.setColumnNameOne(headMap.get(idx));
                        hrTrafficLightData.setColumnNameTwo(hrTrafficLightVersion.getId());
                        hrTrafficLightData.setColumnNameTwo(dataMap.get(idx));
                        hrTrafficLightDataList.add(hrTrafficLightData);
                    }
                }
            }

        } else {
            // 导入所有类型

        }
    }


}
