package com.base.sbc.module.smp;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 物料主数据下发(物料档案)
     */
    @PutMapping("/material")
    public ApiResult material(String[] ids) {
        Integer i = smpService.materials(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }

    /**
     * 颜色主数据下发
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
     * bom下发
     */
    @PutMapping("/sample")
    public ApiResult sample(String[] ids) {
        Integer i = smpService.sample(ids);
        return insertSuccess("下发："+ids.length+"条，成功："+i+"条");
    }
}
