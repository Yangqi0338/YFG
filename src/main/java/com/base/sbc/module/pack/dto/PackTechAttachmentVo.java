package com.base.sbc.module.pack.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.common.vo.AttachmentVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-工艺说明-附件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackTechAttachmentVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-14 16:58
 */
@Data
@ApiModel("资料包-工艺说明-附件 PackTechAttachmentVo")
public class PackTechAttachmentVo extends AttachmentVo {


    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型为空")
    private String packType;

    @ApiModelProperty(value = "工艺类型:裁剪工艺、基础工艺、小部件、注意事项、整烫包装")
    @NotBlank(message = "工艺类型不能为空")
    private String specType;


    public String getPackType() {
        if (StrUtil.isEmpty(packType)) {
            return CollUtil.get(StrUtil.split(getAttachmentType(), CharUtil.DASHED), 0);
        }
        return packType;
    }

    public String getSpecType() {
        if (StrUtil.isEmpty(specType)) {
            return CollUtil.get(StrUtil.split(getAttachmentType(), CharUtil.DASHED), 1);
        }
        return specType;
    }
}
