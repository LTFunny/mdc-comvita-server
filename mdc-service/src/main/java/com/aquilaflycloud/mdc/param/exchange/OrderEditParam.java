package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.exchange.RefundTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * OrderEditParam
 *
 * @author star
 * @date 2020-05-20
 */
@Data
@Accessors(chain = true)
public class OrderEditParam implements Serializable {
    @ApiModelProperty(value = "订单id列表", required = true)
    @NotEmpty(message = "订单id列表不能为空")
    private List<Long> idList;

    @ApiModelProperty(value = "退款类型")
    private RefundTypeEnum refundType;

    @ApiModelProperty(value = "是否过期自动退款")
    private WhetherEnum refundExpired;
}
