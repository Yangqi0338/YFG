package com.base.sbc.open.entity;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * @author 卞康
 * @date 2023/5/25 15:30:56
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_smp_supplier")
public class MtBqReqEntity extends BaseDataEntity<String> {
    /** 联系人 */
    private String contact;
    /** 联系人职位 */
    private String jobTitle;
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
    /** 订单货币 */
    private String orderCurrency;
    /** 统一社会信用代码 */
    private String identificationNumber;
    /** 国家编码 */
    private String country;
    /** 城市 */
    private String city;
    /** 对公账户 */
    private String bankAcct;
    /** 对公开户行 */
    private String bankAccountName;
    /** 对公账户名 */
    private String accountHolder;
    /** 联行号 */
    private String bankKey;
    /** 联行信息集合 */
    private String bankInfos;
    /** 手机 */
    private String cellphone;
    /** 邮箱 */
    private String email;
    /** 街道 */
    private String street;
    /** 电话 */
    private String telephone;


    public BasicsdatumSupplier toBasicsdatumSupplier(){
        BasicsdatumSupplier basicsdatumSupplier =new BasicsdatumSupplier();
        BeanUtil.copyProperties(this,basicsdatumSupplier);
        basicsdatumSupplier.setSupplierCode(partner);
        basicsdatumSupplier.setSupplierType(partnerType);
        basicsdatumSupplier.setCreditCode(identificationNumber);
        basicsdatumSupplier.setSupplier(bpName);
        basicsdatumSupplier.setSupplierAbbreviation(searchTerm1);
        basicsdatumSupplier.setProvinceCity(city);
        basicsdatumSupplier.setPublicBank(bankAccountName);
        basicsdatumSupplier.setPublicAccount(bankAcct);
        JSONArray jsonArray = JSON.parseArray(bankInfos);
        if (!ObjectUtils.isEmpty(jsonArray) && jsonArray.size()>0){
            Object o = jsonArray.get(0);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONBytes(o));
            BeanUtil.copyProperties(jsonObject,basicsdatumSupplier);
        }
        return basicsdatumSupplier;
    }
}
