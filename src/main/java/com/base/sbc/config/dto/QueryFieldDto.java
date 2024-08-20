package com.base.sbc.config.dto;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

@Data
public class QueryFieldDto extends Page {

    //列头全量匹配
    private String columnHeard;

    //列头查询
    Map<String,String> fieldQueryMap;

    //列头code
    private String tableCode;

    private String queryFieldColumn;

    private String orderBy;

    @Override
    public String getOrderBy() {
        if (StrUtil.isNotBlank(this.orderBy)) return this.orderBy;
        if (MapUtil.isEmpty(fieldOrderMap)) return getDefaultOrderBy();
        StrJoiner joiner = StrJoiner.of(COMMA);
        return joiner.append(fieldOrderMap
                .entrySet().stream().map(it -> String.format("%s %s", it.getKey(), it.getValue())).collect(Collectors.toList())
        ).toString();
    }

    private Map<String, String> fieldOrderMap;

    /*是否导出图片*/
    private String imgFlag;

    private List<String> idList;

    // 是否列头匹配
    private boolean columnGroupSearch;

    public void notRequiredDownloadImage() {
        YesOrNoEnum rightStatus = YesOrNoEnum.YES;
        if (rightStatus.getValueStr().equals(imgFlag)) imgFlag = (rightStatus.getValue() + 1) + "";
    }

    public String getDefaultOrderBy() {
        return "";
    }

    public void syncOrderBy() {
        this.orderBy = getOrderBy();
    }
}
