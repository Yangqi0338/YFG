package com.base.sbc.module.common.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.service.FtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 卞康
 * @date 2023/5/26 14:49:43
 * @mail 247967116@qq.com
 */
@RestController
@RequestMapping(BaseController.SAAS_URL + "/ftp")
@RequiredArgsConstructor
public class FtpController extends BaseController {

    private final FtpService ftpService;


    @PostMapping("/upload")
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String remotePath = "/Image";
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            boolean success = ftpService.uploadFile(remotePath, fileName, inputStream);
            if (success) {
                return selectSuccess("上传成功");
            } else {
                return selectSuccess("上传失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        ftpService.disconnect();
    }
}
