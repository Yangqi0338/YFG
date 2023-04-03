package com.base.sbc.module.material.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialQueryDto extends Page {

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

    /**
     * 租户编码
     */
    private String companyCode;

    /**
     * 素材分类id
     */
    private String materialCategoryId;

    /**
     * 品类id
     */
    private Integer categoryId;

    /**
     * 创建人的id
     */
    private String createId;

    /**
     * 文件信息
     */
    private String fileInfo;
}
