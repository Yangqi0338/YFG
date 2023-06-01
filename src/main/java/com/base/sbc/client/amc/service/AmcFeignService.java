package com.base.sbc.client.amc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.amc.TeamVo;
import com.base.sbc.client.amc.entity.CompanyPost;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述： 用户信息
 * @address com.base.sbc.client.amc.service.AmcFeignService
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-12 13:17
 * @version 1.0
 */
@Service
@Slf4j
public class AmcFeignService {

    @Resource
    private AmcService amcService;

    /**
     * 获取用户头像
     * @param ids
     * @return
     */
    public Map<String,String> getUserAvatar(String ids){
        Map<String,String> userAvatarMap=new HashMap<>(16);
        String userAvatarStr = amcService.getUserAvatar(ids);
        JSONObject jsonObject = JSON.parseObject(userAvatarStr);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if(CollUtil.isNotEmpty(userCompanies)){
                for (UserCompany userCompany : userCompanies) {
                    userAvatarMap.put(userCompany.getId(),userCompany.getAvatar());
                }
            }
        }
        return userAvatarMap;
    }

    public UserCompany getUserInfo(String userId){
        String responseStr = amcService.getCompanyUserInfoByUserIds(userId);
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if(CollUtil.isNotEmpty(userCompanies)){
                 return CollUtil.getLast(userCompanies);
            }
        }
        return null;
    }
    /**
     * 获取团队信息
     * @param seasonId 产品季节id
     * @return
     */
    public List<TeamVo> getTeamBySeasonId(String seasonId){
        try {
            String str = amcService.getTeamBySeasonId(seasonId);
            JSONObject jsonObject = JSON.parseObject(str);
            if(jsonObject.getBoolean("success")){
              return  jsonObject.getJSONArray("data").toJavaList(TeamVo.class);
            }
        } catch (Exception e) {
            log.error("获取产品季团队异常",e);
        }
        return null;
    }

    /**
     * 添加头像
     * @param arr
     * @param userIdKey
     * @param avatarKey
     */
    public void addUserAvatarToList(List arr,String userIdKey,String avatarKey){
        try {
            if(CollUtil.isEmpty(arr)){
                return;
            }
            Set<String> userIds=new HashSet<>(arr.size());
            for (Object o : arr) {
                Object property = BeanUtil.getProperty(o, userIdKey);
                if(ObjUtil.isNotNull(property)){
                    userIds.add(property.toString());
                }
            }
            if(CollUtil.isEmpty(userIds)){
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

    public List<UserCompany> getUsersBySeasonId(String planningSeasonId, String dpj) {
        List<UserCompany> userList = null;
        try {
            String result = amcService.getUsersBySeasonId(planningSeasonId, dpj);
            JSONObject jsonObject = JSON.parseObject(result);
            userList = jsonObject.getJSONArray("data").toJavaList(UserCompany.class);
        } catch (Exception e) {
            e.printStackTrace();
            userList = new ArrayList<>(2);
        }
        return userList;
    }

    public List<UserCompany> getTeamUserListByPost(String planningSeasonId, String post) {
        List<UserCompany> userList = getUsersBySeasonId(planningSeasonId, post);
        if (CollUtil.isNotEmpty(userList)) {
            List<UserCompany> collect = userList.stream().filter(user -> {
                CompanyPost one = CollUtil.findOne(user.getPostList(), a -> StrUtil.equals(post, a.getName()));
                return one != null;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
}
