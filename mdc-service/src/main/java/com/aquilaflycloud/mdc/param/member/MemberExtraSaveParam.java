package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MemberExtraSaveParam {
    @ApiModelProperty(value = "是否开通微信智慧商圈")
    private WhetherEnum wechatBusiness;

    @ApiModelProperty(value = "是否开通支付宝会员卡")
    private WhetherEnum alipayCard;
}
