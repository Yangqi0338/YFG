package com.base.sbc.open.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.mapper.HangTagIngredientMapper;
import com.base.sbc.module.hangtag.service.HangTagIngredientService;
import com.base.sbc.open.dto.DesignerDto;
import com.base.sbc.open.dto.SmpDeptDto;
import com.base.sbc.open.dto.SmpPostDto;
import com.base.sbc.open.dto.SmpUserDto;
import com.base.sbc.open.entity.SmpDept;

import com.base.sbc.open.entity.SmpPost;
import com.base.sbc.open.entity.SmpUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/6/29 9:20
 * @mail 247967116@qq.com
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BaseController.OPEN_URL + "/initData")
public class InitDataController {

    private final AmcService amcService;

    @Autowired
    private HangTagIngredientService hangTagIngredientService;

    @PostMapping("/dept")
    public void dept(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        Date date = new Date();
        List<SmpDept> smpDeptList =new ArrayList<>();
        List<SmpDeptDto> list = ExcelImportUtil.importExcel(file.getInputStream(), SmpDeptDto.class, params);
        for (SmpDeptDto smpDeptDto : list) {
            SmpDept smpDept =new SmpDept();
            BeanUtil.copyProperties(smpDeptDto,smpDept);
            smpDept.setCompanyCode("0");
            smpDept.setCreateId("系统初始化");
            smpDept.setCreateName("系统初始化");
            smpDept.setUpdateId("系统初始化");
            smpDept.setUpdateName("系统初始化");
            smpDept.setCreateDate(date);
            smpDept.setUpdateDate(date);
            smpDept.setDelFlag("0");
            smpDeptList.add(smpDept);
        }
        amcService.dept(smpDeptList);
    }

    @GetMapping("/removeIngredientZero")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult removeIngredientZero() {
        List<HangTagIngredient> list = hangTagIngredientService.list(new LambdaQueryWrapper<HangTagIngredient>().isNull(HangTagIngredient::getPercentageStr).isNotNull(HangTagIngredient::getPercentage));
        list = list.stream().filter(it-> it.getPercentage() != null).collect(Collectors.toList());
        list.stream().collect(Collectors.groupingBy(it-> it.getPercentage().intValue() == 100)).forEach((isHundred, sameList)-> {
            sameList.forEach(it-> {
                if (isHundred) {
                    it.setPercentageStr("100");
                }else {
                    it.setPercentageStr(it.getPercentage().setScale(1).toString());
                }
            });
        });
        hangTagIngredientService.saveOrUpdateBatch(list);
        return ApiResult.success("保存成功");
    }

    @PostMapping("/user")
    public void user(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        Date date = new Date();
        List<SmpUser> smpUsers =new ArrayList<>();

        List<SmpUserDto> smpUserDtos = ExcelImportUtil.importExcel(file.getInputStream(), SmpUserDto.class, params);
        for (SmpUserDto smpUserDto : smpUserDtos) {
            SmpUser smpUser1 =new SmpUser();
            BeanUtil.copyProperties(smpUserDto,smpUser1);
            smpUser1.setCompanyCode("0");
            smpUser1.setCreateId("系统初始化");
            smpUser1.setCreateName("系统初始化");
            smpUser1.setUpdateId("系统初始化");
            smpUser1.setUpdateName("系统初始化");
            smpUser1.setCreateDate(date);
            smpUser1.setUpdateDate(date);
            smpUser1.setDelFlag("0");
            smpUsers.add(smpUser1);
        }
        amcService.user(smpUsers);
    }


    /**
     * 设计师编码初始化
     */
    @PostMapping("/designerCode")
    public void designerCode(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        List<DesignerDto> designerDtoList = ExcelImportUtil.importExcel(file.getInputStream(), DesignerDto.class, params);
        amcService.designerCode(designerDtoList);
    }

    @PostMapping("/post")
    public void post(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);

        List<SmpPostDto> smpPostDtoList = ExcelImportUtil.importExcel(file.getInputStream(), SmpPostDto.class, params);
        List<SmpPost> smpPosts = BeanUtil.copyToList(smpPostDtoList, SmpPost.class);
        amcService.post(smpPosts);
    }
}
