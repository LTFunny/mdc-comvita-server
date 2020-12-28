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
 * ExchangeGoodsSkuEditInfo
 *
 * @author zengqingjie
 * @date 2020-07-01
 */
@Data
public class ExchangeGoodsSkuEditInfo implements Serializable {
    @ApiModelProperty(value = "规格id", required = true)
    @NotNull(message = "规格id不能为空")
    private Long id;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "单件奖励值")
    @Min(value = 0, message = "单件奖励值不能小于0")
    private Integer singleReward;

    @ApiModelProperty(value = "单件金额")
    @DecimalMin(value = "0", message = "单件金额不能小于0")
    private BigDecimal singlePrice = BigDecimal.ZERO;

    @ApiModelProperty(value = "库存")
    @Min(value = 0, message = "库存不能小于0")
    private Integer inventoryIncrease;

    @ApiModelProperty(value = "自编码")
    private String selfCode;

}
