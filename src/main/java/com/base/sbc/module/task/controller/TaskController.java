package com.base.sbc.module.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.flowable.service.FlowableFeignService;
import com.base.sbc.client.flowable.vo.FlowQueryVo;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.service.MaterialService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.task.vo.FlowTaskDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * @author 卞康
 * @date 2023/11/13 14:15:17
 * @mail 247967116@qq.com
 */
@Api(tags = "任务")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/task")
@RequiredArgsConstructor
public class TaskController {
    private final FlowableFeignService flowableFeignService;
    private final AttachmentService attachmentService;
    // private final UploadFileService uploadFileService;
    // private final MinioUtils minioUtils;
    private final StyleService styleService;

    private final StylePicUtils stylePicUtils;

    private final MaterialService materialService;

    private final MinioUtils minioUtils;

    @ApiOperation(value = "获取待办列表", response = FlowTaskDto.class)
    @GetMapping(value = "/todoList")
    public ApiResult todoList(FlowQueryVo queryVo){
         //queryVo转map
        Map<String,Object> map=new HashMap<>();
        map.put("category",queryVo.getCategory());
        map.put("taskName",queryVo.getTaskName());
        map.put("startUserId",queryVo.getStartUserId());
        map.put("createTime",queryVo.getCreateTime());
        map.put("contentApproval",queryVo.getContentApproval());
        map.put("isAdmin",queryVo.getIsAdmin());
        map.put("pageNum",queryVo.getPageNum());
        map.put("pageSize",queryVo.getPageSize());
        map.put("startTime",queryVo.getStartTime());
        map.put("endTime",queryVo.getEndTime());
        map.put("procDefName",queryVo.getProcDefName());
        map.put("search",queryVo.getSearch());
        map.put("businessKeyList",queryVo.getBusinessKeyList());
        ApiResult apiResult = flowableFeignService.todoList(map);
        Map<String,Object> data1 = (Map<String, Object>) apiResult.getData();
        String jsonString = JSON.toJSONString(data1);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray jsonArray= jsonObject.getJSONArray("list");
        List<FlowTaskDto> data = jsonArray.toJavaList(FlowTaskDto.class);

        if (data!=null&& !data.isEmpty()) {
            for (FlowTaskDto flowTaskDto : data) {
                String contentApproval = flowTaskDto.getContentApproval();
                String procDefName = flowTaskDto.getProcDefName();
                /*判断是否是款式设计的审批*/
                if (StrUtil.isNotBlank(contentApproval) && StrUtil.equals(procDefName, "款式设计审批")) {
                    /*获取[]中的元素*/
                    Pattern pattern = Pattern.compile("\\[(.*?)\\]");
                    Matcher matcher = pattern.matcher(contentApproval);
                    if (matcher.find()) {
                        /*设计款号*/
                        String designNo = matcher.group(1);
                        if (StrUtil.isNotBlank(designNo)) {
                            Style style = styleService.getOne(new BaseQueryWrapper<Style>().eq("design_no", designNo));
                            if (ObjectUtil.isEmpty(style)) {
                                continue;
                            }
                            List<AttachmentVo> attachmentVoList1 = attachmentService.findByforeignId(style.getId(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_APPROVE_PIC);
                            if (attachmentVoList1 != null && !attachmentVoList1.isEmpty()) {
                                flowTaskDto.setPic(attachmentVoList1.get(0).getUrl());
                            }
                            flowTaskDto.setStylePic(stylePicUtils.getStyleUrl(style.getStylePic()));
                        }
                    }

                }
                if (StrUtil.isNotBlank(contentApproval) &&  procDefName.contains("素材审批")){
                    Material material = materialService.getById(flowTaskDto.getBusinessKey());
                    if (!Objects.isNull(material)){
                        minioUtils.setObjectUrlToObject(material, "picUrl");
                        flowTaskDto.setPic(material.getPicUrl());
                        flowTaskDto.setName(StringUtils.isNotBlank(material.getMaterialName()) ? material.getMaterialName() : material.getFileInfo());
                    }
                }

                // List<String> ids = data.stream().map(FlowTaskDto::getPic).filter(res -> !StringUtils.isEmpty(res) ).collect(Collectors.toList());
                // if (!ids.isEmpty()){
                //     List<Attachment> attachments = attachmentService.listByIds(ids);
                //     List<String> fileIds = attachments.stream().map(Attachment::getFileId).collect(Collectors.toList());
                //     Map<String,String > collect1 = attachments.stream().collect(Collectors.toMap(Attachment::getId, Attachment::getFileId));
                //     List<UploadFile> uploadFiles = uploadFileService.listByIds(fileIds);
                //     Map<String, String> collect = uploadFiles.stream().collect(Collectors.toMap(UploadFile::getId, UploadFile::getUrl));
                //     for (FlowTaskDto flowTaskDto : data) {
                //         String s = collect1.get(flowTaskDto.getPic());
                //         if (!StringUtils.isEmpty(s)){
                //             flowTaskDto.setPic(minioUtils.getObjectUrl(collect.get(s)));
                //         }
                //     }
            }

        }
        data1.put("list",data);
        apiResult.setData(data1);
        return apiResult;
    }

    @ApiOperation(value = "获取已办列表", response = FlowTaskDto.class)
    @GetMapping(value = "/finishedList")
    public ApiResult finishedList(FlowQueryVo queryVo){
         //queryVo转map
        Map<String,Object> map=new HashMap<>();
        map.put("category",queryVo.getCategory());
        map.put("taskName",queryVo.getTaskName());
        map.put("startUserId",queryVo.getStartUserId());
        map.put("createTime",queryVo.getCreateTime());
        map.put("contentApproval",queryVo.getContentApproval());
        map.put("isAdmin",queryVo.getIsAdmin());
        map.put("pageNum",queryVo.getPageNum());
        map.put("pageSize",queryVo.getPageSize());
        map.put("startTime",queryVo.getStartTime());
        map.put("endTime",queryVo.getEndTime());
        map.put("procDefName",queryVo.getProcDefName());
        map.put("search",queryVo.getSearch());
        ApiResult apiResult = flowableFeignService.finishedList(map);
        Map<String,Object> data1 = (Map<String, Object>) apiResult.getData();
        String jsonString = JSON.toJSONString(data1);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray jsonArray= jsonObject.getJSONArray("list");
        List<FlowTaskDto> data = jsonArray.toJavaList(FlowTaskDto.class);

        if (data!=null&& !data.isEmpty()) {
            for (FlowTaskDto flowTaskDto : data) {
                String contentApproval = flowTaskDto.getContentApproval();
                String procDefName = flowTaskDto.getProcDefName();
                /*判断是否是款式设计的审批*/
                if (StrUtil.isNotBlank(contentApproval) && StrUtil.equals(procDefName, "款式设计审批")) {
                    /*获取[]中的元素*/
                    Pattern pattern = Pattern.compile("\\[(.*?)\\]");
                    Matcher matcher = pattern.matcher(contentApproval);
                    if (matcher.find()) {
                        /*设计款号*/
                        String designNo = matcher.group(1);
                        if (StrUtil.isNotBlank(designNo)) {
                            Style style = styleService.getOne(new BaseQueryWrapper<Style>().eq("design_no", designNo));
                            if (ObjectUtil.isEmpty(style)) {
                                continue;
                            }
                            List<AttachmentVo> attachmentVoList1 = attachmentService.findByforeignId(style.getId(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_APPROVE_PIC);
                            if (attachmentVoList1 != null && !attachmentVoList1.isEmpty()) {
                                flowTaskDto.setPic(attachmentVoList1.get(0).getUrl());
                            }
                            flowTaskDto.setStylePic(stylePicUtils.getStyleUrl(style.getStylePic()));
                        }
                    }

                }
                if (StrUtil.isNotBlank(contentApproval) &&  procDefName.contains("素材审批")){
                    Material material = materialService.getById(flowTaskDto.getBusinessKey());
                    if (!Objects.isNull(material)){
                        minioUtils.setObjectUrlToObject(material, "picUrl");
                        flowTaskDto.setPic(material.getPicUrl());
                        flowTaskDto.setName(StringUtils.isNotBlank(material.getMaterialName()) ? material.getMaterialName() : material.getFileInfo());
                    }
                }

                // List<String> ids = data.stream().map(FlowTaskDto::getPic).filter(res -> !StringUtils.isEmpty(res) ).collect(Collectors.toList());
                // if (!ids.isEmpty()){
                //     List<Attachment> attachments = attachmentService.listByIds(ids);
                //     List<String> fileIds = attachments.stream().map(Attachment::getFileId).collect(Collectors.toList());
                //     Map<String,String > collect1 = attachments.stream().collect(Collectors.toMap(Attachment::getId, Attachment::getFileId));
                //     List<UploadFile> uploadFiles = uploadFileService.listByIds(fileIds);
                //     Map<String, String> collect = uploadFiles.stream().collect(Collectors.toMap(UploadFile::getId, UploadFile::getUrl));
                //     for (FlowTaskDto flowTaskDto : data) {
                //         String s = collect1.get(flowTaskDto.getPic());
                //         if (!StringUtils.isEmpty(s)){
                //             flowTaskDto.setPic(minioUtils.getObjectUrl(collect.get(s)));
                //         }
                //     }
            }

        }
        data1.put("list",data);
        apiResult.setData(data1);
        return apiResult;
    }
}
