package com.base.sbc.module.material.controller;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.material.entity.MaterialCollect;
import com.base.sbc.module.material.service.MaterialCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 类描述：素材库收藏控制器
 *
 * @author 卞康
 * @version 1.0
 * @date 创建时间：2023-4-1 16:26:15
 */
@RestController
@Api(value = "与素材收藏相关的所有接口信息", tags = {"素材收藏表接口"})
@RequestMapping(value = BaseController.SAAS_URL + "/materialCollect", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialCollectController extends BaseController {

    @Resource
    private MaterialCollectService materialCollectService;

    @Resource
    private UserUtils userUtils;

    @ApiOperation(value = "素材收藏", notes = "素材收藏")
    @PostMapping("/add")
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult add(@RequestBody MaterialCollect materialCollect) throws Exception {
        String userId = userUtils.getUserId();
        QueryWrapper<MaterialCollect> qc = new QueryWrapper<>();
        qc.eq("material_id", materialCollect.getMaterialId());
        qc.eq("user_id", userId);
        if (materialCollectService.getOne(qc) != null) {
            throw new OtherException("请勿重复收藏");
        }
        materialCollect.setUserId(userId);
        materialCollectService.save(materialCollect);
        return insertSuccess(materialCollect.getId());
    }

    @ApiOperation(value = "素材取消收藏", notes = "素材取消收藏")
    @DeleteMapping("/del")
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult del(MaterialCollect materialCollect) {
        String userId = userUtils.getUserId();
        QueryWrapper<MaterialCollect> qc = new QueryWrapper<>();
        qc.eq("material_id", materialCollect.getMaterialId());
        qc.eq("user_id", userId);
        MaterialCollect materialCollect1 = materialCollectService.getOne(qc);
        if (materialCollect1 == null) {
            throw new OtherException("此素材未收藏");
        }
        return deleteSuccess(materialCollectService.removeById(materialCollect1.getId()));
    }
}
