package com.base.sbc.module.planningproject.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/11/16 17:37:37
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_planning_project_max_category")
@ApiModel("企划看板-规划-大类 PlanningProjectMaxCategory")
public class PlanningProjectMaxCategory {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /** 企划看板规划id */
    @ApiModelProperty(value = "企划看板规划id"  )
    private String planningProjectId;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /** 大类编码 */
    @ApiModelProperty(value = "大类编码"  )
    private String prodCategory1stCode;
    /** 数量 */
    @ApiModelProperty(value = "数量"  )
    private String number;
    private String companyCode;
}
