package com.base.sbc.config;

import cn.afterturn.easypoi.cache.manager.FileLoaderImpl;
import cn.afterturn.easypoi.cache.manager.IFileLoader;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author 卞康
 * @date 2023/11/14 10:35:12
 * @mail 247967116@qq.com
 */
public class IFileLoaderImpl implements IFileLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLoaderImpl.class);

    @Override
    public byte[] getFile(String url) {
        InputStream fileis = null;
        ByteArrayOutputStream baos = null;
        try {

            //判断是否是网络地址
            if (url.startsWith("http")) {
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                try {
                    fileis = urlConnection.getInputStream();
                }catch (Exception e){
                    ClassPathResource classPathResource = new ClassPathResource("static/img/noImage.png");
                    fileis = classPathResource.getInputStream();
                }

            } else {
                //先用绝对路径查询,再查询相对路径
                try {
                    fileis = new FileInputStream(url);
                } catch (FileNotFoundException e) {
                    //获取项目文件
                    fileis = FileLoaderImpl.class.getClassLoader().getResourceAsStream(url);
                }
            }

            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            if (fileis != null) {
                while ((len = fileis.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fileis);
            IOUtils.closeQuietly(baos);
        }
        LOGGER.error(fileis + "这个路径文件没有找到,请查询");
        return null;
    }
}
