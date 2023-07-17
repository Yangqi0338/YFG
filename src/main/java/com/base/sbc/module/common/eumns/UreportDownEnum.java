package com.base.sbc.module.common.eumns;

/**
 * 类描述：Ureport 下载类型
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.eumns.EumnUreportDown
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-17 11:16
 */
public enum UreportDownEnum {

    PDF("pdf", "http://{url}:{port}/ureport/{downType}?_u=mysql:{mysql}-{name}.ureport.xml&billType={billType}&");

    /**
     * 下载类型
     */
    private final String downType;
    /**
     * 下载url 模板地址
     */
    private final String urlTemplate;

    UreportDownEnum(String downType, String urlTemplate) {
        this.downType = downType;
        this.urlTemplate = urlTemplate;
    }

    public String getDownType() {
        return downType;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }
}
