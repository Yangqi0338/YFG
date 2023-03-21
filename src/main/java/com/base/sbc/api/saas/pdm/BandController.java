package com.base.sbc.api.saas.pdm;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.pdm.entity.Band;
import com.base.sbc.pdm.service.BandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
        Integer i = bandService.add(band);
        if (i == 0) {
            return insertDataRepeat("标签名称重复");
        }
        return insertSuccess("新增成功");
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
    /**
     * 启动 停止
     */
    @ApiOperation(value = "批量启用/停用波段", notes = "ids:制造流程ids(多个用逗号拼接), status:0启用1停用")
    @PostMapping("bandStartStop")
    public ApiResult bandStartStop(@RequestBody Band band) {
        return deleteSuccess(bandService.bandStartStop(band));
    }

}
