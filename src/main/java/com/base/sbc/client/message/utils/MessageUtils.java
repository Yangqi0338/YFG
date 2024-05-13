package com.base.sbc.client.message.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.client.message.service.MessagesService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.BaseGlobal;
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
    public void seatSendMessage(List<String> ids, String teamId, GroupUser groupUser) {
        log.info("————————————————————————商品企划下发消息方法参数" + ids + teamId);
        PlanningCategoryItem planningCategoryItem = planningCategoryItemMapper.selectById(ids.get(0));
        String userId = amcFeignService.getUserGroupUserId(planningCategoryItem.getPlanningSeasonId(), teamId, "M设计总监");
        log.info("————————————————————————商品企划下发消息用户" + userId);
        if (!StringUtils.isBlank(userId)) {
            Map<String, String> map = new HashMap<>();
            map.put("categoryName", planningCategoryItem.getProdCategoryName());
            map.put("count", Integer.toString(ids.size()));
            map.put("userId", groupUser.getId());
            map.put("userName", groupUser.getName());
            map.put("avatar", groupUser.getAvatar());
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
     * @param list
     * @param groupUser
     */
    @Async
    public void seasonSendMessage(List<PlanningCategoryItem> list, GroupUser groupUser) {

        if (!CollectionUtils.isEmpty(list)) {
            for (PlanningCategoryItem planningCategoryItem : list) {
                log.info("————————————————————————产品季下发提醒消息用户" + planningCategoryItem.getDesignerId());
                Map<String, String> map = new HashMap<>();
                map.put("userId", groupUser.getId());
                map.put("userName", groupUser.getName());
                map.put("avatar", groupUser.getAvatar());
                map.put("sampleDesignId", planningCategoryItem.getId());
                ModelMessage modelMessage = new ModelMessage();
                modelMessage.setUserIds(planningCategoryItem.getDesignerId());
                modelMessage.setModelCode("YFG002");
                modelMessage.setParams(map);
                String s = messagesService.sendNoticeByModel(modelMessage);
                log.info("————————————————————————产品季下发提醒消息" + s);
            }
        }
    }


    /**
     * 调样发送提醒
     *
     * @param type   fabric:面料  ingredients：辅料
     * @param status 1 消息 0 通知
     */
    @Async
    public void atactiformSendMessage(String type, String status, GroupUser groupUser) {
        /*面料*/
        Map<String, String> map = new HashMap<>();
        String userId = "";
        ModelMessage modelMessage = new ModelMessage();
        if ("fabric".equals(type)) {
            map.put("title", "面料调样单");
        } else {
            map.put("title", "辅料调样单");
        }
        if (StringUtils.isNotBlank(status) && status.equals(BaseGlobal.YES)) {
            userId = amcFeignService.getUserGroupUserId("", "", "M面辅料专员");
            modelMessage.setModelCode("YFG004");
        } else {
            userId = amcFeignService.getUserGroupUserId("", "", "M设计师");
            modelMessage.setModelCode("YFG005");
        }
        if (StringUtils.isNotBlank(userId)) {
            log.info("————————————————————————发送提醒消息用户" + userId);
            map.put("userId", groupUser.getId());
            map.put("userName", groupUser.getName());
            map.put("avatar", groupUser.getAvatar());
            modelMessage.setUserIds("772096418008530944");
            modelMessage.setParams(map);
            String s = messagesService.sendNoticeByModel(modelMessage);
        }

    }


    /**
     * 下发打版指令
     *
     * @param deptId
     */
    @Async
    public void sampleDesignSendMessage(String deptId, String patternNo, GroupUser groupUser) {

        if (StringUtils.isNotBlank(deptId)) {
            String userId = "";
            List<UserCompany> userCompanies = amcFeignService.getDeptManager(deptId, "1");
            if (CollUtil.isNotEmpty(userCompanies)) {
                userId = userCompanies.stream().map(UserCompany::getUserId).collect(Collectors.joining(StrUtil.COMMA));
            }
            if (StringUtils.isNotBlank(userId)) {
                log.info("————————————————————————下发打版指令发送提醒消息用户" + userId);
                Map<String, String> map = new HashMap<>();
                map.put("patternNo", patternNo);
                map.put("status", "分配版师打版");
                map.put("userId", groupUser.getId());
                map.put("userName", groupUser.getName());
                map.put("avatar", groupUser.getAvatar());
                ModelMessage modelMessage = new ModelMessage();
                modelMessage.setUserIds(userId);
                modelMessage.setModelCode("YFG003");
                modelMessage.setParams(map);
                String s = messagesService.sendNoticeByModel(modelMessage);
            }

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
    public void sampleTaskSendMessage(BaseDataEntity bean, JSONObject config, String status, GroupUser groupUser) {
        /*获取消息配置信息*/
        JSONArray msgList = config.getJSONArray("message");
        if (CollUtil.isNotEmpty(msgList)) {
            for (int i = 0; i < msgList.size(); i++) {
                JSONObject msg = msgList.getJSONObject(i);
                /*节点配置消息是否紧急程度*/
                String urgency = msg.getString("urgency");
                /*单的紧急程度*/
                String urgency1 = BeanUtil.getProperty(bean, "urgency");
                if (StringUtils.isBlank(urgency) || urgency.equals(urgency1)) {
                    String userType = msg.getString("userType");
                    String deptUserType = msg.getString("deptUserType");
                    String val = msg.getString("val");
                    String patternNo = msg.getString("patternNo");
                    String stitcherRemark =  BeanUtil.getProperty(bean, "stitcherRemark");
                    /*发送的用户*/
                    String userId = "";
                    if (StrUtil.equals(userType, "user")) {
                        userId = BeanUtil.getProperty(bean, val);
                    } else if (StrUtil.equals(userType, "deptUserType")) {
                        /*查询样衣组长 或部门主管 */
                        List<UserCompany> userCompanies = amcFeignService.getDeptManager(BeanUtil.getProperty(bean, val), deptUserType);
                        if (CollUtil.isNotEmpty(userCompanies)) {
                            userId = userCompanies.stream().map(UserCompany::getUserId).collect(Collectors.joining(StrUtil.COMMA));
                        }
                    } else if (StrUtil.equals(userType, "technology")) {
                        /*工艺员*/
                        userId = amcFeignService.getUserGroupUserId(BeanUtil.getProperty(bean, "planningSeasonId"), "", "M工艺员");
                    } else if (StrUtil.equals(userType, "designer")) {
                        /*设计师*/
                        userId = amcFeignService.getUserGroupUserId(BeanUtil.getProperty(bean, "planningSeasonId"), "", "M设计师");
                    }
                    /*消息提醒*/
                    log.info("————————————————————————样衣任务提醒消息用户" + userId);
                    if (StringUtils.isNotBlank(userId)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("patternNo", BeanUtil.getProperty(bean, patternNo));
                        if(StrUtil.isNotBlank(stitcherRemark)){
                            map.put("stitcherRemark","备注："+ stitcherRemark);
                        }else {
                            map.put("stitcherRemark", "_");
                        }
                        map.put("status", status);
                        map.put("userId", groupUser.getId());
                        map.put("userName", groupUser.getName());
                        map.put("avatar", groupUser.getAvatar());
                        String node = BeanUtil.getProperty(bean, "node");
                        /*判断阶段*/
                        if ("打版任务".equals(node)) {
                            /*是否是黑蛋*/
                            if (urgency1.equals(BaseGlobal.NO)) {
                                /*黑蛋打板*/
                                map.put("address", "/patternMaking/blackTask/blackPatternMakingTask");
                            } else {
                                map.put("address", "/patternMaking/patternMakingTask");
                            }
                        } else {
                            /*是否是黑蛋*/
                            if (urgency1.equals(BaseGlobal.NO)) {
                                /*黑蛋打板*/
                                map.put("address", "/patternMaking/blackTask/blackSampleClothesTask");
                            } else {
                                map.put("address", "/patternMaking/sampleClothesTask");
                            }
                        }
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


    /**
     * 确认收到样衣消息提醒
     *
     * @param patternRoomId
     * @param designNo
     * @param groupUser
     */
    @Async
    public void receiveSampleSendMessage(String patternRoomId, String designNo, GroupUser groupUser) {

        if (StringUtils.isNotBlank(patternRoomId)) {
            String userId = "";
            List<UserCompany> userCompanies = amcFeignService.getDeptManager(patternRoomId, "2");
            if (CollUtil.isNotEmpty(userCompanies)) {
                userId = userCompanies.stream().map(UserCompany::getUserId).collect(Collectors.joining(StrUtil.COMMA));
            }
            if (StringUtils.isNotBlank(userId)) {
                log.info("————————————————————————确认收到样衣消息提醒发送提醒消息用户" + userId);
                Map<String, String> map = new HashMap<>();
                map.put("title", designNo);
                map.put("userId", groupUser.getId());
                map.put("userName", groupUser.getName());
                map.put("avatar", groupUser.getAvatar());
                ModelMessage modelMessage = new ModelMessage();
                modelMessage.setUserIds(userId);
                modelMessage.setModelCode("YFG006");
                modelMessage.setParams(map);
                String s = messagesService.sendNoticeByModel(modelMessage);
            }

        }
    }


    /**
     * 转大货消息提醒
     * 发送两条消息 工艺经理，计控
     *
     * @param patternRoomId
     * @param designNo
     * @param groupUser
     */
    @Async
    public void toBigGoodsSendMessage(String patternRoomId, String designNo, GroupUser groupUser) {
        if (StringUtils.isNotBlank(patternRoomId)) {
            for (int i = 0; i < 2; i++) {
                String userId = "";
                Map<String, String> map = new HashMap<>();
                //            先发送
                if (i == 0) {
                    /*工艺经理*/
                    userId = amcFeignService.getUserGroupUserId(patternRoomId, "", "M工艺经理");
                    map.put("title", "。");
                } else if (i == 1) {
                    /*计控*/
                    userId = amcFeignService.getUserGroupUserId(patternRoomId, "", "M计控");
                    map.put("title", "，可以做产前样");
                }
                if (StringUtils.isNotBlank(userId)) {
                    log.info("————————————————————————确认收到样衣消息提醒发送提醒消息用户" + userId);
                    map.put("designNo", designNo);
                    map.put("userId", groupUser.getId());
                    map.put("userName", groupUser.getName());
                    map.put("avatar", groupUser.getAvatar());
                    ModelMessage modelMessage = new ModelMessage();
                    modelMessage.setUserIds(userId);
                    modelMessage.setModelCode("YFG007");
                    modelMessage.setParams(map);
                    String s = messagesService.sendNoticeByModel(modelMessage);
                }
            }
        }
    }


    /**
     * 确认收到样衣消息提醒
     *
     * @param userId
     * @param designNo
     * @param groupUser
     */
    @Async
    public void prmSendMessage(String userId, String designNo, GroupUser groupUser) {
        if (StringUtils.isNotBlank(userId)) {
            log.info("————————————————————————确认收到样衣消息提醒发送提醒消息用户" + userId);
            Map<String, String> map = new HashMap<>();
            map.put("title", designNo);
            map.put("userId", groupUser.getId());
            map.put("userName", groupUser.getName());
            map.put("avatar", groupUser.getAvatar());
            ModelMessage modelMessage = new ModelMessage();
            modelMessage.setUserIds(userId);
            modelMessage.setModelCode("YFG008");
            modelMessage.setParams(map);
            String s = messagesService.sendNoticeByModel(modelMessage);
        }
    }

    /**
     * 款式定价消息通知
     * @param role
     * @param designNo
     * @param seasonId
     * @param stage
     * @param groupUser
     */
    @Async
    public void stylePricingSendMessage(String role,String designNo,String seasonId,String stage, GroupUser groupUser) {
        if (StringUtils.isNotBlank(role)) {
            String userId = amcFeignService.getUserGroupUserId(seasonId, "", role);
            if (StringUtils.isNotBlank(userId)) {
                log.info("————————————————————————款式定价消息提醒发送提醒消息用户" + userId);
                String title = "";
                if (StringUtils.equals(stage, BaseGlobal.STATUS_CLOSE)) {
                    title = designNo + "计控成本已确定";
                }
                Map<String, String> map = new HashMap<>();
                map.put("title", title);
                map.put("userId", groupUser.getId());
                map.put("userName", groupUser.getName());
                map.put("avatar", groupUser.getAvatar());
                ModelMessage modelMessage = new ModelMessage();
                modelMessage.setUserIds(userId);
                modelMessage.setModelCode("YFG009");
                modelMessage.setParams(map);
                String s = messagesService.sendNoticeByModel(modelMessage);
            }
        }
    }

    /**
     *发送消息
     * @param role
     * @param title
     * @param address
     * @param seasonId
     * @param groupUser
     */
    @Async
    public void sendMessage(String role,String userId,String title,String address,String seasonId, GroupUser groupUser) {
        /*获取产品季下的用户组的人员*/
        if (StringUtils.isNotBlank(role)) {
            userId = amcFeignService.getUserGroupUserId(seasonId, "", role);
        }
        if (StringUtils.isNotBlank(userId)) {
            log.info("————————————————————————发送消息消息提醒发送提醒消息用户" + userId);
            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("userId", groupUser.getId());
            map.put("userName", groupUser.getName());
            map.put("avatar", groupUser.getAvatar());
            map.put("address", address);
            ModelMessage modelMessage = new ModelMessage();
            modelMessage.setUserIds(userId);
            modelMessage.setModelCode("YFG010");
            modelMessage.setParams(map);
            String s = messagesService.sendNoticeByModel(modelMessage);
        }
    }

    /**
     * 发送普通消息
     *
     * @param userId    用户id
     * @param title     标题
     * @param address   地址
     * @param groupUser 用户信息
     */
    public void sendCommonMessage(String userId, String title, String address, GroupUser groupUser) {
        if (StringUtils.isNotBlank(userId)) {
            log.info("————————————————————————发送消息消息提醒发送提醒消息用户" + userId);
            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("userId", groupUser.getId());
            map.put("userName", groupUser.getName());
            map.put("avatar", groupUser.getAvatar());
            map.put("address", address);
            ModelMessage modelMessage = new ModelMessage();
            modelMessage.setUserIds(userId);
            //通用模板
            modelMessage.setModelCode("SJ9113");
            modelMessage.setParams(map);
            String s = messagesService.sendNoticeByModel(modelMessage);
        }
    }

}
