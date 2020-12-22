package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.aquilaflycloud.mdc.enums.ticket.OrderInfoStatusEnum;
import com.aquilaflycloud.mdc.mapper.TicketCallbackFunctionRecordMapper;
import com.aquilaflycloud.mdc.mapper.TicketOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.TicketVerificateInfoMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketCallbackFunctionRecord;
import com.aquilaflycloud.mdc.extra.docom.notify.TicketCallbackOrderInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketVerificateInfo;
import com.aquilaflycloud.mdc.service.TicketCallbackFunctionRecordService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 回调函数记录服务实现类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@Service
@Slf4j
public class TicketCallbackFunctionRecordServiceImpl implements TicketCallbackFunctionRecordService {
    @Resource
    private TicketCallbackFunctionRecordMapper ticketCallbackFunctionRecordMapper;

    @Resource
    private TicketVerificateInfoMapper ticketVerificateInfoMapper;

    @Resource
    private TicketOrderInfoMapper ticketOrderInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)//设置抛出任何异常都回滚
    public JSONObject ticketOrderCallbackHandler(String orderNo, String otaOrderNo, String orderInfoStr) {
        JSONObject result = new JSONObject();
        String isTrue = "true";
        int resultCode = 200;
        String resultMsg = "执行成功";

        log.info("orderNo:" + orderNo + "; otaOrderNo:" + otaOrderNo + "; orderInfoStr" + orderInfoStr);
        try {
            //将字符串转为json对象
            JSONObject orderJson = new JSONObject(orderInfoStr);

            TicketCallbackOrderInfo orderInfo = new TicketCallbackOrderInfo();
            BeanUtil.copyProperties(orderJson, orderInfo);

            System.out.println(orderInfo.toString());

            //判断入参是否为空
            if (StrUtil.isEmpty(orderNo) || StrUtil.isEmpty(otaOrderNo) || ObjectUtil.isNull(orderInfoStr)
                    || StrUtil.isEmpty(orderInfo.getMerchantCode()) || StrUtil.isEmpty(orderInfo.getMerchantName())
                    || StrUtil.isEmpty(orderInfo.getOrderNo()) || StrUtil.isEmpty(orderInfo.getOutOrderNo())
                    || ObjectUtil.isNull(orderInfo.getProductID()) || ObjectUtil.isNull(orderInfo.getSettleFee())
                    || ObjectUtil.isNull(orderInfo.getSellingFee()) || ObjectUtil.isNull(orderInfo.getOrderQty())
                    || StrUtil.isEmpty(orderInfo.getCode())
                    || ObjectUtil.isNull(orderInfo.getCodeQty()) || ObjectUtil.isNull(orderInfo.getUseQty())
                    || ObjectUtil.isNull(orderInfo.getRefundQty()) || ObjectUtil.isNull(orderInfo.getRefundCnt())
                    || ObjectUtil.isNull(orderInfo.getStartDate()) || ObjectUtil.isNull(orderInfo.getEndDate())
                    || ObjectUtil.isNull(orderInfo.getStatus()) || StrUtil.isBlank(orderInfo.getMobile())
                    || StrUtil.isEmpty(orderInfo.getIDCardName())) {

                throw new ServiceException("入参不能为空");
            }

            //保存回调记录
            if ("true".equals(isTrue)) {
                TicketCallbackFunctionRecord ticketCallbackFunctionRecord = new TicketCallbackFunctionRecord(orderNo, otaOrderNo, orderInfo.getMerchantCode(),
                        orderInfo.getMerchantName(), orderInfo.getOrderNo(), orderInfo.getOutOrderNo(),
                        orderInfo.getProductID(), orderInfo.getSettleFee(), orderInfo.getSellingFee(), orderInfo.getOrderQty(), orderInfo.getCode(),
                        orderInfo.getCodeQty(), orderInfo.getUseQty(), orderInfo.getRefundQty(), orderInfo.getRefundCnt(), parseDate(orderInfo.getStartDate()),
                        parseDate(orderInfo.getEndDate()), EnumUtil.likeValueOf(OrderInfoStatusEnum.class, orderInfo.getStatus()),
                        orderInfo.getMobile(), orderInfo.getIDCardName());

                int count = ticketCallbackFunctionRecordMapper.normalInsert(ticketCallbackFunctionRecord);

                if (count <= 0) {
                    throw new ServiceException("保存回调记录失败");
                }
            } else {
                throw new ServiceException("保存回调记录失败");
            }

            //获取数据库中对应订单的数据
            TicketOrderInfo dbOrderInfo = null;
            if ("true".equals(isTrue)) {
                List<TicketOrderInfo> orderInfoList = ticketOrderInfoMapper.normalSelectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                        .eq(TicketOrderInfo::getMerchantCode, orderInfo.getMerchantCode())
                        .eq(TicketOrderInfo::getOrderNo, orderInfo.getOrderNo())
                        .eq(TicketOrderInfo::getOutOrderNo, orderInfo.getOutOrderNo())
                        .eq(TicketOrderInfo::getProductId, orderInfo.getProductID())
                );

                //判断订单是否为一条记录
                if (ObjectUtil.isNull(orderInfoList) || orderInfoList.size() != 1) {
                    throw new ServiceException("保存回调记录失败");
                } else {
                    dbOrderInfo = orderInfoList.get(0);
                }
            } else {
                throw new ServiceException("保存回调记录失败");
            }


            //更新订单状态
            if ("true".equals(isTrue)) {

                TicketOrderInfo ticketOrderInfo = new TicketOrderInfo(orderInfo.getMerchantCode(), orderInfo.getMerchantName(), orderInfo.getOrderNo(), orderInfo.getOutOrderNo(),
                        orderInfo.getProductID(), orderInfo.getSettleFee(), orderInfo.getSellingFee(), orderInfo.getOrderQty(), orderInfo.getCode(),
                        orderInfo.getCodeQty(), orderInfo.getUseQty(), orderInfo.getRefundQty(), orderInfo.getRefundCnt(), parseDate(orderInfo.getStartDate()),
                        parseDate(orderInfo.getEndDate()), EnumUtil.likeValueOf(OrderInfoStatusEnum.class, orderInfo.getStatus()),
                        orderInfo.getMobile(), orderInfo.getIDCardName());
                ticketOrderInfo.setAmount(ticketOrderInfo.getSellingFee().multiply(new BigDecimal(ticketOrderInfo.getOrderQty())));

                //比较更新待更新订单与数据库保存订单信息
                //数据库已核销数量
                int dbUseQty = dbOrderInfo.getUseQty().intValue();
                //回调数据已核销数量
                int useQty = ticketOrderInfo.getUseQty().intValue();
                //数据库已退款数量
                int dbRefundQty = dbOrderInfo.getRefundQty().intValue();
                //回调数据已退款数量
                int refundQty = ticketOrderInfo.getRefundQty().intValue();

                if ((useQty > dbUseQty) && (refundQty == dbRefundQty)) {
                    TicketVerificateInfo ticketVerificateInfo = new TicketVerificateInfo();
                    //将数据库相关信息设置到核销对象
                    BeanUtil.copyProperties(dbOrderInfo, ticketVerificateInfo);
                    //将最新的回调接口数据设置到核销对象，并设置Null值不设置进去，防止遗失租户id等信息
                    BeanUtil.copyProperties(ticketOrderInfo, ticketVerificateInfo, new CopyOptions().setIgnoreNullValue(true));

                    //生成新的id，避免复制时同一个
                    Long id = IdWorker.getId();
                    ticketVerificateInfo.setId(id);
                    //设置核销信息
                    ticketVerificateInfo.setVerificateNum(useQty - dbUseQty);
                    ticketVerificateInfo.setVerificateTimes(useQty - dbUseQty);
                    ticketVerificateInfo.setCreateTime(DateUtil.date());
                    ticketVerificateInfo.setDesignateOrgIds(dbOrderInfo.getDesignateOrgIds());
                    ticketVerificateInfo.setCreatorOrgIds(dbOrderInfo.getCreatorOrgIds());
                    //已核销数量增大&&退款数量不变-则保存到核销记录表中
                    int count = ticketVerificateInfoMapper.normalInsert(ticketVerificateInfo);

                    if (count <= 0) {
                        throw new ServiceException("保存核销记录失败");
                    }
                }

                if ("true".equals(isTrue)) {
                    int count = ticketOrderInfoMapper.normalUpdate(ticketOrderInfo, Wrappers.<TicketOrderInfo>lambdaQuery()
                            .eq(StrUtil.isNotEmpty(ticketOrderInfo.getMerchantCode()), TicketOrderInfo::getMerchantCode, ticketOrderInfo.getMerchantCode())
                            .eq(StrUtil.isNotEmpty(ticketOrderInfo.getOrderNo()), TicketOrderInfo::getOrderNo, ticketOrderInfo.getOrderNo())
                            .eq(StrUtil.isNotEmpty(ticketOrderInfo.getOutOrderNo()), TicketOrderInfo::getOutOrderNo, ticketOrderInfo.getOutOrderNo())
                            .eq(ObjectUtil.isNotNull(ticketOrderInfo.getProductId()), TicketOrderInfo::getProductId, ticketOrderInfo.getProductId())
                            .eq(StrUtil.isNotEmpty(ticketOrderInfo.getEcode()), TicketOrderInfo::getEcode, ticketOrderInfo.getEcode()));
                    if (count <= 0) {
                        throw new ServiceException("更新订单状态失败");
                    }
                } else {
                    throw new ServiceException("更新订单状态失败");
                }

            } else {
                throw new ServiceException("更新订单状态失败");
            }
        } catch (Exception e) {
            log.error("回调函数调用失败", e);
            result.set("IsTrue", "false");
            result.set("ResultCode", 205);
            result.set("ResultMsg", e.getMessage());

            return result;

        }

        result.set("IsTrue", isTrue);
        result.set("ResultCode", resultCode);
        result.set("ResultMsg", resultMsg);

        return result;
    }

    private static Date parseDate(String date) {
        if (date.contains("T")) {
            return DateUtil.parse(date, "yyyy-MM-dd'T'HH:mm:ss");
        } else {
            String replace = date.replace("/Date(", "").replace(")/", "");
            return DateUtil.date(Long.valueOf(replace));
        }
    }
}
