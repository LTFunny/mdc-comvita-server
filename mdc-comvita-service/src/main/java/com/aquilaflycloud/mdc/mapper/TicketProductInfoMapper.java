package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketProductInfo;
import com.aquilaflycloud.mdc.param.ticket.ProductInfoSalesOrderParam;
import com.aquilaflycloud.mdc.result.ticket.ProductInfoSaleOrderSumResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品信息Mapper 接口
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
public interface TicketProductInfoMapper extends AfcBaseMapper<TicketProductInfo> {
    /**
     *
     * @param ticketProductInfoList
     * @return
     */
    int updateBatchByIds(@Param("ticketProductInfoList") List<TicketProductInfo> ticketProductInfoList);

    /**
     * 获取订单分页的产品售卖分析
     * @param page
     * @param param
     * @return
     */
    IPage<ProductInfoSaleOrderSumResult> getSalesProductOrderInfo(IPage<TicketProductInfo> page, ProductInfoSalesOrderParam param, String authSql);
}
