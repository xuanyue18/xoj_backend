package com.xuanyue.xoj.service.impl;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;

import com.xuanyue.xoj.service.FileService;
import com.xuanyue.xoj.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author xuanyue_18
 * 阿里云对象存储实现类
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * 上传头像到OSS
     */
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        //工具类获取值
        String endpoint = FileUtils.END_POINT;
        String accessKeyId = FileUtils.ACCESS_KEY;
        String accessKeySecret = FileUtils.SECRET_KEY;
        String bucketName = FileUtils.BUCKET_NAME;
        InputStream inputStream = null;
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            String datePath = new DateTime().toString("yyyy-MM-dd");
            inputStream = file.getInputStream();

            // 获取文件扩展名
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            // 生成随机文件名
            String randomFileName = UUID.randomUUID() + extension;

            String fileName = datePath + "/" + randomFileName;

            boolean exists = ossClient.doesObjectExist(bucketName, fileName);
            if (exists) {
                ossClient.deleteObject(bucketName, fileName);
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setContentType(getcontentType(extension));
            objectMetadata.setContentDisposition("attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            ossClient.putObject(bucketName, fileName, inputStream);

            ossClient.shutdown();

            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "application/octet-stream";
    }
}