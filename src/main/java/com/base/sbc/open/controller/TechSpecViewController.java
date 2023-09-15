package com.base.sbc.open.controller;


import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
public class TechSpecViewController {

    @Autowired
    PackInfoService packInfoService;

    @GetMapping
    public Map<String, AttachmentVo> getFile(String foreignId, String packType) {
        Map<String, AttachmentVo> result = new HashMap<>(4);
        PackInfoListVo byQw = packInfoService.getDetail(foreignId, packType);
        result.put("pdf", Optional.ofNullable(byQw).map(PackInfoListVo::getTechSpecFile).orElse(null));
        result.put("video", Optional.ofNullable(byQw).map(PackInfoListVo::getTechSpecVideo).orElse(null));
        return result;
    }

}
