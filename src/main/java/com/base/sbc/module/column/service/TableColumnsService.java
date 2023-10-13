package com.base.sbc.module.column.service;

import com.base.sbc.module.column.entity.TableColumns;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/10/13 21:56:49
 * @mail 247967116@qq.com
 */
public interface TableColumnsService extends BaseService<TableColumns> {
    List<TableColumns> listByTableCodeAndGroupIds(String tableCode, List<String> groupIds);
}
