package com.base.sbc.module.material.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "状态查询数组")
    private String[] statusList;

    @ApiModelProperty(value = "查询条件标签id集合")
    private String[] labelIds;

    @ApiModelProperty(value = "是否收藏")
    private boolean collect;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "ids 查询的id集合")
    private List<String> ids;
    @ApiModelProperty(value = "materialCategoryIds 查询的id集合")
    private List<String> materialCategoryIds;
    @ApiModelProperty(value = "尺码筛选")
    private String sizeId;

    @ApiModelProperty(value = "颜色筛选")
    private String colorId;

    @ApiModelProperty(value = "租户编码")
    private String companyCode;

    @ApiModelProperty(value = "素材分类id")
    private String materialCategoryId;

    @ApiModelProperty(value = "素材细分类")
    private String materialSubcategory;

    @ApiModelProperty(value = "品类id")
    private String categoryId;

    @ApiModelProperty(value = "创建人的id")
    private String createId;

    @ApiModelProperty(value = "文件信息")
    private String fileInfo;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "场合搭配code")
    private String occasionCode;

    @ApiModelProperty(value = "颜色搭配code")
    private String colorMatchCode;

    @ApiModelProperty(value = "图案种类")
    private String patternTypes;

    @ApiModelProperty(value = "图案种类列表")
    private List<String>  patternTypeList;

    @ApiModelProperty(value = "运用手法")
    private String useSkill;

    @ApiModelProperty(value = "素材名称集合")
    private String materialNames;

    @ApiModelProperty(value = "标签名称集合")
    private List<String> materialNameList;

    @ApiModelProperty(value = "标签名称")
    private String labelNames;

    @ApiModelProperty(value = "公司标识：0：个人素材上传，1：公司素材上传")
    private String companyFlag;

    @ApiModelProperty(value = "素材描述")
    private String materialRemarks;

    @ApiModelProperty(value = "1启用，0停用")
    private String enableFlag;

    @ApiModelProperty(value = "模糊搜索内容")
    private String content;

    @ApiModelProperty(value = "公司品牌权限列表")
    private List<String> brandList;

    @ApiModelProperty(value = "所属的文件夹id")
    private String folderId;

    @ApiModelProperty(value = "所属的文件夹id")
    private List<String> folderIdList;


}
