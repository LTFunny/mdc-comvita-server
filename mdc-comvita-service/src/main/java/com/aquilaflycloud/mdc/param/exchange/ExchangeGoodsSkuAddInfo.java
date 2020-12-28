package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ExchangeGoodsSkuAddInfo
 *
 * @author zengqingjie
 * @date 2020-07-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeGoodsSkuAddInfo extends AuthParam implements Serializable {
    @ApiModelProperty(value = "规格ids", required = true)
    @NotBlank(message = "规格ids不能为空")
    private String specIds;

    @ApiModelProperty(value = "规格值ids", required = true)
    @NotBlank(message = "规格值ids不能为空")
    private String specValueIds;

    @ApiModelProperty(value = "配置json串", required = true)
    @NotBlank(message = "配置json串不能为空")
    private String configureJson;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "单件奖励值")
    @Min(value = 0, message = "单件奖励值不能小于0")
    private Integer singleReward;

    @ApiModelProperty(value = "单件金额")
    @DecimalMin(value = "0", message = "单件金额不能小于0")
    private BigDecimal singlePrice = BigDecimal.ZERO;

    @ApiModelProperty(value = "库存", required = true)
    @NotNull(message = "库存不能为空")
    @Min(value = 1, message = "库存不能小于1")
    private Integer inventory;

    @ApiModelProperty(value = "自编码")
    private String selfCode;

}
