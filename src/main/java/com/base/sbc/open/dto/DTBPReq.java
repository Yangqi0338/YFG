package com.base.sbc.open.dto;

import lombok.Data;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

@Data
public class DTBPReq {

    protected GeneralData generalData;
    protected Purchasing purchasing;
    protected Identification identification;
    protected AddressInfo addressInfo;
    protected Contacts contacts;
    protected BankDetails bankDetails;
    protected List<DTBPBankDetail> bankInfos;


    @Data
    public static class AddressInfo {

        @XmlElement(name = "COUNTRY")
        protected String country;
        @XmlElement(name = "CITY")
        protected String city;
        @XmlElement(name = "STREET")
        protected String street;
        @XmlElement(name = "TELEPHONE")
        protected String telephone;
        @XmlElement(name = "CELLPHONE")
        protected String cellphone;
        @XmlElement(name = "E_MAIL")
        protected String email;
    }

    @Data
    public static class BankDetails {

        protected String bankacct;

        protected String bankaccountname;

        protected String accountholder;

        protected String bankkey;

    }

    @Data
    public static class Contacts {

        protected String contact;

        protected String jobTitle;

    }

    @Data
    public static class GeneralData {


        protected String partner;

        protected String bpname;

        protected String centralblock;

        protected String searchterm1;

        protected String partnercategory;

        protected String partnergroup;

        protected String partnertype;
    }

    @Data
    public static class Identification {

        protected String identificationnumber;
    }

    @Data
    public static class Purchasing {

        protected String orderCurrency;
    }
}
