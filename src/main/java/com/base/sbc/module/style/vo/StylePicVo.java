package com.base.sbc.module.style.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.style.entity.StylePic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.style.vo.StylePicVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-10-20 17:09
 */
@Data
public class StylePicVo extends StylePic {

    @ApiModelProperty(value = "文件地址")
    private String url;

    @ApiModelProperty(value = "文件地址")
    private String fileId;

    public String getFileId() {
        if (StrUtil.isNotBlank(fileId)) {
            return fileId;
        }
        return getFileName();
    }
}
