package com.base.sbc.client.message.service;

import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.config.common.ApiResult;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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


	/**
	 * 通过模板发送通知 新
	 * @param message 模板
	 * @return
	 */
	@PostMapping("/mmc/api/open/msg/noticeOrMessageByModel")
	public String noticeOrMessageByModel(@RequestBody ModelMessage message);

	/**
	 * 定时截至时间判断
	 * @param code 触发配置code
	 * @return
	 */
	@GetMapping("/mmc/api/open/msg/timingEndTime")
	ApiResult<Boolean> timingEndTime(@RequestParam("code") String code);
}
