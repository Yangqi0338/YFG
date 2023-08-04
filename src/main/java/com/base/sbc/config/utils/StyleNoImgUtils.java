package com.base.sbc.config.utils;

import com.base.sbc.client.oauth.entity.GroupUser;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 
 * 款式图工具类，获取款式图（水印），上传接口待提供(2023-7-26 13:56:24)
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月26日
 */
public class StyleNoImgUtils {
	/*访问地址*/
	public static final String IMG_BASE_URL = "http://img.eifini.com/image/index?goodsno=";
	/**
	 * 通过款号获取款式水印图片
	 * 
	 * @return
	 */
	public static String getStyleNoImgUrl(GroupUser userBy ,String styleNo) {
		// 获取当前用户的工号和姓名
		String badge = userBy.getUsername(); // 当前登录人工号
		String name = userBy.getName();// 当前用户姓名
		// 密钥相关
		String appKey = "PDMImage";
		String salt = "eifiniEMS202307";
		String appSecret = "925091ef18f40b662a55c058cb475137";
		// 加密
		String tiemStr = DateUtils.getDate("yyyyMMddHHmmss");
		String appkeyP = "";
		String badgeP = "";
		String nameP = "";
		try {
			appkeyP = URLEncoder.encode(EncryptUtil.EncryptE2(appKey, salt), "utf-8");
			badgeP = URLEncoder.encode(EncryptUtil.EncryptE2(badge, appSecret), "utf-8");
			nameP = URLEncoder.encode(EncryptUtil.EncryptE2(name, appSecret), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String allStr = badge + name + styleNo + tiemStr + appKey + appSecret;
		String allStrP = DigestUtils.md5DigestAsHex(allStr.getBytes());
		String param = "&useraccount=" + badgeP + "&username=" + nameP + "&time=" + tiemStr + "&key=" + appkeyP
				+ "&md5=" + allStrP;
		return IMG_BASE_URL + styleNo + param;
	}
}
