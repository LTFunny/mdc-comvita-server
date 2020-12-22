package com.aquilaflycloud.mdc.param.sign;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.sign.SignStateEnum;
import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * SignPageParam
 *
 * @author star
 * @date 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SignPageParam extends PageParam<OfflineSignActivity> {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "抽奖活动名称")
    private String signName;

    @ApiModelProperty(value = "状态(sign.SignStateEnum)")
    private SignStateEnum state;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "是否查询有效状态(common.WhetherEnum)(默认否, 为是时state无效)")
    private WhetherEnum valid = WhetherEnum.NO;
}


