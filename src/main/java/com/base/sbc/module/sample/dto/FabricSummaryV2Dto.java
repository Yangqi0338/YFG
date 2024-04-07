package com.base.sbc.module.sample.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@ApiModel("面料汇总-查询")
@AllArgsConstructor
@NoArgsConstructor
public class FabricSummaryV2Dto extends QueryFieldDto {

    private String id;

    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    @ApiModelProperty(value = "汇总编码"  )
    private String fabricSummaryCode;

    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
        private String materialCode;

    @ApiModelProperty(value = "供应商物料编号"  )
    private String supplierFabricCode;

    @ApiModelProperty(value = "询价编号"  )
    private String enquiryCode;

    @ApiModelProperty(value = "年份尾缀"  )
    private String yearSuffix;

    @ApiModelProperty(value = "试穿结果 0不合适，1合适"  )
    private String fittingResult;

    @ApiModelProperty(value = "品牌"  )
    private String brand;

    @ApiModelProperty(value = "理化检测结果（1是0否)"  )
    private String physicochemistryDetectionResult;

    @ApiModelProperty(value = "生产周期-期货"  )
    private BigDecimal productionDay;

    @ApiModelProperty(value = "规格名称"  )
    private String widthName;

}
