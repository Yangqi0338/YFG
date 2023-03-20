package com.base.sbc.api.saas.pdm;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.pdm.entity.Band;
import com.base.sbc.pdm.service.BandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/18 17:54:12
 */
@RestController
@Api(tags = "1.2 SAAS接口[标签]")
@RequestMapping(value = BaseController.SAAS_URL + "/band", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BandController extends BaseController {
    @Resource
    private BandService bandService;

    /**
     * 新增波段
     */
    @PostMapping("/add")
    public ApiResult add(@RequestBody Band band) {
        return insertSuccess(bandService.add(band));
    }

    /**
     * 条件查询列表
     */
    @GetMapping("/listQuery")
    public ApiResult listQuery(Band band, Page page) {
        PageHelper.startPage(page);
        List<Band> bandList = bandService.listQuery(band);
        PageInfo<Band> pageInfo = new PageInfo<>(bandList);
        return selectSuccess(pageInfo);
    }

    /**
     * 根据id查询
     */
    @GetMapping("/getById")
    public ApiResult getById(String id) {
        return selectSuccess(bandService.getById(id));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("delByIds")
    public ApiResult delByIds(String[] ids) {
        return deleteSuccess(bandService.delByIds(ids));
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ApiResult update(@RequestBody Band band) {
        return updateSuccess(bandService.update(band));
    }
}
