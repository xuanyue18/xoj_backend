package com.xuanyue.xoj.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量类，读取配置文件application.yml中的配置
 * @author xuanyue_18
 */
@Component
public class FileUtils implements InitializingBean {

    public static String END_POINT;
    public static String ACCESS_KEY;
    public static String SECRET_KEY;
    public static String BUCKET_NAME;

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.file.access_key}")
    private String access_key;
    @Value("${aliyun.oss.file.secret_key}")
    private String secret_key;
    @Value("${aliyun.oss.file.bucket_name}")
    private String bucket_name;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = this.endpoint;
        ACCESS_KEY = this.access_key;
        SECRET_KEY = this.secret_key;
        BUCKET_NAME = this.bucket_name;
    }
}