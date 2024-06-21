package com.base.sbc.module.smp;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.smp.dto.ScmProductionBudgetQueryDto;
import com.base.sbc.module.smp.dto.SmpProcessSheetDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @DuplicationCheck
    public ApiResult goods(String[] ids){
        Integer i = smpService.goods(ids);
        if (ids.length== i) {
            return insertSuccess("下发：" + ids.length + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + ids.length + "条，成功：" + i + "条,失败：" + (ids.length - i) + "条", 200);
        }
    }

    /**
     * 物料主数据下发(物料档案)
     */
    @PutMapping("/material")
    @DuplicationCheck
    public ApiResult material(String[] ids) {
        Integer i = smpService.materials(ids);
        if (ids.length== i) {
            return insertSuccess("下发：" + ids.length + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + ids.length + "条，成功：" + i + "条,失败：" + (ids.length - i) + "条", 200);
        }
    }

    /**
     * 颜色主数据下发  已验证
     */
    @PutMapping("/color")
    @DuplicationCheck
    public ApiResult color(String[] ids) {
        Integer i = smpService.color(ids);
        if (ids.length== i) {
            return insertSuccess("下发：" + ids.length + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + ids.length + "条，成功：" + i + "条,失败：" + (ids.length - i) + "条", 200);
        }
    }

    /**
     * bom下发
     */
    @PutMapping("/bom")
    @DuplicationCheck
    public ApiResult bom(String[] ids) {
        Integer i = smpService.bom(ids);
        if (ids.length== i) {
            return insertSuccess("下发：" + ids.length + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + ids.length + "条，成功：" + i + "条,失败：" + (ids.length - i) + "条", 200);
        }
    }
    //
    ///**
    // * sample下发
    // */
    //@PutMapping("/sample")
    //public ApiResult sample(String[] ids) {
    //    Integer i = smpService.sample(ids);
    //    return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    //}

    ///**
    // * 产前样下发
    // */
    //@PutMapping("/antenatalSample")
    //public ApiResult antenatalSample(String[] ids) {
    //    Integer i = smpService.antenatalSample(ids);
    //    return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    //}

    /**
     * processSheet 下发
     */
    @PutMapping("/processSheet")
    @DuplicationCheck
    public ApiResult processSheet(@RequestBody List<SmpProcessSheetDto> sheetDtoList) {
        Integer i = smpService.processSheet(sheetDtoList);
        if (sheetDtoList.size()== i) {
            return insertSuccess("下发：" + sheetDtoList.size()+ "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + sheetDtoList.size() + "条，成功：" + i + "条,失败：" + (sheetDtoList.size() - i) + "条", 200);
        }
    }


    /**
     * 面料成分名称码表下发
     */
    @PutMapping("/fabricComposition")
    @DuplicationCheck
    public ApiResult fabricComposition(String[] ids) {
        Integer i = smpService.fabricComposition(ids);
        if (ids.length== i) {
            return insertSuccess("下发：" + ids.length + "条，成功：" + i + "条");
        } else {
            return ApiResult.error("下发：" + ids.length + "条，成功：" + i + "条,失败：" + (ids.length - i) + "条", 200);
        }
    }

    /**
     * escm预算号查询
     */
    @PostMapping("/productionBudgetList")
    public ApiResult productionBudgetList(@RequestBody ScmProductionBudgetQueryDto productionBudgetQueryDto) {
        return smpService.productionBudgetList(productionBudgetQueryDto);
    }
}
