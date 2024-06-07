package com.base.sbc.module.pushrecords.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.resttemplate.RestTemplateService;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.pushrecords.dto.PushRecordsDto;
import com.base.sbc.module.pushrecords.entity.PushRecords;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/7/11 10:46:08
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "推送日志")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/pushRecords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PushRecordsController extends BaseController {
    private final PushRecordsService pushRecordsService;
    private final RestTemplateService restTemplateService;

    @GetMapping("/queryPage")
    public ApiResult queryPage(PushRecordsDto pushRecordsDto) {
        BaseQueryWrapper<PushRecords> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("push_status",pushRecordsDto.getPushStatus());
        queryWrapper.notEmptyIn("module_name",pushRecordsDto.getModuleName());
        queryWrapper.notEmptyIn("function_name",pushRecordsDto.getFunctionName());
        queryWrapper.notEmptyIn("related_id",pushRecordsDto.getRelatedId());
        queryWrapper.notEmptyIn("related_name",pushRecordsDto.getRelatedName());
        queryWrapper.notEmptyLike("create_name",pushRecordsDto.getCreateName());
        queryWrapper.notEmptyLike("business_code",pushRecordsDto.getBusinessCode());
        if(StrUtil.isNotEmpty(pushRecordsDto.getType())){
            queryWrapper.isNotNullStr("business_code");
        }
        queryWrapper.notEmptyLike("business_code",pushRecordsDto.getBusinessCode());
        queryWrapper.between("create_date",pushRecordsDto.getCreateDate());
        queryWrapper.orderByDesc("create_date");
        PageHelper.startPage(pushRecordsDto);
        List<PushRecords> list = pushRecordsService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }


    /**
     * 重推
     */
    @PostMapping("/rePush")
    public ApiResult rePush(String id){
        boolean rePush = pushRecordsService.rePush(id);
        ApiResult<Object> result = ApiResult.success("重推成功");
        result.setSuccess(rePush);
        if (!rePush){
            result.setMessage("重推失败");
        }
        return result;
    }

    /**
     * 重推
     */
    @PostMapping("/batchRePushNewLog")
    public ApiResult batchRePushNewLog(@RequestBody List<String> ids){
        int i = pushRecordsService.batchRePushNewLog(ids);
        if (ids.size() == i) {
            return ApiResult.success("重推：" + ids.size() + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("重推：" + ids.size() + "条，成功：" + i + "条,失败：" + (ids.size() - i) + "条", 200);
        }
    }

    /**
     * 获取查询条件列表
     */
    @GetMapping("/getColumnList")
    public ApiResult getColumnList(String column){
        QueryWrapper<PushRecords> queryWrapper = new QueryWrapper<>();
        String underScoreCase = StringUtils.toUnderScoreCase(column);
        queryWrapper.select(underScoreCase);
        queryWrapper.groupBy(underScoreCase);
        List<PushRecords> list = pushRecordsService.list(queryWrapper);
        List<Map<String, Object>> collect = list.stream().map(item -> {
            Map<String, Object> one = new HashMap<>();
            Object val=BeanUtil.getProperty(item,column);
            one.put("value",val);
            one.put("label",val);
            return one;
        }).collect(Collectors.toList());
        return selectSuccess(collect);
    }
}
