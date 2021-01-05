package com.aquilaflycloud.mdc.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.mapper.MemberInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.aquilaflycloud.util.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PreGoodsReserveMsgSendJob
 *
 * @author star
 * @date 2021/1/5
 */
@Slf4j
@Component
public class PreGoodsReserveMsgSendJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job PreGoodsReserveMsgSendJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        try {
            PreOrderGoodsMapper preOrderGoodsMapper = SpringUtil.getBean(PreOrderGoodsMapper.class);
            List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.normalSelectList(Wrappers.<PreOrderGoods>query()
                    .eq("date_format(reserve_start_time,'%Y-%m-%d')", DateUtil.beginOfDay(DateUtil.date()).offset(DateField.DAY_OF_YEAR, 1))
                    .lambda()
                    .eq(PreOrderGoods::getGoodsType, OrderGoodsTypeEnum.PREPARE)
                    .eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE)
            );
            if (CollUtil.isNotEmpty(preOrderGoodsList)) {
                MemberInfoMapper memberInfoMapper = SpringUtil.getBean(MemberInfoMapper.class);
                WechatMiniProgramSubscribeMessageService messageService = SpringUtil.getBean(WechatMiniProgramSubscribeMessageService.class);
                Map<Long, MemberInfo> memberInfoMap = memberInfoMapper.normalSelectList(Wrappers.<MemberInfo>lambdaQuery()
                        .select(MemberInfo::getId, MemberInfo::getWxAppId, MemberInfo::getOpenId)
                        .in(MemberInfo::getId, preOrderGoodsList.stream().map(PreOrderGoods::getReserveId).collect(Collectors.toList()))
                ).stream().collect(Collectors.toMap(MemberInfo::getId, memberInfo -> memberInfo));
                List<MiniMemberInfo> memberInfoList = new ArrayList<>();
                for (PreOrderGoods goods : preOrderGoodsList) {
                    memberInfoList.clear();
                    MiniMemberInfo miniMemberInfo = new MiniMemberInfo();
                    MemberInfo memberInfo = memberInfoMap.get(goods.getReserveId());
                    miniMemberInfo.setAppId(memberInfo.getWxAppId());
                    miniMemberInfo.setOpenId(memberInfo.getOpenId());
                    memberInfoList.add(miniMemberInfo);
                    String date = DateTime.of(goods.getReserveStartTime()).toString(DatePattern.CHINESE_DATE_FORMAT)
                            + "~" + DateTime.of(goods.getReserveEndTime()).toString(DatePattern.CHINESE_DATE_FORMAT);
                    messageService.sendMiniMessage(memberInfoList, MiniMessageTypeEnum.PREGOODSTAKE, null,
                            goods.getCardPsw(), goods.getReserveShop(), date, "请于" + date + "到店自提哦!");
                }
            }
            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job PreGoodsReserveMsgSendJob Error...");
        }
        log.info("Job PreGoodsReserveMsgSendJob End...");
        return processResult;
    }
}
