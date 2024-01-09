package com.base.sbc.module.purchaseorder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchaseorder.dto.OrderBasisDto;
import com.base.sbc.module.purchaseorder.entity.OrderBasis;
import com.base.sbc.module.purchaseorder.mapper.OrderBasisMapper;
import com.base.sbc.module.purchaseorder.service.OrderBasisService;
import com.base.sbc.module.purchaseorder.vo.OrderBasisVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderBasisServiceImpl extends BaseServiceImpl<OrderBasisMapper, OrderBasis> implements OrderBasisService {
    private static final Logger logger = LoggerFactory.getLogger(OrderBasisService.class);
    @Autowired
    private OrderBasisMapper orderBasisMapper;

    @Override
    public PageInfo<OrderBasisVo> queryPage(OrderBasisDto dto) {
        /*分页*/
        PageHelper.startPage(dto);
        BaseQueryWrapper<OrderBasis> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("season_id",dto.getSeasonId());
        queryWrapper.notEmptyEq("band_code",dto.getBandCode());
        queryWrapper.notEmptyEq("prod_category",dto.getProdCategory());
        queryWrapper.notEmptyLike("research_type",dto.getResearchType());
        queryWrapper.notEmptyLike("commissioning_date",dto.getCommissioningDate());
        queryWrapper.notEmptyLike("prod_category_name",dto.getProdCategoryName());
        queryWrapper.notEmptyLike("band_name",dto.getBandName());
        queryWrapper.notEmptyLike("style_no",dto.getStyleNo());
        queryWrapper.notEmptyLike("purchase_order_name",dto.getPurchaseOrderName());

        List<OrderBasisVo> orderBasisVos = this.getBaseMapper().getQueryPageList(queryWrapper);

        return new PageInfo<>(orderBasisVos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean orderBasisAdd(OrderBasisDto dto) {
        OrderBasis orderBasis = getById(dto.getSeasonId());
        if (orderBasis == null){
            throw new OtherException(BaseErrorEnum.ERR_UPDATE_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        orderBasis.setPurchaseOrderName(dto.getPurchaseOrderName());
        updateById(orderBasis);
        return true;
    }

    @Override
    public String orderBasisSave(OrderBasisDto dto) {
        logger.info("OrderBasisService#orderBasisSave 保存 dto:{}", JSON.toJSONString(dto));
        OrderBasis orderBasis = CopyUtil.copy(dto, OrderBasis.class);
        if (StringUtils.isEmpty(orderBasis.getId())) {
            orderBasis.setId(new IdGen().nextIdStr());
            orderBasis.setCompanyCode(super.getCompanyCode());
            orderBasis.insertInit();
        }else {
            orderBasis.updateInit();
        }
        super.saveOrUpdate(orderBasis);
        return orderBasis.getId();
    }

    @Override
    public String orderBasisUpdate(OrderBasisDto dto) {
        logger.info("OrderBasisService#orderBasisUpdate 保存 dto:{}", JSON.toJSONString(dto));
        OrderBasis orderBasis = CopyUtil.copy(dto, OrderBasis.class);
        if (StringUtils.isEmpty(orderBasis.getId())) {
            orderBasis.setId(new IdGen().nextIdStr());
            orderBasis.setCompanyCode(super.getCompanyCode());
            orderBasis.insertInit();
        }else {
            orderBasis.updateInit();
        }
        super.saveOrUpdate(orderBasis);
        return orderBasis.getId();
    }

    @Override
    public Boolean getCompile(OrderBasisDto dto) {
        OrderBasis orderBasis = getById(dto.getId());
        if (orderBasis == null){
            throw new OtherException(BaseErrorEnum.ERR_UPDATE_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        updateById(orderBasis);
        return true;
    }

    @Override
    public void getStartApproval(String id,String userCompany) {
        logger.info("OrderBasisService#getStartApproval 提交审核 id:{}, userCompany:{}", id, userCompany);
        OrderBasis orderBasis = super.getById(id);
        if (Objects.isNull(orderBasis)) {
            throw new BusinessException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        if (!BaseGlobal.STOCK_STATUS_DRAFT.equals(orderBasis.getStatus()) && !BaseGlobal.STOCK_STATUS_REJECT.equals(orderBasis.getStatus())) {
            throw new BusinessException(BaseErrorEnum.ERR_STATUS_DELETE);
        }
        LambdaUpdateWrapper<OrderBasis> updateWrapper = new LambdaUpdateWrapper<OrderBasis>()
                .set(OrderBasis::getStatus, BaseGlobal.STOCK_STATUS_WAIT_CHECK)
                .eq(OrderBasis::getId, id);
        super.update(updateWrapper);
    }
    // TODO 发起审批

}
