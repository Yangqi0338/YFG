package com.base.sbc.module.common.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类描述：简单用户信息
 * @address com.base.sbc.module.common.vo.UserInfo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-21 09:33
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {
    @ApiModelProperty(value = "id"  ,example = "100")
    private String id;
    @ApiModelProperty(value = "名称"  ,example = "张三")
    private String name;
    @ApiModelProperty(value = "头像"  ,example = "http://....")
    private String avatar;
}
