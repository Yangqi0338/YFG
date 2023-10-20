package com.base.sbc.config.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.CustomStylePicUpload;
import com.base.sbc.config.common.base.UserCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * 款式图工具类，获取款式图（水印），上传接口待提供(2023-7-26 13:56:24)
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月26日
 */

@Component
public class StylePicUtils {

    @Autowired
    CustomStylePicUpload customStylePicUpload;

    public void setStylePic(List list, String fileIdKey) {
        UserCompany userCompany = companyUserInfo.get();
        GroupUser userBy = new GroupUser();
        userBy.setUsername(userCompany.getUserId());
        userBy.setName(userCompany.getAliasUserName());
        setStylePic(userBy, list, fileIdKey);
    }

    /**
     * 设置款式配色图
     * @param userBy
     * @param list
     * @param fileIdKey 修改字段
     */
    public void setStylePic(GroupUser userBy, List list, String fileIdKey) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<String> fileId = new ArrayList<>(12);
        for (Object vo : list) {
            String v = BeanUtil.getProperty(vo, fileIdKey);
            if (StrUtil.isNotBlank(v)) {
                fileId.add(v);
            }
        }
        if (CollUtil.isEmpty(fileId)) {
            return;
        }
        /*获取款式图*/
        for (Object l : list) {
            String v = BeanUtil.getProperty(l, fileIdKey);
            if (StrUtil.isBlank(v)) {
                continue;
            }
            BeanUtil.setProperty(l, fileIdKey, Optional.of(getImgUrl(userBy, v)).orElse(""));
        }
    }


	/**
     * 通过款号获取款式水印图片
     *
     * @return
     */
    public String getImgUrl(GroupUser userBy, String styleNo) {
        // 获取当前用户的工号和姓名
        String badge = userBy.getUsername(); // 当前登录人工号
        String name = userBy.getName();// 当前用户姓名
        // 密钥相关
        String appKey = customStylePicUpload.getKey();
        String salt = customStylePicUpload.getSalt();
        String appSecret = customStylePicUpload.getAppSecret();
        // 加密
        String tiemStr = DateUtils.getDate("yyyyMMddHHmmss");
        String appkeyP = "";
        String badgeP = "";
        String nameP = "";
        try {
            appkeyP = URLEncoder.encode(EncryptUtil.EncryptE2(appKey, salt), "utf-8");
            badgeP = URLEncoder.encode(EncryptUtil.EncryptE2(badge, appSecret), "utf-8");
            nameP = URLEncoder.encode(EncryptUtil.EncryptE2(name, appSecret), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
		}
		String allStr = badge + name + styleNo + tiemStr + appKey + appSecret;
		String allStrP = DigestUtils.md5DigestAsHex(allStr.getBytes());
		String param = "&useraccount=" + badgeP + "&username=" + nameP + "&time=" + tiemStr + "&key=" + appkeyP
				+ "&md5=" + allStrP;
		return customStylePicUpload.getViewUrl() + styleNo + param;
    }
}
