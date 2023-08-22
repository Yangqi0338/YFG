package com.base.sbc.client.message.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.client.message.service.MessagesService;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.mapper.PlanningCategoryItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/***
 * 向消息服务器发送消息的工具类
 * @author youkehai
 *
 */
@Component
public class MessageUtils {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private PlanningCategoryItemMapper planningCategoryItemMapper;

    @Autowired
    private AmcFeignService amcFeignService;

    @Autowired
    private BaseController baseController;

    /**
     * 给需要审核的人发送信息
     *
     * @param msg
     */
    public void sendAuditMessage(ModelMessage msg) {
        msg.setModelCode("SJ502");
        messagesService.sendNoticeByModel(msg);
    }

    /**
     * 发送模板信息
     *
     * @param msg
     */
    public void sendMessage(ModelMessage msg) {
        String s = messagesService.sendNoticeByModel(msg);
        System.out.println(s);
    }


    /**
     * 商品企划下发消息
     *
     * @param ids    id
     * @param teamId 团队id （不传查产品季使用团队）
     */
    @Async
    public void seatSendMessage(List<String> ids, String teamId) {
        log.info("————————————————————————商品企划下发消息方法参数" + ids + teamId);
        PlanningCategoryItem planningCategoryItem = planningCategoryItemMapper.selectById(ids.get(0));
        String userId = amcFeignService.getUserGroupUserId(planningCategoryItem.getPlanningSeasonId(), teamId, "M设计总监");
        log.info("————————————————————————商品企划下发消息用户" + userId);
        if (!StringUtils.isBlank(userId)) {
            Map<String, String> map = new HashMap<>();
            map.put("categoryName", planningCategoryItem.getProdCategoryName());
            map.put("count", Integer.toString(ids.size()));
            map.put("userId", baseController.getUserId());
            map.put("userName", baseController.getUser().getName());
            map.put("avatar", baseController.getUser().getAvatar());
            ModelMessage modelMessage = new ModelMessage();
            modelMessage.setUserIds(userId);
            modelMessage.setModelCode("YFG001");
            modelMessage.setParams(map);
            ids.forEach(i -> {
                String s = messagesService.sendNoticeByModel(modelMessage);
                log.info("————————————————————————商品企划下发消息" + s);
            });
        }
    }

    /**
     * 产品季下发提醒
     *
     * @param userId 用户id
     */
    @Async
    public void seasonSendMessage(List<String> userId) {
        log.info("————————————————————————产品季下发提醒消息用户" + userId);
        if (!CollectionUtils.isEmpty(userId)) {
            Map<String, String> map = new HashMap<>();
            map.put("userId", baseController.getUserId());
            map.put("userName", baseController.getUser().getName());
            map.put("avatar", baseController.getUser().getAvatar());
            ModelMessage modelMessage = new ModelMessage();
            modelMessage.setUserIds(StringUtils.convertListToString(userId));
            modelMessage.setModelCode("YFG002");
            modelMessage.setParams(map);
            String s = messagesService.sendNoticeByModel(modelMessage);
            log.info("————————————————————————产品季下发提醒消息" + s);
        }
    }


    /**
     * 样衣任务在状态提醒
     * 从节点配置里获取当前节点要发送的用户及节点信息
     *
     * @param bean   BaseDataEntity
     * @param config 流程配置的json
     * @param status 下一步的状态
     */
    @Async
    public void sampleTaskSendMessage(BaseDataEntity bean, JSONObject config, String status) {
        JSONArray msgList = config.getJSONArray("message");
        if (CollUtil.isNotEmpty(msgList)) {
            for (int i = 0; i < msgList.size(); i++) {
                JSONObject msg = msgList.getJSONObject(i);
                String userType = msg.getString("userType");
                String deptUserType = msg.getString("deptUserType");
                String val = msg.getString("val");
                String patternNo = msg.getString("patternNo");
                /*发送的用户*/
                String userId = "";
                if (StrUtil.equals(userType, "user")) {
                    userId = BeanUtil.getProperty(bean, val);
                } else if (StrUtil.equals(userType, "deptUserType")) {
                    List<UserCompany> userCompanies = amcFeignService.getDeptManager(BeanUtil.getProperty(bean, val), deptUserType);
                    if (CollUtil.isNotEmpty(userCompanies)) {
                        userId = userCompanies.stream().map(UserCompany::getUserId).collect(Collectors.joining(StrUtil.COMMA));
                    }
                }
                /*消息提醒*/
                log.info("————————————————————————样衣任务提醒消息用户" + userId);
                if (StringUtils.isNotBlank(userId)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("patternNo", BeanUtil.getProperty(bean, patternNo));
                    map.put("status", status);
                    map.put("userId", baseController.getUserId());
                    map.put("userName", baseController.getUser().getName());
                    map.put("avatar", baseController.getUser().getAvatar());
                    ModelMessage modelMessage = new ModelMessage();
                    modelMessage.setUserIds(userId);
                    modelMessage.setModelCode("YFG003");
                    modelMessage.setParams(map);
                    String s = messagesService.sendNoticeByModel(modelMessage);
                    log.info("————————————————————————样衣任务提醒消息" + s);
                }
            }
        }
    }
}
