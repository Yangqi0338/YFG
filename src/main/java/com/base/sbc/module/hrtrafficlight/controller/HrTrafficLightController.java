package com.base.sbc.module.hrtrafficlight.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.hrtrafficlight.dto.HrTrafficLightsDetailDTO;
import com.base.sbc.module.hrtrafficlight.dto.ImportExcelDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightsDTO;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightService;
import com.base.sbc.module.hrtrafficlight.vo.HrTrafficLightsDetailVO;
import com.base.sbc.module.hrtrafficlight.vo.ListHrTrafficLightsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 人事红绿灯控制层
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Api(tags = "人事红绿灯")
@AllArgsConstructor
@RestController
@RequestMapping("/hrTrafficLight")
public class HrTrafficLightController {

    private IHrTrafficLightService hrTrafficLightService;

    /**
     * 人事红绿灯列表查询
     * @param listHrTrafficLightsDTO 查询条件
     * @return 人事红绿灯列表
     */
    @ApiOperation(value = "人事红绿灯列表查询")
    @GetMapping("/listHrTrafficLights")
    public ApiResult<List<ListHrTrafficLightsVO>> listHrTrafficLights(ListHrTrafficLightsDTO listHrTrafficLightsDTO) {
        return ApiResult.success("查询成功", hrTrafficLightService.listHrTrafficLights(listHrTrafficLightsDTO));
    }

    /**
     * 人事红绿灯详情查询
     * @param hrTrafficLightsDetailDTO 查询条件
     * @return 人事红绿灯详情
     */
    @ApiOperation(value = "人事红绿灯详情查询")
    @GetMapping("/getHrTrafficLightsDetail")
    public ApiResult<List<HrTrafficLightsDetailVO>> getHrTrafficLightsDetail(HrTrafficLightsDetailDTO hrTrafficLightsDetailDTO) {
        return ApiResult.success("查询成功", hrTrafficLightService.getHrTrafficLightsDetail(hrTrafficLightsDetailDTO));
    }

    /**
     * 人事红绿灯导入
     * @param file 文件
     * @param trafficLightVersionType 类型
     */
    @ApiOperation(value = "人事红绿灯详情查询")
    @GetMapping("/importExcel")
    public ApiResult<String> importExcel(@RequestParam("file") MultipartFile file,
                                         @RequestParam("hrTrafficLightId") String hrTrafficLightId,
                                         @RequestParam("trafficLightVersionType") Integer trafficLightVersionType) {
        hrTrafficLightService.importExcel(file,hrTrafficLightId, trafficLightVersionType);
        return ApiResult.success();
    }

}
