package com.base.sbc.module.basicsdatum.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicProcessGalleryDto;
import com.base.sbc.module.basicsdatum.dto.BasicProcessGallerySaveDto;
import com.base.sbc.module.basicsdatum.entity.BasicProcessGallery;
import com.base.sbc.module.basicsdatum.service.BasicProcessGalleryService;
import com.base.sbc.module.basicsdatum.vo.BasicProcessGalleryExcelVo;
import com.base.sbc.module.basicsdatum.vo.BasicProcessGalleryVo;
import com.base.sbc.module.common.dto.RemoveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
            basicProcessGalleryService.saveOrUpdate(basicProcessGallerySaveDto);
        }catch (DuplicateKeyException e){
            throw new RuntimeException("该编码或者名称已存在");
        }

        return success("保存成功");
    }

    /**
     * 启用停用
     */
    @PostMapping(value = "/startStop")
    public ApiResult<Object> startStop(@RequestBody BasicProcessGallerySaveDto basicProcessGallerySaveDto) {
        UpdateWrapper<BasicProcessGallery> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", basicProcessGallerySaveDto.getIds());
        updateWrapper.set("status", basicProcessGallerySaveDto.getStatus());
        basicProcessGalleryService.update(updateWrapper);
        return success("保存成功");
    }

    /**
     * 删除
     */
    @PostMapping(value = "/remove")
    @DuplicationCheck
    public ApiResult<Object> remove(@RequestBody RemoveDto removeDto) {
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



}
