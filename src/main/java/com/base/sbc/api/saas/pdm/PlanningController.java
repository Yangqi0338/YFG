package com.base.sbc.api.saas.pdm;

import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.pdm.entity.Band;
import com.base.sbc.pdm.entity.Planning;
import com.base.sbc.pdm.service.PlanningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/17 14:17:04
 */
@RestController
@Api(tags = "1.2 SAAS接口[企划接口]")
@RequestMapping(value = BaseController.SAAS_URL + "/planning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PlanningController extends BaseController {

    @Resource
    private PlanningService planningService;

    @PostMapping("/add")
    public ApiResult add(@RequestBody Planning planning) {
        QueryCondition qc = new QueryCondition();
        qc.andEqualTo("planning_name",planning.getPlanningName());
        Planning byCondition = planningService.getByCondition(qc);
        if (byCondition!=null){
            return insertDataRepeat();
        }
        GroupUser user = getUser();
        planning.setId(IdGen.getId().toString());
        Date date = new Date();
        planning.setCreateDate(date);
        planning.setUpdateDate(date);
        planning.setCreateId(getUserId());
        planning.setUpdateId(getUserId());
        planning.setCreateName(user.getName());
        planning.setUpdateName(user.getName());
        return insertSuccess(planningService.insert(planning));
    }

    /**
     * 条件查询列表
     */
    @GetMapping("/listQuery")
    public ApiResult listQuery(Planning planning, Page page) {
        PageHelper.startPage(page);
        List<Planning> planningList = planningService.listQuery(planning);
        PageInfo<Planning> pageInfo = new PageInfo<>(planningList);
        return selectSuccess(pageInfo);
    }
    @GetMapping("/getById")
    public ApiResult getById(String id) {
        return selectSuccess(planningService.getById(id));
    }

    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String[] ids){
        return selectSuccess(planningService.delByIds(ids));
    }

    @DeleteMapping("/update")
    public ApiResult update(Planning planning){
        return selectSuccess(planningService.update(planning));
    }
}
