package com.base.sbc.module.pack.vo;

import com.base.sbc.module.pack.entity.PackTechSpec;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 类描述：资料包-工艺说明 vo
 *
 * @author lxl
 * @version 1.0
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 15:41:45
 */
@Data
@ApiModel("资料包-工艺说明 PackTechSpecVo")
public class PackTechSpecVo extends PackTechSpec {
    private Integer rows;
}