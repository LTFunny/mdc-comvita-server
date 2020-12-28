package com.aquilaflycloud.mdc.extra.alipay.service;

import com.aquilaflycloud.mdc.mapper.AlipayBusinessMallTradeInfoMapper;
import com.aquilaflycloud.mdc.model.alipay.AlipayBusinessMallTradeInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AlipayBusinessMallTradeNotifyServiceImpl
 *
 * @author Zengqingjie
 * @date 202-06-02
 */
@Service
@Slf4j
public class AlipayBusinessMallTradeNotifyService {
    @Resource
    private AlipayBusinessMallTradeInfoMapper alipayBusinessMallTradeInfoMapper;

    public void saveNotifyInfo(AlipayBusinessMallTradeInfo info) {
        /*TODO 有两个方面，一是防止多次受到消费记录通知导致重复累积，二是防止自动积分与原有的用户积分方式（如上传小票或线下服务台人工积分）重复累积。
           对于同一笔交易，大B可能会收到多次消费记录通知（如用户授权给了多个appid，或大B服务不稳定导致消息重发），因此在收到通知时，务必要通过消息中的支付宝交易号（trade_no）来去重，同一笔交易（以支付宝交易号为准）至累积一次积分。*/
        List<AlipayBusinessMallTradeInfo> infos = alipayBusinessMallTradeInfoMapper.normalSelectList(Wrappers.<AlipayBusinessMallTradeInfo>lambdaQuery()
                .eq(AlipayBusinessMallTradeInfo::getTradeNo, info.getTradeNo())
        );

        if (null != infos && infos.size() > 0) {
            log.info("支付宝商圈交易成功信息订阅回调重复：{notify=" + info.toString() + "}");
            return;
        }

        int count = alipayBusinessMallTradeInfoMapper.normalInsert(info);

        if (count <= 0) {
            throw new ServiceException("支付宝商圈交易成功信息保存失败：{notifyId=" + info.getNotifyId() + "}");
        } else {
            log.info("支付宝商圈交易成功信息订阅回调保存成功：{notifyId=" + info.getNotifyId() + "}");
        }
    }
}
