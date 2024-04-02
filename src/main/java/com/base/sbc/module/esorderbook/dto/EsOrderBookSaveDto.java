/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.dto;

import com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo;
import com.base.sbc.module.esorderbook.vo.EsOrderBookVo;
import lombok.Data;

import java.util.List;

@Data
public class EsOrderBookSaveDto{

    private static final long serialVersionUID = 1L;


    private EsOrderBookVo head;

    private List<EsOrderBookItemVo> itemList;

}
