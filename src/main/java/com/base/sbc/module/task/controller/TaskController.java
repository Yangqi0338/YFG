package com.base.sbc.module.task.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.flowable.service.FlowableFeignService;
import com.base.sbc.client.flowable.vo.FlowQueryVo;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.task.vo.FlowTaskDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final UploadFileService uploadFileService;
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
        ApiResult apiResult = flowableFeignService.todoList(map);
        Map<String,Object> data1 = (Map<String, Object>) apiResult.getData();
        String jsonString = JSON.toJSONString(data1);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        JSONArray jsonArray= jsonObject.getJSONArray("list");
        List<FlowTaskDto> data = jsonArray.toJavaList(FlowTaskDto.class);

        if (data!=null&& !data.isEmpty()){
            for (FlowTaskDto flowTaskDto : data) {
                String contentApproval = flowTaskDto.getContentApproval();
                if (!StringUtils.isEmpty(contentApproval)){
                    String[] split = contentApproval.split("--");
                    if (split.length>1){
                        flowTaskDto.setContentApproval(split[0]);
                        flowTaskDto.setPic(split[1]);
                    }else {
                        flowTaskDto.setContentApproval(split[0].replace("--",""));
                    }
                }
            }



            List<String> ids = data.stream().map(FlowTaskDto::getPic).filter(res -> !StringUtils.isEmpty(res) ).collect(Collectors.toList());
            if (!ids.isEmpty()){
                List<Attachment> attachments = attachmentService.listByIds(ids);
                List<String> fileIds = attachments.stream().map(Attachment::getFileId).collect(Collectors.toList());
                Map<String,String > collect1 = attachments.stream().collect(Collectors.toMap(Attachment::getId, Attachment::getFileId));
                List<UploadFile> uploadFiles = uploadFileService.listByIds(fileIds);
                Map<String, String> collect = uploadFiles.stream().collect(Collectors.toMap(UploadFile::getId, UploadFile::getUrl));
                for (FlowTaskDto flowTaskDto : data) {
                    String s = collect1.get(flowTaskDto.getPic());
                    if (!StringUtils.isEmpty(s)){
                        flowTaskDto.setPic(minioUtils.getObjectUrl(collect.get(s)));
                    }
                }
            }

        }
        data1.put("list",data);
        apiResult.setData(data1);
        return apiResult;
    }
}
