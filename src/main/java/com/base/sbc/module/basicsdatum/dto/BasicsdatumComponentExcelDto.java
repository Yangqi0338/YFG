package com.base.sbc.module.basicsdatum.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data

@TableName(value = "BasicsdatumComponentExcelDto")
public class BasicsdatumComponentExcelDto {

    @Excel(name  = "id" )
    private String id;

    /** 编码 */
    @Excel(name  = "编码"  )
    private String coding;

    /** 部件类别 */
    @Excel(name  = "部件类别")
    private String componentCategory;

    /** 工艺项目 */
    @Excel(name  = "工艺项目"  )
    private String technologyProject;

    /** 描述 */
    @Excel(name  = "描述"  )
    private String description;

    /** 图片 */
    @Excel(name = "图片", type = 2 )
    private String image;

    /** 可用的 */
    @Excel(name  = "可用的" , replace = {"true_0", "false_1"} )
    private String status;


    /**  创建者名称 */
    @Excel(name = "创建人"   )
    protected String createName;

    /** 创建日期 */
    @Excel(name  = "创建" )
    @DateTimeFormat("yyyy/MM/dd hh:mm:ss")
    protected Date createDate;

    /** 更新者名称  */
    @Excel(name  = "修改者" )
    protected String updateName;


    /** 更新日期 */
    @Excel(name  = "修改" )
    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    protected Date updateDate;

}
