package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 16:51
 * @Version 1.0
 */
@Data
public class PreReservationOrderGoodsParam  {

    @ApiModelProperty(value = "提货卡号密码")
    @NotNull(message = "提货卡号密码不能为空")
    private String password;

    @ApiModelProperty(value = "预约人名称")
    private String reserveName;

    @ApiModelProperty(value = "预约人电话")
    private String reservePhone;

    @ApiModelProperty(value = "预约门店")
    private String reserveShop;

    /**
     * 预约开始时间
     */
    @ApiModelProperty(value = "预约开始时间")
    private Date reserveStartTime;

    /**
     * 预约结束时间
     */
    @ApiModelProperty(value = "预约结束时间")
    private Date reserveEndTime;
}
