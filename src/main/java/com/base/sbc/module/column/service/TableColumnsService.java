package com.base.sbc.module.column.service;

import com.base.sbc.client.amc.entity.Job;
import com.base.sbc.module.column.entity.TableColumns;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/10/13 21:56:49
 * @mail 247967116@qq.com
 */
public interface TableColumnsService extends BaseService<TableColumns> {
    /**
     * 根据表格编码和用户组id获取关联的列
     *
     * @param tableCode 表格编码
     * @param  jobs 角色集合
     * @return 列集合
     */
    List<TableColumns> listByTableCodeAndGroupIds(String tableCode, List<Job> jobs);
}
