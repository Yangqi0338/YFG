package com.base.sbc.client.portal.bo;

import lombok.Data;

/**
 * @Create : 2024/7/4 18:41
 **/
@Data
public class ResponseResultBo {

    private String msg;

    private Integer code;

    Object data;

}
