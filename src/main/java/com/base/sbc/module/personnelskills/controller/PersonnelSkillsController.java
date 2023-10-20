package com.base.sbc.module.personnelskills.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.personnelskills.dto.PersonnelSkillsDto;
import com.base.sbc.module.personnelskills.entity.PersonnelSkills;
import com.base.sbc.module.personnelskills.service.PersonnelSkillsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/10 14:39:23
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "人员技能")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/personnelSkills", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonnelSkillsController extends BaseController {
    private final PersonnelSkillsService personnelSkillsService;

    @GetMapping("/queryPage")
    public ApiResult queryPage(PersonnelSkillsDto personnelSkillsDto) {
        BaseQueryWrapper<PersonnelSkills> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyLike("user_names", personnelSkillsDto.getUserNames());
        queryWrapper.notEmptyLike("position_names", personnelSkillsDto.getPositionNames());
        queryWrapper.notEmptyLike("category_names", personnelSkillsDto.getCategoryNames());
        queryWrapper.notEmptyLike("create_name", personnelSkillsDto.getCreateName());
        queryWrapper.notEmptyEq("status", personnelSkillsDto.getStatus());
        queryWrapper.between("create_date", personnelSkillsDto.getCreateDate());
        PageHelper.startPage(personnelSkillsDto);
        List<PersonnelSkills> list = personnelSkillsService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }

    @PostMapping("/save")
    public ApiResult save(@RequestBody PersonnelSkills personnelSkills) {
        QueryWrapper<PersonnelSkills> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("position_ids", personnelSkills.getPositionIds());
        queryWrapper.eq("category_codes", personnelSkills.getCategoryCodes());
        PersonnelSkills one = personnelSkillsService.getOne(queryWrapper);

        if (one != null && !personnelSkills.getId().equals(one.getId())) {
            throw new OtherException("岗位：" + personnelSkills.getPositionNames() + ",品类：" + personnelSkills.getCategoryNames() + "已存在相同的记录");
        }
        personnelSkillsService.saveOrUpdate(personnelSkills);
        return updateSuccess("保存成功");

    }

    @PutMapping("/delByIds")
    public ApiResult delByIds(String[] ids) {
        personnelSkillsService.removeByIds(Arrays.asList(ids));
        return updateSuccess("保存成功");
    }

    /**
     * 启用或者停用
     */
    @PutMapping("/startStop")
    @ApiOperation(value = "启用或者停用")
    public ApiResult startStop(@RequestBody PersonnelSkillsDto personnelSkillsDto) {
        UpdateWrapper<PersonnelSkills> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", personnelSkillsDto.getStatus());
        updateWrapper.in("id", Arrays.asList(personnelSkillsDto.getIds().split(",")));
        personnelSkillsService.update(updateWrapper);
        return updateSuccess("操作成功");
    }
}
