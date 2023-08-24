package com.base.sbc.open.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/15 10:06
 */
@Data
@TableName("t_basicsdatum_material")
@ApiModel("基础资料-物料档案open BasicsdatumMaterial")
public class OpenMaterialDto extends BaseEntity {

    private List<OpenMaterialSubsVo> subs;/*规格集合*/
    private List<String> ImgFiles;/*图片--物料图片附件地址*/
    private String mtCode;/*物料编号--物料编码*/
    private String SpCode;/*供应商编码--供应商编号*/
    private String mtType;/*物料属性--物料类型*/
    private String mtBigCtg;/*物料类别（取物料类别的最大类别）--物料大类*/
    private String mtCtg;/*物料类别（取物料类别的最小类别）物料种类*/
    private String mtName;/*物料名称--物料名称*/
    private String unit;/*采购单位--单位*/
    private BigDecimal loss;/*损耗--默认损耗*/
    private String Year;/*年份--年份*/
    private String Season;/*季节--季节*/
    private String width;/*门幅--门幅*/
    private String weight;/*克重--克重*/
    private BigDecimal MOQty;/*起订量（默认0）--起订量*/
    private String mtSource;/*物料来源--物料来源*/
    private String mtComponent;/*成分确认（取面料成分）--物料成分*/
    private BigDecimal paperTube;/*（默认0）--纸筒*/
    private BigDecimal spatialDifference;/*（默认0）--空差*/
    private BigDecimal rollWeight;/*（默认0）--卷重*/
    private String lngShrink;/*经缩--经缩*/
    private String latShrink;/*纬缩--纬缩*/
    private String status;/*状态（停启用）--是否冻结，*/
    private boolean isDisable;/*状态（停启用）--是否冻结，*/
    private String remark;/*备注--备注*/
    private String ImgPath;/*图片--物料主图路径*/
    private Boolean IsCoverImg;/*默认false--是否覆盖旧物料图片*/
    private BigDecimal price;/*采购报价--成本价*/
    private String defaultStore;/*（默认传-M01）--默认仓库*/
    private String operator;/*修改人--操作人*/
    private String Att02;/*面料属性分类--面料属性*/
    private String Att04;/*送检单号--请检单号*/
    private String mtSubCode;/*物料编号--物料编码*/
    private String colorCode;/*颜色编码--颜色编码*/
    private String colorName;/*颜色--颜色名称，填了取传入的值，不填取配置值*/
    private String specCode;/*规格编码--规格编码*/
    private String specName;/*规格名称--规格名称，填了取传入的值，不填取配置值*/
    private String spColorName;/*供应商色号--供应商色号*/
    private MtExtend mtExtend;

    private String extra;/*默认空--额外*/
    private String isSign;/*默认空--是否指定*/
    private String spMtCode;/*（默认空）--供应商货号*/


    public void init() {
        if ("0".equals(this.status)){
            this.isDisable = true;
        }else{
            this.isDisable = false;
        }
        this.mtExtend = new MtExtend(this.Att02,this.Att04);
    }


    @Data
    class MtExtend{
        private String att02;/*面料属性分类--面料属性*/
        private String att04;/*送检单号--请检单号*/
        private String att01;
        private String att03;
        private String att05;
        private String att06;
        private String att07;
        private String att08;
        private String att09;
        private String att10;
        private String att11;
        private String att12;
        private String att13;
        private String att14;
        private String att15;
        private BigDecimal num01;
        private BigDecimal num02;
        private BigDecimal num03;
        private BigDecimal num04;
        private BigDecimal num05;
        private BigDecimal num06;
        private BigDecimal num07;
        private BigDecimal num08;
        private BigDecimal num09;
        private BigDecimal num10;

        public MtExtend(String att02, String att04) {
            this.att02 = att02;
            this.att04 = att04;
        }
    }
    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }


}
