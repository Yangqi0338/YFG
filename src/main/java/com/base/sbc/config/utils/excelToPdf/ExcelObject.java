package com.base.sbc.config.utils.excelToPdf;

import lombok.Data;
import lombok.Getter;

import java.io.InputStream;

/**
 * Created by cary on 6/15/17.
 */
@Data
public class ExcelObject {
    /**
     * 锚名称
     */
    private String anchorName;
    /**
     * Excel Stream
     */
    private InputStream inputStream;
    /**
     * POI Excel
     */
    @Getter
    private Excel excel;

    public ExcelObject(InputStream inputStream){
        this.inputStream = inputStream;
        this.excel = new Excel(this.inputStream);
    }

    public ExcelObject(String anchorName , InputStream inputStream){
        this.anchorName = anchorName;
        this.inputStream = inputStream;
        this.excel = new Excel(this.inputStream);
    }
}