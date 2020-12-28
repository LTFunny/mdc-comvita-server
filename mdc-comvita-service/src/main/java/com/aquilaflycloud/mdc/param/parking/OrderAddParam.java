package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.common.NoneBlank;
import com.aquilaflycloud.mdc.enums.easypay.PayType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * OrderChargeGetParam
 *
 * @author star
 * @date 2020-02-01
 */
@NoneBlank(fieldNames = {"carNo", "cardId"}, message = "车牌号,车卡号不能同时为空")
@Data
@Accessors(chain = true)
public class OrderAddParam {
    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "扫码参数")
    private String scanParam;

    @ApiModelProperty(value = "停车券领取记录id")
    private List<Long> couponMemberRelIds;

    @ApiModelProperty(value = "支付类型(WECHAT_MINI, ALIPAY)", required = true)
    @NotNull(message = "支付类型不能为空")
    private PayType payType;
}
