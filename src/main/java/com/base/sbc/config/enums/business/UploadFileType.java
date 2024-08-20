package com.base.sbc.config.enums.business;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * {@code 描述：全量标准表类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/30
 */
@Getter
@AllArgsConstructor
public enum UploadFileType {
    /**  */
    sourceMaterial("创意素材库图/附件 t_material.pic_url"),
    planning("商品企划图 t_planning_category_item.style_pic"),
    styleOther("款式设计（除设计款外其他图片及附件）"),
    sampleOther("样衣/打版其他附件"),
    sample("样衣图片（包含产前样） t_pattern_making.sample_pic "),
    preSample("样衣图片（包含产前样） t_pre_production_sample_task.sample_pic"),
    stylePackage("设计BOM标准资料包（除工艺单外）upload_file"),
    dataPackageOther(""),
    materialOther("物料其他附件"),
    config("系统配置附件/图"),
    material("物料主图"),
    patternLibraryFile("版型库文件"),
    patternLibraryPic("版型库图片"),
    fabricAtactiform("调料管理 面料、辅料图片上传"),
    ingredientsAtactiform(""),
    replayRating("ReplayRating", "复盘评分", Arrays.asList("BMP", "JPG", "JPEG", "PNG", "TIFF", "PCX", "RAW", "MP4", "MKV", "AVI", "RMVB")),
    replayRatingFile("ReplayRatingFile", "复盘评分附件", Arrays.asList("DOCX", "DOC", "PDF", "PPT", "PPTX", "XLS", "XLSX", "RTF")),
    markingOrderUpload("markingOrderUpload"),
    planningProjectPlank("planningProjectPlank"),
    materialUpload("materialUpload"),
    Account(""),
    messageTemptableUpload("messageTemptableUpload"),

    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    /** 可以通过的后缀 */
    private List<String> accessSuffix = null;


    UploadFileType(String text) {
        this.code = this.name();
        this.text = text;
    }

    UploadFileType(String text, List<String> accessSuffix) {
        this(text);
        this.accessSuffix = accessSuffix;
    }

    public static UploadFileType findByCode(String code) {
        return Arrays.stream(UploadFileType.values()).filter(it -> it.getCode().equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
