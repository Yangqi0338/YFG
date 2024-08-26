package com.base.sbc.open.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumRangeDifferenceService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMeasurementVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumRangeDifferenceVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author XHTE
 * @create 2024-08-26
 */
@RestController
@Api(tags = "ESCM 公共接口")
@RequestMapping(value = BaseController.OPEN_URL + "/escmOpen", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@AllArgsConstructor
public class EscmOpenController {

    private BasicsdatumRangeDifferenceService basicsdatumRangeDifferenceService;
    private BasicsdatumMeasurementService basicsdatumMeasurementService;
    private MinioUtils minioUtils;
    @ApiOperation(value = "查询已启用的档差管理列表")
    @GetMapping("/listRangeDifference")
    public ApiResult<List<BasicsdatumRangeDifferenceVo>> listRangeDifference() {
        // 初始化返回数据
        List<BasicsdatumRangeDifferenceVo> basicsdatumRangeDifferenceVoList = null;
        LambdaQueryWrapper<BasicsdatumRangeDifference> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicsdatumRangeDifference::getStatus, "0");
        queryWrapper.orderByDesc(BasicsdatumRangeDifference::getCreateDate);
        List<BasicsdatumRangeDifference> basicsdatumRangeDifferences = basicsdatumRangeDifferenceService
                .list(queryWrapper);
        if (ObjectUtil.isNotEmpty(basicsdatumRangeDifferences)) {
            basicsdatumRangeDifferenceVoList = new ArrayList<>(basicsdatumRangeDifferences.size());
            for (BasicsdatumRangeDifference basicsdatumRangeDifference : basicsdatumRangeDifferences) {
                BasicsdatumRangeDifferenceVo basicsdatumRangeDifferenceVo =
                        basicsdatumRangeDifferenceService.getById(basicsdatumRangeDifference.getId());
                basicsdatumRangeDifferenceVoList.add(basicsdatumRangeDifferenceVo);
            }
        }
        return ApiResult.success("查询成功", basicsdatumRangeDifferenceVoList);
    }

    @ApiOperation(value = "查询已启用测量点列表")
    @GetMapping("/listMeasurement")
    public ApiResult<List<BasicsdatumMeasurement>> listMeasurement() {
        // 初始化返回数据
        LambdaQueryWrapper<BasicsdatumMeasurement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicsdatumMeasurement::getStatus, "1");
        queryWrapper.orderByDesc(BasicsdatumMeasurement::getCreateDate);
        List<BasicsdatumMeasurement> basicsdatumMeasurements = basicsdatumMeasurementService.list(queryWrapper);
        minioUtils.setObjectUrlToList(basicsdatumMeasurements, "image");
        return ApiResult.success("查询成功", basicsdatumMeasurements);
    }

}
