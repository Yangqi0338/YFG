package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BasicsdatumMaterialUpdateDto {
    @ApiModelProperty(value = "主键")
    private String id;
    /** 物料编号 */
    @Size(min = 6, max = 12, message = "编号长度不能小于6位，最长不能超过12位!")
    @ApiModelProperty(value = "物料编号")
    @Pattern(regexp ="^[a-zA-Z0-9_-]+$", message= "不能输入特殊字符，只能输入数字，字母，下划线和横杠！")
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /** 中类编码 */
    @ApiModelProperty(value = "中类编码")
    private String category2Code;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称")
    private String category2Name;
    /** 小类编码 */
    @ApiModelProperty(value = "小类编码")
    private String category3Code;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称")
    private String category3Name;


}
