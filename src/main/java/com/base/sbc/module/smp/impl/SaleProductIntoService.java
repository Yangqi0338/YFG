package com.base.sbc.module.smp.impl;


import com.base.sbc.config.common.BaseQueryWrapper;

import java.util.List;
import java.util.Map;

public interface SaleProductIntoService {

    public List<Map<String, Object>> querySaleIntoPage(BaseQueryWrapper qw, Integer total);
}
