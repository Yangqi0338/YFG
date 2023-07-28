package com.base.sbc.module.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.operaLog.service.OperaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/13 11:50:06
 */

public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    @Resource
    private UserUtils userUtils;

    public static final String COMPANY_CODE = "company_code";
    public static final String DEL_FLAG = "del_flag";


    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取企业编码
     *
     * @return
     */
    public String getCompanyCode() {
        return userUtils.getCompanyCode();
    }

    public String getUserId() {
        return userUtils.getUserCompany().getUserId();
    }

    /**
     * 批量提交修改，逻辑删除新增修改
     *
     * @param entityList   实体列表
     * @param queryWrapper 构造器
     * @return 传入实体列表的总长度
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper) {
        String companyCode = userUtils.getCompanyCode();
        //分类
        // 新增的
        Collection<T> addList = new ArrayList<>();
        // 修改的
        Collection<T> updateList = new ArrayList<>();

        Collection<String> ids = new ArrayList<>();
        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add(entity);
            } else {
                //说明是修改的
                updateList.add(entity);
                ids.add(entity.getId());
            }
        }
        if (!StringUtils.isEmpty(companyCode) && !"0".equals(companyCode)) {
            queryWrapper.eq("company_code", companyCode);
        }
        //逻辑删除传进来不存在的
        if (!ids.isEmpty()) {
            queryWrapper.notIn("id", ids);
        }
        this.remove(queryWrapper);
        //新增
        this.saveBatch(addList);
        //修改
        this.updateBatchById(updateList);

        return entityList.size();
    }

    /**
     * 慎用！！！！！！！！。
     * 根据id物理删除数据
     * @param id 主键id
     * @return 操作结果
     */
    @Override
    public Boolean physicalDeleteById(String id) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getEntityClass());
        String tableName = tableInfo.getTableName();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        int update = jdbcTemplate.update(sql, id);
        log.debug("=================> 物理删除 SQL："+sql + " | 返回值："+update);
        return update>0;
    }

    /**
     * 慎用！！！！！！！！。
     * 根据构造器物理删除数据
     *
     * @param queryWrapper 构造器
     * @return 删除的数量
     */
    @Override
    public Integer physicalDeleteById(QueryWrapper<T> queryWrapper) {
        String sqlSelect = queryWrapper.getSqlSelect();
        System.out.println(sqlSelect);
        return null;
    }


    public void setUpdateInfo(UpdateWrapper<T> uw) {
        UserCompany userCompany = userUtils.getUserCompany();
        uw.set("update_id", userCompany.getId());
        uw.set("update_name", userCompany.getAliasUserName());
        uw.set("update_date", new Date());
    }


}
