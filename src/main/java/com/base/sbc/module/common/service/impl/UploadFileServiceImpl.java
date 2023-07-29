/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.mapper.UploadFileMapper;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：上传文件 service类
 * @address com.base.sbc.module.common.service.UploadFileService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 15:16:14
 * @version 1.0
 */
@Service
@Slf4j
public class UploadFileServiceImpl extends BaseServiceImpl<UploadFileMapper, UploadFile> implements UploadFileService {

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/


    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private AttachmentService attachmentService;

    @Override
    public AttachmentVo uploadToMinio(MultipartFile file) {
        try {
            String md5Hex = DigestUtils.md5DigestAsHex(file.getInputStream());
//            UploadFile byMd5 = findByMd5(md5Hex);
//            if(byMd5!=null){
//                byMd5.setName(file.getOriginalFilename());
//                log.info("文件已经存在:"+md5Hex);
//                return byMd5;
//            }
            String objectName = System.currentTimeMillis() + "." + FileUtil.extName(file.getOriginalFilename());
            String contentType = file.getContentType();
            String url = minioUtils.uploadFile(file, objectName, contentType);
            UploadFile newFile =new UploadFile();
            newFile.setMd5(md5Hex);
            newFile.setUrl(url);
            newFile.setName(file.getOriginalFilename());
            newFile.setType(contentType);
            newFile.setStorage("minio");
            newFile.setStatus(BaseGlobal.STATUS_NORMAL);
            newFile.setSize(new BigDecimal(String.valueOf(file.getSize())));
            save(newFile);
            AttachmentVo attachmentVo = BeanUtil.copyProperties(newFile, AttachmentVo.class, "id");
            attachmentVo.setFileId(newFile.getId());
            return attachmentVo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("上传失败");
        }
    }

    @Override
    public AttachmentVo uploadToMinio(BufferedImage bufferedImage, String fileName) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", baos);
            byte[] bytes = baos.toByteArray();

            MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, FileUtil.getMimeType(fileName), new ByteArrayInputStream(bytes));
            AttachmentVo attachmentVo = uploadToMinio(mockMultipartFile);
            return attachmentVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UploadFile findByMd5(String md5) {
        QueryWrapper<UploadFile> qw = new QueryWrapper<>();
        qw.eq("md5", md5);
        List<UploadFile> list = list(qw);
        if (CollUtil.isNotEmpty(list)) {
            return CollUtil.getFirst(list);
        }
        return null;
    }

    @Override
    public Map<String, String> findMapByUrls(List<String> fileUrls) {
        Map<String, String> result=new HashMap<>(16);
        if(CollUtil.isEmpty(fileUrls)){
            return result;
        }
        QueryWrapper<UploadFile> qw = new QueryWrapper<>();
        qw.in("url", fileUrls);
        List<UploadFile> list = list(qw);
        if (CollUtil.isEmpty(list)) {
            return result;
        }
        result = list.stream().collect(Collectors.toMap(k -> k.getUrl(), v -> v.getId(), (a, b) -> b));
        return result;
    }

    @Override
    public String getIdByUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return "";
        }
        QueryWrapper<UploadFile> qw = new QueryWrapper<>();
        qw.eq("url", url);
        qw.eq("del_flag", BaseGlobal.NO);
        qw.last("limit 1");
        UploadFile one = getOne(qw);
        return Optional.ofNullable(one).map(UploadFile::getId).orElse("");
    }

    @Override
    public String getUrlById(String id) {
        UploadFile byId = getById(id);
        return Optional.ofNullable(byId).map(UploadFile::getUrl).orElse("");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delByUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return true;
        }
        QueryWrapper fuQw = new QueryWrapper();
        fuQw.eq("url", url);
        fuQw.last("limit 1");
        UploadFile one = getOne(fuQw);
        if (one != null) {
            removeById(one.getId());
            QueryWrapper aqw = new QueryWrapper();
            aqw.eq("file_id", one.getId());
            List<Attachment> list = attachmentService.list(aqw);
            if (CollUtil.isNotEmpty(list)) {
                attachmentService.removeByIds(list.stream().map(Attachment::getId).collect(Collectors.toList()));
            }
        }
        minioUtils.delFile(url);

        return true;
    }


/** 自定义方法区 不替换的区域【other_end】 **/

}
