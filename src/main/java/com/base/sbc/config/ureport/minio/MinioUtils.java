package com.base.sbc.config.ureport.minio;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.*;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.DateUtils;
import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MinioUtils {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 使用MultipartFile进行文件上传
     * @param file
     * @param objectName
     * @param contentType
     * @return 文件地址
     */
    public String uploadFile(MultipartFile file, String objectName, String contentType) {

        return uploadFile(minioConfig.getBucketName(),file,objectName,contentType);
    }
    public String uploadFile(String bucketName,MultipartFile file, String objectName, String contentType){
        try {

            InputStream inputStream = file.getInputStream();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).contentType(contentType).stream(inputStream, inputStream.available(), -1).build());
            // 获取url
            return String.format("%s/%s/%s", minioConfig.getEndpoint(), minioConfig.getBucketName(), objectName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("文件上传失败"+e.getMessage());
        }
    }

    public String uploadFile(InputStream inputStream, String objectName, String contentType){
        try {
            String object=minioConfig.getDir()+"/"+ DateUtils.getDate()+"/"+objectName;

            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(object).contentType(contentType).stream(inputStream, inputStream.available(), -1).build());
            // 获取url
            return String.format("%s/%s/%s",minioConfig.getEndpoint(),minioConfig.getBucketName(),object);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("文件上传失败"+e.getMessage());
        }
    }

    public String getUrl(String str){
        int fourthSlashIndex = str.indexOf("/", str.indexOf("/", str.indexOf("/", str.indexOf("/") + 1) + 1) + 1);
        if (fourthSlashIndex != -1) {
            String result = str.substring(fourthSlashIndex + 1);
           return result;
        } else {
            return "";
        }
    }

    public String getBucketName(String str){
        int thirdSlashIndex = str.indexOf("/", str.indexOf("/") + 1 + str.indexOf("/", str.indexOf("/") + 1));
        int fourthSlashIndex = str.indexOf("/", thirdSlashIndex + 1);
        String result = str.substring(thirdSlashIndex + 1, fourthSlashIndex);
        return result;
    }
    /**
     * 复制一个新文件删除之前的
     * @param url
     * @param newUrl
     * @return
     */
    public Boolean copyFile(String url, String newUrl){
        if (StrUtil.equals(url, newUrl)) {
            return true;
        }
        try {
            url = CommonUtils.removeQuery(url)  ;
            newUrl = CommonUtils.removeQuery(newUrl);
            String objectName = url.replace(minioConfig.getEndpoint() + "/" + minioConfig.getBucketName(), "");
            String objectName2 = newUrl.replace(minioConfig.getEndpoint() + "/" + minioConfig.getBucketName(), "");
            CopyObjectArgs copyObjectArgs = CopyObjectArgs.builder()
                    .source(CopySource.builder()
                            .bucket(getBucketName(url))
                            .object(getUrl(url))
                            .build())
                    .bucket(getBucketName(newUrl))
                    .object(getUrl(newUrl))
                    .build();
            minioClient.copyObject(copyObjectArgs);
            return  delFile(url);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得文件外链
     *
     * @param bucketName
     * @param objectName
     * @return url
     */
    @SneakyThrows(Exception.class)
    public String getObjectUrl(String bucketName, String objectName) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(args);
    }

    /**
     * 获取资源的访问路径
     *
     * @param url
     * @return
     */
    public String getObjectUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return url;
        }
        if (!url.startsWith(minioConfig.getEndpoint())) {
            return url;
        }
        try {
            //将空格特殊字符进行ASCll码转义
            url = url.replace(" ", "%20");
            List<String> split = StrUtil.split(url, CharUtil.COMMA);
            return split.stream().map(url1 -> {
                String tempUrl = URLUtil.getPath(url1).substring(1);
                int firstIndex = tempUrl.indexOf("/");
                String bucketName = tempUrl.substring(0, firstIndex);
                String objectName = tempUrl.substring(firstIndex + 1);
                GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(1, TimeUnit.DAYS).method(Method.GET).build();
                String presignedObjectUrl = null;
                try {
                    presignedObjectUrl = minioClient.getPresignedObjectUrl(args);
                } catch (Exception e) {
                    return tempUrl;
                }
                return presignedObjectUrl;
            }).collect(Collectors.joining(StrUtil.COMMA));
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
    }

    /**
     * 给 list 设置访问地址
     *
     * @param list
     * @param property 属性
     */
    public void setObjectUrlToList(List list, String... property) {
        if (CollUtil.isEmpty(list) || ArrayUtil.isEmpty(property)) {
            return;
        }
        for (Object o : list) {
            for (String s : property) {
                setObjectUrlToObject(o, s);
            }
        }
    }

    /**
     * 给obj 设置 访问地址
     *
     * @param o        bean or map
     * @param property 属性
     */
    public void setObjectUrlToObject(Object o, String... property) {
        if (ObjectUtil.isEmpty(o) || ArrayUtil.isEmpty(property)) {
            return;
        }
        for (String s : property) {
            Object val = BeanUtil.getProperty(o, s);
            if (ObjectUtil.isEmpty(val)) {
                continue;
            }
            BeanUtil.setProperty(o, s, getObjectUrl(String.valueOf(val)));
        }
    }

    /**
     * file转MultipartFile
     *
     * @param file file
     * @return MultipartFile
     */
    public MultipartFile convertFileToMultipartFile(File file) throws IOException {
        String fileName = file.getName();
        String contentType = "multipart/form-data"; // or find contentType using some utility
        byte[] content = FileCopyUtils.copyToByteArray(file);
        return new MockMultipartFile(fileName, fileName, contentType, content);
    }

    public boolean delFile(String url) {
        try {
            String tempUrl = URLUtil.getPath(url).substring(1);
            // 去掉参数
            int firstIndex = tempUrl.indexOf("/");
            String objectName = tempUrl.substring(firstIndex + 1);

            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param objectName
     * @return
     */
    public boolean hasObject(String objectName) {
        boolean exist = true;
        try {
            minioClient
                    .statObject(StatObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }
}
