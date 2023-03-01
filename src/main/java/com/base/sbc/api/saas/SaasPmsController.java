package com.base.sbc.api.saas;

import com.base.sbc.api.saas.excel.ExportSizeTemplate;
import com.base.sbc.config.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author pjsqwerty
 * @date 创建时间: 2020/6/19
 */
@RestController
@Api(tags = "3.1 Pms基础接口  ")
@RequestMapping(value = BaseController.SAAS_URL + "/pms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SaasPmsController extends BaseController {


    @ApiOperation(value = "导出打色申请表(业务)", notes = "导出打色申请表(业务)")
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
