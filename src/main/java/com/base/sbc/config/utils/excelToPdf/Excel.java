package com.base.sbc.config.utils.excelToPdf;

import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;

@Data
public class Excel {

    protected Workbook workbook;
    protected Sheet sheet;

    public Excel(InputStream is) {
        try {
            this.workbook = WorkbookFactory.create(is);
            this.sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}