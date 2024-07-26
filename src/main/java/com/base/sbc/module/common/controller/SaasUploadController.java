package com.base.sbc.module.common.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.CustomStylePicUpload;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.FilesUtils;
import com.base.sbc.config.utils.FtpUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.DelStylePicDto;
import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.utils.ImageUtils;
import com.base.sbc.module.common.utils.VideoUtil;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.operalog.entity.OperaLogEntity;

import cn.hutool.http.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;

@RestController
@Api(tags = "2.1 文件上传 已登接口[上传]")
@RequestMapping(value = BaseController.SAAS_URL + "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SaasUploadController extends BaseController {
    @Autowired
    private FilesUtils filesUtils;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private FtpUtil util;
    @Autowired
    private CustomStylePicUpload customStylePicUpload;

    @Autowired
    private ThreadPoolTaskExecutor taskUploadExecutor;

    @Resource
    private MinioUtils minioUtils;

    /**
     * 视频上传最大值：50 单位M
     */
    @Value("${upload.video.maxSize:500}")
    private Long videoSMaxSize;


    @ApiOperation(value = "产品图片上传", notes = "用于产品图片上传，返回上传成功的地址")
    @RequestMapping(value = "/productPic", method = RequestMethod.POST)
    public ApiResult uploadPicFile(@RequestParam(value = "file", required = true) MultipartFile file, String type, String code) {
//        return filesUtils.uploadBigData(file, FilesUtils.PRODUCT, request);
        CommonUtils.isImage(file.getOriginalFilename(), true);
        AttachmentVo attachmentVo = uploadFileService.uploadToMinio(file, type, code);
        return ApiResult.success(FilesUtils.SUCCESS, attachmentVo.getUrl(), BeanUtil.beanToMap(attachmentVo));
    }

    @ApiOperation(value = "大货款图,设计款图上传", notes = "用于大货图片、设计款图上传")
    @PostMapping(value = "/uploadStylePic")
    public Object uploadStylePic(Principal user, UploadStylePicDto uploadStylePicDto) {
        CommonUtils.isImage(uploadStylePicDto.getFile().getOriginalFilename(), true);
        if (!customStylePicUpload.isOpen()) {
            ApiResult apiResult = uploadPicFile(uploadStylePicDto.getFile(), null, null);
            return apiResult.getAttributes();
        }
        try {
            //上传大货款图
            if (StrUtil.isNotBlank(uploadStylePicDto.getStyleColorId())) {
                return uploadFileService.uploadStyleImage(uploadStylePicDto, user);
            }
            //上传设计款图
            if (StrUtil.isNotBlank(uploadStylePicDto.getStyleId())) {
                return uploadFileService.uploadDesignImage(uploadStylePicDto, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new OtherException("上传失败:" + e.getMessage());
        }
        throw new OtherException("缺少参数");
    }

    @ApiOperation(value = "删除设计款图或者大货款图")
    @GetMapping("/delStylePic")
    public boolean delStylePic(Principal user, DelStylePicDto dto) {
        if (!customStylePicUpload.isOpen()) {
            return true;
        }
        //删除大货款图
        if (StrUtil.isNotBlank(dto.getStyleColorId())) {
            return uploadFileService.delStyleImage(dto, user);
        }
        //删除设计款图
        if (StrUtil.isNotBlank(dto.getStyleId())) {
            return uploadFileService.delDesignImage(dto, user);
        }

        throw new OtherException("缺少参数");
    }

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public AttachmentVo uploadFile(@RequestPart(value = "file", required = false) MultipartFile file,  String type, String code, String isCompact) throws Throwable {
        if (file == null || file.getSize() == 0) {
            throw new OtherException("上传文件为空");
        }
        logger.info("开始上传视频-----------！");
        checkUploadVideoSize(file);
        String contentType = file.getContentType();
        AttachmentVo attachmentVo = uploadFileService.uploadToMinio(file, type, code);
        //异步压缩视频
        if ("1".equals(isCompact) && contentType.endsWith("mp4")){
            taskUploadExecutor.submit(() ->uploadFileService.asyncCompress(attachmentVo.getFileId(),contentType,type,code));
        }
        return attachmentVo;
    }

    /**
     * 转换视频
     * @param sourceFile
     * @param isCompact 是否转换
     * @return
     */
    private MultipartFile getVideoConvert(MultipartFile sourceFile, String isCompact){
        if (videoSMaxSize != null && videoSMaxSize < sourceFile.getSize()/1048576){
            throw new OtherException("允许视频上传的最大值为："+videoSMaxSize+"M");
        }
        String contentType = sourceFile.getContentType();
        if (!"1".equals(isCompact) || !sourceFile.getContentType().endsWith("mp4")){
            return sourceFile;
        }
        File targetFile=null;
        File newFile = null;

        try{
            targetFile= VideoUtil.toFile(sourceFile);
            newFile = VideoUtil.compressionVideo(targetFile, UUID.randomUUID().toString().replace("-", "") + ".mp4");
//            minioUtils.convertFileToMultipartFile(newFile);
            return VideoUtil.toMultipartFile(newFile,contentType);
        }catch (Exception e){
            e.printStackTrace();
            throw new OtherException(e.getMessage());
        }finally{
            if (newFile != null){
                newFile.delete();
            }
        }
    }


    /**
     * 检查 上传视频大小
     * @param sourceFile
     */
    private void checkUploadVideoSize(MultipartFile sourceFile){
        if (!sourceFile.getContentType().endsWith("mp4")){
            return;
        }
        if (videoSMaxSize != null && videoSMaxSize < sourceFile.getSize()/1048576){
            throw new OtherException("允许视频上传的最大值为："+videoSMaxSize+"M");
        }
    }


//    public void asyncCompress(String fileId, String contentType, String type, String code) {
//        UploadFile uploadFile = uploadFileService.getById(fileId);
//        if (Objects.isNull(uploadFile)){
//            return;
//        }
//        String url = uploadFile.getUrl();
//        String objectUrl = minioUtils.getObjectUrl(url);
//        File targetFile = null;
//        File newFile = null;
//        try {
//            targetFile = VideoUtil.toUrlFile(objectUrl + "&opacity=0",url);
//            newFile = VideoUtil.compressionVideo(targetFile, UUID.randomUUID().toString().replace("-", "") + ".mp4");
//            MultipartFile multipartFile = VideoUtil.toMultipartFile(newFile, contentType);
//            AttachmentVo attachmentVo = uploadFileService.uploadToMinio(multipartFile, type, code);
//            String newUrl = CommonUtils.removeQuery(attachmentVo.getUrl());
//            UpdateWrapper<UploadFile> uw = new UpdateWrapper<>();
//            uw.lambda().eq(UploadFile::getId,fileId);
//            uw.lambda().set(UploadFile::getUrl,newUrl);
//            boolean update = uploadFileService.update(uw);
//            if (update){
//                this.delUrl(url);
//            }
//            uploadFileService.removeById(attachmentVo.getFileId());
//        }catch (Exception e){
//            logger.error("asyncCompress error={}, fileId={} type={}, code={}",e.getMessage(),fileId, type,code);
//            e.printStackTrace();
//            throw new OtherException("压缩转换失败");
//        }finally {
//            if (targetFile !=null){
//                targetFile.delete();
//            }
//            if (newFile !=null){
//                newFile.delete();
//            }
//        }
//
//    }
//    public void delUrl(String url) {
//        if (TransactionSynchronizationManager.isActualTransactionActive()) {
//            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//                @Override
//                public void afterCommit() {
//                    minioUtils.delFile(url);
//                }
//            });
//        }
//    }


}
