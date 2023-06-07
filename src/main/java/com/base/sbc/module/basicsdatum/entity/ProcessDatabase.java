package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/2 17:48:27
 * @mail 247967116@qq.com
 *
 * 工艺资料库
 */
@Data
@TableName("t_process_database")
public class ProcessDatabase extends BaseDataEntity<String> {
    /**工艺项目(名称)*/
    private String processName;
    /**编码*/
    private String code;
    /**品牌id*/
    private String brandId;
    /**品牌名称*/
    private String brandName;
    /**工艺类型*/
    private String processType;
    /**工艺要求*/
    private String processRequire;
    /**描述*/
    private String description;
    /**品类id*/
    private String categoryId;
    /**品类名称*/
    private String categoryName;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String picture;
    /**状态*/
    private String status;
    /**类别 1：部件库，2：基础工艺，3：外辅工艺，4：裁剪工艺，5：注意事项，6：整烫包装，7：模板部件*/
    private String type;
}
