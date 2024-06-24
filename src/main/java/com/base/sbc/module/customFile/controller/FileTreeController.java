package com.base.sbc.module.customFile.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.customFile.dto.FileTreeDto;
import com.base.sbc.module.customFile.dto.MergeFolderDto;
import com.base.sbc.module.customFile.entity.FileTree;
import com.base.sbc.module.customFile.service.FileTreeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(
    value = "自定义文件夹相关接口",
    tags = {"自定义文件夹相关接口"})
@RequestMapping(
    value = BaseController.SAAS_URL + "/customFile",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FileTreeController extends BaseController {

  @Autowired private FileTreeService fileTreeService;

  @PostMapping("/addOrUpdate")
  @Transactional(rollbackFor = {Exception.class})
  @ApiOperation(value = "新增文件夹", notes = "新增文件夹")
  public ApiResult saveOrUpdate(@RequestBody FileTreeDto fileTreeDto) {
    String id = fileTreeService.addOrUpdate(fileTreeDto);
    return insertSuccess(id);
  }

  @GetMapping("/queryFileTree")
  @Transactional(rollbackFor = {Exception.class})
  @ApiOperation(value = "获取文件夹", notes = "获取文件夹")
  public ApiResult queryFileTree(FileTreeDto fileTreeDto) {
    List<FileTree> fileTrees = fileTreeService.queryFileTree(fileTreeDto);
    return success("查询成功", fileTrees);
  }

  @GetMapping("/del")
  @Transactional(rollbackFor = {Exception.class})
  @ApiOperation(value = "删除文件夹", notes = "删除文件夹")
  public ApiResult del(String id) {
    boolean b = fileTreeService.del(id);
    return deleteSuccess(b);
  }

  @PostMapping("/mergeFolder")
  @Transactional(rollbackFor = {Exception.class})
  @ApiOperation(value = "合并文件夹", notes = "合并文件夹")
  public ApiResult mergeFolder(@RequestBody MergeFolderDto dto) {
    Boolean result = fileTreeService.mergeFolder(dto);
    return success("合并成功", result);
  }
}
