package com.base.sbc.module.hrtrafficlight.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 人事红绿灯版本实体类
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Getter
@Setter
@TableName("t_hr_traffic_light_version")
@ApiModel(value = "人事红绿灯版本实体类", description = "人事红绿灯版本实体类")
public class HrTrafficLightVersion extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人事红绿灯主表 ID
     */
    @ApiModelProperty("人事红绿灯主表 ID")
    private String hrTrafficLightId;

    /**
     * 类型
     * 1-部门
     * 2-个人
     * 3-个人详情-下单成功率
     * 4-个人详情-单款平均样衣件数
     * 5-个人详情-TOP款次数
     * 6-个人详情-明细单准确率
     * 7-个人详情-面料详单准确率
     * 8个人详情-面辅料齐套率
     * 9个人详情-样衣参考率
     * 10-个人详情-下店市调完成率
     * 11-个人详情-产销率 SA款
     * 12-月均出稿数（cmt+fob）
     * 13-月均下单数SKC（cmt+fob）
     * 14-月均下单数SKU（cmt+fob）
     */
    @ApiModelProperty("类型" +
            "1-部门 " +
            "2-个人 " +
            "3-个人详情-下单成功率 " +
            "4-个人详情-单款平均样衣件数 " +
            "5-个人详情-TOP款次数 " +
            "6-个人详情-明细单准确率 " +
            "7-个人详情-面料详单准确率 " +
            "8个人详情-面辅料齐套率 " +
            "9个人详情-样衣参考率 " +
            "10-个人详情-下店市调完成率 " +
            "11-个人详情-产销率 SA款 " +
            "12-月均出稿数（cmt+fob） " +
            "13-月均下单数SKC（cmt+fob）" +
            "14-月均下单数SKU（cmt+fob）")
    private Integer type;

    /**
     * 版本编号
     */
    @ApiModelProperty("版本编号")
    private String version;

}
