/******************************************************************************
 * Copyright (C) YYYY ChangSha San Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为XXXXX开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/

package com.base.sbc.config.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

/**
 * POI导出Excel工具类
 *
 * @author yang
 * @history 2015-12-07 杨锐 新建
 * @since JDK1.7
 */
@SuppressWarnings("deprecation")
public class PoiUtilsXlsx {

    private PoiUtilsXlsx() {

    }

    /**
     * 创建字体
     *
     * @param workbook   excel对象
     * @param fontName   字体名
     * @param fontHeight 字体大小
     * @param boldWeight 字体粗细
     * @param color      字体颜色
     * @return 字体对象
     */
    public static XSSFFont createFont(XSSFWorkbook workbook, String fontName, int fontHeight,
                                      short boldWeight, short color) {
        XSSFFont fontStyle = workbook.createFont();
        fontStyle.setFontHeight((short) fontHeight);
        fontStyle.setFontName(fontName);
        if (boldWeight > 0) {
            fontStyle.setBold(true);
        }
        if (color > 0) {
            fontStyle.setColor(color);
        }
        return fontStyle;
    }

    /**
     * 创建字体
     *
     * @param workbook   excel对象
     * @param fontName   字体名
     * @param fontHeight 字体大小
     * @param boldWeight 字体粗细
     * @param color      字体颜色
     * @return 字体对象
     */
    public static XSSFFont createFont(XSSFWorkbook workbook, String fontName, int fontHeight,
                                      short boldWeight, XSSFColor color) {
        XSSFFont fontStyle = workbook.createFont();
        fontStyle.setFontHeight((short) fontHeight);
        fontStyle.setFontName(fontName);
        if (boldWeight > 0) {
            //fontStyle.setBoldweight(boldWeight);
            fontStyle.setBold(true);
        }
        fontStyle.setColor(color);
        return fontStyle;
    }

    /**
     * 创建字体
     *
     * @param workbook       excel对象
     * @param fontName       字体名
     * @param heightInPoints 字号
     * @param boldWeight     字体粗细
     * @return 字体对象
     */
    public static XSSFFont createFont(XSSFWorkbook workbook, String fontName, short heightInPoints,
                                      short boldWeight) {
        XSSFFont fontStyle = workbook.createFont();
        fontStyle.setFontHeightInPoints(heightInPoints);
        fontStyle.setFontName(fontName);
        if (boldWeight > 0) {
            fontStyle.setBold(true);
        }
        return fontStyle;
    }

    /**
     * 创建字体
     *
     * @param workbook       excel对象
     * @param fontName       字体名
     * @param heightInPoints 字号
     * @param boldWeight     字体粗细
     * @param color          字体颜色
     * @return 字体对象
     */
    public static XSSFFont createFont(XSSFWorkbook workbook, String fontName, short heightInPoints,
                                      short boldWeight, XSSFColor color) {
        XSSFFont fontStyle = createFont(workbook, fontName, heightInPoints, boldWeight);
        fontStyle.setColor(color);
        return fontStyle;
    }

    /**
     * 创建单元格样式
     *
     * @param workbook excel对象
     * @param font     字体
     * @return 单元格样式
     */
    public static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, XSSFFont font) {
        XSSFCellStyle objCellStyle = workbook.createCellStyle();
        objCellStyle.setFont(font);
        objCellStyle.setAlignment(HorizontalAlignment.CENTER);
        objCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        objCellStyle.setWrapText(true);
        return objCellStyle;
    }

    /**
     * 创建单元格样式，可以从另一样式中继承设置
     *
     * @param workbook  excel对象
     * @param cellStyle 另一样式
     * @param font      字体
     * @return 新样式
     */
    public static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, XSSFCellStyle cellStyle,
                                                XSSFFont font) {
        XSSFCellStyle objNewCellStyle = workbook.createCellStyle();
        objNewCellStyle.setFont(font == null ? workbook.getFontAt(cellStyle.getFontIndex()) : font);
        objNewCellStyle.setWrapText(true);
        objNewCellStyle.setBorderLeft(cellStyle.getBorderLeft());
        objNewCellStyle.setBorderTop(cellStyle.getBorderTop());
        return objNewCellStyle;
    }

    /**
     * 创建单元格样式
     *
     * @param workbook
     * @param font      字体
     * @param xssfColor 颜色
     * @return
     */
    public static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, XSSFFont font, XSSFColor xssfColor, HorizontalAlignment align) {
        XSSFCellStyle objCellStyle = workbook.createCellStyle();
        objCellStyle.setFont(font);
        objCellStyle.setAlignment(align);
        objCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        objCellStyle.setBorderTop(BorderStyle.THIN);
        objCellStyle.setBorderRight(BorderStyle.THIN);
        objCellStyle.setBorderBottom(BorderStyle.THIN);
        objCellStyle.setBorderLeft(BorderStyle.THIN);
        objCellStyle.setWrapText(true);
        objCellStyle.setFillForegroundColor(xssfColor); // 是设置前景色不是背景色
        objCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return objCellStyle;
    }



    /**
     * 创建单元格样式，可以从另一样式中继承设置
     *
     * @param workbook  excel对象
     * @param cellStyle 另一样式
     * @param font      字体
     * @param alignment 对齐
     * @return 新样式
     */
    public static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, XSSFCellStyle cellStyle,
                                                XSSFFont font, HorizontalAlignment alignment) {
        XSSFCellStyle objNewCellStyle = workbook.createCellStyle();
        objNewCellStyle.setFont(font == null ? workbook.getFontAt(cellStyle.getFontIndex()) : font);
        objNewCellStyle.setAlignment(alignment);
        objNewCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        objNewCellStyle.setWrapText(true);
        objNewCellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        objNewCellStyle.setBorderLeft(BorderStyle.THIN); // 左边框
        objNewCellStyle.setBorderRight(BorderStyle.THIN); // 右边框
        objNewCellStyle.setBorderTop(BorderStyle.THIN); // 上边框
        return objNewCellStyle;
    }

    /**
     * 创建行
     *
     * @param sheet  所在工作簿
     * @param rowId  行号
     * @param height 行高
     * @return 行对象
     */
    public static XSSFRow createRow(XSSFSheet sheet, int rowId, int height) {
        XSSFRow objRow = sheet.createRow(rowId);
        if (height > 0) {
            objRow.setHeightInPoints(height);
        }
        return objRow;
    }

    /**
     * 创建文本类型的单元格
     *
     * @param row       行对象
     * @param colFrom   单元格开始列号
     * @param value     单元格的值
     * @param cellStyle 单元格的样式
     */
    public static void createStringCell(XSSFRow row, int colFrom, String value, XSSFCellStyle cellStyle) {
        XSSFCell objCell = row.createCell((short) colFrom);
        objCell.setCellValue(new XSSFRichTextString(value));
        objCell.setCellStyle(cellStyle);
        objCell.setCellType(CellType.STRING);
    }

    /**
     *  设置文本类型的单元格
     * @param objCell 单元格
     * @param value  文本值
     */
    public static void setStringCell(XSSFCell objCell,String value){
        objCell.setCellValue(new XSSFRichTextString(value));
        objCell.setCellType(CellType.STRING);

    }
    /**
     * 创建数值类型的单元格
     *
     * @param row       行对象
     * @param colFrom   单元格开始列号
     * @param value     单元格的值
     * @param cellStyle 单元格的样式
     */
    public static void createNumericCell(XSSFRow row, int colFrom, double value, XSSFCellStyle cellStyle) {
        XSSFCell objCell = row.createCell((short) colFrom);
        objCell.setCellValue(value);
        objCell.setCellStyle(cellStyle);
        objCell.setCellType(CellType.NUMERIC);
    }

    /**
     * 合并单元格  【有问题】
     *
     * @param sheet     所在工作簿
     * @param cellStyle 合并后单元格的样式
     * @param rowFrom   开始行号
     * @param colFrom   开始列号
     * @param rowTo     结束行号
     * @param colTo     结束列号
     */
    public static void mergeCells(XSSFSheet sheet, XSSFCellStyle cellStyle, int rowFrom, int colFrom,
                                  int rowTo, int colTo) {
        XSSFRow objRow = null;
        XSSFCell objCell = null;
        for (int i = rowFrom; i <= rowTo; i++) {
            objRow = sheet.getRow(i);
            if (objRow == null) {
                objRow = sheet.createRow(i);
            }
            for (int j = colFrom; j <= colTo; j++) {
                // 主要是为了设置合并单元格的线条，如果不创建则合并单元格的线条会出现问题
                objCell = objRow.getCell((short) j);
                if (objCell == null) {
                    objCell = objRow.createCell((short) j);
                    objCell.setCellValue(new XSSFRichTextString(""));
                    objCell.setCellStyle(cellStyle);
                }
            }
        }
        CellRangeAddress objRegion = new CellRangeAddress(rowFrom, rowTo, colFrom, colTo);
        sheet.addMergedRegion(objRegion);
    }

    /**
     * 画斜线
     *
     * @param patriarch 工作簿
     * @param rowFrom 起点所在行
     * @param colFrom 起点所在列
     * @param rowTo 终点所在行
     * @param colTo 终点所在列
     */
//	    public static void drawLine(XSSFPatriarch patriarch, int rowFrom, int colFrom, int rowTo, int colTo) {
//	        XSSFClientAnchor anchor = new XSSFClientAnchor();
//	        anchor.setAnchor((short) colFrom, rowFrom, 0, 0, (short) colTo, rowTo, 0, 0);
//	        patriarch.createSimpleShape(anchor);
//	    }

    /**
     * 画斜线
     *
     * @param patriarch
     * @param col1      开始列
     * @param row1      开始行
     * @param col2      结束列
     * @param row2      结束行
     */
    public static void drawLine(XSSFDrawing patriarch, int col1, int row1, int col2, int row2) {
        //因为要画到下一行/列 的开始所以要结束行/列+1
        XSSFSimpleShape shape = patriarch.createSimpleShape(new XSSFClientAnchor(0, 0, 1023, 255, col1, row1, col2 + 1, row2 + 1));
        shape.setShapeType(ShapeTypes.LINE);// 设置形状类型为线型
        shape.setLineWidth(0.5);// 设置线宽
        shape.setLineStyle(0);// 设置线的风格
        shape.setLineStyleColor(0, 0, 0);// 设置线的颜色
    }

    /**
     * 创建文本类型的单元格
     *
     * @param row       行对象
     * @param colFrom   单元格开始列号
     * @param value     单元格的值
     * @param cellStyle 单元格的样式
     * @param objWb     当前工作薄对象
     */
    public static void createTextStringCell(XSSFRow row, int colFrom, String value, XSSFCellStyle cellStyle,
                                            XSSFWorkbook objWb) {
        XSSFCell cell = row.createCell((short) colFrom);
        if (cell.getCellType() != CellType.STRING) {
            cell.setCellType(CellType.STRING);
        }
        XSSFDataFormat format = objWb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        cell.setCellStyle(cellStyle);
        cell.setCellValue(new XSSFRichTextString(value));
    }


    /**
     * 设置列宽
     *
     * @param objSheet
     * @param columnIndex
     * @param width
     */
    public static void setColumnWidth(XSSFSheet objSheet, int columnIndex, int width) {
        objSheet.setColumnWidth(columnIndex, width * 256);
    }
}
