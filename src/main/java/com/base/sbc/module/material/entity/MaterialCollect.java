package com.base.sbc.module.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：素材收藏表 实体类
 * @author 卞康
 * @date 创建时间：2023-3-24 9:44:55
 * @version 1.0
 */
@Data
@TableName("t_material_collect")
@EqualsAndHashCode(callSuper = true)
public class MaterialCollect extends BaseDataEntity<String> {
    /** 用户id */
    private String userId;
    /** 素材id */
    private String materialId;

    @ApiModelProperty(value = "所属的文件夹id")
    private String folderId;
}

