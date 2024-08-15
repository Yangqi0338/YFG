package com.base.sbc.module.hrtrafficlight.service;

import com.base.sbc.module.hrtrafficlight.dto.HrTrafficLightsDetailDTO;
import com.base.sbc.module.hrtrafficlight.dto.ImportExcelDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightsDTO;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLight;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.hrtrafficlight.vo.HrTrafficLightsDetailVO;
import com.base.sbc.module.hrtrafficlight.vo.ListHrTrafficLightsVO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 人事红绿灯接口
 *
 * @author XHTE
 * @create 2024-08-15
 */
public interface IHrTrafficLightService extends IService<HrTrafficLight> {

    /**
     * 人事红绿灯列表查询
     *
     * @param listHrTrafficLightsDTO 查询条件
     * @return 人事红绿灯列表
     */
    List<ListHrTrafficLightsVO> listHrTrafficLights(ListHrTrafficLightsDTO listHrTrafficLightsDTO);

    /**
     * 人事红绿灯详情查询
     *
     * @param hrTrafficLightsDetailDTO 查询条件
     * @return 人事红绿灯详情
     */
    List<HrTrafficLightsDetailVO> getHrTrafficLightsDetail(HrTrafficLightsDetailDTO hrTrafficLightsDetailDTO);

    /**
     * 人事红绿灯导入
     *
     * @param file 文件
     * @param trafficLightVersionType 类型
     */
    void importExcel(MultipartFile file, String hrTrafficLightId, Integer trafficLightVersionType);

}
