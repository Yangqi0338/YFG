package com.base.sbc.api.saas.pdm;

import cn.hutool.core.lang.Snowflake;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.pdm.entity.Planning;
import com.base.sbc.pdm.service.PlanningService;
import freemarker.template.utility.SecurityUtilities;
import io.swagger.annotations.Api;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 卞康
 * @date 2023/3/17 14:17:04
 */
@RestController
@Api(tags="1.2 SAAS接口[企划接口]")
@RequestMapping(value = BaseController.SAAS_URL+"/planning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PlanningController extends BaseController{

    @Resource
    private PlanningService planningService;

    @Resource
    private UserUtils userUtils;
    @PostMapping("/add")
    public ApiResult add(Planning planning){
        GroupUser user = userUtils.getUser(getUserId());
        planning.setId(IdGen.getId().toString());
        Date date =new Date();
        planning.setCreateDate(date);
        planning.setUpdateDate(date);
        planning.setCreateId(getUserId());
        planning.setUpdateId(getUserId());
        planning.setCreateName(user.getName());
        planning.setUpdateName(user.getName());
        return selectSuccess(planningService.insert(planning));
    }
}
