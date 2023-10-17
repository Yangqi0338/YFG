package com.base.sbc.module.column.service.impl;

import com.base.sbc.client.amc.entity.Job;
import com.base.sbc.module.column.TableColumnsMapper;
import com.base.sbc.module.column.entity.TableColumns;
import com.base.sbc.module.column.service.TableColumnsService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/10/13 21:58:19
 * @mail 247967116@qq.com
 */
@Service
public class TableColumnsServiceImpl extends BaseServiceImpl<TableColumnsMapper, TableColumns> implements TableColumnsService {

    /**
     * 根据表格编码和用户组id获取关联的列
     *
     * @param tableCode 表格编码
     * @param jobs      角色集合
     * @return 列集合
     */
    @Override
    public List<TableColumns> listByTableCodeAndGroupIds(String tableCode, List<Job> jobs) {
        // 先模拟数据,管理员全部字段,下单员只看供应商物料号,单位,颜色(通用),厂家有效门幅,单件用量,损耗
        // supplierMaterialCode,purchaseUnitName,stockUnitName,translate,designUnitUse,bulkUnitUse,lossRate
        List<TableColumns> tableColumns = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        // if (jobs.stream().anyMatch(job -> job.getName().equals("系统管理员"))) {
        //     // 管理员
        //     columnNames.add("*");
        // } else

        if (jobs.stream().anyMatch(job -> "下单员".equals(job.getName()) || "外发技术下单员".equals(job.getName())) && jobs.size()==1) {
            // 下单员
            columnNames.add("row-select");
            columnNames.add("scmSendFlag");
            columnNames.add("collocationName");
            columnNames.add("mainFlag");
            columnNames.add("imageUrl");
            columnNames.add("materialCodeName");

            columnNames.add("supplierMaterialCode");
            columnNames.add("purchaseUnitName");
            columnNames.add("stockUnitName");
            columnNames.add("colorCode");
            columnNames.add("translate");
            columnNames.add("partName");
            columnNames.add("bulkUnitUse");
            columnNames.add("lossRate");

        }else {
            columnNames.add("*");
        }

        for (String columnName : columnNames) {
            TableColumns tableColumn = new TableColumns();
            tableColumn.setTitle(columnName);
            tableColumns.add(tableColumn);
        }
        return tableColumns;
    }
}
