package com.base.sbc.module.column.service.impl;

import com.base.sbc.module.column.TableColumnsMapper;
import com.base.sbc.module.column.entity.TableColumns;
import com.base.sbc.module.column.service.TableColumnsService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/10/13 21:58:19
 * @mail 247967116@qq.com
 */
@Service
public class TableColumnsServiceImpl extends BaseServiceImpl<TableColumnsMapper, TableColumns> implements TableColumnsService {
    /**
     * @param tableCode
     * @param groupIds
     * @return
     */
    @Override
    public List<TableColumns> listByTableCodeAndGroupIds(String tableCode, List<String> groupIds) {

        return null;
    }
}
