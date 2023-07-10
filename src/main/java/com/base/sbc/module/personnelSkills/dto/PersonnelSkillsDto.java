package com.base.sbc.module.personnelSkills.dto;

import com.base.sbc.module.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/7/10 14:40:35
 * @mail 247967116@qq.com
 */
@Data
public class PersonnelSkillsDto extends BaseDto {
    /** 用户名称集合 */
    @ApiModelProperty(value = "用户名称集合"  )
    private String userNames;


    /** 岗位名称集合 */
    @ApiModelProperty(value = "岗位名称集合"  )
    private String positionNames;

    /** 品类名称集合 */
    @ApiModelProperty(value = "品类名称集合"  )
    private String categoryNames;
}
