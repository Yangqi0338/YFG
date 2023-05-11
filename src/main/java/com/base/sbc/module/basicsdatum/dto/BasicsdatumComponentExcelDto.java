package com.base.sbc.module.basicsdatum.dto;


import cn.hutool.socket.aio.ReadHandler;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Data

@TableName(value = "BasicsdatumComponentExcelDto")
public class BasicsdatumComponentExcelDto {
    /** 编码 */
    @ExcelProperty(value = "编码"  )
    private String coding;

    /** 部件类别 */
    @ExcelProperty(value = "部件类别"  )
    private String componentCategory;

    /** 工艺项目 */
    @ExcelProperty(value = "工艺项目"  )
    private String technologyProject;

    /** 描述 */
    @ExcelProperty(value = "描述"  )
    private String description;

    /** 图片 */
    @ExcelProperty(value = "图片"  )
    private File imageFile;



    /** 可用的 */
    @ExcelProperty(value = "可用的"  )
    private String status;


    /**  创建者名称 */
    @ExcelProperty(value = "创建人"  )
    protected String createName;

    /** 创建日期 */
    @ExcelProperty(value = "创建" )
    @DateTimeFormat("yyyy/MM/dd hh:mm:ss")
    protected Date createDate;

    /** 更新者名称  */
    @ExcelProperty(value = "修改者"  )
    protected String updateName;


    /** 更新日期 */
    @ExcelProperty(value = "修改"  )
    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    protected Date updateDate;

}
