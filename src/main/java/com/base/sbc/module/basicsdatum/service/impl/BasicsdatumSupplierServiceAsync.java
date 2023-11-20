package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/11/8 14:58:16
 * @mail 247967116@qq.com
 */
@Service
@Slf4j

public class BasicsdatumSupplierServiceAsync {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private BasicsdatumSupplierService basicsdatumSupplierService;



    /**
     * 异步写进数据库
     */
    @Async
    public void asyncSaveOrUpdate(List<BasicsdatumSupplier> basicsdatumSupplierList){
        try {
            log.debug("开始导入供应商");
            for (BasicsdatumSupplier basicsdatumSupplier : basicsdatumSupplierList) {
                QueryWrapper<BasicsdatumSupplier> queryWrapper =new BaseQueryWrapper<>();
                queryWrapper.eq("supplier_code",basicsdatumSupplier.getSupplierCode());
                basicsdatumSupplierService.saveOrUpdate(basicsdatumSupplier,queryWrapper);
            }
        }catch (Exception e){
            log.error("导入供应商失败",e);
        }finally {
            log.debug("导入供应商结束,清除缓存");
            redisUtils.del("com.base.sbc.module.basicsdatum.controller.BasicsdatumSupplierControllerbasicsdatumSupplierImportExcel");
        }
    }
}
