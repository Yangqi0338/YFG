package com.base.sbc.client.amc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.TeamVo;
import com.base.sbc.client.amc.entity.Team;
import com.base.sbc.client.amc.entity.UserDept;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述： 用户信息
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.client.amc.service.AmcFeignService
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-12 13:17
 */
@Service
@Slf4j
public class AmcFeignService {

    @Resource
    private AmcService amcService;

    public static ThreadLocal<List<String>> userPlanningSeasonId = new ThreadLocal<>();

    /**
     * 获取用户头像
     *
     * @param ids
     * @return
     */
    public Map<String, String> getUserAvatar(String ids) {
        Map<String, String> userAvatarMap = new HashMap<>(16);
        String userAvatarStr = amcService.getUserAvatar(ids);
        JSONObject jsonObject = JSON.parseObject(userAvatarStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if (CollUtil.isNotEmpty(userCompanies)) {
                for (UserCompany userCompany : userCompanies) {
                    userAvatarMap.put(userCompany.getUserId(), Opt.ofBlankAble(userCompany.getAvatar()).orElse(userCompany.getAliasUserAvatar()));
                }
            }
        }
        return userAvatarMap;
    }

    public UserCompany getUserInfo(String userId) {
        return getUserInfo(userId, null);
    }

    /**
     * @param userId
     * @param dpj    非空 查询部门岗位角色
     * @return
     */
    public UserCompany getUserInfo(String userId, String dpj) {
        String responseStr = amcService.getCompanyUserInfoByUserIds(userId, dpj);
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if (CollUtil.isNotEmpty(userCompanies)) {
                return CollUtil.getLast(userCompanies);
            }
        }
        return null;
    }

    public List<UserCompany> getDeptManager(String deptId, String userType) {
        String responseStr = amcService.getDeptManager(deptId, userType);
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            return userCompanies;
        }
        return null;
    }
    public List<UserCompany> getUserCodeNotNullUserList() {
        String responseStr = amcService.getUserCodeNotNullUserList();
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            return userCompanies;
        }
        return null;
    }

    public boolean isSampleTeamLeader(String deptId, String userId) {
        List<UserCompany> deptManager = getDeptManager(deptId, "2");
        if (CollUtil.isNotEmpty(deptManager)) {
            for (UserCompany userCompany : deptManager) {
                if (StrUtil.equals(userCompany.getUserId(), userId)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取团队信息
     *
     * @param seasonId 产品季节id
     * @return
     */
    public List<TeamVo> getTeamBySeasonId(String seasonId) {
        try {
            String str = amcService.getTeamBySeasonId(seasonId);
            JSONObject jsonObject = JSON.parseObject(str);
            if (jsonObject.getBoolean("success")) {
                return jsonObject.getJSONArray("data").toJavaList(TeamVo.class);
            }
        } catch (Exception e) {
            log.error("获取产品季团队异常", e);
        }
        return null;
    }

    /**
     * 添加头像
     *
     * @param arr
     * @param userIdKey
     * @param avatarKey
     */
    public void addUserAvatarToList(List arr, String userIdKey, String avatarKey) {
        try {
            if (CollUtil.isEmpty(arr)) {
                return;
            }
            Set<String> userIds = new HashSet<>(arr.size());
            for (Object o : arr) {
                Object property = BeanUtil.getProperty(o, userIdKey);
                if (ObjUtil.isNotNull(property)) {
                    userIds.add(property.toString());
                }
            }
            if (CollUtil.isEmpty(userIds)) {
                return;
            }
            Map<String, String> userAvatar = getUserAvatar(CollUtil.join(userIds, StrUtil.COMMA));
            for (Object o : arr) {
                Object val = MapUtil.getStr(userAvatar, BeanUtil.getProperty(o, userIdKey), null);
                BeanUtil.setProperty(o, avatarKey, val);
            }
        } catch (Exception e) {
            log.error("获取头像失败", e);
        }

    }

    public List<UserCompany> getUsersBySeasonId(String planningSeasonId, String dpj, String post) {
        List<UserCompany> userList = null;
        try {
            String result = amcService.getUsersBySeasonId(planningSeasonId, dpj, post);
            JSONObject jsonObject = JSON.parseObject(result);
            userList = jsonObject.getJSONArray("data").toJavaList(UserCompany.class);
        } catch (Exception e) {
            e.printStackTrace();
            userList = new ArrayList<>(2);
        }
        return userList;
    }

    public List<UserCompany> getTeamUserListByPost(String planningSeasonId, String post) {
        List<UserCompany> userList = getUsersBySeasonId(planningSeasonId, BaseGlobal.YES, post);
        return userList;
    }

    public List<String> getPlanningSeasonIdByUserId(String userId) {
        List<String> userList = null;
        try {
            List<String> cacheIds = userPlanningSeasonId.get();
            if (CollUtil.isNotEmpty(cacheIds)) {
                return cacheIds;
            }
            String result = amcService.getPlanningSeasonIdByUserId(userId);
            JSONObject jsonObject = JSON.parseObject(result);
            userList = jsonObject.getJSONArray("data").toJavaList(String.class);
            userPlanningSeasonId.set(userList);
        } catch (Exception e) {
            e.printStackTrace();
            userList = new ArrayList<>(2);
        }finally {
            userPlanningSeasonId.remove();
        }
        return userList;

    }

    /**
     * 设置权限
     * 查询userId 所在的产品季id ，设置条件
     *
     * @param qw     QueryWrapper 条件构造器
     * @param column 产品季列名
     * @param userId 用户id
     */
    public void teamAuth(QueryWrapper qw, String column, String userId) {
//        List<String> planningSeasonIdByUserId = getPlanningSeasonIdByUserId(userId);
//        if (CollUtil.isEmpty(planningSeasonIdByUserId)) {
//            throw new OtherException("您不在团队里面");
//        }
//        qw.in(column, planningSeasonIdByUserId);
    }

    /**
     * 设置头像
     * 1先获取有UserAvatar注解的字段，拿到用户id属性，头像属性
     * 2 去amc 查
     * 3 设置头像
     *
     * @param obj
     */
    public void setUserAvatarToObj(Object obj) {
        try {
            Map<String, String> avatarUserIdKey = getUserAvatarMap(obj);
            setUserAvatarToObj(obj, avatarUserIdKey);
        } catch (Exception e) {
            log.error("获取头像失败", e);
        }
    }


    /**
     * 设置头像
     *
     * @param obj
     * @param avatarUserIdKey key 用户id 属性 ,val 头像属性
     */
    public void setUserAvatarToObj(Object obj, Map<String, String> avatarUserIdKey) {
        try {
            if (MapUtil.isEmpty(avatarUserIdKey)) {
                return;
            }
            List<String> userIds = new ArrayList<>(16);
            //获取用户id
            for (String s : avatarUserIdKey.values()) {
                String userId = BeanUtil.getProperty(obj, s);
                if (StrUtil.isNotBlank(userId)) {
                    userIds.add(userId);
                }
            }
            if (CollUtil.isEmpty(userIds)) {
                return;
            }
            // 查询头像
            Map<String, String> userAvatar = getUserAvatar(CollUtil.join(userIds, StrUtil.COMMA));
            // 设置头像值
            for (Map.Entry<String, String> kv : avatarUserIdKey.entrySet()) {
                BeanUtil.setProperty(obj, kv.getKey(), userAvatar.get(BeanUtil.getProperty(obj, kv.getValue())));
            }
        } catch (Exception e) {
            log.error("获取头像失败", e);
        }
    }

    public void setUserAvatarToList(List list, Map<String, String> avatarUserIdKey) {
        // 获取id
        if (MapUtil.isEmpty(avatarUserIdKey)) {
            return;
        }
        List<String> userIds = new ArrayList<>(16);
        //获取用户id
        for (Object obj : list) {
            for (String s : avatarUserIdKey.values()) {
                String userId = BeanUtil.getProperty(obj, s);
                if (StrUtil.isNotBlank(userId)) {
                    userIds.add(userId);
                }
            }

        }
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        // 查询头像
        Map<String, String> userAvatar = getUserAvatar(CollUtil.join(userIds, StrUtil.COMMA));
        for (Object obj : list) {
            if (obj == null) {
                continue;
            }
            for (Map.Entry<String, String> kv : avatarUserIdKey.entrySet()) {
                BeanUtil.setProperty(obj, kv.getKey(), userAvatar.get(BeanUtil.getProperty(obj, kv.getValue())));
            }
        }
    }

    public void setUserAvatarToList(List list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Object obj = list.get(0);
        Map<String, String> avatarUserIdKey = getUserAvatarMap(obj);
        setUserAvatarToList(list, avatarUserIdKey);
    }


    private static Map<String, String> getUserAvatarMap(Object obj) {
        Map<String, String> avatarUserIdKey = new HashMap<>(16);
        Field[] fields = obj.getClass().getDeclaredFields();
        // 遍历字段
        for (Field field : fields) {
            // 检查字段上是否存在UserAvatar的注解
            if (field.isAnnotationPresent(UserAvatar.class)) {
                // 获取字段上的注解对象
                UserAvatar annotation = field.getAnnotation(UserAvatar.class);
                avatarUserIdKey.put(field.getName(), annotation.value());
            }
        }
        return avatarUserIdKey;
    }

    /**
     * 方法描述： 获取团队下的用户组用户
     *
     * @param seasonId  产品季id
     * @param teamId    团队id
     * @param groupName 用户组名称
     * @return
     */
    public String getUserGroupUserId(String seasonId, String teamId, String groupName) {
        try {
            String str = amcService.getUserGroupUserId(seasonId, teamId, groupName);
            JSONObject jsonObject = JSON.parseObject(str);
            if (jsonObject.getBoolean("success")) {
                return ObjectUtils.isEmpty(jsonObject.get("data")) ? "" : jsonObject.get("data").toString();
            }
        } catch (Exception e) {
            log.error("获取团队下的用户组用户异常", e);
        }
        return null;
    }

    public void teamSave(String seasonId, String teamName) {
        log.info("AmcFeignService#teamSave 团队保存，seasonId:{}. teamName:{}", seasonId, teamName);
        Team team = new Team();
        team.setSeasonId(seasonId);
        team.setName(teamName);
        ApiResult apiResult = amcService.teamSave(team);
        log.info("AmcFeignService#teamSave 团队保存返回结果，seasonId:{}. teamName:{},apiResult:{}", seasonId, teamName, JSON.toJSONString(apiResult));
    }

    /**
     * 产品季默认分配团队
     * @param seasonId
     */
    public void seasonSaveDefaultTeam(String seasonId) {
        log.info("AmcFeignService#seasonSaveDefaultTeam 产品季默认分配团队，seasonId:{}", seasonId);
        ApiResult apiResult = amcService.seasonSaveDefaultTeam(seasonId);
        log.info("AmcFeignService#seasonSaveDefaultTeam 产品季默认分配团队返回结果，seasonId:{},apiResult:{}", seasonId, JSON.toJSONString(apiResult));
    }


    public UserCompany getUserByUserId(String userId) {
        UserCompany userList = null;
        try {
            String result = amcService.getUserByUserId(userId);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject data = jsonObject.getJSONObject(BaseConstant.DATA);
            userList = data.toJavaObject(UserCompany.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<UserCompany> getUserByUserIds(String userId) {
        List<UserCompany> userList = null;
        try {
            String result = amcService.getUserByUserIds(userId);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            userList = data.toJavaList(UserCompany.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * 获取当官的 如样衣组长2 部门主管 1
     *
     * @param userId
     * @param userType
     * @return
     */
    public List<String> getUserDepByUserIdUserType(String userId, String userType) {

        try {
            String result = amcService.getUserDeptInfoById(userId, userType);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserDept> userList = data.toJavaList(UserDept.class);
            if (CollUtil.isNotEmpty(userList)) {
                return userList.stream().filter(item -> StrUtil.equals(userType, item.getUserType()))
                        .map(item -> item.getDeptId()).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户头像及工号
     *
     * @param ids
     * @return
     */
    public Map<String, UserCompany> getUserAvatarAndUserName(String ids) {
        Map<String, UserCompany> userAvatarMap = new HashMap<>(16);
        String userAvatarStr = amcService.getUserAvatar(ids);
        JSONObject jsonObject = JSON.parseObject(userAvatarStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if (CollUtil.isNotEmpty(userCompanies)) {
                for (UserCompany userCompany : userCompanies) {
                    UserCompany userCompany1 = new UserCompany();
                    userCompany1.setAliasUserAvatar(Opt.ofBlankAble(userCompany.getAvatar()).orElse(userCompany.getAliasUserAvatar()));
                    userCompany1.setUsername(userCompany.getUsername());
                    userAvatarMap.put(userCompany.getUserId(), userCompany1);
                }
            }
        }
        return userAvatarMap;
    }

    public Map<String,UserCompany> getAllUserDeptByType(String userType) {
        String responseStr = amcService.getAllUserDeptByType(userType);
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONObject data = jsonObject.getJSONObject(BaseConstant.DATA);
            Map<String,UserCompany> userList = new HashMap<>();
            for (String key : data.keySet()) {
                userList.put(key, data.getObject(key, UserCompany.class));
            }
            return userList;
        }
        return new HashMap<>();
    }

}
