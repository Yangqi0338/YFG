package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * 物料档案规格
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料档案规格实体")
public class BasicsdatumMaterialWidthSaveDto extends BasicsdatumMaterialWidth {
	@NotBlank(message = "ID必填,新增-1")
	@ApiModelProperty(value = "id", required = true)
	private String id;
	private String parentId;
	private String oldWidthCode;
}
