package com.xuanyue.xoj.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author xuanyue_18
 */
public interface FileService {
    /**
     * 上传头像到OSS
     *
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);
}
