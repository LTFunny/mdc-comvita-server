package com.aquilaflycloud.mdc.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.mapper.SqlInjectorMapper;
import com.aquilaflycloud.util.SpringUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * InventoryEarlyWarningJob
 * 对剩余库存数进行校验,对不上的进行预警并根据参数进行修改
 *
 * @author star
 * @date 2020-05-12
 */
@Slf4j
@Component
public class InventoryEarlyWarningJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job InventoryEarlyWarningJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        String param = context.getJobParameters();
        log.info("jobParameters: {}", param);
        try {
            SqlInjectorMapper sqlInjectorMapper = SpringUtil.getBean(SqlInjectorMapper.class);
            String parkingCouponSql = "select a.coupon_id, a.coupon_code, a.coupon_name, sum(case when a.inventory_type = 1 then a.coupon_total_worth else a.coupon_count end) actual_distribute, sum(case when a.inventory_type = 1 then a.coupon_total_worth else a.coupon_count end) - b.distribute difference, b.distribute, b.inventory - b.distribute surplus_inventory, b.inventory from parking_coupon_member_rel a left join parking_coupon b on a.coupon_id = b.id group by a.coupon_id having difference != 0";
            List<Map<String, Object>> list = sqlInjectorMapper.normalSelectDataList(parkingCouponSql);
            if (CollUtil.isNotEmpty(list)) {
                dingTalkHandler("parkingCoupon", list, param);
                //是否更改记录库存数
                if (StrUtil.equals("true", param)) {
                    String updateSql = "update parking_coupon, ( select coupon_id, sum(case when inventory_type = 1 then coupon_total_worth else coupon_count end) actual_distribute from parking_coupon_member_rel group by coupon_id ) a set distribute = a.actual_distribute where id = a.coupon_id";
                    sqlInjectorMapper.normalUpdateData(updateSql);
                }
            }
            String couponSql = "select a.coupon_id, a.coupon_code, a.coupon_name, count( 1 ) actual_receive_count, count( 1 ) - b.receive_count difference, b.receive_count, b.inventory - b.receive_count surplus_inventory, b.inventory from coupon_member_rel a left join coupon_info b on a.coupon_id = b.id group by a.coupon_id having difference != 0";
            list = sqlInjectorMapper.normalSelectDataList(couponSql);
            if (CollUtil.isNotEmpty(list)) {
                dingTalkHandler("coupon", list, param);
                //是否更改记录库存数
                if (StrUtil.equals("true", param)) {
                    String updateSql = "update coupon_info, ( select coupon_id, count( 1 ) actual_receive_count from coupon_member_rel group by coupon_id ) a set receive_count = a.actual_receive_count where id = a.coupon_id";
                    sqlInjectorMapper.normalUpdateData(updateSql);
                }
            }
            //除实物外的订单操作
            String exchangeSql = "select a.goods_id, b.goods_code, a.goods_name, sum( a.goods_count ) actual_exchange_count, sum( a.goods_count ) - b.exchange_count difference, b.exchange_count, b.inventory - b.exchange_count surplus_inventory, b.inventory from exchange_order a left join exchange_goods b on a.goods_id = b.id where a.goods_type!='1' AND a.exchange_time is not null group by a.goods_id having difference != 0";
            list = sqlInjectorMapper.normalSelectDataList(exchangeSql);
            if (CollUtil.isNotEmpty(list)) {
                dingTalkHandler("exchange", list, param);
                //是否更改记录库存数
                if (StrUtil.equals("true", param)) {
                    String updateSql = "update exchange_goods, ( select goods_id, goods_type, sum( goods_count ) actual_exchange_count from exchange_order where exchange_time is not null group by goods_id ) a set exchange_count = a.actual_exchange_count where id = a.goods_id AND a.goods_type!='1'";
                    sqlInjectorMapper.normalUpdateData(updateSql);
                }
            }
            //实物的订单操作
            String exchangePhysicalSql = "SELECT a.goods_id, a.sku_id, a.goods_name, sum( a.goods_count ) actual_exchange_count, sum( a.goods_count ) - b.exchange_count difference, b.exchange_count, b.inventory - b.exchange_count surplus_inventory, b.inventory FROM exchange_order a LEFT JOIN exchange_goods_sku_info b ON a.goods_id = b.goods_id AND a.sku_id = b.id WHERE a.exchange_time IS NOT NULL AND a.goods_type='1' GROUP BY a.goods_id, a.sku_id HAVING difference != 0";
            list = sqlInjectorMapper.normalSelectDataList(exchangePhysicalSql);
            if (CollUtil.isNotEmpty(list)) {
                dingTalkHandler("exchangePhysical", list, param);
                //是否更改记录库存数
                if (StrUtil.equals("true", param)) {
                    String updateSql = "UPDATE exchange_goods_sku_info b, ( SELECT goods_id, sku_id,goods_type, sum( goods_count ) actual_exchange_count FROM exchange_order WHERE exchange_time IS NOT NULL GROUP BY goods_id, sku_id ) a SET exchange_count = a.actual_exchange_count WHERE b.id = a.sku_id AND b.goods_id=a.goods_id";
                    sqlInjectorMapper.normalUpdateData(updateSql);
                }
            }

            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job InventoryEarlyWarningJob Error...");
        }
        log.info("Job InventoryEarlyWarningJob End...");
        return processResult;
    }

    private static void dingTalkHandler(String type, List<Map<String, Object>> recordList, String param) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=8ebcc41c29497e9e59d5e4cf249714798b506c0a6c3c26cafdb4d4d3344e7bd4");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        StrBuilder result = StrBuilder.create();
        switch (type) {
            case "parkingCoupon": {
                result = result.append("停车券库存校验:\n");
                for (Map<String, Object> map : recordList) {
                    result.append("停车券编码:").append(map.get("coupon_code")).append(", 实际派发数:").append(map.get("actual_distribute"))
                            .append(", 记录派发数:").append(map.get("distribute")).append(", 差额:").append(map.get("difference")).append("\n");
                }
                break;
            }
            case "coupon": {
                result = result.append("优惠券库存校验:\n");
                for (Map<String, Object> map : recordList) {
                    result.append("优惠券编码:").append(map.get("coupon_code")).append(", 实际领取数:").append(map.get("actual_receive_count"))
                            .append(", 记录领取数:").append(map.get("receive_count")).append(", 差额:").append(map.get("difference")).append("\n");
                }
                break;
            }
            case "exchange": {
                result = result.append("兑换商品库存校验:\n");
                for (Map<String, Object> map : recordList) {
                    result.append("商品编码:").append(map.get("goods_code")).append(", 实际兑换数:").append(map.get("actual_exchange_count"))
                            .append(", 记录兑换数:").append(map.get("exchange_count")).append(", 差额:").append(map.get("difference")).append("\n");
                }
                break;
            }
            //实物商品
            case "exchangePhysical": {
                result = result.append("兑换商品库存校验:\n");
                for (Map<String, Object> map : recordList) {
                    result.append("商品id:").append(map.get("goods_id")).append(", 商品skuId:").append(map.get("sku_id")).append(", 实际兑换数:").append(map.get("actual_exchange_count"))
                            .append(", 记录兑换数:").append(map.get("exchange_count")).append(", 差额:").append(map.get("difference")).append("\n");
                }
                break;
            }
            default:
        }
        if (StrUtil.equals("true", param)) {
            result.append("已修复异常并更新已派发数");
        }
        text.setContent(result.toString());
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList("13650989413", "18316023928"));
        request.setAt(at);
        try {
            OapiRobotSendResponse response = client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
