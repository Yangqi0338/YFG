package com.base.sbc.module.material.dto;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.module.material.entity.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:48:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialAllDto extends Material {

    /**
     * 所有标签
     */
    private List<MaterialLabel> labels;

    /**
     * 所有尺码
     */
    private List<MaterialSize> sizes;

    /**
     * 所有颜色
     */
    private List<MaterialColor> colors;

    /**
     * 状态查询数组
     */
    private String[] statusList;

    /**
     * 查询条件标签id集合
     */
    private String[] labelIds;

    /**
     * 收藏id
     */
    private String collectId;

    /**
     * ids 查询的id集合
     */
    private List<String> ids;

    /**
     * 尺码筛选
     */
    private String sizeId;

    /**
     * 颜色筛选
     */
    private String colorId;

}
