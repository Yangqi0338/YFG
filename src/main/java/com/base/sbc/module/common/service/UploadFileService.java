/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.service;

import com.base.sbc.config.enums.business.UploadFileType;
import com.base.sbc.module.common.dto.DelStylePicDto;
import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingListVo;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：上传文件 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.common.service.UploadFileService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 15:16:14
 */
public interface UploadFileService extends BaseService<UploadFile> {

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 上传到minio
     *
     * @param file
     * @return
     */
    AttachmentVo uploadToMinio(MultipartFile file, UploadFileType type, String code);

    /**
     * 上传到minio
     *
     * @param file
     * @return
     */
    AttachmentVo uploadToMinio(MultipartFile file);

    /**
     * 上传到minio
     *
     * @param file
     * @param objectName 路径
     * @return
     */
    AttachmentVo uploadToMinio(MultipartFile file, String objectName);

    /**
     * 上传到minio
     *
     * @param bufferedImage
     * @return
     */
    AttachmentVo uploadToMinio(BufferedImage bufferedImage, String fileName);

    /**
     * 修改图片名称
     * @param url
     * @param newUrl
     * @return
     */
    Boolean updatePicName(String url, String newUrl);


    /**
     * 通过md5查找文件
     *
     * @param md5
     * @return
     */
    UploadFile findByMd5(String md5);

    /**
     * 获取文件id
     *
     * @param fileUrls
     * @return key = url,val=id
     */
    Map<String, String> findMapByUrls(List<String> fileUrls);

    String getIdByUrl(String url);

    String getUrlById(String id);

    boolean delByUrl(String url);

    /**
     * 上传款式图
     *
     * @return
     */
    Boolean uploadStyleImage(UploadStylePicDto uploadStylePicDto, Principal user) throws Exception;

    /**
     * 上传款式设计图
     *
     * @param uploadStylePicDto
     * @param user
     * @return
     * @throws Exception
     */
    Object uploadDesignImage(UploadStylePicDto uploadStylePicDto, Principal user) throws Exception;

    boolean delStyleImage(DelStylePicDto dto, Principal user);

    /**
     * 删除款式配色图片接口
     * @param dto 参数
     * @param user 用户信息
     * @param delFileName 删除图片名称
     * @return
     */
    boolean delStyleColorImage(DelStylePicDto dto, Principal user,String delFileName,String picType);



    boolean delDesignImage(DelStylePicDto dto, Principal user);


    /**
     * 从服务器上下载图片
     * @param key
     * @param fileName
     * @return
     */
    MultipartFile downloadImage(String key,String fileName) throws IOException;

    /**
     *  通过fileId 给 list 设置访问地址
     *
     * @param list
     * @param property 属性
     */
    void setObjectUrlToList(List list, String... property);

    void setObjectUrlToObject(Object o, String... property);



    /**
     * 通过id获取预览地址
     *
     * @param id
     * @return
     */
    String getReviewUrlById(String id);

/** 自定义方法区 不替换的区域【other_end】 **/


}
