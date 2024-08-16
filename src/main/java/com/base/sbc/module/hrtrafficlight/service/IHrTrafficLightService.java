package com.base.sbc.module.hrtrafficlight.service;

import com.base.sbc.module.hrtrafficlight.dto.AddOrUpdateHrTrafficLightDTO;
import com.base.sbc.module.hrtrafficlight.dto.HrTrafficLightDetailDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightVersionsDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightsDTO;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLight;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLightVersion;
import com.base.sbc.module.hrtrafficlight.vo.HrTrafficLightsDetailVO;
import com.base.sbc.module.hrtrafficlight.vo.ListHrTrafficLightVersionsVO;
import com.base.sbc.module.hrtrafficlight.vo.ListHrTrafficLightsVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 人事红绿灯接口
 *
 * @author XHTE
 * @create 2024-08-15
 */
public interface IHrTrafficLightService extends IService<HrTrafficLight> {

    /**
     * 新增/更新人事红绿灯
     *
     * @param addOrUpdateHrTrafficLightDTO 新增/更新数据
     */
    void addOrUpdateHrTrafficLight(AddOrUpdateHrTrafficLightDTO addOrUpdateHrTrafficLightDTO);

    /**
     * 人事红绿灯列表查询
     *
     * @param listHrTrafficLightsDTO 查询条件
     * @return 人事红绿灯列表
     */
    List<ListHrTrafficLightsVO> listHrTrafficLights(ListHrTrafficLightsDTO listHrTrafficLightsDTO);

    /**
     * 人事红绿灯版本列表查询
     *
     * @param listHrTrafficLightVersionsDTO 查询条件
     * @return 人事红绿灯版本列表
     */
    List<HrTrafficLightVersion> listHrTrafficLightVersions(ListHrTrafficLightVersionsDTO listHrTrafficLightVersionsDTO);

    /**
     * 人事红绿灯详情查询
     *
     * @param hrTrafficLightDetailDTO 查询条件
     * @return 人事红绿灯详情
     */
    HrTrafficLightsDetailVO getHrTrafficLightDetail(HrTrafficLightDetailDTO hrTrafficLightDetailDTO);

    /**
     * 人事红绿灯导入
     *
     * @param file 文件
     * @param trafficLightVersionType 类型
     */
    void importExcel(MultipartFile file, String hrTrafficLightId, Integer trafficLightVersionType);

    void readExcel(MultipartFile file,
                   String hrTrafficLightId,
                   Integer trafficLightVersionType,
                   String sheetNoName,
                   List<Integer> twoHeadType);
}
