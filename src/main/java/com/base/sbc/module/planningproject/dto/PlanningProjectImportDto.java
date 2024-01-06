package com.base.sbc.module.planningproject.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "上市时间", orderNum = "2")
    private Date marketTime;
    @Excel(name = "下单时间", orderNum = "3")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;
    @Excel(name = "面料下单时间", orderNum = "4")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fabricOrderTime;
    @Excel(name = "SKC数", orderNum = "5")
    private String skcNumber;
}
