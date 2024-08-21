package com.base.sbc.module.hrtrafficlight.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 人事红绿灯导入入参
 *
 * @author XHTE
 * @create 2024-08-15
 */
@Data
@ApiModel(value = "人事红绿灯导入入参", description = "人事红绿灯导入入参")
public class ImportExcelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 导入文件
     */
    @ApiModelProperty("导入文件")
    private MultipartFile file;

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

}
