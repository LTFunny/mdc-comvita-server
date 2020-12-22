package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ParkingUnlicensedPageParam
 *
 * @author star
 * @date 2020-03-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ParkingUnlicensedPageParam extends PageParam {
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    @ApiModelProperty(value = "短车卡号")
    private String shortCardId;

    @ApiModelProperty(value = "扫码进场开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "扫码进场结束时间")
    private Date createTimeEnd;
}
