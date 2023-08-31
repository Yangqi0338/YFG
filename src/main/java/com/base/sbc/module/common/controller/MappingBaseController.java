package com.base.sbc.module.common.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.service.BaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/8/31 10:45:16
 * @mail 247967116@qq.com
 * 通用控制层,controller继承此类即可有默认的方法映射
 */
@RestController
public class MappingBaseController<T> extends BaseController {


    /**
     * 注入通用service
     */
    private BaseService<T> baseService;
    @Autowired
    public void setBaseService(ApplicationContext applicationContext) {
        ResolvableType resolvableType = ResolvableType.forClass(getClass()).as(MappingBaseController.class);
        Type type = resolvableType.getGeneric().getType();
        String name = "";
        if (!"T".equals(type.getTypeName())) {
            name = type.getTypeName();
        }
        Map<String, BaseService> beansOfType = applicationContext.getBeansOfType(BaseService.class);
        for (String s : beansOfType.keySet()) {
            BaseService baseService1 = beansOfType.get(s);
            if (baseService1.getEntityClass().getName().equals(name)) {
                baseService = baseService1;
            }
        }
    }

    /**
     * 根据ids查询对应的列表1,2,3
     */
    @GetMapping("/listByIds")
    public ApiResult listByIds(String ids) {
        return selectSuccess(baseService.listByIds(Arrays.asList(ids.split(","))));
    }

    /**
     * 根据id查询所有字段
     * @param id 注解id
     * @return 实体对象
     */
    @GetMapping("/getById")
    public ApiResult getById(String id) {
        return selectSuccess(baseService.getById(id));
    }

    /**
     * 批量启用/停用
     * @param baseDto ids:, status:0启用1停用
     * @return 是否成功
     */
    @ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
    @PostMapping("/startStop")
    public Boolean startStop(@RequestBody BaseDto baseDto) {
        //根据传入的id进行批量启用/停用
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", Arrays.asList(baseDto.getIds().split(",")));
        updateWrapper.set("status", baseDto.getStatus());
        return baseService.update(updateWrapper);
    }


    /**
     * 根据ids批量删除
     * @param ids  ids:1,2,3
     * @return 是否成功
     */
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String ids) {
        baseService.removeByIds(Arrays.asList(ids.split(",")));
        return deleteSuccess("删除成功");
    }
}
