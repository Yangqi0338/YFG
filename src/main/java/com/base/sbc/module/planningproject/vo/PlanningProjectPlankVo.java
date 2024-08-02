package com.base.sbc.module.planningproject.vo;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlankDimension;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.base.sbc.module.tablecolumn.entity.TableColumn;
import com.base.sbc.module.tablecolumn.vo.TableColumnVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/11/20 18:10:14
 * @mail 247967116@qq.com
 */
@Data
public class PlanningProjectPlankVo extends PlanningProjectPlank {
    /** 大类编码 */
    @ApiModelProperty(value = "大类编码"  )
    private String prodCategory1stCode;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String prodCategoryCode;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String prodCategoryName;
    /** 中类code */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2ndCode;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /** 第一维度id */
    @ApiModelProperty(value = "第一维度id"  )
    private String dimensionId;
    /** 第一维度名称 */
    @ApiModelProperty(value = "第一维度名称"  )
    private String dimensionName;
    /** 第一维度名称 */
    @ApiModelProperty(value = "第一维度编码"  )
    private String dimensionCode;
    /** 维度值 */
    @ApiModelProperty(value = "维度值"  )
    private String dimensionValue;
    /** 是否开启中类(0:未开启,1:开启) */
    @ApiModelProperty(value = "是否开启中类(0:未开启,1:开启)"  )
    private String isProdCategory2nd;
    /**
     * 是否是虚拟坑位
     */
    @ApiModelProperty(value = "是否是虚拟坑位(1:是,0:否"  )
    private String isVirtual;

    /**
     * 产销
     */
    @ApiModelProperty(value = "产销"  )
    private String saleInto;
    /**
     * 销量
     */
    @ApiModelProperty(value = "销量"  )
    private String sale;

    private List<FieldManagementVo> fieldManagementVos;

    private List<FieldManagementVo> oldFieldManagementVos;

    private StyleColorVo oldStyleColor;
    private StyleColorVo styleColor;
    /**
     * 表头列
     */
    private List<TableColumnVo> columnVos;

    private  List<PlanningProjectPlankDimension> dimensionList;
    private  String selfPic;
}
