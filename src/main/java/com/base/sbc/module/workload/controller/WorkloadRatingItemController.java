/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.fabric.dto.DelDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingItemDTO;
import com.base.sbc.module.workload.listener.WorkloadRatingItemImportListener;
import com.base.sbc.module.workload.service.WorkloadRatingDetailService;
import com.base.sbc.module.workload.service.WorkloadRatingItemService;
import com.base.sbc.module.workload.vo.WorkloadRatingItemQO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 类描述：工作量评分选项配置 Controller类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.web.WorkloadRatingConfigController
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 */
@RestController
@Api(tags = "工作量评分选项值")
@RequestMapping(value = BaseController.SAAS_URL + "/workloadRatingItem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WorkloadRatingItemController extends BaseController {

    @Autowired
    private WorkloadRatingItemService workloadRatingItemService;

    @Autowired
    private WorkloadRatingDetailService workloadRatingDetailService;

    @Autowired
    private WorkloadRatingItemImportListener importListener;

    @ApiOperation(value = "分页查询")
    @GetMapping("queryPageInfo")
    public ApiResult<PageInfo<WorkloadRatingItemVO>> queryPageInfo(@Validated WorkloadRatingItemQO qo) {
        return selectSuccess(workloadRatingItemService.queryPageInfo(qo));
    }

    @ApiOperation(value = "删除")
    @PostMapping("delByIds")
    public ApiResult<Boolean> delByIds(@Validated @RequestBody DelDTO delDto) {
        return deleteSuccess(workloadRatingItemService.delByIds(delDto.getIds()));
    }

    @ApiOperation(value = "保存")
    @PostMapping("save")
    public ApiResult<List<WorkloadRatingItemDTO>> save(@Validated @RequestBody List<WorkloadRatingItemDTO> workloadRatingItemList) {
        workloadRatingItemService.save(workloadRatingItemList);
        return updateSuccess(workloadRatingItemList);
    }

    @ApiOperation(value = "保存")
    @PostMapping("importExcel")
    public ApiResult<String> importExcel(@RequestParam("file") MultipartFile file, WorkloadRatingItemQO excelQueryDto) throws IOException {
        importListener.setExcelQueryDto(excelQueryDto);
        try {
            EasyExcel.read(file.getInputStream(), importListener).doReadAllSync();
            String warnMsg = importListener.dataVerifyHandler();
            ApiResult<String> result = selectSuccess(String.format("导入成功. %s", warnMsg));
            if (StrUtil.isNotBlank(warnMsg)) {
                result.setMessage(String.format("%s, 请问错误是否需要导出?", warnMsg));
            }else {
                result.setStatus(200);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.error(String.format("导入失败, 请你根据导入规则进行导入\n%s", e.getMessage()), 0);
        }
    }

    @ApiOperation(value = "计算新的总和")
    @PostMapping("calculate")
    public ApiResult<WorkloadRatingDetailDTO> calculate(@Validated @RequestBody WorkloadRatingDetailDTO workloadRatingDetailDTO) {
        workloadRatingDetailService.save(workloadRatingDetailDTO);
        return updateSuccess(workloadRatingDetailDTO);
    }

}































