package com.base.sbc.module.pack.dto;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：物料清单打印明细单
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.BomPrintVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-07 09:22
 */

@Data
@ApiModel("资料包-物料清单打印明细单Vo BomPrintVo")
public class BomPrintVo {
    @ApiModelProperty(value = "波段名称")
    private String bandName;

    @ApiModelProperty(value = "品名")
    private String productName;

    @ApiModelProperty(value = "下单日期")
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private Date placeOrderDate;

    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "款号")
    private String styleNo;


    @ApiModelProperty(value = "是否主推(0否,1:是)")
    private String isMainly;

    @ApiModelProperty(value = "设计编号")
    private String designNo;


    @ApiModelProperty(value = "款图")
    private String stylePic;

    @ApiModelProperty(value = "制单人")
    private String createName;
    @ApiModelProperty(value = "设计师")
    private String designer;
    @ApiModelProperty(value = "设计组长")
    private String designTeamLeader;
    @ApiModelProperty(value = "设计总监")
    private String designDirector;
    @ApiModelProperty(value = "设计经理")
    private String designManager;


    /**
     * 唛类信息
     */
    @ApiModelProperty(value = "唛类信息")
    private String apparelLabels;
    /**
     * 特别注意
     */
    @ApiModelProperty(value = "特别注意")
    private String specNotice;
    /**
     * 特殊工艺备注
     */
    @ApiModelProperty(value = "特殊工艺备注")
    private String specialSpecComments;

    @ApiModelProperty(value = "物料清单")
    private List<PackBomVo> bomList;


    /**
     * get 粘衬信息
     *
     * @return
     */
    public String getAdhesiveLiningInfo() {
        if (CollUtil.isEmpty(bomList)) {
            return "";
        }

        String collect = bomList.stream()
                .filter(item -> StrUtil.equals("DP008", item.getCollocationCode()) || StrUtil.equals("衬", item.getCollocationName()))
                .map(item -> {
                    ArrayList<String> strings = CollUtil.newArrayList(
                            item.getMaterialCode(),
                            item.getMaterialName(),
                            item.getColor() + item.getColorCode(),
                            Opt.ofNullable(item.getUnitUse()).map(unitUse -> NumberUtil.toStr(unitUse)).orElse("") + item.getStockUnitName(),
                            "件"
                    );
                    String join = CollUtil.join(CollUtil.removeBlank(strings), "/");
                    return join;
                }).collect(Collectors.joining(" "));
        return collect;
    }

}
