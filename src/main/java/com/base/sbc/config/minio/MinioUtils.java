package com.base.sbc.config.minio;

import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.DateUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
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
            String object=minioConfig.getDir()+"/"+ DateUtils.getDate()+"/"+objectName;
            InputStream inputStream = file.getInputStream();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(object).contentType(contentType).stream(inputStream, inputStream.available(), -1).build());
            // 获取url
            return String.format("%s/%s/%s",minioConfig.getEndpoint(),minioConfig.getBucketName(),object);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("文件上传失败"+e.getMessage());
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
}
