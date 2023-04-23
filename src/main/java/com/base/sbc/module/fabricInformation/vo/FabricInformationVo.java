package com.base.sbc.module.fabricInformation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/*返回面料信息*/
@Data
public class FabricInformationVo extends FabricDetailedInformationVo {

    private String id;

    /** 面料详细id */
    @ApiModelProperty(value = "面料详细id"  )
    private String fabricDetailedId;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 厂家 */
    @ApiModelProperty(value = "厂家"  )
    private String manufacturer;
    /** 厂家编号 */
    @ApiModelProperty(value = "厂家编号"  )
    private String manufacturerNumber;
    /** 厂家色号 */
    @ApiModelProperty(value = "厂家色号"  )
    private String manufacturerColour;
    /** 是否新面料（0是 1否 */
    @ApiModelProperty(value = "是否新面料（0是 1否"  )
    private String isNewFabric;
    /** 数量（米） */
    @ApiModelProperty(value = "数量（米）"  )
    private Integer quantity;
    /** 登记时间 */
    @ApiModelProperty(value = "登记时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerDate;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;



}
