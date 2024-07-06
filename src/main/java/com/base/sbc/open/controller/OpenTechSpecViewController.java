package com.base.sbc.open.controller;


import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioConfig;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.utils.GenTechSpecPdfFile;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
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
@RequestMapping(value = BaseController.SAAS_URL + "/techSpecView")
public class OpenTechSpecViewController {

    @Autowired
    PackInfoService packInfoService;
    @Autowired
    AttachmentService attachmentService;
    @Resource
    private UploadFileService uploadFileService;
    @Resource
    private PackInfoStatusService packInfoStatusService;

    @Autowired
    private MinioConfig minioConfig;
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
    public String html(String foreignId, String packType, String userId, String minioDomain, HttpServletResponse response) throws IOException {
        PackCommonSearchDto dto = new PackCommonSearchDto();
        dto.setForeignId(foreignId);
        dto.setPackType(packType);

//        GenTechSpecPdfFile genTechSpecPdfFile = packInfoService.queryGenTechSpecPdfFile(null, dto);
        PackInfoListVo detail = packInfoService.getDetail(foreignId, dto.getPackType());

        if(ObjectUtils.isNotNull(detail)) {
            return   detail.getTechSpecFile().getUrl();
//                response.sendRedirect(detail.getTechSpecFile().getUrl());
        }
        return "";
//        genTechSpecPdfFile.setPdfView(false);
//        String html;
//        try {
//            html = genTechSpecPdfFile.toHtml();
//            html = html.replaceAll(minioDomain, "/minio");
//        } catch (Exception e) {
//            html = e.getMessage();
//        }
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter writer = response.getWriter();
//        writer.write(html);
//        writer.flush();
//        writer.close();
    }

    @ApiOperation(value = "生成工艺说明文件")
    @GetMapping("/newPdfHtml")
    public String genTechSpecFile2Html(Model model, Principal user, @Valid PackCommonSearchDto dto) {
        GroupUser groupUser = userUtils.getUserBy(user);
        Pair<String, JSONObject> pair = packInfoService.genTechSpecFile2Html(groupUser, dto);
        model.addAllAttributes(pair.getValue());
        return pair.getKey();
    }

}
