package com.base.sbc.module.hrtrafficlight.vo;

import com.base.sbc.module.hrtrafficlight.entity.HrTrafficLightData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人事红绿灯详情返回值
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Data
@ApiModel(value = "人事红绿灯详情返回值", description = "人事红绿灯详情返回值")
public class ListHrTrafficLightVersionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表头列表
     */
    @ApiModelProperty("表头列表")
    private List<HrTrafficLightData> headList;

    /**
     * 数据列表
     */
    @ApiModelProperty("数据列表")
    private List<HrTrafficLightData> dataList;
}
