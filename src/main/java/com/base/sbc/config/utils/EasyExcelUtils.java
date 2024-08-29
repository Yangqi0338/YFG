package com.base.sbc.config.utils;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.DefaultResourceLoader;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * EasyExcel 相关工具类
 */
@Slf4j
public class EasyExcelUtils {

    /**
     * Excel所有图片设置
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    //参数依次为图片字节，图片宽度(像素)，图片高度，行高(厘米)，列宽
    public static WriteCellData<Void> imageCells(byte[] bytes, Double imageWidth, Double imageHight, Double rowLength, Double columLength) throws IOException {

        //等比例缩小图片，直到图片能放在单元格下，每次缩小20%
        Integer top = 0;
        Integer left = 0;
        //厘米转换成像素
        rowLength = rowLength*28;
        columLength = columLength*28;
        while (true){
            if(imageHight < rowLength && imageWidth < columLength){
                //计算边框值
                top = Math.toIntExact(Math.round((rowLength - imageHight)/2));
                left = Math.toIntExact(Math.round((columLength - imageWidth)/2));
                break;
            }else {
                imageHight = imageHight*0.8;
                imageWidth = imageWidth*0.8;
            }
        }
        WriteCellData<Void> writeCellData = new WriteCellData<>();
        // 这里可以设置为 EMPTY 则代表不需要其他数据了
        //writeCellData.setType(CellDataTypeEnum.EMPTY);

        // 可以放入多个图片
        List<ImageData> imageDataList = new ArrayList<>();
        writeCellData.setImageDataList(imageDataList);


        ImageData imageData = new ImageData();
        imageDataList.add(imageData);
        // 设置图片
        imageData.setImage(bytes);
        // 图片类型
        //imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
        // 上 右 下 左 需要留空，这个类似于 css 的 margin；这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
        imageData.setTop(top);
        imageData.setRight(left);
        imageData.setBottom(top);
        imageData.setLeft(left);

        // * 设置图片的位置。Relative表示相对于当前的单元格index。first是左上点，last是对角线的右下点，这样确定一个图片的位置和大小。
        // 目前填充模板的图片变量是images，index：row=7,column=0。所有图片都基于此位置来设置相对位置
        // 第1张图片相对位置
        imageData.setRelativeFirstRowIndex(0);
        imageData.setRelativeFirstColumnIndex(0);
        imageData.setRelativeLastRowIndex(0);
        imageData.setRelativeLastColumnIndex(0);

        return writeCellData;
    }

    /**
     * 单模板多 sheet获取
     * @param path 路径
     * @param sheetCount sheet数量
     * @return 模板流
     */
    public static ByteArrayOutputStream templateSheetCreate(String path, Integer sheetCount) {
        try{
            InputStream fileInputStream = new DefaultResourceLoader().getResource(path).getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            //设置模板的第一个sheet的名称
            workbook.setSheetName(0, "Sheet0");
            for (int i = 0; i < sheetCount-1; i++) {
                // 复制模板，得到第i个sheet
                int num = i + 1;
                workbook.cloneSheet(0, "Sheet" + num);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //模板写到流里
            workbook.write(bos);
            return bos;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载excel
     * @param fileName 文件名称
     * @param bos 模板流
     * @param response 响应
     */
    public static void downLoadExcel(String fileName, ByteArrayOutputStream bos, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(bos.size()));
            out.write(bos.toByteArray() );
        } catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static class ImageModifyHandler implements CellWriteHandler {

        // 256分之一的字符宽度转换为标准字符宽度。
        public static Integer standardCharacterWidth = 256;

        // 7.5是一个估算的字符到像素的转换因子
        public static Float character2PixelFactor = 7.5f;

        // 将点转换为英寸，因为1点 = 1/72英寸。
        public static Integer pixel2InchFactor = 72;

        // 英寸转换为像素，其中96是常用的DPI（每英寸像素数）值。
        public static Integer dpi = 96;

        // 行高与像素的转换因子
        public static Float rowHeight2PixelFactor = 1.3333f;

        // 边距
        public static Integer spacingFactor = 5;

        /**
         * 后单元格数据转换
         *
         * @param writeSheetHolder 写单夹
         * @param writeTableHolder 写表夹
         * @param cellData         单元格数据
         * @param cell             细胞
         * @param head             头
         * @param relativeRowIndex 相对行索引
         * @param isHead           是头
         */
        @Override
        public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                           WriteCellData<?> cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            boolean noImageValue = Objects.isNull(cellData) || cellData.getImageDataList() == null || cellData.getImageDataList().size() == 0;
            if (Objects.equals(Boolean.TRUE, isHead) || noImageValue) {
                return;
            }
            Sheet sheet = cell.getSheet();
            int mergeColumNum = getMergeColumNum(cell, sheet);
            int mergeRowNum = getMergeRowNum(cell, sheet);
            ImageData imageData = cellData.getImageDataList().get(0);
            imageData.setRelativeLastRowIndex(mergeRowNum - 1);
            imageData.setRelativeLastColumnIndex(mergeColumNum - 1);

            // 处理图像缩放 - 等比例缩放
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData.getImage());
                java.awt.image.BufferedImage image = ImageIO.read(bis);
                int targetWidth = (int) (sheet.getColumnWidth(cell.getColumnIndex()) / standardCharacterWidth * character2PixelFactor);
                int targetHeight = (int) (cell.getRow().getHeightInPoints() / pixel2InchFactor * dpi);

                // 计算图像的缩放比例
                double scaleX = (double) targetWidth / image.getWidth();
                double scaleY = (double) targetHeight / image.getHeight();
                double scale = Math.min(scaleX, scaleY);

                // 计算缩放后的图像大小
                int scaledWidth = (int) (image.getWidth() * scale) - spacingFactor;
                int scaledHeight = (int) (image.getHeight() * scale) - spacingFactor;

                // 计算上下左右四个角的空白
                int topPadding = (targetHeight - scaledHeight) / 2;
                int bottomPadding = targetHeight - scaledHeight - topPadding;
                int leftPadding = (targetWidth - scaledWidth) / 2;
                int rightPadding = targetWidth - scaledWidth - leftPadding;

                // 行高（点）= 像素高度 / 1.3333
                imageData.setTop((int) (topPadding / rowHeight2PixelFactor));
                imageData.setBottom((int) (bottomPadding / rowHeight2PixelFactor));
                imageData.setLeft((int) (leftPadding / rowHeight2PixelFactor));
                imageData.setRight((int) (rightPadding / rowHeight2PixelFactor));

                bis.close();
            } catch (Exception e) {
            }

            CellWriteHandler.super.afterCellDataConverted(writeSheetHolder, writeTableHolder, cellData, cell, head, relativeRowIndex, isHead);
        }


        /**
         * 得到合并行num
         *
         * @param cell  细胞
         * @param sheet 表
         * @return int
         */
        public static int getMergeRowNum(Cell cell, Sheet sheet) {
            int mergeSize = 1;
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            for (CellRangeAddress cellRangeAddress : mergedRegions) {
                if (cellRangeAddress.isInRange(cell)) {
                    //获取合并的行数
                    mergeSize = cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow() + 1;
                    break;
                }
            }
            return mergeSize;
        }

        /**
         * 得到合并列num
         *
         * @param cell  细胞
         * @param sheet 表
         * @return int
         */
        public static int getMergeColumNum(Cell cell, Sheet sheet) {
            int mergeSize = 1;
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            for (CellRangeAddress cellRangeAddress : mergedRegions) {
                if (cellRangeAddress.isInRange(cell)) {
                    //获取合并的列数
                    mergeSize = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
                    break;
                }
            }
            return mergeSize;
        }

    }
}
