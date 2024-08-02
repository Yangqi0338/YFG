package com.base.sbc.config.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.CustomStylePicUpload;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.vo.AttachmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    AttachmentService attachmentService;

    public GroupUser getGroupUser() {
        UserCompany userCompany = companyUserInfo.get();
        GroupUser userBy = new GroupUser();
        userBy.setUsername(userCompany.getUserId());
        userBy.setName(userCompany.getAliasUserName());
        return userBy;
    }


    /**
     * 获取款式设计图片的url
     *
     * @param stylePic
     * @return
     */
    public String getStyleUrl(String stylePic) {
        if (customStylePicUpload.isOpen()) {
            return getStyleColorUrl2(stylePic,100);
        }
        AttachmentVo attachmentVo = attachmentService.getAttachmentByFileId(stylePic);
        return Optional.ofNullable(attachmentVo).map(v -> v.getUrl()).orElse(null);
    }

    /**
     * 款式设计图
     *
     * @param list
     * @param fileIdKey
     */
    public void setStylePic(List list, String fileIdKey) {
        if (customStylePicUpload.isOpen()) {
            setStyleColorPic2(list, fileIdKey);
        } else {
            attachmentService.setListStylePic(list, fileIdKey);
        }
    }

    /**
     * 款式设计图
     * @param list
     * @param fileIdKey 修改字段
     * @param lossnum 清晰度
     */
    public void setStylePic(List list, String fileIdKey,Integer lossnum) {
        if (customStylePicUpload.isOpen()) {
            setStyleColorPic2(list, fileIdKey,lossnum);
        } else {
            attachmentService.setListStylePic(list, fileIdKey);
        }
    }

    /**
     * 获取大货款号的图片
     * @param list
     * @param fileIdKey 修改字段
     * @param lossnum 清晰度
     */
    public void setStyleColorPic2(List list, String fileIdKey,Integer lossnum) {
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
            BeanUtil.setProperty(l, fileIdKey, Optional.of(getStyleColorUrl2(v,lossnum)).orElse(""));
        }
    }

    /**
     * 获取大货款号的图片
     *
     * @param list
     * @param fileIdKey 修改字段
     */
    public List<Object> setStyleColorPic2(List list, String fileIdKey) {
        if (CollUtil.isEmpty(list)) {
            return list;
        }
        List<String> fileId = new ArrayList<>(12);
        for (Object vo : list) {
            String v = BeanUtil.getProperty(vo, fileIdKey);
            if (StrUtil.isNotBlank(v)) {
                fileId.add(v);
            }
        }
        if (CollUtil.isEmpty(fileId)) {
            return list;
        }
        /*获取款式图*/
        List<Object> result = new ArrayList<>();
        for (Object l : list) {
            String v = BeanUtil.getProperty(l, fileIdKey);
            if (StrUtil.isBlank(v)) {
                result.add(l);
                continue;
            }
            String url = Optional.of(getStyleColorUrl2(v, 100)).orElse("");
            BeanUtil.setProperty(l, fileIdKey, url);
            if (StrUtil.isBlank(v)) {
                result.add(l);
            }
        }
        return result;
    }

    public void setStyleColorPic2(List list) {
        setStyleColorPic2(setStyleColorPic2(list, "styleColorPic"), "stylePic");
    }


    /**
     * 获取大货款号的url
     * @param fileName
     * @param lossnum
     * @return
     */
    public String getStyleColorUrl2(String fileName,Integer lossnum) {
        if (StrUtil.isBlank(fileName)) {
            return null;
        }
        GroupUser userBy = getGroupUser();
        // 获取当前用户的工号和姓名
        String badge = userBy.getUsername();
        String name = userBy.getName();
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
        String allStr = badge + name + fileName + tiemStr + appKey + appSecret;
        String allStrP = DigestUtils.md5DigestAsHex(allStr.getBytes());
		String param = "&useraccount=" + badgeP + "&username=" + nameP + "&time=" + tiemStr + "&key=" + appkeyP
				+ "&md5=" + allStrP+"&lossnum="+lossnum;
        return customStylePicUpload.getViewUrl() + fileName + param;
    }
}
