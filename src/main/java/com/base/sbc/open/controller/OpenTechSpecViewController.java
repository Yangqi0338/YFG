package com.base.sbc.open.controller;


import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.utils.GenTechSpecPdfFile;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：工艺单视频 PDF 预览
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.open.controller.TechSpecViewController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-15 16:51
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/techSpecView")
public class OpenTechSpecViewController {

    @Autowired
    PackInfoService packInfoService;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    UserUtils userUtils;

    @GetMapping
    public Map<String, Object> getFile(String foreignId, String packType, String userId) {
        // GroupUser user = userUtils.getUser(userId);
        PackCommonSearchDto dto = new PackCommonSearchDto();
        dto.setForeignId(foreignId);
        dto.setPackType(packType);
        Map<String, Object> result = new HashMap<>(4);
        PackInfoListVo byQw = packInfoService.getDetail(foreignId, packType);
        if (byQw == null) {
            throw new OtherException("获取资料包信息失败");
        }
        result.put("video", byQw.getTechSpecVideo());
        GenTechSpecPdfFile genTechSpecPdfFile = packInfoService.queryGenTechSpecPdfFile(null, dto);
        genTechSpecPdfFile.setPdfView(false);
        String html;
        try {
            html = genTechSpecPdfFile.toHtml();
        } catch (Exception e) {
            html = e.getMessage();
        }
        result.put("html", html);
        return result;
    }

    @GetMapping("pdfHtml")
    public void html(String foreignId, String packType, String userId, String minioDomain, HttpServletResponse response) throws IOException {
        PackCommonSearchDto dto = new PackCommonSearchDto();
        dto.setForeignId(foreignId);
        dto.setPackType(packType);

        GenTechSpecPdfFile genTechSpecPdfFile = packInfoService.queryGenTechSpecPdfFile(null, dto);
        genTechSpecPdfFile.setPdfView(false);
        String html;
        try {
            html = genTechSpecPdfFile.toHtml();
            html = html.replaceAll(minioDomain, "/minio");
        } catch (Exception e) {
            html = e.getMessage();
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(html);
        writer.flush();
        writer.close();
    }

}
