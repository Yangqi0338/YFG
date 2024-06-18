package com.base.sbc.module.style.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
/*查询大货*/
public class QueryBulkCargoDto extends QueryFieldDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "品类")
    private String categoryName;

    @ApiModelProperty(value = "大货编号多个使用，分割"  )
    private String styleNo;

    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    @ApiModelProperty(value = "款号"  )
    private String   designNo;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "大类名称"  )
    private String  prodCategory1stName;

    @ApiModelProperty(value = "中类")
    private String prodCategory2ndName;

    @ApiModelProperty(value = "中类"  )
    private String prodCategory2nd;

    @ApiModelProperty(value = "小类编码")
    private String prodCategory3nd;

    @ApiModelProperty(value = "小类")
    private String prodCategory3ndName;

    private String season;

    private String year;

    /*是否导出图片*/
    private String imgFlag;

    /*导出标记*/
    private String excelFlag;
}
