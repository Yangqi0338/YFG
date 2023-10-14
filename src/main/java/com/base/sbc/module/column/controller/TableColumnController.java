package com.base.sbc.module.column.controller;

import com.base.sbc.client.amc.entity.Group;
import com.base.sbc.client.amc.entity.Job;
import com.base.sbc.client.amc.entity.UserGroup;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.column.entity.TableColumns;
import com.base.sbc.module.column.service.TableColumnsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/10/13 19:25:06
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "表格列配置")
@RequestMapping(value = BaseController.SAAS_URL + "/tableColumn", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class TableColumnController extends BaseController{

    private final AmcService amcService;
    private final TableColumnsService tableColumnsService;
    /**
     * 根据表格编码获取列
     */
    @ApiOperation(value = "根据表格编码获取列")
    @GetMapping("/getTableColumnByTableCode")
    public ApiResult getTableColumnByTableCode(String tableCode, @RequestHeader("userId") String userId) {
        List<Job> jobs = amcService.getByUserId(userId);

        //根据用户组id和表格编码获取关联的列
        List<TableColumns> tableColumns = tableColumnsService.listByTableCodeAndGroupIds(tableCode, jobs);
        return selectSuccess(tableColumns);
    }
}
