package com.base.sbc.module.sample.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.base.sbc.module.sample.entity.FabricDetailedInformation;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/*返回de 面料详细信息*/
@Data
public class FabricDetailedInformationVo extends FabricDetailedInformation {
    private String id;
    /** 面料是否可用(0是，1否) */
    @ApiModelProperty(value = "面料是否可用(0是，1否)"  )
    private String fabricIsUsable;
    /** 面料价格 */
    @ApiModelProperty(value = "面料价格"  )
    private BigDecimal fabricPrice;
    /** 纱支规格 */
    @ApiModelProperty(value = "纱支规格"  )
    private String specification;
    /** 密度 */
    @ApiModelProperty(value = "密度"  )
    private String density;
    /** 厂家成分 */
    @ApiModelProperty(value = "厂家成分"  )
    private String supplierFactoryIngredient;
    /** 货期 */
    @ApiModelProperty(value = "货期"  )
    private Integer leadtime;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    private Integer minimumOrderQuantity;
    /** 厂家有效门幅 */
    @ApiModelProperty(value = "厂家有效门幅"  )
    private String translate;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private String gramWeight; ;
    /** 胚布情况 */
    @ApiModelProperty(value = "胚布情况"  )
    private String germinalCondition;
    /** 调样日期 */
    @ApiModelProperty(value = "调样日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date atactiformDate;
    /** 预估到样时间 */
    @ApiModelProperty(value = "预估到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date practicalAtactiformDate;
    /** 留样送检时间 */
    @ApiModelProperty(value = "留样送检时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectDate;
    /** 理化检测结果（0是1否 */
    @ApiModelProperty(value = "理化检测结果（0是1否"  )
    private String physicochemistryDetectionResult;
    /** 样衣试穿洗涤送检时间 */
    @ApiModelProperty(value = "样衣试穿洗涤送检时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sampleWashingInspectionDate;
    /** 洗涤检测结果（0是1否 */
    @ApiModelProperty(value = "洗涤检测结果（0是1否"  )
    private String washDetectionResult;
    private String imageUrl;
    /** 是否草稿（0是：1否 */
    @ApiModelProperty(value = "是否草稿（0是：1否"  )
    private String isDraft;
    /*理化报告地址*/
    private String  reportUrl;

    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;



    @TableField(exist = false)
    private String codeName;

    public String getCodeName() {

        String format = String.format("%06d", this.getCode());
        return "mldyd"+format;
    }


    private Integer code;
}
