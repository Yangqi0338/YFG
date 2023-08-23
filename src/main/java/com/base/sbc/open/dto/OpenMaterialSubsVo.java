package com.base.sbc.open.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/17 11:31
 */
@Data
@TableName("t_basicsdatum_material_color")
@ApiModel("基础资料-物料档案open BasicsdatumMaterial")
public class OpenMaterialSubsVo extends BaseEntity {

    /**
     * 初始化数据
     * @param color
     * @param width
     * @param material
     */
    public void init(BasicsdatumMaterialColor color, BasicsdatumMaterialWidth width, OpenMaterialDto material) {
        String s = "";
        this.mtSubCode = material.getMtCode();
        this.ImgPath = material.getImgPath();
        if (color != null){
            this.colorCode = color.getColorCode();
            this.colorName = color.getColorName();
            this.spColorName = color.getSupplierColorCode();
        }
        if (width != null){
            this.specCode = width.getWidthCode();
            this.specName = width.getName();
        }
    }


    /**物料编码*/
    private String mtSubCode;
    /**颜色编码*/
    private String colorCode;
    /**颜色名称*/
    private String colorName;
    /**规格编码*/
    private String specCode;
    /**规格名称*/
    private String specName;
    /**供应商色号*/
    private String spColorName;
    /**skc图片*/
    private String ImgPath;
    private MtsubExtend mtsubExtend;


    /**subs扩展字段*/
    @Data
    class MtsubExtend{
        private String att01;
        private String att02;
        private String att03;
        private String att04;
        private String att05;
        private String att06;
        private String att07;
        private String att08;
        private String att09;
        private String att10;
        private BigDecimal num01;
        private BigDecimal num02;
        private BigDecimal num03;
        private BigDecimal num04;
        private BigDecimal num05;
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

}
