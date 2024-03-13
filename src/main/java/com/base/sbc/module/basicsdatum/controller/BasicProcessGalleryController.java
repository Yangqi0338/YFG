package com.base.sbc.module.basicsdatum.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicProcessGalleryDto;
import com.base.sbc.module.basicsdatum.dto.BasicProcessGallerySaveDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicProcessGallery;
import com.base.sbc.module.basicsdatum.service.BasicProcessGalleryService;
import com.base.sbc.module.basicsdatum.vo.BasicProcessGalleryExcelVo;
import com.base.sbc.module.basicsdatum.vo.BasicProcessGalleryVo;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author 卞康
 * @date 2024-03-11 11:09:48
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/basicProcessGallery", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

public class BasicProcessGalleryController extends BaseController {
    private final BasicProcessGalleryService basicProcessGalleryService;
    private final UploadFileService uploadFileService;
    private final MinioUtils minioUtils;
    /**
     * 分页条件查询
     */
    @GetMapping(value = "/queryPage")
    public ApiResult<Object> queryPage(BasicProcessGalleryDto basicProcessGalleryDto) {
        return success("查询成功", basicProcessGalleryService.queryPage(basicProcessGalleryDto));
    }
    /**
     * 新增或者修改
     */
    @PostMapping(value = "/saveOrUpdate")
    @DuplicationCheck
    public ApiResult<Object> saveOrUpdate(@RequestBody BasicProcessGallerySaveDto basicProcessGallerySaveDto) {
        String image = basicProcessGallerySaveDto.getImage();
        if (StringUtils.isNotBlank(image)){
            String[] split = image.split("\\?");
            basicProcessGallerySaveDto.setImage(split[0]);
        }
        try {
            basicProcessGalleryService.saveOrUpdate(basicProcessGallerySaveDto,"基础工艺图库");
        }catch (DuplicateKeyException e){
            throw new RuntimeException("该编码或者名称已存在");
        }

        return success("保存成功");
    }

    /**
     * 启用停用
     */
    @PostMapping(value = "/startStop")
    public ApiResult<Object> startStop(@RequestBody StartStopDto startStopDto) {
        startStopDto.setName("基础工艺图库");
        basicProcessGalleryService.startStopLog(startStopDto);
        return success("保存成功");
    }

    /**
     * 删除
     */
    @PostMapping(value = "/remove")
    @DuplicationCheck
    public ApiResult<Object> remove(@RequestBody RemoveDto removeDto) {
        removeDto.setName("基础工艺图库");
        basicProcessGalleryService.removeByIds(removeDto);
        return success("删除成功");
    }


    /**
     * 导出
     */
    @GetMapping(value = "/export")
    @DuplicationCheck(type = 1)
    public void export(BasicProcessGalleryDto basicProcessGalleryDto, HttpServletResponse response) throws IOException {
        List<BasicProcessGalleryVo> list = basicProcessGalleryService.export(basicProcessGalleryDto);
        List<BasicProcessGalleryExcelVo> basicProcessGalleryExcelVos = BeanUtil.copyToList(list, BasicProcessGalleryExcelVo.class);

        ExcelUtils.exportExcel(basicProcessGalleryExcelVos,BasicProcessGalleryExcelVo.class, "基础工艺图库.xlsx", new ExportParams(null ,"基础工艺图库", ExcelType.HSSF), response);
    }

    /**
     * 根据文件id复制文件
     */
    @GetMapping(value = "/getNewFile")
    public ApiResult<Object> getNewFile(String fileId,String code,String type) throws IOException {
        UploadFile uploadFile = uploadFileService.getById(fileId);
        minioUtils.setObjectUrlToObject(uploadFile,"url");
        URL fileUrl = new URL(uploadFile.getUrl());
        HttpURLConnection conn = (HttpURLConnection)fileUrl.openConnection();

        InputStream inputStream = conn.getInputStream();
        // byte[] bytes = IOUtils.toByteArray(inputStream);
        // inputStream.close();
        // InputStream inputStream1 = new ByteArrayInputStream(bytes);
        MultipartFile multipartFile=new MockMultipartFile(uploadFile.getName(),uploadFile.getName(),uploadFile.getType(),inputStream);

        AttachmentVo attachmentVo = uploadFileService.uploadToMinio(multipartFile, type, code);

        return selectSuccess(attachmentVo);
    }
}
