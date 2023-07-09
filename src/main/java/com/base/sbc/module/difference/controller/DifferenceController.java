package com.base.sbc.module.difference.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.difference.entity.Difference;
import com.base.sbc.module.difference.service.DifferenceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/1 14:58
 * @mail 247967116@qq.com
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "物料颜色库")
@RequestMapping(value = BaseController.SAAS_URL + "/difference", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DifferenceController extends BaseController {
    private final DifferenceService differenceService;

    /**
     * 通过关联编码查询
     */
    @GetMapping("/getByCode")
    public ApiResult getByCode(String code) {
        return selectSuccess(differenceService.list(new QueryWrapper<Difference>().eq("range_difference_id", code)));
    }

    /**
     * 批量保存，修改，新增，删除
     */
    @PostMapping("/getByCode")
    public ApiResult getByCode(@RequestBody List<Difference> differenceList,String code) {

        differenceService.addAndUpdateAndDelList(differenceList,new BaseQueryWrapper<Difference>().eq("range_difference_id",code));
        return insertSuccess("操作成功");
    }

}
