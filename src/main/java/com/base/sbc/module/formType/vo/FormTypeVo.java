package com.base.sbc.module.formType.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FormTypeVo {
    /** 编号 */
    @ApiModelProperty(value = "编号"  )
    private String id;

    /** 分组 */
    @ApiModelProperty(value = "分组"  )
    private String groupId;

    @ApiModelProperty(value = "分组名称"  )
    private String groupName;

    /** 数据库表名 */
    @ApiModelProperty(value = "数据库表名"  )
    private String name;

    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String coding;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间"  )
    private Date createTime;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间"  )
    private Date updateTime;

    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    /** 说明 */
    @ApiModelProperty(value = "说明" ,example = "")
    private String tableExplain;
}
