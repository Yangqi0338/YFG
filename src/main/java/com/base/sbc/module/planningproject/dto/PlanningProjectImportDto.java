package com.base.sbc.module.planningproject.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/11/24 14:45:58
 * @mail 247967116@qq.com
 */
@Data
@TableName("planning_project_plank_import")
public class PlanningProjectImportDto {
@TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String seasonId;
    /**
     * 渠道编码
     */
    @ApiModelProperty(value = "渠道编码")
    private String planningChannelCode;
    @Excel(name = "月-周别", orderNum = "0")
    private String week;
    @Excel(name = "上市波段", orderNum = "1")
    private String waveBand;
    @Excel(name = "上市时间", orderNum = "2")
    private String marketTime;
    @Excel(name = "下单时间", orderNum = "3")
    private String orderTime;
    @Excel(name = "面料下单时间", orderNum = "4")
    private String fabricOrderTime;
    @Excel(name = "SKC数", orderNum = "5")
    private String skcNumber;
}
