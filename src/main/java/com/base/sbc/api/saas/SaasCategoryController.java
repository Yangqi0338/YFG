package com.base.sbc.api.saas;

import com.base.sbc.api.saas.entity.Page;
import com.base.sbc.basedata.entity.CategorySizeMethod;
import com.base.sbc.basedata.service.CategorySizeMethodService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 */
@RestController
@Api(tags="1.2 SAAS接口[定义品类尺寸量法与检验标准]")
@RequestMapping(value = BaseController.SAAS_URL+"/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SaasCategoryController extends BaseController{

    @Autowired
    private CategorySizeMethodService categorySizeMethodService;
    @Autowired
    private UserUtils userUtils;
    @ApiOperation(value="对某个品类进行尺寸量法定义(新增修改删除)(编写)", notes="品类名称(categoryName),size(所有的尺码，用逗号隔开，例如：x,xl,xxl),standard(每个尺码对应的标准值，用逗号隔开,例如：8.6,8.98,7),method,partName,tolerance(公差)")
    @PostMapping("/categorySizeMethods")
    public ApiResult definitionSize(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany,@RequestBody List<CategorySizeMethod> categorySizeMethod) {
        String categoryName = "";
        GroupUser users=userUtils.getUserBy(user);
        int i = 0;
        if(categorySizeMethod==null || categorySizeMethod.isEmpty()) {
            return categorySizeMethodService.insetAll(users,categorySizeMethod,categoryName,userCompany);
        }
        List<String> ids = IdGen.getIds(categorySizeMethod.size());
        for (CategorySizeMethod csm : categorySizeMethod) {
            if(StringUtils.isBlank(csm.getCategoryName())||StringUtils.isBlank(csm.getTolerance())
                    || StringUtils.isBlank(csm.getPartName()) ) {
                return updateAttributeNotRequirements(categorySizeMethod);
            }
            csm.setCompanyCode(userCompany);
            csm.setId(ids.get(i));
            csm.setDelFlag(BaseGlobal.DEL_FLAG_NORMAL);
            csm.setCreateDate(new Date());
            csm.setCreateId(users.getId());
            i++;
            categoryName = csm.getCategoryName();
        }

        return categorySizeMethodService.insetAll(users,categorySizeMethod,categoryName,userCompany);
    }

}
