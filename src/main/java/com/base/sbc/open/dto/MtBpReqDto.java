package com.base.sbc.open.dto;

import lombok.Data;

import java.util.List;
/**
 * @author 卞康
 * @date 2023/5/18 14:51:40
 * @mail 247967116@qq.com
 */
@Data
public class MtBpReqDto {
    private GeneralData generalData;
    private Purchasing purchasing;
    private Identification identification;
    private AddressInfo addressInfo;
    private BankDetails bankDetails;
    private List<BankInfos> bankInfos;
    private Contacts contacts;

    @Data
    public static class Contacts {

        protected String contact;

        protected String jobTitle;

    }
    @Data
    public static class GeneralData {
        /** 供应商编码 */
        private String partner;
        /** 供应商名称 */
        private String bpName;
        /** 简称 */
        private String searchTerm1;
        /** 个体商户=1 */
        private String partnerCategory;
        /** 供应商分组 */
        private String partnerGroup;
        /** 供应商类型 */
        private String partnerType;
    }

    @Data
    public static class Purchasing {
        /** 订单货币 */
        private String orderCurrency;
    }

    @Data
    public static class Identification {
        /** 统一社会信用代码 */
        private String identificationNumber;
    }

    @Data
    public static class AddressInfo {
        /** 国家编码 */
        private String country;
        /** 城市 */
        private String city;
    }

    @Data
    public static class BankDetails {
        /** 对公账户 */
        private String bankAcct;
        /** 对公开户行 */
        private String bankAccountName;
        /** 对公账户名 */
        private String accountHolder;
        /** 联行号 */
        private String bankKey;
    }

    @Data
    public static class BankInfos {
        /** 明细行id */
        private String bankDetailId;
        /** 国家编码 */
        private String bankCtry;
        /** 联行号 */
        private String bankKey;
        /** 对公账户 */
        private String bankAcctFull;
        /** 对公账户名 */
        private String accountHolder;
        /** 对公开户行 */
        private String bankAccountName;
        /** 对公账户 */
        private String bankAcct;
        /** 银行参考 */
        private String bankRef;
    }
}
