package com.base.sbc.module.common.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.eumns.UreportDownEnum;
import com.base.sbc.module.common.vo.AttachmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：Ureport 工具
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.service.UreportService
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-15 19:38
 */
@Component
public class UreportService {

    @Value("${ureport.url}")
    private String url;

    @Value("${ureport.port}")
    private String port;

    @Value("${ureport.mysql}")
    private String mysql;

    @Autowired
    private UploadFileService uploadFileService;


    /**
     * 从Ureport 下载文件 并上传到minio
     *
     * @param billType 打印模板配置.单据类型
     * @param name     打印模板配置.名称
     * @param fileName 上传到minio 的文件名称,请带着后缀
     * @param params   报表参数
     * @return
     */
    public AttachmentVo downFileAndUploadMinio(UreportDownEnum ureportDown, String billType, String name, String fileName, Map<String, String> params) {

        try {
            //1 组装url eg http://10.98.250.44:9111/ureport/pdf?_u=mysql:677447590605750272-%25E5%25B7%25A5%25E8%2589%25BA%25E5%258D%2595.ureport.xml&billType=process&styleId=1670714798356213761
            Map<String, String> paramsMap = new HashMap<>(16);
            paramsMap.put("url", url);
            paramsMap.put("port", port);
            paramsMap.put("mysql", mysql);
            paramsMap.put("billType", billType);
            paramsMap.put("downType", ureportDown.getDownType());
            paramsMap.put("name", URLUtil.encode(URLUtil.encode(name)));
            String url1 = StrUtil.format(ureportDown.getUrlTemplate(), paramsMap);
            String urlParams = URLUtil.buildQuery(params, Charset.defaultCharset());
            String url = url1 + urlParams;
            System.out.println("url:" + url);
            //2 下载
            byte[] bytes = HttpUtil.downloadBytes(url);
            //3 上传到minio
            MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, FileUtil.getMimeType(fileName), new ByteArrayInputStream(bytes));
            return uploadFileService.uploadToMinio(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("生成文件失败");
        }
    }
}
