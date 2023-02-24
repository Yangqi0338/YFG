package com.base.sbc.config.enums;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
public enum BaseDataValueEnum {
	/**暂不在库*/
	PRODUCT_NOT_ADDRESS("PRODUCT_NOT_ADDRESS");
	private String dataMessage;

	private BaseDataValueEnum(String dataMessage) {
		this.dataMessage = dataMessage;
	}
	public String getDataMessage() {
		return dataMessage;
	}

	public void setDataMessage(String dataMessage) {
		this.dataMessage = dataMessage;
	}
}
