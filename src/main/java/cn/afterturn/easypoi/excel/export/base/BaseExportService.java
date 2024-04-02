//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.afterturn.easypoi.excel.export.base;

import cn.afterturn.easypoi.cache.ImageCache;
import cn.afterturn.easypoi.entity.BaseTypeConstants;
import cn.afterturn.easypoi.entity.SpecialSymbolsEntity;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import cn.afterturn.easypoi.util.PoiExcelGraphDataUtil;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.text.DecimalFormat;
import java.util.*;

public abstract class BaseExportService extends ExportCommonService {
    private int currentIndex = 0;
    protected ExcelType type;
    private Map<Integer, Double> statistics;
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");
    protected IExcelExportStyler excelExportStyler;

    public BaseExportService() {
        this.type = ExcelType.XSSF;
        this.statistics = new HashMap();
    }

    public int[] createCells(Drawing patriarch, int index, Object t, List<ExcelExportEntity> excelParams, Sheet sheet, Workbook workbook, short rowHeight, int cellNum) {
        try {
            Row row = sheet.getRow(index) == null ? sheet.createRow(index) : sheet.getRow(index);
            if (rowHeight != -1) {
                row.setHeight(rowHeight);
            }

            int maxHeight = 1;
            int listMaxHeight = 1;
            int margeCellNum = cellNum;
            int indexKey = 0;
            if (excelParams != null && !excelParams.isEmpty()) {
                indexKey = this.createIndexCell(row, index, (ExcelExportEntity)excelParams.get(0));
            }

            cellNum += indexKey;
            int k = indexKey;

            ExcelExportEntity entity;
            int paramSize;
            for(paramSize = excelParams.size(); k < paramSize; ++k) {
                entity = (ExcelExportEntity)excelParams.get(k);
                if (entity.getList() == null) {
                    Object value = this.getCellValue(entity, t);
                    if (entity.getType() == BaseTypeConstants.STRING_TYPE) {
                        this.createStringCell(row, cellNum++, value == null ? "" : value.toString(), index % 2 == 0 ? this.getStyles(false, entity) : this.getStyles(true, entity), entity);
                    } else if (entity.getType() == BaseTypeConstants.DOUBLE_TYPE) {
                        this.createDoubleCell(row, cellNum++, value == null ? "" : value.toString(), index % 2 == 0 ? this.getStyles(false, entity) : this.getStyles(true, entity), entity);
                    } else if (entity.getType() == BaseTypeConstants.Symbol_TYPE) {
                        this.createSymbolCell(row, cellNum++, value, index % 2 == 0 ? this.getStyles(false, entity) : this.getStyles(true, entity), entity);
                    } else {
                        this.createImageCell(patriarch, entity, row, cellNum++, value == null ? "" : value.toString(), t);
                    }

                    if (entity.isHyperlink()) {
                        row.getCell(cellNum - 1).setHyperlink(this.dataHandler.getHyperlink(row.getSheet().getWorkbook().getCreationHelper(), t, entity.getName(), value));
                    }
                } else {
                    Collection<?> list = this.getListCellValue(entity, t);
                    int tmpListHeight = 0;
                    if (list != null && list.size() > 0) {
                        int tempCellNum = 0;

                        int[] temp;
                        for(Iterator var20 = list.iterator(); var20.hasNext(); tmpListHeight += temp[0]) {
                            Object obj = var20.next();
                            temp = this.createCells(patriarch, index + tmpListHeight, obj, entity.getList(), sheet, workbook, rowHeight, cellNum);
                            tempCellNum = temp[1];
                        }

                        cellNum = tempCellNum;
                        listMaxHeight = Math.max(listMaxHeight, tmpListHeight);
                    } else {
                        cellNum += this.getListCellSize(entity.getList());
                    }
                }
            }

            maxHeight += listMaxHeight - 1;
            if (indexKey == 1 && ((ExcelExportEntity)excelParams.get(1)).isNeedMerge()) {
                ((ExcelExportEntity)excelParams.get(0)).setNeedMerge(true);
            }

            k = indexKey;

            for(paramSize = excelParams.size(); k < paramSize; ++k) {
                entity = (ExcelExportEntity)excelParams.get(k);
                if (entity.getList() != null) {
                    margeCellNum += entity.getList().size();
                } else if (entity.isNeedMerge() && maxHeight > 1) {
                    for(int i = index + 1; i < index + maxHeight; ++i) {
                        if (!(sheet instanceof SXSSFSheet) || i > ((SXSSFSheet)sheet).getLastFlushedRowNum()) {
                            if (sheet.getRow(i) == null) {
                                try {
                                    sheet.createRow(i);
                                } catch (Exception var23) {
                                    var23.printStackTrace();
                                }
                            }

                            sheet.getRow(i).createCell(margeCellNum);
                            sheet.getRow(i).getCell(margeCellNum).setCellStyle(this.getStyles(false, entity));
                        }
                    }

                    PoiMergeCellUtil.addMergedRegion(sheet, index, index + maxHeight - 1, margeCellNum, margeCellNum);
                    ++margeCellNum;
                }
            }

            return new int[]{maxHeight, cellNum};
        } catch (Exception var24) {
            LOGGER.error("excel cell export error ,data is :{}", ReflectionToStringBuilder.toString(t));
            LOGGER.error(var24.getMessage(), var24);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, var24);
        }
    }

    private void createSymbolCell(Row row, int index, Object specialSymbolsEntity, CellStyle style, ExcelExportEntity entity) {
        SpecialSymbolsEntity symbol = (SpecialSymbolsEntity)specialSymbolsEntity;
        Cell cell = row.createCell(index);
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setFontName(symbol.getFont());
        Object rtext;
        if (cell instanceof HSSFCell) {
            rtext = new HSSFRichTextString(symbol.getUnicode());
            ((RichTextString)rtext).applyFont(font);
        } else {
            rtext = new XSSFRichTextString(symbol.getUnicode());
            ((RichTextString)rtext).applyFont(font);
        }

        cell.setCellValue((RichTextString)rtext);
        if (style != null) {
            cell.setCellStyle(style);
        }

    }

    protected int getListCellSize(List<ExcelExportEntity> list) {
        int cellSize = 0;
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            ExcelExportEntity ee = (ExcelExportEntity)var3.next();
            if (ee.getList() != null) {
                cellSize += this.getListCellSize(ee.getList());
            } else {
                ++cellSize;
            }
        }

        return cellSize;
    }

    public void createImageCell(Drawing patriarch, ExcelExportEntity entity, Row row, int i, String imagePath, Object obj) throws Exception {
        Cell cell = row.createCell(i);
        byte[] value = null;
        if (entity.getExportImageType() != 1) {
            if (entity.getMethods() == null && entity.getMethod() == null) {
                value = (byte[])((byte[])PoiPublicUtil.getParamsValue(entity.getKey().toString(), obj));
            } else {
                value = (byte[])((byte[])(entity.getMethods() != null ? this.getFieldBySomeMethod(entity.getMethods(), obj, entity.getMethodsParams()) : this.getFieldByMethod(entity.getMethod(), obj, entity.getMethodParams())));
            }
        }

        this.createImageCell(cell, 50.0 * entity.getHeight(), entity.getExportImageType() == 1 ? imagePath : null, value);
    }

    public void createImageCell(Cell cell, double height, String imagePath, byte[] data) throws Exception {
        if (height > (double)cell.getRow().getHeight()) {
            cell.getRow().setHeight((short)((int)height));
        }

        Object anchor;
        if (this.type.equals(ExcelType.HSSF)) {
            anchor = new HSSFClientAnchor(10, 10, 1010, 245, (short)cell.getColumnIndex(), cell.getRow().getRowNum(), (short)cell.getColumnIndex(), cell.getRow().getRowNum());
        } else {
            anchor = new XSSFClientAnchor(127000, 127000, 12827000, 3111500, (short)cell.getColumnIndex(), cell.getRow().getRowNum(), (short)cell.getColumnIndex(), cell.getRow().getRowNum());
        }

        if (StringUtils.isNotEmpty(imagePath)) {
            data = ImageCache.getImage(imagePath);
        }

        if (data != null) {
            PoiExcelGraphDataUtil.getDrawingPatriarch(cell.getSheet()).createPicture((ClientAnchor)anchor, cell.getSheet().getWorkbook().addPicture(data, this.getImageType(data)));
        }

    }

    public void createImageCell(Cell cell, double height, int rowspan, int colspan, String imagePath, byte[] data) throws Exception {
        if (height > (double)cell.getRow().getHeight()) {
            cell.getRow().setHeight((short)((int)height));
        }

        Object anchor;
        if (this.type.equals(ExcelType.HSSF)) {
            anchor = new HSSFClientAnchor(10, 10, 1010, 245, (short)cell.getColumnIndex(), cell.getRow().getRowNum(), (short)(cell.getColumnIndex() + colspan - 1), cell.getRow().getRowNum() + rowspan - 1);
        } else {
            anchor = new XSSFClientAnchor(10 * Units.EMU_PER_POINT, 10 * Units.EMU_PER_POINT, 1010 * Units.EMU_PER_POINT, 245 * Units.EMU_PER_POINT, (short) cell.getColumnIndex(), cell.getRow().getRowNum(), (short) (cell.getColumnIndex() + 1),
                    cell.getRow().getRowNum() + 1);
        }

        if (StringUtils.isNotEmpty(imagePath)) {
            data = ImageCache.getImage(imagePath);
        }

        if (data != null) {
            PoiExcelGraphDataUtil.getDrawingPatriarch(cell.getSheet()).createPicture((ClientAnchor)anchor, cell.getSheet().getWorkbook().addPicture(data, this.getImageType(data)));
        }

    }

    private int createIndexCell(Row row, int index, ExcelExportEntity excelExportEntity) {
        if (excelExportEntity.getFormat() != null && excelExportEntity.getFormat().equals("isAddIndex")) {
            this.createStringCell(row, 0, this.currentIndex + "", index % 2 == 0 ? this.getStyles(false, (ExcelExportEntity)null) : this.getStyles(true, (ExcelExportEntity)null), (ExcelExportEntity)null);
            ++this.currentIndex;
            return 1;
        } else {
            return 0;
        }
    }

    public void createListCells(Drawing patriarch, int index, int cellNum, Object obj, List<ExcelExportEntity> excelParams, Sheet sheet, Workbook workbook, short rowHeight) throws Exception {
        Row row;
        if (sheet.getRow(index) == null) {
            row = sheet.createRow(index);
            if (rowHeight != -1) {
                row.setHeight(rowHeight);
            }
        } else {
            row = sheet.getRow(index);
            if (rowHeight != -1) {
                row.setHeight(rowHeight);
            }
        }

        int k = 0;

        for(int paramSize = excelParams.size(); k < paramSize; ++k) {
            ExcelExportEntity entity = (ExcelExportEntity)excelParams.get(k);
            Object value = this.getCellValue(entity, obj);
            if (entity.getType() == BaseTypeConstants.STRING_TYPE) {
                this.createStringCell(row, cellNum++, value == null ? "" : value.toString(), row.getRowNum() % 2 == 0 ? this.getStyles(false, entity) : this.getStyles(true, entity), entity);
                if (entity.isHyperlink()) {
                    row.getCell(cellNum - 1).setHyperlink(this.dataHandler.getHyperlink(row.getSheet().getWorkbook().getCreationHelper(), obj, entity.getName(), value));
                }
            } else if (entity.getType() == BaseTypeConstants.DOUBLE_TYPE) {
                this.createDoubleCell(row, cellNum++, value == null ? "" : value.toString(), index % 2 == 0 ? this.getStyles(false, entity) : this.getStyles(true, entity), entity);
                if (entity.isHyperlink()) {
                    row.getCell(cellNum - 1).setHyperlink(this.dataHandler.getHyperlink(row.getSheet().getWorkbook().getCreationHelper(), obj, entity.getName(), value));
                }
            } else {
                this.createImageCell(patriarch, entity, row, cellNum++, value == null ? "" : value.toString(), obj);
            }
        }

    }

    public void createStringCell(Row row, int index, String text, CellStyle style, ExcelExportEntity entity) {
        Cell cell = row.createCell(index);
        if (style != null && style.getDataFormat() > 0 && style.getDataFormat() < 12) {
            cell.setCellValue(Double.parseDouble(text));
        } else {
            Object rtext;
            if (cell instanceof HSSFCell) {
                rtext = new HSSFRichTextString(text);
            } else {
                rtext = new XSSFRichTextString(text);
            }

            cell.setCellValue((RichTextString)rtext);
        }

        if (style != null) {
            cell.setCellStyle(style);
        }

        this.createCellComment(row, cell, text, entity);
        this.addStatisticsData(index, text, entity);
    }

    public void createDoubleCell(Row row, int index, String text, CellStyle style, ExcelExportEntity entity) {
        Cell cell = row.createCell(index);
        if (text != null && text.length() > 0) {
            try {
                cell.setCellValue(Double.parseDouble(text));
            } catch (NumberFormatException var8) {
                cell.setCellValue(text);
            }
        }

        if (style != null) {
            cell.setCellStyle(style);
        }

        this.createCellComment(row, cell, text, entity);
        this.addStatisticsData(index, text, entity);
    }

    private void createCellComment(Row row, Cell cell, String text, ExcelExportEntity entity) {
        if (this.commentHandler != null) {
            String comment = entity != null && !entity.getName().equals(text) && (entity.getGroupName() == null || !entity.getGroupName().equals(text)) ? this.commentHandler.getComment(entity.getName(), text) : this.commentHandler.getComment(text);
            if (StringUtils.isNotBlank(comment)) {
                cell.setCellComment(this.getComment(cell, comment, this.commentHandler.getAuthor()));
            }
        }

    }

    private Comment getComment(Cell cell, String commentText, String author) {
        Comment comment = null;
        if (cell instanceof HSSFCell) {
            comment = cell.getSheet().createDrawingPatriarch().createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short)3, 2, (short)5, commentText.length() / 15 + 2));
            comment.setString(new HSSFRichTextString(commentText));
        } else {
            comment = cell.getSheet().createDrawingPatriarch().createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 3, 2, 5, commentText.length() / 15 + 2));
            comment.setString(new XSSFRichTextString(commentText));
        }

        if (StringUtils.isNotBlank(author)) {
            comment.setAuthor(author);
        }

        return comment;
    }

    public void addStatisticsRow(CellStyle styles, Sheet sheet) {
        if (this.statistics.size() > 0) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("add statistics data ,size is {}", this.statistics.size());
            }

            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = this.statistics.keySet();
            this.createStringCell(row, 0, "合计", styles, (ExcelExportEntity)null);
            Iterator var5 = keys.iterator();

            while(var5.hasNext()) {
                Integer key = (Integer)var5.next();
                this.createStringCell(row, key, DOUBLE_FORMAT.format(this.statistics.get(key)), styles, (ExcelExportEntity)null);
            }

            this.statistics.clear();
        }

    }

    private void addStatisticsData(Integer index, String text, ExcelExportEntity entity) {
        if (entity != null && entity.isStatistics()) {
            Double temp = 0.0;
            if (!this.statistics.containsKey(index)) {
                this.statistics.put(index, temp);
            }

            try {
                temp = Double.valueOf(text);
            } catch (NumberFormatException var6) {
            }

            this.statistics.put(index, (Double)this.statistics.get(index) + temp);
        }

    }

    public int getImageType(byte[] value) {
        String type = PoiPublicUtil.getFileExtendName(value);
        if ("JPG".equalsIgnoreCase(type)) {
            return 5;
        } else {
            return "PNG".equalsIgnoreCase(type) ? 6 : 5;
        }
    }

    private Map<Integer, int[]> getMergeDataMap(List<ExcelExportEntity> excelParams) {
        Map<Integer, int[]> mergeMap = new HashMap();
        int i = 0;
        Iterator var4 = excelParams.iterator();

        while(true) {
            while(var4.hasNext()) {
                ExcelExportEntity entity = (ExcelExportEntity)var4.next();
                if (entity.isMergeVertical()) {
                    mergeMap.put(i, entity.getMergeRely());
                }

                if (entity.getList() != null) {
                    for(Iterator var6 = entity.getList().iterator(); var6.hasNext(); ++i) {
                        ExcelExportEntity inner = (ExcelExportEntity)var6.next();
                        if (inner.isMergeVertical()) {
                            mergeMap.put(i, inner.getMergeRely());
                        }
                    }
                } else {
                    ++i;
                }
            }

            return mergeMap;
        }
    }

    public CellStyle getStyles(boolean needOne, ExcelExportEntity entity) {
        return this.excelExportStyler.getStyles(needOne, entity);
    }

    public void mergeCells(Sheet sheet, List<ExcelExportEntity> excelParams, int titleHeight) {
        Map<Integer, int[]> mergeMap = this.getMergeDataMap(excelParams);
        PoiMergeCellUtil.mergeCells(sheet, mergeMap, titleHeight);
    }

    public void setCellWith(List<ExcelExportEntity> excelParams, Sheet sheet) {
        int index = 0;

        for(int i = 0; i < excelParams.size(); ++i) {
            if (((ExcelExportEntity)excelParams.get(i)).getList() != null) {
                List<ExcelExportEntity> list = ((ExcelExportEntity)excelParams.get(i)).getList();

                for(int j = 0; j < list.size(); ++j) {
                    sheet.setColumnWidth(index, (int)(256.0 * ((ExcelExportEntity)list.get(j)).getWidth()));
                    ++index;
                }
            } else {
                sheet.setColumnWidth(index, (int)(256.0 * ((ExcelExportEntity)excelParams.get(i)).getWidth()));
                ++index;
            }
        }

    }

    public void setColumnHidden(List<ExcelExportEntity> excelParams, Sheet sheet) {
        int index = 0;

        for(int i = 0; i < excelParams.size(); ++i) {
            if (((ExcelExportEntity)excelParams.get(i)).getList() != null) {
                List<ExcelExportEntity> list = ((ExcelExportEntity)excelParams.get(i)).getList();

                for(int j = 0; j < list.size(); ++j) {
                    sheet.setColumnHidden(index, ((ExcelExportEntity)list.get(j)).isColumnHidden());
                    ++index;
                }
            } else {
                sheet.setColumnHidden(index, ((ExcelExportEntity)excelParams.get(i)).isColumnHidden());
                ++index;
            }
        }

    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setExcelExportStyler(IExcelExportStyler excelExportStyler) {
        this.excelExportStyler = excelExportStyler;
    }

    public IExcelExportStyler getExcelExportStyler() {
        return this.excelExportStyler;
    }
}
