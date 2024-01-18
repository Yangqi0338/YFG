package com.base.sbc.module.operalog.dto;

import com.base.sbc.module.common.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/6/19 16:55:50
 * @mail 247967116@qq.com
 */
@Data
public class OperaLogDto extends BaseDto {
    /**
     * 单据id
     */
    private String documentId;

    private String documentCode;

    private List<String> documentCodeList;

    private String documentName;

    private String parentId;

    private String path;
    private String content;
    private String userCompany;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 模块名称
     */
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String[] createDate;

    public void init(){

    }
}
