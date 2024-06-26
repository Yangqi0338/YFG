package com.base.sbc.module.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.base.sbc.config.annotation.QueryField;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.mapper.BaseEnhanceMapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.github.pagehelper.SqlUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Resource
    public OperaLogService operaLogService;

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

    public String getVirtualDeptIds() {
        List<String> virtualDeptIds = userUtils.getUserCompany().getVirtualDeptIds();
        if (virtualDeptIds.size() > 1) throw new OtherException("!当前操作人非法存在于两个及以上的虚拟部门!");
        return virtualDeptIds.get(0);
    }

    public String getUserName() {
        return userUtils.getUserCompany().getAliasUserName();
    }

    public SqlSession getSqlSession(){
        SqlSessionFactory sqlSessionFactory = SqlHelper.sqlSessionFactory(entityClass);
        SqlSessionHolder sqlSessionHolder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sqlSessionFactory);
        boolean transaction = TransactionSynchronizationManager.isSynchronizationActive();
        SqlSession sqlSession;
        if (sqlSessionHolder != null) {
            sqlSession = sqlSessionHolder.getSqlSession();
            sqlSession.commit(!transaction);
        }

        sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        return sqlSession;
    }

    /**
     * 跟据字段名称和字段集合查询列表
     *
     * @param fieldName 字段名称
     * @param list      数据集合
     * @return 查询结果
     */
    @Override
    public List<T> listByField(String fieldName, Collection<?> list) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 驼峰转下划线
        fieldName = StringUtils.toUnderScoreCase(fieldName);
        queryWrapper.in(fieldName, list);
        return this.list(queryWrapper);
    }

    /**
     * 跟据字段名称和字段集合查询列表
     *
     * @param fieldName 字段名称
     * @param list      数据集合
     * @return 查询结果
     */

    @Override
    public List<T> listByField(String fieldName, String list) {
        return this.listByField(fieldName, StrUtil.split(list, CharUtil.COMMA));
    }

    /**
     * 跟据字段名称和字段查询一条数据
     *
     * @param fieldName
     * @param id
     * @return
     */
    @Override
    public T getByOne(String fieldName, String id) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 驼峰转下划线
        fieldName = StringUtils.toUnderScoreCase(fieldName);
        queryWrapper.eq(fieldName, id);
        return this.getOne(queryWrapper);
    }

    /**
     * 跟据字段名称和字段查询多条数据
     *
     * @param fieldName
     * @param id
     * @return
     */
    @Override
    public List<T> getByList(String fieldName, String id) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 驼峰转下划线
        fieldName = StringUtils.toUnderScoreCase(fieldName);
        queryWrapper.eq(fieldName, id);
        return this.list(queryWrapper);
    }

    /**
     * 根据传入的对象，查询符合条件的数据,默认模糊查询
     * 根据注解in字段,确定是否是in查询
     * 根据注解not字段,确定是否是not查询
     * 根据注解like字段,确定是否是like查询
     *
     * @param object   传入的对象
     * @param exFields 排除的字段
     * @return 查询结果
     */
    @Override
    public List<T> listByObject(Object object, BaseQueryWrapper<T> baseQueryWrapper, String... exFields) {
        // todo 未完成
        List<Field> allFields = CommonUtils.getAllFields(object);
        // 去掉排除的字段
        List<String> exFieldList = Arrays.asList(exFields);
        allFields = allFields.stream().filter(field -> !exFieldList.contains(field.getName())).collect(Collectors.toList());
        for (Field field : allFields) {
            try {
                //如果值为空，跳过
                if (field.get(object) == null) {
                    continue;
                }
                field.setAccessible(true);
                // 驼峰转下划线
                String fieldName = StringUtils.toUnderScoreCase(field.getName());
                //如果没有注解就按照默认模糊查询
                QueryField annotation = field.getAnnotation(QueryField.class);
                if(annotation== null){
                    baseQueryWrapper.like(fieldName, field.get(object));
                    continue;
                }
                //根据注解type字段,进行查询
                CommonUtils.getAllMethod(baseQueryWrapper).forEach(method -> {
                    if (method.getName().equals(annotation.type())) {
                        try {
                            if (annotation.type().equals("in")) {
                                // 如果是in查询，需要传入集合,如果不是集合，就转成集合
                                if (!(field.get(object) instanceof Collection)) {
                                    Collection<Object> list = new ArrayList<>();
                                    list.add(field.get(object));
                                    method.invoke(baseQueryWrapper, fieldName, list);
                                    return;
                                }
                            }

                            method.invoke(baseQueryWrapper, fieldName, field.get(object));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
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
        // 分类
        // 新增的
        Collection<T> addList = new ArrayList<>();
        // 修改的
        Collection<T> updateList = new ArrayList<>();

        Collection<String> ids = new ArrayList<>();
        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                // 说明是新增的
                entity.setId(null);
                addList.add(entity);
            } else {
                // 说明是修改的
                updateList.add(entity);
                ids.add(entity.getId());
            }
        }
        if (!StringUtils.isEmpty(companyCode) && !"0".equals(companyCode)) {
            queryWrapper.eq("company_code", companyCode);
        }
        // 逻辑删除传进来不存在的
        if (!ids.isEmpty()) {
            queryWrapper.notIn("id", ids);
        }
        this.remove(queryWrapper);
        // 新增
        this.saveBatch(addList);
        // 修改
        this.updateBatchById(updateList);

        return entityList.size();
    }

    /**
     * 慎用！！！！！！！！。
     * 根据id物理删除数据
     *
     * @param id 主键id
     * @return 操作结果
     */
    @Override
    public Boolean physicalDeleteById(String id) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getEntityClass());
        String tableName = tableInfo.getTableName();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        int update = jdbcTemplate.update(sql, id);
        log.warn("=================> 物理删除 SQL：" + sql + " | 返回值：" + update);
        return update > 0;
    }

    /**
     * 慎用！！！！！！！！。
     * 根据构造器物理删除数据
     *
     * @param queryWrapper 构造器
     * @return 删除的数量
     */
    @Override
    public Integer physicalDeleteQWrap(QueryWrapper<T> queryWrapper) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getEntityClass());
        String tableName = tableInfo.getTableName();

        String customSqlSegment = queryWrapper.getCustomSqlSegment();

        Map<String, Object> paramNameValuePairs = queryWrapper.getParamNameValuePairs();
        ArrayList<Object> list = new ArrayList<>();
        int i = 1;
        for (String key : paramNameValuePairs.keySet()) {
            customSqlSegment = customSqlSegment.replace("#{ew.paramNameValuePairs.MPGENVAL" + i + "}", "?");
            list.add(paramNameValuePairs.get("MPGENVAL" + i));
            i++;
        }

        String sql = String.format("DELETE FROM %s %s", tableName, customSqlSegment);
        Object[] params = list.toArray();
        int update = jdbcTemplate.update(sql, params);
        log.warn("=================> 物理删除 SQL： " + sql + " | 参数：" + Arrays.toString(params) + " | 删除的行数：" + update);
        return update;
    }


    public void setUpdateInfo(UpdateWrapper<T> uw) {
        UserCompany userCompany = userUtils.getUserCompany();
        uw.set("update_id", userCompany.getId());
        uw.set("update_name", userCompany.getAliasUserName());
        uw.set("update_date", new Date());
    }


    /**
     * 新增或者更新,并且记录日志
     *
     * @param entity 实体对象
     * @param name   模块名称
     * @return boolean
     */
    @Override
    public boolean saveOrUpdate(T entity, String name) {
        if (!StringUtils.isEmpty(entity.getId())) {
            return this.updateById(entity, name);
        } else {
            return this.save(entity, name);
        }
    }


    /**
     * 新增或者更新,并且记录日志
     *
     * @param entity       实体对象
     * @param name         模块名称
     * @param documentName 名称
     * @param documentCode 编码
     * @return boolean
     */
    @Override
    public boolean saveOrUpdate(T entity, String name, String documentName, String documentCode) {
        if (this.getById(entity.getId()) != null) {
            return this.updateById(entity, name, documentName, documentCode);
        } else {
            return this.save(entity, name, documentName, documentCode);
        }
    }

    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    public boolean save(T entity, String name) {
        String type = "新增";
        boolean save = this.save(entity);
        if (save) {
            this.saveOperaLog(type, name, entity, null);
        }
        return save;
    }

    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    public boolean save(T entity, String name, String documentName, String documentCode) {
        String type = "新增";
        boolean save = this.save(entity);
        if (save) {
            this.saveOperaLog(type, name, documentName, documentCode, entity, null);
        }
        return save;
    }

    /**
     * 批量保存日志
     *
     * @param entity
     * @param name
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<T> entity, String name) {
        String type = "新增";
        boolean save = saveBatch(entity);
        if (save) {
            this.saveOperaLogBatch(type, name, entity, new ArrayList<>());
        }
        return save;
    }

    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    public boolean updateById(T entity, String name) {
        String type = "修改";
        T oldEntity = this.getById(entity.getId());
        boolean update = this.updateById(entity);
        if (update) {
            this.saveOperaLog(type, name, entity, oldEntity);
        }
        return update;
    }

    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    public boolean updateById(T entity, String name, String documentName, String documentCode) {
        String type = "修改";
        T oldEntity = this.getById(entity.getId());
        boolean update = this.updateById(entity);
        if (update) {
            this.saveOperaLog(type, name, documentName, documentCode, entity, oldEntity);
        }
        return update;
    }


    /**
     * @param entity
     * @param name   模块名称
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(List<T> entity, String name) {
        if (CollUtil.isEmpty(entity)) {
            return true;
        }
        String type = "修改";
        List<String> ids = entity.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<T> oldEntity = this.listByIds(ids);
        boolean update = this.updateBatchById(entity);
        if (update) {
            this.saveOperaLogBatch(type, name, entity, oldEntity);
        }
        return update;
    }


    /**
     * 批量保存操作日志
     *
     * @param type
     * @param name
     * @param newObject
     * @param oldObject
     */
    @Override
    public void saveOperaLogBatch(String type, String name, List<T> newObject, List<T> oldObject) {
        List<OperaLogEntity> operaLogEntityList = new ArrayList<>();

        for (T t : newObject) {
            JSONArray jsonArray = new JSONArray();
            for (T t1 : oldObject) {
                if (t.getId().equals(t1.getId())) {
                    jsonArray = CommonUtils.recordField(t, t1);
                }
            }
            String documentCode = this.getFieldValueByName("code", newObject);
            String documentName = this.getFieldValueByName("name", newObject);

            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setDocumentId(t.getId());
            operaLogEntity.setName(name);
            operaLogEntity.setType(type);
            operaLogEntity.setDocumentName(documentName);
            operaLogEntity.setDocumentCode(documentCode);
            operaLogEntity.setJsonContent(jsonArray.toJSONString());
            operaLogEntityList.add(operaLogEntity);
        }
        operaLogService.saveBatch(operaLogEntityList);
    }

    /**
     * 保存操作日志
     *
     * @param type      操作类型  新增 修改 删除
     * @param name      模块名称
     * @param newObject 新对象
     * @param oldObject 旧对象
     */
    @Override
    public void saveOperaLog(String type, String name, T newObject, T oldObject) {
        String documentCode = this.getFieldValueByName("code", newObject);
        String documentName = this.getFieldValueByName("name", newObject);
        this.saveOperaLog(type, name, documentName, documentCode, newObject, oldObject);
    }

    @Override
    public void saveOperaLog(OperaLogEntity operaLogEntity) {
        operaLogService.save(operaLogEntity);
    }

    /**
     * 保存操作日志
     *
     * @param type      操作类型  新增 修改 删除
     * @param name      模块名称
     * @param newObject 新对象
     * @param oldObject 旧对象
     */
    @Override
    public void saveOperaLog(String type, String name, String documentName, String documentCode, T newObject, T oldObject) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        JSONArray jsonArray = CommonUtils.recordField(newObject, oldObject);
        operaLogEntity.setDocumentId(newObject.getId());
        operaLogEntity.setJsonContent(jsonArray.toJSONString());
        operaLogEntity.setName(name);
        operaLogEntity.setDocumentName(documentName);
        operaLogEntity.setDocumentCode(documentCode);
        operaLogEntity.setType(type);
        operaLogService.save(operaLogEntity);
    }


    /**
     * 保存操作日志
     *
     * @param type 操作类型  新增 修改 删除
     * @param name 模块名称
     */
    @Override
    public void saveOperaLog(String type, String name, String documentName, String documentCode, Map<String, String> data) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<String, String> listEntry : data.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", listEntry.getKey());
            if (listEntry.getValue() != null) {
                String[] split = listEntry.getValue().split("->");
                jsonObject.put("oldStr", split.length > 0 ? split[0] : "");
                jsonObject.put("newStr", split.length > 1 ? split[1] : "");
                jsonArray.add(jsonObject);
            }

        }

        operaLogEntity.setJsonContent(jsonArray.toJSONString());
        operaLogEntity.setName(name);
        operaLogEntity.setDocumentName(documentName);
        operaLogEntity.setDocumentCode(documentCode);
        operaLogEntity.setType(type);
        operaLogService.save(operaLogEntity);
    }


    /**
     * 保存操作日志
     *
     * @param newObject
     * @param oldObject
     */
    @Override
    public void saveOrUpdateOperaLog(Object newObject, Object oldObject, OperaLogEntity operaLogEntity) {
        JSONArray jsonArray = CommonUtils.recordField(newObject, oldObject);
        operaLogEntity.setJsonContent(jsonArray.toJSONString());
        operaLogService.save(operaLogEntity);
    }

    /**
     * 保存修改操作日志
     *
     * @param type         操作类型 新增/修改
     * @param name         模块名称
     * @param parentId     父级id
     * @param documentName 单据名称
     * @param documentCode 单据编码
     * @param newObject    新对象
     * @param oldObject    原对象
     */
    @Override
    public void saveOperaLog(String type, String name, String parentId, String documentName, String documentCode, T newObject, T oldObject) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        JSONArray jsonArray = CommonUtils.recordField(newObject, oldObject);
        operaLogEntity.setDocumentId(newObject.getId());
        operaLogEntity.setJsonContent(jsonArray.toJSONString());
        operaLogEntity.setName(name);
        operaLogEntity.setDocumentName(documentName);
        operaLogEntity.setDocumentCode(documentCode);
        operaLogEntity.setType(type);
        operaLogEntity.setParentId(parentId);
        operaLogService.save(operaLogEntity);
    }

    /**
     * 批量保存操作日志
     */
    @Override
    public void saveBatchOperaLog(List<T> newObject, OperaLogEntity operaLogEntity) {
        updateBatchOperaLog(newObject, null, operaLogEntity);
    }

    /**
     * 批量修改操作日志
     *
     * @param newObject
     * @param oldObject
     * @param operaLogEntity
     */
    @Override
    public void updateBatchOperaLog(List<T> newObject, List<T> oldObject, OperaLogEntity operaLogEntity) {
        List<OperaLogEntity> operaLogEntityList = new ArrayList<>();
        Map<String, T> newMap = Opt.ofNullable(newObject).map(no -> no.stream().collect(Collectors.toMap(BaseEntity::getId, v -> v, (a, b) -> a))).orElse(MapUtil.empty());
        Map<String, T> oldMap = Opt.ofNullable(oldObject).map(no -> no.stream().collect(Collectors.toMap(BaseEntity::getId, v -> v, (a, b) -> a))).orElse(MapUtil.empty());
        for (Map.Entry<String, T> entry : newMap.entrySet()) {
            OperaLogEntity log = BeanUtil.copyProperties(operaLogEntity, OperaLogEntity.class);
            log.setId(null);
            String id = entry.getKey();
            T ne = entry.getValue();
            T oe = oldMap.get(id);
            JSONArray jsonArray = CommonUtils.recordField(ne, oe);
            log.setJsonContent(jsonArray.toJSONString());
            log.setDocumentId(id);
            log.setDocumentName(this.getFieldValueByName(log.getDocumentNameField(), ne));
            log.setDocumentCode(this.getFieldValueByName(log.getDocumentCodeField(), ne));
            operaLogEntityList.add(log);
        }
        operaLogService.saveOrUpdateBatch(operaLogEntityList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeByIds(Collection<?> list, OperaLogEntity operaLogEntity) {
        String ids = CollUtil.join(list, StrUtil.COMMA);
        operaLogEntity.setType("删除");
        operaLogEntity.setContent(ids);
        operaLogService.save(operaLogEntity);
        boolean flg = super.removeByIds(list);
        return flg;
    }

    @Override
    public boolean removeById(Serializable id, OperaLogEntity operaLogEntity) {
        boolean b = super.removeById(id);
        operaLogEntity.setType("删除");
        operaLogEntity.setDocumentId(id.toString());
        operaLogEntity.setContent(id.toString());
        operaLogService.save(operaLogEntity);
        return b;
    }

    @Override
    @Transactional
    public boolean removeByIds(RemoveDto removeDto) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        List<String> ids = Arrays.asList(removeDto.getIds().split(","));
        operaLogEntity.setName(removeDto.getName());
        operaLogEntity.setType("删除");
        operaLogEntity.setContent(removeDto.getIds());
        operaLogEntity.setDocumentName(removeDto.getNames());
        operaLogEntity.setParentId(removeDto.getParentId());
        operaLogEntity.setDocumentCode(removeDto.getCodes());
        operaLogService.save(operaLogEntity);
        return super.removeByIds(ids);
    }

    @Override
    public boolean saveLog(OperaLogEntity operaLogEntity) {
        return operaLogService.save(operaLogEntity);
    }

    /**
     * 启用与停用记录日志
     */
    @Override
    public void startStopLog(StartStopDto startStopDto) {
        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setDocumentName(startStopDto.getNames());
        operaLogEntity.setDocumentCode(startStopDto.getCodes());
        operaLogEntity.setType("0".equals(startStopDto.getStatus()) ? "启用" : "停用");
        operaLogEntity.setName(startStopDto.getName());
        operaLogEntity.setParentId(startStopDto.getParentId());
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", startStopDto.getIds());
        updateWrapper.set("status", startStopDto.getStatus());
        this.update(updateWrapper);
        this.saveLog(operaLogEntity);
    }

    @Override
    public boolean exists(Wrapper<T> wrapper) {
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean exists(String id) {
        return this.count(new QueryWrapper<T>().eq("id",id)) > 0;
    }

    @Override
    public T findOne(QueryWrapper<T> wrapper) {
        return this.list(wrapper.last("limit 1")).stream().findFirst().orElse(null);
    }

    @Override
    public T findOne(LambdaQueryWrapper<T> wrapper) {
        return this.list(wrapper.last("limit 1")).stream().findFirst().orElse(null);
    }

    @Override
    public <R> List<R> listOneField(LambdaQueryWrapper<T> wrapper, SFunction<T, R> function) {
        return this.list(wrapper.select(function)).stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }

    @Override
    public <R> List<R> listByIds2OneField(List<String> ids, SFunction<T, R> function) {
        return listOneField(new LambdaQueryWrapper<T>().in(T::getId, ids), function);
    }


    /**
     * 根据字段名称获取对象的值，包括父类的字段
     */
    private String getFieldValueByName(String fieldName, Object o) {
        try {
            Field field = null;
            Class<?> currentClass = o.getClass();

            // 从当前类及其父类中查找字段
            while (currentClass != null) {
                try {
                    field = currentClass.getDeclaredField(fieldName);
                    break;  // 找到字段后退出循环
                } catch (NoSuchFieldException e) {
                    // 如果当前类没有该字段，尝试查找父类
                    currentClass = currentClass.getSuperclass();
                }
            }

            if (field != null) {
                field.setAccessible(true);
                return (String) field.get(o);
            } else {
                return null;  // 未找到指定字段
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public <R> R findOneField(LambdaQueryWrapper<T> wrapper, SFunction<T, R> function) {
        // 先清掉PageHelper,以免报错
        // https://blog.csdn.net/qq_42696265/article/details/131944397
        SqlUtil.clearLocalPage();
        return this.list(wrapper.select(function).last("limit 1")).stream().findFirst().map(function).orElse(null);
    }

    @Override
    public <R> R findByIds2OneField(String id, SFunction<T, R> function) {
        return findOneField(new LambdaQueryWrapper<T>().eq(T::getId, id), function);
    }

    @Override
    public <R> List<R> groupOneField(LambdaQueryWrapper<T> wrapper, SFunction<T, R> function) {
        return this.list(wrapper.groupBy(function).select(function)).stream().map(function).collect(Collectors.toList());
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtil.isEmpty(entityList)) return false;
        if (baseMapper instanceof BaseEnhanceMapper) {
            int maxSize = entityList.size();
            int forCount = maxSize / batchSize;
            int affectRow = 0;

            TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
            if (maxSize <= batchSize) affectRow = ((BaseEnhanceMapper<T>) baseMapper).saveOrUpdateBatch(entityList);
            else {
                for (int i = 0; i <= forCount; i++) {
                    List<T> executeList = CollUtil.sub(entityList, i * batchSize, Math.min((i + 1) * batchSize, maxSize));
                    affectRow += ((BaseEnhanceMapper<T>) baseMapper).saveOrUpdateBatch(executeList);
                }
            }

            return affectRow > 0;
        }
        return super.saveOrUpdateBatch(entityList, batchSize);
    }
}
