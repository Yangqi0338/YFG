package com.base.sbc.module.style.vo;


import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingListVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 类描述：样衣明细
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SampleVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 11:48
 */
@Data
@ApiModel("款式设计明细返回分享供应商实体 SampleVo ")
public class StyleSupplierVo extends StyleVo {

    //打版信息
    private PatternMakingListVo patternMaking;

    //动态字段集合
    private Map<String, List<FieldManagementVo>> filedMap;

}
