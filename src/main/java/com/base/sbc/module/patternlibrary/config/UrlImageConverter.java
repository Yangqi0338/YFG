package com.base.sbc.module.patternlibrary.config;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片转换器
 *
 * @author XHTE
 * @create 2024-03-22
 */
@Slf4j
public class UrlImageConverter implements Converter<URL> {
    public static int urlConnectTimeout = 1000;
    public static int urlReadTimeout = 5000;

    public UrlImageConverter() {
    }

    public Class<?> supportJavaTypeKey() {
        return URL.class;
    }

    public WriteCellData<?> convertToExcelData(URL value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws IOException {
        InputStream inputStream = null;

        WriteCellData writeCellData;
        try {
            URLConnection urlConnection = value.openConnection();
            urlConnection.setConnectTimeout(urlConnectTimeout);
            urlConnection.setReadTimeout(urlReadTimeout);
            inputStream = urlConnection.getInputStream();
            byte[] bytes = IoUtils.toByteArray(inputStream);
            writeCellData = new WriteCellData(bytes);

        } catch (Exception e) {
            // 报错不停止 直接返回空
            log.error("版型库导出图片转换失败！", e);
            return new WriteCellData(new byte[0]);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

        }

        return writeCellData;
    }
}