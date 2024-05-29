package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.List;

@Data
@ApiModel("面料汇总-探究")
@AllArgsConstructor
@NoArgsConstructor
public class FabricSummarySaveDTO {

    @ApiModelProperty(value = "组Id"  )
    @NotBlank(message = "组Id不能为空")
    private String groupId;

    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    @ApiModelProperty(value = "物料信息")
    @Valid
    List<FabricInfo> fabricInfos;

    @Data
    public static class FabricInfo {

        @ApiModelProperty(value = "物料编号")
        @NotBlank(message = "物料编号不能为空")
        private String materialCode;

        /** 规格名称 */
        @ApiModelProperty(value = "规格名称"  )
        private String widthName;
    }

}
