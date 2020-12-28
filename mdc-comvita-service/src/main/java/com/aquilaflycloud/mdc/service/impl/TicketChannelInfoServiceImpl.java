package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoPageTypeEnum;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoStateEnum;
import com.aquilaflycloud.mdc.enums.ticket.OrderInfoStatusEnum;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.mapper.TicketChannelInfoMapper;
import com.aquilaflycloud.mdc.mapper.TicketOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.TicketOrdernoOtaordernoRelationMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketChannelInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import com.aquilaflycloud.mdc.param.recommendation.RecommendationGetParam;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.param.wechat.MiniProgramQrCodeUnLimitGetParam;
import com.aquilaflycloud.mdc.result.recommendation.RecommendationResult;
import com.aquilaflycloud.mdc.result.ticket.*;
import com.aquilaflycloud.mdc.service.RecommendationService;
import com.aquilaflycloud.mdc.service.TicketChannelInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 渠道信息服务实现类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@Service
public class TicketChannelInfoServiceImpl implements TicketChannelInfoService {
    @Resource
    private WechatOpenPlatformService wechatOpenPlatformService;
    @Resource
    private TicketChannelInfoMapper ticketChannelInfoMapper;
    @Resource
    private TicketOrderInfoMapper ticketOrderInfoMapper;
    @Resource
    private RecommendationService recommendationService;

    @Override
    public TicketChannelInfo addChannelInfo(ChannelInfoAddParam param) {
        //校验渠道名称不能重复
        List<TicketChannelInfo> ticketChannelInfos = ticketChannelInfoMapper.selectList(Wrappers.<TicketChannelInfo>lambdaQuery()
                .eq(TicketChannelInfo::getName, param.getName())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (ObjectUtil.isNotNull(ticketChannelInfos) && ticketChannelInfos.size() > 0) {
            throw new ServiceException("渠道名称不能重复，请重新输入");
        }

        TicketChannelInfo ticketChannelInfo = new TicketChannelInfo();
        BeanUtil.copyProperties(param, ticketChannelInfo);
        Long id = MdcUtil.getSnowflakeId();
        ticketChannelInfo.setId(id);
        ticketChannelInfo.setState(ChannelInfoStateEnum.NORMAL);

        StrBuilder scene = StrBuilder.create().append("id=").append(id);
        String pagePath = "";
        if (param.getShowPageType() == ChannelInfoPageTypeEnum.SCENICSPOT) {
            pagePath = param.getShowPage().getPath();
            switch (param.getShowPage()) {
                case OCEAN: {
                    scene.append("&t=").append(ScenicSpotTypeEnum.OCEAN.getType());
                    break;
                }
                case RAINFOREST: {
                    scene.append("&t=").append(ScenicSpotTypeEnum.MUSEUM.getType());
                    break;
                }
                case MUSEUM: {
                    scene.append("&t=").append(ScenicSpotTypeEnum.RAINFOREST.getType());
                    break;
                }
                default:
            }
        } else if (param.getShowPageType() == ChannelInfoPageTypeEnum.RECOMMENDATION) {
            pagePath = "pages/recommend/recommend";
        }
        String miniCode = wechatOpenPlatformService.miniCodeUnLimitGet(
                new MiniProgramQrCodeUnLimitGetParam().setAppId(param.getAppId()).setScene(scene.toString()).setPagePath(pagePath));
        ticketChannelInfo.setSmallQrCodeUrl(miniCode);
        int count = ticketChannelInfoMapper.insert(ticketChannelInfo);
        if (count <= 0) {
            throw new ServiceException("保存渠道失败");
        }
        return ticketChannelInfo;
    }

    @Override
    public IPage<TicketChannelInfo> pageChannelInfo(ChannelInfoPageParam param) {
        return ticketChannelInfoMapper.selectPage(param.page(), Wrappers.<TicketChannelInfo>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), TicketChannelInfo::getName, param.getName())
                .like(StrUtil.isNotBlank(param.getResponsiblePerson()), TicketChannelInfo::getResponsiblePerson, param.getResponsiblePerson())
                .like(StrUtil.isNotBlank(param.getContactNumber()), TicketChannelInfo::getContactNumber, param.getContactNumber())
                .eq(param.getState() != null, TicketChannelInfo::getState, param.getState())
                .ge(param.getStartCreateTime() != null, TicketChannelInfo::getCreateTime, param.getStartCreateTime())
                .le(param.getEndCreateTime() != null, TicketChannelInfo::getCreateTime, param.getEndCreateTime())
                .orderByDesc(TicketChannelInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams()));
    }

    @Override
    public int updateChannelInfo(ChannelInfoUpdateParam param) {
        TicketChannelInfo ticketChannelInfo = new TicketChannelInfo();
        BeanUtil.copyProperties(param, ticketChannelInfo);

        return ticketChannelInfoMapper.updateById(ticketChannelInfo);
    }

    @Override
    public TicketChannelSaleResult channelSales(ChannelSalesParam param) {
        try {

            List<TicketChannelInfo> ticketChannelInfos = ticketChannelInfoMapper.selectList(Wrappers.<TicketChannelInfo>lambdaQuery()
                    .orderByDesc(TicketChannelInfo::getCreateTime)
                    .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
            );

            List<Long> channelIds = new ArrayList<>();
            if (ObjectUtil.isNotNull(ticketChannelInfos) && ticketChannelInfos.size() > 0) {
                channelIds = ticketChannelInfos.stream().map(TicketChannelInfo::getId).collect(toList());
            }

            //获取有渠道id的订单详情
            List<TicketOrderInfo> orderInfoList = new ArrayList<>();

            if (ObjectUtil.isNotNull(ticketChannelInfos) && ticketChannelInfos.size() > 0) {
                orderInfoList = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                        .isNotNull(TicketOrderInfo::getChannelId)
                        .notIn(TicketOrderInfo::getStatus, OrderInfoStatusEnum.UN_PAID, OrderInfoStatusEnum.INVALID)
                        .ge(ObjectUtil.isNotNull(param.getStartDate()), TicketOrderInfo::getCreateTime, param.getStartDate())
                        .le(ObjectUtil.isNotNull(param.getEndDate()), TicketOrderInfo::getCreateTime, param.getEndDate())
                        .in(TicketOrderInfo::getChannelId, channelIds)
                );
            }

            //获取全部渠道订单总数
            Long allOrderCount = new Long(orderInfoList.stream().collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size());
            //获取全部渠道销售金额
            BigDecimal allAmount = orderInfoList.stream().map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            //渠道销售金额
            BigDecimal channelAmount = new BigDecimal(0);
            //渠道订单数
            Long channelOrderCount = 0L;
            //购票人数
            Long channelPersonNumber = 0L;
            //人均消费金额
            double channelAverage = 0.00;

            if (ObjectUtil.isNull(param.getId())) {
                //渠道id为空，则设置为全部渠道的和
                channelOrderCount = allOrderCount;
                channelAmount = allAmount;
                channelPersonNumber = new Long(orderInfoList.stream().collect(Collectors.groupingBy(TicketOrderInfo::getMemberId)).size());
                if (channelPersonNumber > 0) {
                    channelAverage = channelAmount.divide(new BigDecimal(channelPersonNumber), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            } else if (ObjectUtil.isNotNull(param.getId())) {
                //渠道id为空，则设置为该渠道的和
                channelOrderCount = new Long(orderInfoList.stream().filter(item -> item.getChannelId().equals(param.getId())).collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size());
                channelAmount = orderInfoList.stream().filter(item -> item.getChannelId().equals(param.getId())).map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                channelPersonNumber = new Long(orderInfoList.stream().filter(item -> item.getChannelId().equals(param.getId())).collect(Collectors.groupingBy(TicketOrderInfo::getMemberId)).size());
                if (channelPersonNumber > 0) {
                    channelAverage = channelAmount.divide(new BigDecimal(channelPersonNumber), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }


            //获取日期列表
            List<String> days = DateUtil.rangeToList(param.getStartDate(), param.getEndDate(), DateField.DAY_OF_YEAR)
                    .stream().map(item-> DateUtil.format(item, "yyyy-MM-dd")).collect(toList());
            List<Long> orderCountList = new ArrayList<>();
            List<Double> sellAmountList = new ArrayList<>();
            //如果渠道id为空，则求和订单数和销售金额每天数据
            for (int i = 0; i < days.size(); i++) {
                String itemDay = days.get(i);
                BigDecimal sum = null;
                Long count = null;
                if (ObjectUtil.isNull(param.getId())) {
                    //获取当前日期的订单金额和
                    sum = orderInfoList.stream().filter(
                            item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                            .map(TicketOrderInfo::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    count = new Long(orderInfoList.stream().filter(
                            item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                            .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size());
                } else if (ObjectUtil.isNotNull(param.getId())) {
                    //如果渠道id不为空，则统计该渠道的订单数和销售金额数据
                    //获取当前日期的订单金额和
                    sum = orderInfoList.stream()
                            .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                            .filter(item -> ObjectUtil.equal(item.getChannelId(), param.getId()))
                            .map(TicketOrderInfo::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    count = new Long(orderInfoList.stream()
                            .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                            .filter(item -> ObjectUtil.equal(item.getChannelId(), param.getId()))
                            .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size());

                }

                if (ObjectUtil.isNull(sum)) {
                    sum = new BigDecimal(0);
                }

                if (ObjectUtil.isNull(count)) {
                    count = 0L;
                }

                orderCountList.add(count);
                sellAmountList.add(sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }



            Map<Long, List<TicketOrderInfo>> collect = orderInfoList.stream().collect(Collectors.groupingBy(item -> item.getChannelId()));
            Map<Long, Long> chnnelOrderCount = new HashMap<>();
            Map<Long, Double> chnnelOrderSum = new HashMap<>();
            //循环去重订单关联关系id
            for (Long id : collect.keySet()) {
                List<TicketOrderInfo> item = collect.get(id);
                chnnelOrderCount.put(id, new Long(item.stream().collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size()));
                chnnelOrderSum.put(id, item.stream().map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }

            List<TicketChannelPieChartResult> channelOrderCountList = new ArrayList<>();
            List<TicketChannelPieChartResult> channelSellAmountList = new ArrayList<>();
            if (ObjectUtil.isNotNull(ticketChannelInfos) && ticketChannelInfos.size() > 0) {
                for (int i = 0; i < ticketChannelInfos.size(); i++) {
                    TicketChannelInfo ticketChannelInfo = ticketChannelInfos.get(i);
                    Long id = ticketChannelInfo.getId();

                    //过滤渠道订单数为0的数据
                    Object countValue = chnnelOrderCount.get(id);
                    if (ObjectUtil.isNotNull(countValue) && (long)countValue > 0) {
                        channelOrderCountList.add(new TicketChannelPieChartResult(
                                id, ticketChannelInfo.getName(), countValue));
                    }

                    //过滤渠道订单金额为0的数据
                    Object amountValue = chnnelOrderSum.get(id);
                    if (ObjectUtil.isNotNull(amountValue) && (double)amountValue > 0) {
                        channelSellAmountList.add(new TicketChannelPieChartResult(
                                id, ticketChannelInfo.getName(), amountValue));
                    }
                }
            }
            TicketChannelSaleResult ticketChannelSaleResult = new TicketChannelSaleResult();
            ticketChannelSaleResult.setTicketChannelSaleHeaderResult(new TicketChannelSaleHeaderResult(channelAmount, channelOrderCount, channelPersonNumber, channelAverage));
            ticketChannelSaleResult.setDays(days);
            ticketChannelSaleResult.setOrderCountList(orderCountList);
            ticketChannelSaleResult.setSellAmountList(sellAmountList);
            ticketChannelSaleResult.setChannelOrderCountList(channelOrderCountList);
            ticketChannelSaleResult.setChannelSellAmountList(channelSellAmountList);

            return ticketChannelSaleResult;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("获取数据失败");
        }

    }

    @Override
    public TicketChannelInfoByIdResult getChannelInfo(ChannelInfoGetParam param) {
        TicketChannelInfoByIdResult result = new TicketChannelInfoByIdResult();
        TicketChannelInfo ticketChannelInfo = ticketChannelInfoMapper.selectById(param.getId());
        BeanUtil.copyProperties(ticketChannelInfo, result);

        if (ObjectUtil.equal(result.getShowPageType(), ChannelInfoPageTypeEnum.RECOMMENDATION) && ObjectUtil.isNotNull(result.getRefId())) {
            RecommendationGetParam recommendationGetParam = new RecommendationGetParam();
            recommendationGetParam.setId(result.getRefId());

            RecommendationResult recommendation = recommendationService.getRecommendation(recommendationGetParam);

            result.setRecommendTitle(ObjectUtil.isNotNull(recommendation)?recommendation.getTitle() : null);
        }

        return result;
    }

    @Override
    public IPage<TicketOrderInfoSalesResult> channelSalesOrderInfo(ChannelSalesOrderInfoParam param) {
        //获取渠道列表
        List<TicketChannelInfo> ticketChannelInfos = ticketChannelInfoMapper.selectList(Wrappers.<TicketChannelInfo>lambdaQuery()
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        //保存渠道信息(key=渠道id,value=渠道名称)
        Map<Long, String> ticketChannelInfoNames = new HashMap<>();
        if (ObjectUtil.isNotNull(ticketChannelInfos) || ticketChannelInfos.size() > 0) {
            for (int i = 0; i < ticketChannelInfos.size(); i++) {
                TicketChannelInfo item = ticketChannelInfos.get(i);
                ticketChannelInfoNames.put(item.getId(), item.getName());
            }
        }

        List<Long> channelIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(ticketChannelInfos) && ticketChannelInfos.size() > 0) {
            channelIds = ticketChannelInfos.stream().map(TicketChannelInfo::getId).collect(toList());
        }

        IPage<TicketOrderInfo> ticketOrderInfoIPage = new Page<>();

        if (ObjectUtil.isNotNull(ticketChannelInfos) && ticketChannelInfos.size() > 0) {
            //获取有渠道id的订单详情
            ticketOrderInfoIPage = ticketOrderInfoMapper.selectPage(param.page(), Wrappers.<TicketOrderInfo>lambdaQuery()
                    .isNotNull(TicketOrderInfo::getChannelId)
                    .eq(ObjectUtil.isNotNull(param.getId()), TicketOrderInfo::getChannelId, param.getId())
                    .in(TicketOrderInfo::getChannelId, channelIds)
                    .notIn(TicketOrderInfo::getStatus, OrderInfoStatusEnum.UN_PAID, OrderInfoStatusEnum.INVALID)
                    .ge(ObjectUtil.isNotNull(param.getStartDate()), TicketOrderInfo::getCreateTime, param.getStartDate())
                    .le(ObjectUtil.isNotNull(param.getEndDate()), TicketOrderInfo::getCreateTime, param.getEndDate())
                    .orderByDesc(TicketOrderInfo::getCreateTime)
            );
        }

        List<TicketOrderInfo> records = ticketOrderInfoIPage.getRecords();
        //封装渠道名称到集合
        List<TicketOrderInfoSalesResult> resultList = new ArrayList<>();
        if (ObjectUtil.isNotNull(records) && records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                TicketOrderInfo item = records.get(i);
                TicketOrderInfoSalesResult salesResult = new TicketOrderInfoSalesResult();
                BeanUtil.copyProperties(item, salesResult);
                salesResult.setChannelName(ticketChannelInfoNames.get(salesResult.getChannelId()));
                resultList.add(salesResult);
            }
        }

        IPage<TicketOrderInfoSalesResult> resultInfos = new Page<>(ticketOrderInfoIPage.getCurrent(), ticketOrderInfoIPage.getSize(),
                ticketOrderInfoIPage.getTotal());
        resultInfos.setRecords(resultList);

        return resultInfos;
    }

    @Override
    public List<TicketChannelInfo> listChannelInfo(ChannelInfoListParam param) {
        return ticketChannelInfoMapper.selectList(Wrappers.<TicketChannelInfo>lambdaQuery()
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }

    @Override
    public void updateChannelInfoQrCode(ChannelInfoUpdateQrCodeParam param) {
        TicketChannelInfo ticketChannelInfo = new TicketChannelInfo();
        BeanUtil.copyProperties(param, ticketChannelInfo);

        ticketChannelInfoMapper.updateById(ticketChannelInfo);
    }

    @Override
    public ChannelInfoRefIdResult getRefId(ChannelInfoRefIdParam param) {
        ChannelInfoRefIdResult result = new ChannelInfoRefIdResult();
        TicketChannelInfo ticketChannelInfo = ticketChannelInfoMapper.selectById(param.getId());
        if (ObjectUtil.isNotNull(ticketChannelInfo) && ObjectUtil.isNotNull(ticketChannelInfo.getRefId())) {
            result.setRefId(ticketChannelInfo.getRefId());
        }

        return result;
    }

}
