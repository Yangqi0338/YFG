/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.service;

import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.*;
import com.base.sbc.pdm.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.pdm.entity.Tag;
import com.base.sbc.pdm.dao.TagDao;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * 类描述： service类
 *
 * @author lile
 * @version 1.0
 * @address com.base.sbc.pdm.service.TagService
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-18 10:05:23
 */
@Service
@Transactional(readOnly = true)
public class TagService extends BaseService<Tag> {

    @Autowired
    private TagDao tagDao;

    @Resource
    private TagMapper tagMapper;


    @Resource
    private BaseController baseController;

    @Override
    protected BaseDao<Tag> getEntityDao() {
        return tagDao;
    }

    public List<Tag> listQuery(Tag tag) {
        return tagMapper.listQuery(tag);
    }

    @Transactional(readOnly = false)
    public Integer delByIds(Tag tag) {
        tag.preUpdate();
        return tagMapper.delByIds(tag);
    }

    @Transactional(readOnly = false)
    public Integer update(Tag tag) {
        Tag tag1 = this.getById(tag.getId());
        if (!Objects.equals(tag1.getGroupId(), tag.getGroupId()) || !Objects.equals(tag1.getTagName(), tag.getTagName())) {
            Tag tag2 = tagMapper.getByNameAndGroupId(tag);
            if (tag2 != null) {
                return 0;
            }
        }
        tag.preUpdate();
        return tagMapper.update(tag);
    }

    @Transactional(readOnly = false)
    public Integer add(Tag tag) {
        //先去验证标签名是否重复,同类型下不能同标签名
        Tag tag1 = tagMapper.getByNameAndGroupId(tag);
        if (tag1 != null) {
            return 0;
        }
        tag.setCompanyCode(baseController.getUserCompany());
        tag.preInsert();
        tag.setDelFlag("0");
        return this.insert(tag);
    }

}
