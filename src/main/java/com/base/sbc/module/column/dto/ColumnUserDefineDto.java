package com.base.sbc.module.column.dto;

import com.base.sbc.module.column.entity.ColumnUserDefine;
import com.base.sbc.module.column.entity.ColumnUserDefineItem;
import lombok.Data;

import java.util.List;

@Data
public class ColumnUserDefineDto {

    private ColumnUserDefine columnUserDefine;

    private List<ColumnUserDefineItem> itemList;

}
