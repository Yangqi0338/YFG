package com.base.sbc.config.utils;

import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TemplateExcelUtils {
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


}
