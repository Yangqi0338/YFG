package com.base.sbc.client.tas.service;

import com.base.sbc.module.purchase.dto.LinkMorePurchaseOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author xiong
 *
 */
@FeignClient(name = "tas", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
//@FeignClient(name = "tas", url = "http://localhost:10601/", decode404 = true)
public interface TasService {
	/**
	 * 推送采购单——》领猫
	 * @return
	 */
	@PostMapping("/tas/api/saas/linkMore/pushPurchaseOrder")
//	@PostMapping("/api/saas/linkMore/pushPurchaseOrder")
	String pushPurchaseOrder(@RequestBody LinkMorePurchaseOrderDto purchaseOrderDto);

}
