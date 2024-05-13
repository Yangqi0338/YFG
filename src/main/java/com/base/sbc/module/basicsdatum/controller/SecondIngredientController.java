package com.base.sbc.module.basicsdatum.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.annotation.DuplicationCheck;
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
import org.springframework.context.annotation.Lazy;
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
import java.util.Comparator;
import java.util.LinkedList;
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
    @Lazy
    @Autowired
    public SmpService smpService;

    public static String uniqueDictCode = "pd021";
    private final String dictPreCode = "EJ";

    @GetMapping("/sync2scm")
    @ApiOperation(value = "同步到scm")
    @DuplicationCheck(time = 1)
    public ApiResult sync2scm(@Valid @NotEmpty(message = "同步code列表不能为空") String[] ids){
        List<BasicBaseDict> pd021DictList = ccmFeignService.getAllDictInfoToList(uniqueDictCode);
        List<SecondIngredientSyncDto> syncDtoList = pd021DictList.stream().filter(it -> Arrays.asList(ids).contains(it.getId())).map(it -> {
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

    @GetMapping("/batchDeleteAndSync")
    @ApiOperation(value = "批量删除并同步")
    @DuplicationCheck(time = 1)
    public ApiResult batchDeleteAndSync(@Valid @NotEmpty(message = "同步id列表不能为空") String[] ids){
        List<BasicBaseDict> pd021DictList = ccmFeignService.getAllDictInfoToList(uniqueDictCode);
        List<SecondIngredientSyncDto> syncDtoList = pd021DictList.stream().filter(it -> Arrays.asList(ids).contains(it.getId())).map(it -> {
            SecondIngredientSyncDto secondIngredientSyncDto = BeanUtil.copyProperties(it, SecondIngredientSyncDto.class);
            secondIngredientSyncDto.setKindCode(it.getValue());
            secondIngredientSyncDto.setKindName(it.getName());
            secondIngredientSyncDto.setStatus("1");
            return secondIngredientSyncDto;
        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(syncDtoList)) {
            smpService.secondIngredient(syncDtoList);
        }

        ccmService.batchDeleteDict(String.join(",", ids));

        return success("同步成功");
    }

    @PostMapping("/batchInsert")
    @ApiOperation(value = "新增字典")
    @DuplicationCheck(time = 2)
    public ApiResult batchInsert(@Valid @RequestBody List<BasicBaseDict> basicBaseDicts){
        List<BasicBaseDict> pd021DictList = ccmFeignService.getAllDictInfoToList(uniqueDictCode);
        pd021DictList = pd021DictList.stream().filter(it-> NumberUtil.isNumber(it.getValue().replace(dictPreCode,""))).collect(Collectors.toList());
        int startIndex = pd021DictList.size();

        if (startIndex != 0) {
            pd021DictList.sort(Comparator.comparing(BasicBaseDict::getId));
            BasicBaseDict baseDict = new LinkedList<>(pd021DictList).getLast() ;
            if (baseDict != null) {
                startIndex = Integer.parseInt(baseDict.getValue().replace(dictPreCode, ""));
            }
        }

        AtomicInteger num = new AtomicInteger(startIndex);
        ccmService.batchInsert(basicBaseDicts.stream().peek(it-> {
            it.setValue(dictPreCode + (num.incrementAndGet() < 100 ? (num.get() < 10 ? "00" : "0") : "") + num.get());
        }).collect(Collectors.toList()));
        return success("新增字典成功");
    }

}
