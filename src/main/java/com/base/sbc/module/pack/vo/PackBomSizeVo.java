package com.base.sbc.module.pack.vo;

import com.base.sbc.module.pack.entity.PackBomSize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 类描述：资料包-物料清单-尺码
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackBomVersionVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 16:45
 */
@Data
@ApiModel("资料包-物料清单-尺码 PackBomSizeVo")
public class PackBomSizeVo extends PackBomSize {

    @ApiModelProperty(value = "排序用")
    private String sort;
}
