package com.base.sbc.config.ureport.minio;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：minio 配置
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.config.minio.MinioConfig
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-12 13:28
 */
@Data
@Configuration
public class MinioConfig {

    /**
     * 访问地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * secretKey是你账户的密码
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 默认存储桶
     */
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${minio.dir:pdm}")
    private String dir;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return minioClient;
    }
}
