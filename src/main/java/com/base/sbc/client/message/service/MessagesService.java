package com.base.sbc.client.message.service;

import com.base.sbc.client.message.entity.ModelMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Youkehai
 * @data 创建时间:2021/6/28
 */
@FeignClient(name = "BASE-MESSAGE", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
public interface MessagesService {


	/**
	 * 通过模板发送通知
	 * @param message 模板
	 * @return
	 */
	@PostMapping("/mmc/api/token/sendNotices/noticeOrMessageByModel")
	public String sendNoticeByModel(@RequestBody ModelMessage message);
}
