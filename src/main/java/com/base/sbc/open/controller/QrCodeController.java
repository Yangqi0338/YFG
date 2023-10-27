package com.base.sbc.open.controller;

import cn.hutool.core.lang.Opt;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.base.sbc.config.common.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 类描述：二维码开放接口
 * @address com.base.sbc.open.controller.QrCodeController
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-26 11:23
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/qrCode")
public class QrCodeController {


        @GetMapping
        public void getQrCode(HttpServletResponse response, String content){
            try {
                String qrCodeContent = Opt.ofBlankAble(content).orElse("无内容");
                QrConfig qrConfig = new QrConfig(128, 128);
                qrConfig.setMargin(2);
                BufferedImage image = QrCodeUtil.generate(qrCodeContent, qrConfig);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] bytes = baos.toByteArray();
                response.setContentType("image/png");
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
