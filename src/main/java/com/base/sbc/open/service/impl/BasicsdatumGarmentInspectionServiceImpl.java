/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.open.dto.BasicsdatumGarmentInspectionDetailDto;
import com.base.sbc.open.dto.BasicsdatumGarmentInspectionDto;
import com.base.sbc.open.entity.BasicsdatumGarmentInspection;
import com.base.sbc.open.entity.BasicsdatumGarmentInspectionDetail;
import com.base.sbc.open.mapper.BasicsdatumGarmentInspectionMapper;
import com.base.sbc.open.service.BasicsdatumGarmentInspectionDetailService;
import com.base.sbc.open.service.BasicsdatumGarmentInspectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：成衣成分送检 service类
 * @address com.base.sbc.open.service.BasicsdatumGarmentInspectionService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-17 15:18:54
 * @version 1.0  
 */
@Service
public class BasicsdatumGarmentInspectionServiceImpl extends BaseServiceImpl<BasicsdatumGarmentInspectionMapper, BasicsdatumGarmentInspection> implements BasicsdatumGarmentInspectionService {

    @Resource
    private BasicsdatumGarmentInspectionDetailService basicsdatumGarmentInspectionDetailService;
    @Resource
    private PushRecordsService pushRecordsService;
    @Resource
    private AttachmentService attachmentService;

    @Transactional
    @Override
    public void saveGarmentInspection(BasicsdatumGarmentInspectionDto garmentInspectionDto) {
        String year = garmentInspectionDto.getYear();
        String styleNo = garmentInspectionDto.getStyleNo();
        List<BasicsdatumGarmentInspectionDetailDto> detailDtoList = garmentInspectionDto.getGarmentInspectionDetailDtoList();
        if (StrUtil.isBlank(styleNo) ||  StrUtil.isBlank(year)) {
            throw new OtherException("大货款号或年份数据不能为空！");
        }
        if (CollUtil.isEmpty(detailDtoList)) {
            throw new OtherException("明细数据不能为空！");
        }

        String billId = null;
        QueryWrapper inspectionQueryWrapper = new QueryWrapper<BasicsdatumGarmentInspection>()
                .eq("style_no", styleNo)
                .eq("year", year);

        BasicsdatumGarmentInspection basicsdatumGarmentInspection = BeanUtil.copyProperties(garmentInspectionDto, BasicsdatumGarmentInspection.class);

        BasicsdatumGarmentInspection result = this.getOne(inspectionQueryWrapper);

        if (result != null) {
            this.updateById(result);
            billId = result.getId();
        }else{
            this.save(basicsdatumGarmentInspection);
            billId = basicsdatumGarmentInspection.getId();
        }

        QueryWrapper<BasicsdatumGarmentInspectionDetail> detailQueryWrapper = new QueryWrapper<BasicsdatumGarmentInspectionDetail>()
                .eq("bill_id", billId);

        List<BasicsdatumGarmentInspectionDetail> detailList = basicsdatumGarmentInspectionDetailService.list(detailQueryWrapper);

        if(CollUtil.isNotEmpty(detailList)){
            for (BasicsdatumGarmentInspectionDetail inspectionDetail : detailList) {
                //移除成衣成分送检明细数据
                basicsdatumGarmentInspectionDetailService.removeById(inspectionDetail.getId());
                //移除成衣成分送检图片数据
                List<Attachment> attachments = attachmentService.list(new QueryWrapper<Attachment>().eq("foreign_id", inspectionDetail.getId()));
                for (Attachment attachment : attachments) {
                    attachmentService.removeById(attachment.getId());
                }
            }
        }


        //重新新增明细和图片数据
        for (BasicsdatumGarmentInspectionDetailDto inspectionDetailDto : detailDtoList) {

            BasicsdatumGarmentInspectionDetail inspectionDetail = BeanUtil.copyProperties(inspectionDetailDto, BasicsdatumGarmentInspectionDetail.class);
            inspectionDetail.setBillId(billId);
            inspectionDetail.setYear(garmentInspectionDto.getYear());
            inspectionDetail.setStyleNo(garmentInspectionDto.getStyleNo());
            basicsdatumGarmentInspectionDetailService.save(inspectionDetail);

            List<String> attachmentUrlList = inspectionDetailDto.getAttachmentUrlList();

            if (CollUtil.isNotEmpty(attachmentUrlList)) {
                for (String url : attachmentUrlList) {
                    Attachment attachment = new Attachment();
                    attachment.setType("GARMENT_INSPECTION");
                    attachment.setForeignId(inspectionDetail.getId());
                    attachment.setStatus("0");
                    attachment.setRemarks(url);
                    attachmentService.save(attachment);
                }
            }
        }

        //保存日志
//        HttpResp httpResp = new HttpResp();
//        private String statusCode;
//        private String code;
//        private boolean success;
//        private String message;
//        private String url;
//        pushRecordsService.pushRecordSave(httpResp, JSONObject.toJSONString(garmentInspectionDto), "SCM", "成衣成分送检接收");

    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
