package com.base.sbc.module.categorysize.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.categorysize.excel.ExportSizeTemplate;
import com.base.sbc.module.categorysize.entity.CategorySizeMethod;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.categorysize.service.CategorySizeMethodService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author 卞康
 */
@RestController
@Api(tags = "1.2 SAAS接口[定义品类尺寸量法与检验标准]")
@RequestMapping(value = BaseController.SAAS_URL + "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CategorySizeController extends BaseController {

    @Resource
    private CategorySizeMethodService categorySizeMethodService;

    @ApiOperation(value = "对某个品类进行尺寸量法定义(新增修改删除)(编写)", notes = "品类名称(categoryName),size(所有的尺码，用逗号隔开，例如：x,xl,xxl),standard(每个尺码对应的标准值，用逗号隔开,例如：8.6,8.98,7),method,partName,tolerance(公差)")
    @PostMapping("/categorySizeMethods")
    public ApiResult definitionSize(String categoryName, @RequestBody List<CategorySizeMethod> categorySizeMethod) {
        return updateSuccess(categorySizeMethodService.updateList(categorySizeMethod,categoryName));
    }

    @GetMapping("/categorySizeMethods")
    public ApiResult getSizeMethodsByCn(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode,
                                        Page page, String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return selectAttributeNotRequirements("categoryName");
        }

        QueryWrapper<CategorySizeMethod> qc = new QueryWrapper<>();
        qc.eq(COMPANY_CODE, companyCode);
        qc.eq(CATEGORY_NAME, categoryName);
        com.github.pagehelper.Page<CategorySizeMethod> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
        categorySizeMethodService.list(qc);
        PageInfo<CategorySizeMethod> pageList = pages.toPageInfo();
        if (pageList.getList() != null && pageList.getList().size() > 0) {
            for (CategorySizeMethod categorySizeMethod : pageList.getList()) {
                if (StringUtils.isNoneBlank(categorySizeMethod.getSize())) {
                    categorySizeMethod.setSizeList(StringUtils.convertList(categorySizeMethod.getSize()));
                }
                if (StringUtils.isNoneBlank(categorySizeMethod.getStandard())) {
                    categorySizeMethod.setStandardList(StringUtils.convertList(categorySizeMethod.getStandard(), true));
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
