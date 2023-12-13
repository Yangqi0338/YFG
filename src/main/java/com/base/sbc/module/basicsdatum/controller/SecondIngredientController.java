package com.base.sbc.module.basicsdatum.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.SecondIngredientSyncDto;
import com.base.sbc.module.basicsdatum.dto.SpecificationGroupDto;
import com.base.sbc.module.basicsdatum.entity.SpecificationGroup;
import com.base.sbc.module.basicsdatum.service.SpecificationGroupService;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang.math.IntRange;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/6/27 17:25
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@Api(tags = "基础资料-成分二级分类")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/secondIngredient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SecondIngredientController extends BaseController {

    @Autowired
    public CcmFeignService ccmFeignService;

    @Autowired
    public CcmService ccmService;

    @Autowired
    public SmpService smpService;

    private String uniqueDictCode = "pd021";
    private String dictPreCode = "EJ";

    @GetMapping("/sync2scm")
    @ApiOperation(value = "同步到scm")
    public ApiResult sync2scm(@Valid @NotEmpty(message = "同步code列表不能为空") String[] codeList){
        List<BasicBaseDict> pd021DictList = ccmFeignService.getDictInfoToList(uniqueDictCode);
        List<SecondIngredientSyncDto> syncDtoList = pd021DictList.stream().filter(it -> Arrays.asList(codeList).contains(it.getValue())).map(it -> {
            SecondIngredientSyncDto secondIngredientSyncDto = BeanUtil.copyProperties(it, SecondIngredientSyncDto.class);
            secondIngredientSyncDto.setKindCode(it.getValue());
            secondIngredientSyncDto.setKindName(it.getName());
            return secondIngredientSyncDto;
        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(syncDtoList)) {
            smpService.secondIngredient(syncDtoList);
        }
        return success("同步成功");
    }

    @PostMapping("/batchInsert")
    @ApiOperation(value = "新增字典")
    public ApiResult batchInsert(@Valid @RequestBody List<BasicBaseDict> basicBaseDicts){
        List<BasicBaseDict> pd021DictList = ccmFeignService.getDictInfoToList(uniqueDictCode);
        AtomicInteger num = new AtomicInteger(pd021DictList.size());
        ccmService.batchInsert(basicBaseDicts.stream().peek(it-> {
            it.setValue(dictPreCode + (num.incrementAndGet() < 100 ? (num.get() < 10 ? "00" : "0") : "") + num.get());
        }).collect(Collectors.toList()));
        return success("新增字典成功");
    }

}
