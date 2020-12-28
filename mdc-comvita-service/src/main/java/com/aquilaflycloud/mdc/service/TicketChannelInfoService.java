package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.ticket.TicketChannelInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.ChannelInfoRefIdResult;
import com.aquilaflycloud.mdc.result.ticket.TicketChannelInfoByIdResult;
import com.aquilaflycloud.mdc.result.ticket.TicketChannelSaleResult;
import com.aquilaflycloud.mdc.result.ticket.TicketOrderInfoSalesResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * <p>
 * 渠道信息服务类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
public interface TicketChannelInfoService {

    /**
     * 添加渠道信息
     * @param param
     * @return
     */
    TicketChannelInfo addChannelInfo(ChannelInfoAddParam param);

    /**
     * 分页查询渠道信息
     * @param param
     * @return
     */
    IPage<TicketChannelInfo> pageChannelInfo(ChannelInfoPageParam param);

    int updateChannelInfo(ChannelInfoUpdateParam param);

    TicketChannelSaleResult channelSales(ChannelSalesParam param);

    /**
     * 根据渠道id获取渠道信息
     * @param param
     * @return
     */
    TicketChannelInfoByIdResult getChannelInfo(ChannelInfoGetParam param);

    IPage<TicketOrderInfoSalesResult> channelSalesOrderInfo(ChannelSalesOrderInfoParam param);

    /**
     * 渠道列表接口
     * @return
     */
    List<TicketChannelInfo> listChannelInfo(ChannelInfoListParam param);

    /**
     * 渠道更新二维码链接
     * @param param
     */
    void updateChannelInfoQrCode(ChannelInfoUpdateQrCodeParam param);

    /**
     * 获取最新推荐id
     * @param param
     */
    ChannelInfoRefIdResult getRefId(ChannelInfoRefIdParam param);
}
