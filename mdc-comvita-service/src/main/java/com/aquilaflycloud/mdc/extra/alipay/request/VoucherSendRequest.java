package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VoucherSendRequest
 *
 * @author star
 * @date 2020-06-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherSendRequest extends AlipayBaseRequest {
    /**
     * 券金额。浮点数，格式为#.00，单位是元。红包发放时填写，其它情形不能填
     */
    private String amount;

    /**
     * 扩展参数，当前仅允许传入的key值为"alipayMiniAppToken"
     */
    private String extendInfo;

    /**
     * 支付宝登录ID，手机或邮箱 。user_id, login_id, taobao_nick不能同时为空，优先级依次降低
     */
    private String loginId;

    /**
     * 发券备注
     */
    private String memo;

    /**
     * 外部业务订单号，用于幂等控制，相同template_id和out_biz_no认为是同一次业务
     */
    private String outBizNo;

    /**
     * 淘宝昵称 。user_id, login_id, taobao_nick不能同时为空，优先级依次降低
     */
    private String taobaoNick;

    /**
     * 券模板ID
     */
    private String templateId;

    /**
     * 支付宝用户ID 。user_id, login_id, taobao_nick不能同时为空，优先级依次降低
     */
    private String userId;
}
