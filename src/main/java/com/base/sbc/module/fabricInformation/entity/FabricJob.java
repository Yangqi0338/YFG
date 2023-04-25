package com.base.sbc.module.fabricInformation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_job")
public class FabricJob extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;

    /** 面料基本信息id */
    @ApiModelProperty(value = "面料基本信息id"  )
    private String fabricBasicId;

    /** 岗位id */
    @ApiModelProperty(value = "岗位id"  )
    private String userJobId;

    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;

    /*发起状态（0我发起 1我接受*/
    private String  sponsorStatus;
}
