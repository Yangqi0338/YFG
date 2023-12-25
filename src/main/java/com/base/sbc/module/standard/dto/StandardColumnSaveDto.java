package com.base.sbc.module.standard.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialColor;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.MaterialSize;
import com.base.sbc.module.standard.entity.StandardColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @author 孔祥基
 * @date 2023/4/3 13:27:32
 */
@Data
public class StandardColumnSaveDto {

    /** 实体主键 */
    protected String id;

    /** 属性名称 */
    @ApiModelProperty(value = "吊牌属性名称"  )
    @NotBlank(message = "请输入吊牌属性名称")
    private String name;

    /** 属性模式 */
    @ApiModelProperty(value = "属性模式"  )
    private StandardColumnModel model;
}
