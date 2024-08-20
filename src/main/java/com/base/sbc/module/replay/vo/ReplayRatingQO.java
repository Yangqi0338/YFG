/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.constant.ReplayRatingProperties;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailOrderStatusEnum;
import com.base.sbc.config.enums.business.replay.ReplayRatingType;
import com.base.sbc.module.replay.dto.ProductionSaleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

/**
 * 类描述：基础资料-复盘评分QueryDto 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.dto.ReplayRatingQueryDto
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReplayRatingQO extends QueryFieldDto {

    /** 复盘维度类型 */
    @ApiModelProperty(value = "复盘维度类型")
    @NotNull(message = "查询类型不能为空")
    private ReplayRatingType type;

    /** 产品季id */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /** 波段 */
    @ApiModelProperty(value = "波段")
    private String bandName;

    /** 大类code */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;

    /** 品类code */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;

    /** 中类code */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

    /** 小类code */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /** 评分状态：0未评分 1已评分 */
    @ApiModelProperty(value = "评分状态：0未评分 1已评分")
    private YesOrNoEnum ratingFlag;

    /** 版型库id */
    @ApiModelProperty(value = "版型库id")
    private String registeringId;

    /** 套版款号 */
    @ApiModelProperty(value = "套版款号")
    private String registeringNo;

    /** 廓形 */
    @ApiModelProperty(value = "廓形")
    private String silhouetteName;

    /** 申请版型库状态 */
    @ApiModelProperty(value = "申请版型库状态")
    private YesOrNoEnum transferPatternFlag;

    /** 物料编号 */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    /** 面料供应商 */
    @ApiModelProperty(value = "面料供应商")
    private String supplierId;

    /** 面料颜色 */
    @ApiModelProperty(value = "面料颜色")
    private String colorCode;

    /** 面料自主研发 */
    @ApiModelProperty(value = "面料自主研发")
    private YesOrNoEnum materialOwnResearchFlag;

    /** 查询总数标志 */
    @ApiModelProperty(value = "查询总数标志")
    private YesOrNoEnum findTotalFlag;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "字段说明")
    private String fieldExplain;

    /** 非开发分类 */
    @ApiModelProperty(value = "非开发分类")
    private String noDevClass = ReplayRatingProperties.noDevClass;

    /** 物料分类 */
    @ApiModelProperty(value = "物料分类")
    private String category1Code = ReplayRatingProperties.category1Code;

    /** 非物料编码 */
    @ApiModelProperty(value = "非物料编码")
    private String noMaterialCode = ReplayRatingProperties.noMaterialCode;

    /** 订货本下单状态 */
    @ApiModelProperty(value = "订货本下单状态")
    private OrderBookDetailOrderStatusEnum orderBookStatus = ReplayRatingProperties.orderBookStatus;


    public String getMaterialCodeSql() {
        return "select 1 from t_pack_bom tpb left join t_pack_info tpi ON tpi.id = tpb.foreign_id " +
                "where tpb.pack_type = 'packBigGoods' and tpb.del_flag = '0' AND tpb.status = '1' and tpi.style_color_id = tsc.id and tpi.del_flag = '0'" +
                "AND tpb.material_code = {0}";
    }

    public String getSilhouetteSql() {
        return "select 1 from t_field_val tfv where tfv.foreign_id = ts.id AND tfv.del_flag = '0' AND tfv.data_group = {0} AND tfv.field_explain = {1} AND tfv.val_name = {2}";
    }

    public boolean needSpecialTotalSum() {
        return StrUtil.isNotBlank(planningSeasonId);
    }

    @Override
    public boolean countCal() {
        return findTotalFlag != YesOrNoEnum.NO;
    }

    public boolean isStyleReverse() {
        return findOrderComparator(this.getFieldOrderMap()) != null;
    }

    public Comparator<ProductionSaleDTO> findOrderComparator(Map<String, String> map) {
        if (MapUtil.isEmpty(this.getFieldOrderMap())) return null;
        ProductionSaleDTO productionSaleDTO = BeanUtil.toBean(MapUtil.edit(map,
                (entry) -> MapUtil.entry(entry.getKey(), Arrays.asList("desc", "DESC").contains(entry.getValue()) ? "1" : "0")), ProductionSaleDTO.class);
        return productionSaleDTO.findOrderComparator();
    }

    @Override
    public String getDefaultOrderBy() {
        switch (type) {
            case STYLE:
                return "tsc.design_no";
            default:
                return super.getDefaultOrderBy();
        }
    }
}
