package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * CouponRelUseParam
 *
 * @author star
 * @date 2020-02-11
 */
@Data
@Accessors(chain = true)
public class CouponRelUseParam {
    @ApiModelProperty(value = "会员停车券记录id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;
}
