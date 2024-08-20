package com.base.sbc.module.hrtrafficlight.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.hrtrafficlight.dto.AddOrUpdateHrTrafficLightDTO;
import com.base.sbc.module.hrtrafficlight.dto.HrTrafficLightDetailDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightVersionsDTO;
import com.base.sbc.module.hrtrafficlight.dto.ListHrTrafficLightsDTO;
import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLightVersion;
import com.base.sbc.module.hrtrafficlight.service.IHrTrafficLightService;
import com.base.sbc.module.hrtrafficlight.vo.HrTrafficLightsDetailVO;
import com.base.sbc.module.hrtrafficlight.vo.ListHrTrafficLightsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
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
@RequestMapping(value = BaseController.SAAS_URL + "/hrTrafficLight", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HrTrafficLightController {

    private IHrTrafficLightService hrTrafficLightService;

    /**
     * 新增/更新/删除人事红绿灯
     *
     * @param addOrUpdateHrTrafficLightDTO 新增/更新/删除数据
     */
    @ApiOperation(value = "新增/更新人事红绿灯")
    @GetMapping("/addOrUpdateHrTrafficLight")
    public ApiResult<String> addOrUpdateHrTrafficLight(AddOrUpdateHrTrafficLightDTO addOrUpdateHrTrafficLightDTO) {
        hrTrafficLightService.addOrUpdateHrTrafficLight(addOrUpdateHrTrafficLightDTO);
        return ApiResult.success("操作成功");
    }


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
     * 人事红绿灯版本列表查询
     *
     * @param listHrTrafficLightVersionsDTO 查询条件
     * @return 人事红绿灯版本列表
     */
    @ApiOperation(value = "人事红绿灯版本列表查询")
    @GetMapping("/listHrTrafficLightVersions")
    public ApiResult<List<HrTrafficLightVersion>> listHrTrafficLightVersions(ListHrTrafficLightVersionsDTO listHrTrafficLightVersionsDTO) {
        return ApiResult.success("查询成功", hrTrafficLightService.listHrTrafficLightVersions(listHrTrafficLightVersionsDTO));
    }


    /**
     * 人事红绿灯详情查询
     * @param hrTrafficLightDetailDTO 查询条件
     * @return 人事红绿灯详情
     */
    @ApiOperation(value = "人事红绿灯详情查询")
    @GetMapping("/getHrTrafficLightDetail")
    public ApiResult<HrTrafficLightsDetailVO> getHrTrafficLightDetail(HrTrafficLightDetailDTO hrTrafficLightDetailDTO) {
        return ApiResult.success("查询成功", hrTrafficLightService.getHrTrafficLightDetail(hrTrafficLightDetailDTO));
    }

    /**
     * 人事红绿灯导入
     * @param file 文件
     * @param trafficLightVersionType 类型
     */
    @ApiOperation(value = "人事红绿灯导入")
    @PostMapping("/importExcel")
    public ApiResult<String> importExcel(@RequestParam("file") MultipartFile file,
                                         @RequestParam("hrTrafficLightId") String hrTrafficLightId,
                                         @RequestParam(value = "trafficLightVersionType", required = false) Integer trafficLightVersionType) {
        hrTrafficLightService.importExcel(file,hrTrafficLightId, trafficLightVersionType);
        return ApiResult.success("导入成功");
    }

}
