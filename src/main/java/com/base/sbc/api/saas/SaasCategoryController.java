package com.base.sbc.api.saas;

import com.base.sbc.api.saas.entity.Page;
import com.base.sbc.api.saas.excel.ExportSizeTemplate;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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

    @GetMapping("/categorySizeMethods")
    public ApiResult getSizeMethodsByCn(Principal user, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode,
                                        Page page, String categoryName) {
        if(StringUtils.isBlank(categoryName)) {
            return selectAttributeNotRequirements("categoryName");
        }
        QueryCondition qc = new QueryCondition();

        // 页面条件
        if (StringUtils.isNoneBlank(page.getSql())) {
            qc.andConditionSql(page.getSql());
        }
        com.github.pagehelper.Page<CategorySizeMethod> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
        qc.andEqualTo(COMPANY_CODE, companyCode);
        qc.andEqualTo(CATEGORY_NAME, categoryName);
        categorySizeMethodService.findByCondition(qc);
        PageInfo<CategorySizeMethod> pageList = pages.toPageInfo();
        if (pageList.getList() != null && pageList.getList().size() > 0) {
            for (CategorySizeMethod categorySizeMethod : pageList.getList()) {
                if(StringUtils.isNoneBlank(categorySizeMethod.getSize())) {
                    categorySizeMethod.setSizeList(StringUtils.convertList(categorySizeMethod.getSize()));
                }
                if(StringUtils.isNoneBlank(categorySizeMethod.getStandard())) {
                    categorySizeMethod.setStandardList(StringUtils.convertList(categorySizeMethod.getStandard(),true));
                }
            }
            return selectSuccess(pageList);
        }
        return selectNotFound();
    }

    @ApiOperation(value = "尺寸量法导入模板下载", notes = "尺寸量法导入模板下载")
    @GetMapping("/downLoadTemplate")
    public void downLoadExcel(@RequestParam(value = "sizes", required = true) String sizes, HttpServletRequest request,
                              HttpServletResponse response) {
        String strFileName = "尺寸表模板.xlsx";
        OutputStream objStream = null;
        try {
            objStream = response.getOutputStream();
            response.reset();
            // 设置文件名称
            response.setContentType("application/x-msdownload");
            request.setCharacterEncoding("UTF-8");
            // 火狐浏览器
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(strFileName, "UTF-8"));
            ExportSizeTemplate excel = new ExportSizeTemplate();
            XSSFWorkbook objWb = excel.createWorkBook(sizes);
            objWb.write(objStream);
            objStream.flush();
            objStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
