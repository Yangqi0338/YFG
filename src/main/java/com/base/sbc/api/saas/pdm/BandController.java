package com.base.sbc.api.saas.pdm;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.pdm.entity.Band;
import com.base.sbc.pdm.service.BandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public String add(@RequestBody Band band) {
        return bandService.add(band);
    }

    /**
     * 条件查询列表
     */
    @GetMapping("/listQuery")
    public ApiResult listQuery(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, Band band, Page page) {
        QueryCondition qc = new QueryCondition();
        qc.andEqualTo("company_code", userCompany);
        qc.andEqualTo("del_flag", "0");

        if(StringUtils.isNotBlank(page.getSearch())){
            qc.andLikeOr(page.getSearch(), "band_name", "code");
        }
        qc.setOrderByClause("create_date desc");
        return  bandService.findPageByCondition(qc,page);
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
