package com.base.sbc.open.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/8/28 11:24:26
 * @mail 247967116@qq.com
 */
@Data
public class DesignerDto {
    @Excel(name = "用户登录")
    private String username;
    @Excel(name = "显示名称")
    private String name;
    @Excel(name = "名")
    private String designerCode;
}
