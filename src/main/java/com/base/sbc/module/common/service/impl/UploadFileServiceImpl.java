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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioConfig;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.config.utils.ImgUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumMaterialMapper;
import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.mapper.UploadFileMapper;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.mapper.PreProductionSampleTaskMapper;
import com.base.sbc.module.sample.vo.StyleUploadVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleColorMapper;
import com.base.sbc.module.style.mapper.StyleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.base.sbc.config.utils.EncryptUtil.EncryptE2;

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
    public static final String UPLOAD_PHOTO = "http://img.eifini.com/image/UploadPhoto";

    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private StyleColorMapper styleColorMapper;

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private StyleMapper styleMapper;

    @Autowired
    private BasicsdatumMaterialMapper basicsdatumMaterialMapper;

    @Autowired
    private PatternMakingMapper patternMakingMapper;

    @Autowired
    private PreProductionSampleTaskMapper preProductionSampleTaskMapper;

    @Autowired
    private PlanningCategoryItemMapper planningCategoryItemMapper;

    @Override
    public AttachmentVo uploadToMinio(MultipartFile file) {
        return uploadToMinio(file, null);
    }

    @Override
    public AttachmentVo uploadToMinio(MultipartFile file,String type,String code) {
        try {
            String md5Hex = DigestUtils.md5DigestAsHex(file.getInputStream());
            String objectName = "";
            String extName = FileUtil.extName(file.getOriginalFilename());
            if (StrUtil.isBlank(extName)) {
                throw new OtherException("文件无后缀名");
            }
            if (StringUtils.isNotBlank(type)) {
                switch (type) {
                    /*创意素材库图/附件*/
                    case "sourceMaterial":
                        objectName = type + "/" + DateUtils.getDate() + "/" + System.currentTimeMillis() + "." + extName;
                        break;
                    /*商品企划图*/
                    case "planning":
                        PlanningCategoryItem planningCategoryItem = planningCategoryItemMapper.selectById(code);
                        objectName = type + "/" + planningCategoryItem.getBrandName() + "/" + planningCategoryItem.getYearName() + "/" + planningCategoryItem.getDesignNo() + "." + extName;
                        break;
                    /*款式设计（除设计款外其他图片及附件）*/
                    /*样衣/打版其他附件*/
                    case "styleOther":
                    case "sampleOther":
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.eq("design_no", code);
                        Style styel = styleMapper.selectOne(queryWrapper);
                        objectName = type + "/" + styel.getBrandName() + "/" + styel.getYearName() + "/" + styel.getDesignNo() + "/" + System.currentTimeMillis() + "." + extName;

                        break;
                    /*样衣图片（包含产前样）*/
                    case "sample":
                        PatternMaking patternMaking = patternMakingMapper.selectById(code);
                        Style style = styleMapper.selectById(patternMaking.getStyleId());
                        objectName = "sample/" + style.getBrandName() + "/" + style.getYearName() + "/" + style.getDesignNo() + "/" + patternMaking.getSampleBarCode() + "." + extName;
                        break;
                    /*样衣图片（包含产前样）*/
                    case "preSample":
                        PreProductionSampleTask preProductionSampleTask = preProductionSampleTaskMapper.selectById(code);
                        Style style1 = styleMapper.selectById(preProductionSampleTask.getStyleId());
                        objectName = "sample/" + style1.getBrandName() + "/" + style1.getYearName() + "/" + style1.getDesignNo() + "/" + preProductionSampleTask.getSampleBarCode() + "." + extName;
                        break;
                    /*设计BOM标准资料包（除工艺单外）*/
                    case "stylePackage":
                    case "dataPackageOther":
                        objectName = type + "/" + code + "/" + System.currentTimeMillis() + "." + extName;
                        break;
                    /*物料其他附件*/
                    case "materialOther":
                        objectName = type + "/" + System.currentTimeMillis() + "." + extName;
                        break;
                    /*系统配置附件/图*/
                    case "config":
                        objectName = "system/config" + "/" + System.currentTimeMillis() + "." + extName;
                        break;
                    /*物料主图*/
                    case "material":
                        /*查询物料的数据*/
                        BasicsdatumMaterial basicsdatumMaterial = basicsdatumMaterialMapper.selectById(code);
                        if (ObjectUtils.isEmpty(basicsdatumMaterial)) {
                            throw new OtherException("没有物料信息");
                        }
                        if (StringUtils.isEmpty(basicsdatumMaterial.getYearName())) {
                            throw new OtherException("没有年份信息，先保存");
                        }
                        objectName = type + "/" + basicsdatumMaterial.getYearName() + "/" + basicsdatumMaterial.getSeasonName() + "/" + basicsdatumMaterial.getMaterialCode() + "." + extName;
                        break;
                    default:
                        objectName = DateUtils.getDate() + "/" + System.currentTimeMillis() + "." + extName;
                }
            } else {
                objectName = DateUtils.getDate() + "/" + System.currentTimeMillis() + "." + extName;
            }

            String contentType = file.getContentType();
            String url = minioUtils.uploadFile(file, objectName, contentType);
            UploadFile newFile = new UploadFile();
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
            throw new OtherException("上传失败:" + e.getMessage());
        }
    }

    @Override
    public AttachmentVo uploadToMinio(MultipartFile file, String path) {
        try {
            String md5Hex = DigestUtils.md5DigestAsHex(file.getInputStream());
//            UploadFile byMd5 = findByMd5(md5Hex);
//            if(byMd5!=null){
//                byMd5.setName(file.getOriginalFilename());
//                log.info("文件已经存在:"+md5Hex);
//                return byMd5;
//            }
            String objectName = path;
            if (path == null) {
                String extName = FileUtil.extName(file.getOriginalFilename());
                if (StrUtil.isBlank(extName)) {
                    throw new OtherException("文件无后缀名");
                }
                objectName = minioConfig.getDir() + "/" + DateUtils.getDate() + "/" + System.currentTimeMillis() + "." + extName;
            }

            String contentType = file.getContentType();
            String url = minioUtils.uploadFile(file, objectName, contentType);
            UploadFile newFile = new UploadFile();
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
            throw new OtherException("上传失败:" + e.getMessage());
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

    /**
     * 修改图片名称
     *
     * @param url
     * @param newUrl
     * @return
     */
    @Override
    public Boolean updatePicName(String url, String newUrl) {
        return minioUtils.copyFile(url,newUrl);
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

    /**
     * 上传款式图
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean uploadStyleImage(UploadStylePicDto dto, Principal user) throws Exception {
        GroupUser userBy = userUtils.getUserBy(user);

        if(StringUtils.isBlank(dto.getStyleColorId())){
            throw new OtherException("配色id不能为空");
        }
        /*获取年季节品牌等信息*/
        StyleUploadVo styleUploadVo = styleColorMapper.getStyleUploadInfo(dto.getStyleColorId());
        /*获取文件类型*/
        String type = dto.getFile().getOriginalFilename().substring(dto.getFile().getOriginalFilename().lastIndexOf(".")+1);
        File file = null;
        try {
             file = File.createTempFile("temp", null);
        } catch (Exception e) {
            throw new OtherException("文件错误");
        }

        dto.getFile().transferTo(file);
        /*图片类型 （0 jpg,1 png 可以不传 默认0），非必填*/
        String pictype = "0";
        if (StringUtils.isNotBlank(type)) {
            if (type.equalsIgnoreCase("jpg")) {
                pictype = "0";
            } else if (type.equalsIgnoreCase("png")) {
                pictype = "1";
            }
        }

        String path =  file.toURI().getPath(); //图片路径
        Map<String, String> paraMap = new HashMap<String, String>();

        /*账号*/
        String useraccount = userBy.getUsername();
        /*申请开通权限APPKEY ，AES182 结果url编码*/
        String key = "PDMImage";
        String APPsecret = "925091ef18f40b662a55c058cb475137";
        String salt = "eifiniEMS202307";

        paraMap.put("brand", styleUploadVo.getBrand()); //品牌
        paraMap.put("year", styleUploadVo.getYear()); //年份
        paraMap.put("quarter", styleUploadVo.getSeason()); //季节
        /*图片名称*/
        paraMap.put("picname", styleUploadVo.getStyleNo()); //图片新的名称，以此名称为准
        paraMap.put("pictype", pictype);//图片文件类型 0 jpg ；1 png
        /*PDM_Style:款式图
        * PDM：大货款图*/
        paraMap.put("folderName", "PDM");//文件夹
        /*正式环境传 0 或者不穿
        * 测试环境传：1*/
        paraMap.put("debug", "1"); //传递plm图片
        paraMap.put("useraccount", useraccount); //传递plm图片
        paraMap.put("model", "0"); //传递plm图片
        String appkeyP = URLEncoder.encode(EncryptE2(key, salt), "utf-8");
        paraMap.put("key", appkeyP); //传递plm图片
        String md5 = useraccount + key + APPsecret;
        String allStrP = DigestUtils.md5DigestAsHex(md5.getBytes());
        paraMap.put("md5", allStrP); //传递plm图片
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("img", path);
        String res = "";
        try {
            res = ImgUtils.formUpload(UPLOAD_PHOTO, paraMap, fileMap);
        } catch (Exception e) {
            throw new OtherException("图片上传失败");
        }finally {
            file.delete();
        }
        JSONObject jsonObject =  JSON.parseObject(res);
        if (Boolean.parseBoolean (jsonObject.get("Sucess").toString())) {
            StyleColor styleColor = styleColorMapper.selectById(dto.getStyleColorId());
            styleColor.setStyleColorPic(jsonObject.get("FileName").toString());
            return  styleColorMapper.updateById(styleColor)>0;
        } else {
            throw new OtherException(jsonObject.get("Msg").toString());
        }
    }

    public String getTemporaryFilePath(MultipartFile multipartFile) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("temp", null);

        // 将 MultipartFile 的内容写入临时文件
        multipartFile.transferTo(tempFile);

        // 获取临时文件的路径
        String temporaryFilePath = tempFile.getAbsolutePath();
/*        String[] temporaryFilePaths =  temporaryFilePath.split("\\\\");
        temporaryFilePath =  temporaryFilePath.replaceAll(temporaryFilePaths[temporaryFilePaths.length-1],multipartFile.getOriginalFilename());*/
        // 删除临时文件
        tempFile.delete();
        return temporaryFilePath;
    }

/** 自定义方法区 不替换的区域【other_end】 **/

}
