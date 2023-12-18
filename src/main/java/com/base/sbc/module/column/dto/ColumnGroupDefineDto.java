package com.base.sbc.module.column.dto;

import com.base.sbc.module.column.entity.ColumnGroupDefine;
import com.base.sbc.module.column.entity.ColumnGroupDefineItem;
import lombok.Data;

import java.util.List;

@Data
public class ColumnGroupDefineDto {

    private ColumnGroupDefine columnGroupDefine;

    private List<ColumnGroupDefineItem> itemList;

    private String userGroupId;

}
