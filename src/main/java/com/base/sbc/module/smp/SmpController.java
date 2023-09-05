package com.base.sbc.module.smp;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.smp.dto.SmpProcessSheetDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/12 9:47:00
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "smp数据下发")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/distributeSmp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmpController extends BaseController {

    private final SmpService smpService;

    /**
     * 商品主数据下发（款式配色）
     */
    @PutMapping("/goods")
    public ApiResult goods(String[] ids){
        Integer i = smpService.goods(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }

    /**
     * 物料主数据下发(物料档案)
     */
    @PutMapping("/material")
    public ApiResult material(String[] ids) {
        Integer i = smpService.materials(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }

    /**
     * 颜色主数据下发  已验证
     */
    @PutMapping("/color")
    public ApiResult color(String[] ids) {
        Integer i = smpService.color(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }

    /**
     * bom下发
     */
    @PutMapping("/bom")
    public ApiResult bom(String[] ids) {
        Integer i = smpService.bom(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }

    /**
     * sample下发
     */
    @PutMapping("/sample")
    public ApiResult sample(String[] ids) {
        Integer i = smpService.sample(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }

    /**
     * 产前样下发
     */
    @PutMapping("/antenatalSample")
    public ApiResult antenatalSample(String[] ids) {
        Integer i = smpService.antenatalSample(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }

    /**
     * processSheet 下发
     */
    @PutMapping("/processSheet")
    public ApiResult processSheet(@RequestBody List<SmpProcessSheetDto> sheetDtoList) {
        Integer i = smpService.processSheet(sheetDtoList);
        return insertSuccess("下发：" + sheetDtoList.size() + "条，成功：" + i + "条");
    }


    /**
     * 面料成分名称码表下发
     */
    @PutMapping("/fabricComposition")
    public ApiResult fabricComposition(String[] ids) {
        Integer i = smpService.fabricComposition(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }
}
